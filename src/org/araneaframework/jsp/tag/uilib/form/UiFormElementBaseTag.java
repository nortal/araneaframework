/**
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
**/

package org.araneaframework.jsp.tag.uilib.form;				

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.UiMissingIdException;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUpdateRegionUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Base form element tag.
 * 
 * @author Oleg Mürk
 */
public class UiFormElementBaseTag extends UiPresentationTag implements UiFormElementTagInterface {
	public final static String COUNTER_KEY_REQUEST = "org.araneaframework.jsp.ui.uilib.form.UiFormElementBaseTag.COUNTER";
	
	protected String contextWidgetId;
	
	protected String systemFormId;
	
	protected String formFullId;
	protected String formScopedFullId;
	protected FormWidget.ViewModel formViewModel;
	
	protected FormElement.ViewModel formElementViewModel;
	protected	Control.ViewModel controlViewModel;
	protected String localizedLabel;
	protected String accessKey;
	
	protected String id;	
	protected boolean events;
	protected boolean validate;
	protected boolean validateOnEvent;
	protected String tabindex;
	protected String updateRegions;
	protected String globalUpdateRegions;  
	
	protected List updateRegionNames;    
	
	protected String accessKeyId;
	
	private boolean hasElementContextSpan = true;
	
	//
	// Attributes
	//
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Element id, can also be inherited."
	 */
	public void setId(String id) throws JspException {
		this.id = (String)evaluateNotNull("id", id, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Whether the element will send the events that are registered by server-side (by default "true")."
	 */	
	public void setEvents(String events) throws JspException {
		this.events = ((Boolean)evaluateNotNull("events", events, Boolean.class)).booleanValue(); 
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Whether the element will be validated on the client-side when the form is submitted (by default "true")."
	 */	
	public void setValidate(String validate) throws JspException {
		this.validate = ((Boolean)evaluateNotNull("validate", validate, Boolean.class)).booleanValue(); 
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Whether the form will be validated on the client-side when the element generates an event (by default "false")."
	 */	
	public void setValidateOnEvent(String validateOnEvent) throws JspException {
		this.validateOnEvent = ((Boolean)evaluateNotNull("validateOnEvent", validateOnEvent, Boolean.class)).booleanValue(); 
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Element tabindex."
	 */	
	public void setTabindex(String tabindex) throws JspException {
		this.tabindex = (String)evaluateNotNull("tabindex", tabindex, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details."
	 */	
	public void setUpdateRegions(String updateRegions) throws JspException {
		this.updateRegions = (String) evaluate("updateRegions", updateRegions, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Enumerates the regions of markup to be updated globally. Please see <code><ui:updateRegion></code> for details."
	 */	
	public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException {
		this.globalUpdateRegions = (String) evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
	}  
	
	/** 	
	 * Id of the resource specifying the accesskey for this element.
	 * Support for accesskeys is specific for each form element and is 
	 * to be implemented in each form element separately.
	 * 
	 * Usually you won't need to specify the access key for the element at all, but rather
	 * for the element's label.
	 * It's ok to specify null, empty string, or nothing for this property.
	 */
	public void setAccessKeyId(String accessKeyId) throws JspException {
		this.accessKeyId = (String)evaluate("accessKeyId", accessKeyId, String.class);
	}
	
	//
	// Implementation
	//
	
	protected int before(Writer out) throws Exception {
		super.before(out);    
		
		//Get context widget id
		contextWidgetId = UiWidgetUtil.getContextWidgetFullId(pageContext);	
		
		// Get system form id 
		systemFormId = (String)readAttribute(UiSystemFormTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get form data		
		formScopedFullId = (String)readAttribute(UiFormTag.FORM_SCOPED_FULL_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		formFullId = (String)readAttribute(UiFormTag.FORM_FULL_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		formViewModel = (FormWidget.ViewModel)readAttribute(UiFormTag.FORM_VIEW_MODEL_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		FormWidget form = (FormWidget)readAttribute(UiFormTag.FORM_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		//In case the tag is in formElement tag
		if (id == null && getAttribute(UiFormElementTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE) != null) 
			id = (String) getAttribute(UiFormElementTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		if (id == null) throw new UiMissingIdException(this);        
		formElementViewModel = 
			(FormElement.ViewModel) UiWidgetUtil.traverseToSubWidget(form, id)._getViewable().getViewModel();   
		
		// Get control	
		controlViewModel = (formElementViewModel).getControl();
		localizedLabel = UiUtil.getResourceString(pageContext, controlViewModel.getLabel());
		
		// We shall use the accesskey HTML attribute for this form element only if the attribute "accessKey" 
		// was explicitly set (otherwise in most common cases the label tag sets up the access key)
		if (accessKeyId != null) {
			accessKey = UiUtil.getResourceStringOrNull(pageContext, accessKeyId);
		}
		if (accessKey != null && accessKey.length() != 1) accessKey = null;
		
		if (hasElementContextSpan) writeFormElementContextOpen(out, formScopedFullId, id, pageContext);
		
		UiUtil.writeHiddenInputElement(out, getScopedFullFieldId() + ".__present", "true");
		
		updateRegionNames = UiUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions, globalUpdateRegions);
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
	
	
	protected int after(Writer out) throws Exception {
		if (hasElementContextSpan) writeFormElementContextClose(out);
		// Continue
		super.after(out);
		return EVAL_PAGE;
	}
	
	
	protected void init() {
		super.init();
		
		this.hasElementContextSpan = true;
		this.id = null;
		this.events = true;
		this.validate = true;
		this.validateOnEvent = false;
		this.tabindex = null;
		
		this.updateRegions = null;
		this.globalUpdateRegions = null;
	}
	
	/**
	 * Computes field name.
	 */	
	protected String getScopedFullFieldId() {
		return formScopedFullId + "." + id;
	}
	
	/**
	 * Computes field name.
	 */	
	protected String getFullFieldId() {
		return formFullId + "." + id;
	}
	
	/**
	 * Asserts that associated control is of given type. If the
	 * condition does not hold, throws exception.
	 */
	protected void assertControlType(String type) throws JspException {
		if (!controlViewModel.getControlType().equals(type))
			throw new UiException("Control of type '" + type + "' expected in form element '" + id + "' instead of '" + controlViewModel.getControlType() + "'");
	}
	
	
	/** 
	 * Write a span with random id around the element, and register this span with javascript
	 * @param elementName the name of the element for which the uiFormElementContext function will be invoked
	 * @throws Exception 
	 */
	public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId, PageContext pageContext) throws Exception{
		
		//  Enclose the element in a <span id=somerandomid>
		//  Register this span using javascript
		String spanId = "form-element-span-" + generateId(pageContext);
		String elementName = fullFormId + "." + elementId;
		
		// Determine whether form element with that id is valid
		
		// This code actually prevents using validation for non-simple form elements
		// (this may be important because simpleLabel calls this method)
		FormWidget form = 
			(FormWidget)UiUtil.readAttribute(pageContext, UiFormTag.FORM_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		FormElement.ViewModel formElementViewModel = 
			(FormElement.ViewModel) UiWidgetUtil.traverseToSubWidget(form, elementId)._getViewable().getViewModel();
		boolean isValid = formElementViewModel.isValid();
		
		
		UiUtil.writeOpenStartTag(out, "span");
		UiUtil.writeAttribute(out, "id", spanId);
		
		// We'll also use the span around a form element for tracking keyboard events.
		// that is, the span will call our handler on a keypress.
		// Another, better way would be to set up some global keypress handler, but unfortunately 
		// that way it is too difficult to determine exactly which element was the target for the event.
		
		// All events are sent to a handler called "uiHandleKeypress(event, formElementId)"
		// We use the "keydown" event, not keypress, because this allows to
		// catch F2 in IE.
		UiUtil.writeAttribute(out, "onkeydown", "uiHandleKeypress(event, '" + elementName +"');");
		UiUtil.writeCloseStartTag(out);
		
		UiUtil.writeStartTag(out, "script");
		out.write("uiFormElementContext(");
		UiUtil.writeScriptString(out, elementName);
		out.write(", ");
		UiUtil.writeScriptString(out, spanId);
		out.write(", ");
		out.write(isValid ? "true" : "false");    
		out.write(");\n");
		UiUtil.writeEndTag_SS(out, "script");
	}
	
	/**
	 * Closes the span opened by writeFormElementContextOpen
	 * @param out
	 * @throws IOException
	 */
	public static void writeFormElementContextClose(Writer out) throws IOException{
		UiUtil.writeEndTag_SS(out, "span");    
	}
	
	/**
	 * Generates an id, that is unique in request scope.
	 * @param pageContext
	 * @return
	 */
	public static Long generateId(PageContext pageContext){
		Long counter = (Long)pageContext.getAttribute(COUNTER_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		if (counter == null)
			counter = new Long(0);
		else
			counter = new Long(counter.longValue() + 1);
		pageContext.setAttribute(COUNTER_KEY_REQUEST, counter, PageContext.REQUEST_SCOPE);
		return counter;
	}
	
	/**
	 * Determines whether a "context span" will surround the contents of this tag.
	 * You should only be interested in this property if you are making a subclass of 
	 * this tag, and the tag might be used as a container for other form element tags.
	 * 
	 * Normally, each "form-element" tag surrounds its contents with a HTML &lt;span&gt;
	 * tag, denoting the span of this element (this is later used to highlight element in
	 * validation or to capture keyboard events).
	 * These spans should not nest as this will produce problems capturing keyboard events.
	 * (the same keypress will be captured twice: once by the child span, and once by the parent).
	 * 
	 * Therefore, if this "form-element-tag" might contain child "form-elements" (like a CheckboxMultiselect
	 * for example), it should not have its own "context span", only its children should. (or maybe vice-versa).
	 * Anyway, setting this value to false will disable the context span. Default is true.
	 * 
	 * @param hasElementContextSpan
	 */
	protected void setHasElementContextSpan(boolean hasElementContextSpan) {
		this.hasElementContextSpan = hasElementContextSpan;
	}
	
	/**
	 * @see #setHasElementContextSpan
	 */
	protected boolean getHasElementContextSpan(){
		return hasElementContextSpan;
	}
	
	//// Script writing
	
	/**
	 * Writes standard validation script that checks element for mandatority.
	 * This function should not actually be ever used and its main value here
	 * is to illustrate a typical validation script.
	 * A specific validator should be used for every type of control.
	 * @author Konstantin Tretyakov
	 */	
	private void writeValidationScript(Writer out, String name, String label, boolean isMandatory) throws IOException {
		UiUtil.writeStartTag(out, "script");
		out.write("uiAddDefaultValidator(");
		UiUtil.writeScriptString(out, name);
		out.write(", ");
		UiUtil.writeScriptString(out, label);
		out.write(", ");		
		out.write(isMandatory ? "true" : "false");
		out.write(");\n");
		UiUtil.writeEndTag_SS(out, "script");
	}
	
	/**
	 * Writes event handling attribute which validates the form, if neccessary, and submits 
	 * event to the system form.
	 * @throws JspException 
	 */
	protected void writeEventAttributeForUiEvent(Writer out, String attributeName, String id, String eventId, boolean validate, String precondition, List updateRegions) throws IOException, JspException { 
		UiStdWidgetCallUtil.writeEventAttributeForFormEvent(
				pageContext, 
				out, 
				attributeName, 
				systemFormId,  
				formFullId, 
				id, 
				eventId, 
				null, 
				validate, 
				precondition,
				updateRegions);
	}	
	
	/** 
	 * Writes event handling function that is called on closeCalendar javascript function on picking date
	 * @author <a href='mailto:margus@webmedia.ee'>Margus Väli</a> 6.05.2005
	 * @throws JspException 
	 */
	protected void writeEventScriptForCalendar(Writer out, String id, boolean validate, String precondition) throws IOException, JspException {
		UiStdWidgetCallUtil.writeEventScriptForFormEvent(
				pageContext, 
				out, 
				systemFormId, 
				formFullId, 
				this.id, 
				id, 
				null, 
				validate, 
				precondition, 
				null);
	}
}
