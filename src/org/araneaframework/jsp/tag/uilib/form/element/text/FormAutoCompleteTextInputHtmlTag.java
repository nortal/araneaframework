package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.ApplicationService;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.StateVersioningContext;
import org.araneaframework.jsp.UiUpdateEvent;
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
    writeTextInput(out, "text", true, attributes);

    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", "ACdiv." + getFullFieldId());
    JspUtil.writeAttribute(out, "class", divClass);
    // hide div to avoid flicker that occurs otherwise when new Ajax.AutoCompleter is finally created
    JspUtil.writeAttribute(out, "style", "display:none;");
    JspUtil.writeCloseStartTag(out);
    JspUtil.writeEndTag(out, "div");

    StringBuffer acRequestUrl = constructACUrl();

    JspUtil.writeStartTag_SS(out, "script type=\"text/javascript\"");
   	out.write(constructACRegistrationScript(viewModel, acRequestUrl));
   	JspUtil.writeEndTag(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Helper functions for construction of URL.
   * ***********************************************************************************/
  protected StringBuffer constructACUrl() {
    StringBuffer result = new StringBuffer();
    //this.getActionSubmitURL = function(systemForm, actionId, actionTarget, actionParam, sync, extraParams) {
    
    result.append("_ap.getSystemForm().action + \"?");
    result.append(constructServiceParameter(TopServiceContext.TOP_SERVICE_KEY)).append(" + \"").append('&');
    result.append(constructServiceParameter(TransactionContext.TRANSACTION_ID_KEY)).append(" + \"").append('&');
    result.append(constructServiceParameter(ThreadContext.THREAD_SERVICE_KEY)).append(" + \"").append('&');
    result.append(ApplicationService.ACTION_PATH_KEY).append('=').append(getFullFieldId()).append('&');
    result.append(ApplicationService.ACTION_HANDLER_ID_KEY).append('=').append(AutoCompleteTextControl.LISTENER_NAME);
    
    StateVersioningContext ctx = (StateVersioningContext)getEnvironment().getEntry(StateVersioningContext.class);
    if (ctx != null) {
      //XXX: wtf is that
      result.append('&');
      result.append(constructServiceParameter(StateVersioningContext.STATE_ID_REQUEST_KEY)).append(" + \"");
    }

	  return result;
  }
  
  protected String constructServiceParameter(String serviceId) {
	StringBuffer result = new StringBuffer();
	result.
	  append(serviceId).
	  append("=\"").
	  append("+_ap.getSystemForm().").
	  append(serviceId).
	  append(".value");
    return result.toString();
  }

  /* ***********************************************************************************
   * Script for registration of the new autocompleter.
   * ***********************************************************************************/
  protected String constructACRegistrationScript(AutoCompleteTextControl.ViewModel viewModel, StringBuffer acRequestUrl) {
    StringBuffer script = new StringBuffer();
    script.append("Aranea.Behaviour.doAutoCompleteInputSetup('");
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
  
  /** @since 1.0.2 */
  protected Map getOptionMap() {
    AutoCompleteTextControl.ViewModel viewModel = ((AutoCompleteTextControl.ViewModel) controlViewModel);

    Map result = new HashMap(1);
    result.put("minChars", String.valueOf(viewModel.getMinCompletionLength()));
    if (!viewModel.isDisabled() && events && viewModel.isOnChangeEventRegistered())
      result.put("afterUpdateElement", "function(el, selectedEl) { AraneaPage.findSystemForm(); " + JspWidgetCallUtil.getSubmitScriptForEvent(getOnChangeEvent()) + "}");
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
