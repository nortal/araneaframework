package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.DateTimeControl;

/**
 * Date/time input form element tag.
 *
 * @author Marko Muts
 * @jsp.tag
 *   name = "dateTimeInput"
 *   body-content = "JSP"
 *   description = "Form date and time input field (custom control), represents UiLib "DateTimeControl"."
 */
public class FormDateTimeInputHtmlTag extends BaseFormDateTimeInputHtmlTag {

  protected String timeStyleClass;

  protected String dateStyleClass;

  protected boolean showTimeSelect = true;

  protected boolean inline = false;

  public FormDateTimeInputHtmlTag() {
    this.timeStyleClass = "aranea-time";
    this.dateStyleClass = "aranea-date";
  }

  public String getDateStyleClass() {
    return this.dateStyleClass;
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlTypes("DateTimeControl", "JodaDateTimeControl");

    // Prepare
    String name = this.getFullFieldId();
    DateTimeControl.ViewModel viewModel = (DateTimeControl.ViewModel) this.controlViewModel;

    Integer hour = viewModel.getHour();
    Integer minute = viewModel.getMinute();

    if (this.showTimeSelect) {
      if (hour == null) {
        hour = getParamValue(name + ".select1");
      }
      if (minute == null) {
        minute = getParamValue(name + ".select2");
      }
    }

    // Write
    if (!this.inline) {
      out.write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");
    }

    this.writeDateInput(
        out,
        name,
        name + ".date",
        viewModel.getDate(),
        this.localizedLabel,
        viewModel.isMandatory(),
        this.formElementViewModel.isValid(),
        DEFAULT_DATE_INPUT_SIZE,
        viewModel.isDateDisabled(),
        getDateStyleClass(),
        this.accessKey);

    out.write("&nbsp;");

    writeTimeInput(
        out,
        name,
        viewModel.getTime(),
        this.localizedLabel,
        DEFAULT_TIME_INPUT_SIZE,
        viewModel.isDisabled());

    if (this.showTimeSelect) {
      writeHourSelect(out, name, viewModel.isTimeDisabled(), viewModel.getHour());
      writeMinuteSelect(out, name, viewModel.isTimeDisabled(), viewModel.getMinute());
    }

    if (!this.inline) {
      out.write("</td></tr></table>\n");
    }

    return super.doEndTag(out);
  }

  protected Integer getParamValue(String param) {
    String value = ServletUtil.getRequest(getOutputData().getInputData()).getParameter(param);
    return StringUtils.isBlank(value) ? null : Integer.valueOf(value);
  }

  public String getTimeStyleClass() {
    return this.timeStyleClass;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Css class for date."
   */
  public void setDateStyleClass(String dateCssClass) throws JspException {
    this.dateStyleClass = (String) evaluate("dateStyleClass", dateCssClass, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Css class for time."
   */
  public void setTimeStyleClass(String timeCssClass) throws JspException {
    this.timeStyleClass = (String) evaluate("timeStyleClass", timeCssClass, String.class);
  }

  /* ***********************************************************************************
   * Helper functions, mostly javascript.
   * ***********************************************************************************/

  protected void writeHourSelect(Writer out, String name, boolean disabled, Integer hour) throws IOException {
    writeSelect(out, name, disabled, hour, true);
  }

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
    writeSelect(out, name, disabled, minute, false);
  }

  protected void writeSelect(Writer out, String name, boolean disabled, Integer value, boolean isHour) throws IOException {
    DateTimeControl.ViewModel viewModel = (DateTimeControl.ViewModel) this.controlViewModel;

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
    out.write(".");
    out.write(selectField);
    out.write("\" name=\"");
    out.write(name);
    out.write(".");
    out.write(selectField);
    out.write("\" onchange=\"");
    out.write(fillXJSCallConstructor("Aranea.UI.fillTimeText", name));
    out.write(";");

    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {
      out.write(JspWidgetCallUtil.getSubmitScriptForEvent());
    }

    out.write("\"");

    if (disabled) {
      out.write(" disabled=\"disabled\"");
    }

    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {
      UiUpdateEvent event = new UiUpdateEvent(
          OnChangeEventListener.ON_CHANGE_EVENT, name, null, this.updateRegionNames);
      event.setEventPrecondition(precondition);

      out.write(" ");
      out.write(event.getEventAttributes().toString());
    }
    out.write(">\n");

    JspUtil.writeStartTag_SS(out, "script");
    out.write(selectScript);
    JspUtil.writeEndTag_SS(out, "script");

    JspUtil.writeEndTag_SS(out, "select");
  }

  /**
   * Writes out time input
   */
  protected void writeTimeInput(Writer out, String name, String value,
      String label, Long size, boolean disabled) throws Exception {

    DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel) this.controlViewModel);

    if (viewModel.getTimeViewModel().getInputFilter() != null) {
      this.attributes.put(AraneaAttributes.FilteredInputControl.CHARACTER_FILTER,
          viewModel.getTimeViewModel().getInputFilter().getCharacterFilter());
    }

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", name + ".time");
    JspUtil.writeAttribute(out, "name", name + ".time");
    JspUtil.writeAttribute(out, "class", getTimeStyleClass());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", tabindex);

    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {

      JspUtil.writeAttribute(out, "onfocus", "Aranea.UI.saveValue(this)");
      UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
      event.setEventPrecondition(getTimeInputOnChangePrecondition(name
          + ".time"));
      out.write(" ");
      out.write(event.getEventAttributes().toString());
    }

    StringBuffer onBlur = new StringBuffer();
    if (this.showTimeSelect) {
      onBlur.append(fillXJSCallConstructor("Aranea.UI.fillTimeSelect", name) + ";");
    }

    if (!disabled && this.events && viewModel.isOnChangeEventRegistered()) {
      onBlur.append(JspWidgetCallUtil.getSubmitScriptForEvent());
    }

    JspUtil.writeAttribute(out, "onBlur", onBlur.toString());

    if (disabled) {
      if (viewModel.isDisabled()) {
        JspUtil.writeAttribute(out, this.disabledRenderMode,
            this.disabledRenderMode);
      }
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }

  /**
   * Writes out date input.
   *
   * If just super.writeDateInput is called then date's viewmodel does not have
     * correct information about whether onchange listeners are registered and who has registered them.
   */
  protected void writeDateInput(
      Writer out,
      String id,
      String name,
      String value,
      String label,
      boolean mandatory,
      boolean valid,
      Long size,
      boolean disabled,
      String styleClass,
      String accessKey) throws Exception {
    DateTimeControl.ViewModel dateTimeViewModel = ((DateTimeControl.ViewModel) controlViewModel);
    // Write input tag

    if (dateTimeViewModel.getDateViewModel().getInputFilter() != null) {
      this.attributes.put(AraneaAttributes.FilteredInputControl.CHARACTER_FILTER, dateTimeViewModel.getDateViewModel().getInputFilter().getCharacterFilter());
    }

    if (StringUtils.isBlank(id)) {
      id = name;
    }

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", styleClass);
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);
    if (!StringUtils.isBlank(accessKey)) JspUtil.writeAttribute(out, "accesskey", accessKey);

    if (disabled) {
      JspUtil.writeAttribute(out, this.disabledRenderMode,
        this.disabledRenderMode);
    } else if (this.events && dateTimeViewModel.isOnChangeEventRegistered()) {
      writeSubmitScriptForUiEvent(out, "onchange", this.derivedId, "onChanged",
          this.onChangePrecondition, this.updateRegionNames);
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);

    if (!disabled) {
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

  protected String fillXJSCallConstructor(String function, String element) {
    return FormTimeInputHtmlTag.staticFillXJSCall(function, element + ".time",
        element + ".select1", element + ".select2");
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Boolean, specifying whether HTML &lt;select&;gt;'s should be shown for foolproof hour/minute selection."
   *
   * @since 1.0.3
   */
  public void setShowTimeSelect(String showTimeSelect) throws JspException {
    this.showTimeSelect = ((Boolean) evaluate("showTimeSelect", showTimeSelect,
        Boolean.class)).booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Boolean, specifying whether the inputs should be wrapped in an HTML table (default: true)."
   *
   * @since 1.2.1
   */
  public void setInline(String inline) throws JspException {
    this.inline = ((Boolean) evaluate("inline", inline, Boolean.class)).booleanValue();
  }

}
