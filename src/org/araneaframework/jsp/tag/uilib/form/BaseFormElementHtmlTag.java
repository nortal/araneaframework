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
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.util.ConfigurationContextUtil;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;


/**
 * Base form element tag.
 * 
 * @author Oleg Mürk
 */
public class BaseFormElementHtmlTag extends PresentationTag implements FormElementTagInterface {
	/** @since 1.1 */
	public static final String FORMELEMENT_SPAN_PREFIX = "fe-span-";

    public static final String RENDER_DISABLED_DISABLED = "disabled";

    public static final String RENDER_DISABLED_READONLY = "read-only";

    protected String formFullId;
	
	protected FormWidget.ViewModel formViewModel;
	protected FormElement.ViewModel formElementViewModel;
	protected Control.ViewModel controlViewModel;
	
	protected String localizedLabel;
	
	protected String accessKeyId;
	
	protected List updateRegionNames;    
	private boolean hasElementContextSpan = true;
	
	protected String derivedId;
	
	//Attributes
	
	protected boolean events = true;
	protected boolean validateOnEvent = false;
	/** @since 1.1 */
	protected boolean backgroundValidation = false;
		
	protected String accessKey;
	
	protected String id;
	protected String tabindex;
	protected String updateRegions;
	protected String globalUpdateRegions;  
	
	/* ***********************************************************************************
	 * Start & End tags
	 * ***********************************************************************************/

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);    

		// Get form data		
		formFullId = (String)requireContextEntry(FormTag.FORM_FULL_ID_KEY);
		formViewModel = (FormWidget.ViewModel)requireContextEntry(FormTag.FORM_VIEW_MODEL_KEY);
		FormWidget form = (FormWidget)requireContextEntry(FormTag.FORM_KEY);

		//In case the tag is in formElement tag

		derivedId = id;
		if (derivedId == null && getContextEntry(FormElementTag.ID_KEY) != null) 
			derivedId = (String) getContextEntry(FormElementTag.ID_KEY);
		if (derivedId == null) throw new MissingFormElementIdAraneaJspException(this);   
		
		FormElement fe = ((FormElement)JspWidgetUtil.traverseToSubWidget(form, derivedId));
		fe.rendered();

		formElementViewModel = 
			(FormElement.ViewModel) fe._getViewable().getViewModel();   

		// Get control	
		controlViewModel = (formElementViewModel).getControl();
		localizedLabel = JspUtil.getResourceString(pageContext, formElementViewModel.getLabel());

		// We shall use the accesskey HTML attribute for this form element only if the attribute "accessKey" 
		// was explicitly set (otherwise in most common cases the label tag sets up the access key)
		if (accessKeyId != null) {
			accessKey = JspUtil.getResourceStringOrNull(pageContext, accessKeyId);
		}
		if (accessKey != null && accessKey.length() != 1) accessKey = null;

		if (hasElementContextSpan)
            writeFormElementContextOpen(out, formFullId, derivedId, true, pageContext);

		updateRegionNames = JspUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions, globalUpdateRegions);
		
		addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, this.getFullFieldId());
		
		backgroundValidation = fe.isBackgroundValidation();

		// Continue
		return EVAL_BODY_INCLUDE;		
	}


	protected int doEndTag(Writer out) throws Exception {
		if (hasElementContextSpan) {
			writeFormElementContextClose(out);
			writeFormElementValidityMarkers(out, formElementViewModel.isValid(), FORMELEMENT_SPAN_PREFIX + formFullId + "." + derivedId);
			writeFormElementValidationErrorMessages(out);
		}

		return super.doEndTag(out);
	}

	/**
	 * @since 1.1
	 */
	protected void writeFormElementValidationErrorMessages(Writer out) throws JspException, AraneaJspException, IOException {
		if (!formElementViewModel.isValid()) {
		    FormWidget form = (FormWidget)requireContextEntry(FormTag.FORM_KEY);
		    String errors = formElementViewModel.getFormElementValidationErrorRenderer().getClientRenderText(((FormElement)JspWidgetUtil.traverseToSubWidget(form, derivedId)));
		    out.write(errors);
		}
	}

	public void doFinally() {
		super.doFinally();
		formViewModel = null;
		formElementViewModel = null;
		controlViewModel = null;
		backgroundValidation = false;
	}

    /**
     * Writes out an attribute that correctly configures background validation for formelement rendered with this tag.
     * Note that when application wide validation settings are the same as for that formelement, no attribute is 
     * written out. 
     * 
     * IMPL: writes out arn-bgValidate='booleanvalue' when necessary. 
     * 
     * @since 1.1 */
    protected void writeBackgroundValidationAttribute(Writer out) throws Exception {
      if (this.backgroundValidation != 
            ConfigurationContextUtil.isBackgroundFormValidationEnabled(UilibEnvironmentUtil.getConfigurationContext(getEnvironment()))) {
        JspUtil.writeAttribute(out, AraneaAttributes.BACKGROUND_VALIDATION_ATTRIBUTE, String.valueOf(this.backgroundValidation));
      }
    }
	/* ***********************************************************************************
	 * Tag attributes
	 * ***********************************************************************************/

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
	 *   description = "Whether the element will send the events that are registered by server-side (by default 'true')."
	 */	
	public void setEvents(String events) throws JspException {
		this.events = ((Boolean)evaluateNotNull("events", events, Boolean.class)).booleanValue(); 
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
	 *   description = "HTML tabindex for the element."
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

	/**
	 * Computes field name.
	 */	
	protected String getFullFieldId() {
		return formFullId + "." + derivedId;
	}

	/**
	 * Asserts that associated control is of given type. If the
	 * condition does not hold, throws exception.
	 */
	protected void assertControlType(String type) throws JspException {
		if (!controlViewModel.getControlType().equals(type))
			throw new AraneaJspException("Control of type '" + type + "' expected in form element '" + derivedId + "' instead of '" + controlViewModel.getControlType() + "'");
	}


	public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId, PageContext pageContext) throws Exception{
		writeFormElementContextOpen(out, fullFormId, elementId, true, pageContext);
	}
	
  public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId, boolean isPresent, PageContext pageContext) throws Exception{
    writeFormElementContextOpen(out, fullFormId, elementId, isPresent, pageContext, FORMELEMENT_SPAN_PREFIX);
  }
	/** 
	 * Write a span with random id around the element, and register this span with javascript 
	 * (done by external behavior scripts, span functions as keyboard handler).
	 * Default implementation does not use any parameters except <code>Writer</code> and <code>PageContext</code>.
	 */

	public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId, boolean isPresent, PageContext pageContext, String idPrefix) throws Exception{
		//  Enclose the element in a <span id=someuniqueid>
		String spanId = idPrefix + fullFormId + "." + elementId;

		JspUtil.writeOpenStartTag(out, "span");
		JspUtil.writeAttribute(out, "id", spanId);

		// We'll also use the span around a form element for tracking keyboard events.
		// that is, the span will call our handler on a keypress.
		// Another, better way would be to set up some global keypress handler, but unfortunately 
		// that way it is too difficult to determine exactly which element was the target for the event.

		// All events are sent to a handler called "uiHandleKeypress(event, formElementId)"
		// We use the "keydown" event, not keypress, because this allows to
		// catch F2 in IE.
		// Actual onkeydown event is attached to span with behavioural javascript -- 
		// that also takes care of adding hidden element into DOM that indicates this
        // form element is present in request.		
		JspUtil.writeCloseStartTag(out);
	}

	/**
	 * Closes the span opened by writeFormElementContextOpen
	 * @param out
	 * @throws IOException
	 */
	public static void writeFormElementContextClose(Writer out) throws IOException{
		JspUtil.writeEndTag_SS(out, "span");
	}
	
	/** @since 1.1 */
	public static void writeFormElementValidityMarkers(Writer out, boolean valid, String spanId) throws Exception {
		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);

		out.write("Aranea.UI.markFEContentStatus(" + valid + ", $('" +spanId + "'));");

		JspUtil.writeEndTag_SS(out, "script");
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

	/**
	 * Writes event custom attributes and submit script for <i>attributeName</i>.  
	 */
	protected void writeSubmitScriptForUiEvent(Writer out, String attributeName, String id, String eventId, String precondition, List updateRegions) throws IOException {
        UiUpdateEvent event = new UiUpdateEvent(eventId, formFullId + "." + id, null, updateRegions);
        event.setEventPrecondition(precondition);
        JspUtil.writeEventAttributes(out, event);
        JspWidgetCallUtil.writeSubmitScriptForEvent(out, attributeName);
	}
	
	protected void writeSubmitScriptForUiEvent(Writer out, String attributeName) throws IOException {
		JspUtil.writeOpenAttribute(out, attributeName);
		JspWidgetCallUtil.writeSubmitScriptForEvent(out, attributeName);
		JspUtil.writeCloseAttribute(out);
	}

	protected String evaluateRenderMode(String renderMode) throws JspException {
      String resultRenderMode = (String) evaluateNotNull("renderMode",
          renderMode, String.class);

      if (!resultRenderMode.equals(RENDER_DISABLED_DISABLED)
          && !resultRenderMode.equals(RENDER_DISABLED_READONLY)) {

        throw new JspException("Valid options for the renderMode attribute are '"
            + RENDER_DISABLED_DISABLED + "' and '" + RENDER_DISABLED_READONLY
            + "'. The value '" + resultRenderMode + "' is not valid.");
      }

      return resultRenderMode;
  }

}
