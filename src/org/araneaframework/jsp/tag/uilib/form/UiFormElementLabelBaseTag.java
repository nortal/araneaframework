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
import org.araneaframework.jsp.UiMissingIdException;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Base form element label tag.
 * 
 * @author Oleg Mürk
 */
public class UiFormElementLabelBaseTag extends UiPresentationTag {
	protected FormWidget.ViewModel formViewModel;
	
	protected FormElement.ViewModel formElementViewModel;
	protected Control.ViewModel controlViewModel;
	protected String localizedLabel;		
	protected String accessKey;
	
	protected String id;
	protected boolean showMandatory;
	protected boolean showColon;
	protected String accessKeyId;
	
	protected void init() {
		super.init();
		id = null;
		showMandatory = true;
		showColon = true;
	}
	
	//
	// Attributes
	//
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Element id." 
	 */
	public void setId(String id) throws JspException {
		this.id = (String)evaluateNotNull("id", id, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Whether an asterisk is shown when the element is mandatory." 
	 */
	public void setShowMandatory(String showMandatory) throws JspException {
		this.showMandatory = ((Boolean)(evaluateNotNull("showMandatory", showMandatory, Boolean.class))).booleanValue();
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Whether a colon (":") is draw after the label." 
	 */
	public void setShowColon(String showColon) throws JspException {
		this.showColon = ((Boolean)(evaluateNotNull("showColumn", showColon, Boolean.class))).booleanValue();
	}
	
	/**
	 * Set the id of the resource containing the access key for this label.
	 * It may be null. In this case the default resource id is used: 
	 * 	&lt;label-id&gt;.access-key
	 * (where label-id is the resource id containing the label for the element.
	 *  if it exists).
	 * If such resource exists and specifies a single character, the 
	 * tag outputs an additional accesskey attribute for the HTML a &lt;label&gt; element.
	 * If given resource does not exist, it's also ok. Noone will die. 
	 */
	public void setAccessKeyId(String accessKeyId) throws JspException {
		this.accessKeyId = (String)evaluate("accessKeyId", accessKeyId, String.class);
	}
	
	//
	// Implementation
	//
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		// Get form data		
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
		localizedLabel = UiUtil.getResourceString(pageContext, controlViewModel.getLabel());    							
		
		if (accessKeyId == null) {
			
			// If controlViewModel.getLabel() did not specify a resource, we 
			// assume that controlViewModel.getLabel() + ".access-key" will also 
			// _not_ specify a legal resource.
			accessKey = UiUtil.getResourceStringOrNull(pageContext,  controlViewModel.getLabel() + ".access-key");
		}
		else {
			accessKey = UiUtil.getResourceStringOrNull(pageContext, accessKeyId);
		}
		if (accessKey != null && accessKey.length() != 1) accessKey = null;
		
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
}
