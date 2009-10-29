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

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.DateControl;

/**
 * Standard date time input form element base tag.
 * 
 * @author Oleg Mürk
 * @author <a href='mailto:margus@webmedia.ee'>Margus Väli</a> 2.05.2005 - precondition and onblur event notifying
 *         listeners
 */
public class BaseFormDateTimeInputHtmlTag extends BaseFormElementHtmlTag {

  public final static String CALENDAR_BUTTON_ID_SUFFIX = "_cbutton";

  public final static Long DEFAULT_DATE_INPUT_SIZE = 11L;

  public final static Long DEFAULT_TIME_INPUT_SIZE = 5L;

  protected String onChangePrecondition;

  protected String calendarAlignment;

  protected String calendarIconClass = "middle";

  protected String disabledRenderMode = RENDER_DISABLED_DISABLED;

  protected boolean disableCalendar = false;

  protected String dateFormat = "%d.%m.%Y";

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Precondition for deciding whether go to server side or not."
   */
  public void setOnChangePrecondition(String onChangePrecondition) {
    this.onChangePrecondition = evaluate("onChangePrecondition", onChangePrecondition, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Specifies how to render a disabled input. Valid options are <code>'disabled'</code> and <code>'read-only'</code>. Default is <code>'disabled'</code>."
   * @since 1.1.3
   */
  public void setDisabledRenderMode(String disabledRenderMode) throws JspException {
    this.disabledRenderMode = evaluateDisabledRenderMode(disabledRenderMode);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Alignment for popup calendar. In form 'zx' where z is in {TBCtb} and x in {LRClr}. Default is 'Br' (Bottom, right)."
   */
  public void setCalendarAlignment(String calendarAlignment) {
    this.calendarAlignment = evaluate("calendarAlignment", calendarAlignment, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Specifies whether the calendar should not be rendered (default is false)."
   */
  public void setDisableCalendar(String disableCalendar) throws JspException {
    this.disableCalendar = evaluateNotNull("disableCalendar", disableCalendar, Boolean.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Specifies date format for date input (default is '%d.%m.%Y')."
   */
  public void setDateFormat(String dateFormat) throws JspException {
    this.dateFormat = evaluateNotNull("dateFormat", dateFormat, String.class);
  }

  /**
   * Writes out date input
   * 
   * @param out The writer of rendered output.
   * @param id The ID to give to the rendered input.
   * @param name The name to give to the rendered input.
   * @param valueThe value to give to the rendered input.
   * @param label The label to give to the rendered input.
   * @param isMandatory Whether the rendered input is mandatory.
   * @param isValid Whether the current value of the rendered input is currently valid.
   * @param size The size attribute of the rendered input.
   * @param disabled Whether the date input is currently disabled.
   * @param styleClass The CSS classes to give to the rendered input.
   * @param accessKey An access key to give to the rendered input.
   * @param viewModel The ViewModel with data of the current control.
   * @throws Exception Any exception that may occur.
   */
  protected void writeDateInput(Writer out, String id, String name, String value, String label, boolean isMandatory,
      boolean isValid, Long size, boolean disabled, String styleClass, String accessKey, DateControl.ViewModel viewModel)
      throws Exception {

    if (viewModel.getInputFilter() != null) {
      this.attributes.put(AraneaAttributes.FilteredInputControl.CHARACTER_FILTER, viewModel.getInputFilter()
          .getCharacterFilter());
    }

    if (StringUtils.isBlank(id)) {
      id = getFullFieldId();
    }

    // Write input tag
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);
    if (StringUtils.isNotBlank(accessKey)) {
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    }

    if (disabled) {
      if (viewModel.isDisabled()) {
        JspUtil.writeAttribute(out, this.disabledRenderMode, this.disabledRenderMode);
      }
    } else if (this.events && viewModel.isOnChangeEventRegistered()) {
      writeSubmitScriptForUiEvent(out, "onchange", this.derivedId, "onChanged", this.onChangePrecondition,
          this.updateRegionNames);
    }

    // validation won't occur with Event.observe registered in aranea-behaviour when date selected from calendar
    if (!viewModel.isOnChangeEventRegistered() && this.backgroundValidation) {
      JspUtil.writeAttribute(out, "onchange", "formElementValidationActionCall(this)");
    }

    writeBackgroundValidationAttribute(out);

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);

    if (!disabled && !this.disableCalendar) {

      JspUtil.writeOpenStartTag(out, "a");
      JspUtil.writeAttribute(out, "href", "javascript:;");
      JspUtil.writeCloseStartTag_SS(out);

      String calendarImgId = id + CALENDAR_BUTTON_ID_SUFFIX;
      JspUtil.writeOpenStartTag(out, "img");
      out.write(" src=\"");
      out.write(FileImportUtil.getImportString("gfx/ico_calendar.gif", this.pageContext.getRequest()));
      out.write("\" ");
      JspUtil.writeAttribute(out, "id", calendarImgId);
      JspUtil.writeAttribute(out, "class", this.calendarIconClass);
      JspUtil.writeCloseStartEndTag_SS(out);

      JspUtil.writeEndTag_SS(out, "a");

      writeCalendarScript(out, id, this.dateFormat);
    }
  }

  /**
   * Returns a script that sets the currently selected SELECT option on client side.
   * @since 1.0.3
   */
  protected String getTimeSelectScript(String selectId, Integer value, int valueCount) {
    StringBuffer sb = new StringBuffer("Aranea.UI.addOptions('");
    sb.append(selectId);
    sb.append("',");
    sb.append(String.valueOf(valueCount));
    sb.append(",");
    sb.append(value != null ? value.toString() : "null");
    sb.append(");");
    return sb.toString();
  }

  /**
   * @since 1.0.3
   */
  protected String getTimeInputOnChangePrecondition(String timeInputId) {
    if (this.onChangePrecondition != null) {
      return this.onChangePrecondition;
    }

    String timeInputRef = new StringBuffer("$F('").append(timeInputId).append("')").toString();

    StringBuffer precondition = new StringBuffer();
    precondition.append("return Aranea.UI.isChanged('");
    precondition.append(timeInputId);
    precondition.append("') && ((");
    precondition.append(timeInputRef);
    precondition.append(".length==5) || (");
    precondition.append(timeInputRef);
    precondition.append(".length==0))");
    return precondition.toString();
  }

  /**
   * @since 1.0.3
   */
  protected String getHourSelectOnChangePrecondition(String timeInputId) {
    return getSelectOnChangePrecondition(timeInputId);
  }

  /**
   * @since 1.0.3
   */
  protected String getMinuteSelectOnChangePrecondition(String timeInputId) {
    return getSelectOnChangePrecondition(timeInputId);
  }

  /**
   * @since 1.0.3
   */
  protected String getSelectOnChangePrecondition(String timeInputId) {
    String precondition = this.onChangePrecondition;
    if (precondition == null) {
      precondition = "return $F('" + timeInputId + "').length==5";
    }
    return precondition;
  }

  protected void writeCalendarScript(Writer out, String id, String format) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag(out);

    StringBuffer script = new StringBuffer();
    script.append("Aranea.UI.calendarSetup('");
    script.append(id);
    script.append("', '");
    script.append(format);
    script.append("', ");

    if (this.calendarAlignment == null) {
      script.append("null");
    } else {
      script.append("'");
      script.append(this.calendarAlignment);
      script.append("'");
    }

    script.append(");");

    out.write(script.toString());
    JspUtil.writeEndTag_SS(out, "script");
  }

}
