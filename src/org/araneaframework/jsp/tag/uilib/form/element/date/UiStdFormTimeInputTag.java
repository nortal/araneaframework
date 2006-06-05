package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Time input form element tag.
 * 
 * @author Marko Muts
 * @jsp.tag
 *   name = "timeInput"
 *   body-content = "JSP"
 *   description = "Form time input field (custom control), represents UiLib "TimeControl"."
 */
public class UiStdFormTimeInputTag extends UiStdFormDateTimeInputBaseTag {
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("TimeControl");

    // Prepare    
    String name = this.getScopedFullFieldId();
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;

    // Write
    out
        .write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");
    this.writeTimeInput(out, name, name, viewModel.getSimpleValue(),
        localizedLabel, timeInputSize, viewModel.isDisabled(),
        accessKey);

    if (validate)
      writeValidationScript(out, viewModel);

    Date currentTime = null;
    Integer minute = null, hour = null;
    try {
      if (viewModel.getSimpleValue() != null) {
   	    currentTime = viewModel.getCurrentSimpleDateTimeFormat().parse(viewModel.getSimpleValue());
  	    hour = new Integer(currentTime.getHours());
    	    minute = new Integer(currentTime.getMinutes());
      }
    } catch (ParseException e) {	}
    writeHourSelect(out, name, systemFormId, viewModel.isDisabled(), hour);
    writeMinuteSelect(out, name, viewModel.isDisabled(), minute);

    out.write("</td></tr></table>\n");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
    out.write("<select name='" + name + ".select2' onChange=\""
        + fillXJSCallConstructor("fillTimeText", systemFormId, name, name + ".select1", name + ".select2")
        + ";\"");

    if (disabled)
      out.write(" disabled=\"true\"");
    out.write(">\n");
    
    StringBuffer sb = new StringBuffer().append("<script type=\"text/javascript\">");
    sb.append("addOptions('"+name+".select2',60,").append(minute != null ? minute.toString():"null").append(");");
    sb.append("</script>\n</select>\n");
    
    out.write(sb.toString());
  }

  protected void writeHourSelect(Writer out, String name, String systemFormId, boolean disabled, Integer hour) throws IOException {
    out.write("<select name='" + name + ".select1' onChange=\""
        + fillXJSCallConstructor("fillTimeText", systemFormId, name, name + ".select1", name + ".select2")
        + ";\"");
    if (disabled)
      out.write(" disabled=\"true\"");
    out.write(">\n");
    
    StringBuffer sb = new StringBuffer().append("<script type=\"text/javascript\">");
    sb.append("addOptions('"+name+".select1',24,").append(hour != null ? hour.toString():"null").append(");");
    sb.append("</script>\n</select>\n");
    
    out.write(sb.toString());
  }

  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, StringArrayRequestControl.ViewModel viewModel) throws IOException {
    UiUtil.writeStartTag(out, "script");
    out.write("uiAddTimeValidator(");
    UiUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    UiUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");
    out.write(");\n");
    UiUtil.writeEndTag_SS(out, "script");
  }

  /**
   * Writes out time input
   */
  protected void writeTimeInput(Writer out, 
      String id, 
      String name,
      String value, 
      String label, 
      Long size, 
      boolean disabled,
      String accessKey) throws Exception {
    // Write input tag
    UiUtil.writeOpenStartTag(out, "input");
    if (!StringUtils.isBlank(id))
      UiUtil.writeAttribute(out, "id", id);
    UiUtil.writeAttribute(out, "name", name);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "type", "text");
    UiUtil.writeAttribute(out, "value", value);
    UiUtil.writeAttribute(out, "size", size);
    UiUtil.writeAttribute(out, "label", label);
    UiUtil.writeAttribute(out, "tabindex", tabindex);
    UiUtil.writeAttribute(out, "onBlur", fillXJSCallConstructor("fillTimeSelect", systemFormId, name, name +".select1", name + ".select2") + ";");
    if (!StringUtils.isBlank(accessKey))
      UiUtil.writeAttribute(out, "accesskey", accessKey);
    if (disabled)
      UiUtil.writeAttribute(out, "disabled", "true");
    UiUtil.writeAttributes(out, attributes);
    UiUtil.writeCloseStartEndTag_SS(out);
  }

  protected String fillXJSCallConstructor(String function, String formId, String timeInputEl, String hourSelectEl, String minuteSelectEl) {
    return UiStdFormTimeInputTag.staticFillXJSCall(function, formId, timeInputEl, hourSelectEl, minuteSelectEl);
  }
  
  public static final String staticFillXJSCall(String function, String formId, String timeInputEl, String hourSelectEl, String minuteSelectEl) {
    return function + "(document." + formId + ", '" + timeInputEl + "', '"  +  hourSelectEl + "', '" + minuteSelectEl + "')";
  }
}
