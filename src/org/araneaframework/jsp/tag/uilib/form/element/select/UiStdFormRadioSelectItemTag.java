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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.SelectControl;


/**
 * Standard radio select item form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "radioSelectItem"
 *   body-content = "JSP"
 *   description = "Form radio button, represents one item from UiLib "SelectControl"."
 */
public class UiStdFormRadioSelectItemTag extends UiFormElementBaseTag {
	protected void init() {
		super.init();
		value = null;
		onChangePrecondition = "return true;";
		styleClass = "aranea-radio";
	}	
	
	//
	// Attributes
	//  
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The value of this radio button that will be submitted with form if this radio button is selected." 
	 */
	public void setValue(String value) throws JspException  {
		this.value = (String)evaluateNotNull("value", value, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Precondition for deciding whether go to server side or not." 
	 */
	public void setOnChangePrecondition(String onChangePrecondition) throws JspException {
		this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
	}
	
	//
	// Implementation
	//  
	
	protected int after(Writer out) throws Exception {
		// Type check
		assertControlType("SelectControl");		
		
		// Prepare
		String name = this.getScopedFullFieldId(); 		
		SelectControl.ViewModel viewModel = ((SelectControl.ViewModel)controlViewModel);
		
		// Write input tag
		String selectedValue = viewModel.getSimpleValue();
		boolean selected = (selectedValue == null && value == null) ||
		(selectedValue != null && selectedValue.equals(value));
		
		if (!viewModel.containsItem(value)) {
			throw new UiException("Value '"+value+"' not found in values list.");
		}
		
		
		
		UiUtil.writeOpenStartTag(out, "input");
		UiUtil.writeAttribute(out, "name", name);
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeAttribute(out, "type", "radio");    
		UiUtil.writeAttribute(out, "label", localizedLabel);
		UiUtil.writeAttribute(out, "value", value);
		UiUtil.writeAttribute(out, "tabindex", tabindex);
		if (viewModel.isDisabled() || viewModel.getSelectItemByValue(value).isDisabled())
			UiUtil.writeAttribute(out, "disabled", "true");
		if (selected)
			UiUtil.writeAttribute(out, "checked", "true");    
		if (events && viewModel.isOnChangeEventRegistered())
			this.writeEventAttributeForUiEvent(out, "onclick", id, "onChanged", validateOnEvent, onChangePrecondition,
					updateRegionNames);
		UiUtil.writeAttributes(out, attributes);
		UiUtil.writeCloseStartEndTag_SS(out);
		
		if (validate) writeValidationScript(out, viewModel);
		
		// Continue
		super.after(out);
		return EVAL_PAGE;	
	}
	
	protected String value;
	protected String onChangePrecondition;
	
	/**
	 * Write validation javascript
	 * @author Konstantin Tretyakov
	 */
	protected void writeValidationScript(Writer out, SelectControl.ViewModel viewModel) throws IOException {
		UiUtil.writeStartTag(out, "script");
		out.write("uiAddRadioValidator(");
		UiUtil.writeScriptString(out, getScopedFullFieldId());
		out.write(", ");
		UiUtil.writeScriptString(out, localizedLabel);
		out.write(", ");
		out.write(viewModel.isMandatory() ? "true" : "false");
		out.write(");\n");
		UiUtil.writeEndTag_SS(out, "script");
	}      
}
