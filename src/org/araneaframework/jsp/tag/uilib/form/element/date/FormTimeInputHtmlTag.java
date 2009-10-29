/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Time input form element tag.
 * 
 * @author Marko Muts
 * @jsp.tag
 *  name = "timeInput"
 *  body-content = "JSP"
 *  description = "Form time input field (custom control), represents UiLib "TimeControl"."
 */
public class FormTimeInputHtmlTag extends BaseFormDateTimeInputHtmlTag {

  protected boolean showTimeSelect = true;

  protected boolean surroundingTable = true;

  public FormTimeInputHtmlTag() {
    this.baseStyleClass = "aranea-time";
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("TimeControl");

    // Prepare
    String name = this.getFullFieldId();
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) this.controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;

    if (this.surroundingTable) {
      out.write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");
    }

    this.writeTimeInput(out, name, name, viewModel.getSimpleValue(), this.localizedLabel, timeInputSize, viewModel
        .isDisabled(), this.accessKey);

    Integer minute = null, hour = null;
    try {
      if (viewModel.getSimpleValue() != null) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(viewModel.getCurrentSimpleDateTimeFormat().parse(viewModel.getSimpleValue()));

        hour = new Integer(calendar.get(Calendar.HOUR_OF_DAY));
        minute = new Integer(calendar.get(Calendar.MINUTE));
      }
    } catch (ParseException e) {
      if (this.showTimeSelect) {
        // try to preserve the contents of selects anyway
        String strHour = ServletUtil.getRequest(getOutputData().getInputData()).getParameter(name + ".select1");
        if (strHour != null && !(strHour.trim().length() == 0)) {
          hour = Integer.valueOf(strHour.trim());
        }
        String strMinute = ServletUtil.getRequest(getOutputData().getInputData()).getParameter(name + ".select2");
        if (strMinute != null && !(strMinute.trim().length() == 0)) {
          minute = Integer.valueOf(strMinute);
        }
      }
    }

    if (this.showTimeSelect) {
      writeHourSelect(out, name, viewModel.isDisabled(), hour);
      writeMinuteSelect(out, name, viewModel.isDisabled(), minute);
    }

    if (this.surroundingTable) {
      out.write("</td></tr></table>\n");
    }

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void writeHourSelect(Writer out, String name, boolean disabled, Integer hour) throws IOException {
    writeSelect(out, name, disabled, hour, true);
  }

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
    writeSelect(out, name, disabled, minute, false);
  }

  protected void writeSelect(Writer out, String name, boolean disabled, Integer value, boolean isHour)
      throws IOException {
    TimeControl.ViewModel viewModel = (TimeControl.ViewModel) this.controlViewModel;

    // In case of read-only time control, we don't want to show select boxes.
    if (viewModel.isDisabled() && RENDER_DISABLED_READONLY.equals(this.disabledRenderMode)) {
      return;
    }

    String selectField = null;
    String precondition = null;
    String selectScript = null;

    if (isHour) {
      selectField = "select1";
      precondition = getHourSelectOnChangePrecondition(name + ".time");
      selectScript = getTimeSelectScript(name + "." + selectField, value, 24);
    } else {
      selectField = "select2";
      precondition = getMinuteSelectOnChangePrecondition(name + ".time");
      selectScript = getTimeSelectScript(name + "." + selectField, value, 60);
    }

    out.write("<select id=\"");
    out.write(name);
    out.write('.');
    out.write(selectField);
    out.write("\" name=\"");
    out.write(name);
    out.write('.');
    out.write(selectField);
    out.write("\" onchange=\"");
    out.write(fillXJSCallConstructor("Aranea.UI.fillTimeText", name, name + ".select1", name + ".select2"));
    out.write(';');

    if (!disabled && this.events) {
      out.write("if($('");
      out.write(name);
      out.write("').size==$F('");
      out.write(name);
      out.write("').length)");
      out.write(JspWidgetCallUtil.getSubmitScriptForEvent());
    }

    out.write('"');

    if (disabled) {
      out.write(" disabled=\"disabled\"");
    }

    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {
      UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, this.updateRegionNames);
      event.setEventPrecondition(precondition);
      out.write(' ');
      out.write(event.getEventAttributes().toString());
    }
    JspUtil.writeCloseStartTag(out);

    JspUtil.writeStartTag_SS(out, "script");
    out.write(selectScript);
    JspUtil.writeEndTag_SS(out, "script");

    JspUtil.writeEndTag_SS(out, "select");
  }

  /**
   * Writes out time input
   * 
  /**
   * Writes out time INPUT
   * 
   * @param out The writer of rendered output.
   * @param id The ID of the time input.
   * @param name The name of the time input.
   * @param value The value of the time input.
   * @param label The label ID of the time input.
   * @param size The size attribute of the time input.
   * @param disabled Whether the time control is currently disabled.
   * @param accessKey The access-key for the time input.
   * @throws Exception Any exception that may occur.
   */
  protected void writeTimeInput(Writer out, String id, String name, String value, String label, Long size,
      boolean disabled, String accessKey) throws Exception {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) this.controlViewModel);

    if (StringUtils.isBlank(id)) {
      id = name;
    }

    // Write input tag
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    writeBackgroundValidationAttribute(out);

    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {
      JspUtil.writeAttribute(out, "onfocus", "Aranea.UI.saveValue(this)");
      UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, this.updateRegionNames);
      event.setEventPrecondition(getTimeInputOnChangePrecondition(name));
      out.write(" ");
      out.write(event.getEventAttributes().toString());
    }

    // validation won't occur with Event.observe registered in aranea-behaviour when date selected from calendar
    if (!viewModel.isOnChangeEventRegistered() && !disabled && this.backgroundValidation) {
      JspUtil.writeAttribute(out, "onchange", "formElementValidationActionCall(this)");
    }

    StringBuffer onBlur = new StringBuffer();
    if (this.showTimeSelect) {
      onBlur.append(fillXJSCallConstructor("Aranea.UI.fillTimeSelect", name, name + ".select1", name + ".select2")
          + ";");
    }
    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {
      onBlur.append(JspWidgetCallUtil.getSubmitScriptForEvent());
    }
    JspUtil.writeAttribute(out, "onblur", onBlur.toString());

    if (!StringUtils.isBlank(accessKey)) {
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    }
    if (disabled) {
      if (viewModel.isDisabled()) {
        JspUtil.writeAttribute(out, this.disabledRenderMode, this.disabledRenderMode);
      }
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }

  protected String fillXJSCallConstructor(String function, String timeInputEl, String hourSelectEl,
      String minuteSelectEl) {
    return FormTimeInputHtmlTag.staticFillXJSCall(function, timeInputEl, hourSelectEl, minuteSelectEl);
  }

  public static final String staticFillXJSCall(String function, String timeInputEl, String hourSelectEl,
      String minuteSelectEl) {
    StringBuffer sb = new StringBuffer(function).append("('").append(timeInputEl).append("','").append(hourSelectEl);
    return sb.append("','").append(minuteSelectEl).append("')").toString();
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Boolean, specifying whether HTML &lt;select&gt;'s should be shown for hour/minute selection."
   * 
   * @since 1.0.3
   */
  public void setShowTimeSelect(String showTimeSelect) {
    this.showTimeSelect = evaluate("showTimeSelect", showTimeSelect, Boolean.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Boolean, specifying whether HTML &lt;table&&gt;'s should be rendered to around this time input. Default is true."
   * 
   * @since 1.1
   */
  public void setSurroundingTable(String surroundingTable) {
    this.surroundingTable = evaluate("surroundingTable", surroundingTable, Boolean.class);
  }
}
