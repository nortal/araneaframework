package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;

/**
 * Standard text input form element tag with autocompletion.
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag 
 *   name = "autoCompleteTextInput" 
 * 	 body-content = "JSP" 
 *   description = "Form text input field, represents UiLib &quot;AutoCompleteTextControl&quot;."
 */
public class FormAutoCompleteTextInputHtmlTag extends BaseFormTextInputHtmlTag {
  protected String divClass = "autocompletediv";  

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("AutoCompleteTextControl");

    AutoCompleteTextControl.ViewModel viewModel = ((AutoCompleteTextControl.ViewModel) controlViewModel);

    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("maxlength", viewModel.getMaxLength() + "");
    attributes.put("autocomplete", "off");

    if (this.onChangePrecondition == null) {
      this.onChangePrecondition = "return ";
    } else if (!this.onChangePrecondition.endsWith(";")) {
      this.onChangePrecondition += " return ";
    } else {
      this.onChangePrecondition += " && ";
    }

    this.onChangePrecondition += "!$('ACdiv." + getFullFieldId() + "').visible();";

    boolean events = this.events;
    this.events = false;
    writeTextInput(out, "text", true, attributes);
    this.events = events;

    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", "ACdiv." + getFullFieldId());
    JspUtil.writeAttribute(out, "class", divClass);
    JspUtil.writeAttribute(out, "style", "display:none;");
    JspUtil.writeCloseStartTag(out);
    JspUtil.writeEndTag(out, "div");

    JspUtil.writeStartTag_SS(out, "script type=\"text/javascript\"");
   	out.write(constructACRegistrationScript(viewModel));
   	JspUtil.writeEndTag(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Script for registration of the new autocompleter.
   * ***********************************************************************************/
  protected String constructACRegistrationScript(AutoCompleteTextControl.ViewModel viewModel) {
    StringBuffer script = new StringBuffer("Aranea.Behaviour.doAutoCompleteInputSetup('");
    script.append(getFullFieldId());
    script.append("',");

    if (viewModel.isOnChangeEventRegistered()) {
      script.append("'");
      script.append(OnChangeEventListener.ON_CHANGE_EVENT);
      script.append("',");
    } else {
      script.append("null,");
    }

    if (this.updateRegions != null && this.updateRegions.length() > 0) {
      script.append("'");
      script.append(this.updateRegions);
      script.append("',");
    } else {
      script.append("null,");
    }

    script.append("{minChars: ");
    script.append(String.valueOf(viewModel.getMinCompletionLength()));
    script.append("});");

    return script.toString();
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  
  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Class attribute assigned to &lt;DIV&gt; inside which suggestions are shown."
   */
  public void setDivClass(String divClass) {
    this.divClass = evaluate("divClass", divClass, String.class);
  }
}
