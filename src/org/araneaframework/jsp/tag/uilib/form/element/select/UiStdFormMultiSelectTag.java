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
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Standard multiselect form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "multiSelect"
 *   body-content = "JSP"
 *   description = "Form list input field, represents UiLib "MultiSelectControl"."
 */
public class UiStdFormMultiSelectTag extends UiFormElementBaseTag {
	
	protected void init() {
		super.init();
		size = null;
		baseStyleClass = "aranea-multi-select";
	}
	
//
	// Attributes
	//  
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Vertical size, number of options displayed." 
	 */
	public void setSize(String size) throws JspException {
		this.size = (Long)evaluate("size", size, Long.class);
	}
	
	//
	// Implementation
	//  
	
	protected int after(Writer out) throws Exception {
		// Type check
		assertControlType("MultiSelectControl");		
		
		// Prepare
		String name = this.getScopedFullFieldId(); 		
		MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);
		
		// Write input tag
		UiUtil.writeOpenStartTag(out, "select");
		UiUtil.writeAttribute(out, "id", name);
		UiUtil.writeAttribute(out, "name", name);
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeAttribute(out, "multiple", "true");
		UiUtil.writeAttribute(out, "size", size);		
		UiUtil.writeAttribute(out, "label", localizedLabel);
		UiUtil.writeAttribute(out, "tabindex", tabindex);
		if (viewModel.isOnChangeEventRegistered())
			this.writeEventAttributeForUiEvent(out, "onchange", id, "onChanged", validateOnEvent, "",
					updateRegionNames);
		if (viewModel.isDisabled())
			UiUtil.writeAttribute(out, "disabled", "true");
		UiUtil.writeAttributes(out, attributes);
		UiUtil.writeCloseStartTag(out);			
		
		for(Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
			DisplayItem item = (DisplayItem)i.next();
			String value = item.getValue();
			String label = item.getDisplayString();
			
			UiUtil.writeOpenStartTag(out, "option");			
			UiUtil.writeAttribute(out, "value", value != null ? value : "");
			if (viewModel.getValueSet().contains(value))
				UiUtil.writeAttribute(out, "selected", "true");
			UiUtil.writeCloseStartTag_SS(out);
			UiUtil.writeEscaped(out, label);
			UiUtil.writeEndTag(out, "option");
		}
		
		// Close tag
		UiUtil.writeEndTag_SS(out, "select");
		
		if (validate) writeValidationScript(out, viewModel);
		
		// Continue
		super.after(out);
		return EVAL_PAGE;	
	}
	
	protected Long size;
	
	/**
	 * Write validation javascript
	 * @author Konstantin Tretyakov
	 */
	protected void writeValidationScript(Writer out, MultiSelectControl.ViewModel viewModel) throws IOException {
		UiUtil.writeStartTag(out, "script");
		out.write("uiAddMultiselectValidator(");
		UiUtil.writeScriptString(out, getScopedFullFieldId());
		out.write(", ");
		UiUtil.writeScriptString(out, localizedLabel);
		out.write(", ");
		out.write(viewModel.isMandatory() ? "true" : "false");
		out.write(");\n");
		UiUtil.writeEndTag_SS(out, "script");
	}    
}
