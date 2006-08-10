package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.TimestampControl.ViewModel;

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

  public FormDateTimeInputHtmlTag() {
    timeStyleClass = "aranea-time";
    dateStyleClass = "aranea-date";
  }

  public String getDateStyleClass() {
    return dateStyleClass;
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("DateTimeControl");

    // Prepare
    String name = this.getScopedFullFieldId();
    DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel) controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;
    Long dateInputSize = DEFAULT_DATE_INPUT_SIZE;
    JspUtil.writeHiddenInputElement(out, getScopedFullFieldId() + ".__present", "true");

    // Write
    out.write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");

    this.writeDateInput(
        out,
        name,
        name + ".date", 
        viewModel.getDate(), 
        localizedLabel,
        viewModel.isMandatory(), 
        formElementViewModel.isValid(),
        dateInputSize,
        viewModel.isDisabled(),
        getDateStyleClass(),
        accessKey,
        viewModel.getDateViewModel());

    out.write("&nbsp;");
    writeTimeInput(out, name, viewModel.getTime(), localizedLabel,
        timeInputSize, viewModel.isDisabled());

    Date currentTime = null;
    Integer minute = null, hour = null;
    try {
      ViewModel timeViewModel = viewModel.getTimeViewModel();
      if (timeViewModel.getSimpleValue() != null) {
   	    currentTime = timeViewModel.getCurrentSimpleDateTimeFormat().parse(timeViewModel.getSimpleValue());
  	    hour = new Integer(currentTime.getHours());
    	    minute = new Integer(currentTime.getMinutes());
      }
    } catch (ParseException e) {	}

    writeHourSelect(out, name, viewModel.isDisabled(), hour);
    writeMinuteSelect(out, name, viewModel.isDisabled(), minute);

    out.write("</td></tr></table>\n");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  public String getTimeStyleClass() {
    return timeStyleClass;
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

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
    out.write("<select name='"
        + name
        + ".select2' onChange=\"" +
        fillXJSCallConstructor("fillTimeText", systemFormId, name)
        + ";\"");

    if (disabled)
      out.write(" disabled=\"true\"");
    out.write(">\n");

    StringBuffer sb = new StringBuffer().append("<script type=\"text/javascript\">");
    sb.append("addOptions('"+name+".select2',60,").append(minute != null ? minute.toString():"null").append(");");
    sb.append("</script>\n</select>\n");
    
    out.write(sb.toString());
  }

  protected void writeHourSelect(Writer out, String name, boolean disabled, Integer hour) throws IOException {
    out.write("<select name='"
        + name
        + ".select1' onChange=\"" + 
        fillXJSCallConstructor("fillTimeText", systemFormId, name) + ";\"");
    if (disabled)
      out.write(" disabled=\"true\"");
    out.write(">\n");
    
    StringBuffer sb = new StringBuffer().append("<script type=\"text/javascript\">");
    sb.append("addOptions('" + name +".select1',24,").append(hour != null ? hour.toString():"null").append(");");
    sb.append("</script>\n</select>\n");
    
    out.write(sb.toString());
  }

  /**
   * Writes out time input
   */
  protected void writeTimeInput(Writer out, String name, String value,
      String label, Long size, boolean disabled) throws Exception {
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "name", name + ".time");
    JspUtil.writeAttribute(out, "class", getTimeStyleClass());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "label", label);
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttribute(out, "onBlur", fillXJSCallConstructor("fillTimeSelect", systemFormId, name) + ";");
    if (disabled)
      JspUtil.writeAttribute(out, "disabled", "true");
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }

  protected String fillXJSCallConstructor(String function, String formId, String element) {
    return FormTimeInputHtmlTag.staticFillXJSCall(function, formId, element +".time", element + ".select1", element + ".select2");
  }
}
