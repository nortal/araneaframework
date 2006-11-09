package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.BaseApplicationService;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.container.StandardContainerWidget;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.form.BaseSystemFormHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
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

    Map attributes = new HashMap();
    attributes.put("maxlength", viewModel.getMaxLength());
    attributes.put("autocomplete", "off");

    // TODO: make it completely consistent with every other tag 
    // Disallow event output from base tag -- directly outwritten onblur attribute breaks onchange functionality
    // due to autocomplete component attaching its own eventlisteners in javascript. Not sure why!
    // Attaching base onblur with Event.observe() does not work quite correctly either. It is probably caused 
    // by browser workaround in scriptaculous library.
    boolean b = events;
    events = false;
    writeTextInput(out, "text", true, attributes);
    events = b;
    // restore 
    //out.write("<script>Event.observe($('" + getScopedFullFieldId() + "'), 'change', function(){ " + JspWidgetCallUtil.getSubmitScriptForEvent() +"});</script>");
    
    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", "ACdiv." + getScopedFullFieldId());
    JspUtil.writeAttribute(out, "class", divClass);
    JspUtil.writeCloseStartTag(out);
    JspUtil.writeEndTag(out, "div");

    String systemFormId = (String)requireContextEntry(BaseSystemFormHtmlTag.SYSTEM_FORM_ID_KEY);
    StringBuffer acRequestUrl = constructACUrl(systemFormId);

   	JspUtil.writeStartTag_SS(out, "script");
   	out.write(constructACRegistrationScript(viewModel, acRequestUrl));
   	JspUtil.writeEndTag(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Helper functions for construction of URL.
   * ***********************************************************************************/
  protected StringBuffer constructACUrl(String systemFormId) {
    StringBuffer result = new StringBuffer();
    result.append("document.").append(systemFormId).append(".action + \"?");
    result.append(constructServiceParameter(systemFormId, TopServiceContext.TOP_SERVICE_KEY)).append(" + \"").append('&');
    result.append(constructServiceParameter(systemFormId, TransactionContext.TRANSACTION_ID_KEY)).append(" + \"").append('&');
    result.append(constructServiceParameter(systemFormId, ThreadContext.THREAD_SERVICE_KEY)).append(" + \"").append('&');
    result.append(StandardContainerWidget.ACTION_PATH_KEY).append('=').append(getScopedFullFieldId()).append('&');
    result.append(BaseApplicationService.ACTION_ID_ATTRIBUTE).append('=').append(AutoCompleteTextControl.LISTENER_NAME);

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
    script.append("\", {");
    
    // Autocompleter options
    for (Iterator i = getOptionMap().entrySet().iterator(); i.hasNext(); ) {
    	Map.Entry entry = (Map.Entry) i.next();
    	script.append((String)entry.getKey());
    	script.append(":");
    	script.append((String)entry.getValue());
    	if (i.hasNext()) script.append(",");
    }

    script.append("});");

	return script.toString();
  }
  
  /** @since 1.0.2 */
  protected Map getOptionMap() {
    AutoCompleteTextControl.ViewModel viewModel = ((AutoCompleteTextControl.ViewModel) controlViewModel);

    Map result = new HashMap(2);
    result.put("minChars", String.valueOf(viewModel.getMinCompletionLength()));
    if (!viewModel.isDisabled() && events && viewModel.isOnChangeEventRegistered())
      result.put("afterUpdateElement", "function(el, selectedEl) {" + JspWidgetCallUtil.getSubmitScriptForEvent(systemFormId, getOnChangeEvent()) + "}");
    return result;
  }
  
  protected UiUpdateEvent getOnChangeEvent() {
    return new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, formFullId + "." + derivedId, (String)null, updateRegionNames);
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
