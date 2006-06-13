package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * Standard text input form element tag with autocompletion.
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * 
 * @jsp.tag name = "autoCompleteTextInput" body-content = "JSP" description =
 *          "Form text input field, represents UiLib "AutoCompleteTextControl"."
 */
public class UiStdFormAutoCompleteTextInputTag extends
    UiStdFormValidatingTextInputBaseTag {

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("AutoCompleteTextControl");

    String name = this.getScopedFullFieldId();
    TextControl.ViewModel viewModel = ((TextControl.ViewModel) controlViewModel);

    Map attributes = new HashMap();
    attributes.put("maxLength", viewModel.getMaxLength());
    attributes.put("autocomplete", "off");
    writeTextInput(out, "text", true, attributes);
    UiUtil.writeOpenStartTag(out, "div");
    UiUtil.writeAttribute(out, "id", "ACdiv." + getScopedFullFieldId());
    UiUtil.writeAttribute(out, "class", "autocompletediv");
    UiUtil.writeCloseStartTag(out);
    UiUtil.writeEndTag(out, "div");
    writeTextInputValidation(out);
    
    //XXX
    String systemFormId = (String)getContextEntry(UiSystemFormTag.SYSTEM_FORM_ID_KEY);
    StringBuffer url = new StringBuffer();
    url.append("document.").append(systemFormId).append(".action + \"?");
    url.append("topServiceId=\" + document.").append(systemFormId).append(".topServiceId.value + \"");
    url.append("&transactionId=\" + document.").append(systemFormId).append(".transactionId.value + \"");
    url.append("&threadServiceId=\" + document.").append(systemFormId).append(".threadServiceId.value + \"");
    url.append("&widgetActionPath=").append(getScopedFullFieldId());
    url.append("&serviceActionListenerId=autocomplete&");
    url.append(getScopedFullFieldId()).append(".__present=true\"");
    
    UiUtil.writeOpenStartTag(out, "script");
    UiUtil.writeCloseStartTag(out);
    StringBuffer script = new StringBuffer();
    script.append("new Ajax.Autocompleter(\"");
    script.append(getScopedFullFieldId());
    script.append("\", \"ACdiv.");
    script.append(getScopedFullFieldId());
    script.append("\", ");
    script.append(url);
    script.append(", {minChars: 2});");
    out.write(script.toString());
    UiUtil.writeEndTag(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

}
