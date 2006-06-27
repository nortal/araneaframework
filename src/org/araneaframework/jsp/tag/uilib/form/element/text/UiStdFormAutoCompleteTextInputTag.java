package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.StandardService;
import org.araneaframework.framework.container.StandardWidgetContainerWidget;
import org.araneaframework.framework.filter.StandardTransactionFilterWidget;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.framework.router.StandardTopServiceRouterService;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;

/**
 * Standard text input form element tag with autocompletion.
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@webmedia.ee)
 * 
 * @jsp.tag 
 *   name = "autoCompleteTextInput" 
 * 	 body-content = "JSP" 
 *   description = "Form text input field, represents UiLib &quot;AutoCompleteTextControl&quot;."
 */
public class UiStdFormAutoCompleteTextInputTag extends UiStdFormValidatingTextInputBaseTag {
  protected String divClass = "autocompletediv";  

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("AutoCompleteTextControl");

    AutoCompleteTextControl.ViewModel viewModel = ((AutoCompleteTextControl.ViewModel) controlViewModel);

    Map attributes = new HashMap();
    attributes.put("maxLength", viewModel.getMaxLength());
    attributes.put("autocomplete", "off");
    writeTextInput(out, "text", true, attributes);
    UiUtil.writeOpenStartTag(out, "div");
    UiUtil.writeAttribute(out, "id", "ACdiv." + getScopedFullFieldId());
    UiUtil.writeAttribute(out, "class", divClass);
    UiUtil.writeCloseStartTag(out);
    UiUtil.writeEndTag(out, "div");
    writeTextInputValidation(out);

    String systemFormId = (String)requireContextEntry(UiSystemFormTag.SYSTEM_FORM_ID_KEY);
    StringBuffer acRequestUrl = constructACUrl(systemFormId);
    
    UiUtil.writeStartTag_SS(out, "script");
    out.write(constructACRegistrationScript(viewModel, acRequestUrl).toString());
    UiUtil.writeEndTag(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Helper functions for construction of URL.
   * ***********************************************************************************/
  protected StringBuffer constructACUrl(String systemFormId) {
    StringBuffer result = new StringBuffer();
    result.append("document.").append(systemFormId).append(".action + \"?");
    result.append(constructServiceParameter(systemFormId, StandardTopServiceRouterService.TOP_SERVICE_KEY)).append(" + \"").append('&');
    result.append(constructServiceParameter(systemFormId, StandardTransactionFilterWidget.TRANSACTION_ID_KEY)).append(" + \"").append('&');
    result.append(constructServiceParameter(systemFormId, StandardThreadServiceRouterService.THREAD_SERVICE_KEY)).append(" + \"").append('&');
    result.append(StandardWidgetContainerWidget.ACTION_PATH_KEY).append('=').append(getScopedFullFieldId()).append('&');
    result.append(StandardService.ACTION_ID_ATTRIBUTE).append('=').append(AutoCompleteTextControl.LISTENER_NAME).append('&');
    result.append(getScopedFullFieldId()).append(".__present=true\"");

	return result;
  }
  
  protected String constructServiceParameter(String systemFormId, String serviceId) {
	StringBuffer result = new StringBuffer();
	result.
	  append(serviceId).
	  append("=\"").
	  append("+document.").
	  append(systemFormId).
	  append('.').
	  append(serviceId).
	  append(".value");
    return result.toString();
  }
  
  /* ***********************************************************************************
   * Script for registration of the new autocompleter.
   * ***********************************************************************************/
  protected String constructACRegistrationScript(AutoCompleteTextControl.ViewModel viewModel, StringBuffer acRequestUrl) {
	StringBuffer script = new StringBuffer();
    script.append("new Ajax.Autocompleter(\"");
    script.append(getScopedFullFieldId());
    script.append("\", \"ACdiv.");
    script.append(getScopedFullFieldId());
    script.append("\", ");
    script.append(acRequestUrl);
    script.append(", {minChars: ");
    script.append(viewModel.getMinCompletionLength());
    script.append("});");

	return script.toString();
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  
  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Class attribute assigned to &lt;DIV&gt; inside which suggestions are shown."
   */
  public void setDivClass(String divClass) throws JspException {
    this.divClass = (String) evaluate("divClass", divClass, String.class);
  }
}
