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

import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.UiMissingIdException;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Base form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class UiFormElementBaseDisplayTag extends UiPresentationTag implements UiFormElementTagInterface {
	
	protected FormWidget.ViewModel formViewModel;
	
	protected FormElement.ViewModel formElementViewModel;
	protected	Control.ViewModel controlViewModel;
	
	protected String id;	
	protected boolean events;
	protected boolean validate;
	protected boolean validateOnEvent;
	protected String tabindex;
	
	protected void init() {
		super.init();
		
		this.id = null;
		this.events = true;
		this.validate = true;
		this.validateOnEvent = false;
		this.tabindex = null;
	}
	
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
	
	public void setEvents(String events) throws JspException {
		this.events = ((Boolean)evaluateNotNull("events", events, Boolean.class)).booleanValue(); 
	}
	
	public void setValidate(String validate) throws JspException {
		this.validate = ((Boolean)evaluateNotNull("validate", validate, Boolean.class)).booleanValue(); 
	}
	
	public void setValidateOnEvent(String validateOnEvent) throws JspException {
		this.validateOnEvent = ((Boolean)evaluateNotNull("validateOnEvent", validateOnEvent, Boolean.class)).booleanValue(); 
	}
	
	public void setTabindex(String tabindex) throws JspException {
		this.tabindex = (String)evaluateNotNull("tabindex", tabindex, String.class);
	}
	
	public void setUpdateRegions(String updateRegions) throws JspException {
	}
	
	public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException {
	}  
	
	//
	// Implementation
	//
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		formViewModel = (FormWidget.ViewModel)requireContextEntry(UiFormTag.FORM_VIEW_MODEL_KEY_REQUEST);
		FormWidget form = 
			(FormWidget)UiUtil.requireContextEntry(pageContext, UiFormTag.FORM_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		//In case the tag is in formElement tag
		if (id == null && getContextEntry(UiFormElementTag.ID_KEY_REQUEST) != null)
			id = (String) getContextEntry(UiFormElementTag.ID_KEY_REQUEST);
		
		if (id == null) throw new UiMissingIdException(this);
		
		formElementViewModel = 
			(FormElement.ViewModel) UiWidgetUtil.traverseToSubWidget(form, id)._getViewable().getViewModel();   
		
		// Get control	
		controlViewModel = (formElementViewModel).getControl();
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
	
	
	protected int doEndTag(Writer out) throws Exception {
		// Continue
		super.doEndTag(out);
		return EVAL_PAGE;
	}
	
	/**
	 * Asserts that associated control is of given type. If the
	 * condition does not hold, throws exception.
	 */
	protected void assertControlType(String type) throws JspException {
		if (!controlViewModel.getControlType().equals(type))
			throw new UiException("Control of type '" + type + "' expected in form element '" + id + "' instead of '" + controlViewModel.getControlType() + "'");
	}
}
