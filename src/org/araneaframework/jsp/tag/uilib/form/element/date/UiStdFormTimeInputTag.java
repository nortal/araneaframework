package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * Time input form element tag.
 * 
 * @author Marko Muts
 *  @jsp.tag
 *   name = "timeInput"
 *   body-content = "JSP"
 *   description = "Form time input field (custom control), represents UiLib "TimeControl"."
 */
public class UiStdFormTimeInputTag extends UiStdFormDateTimeInputBaseTag {

  public UiStdFormTimeInputTag() {
    styleClass = "aranea-time";
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("TimeControl");

    // Prepare    
    String name = this.getScopedFullFieldId();
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;

    // Write
    out
        .write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");
    this.writeTimeInput(out, name, name, viewModel.getSimpleValue(),
        localizedLabel, timeInputSize, viewModel.isDisabled(),
        accessKey);

    if (validate)
      writeValidationScript(out, viewModel);

    writeHourSelect(out, name, systemFormId);
    writeMinuteSelect(out, name);

    out.write("</td></tr></table>\n");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void writeMinuteSelect(Writer out, String name) throws IOException {
    out.write("<select name='" + name + ".select2' onChange=\""
        + fillXJSCallConstructor("fillTimeText", systemFormId, name)
        + ";\">\n");

    out.write("<option value=''></option>\n");
    for (int i = 0; i < 60; i++) {
      out.write("<option value='"
          + (i < 10 ? "0" + i : String.valueOf(i)) + "'>"
          + (i < 10 ? "0" + i : String.valueOf(i)) + "</option>\n");
    }
    out.write("</select>\n");
    out.write("<SCRIPT>"
        + fillXJSCallConstructor("fillTimeSelect", systemFormId, name)
        + ";</SCRIPT>\n");
  }

  protected void writeHourSelect(Writer out, String name, String systemFormId) throws IOException {
    out.write("<select name='" + name + ".select1' onChange=\""
        + fillXJSCallConstructor("fillTimeText", systemFormId, name)
        + ";\">\n");
    out.write("<option value=''></option>\n");

    for (int i = 0; i < 24; i++) {
      out.write("<option value='"
          + (i < 10 ? "0" + i : String.valueOf(i)) + "'>"
          + (i < 10 ? "0" + i : String.valueOf(i)) + "</option>\n");
    }
    out.write("</select>\n");
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
    UiUtil.writeAttribute(out, "onBlur", "fillTimeSelect('" + name + "');");
    if (!StringUtils.isBlank(accessKey))
      UiUtil.writeAttribute(out, "accesskey", accessKey);
    if (disabled)
      UiUtil.writeAttribute(out, "disabled", "true");
    UiUtil.writeAttributes(out, attributes);
    UiUtil.writeCloseStartEndTag_SS(out);
  }

  protected String fillXJSCallConstructor(String function, String formId, String element) {
    return UiStdFormDateTimeInputTag.staticFillXJSCall(function, formId, element);
  }
}
