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

import java.io.Writer;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Standard select form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "checkboxMultiSelect"
 *   body-content = "JSP"
 *   description = "Form multiselect checkbox field, represents UiLib 'MultiSelectControl'."
 */
public class UiStdFormCheckboxMultiSelectTag extends UiFormElementBaseTag {
	protected String type;
	protected boolean labelBefore;
	
	protected void init() {
		super.init();
		setHasElementContextSpan(false);
		type = "horizontal";
		labelBefore = false;
	} 
	
	//
	// Attributes
	//  
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The way the checkboxes will be rendered - can be either "vertical" or "horizontal". By default "horizontal"." 
	 */
	public void setType(String type) throws JspException {
		this.type = (String)evaluate("type", type, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Boolean that controls whether label is before or after each checkbox. False by default." 
	 */
	public void setLabelBefore(String labelBefore) throws JspException {
		this.labelBefore = ((Boolean) evaluateNotNull("labelBefore", labelBefore, Boolean.class)).booleanValue();
	}
	
	//
	// Implementation
	//  
	
	public int doEndTag(Writer out) throws Exception {
		// Type check
		assertControlType("MultiSelectControl");		
		
		if (!"horizontal".equals(type) && !"vertical".equals(type))
			throw new UiException("Attribute 'type' cna be only either 'horizontal' or 'vertical'!");
		
		// Prepare
		MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);
		
		UiStdFormCheckboxMultiSelectItemLabelTag label = new UiStdFormCheckboxMultiSelectItemLabelTag();
		UiStdFormCheckboxMultiSelectItemTag item = new UiStdFormCheckboxMultiSelectItemTag();
		
		for (Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
			DisplayItem displayItem = (DisplayItem) i.next();
			
			if (labelBefore) writeLabel(label, id, displayItem.getValue());
			
			registerSubtag(item);
			
			item.setId(id);
			item.setValue(displayItem.getValue());
			item.setEvents(events ? "true" : "false");
			item.setValidate(validate ? "true" : "false");
			item.setValidateOnEvent(validateOnEvent ? "true" : "false");
			
			if(updateRegions != null)
				item.setUpdateRegions(updateRegions);
			if(globalUpdateRegions != null)
				item.setGlobalUpdateRegions(globalUpdateRegions);
			item.setStyleClass(styleClass);
			
			if(tabindex != null)
				item.setTabindex(tabindex);				
			
			executeStartSubtag(item);
			executeEndTagAndUnregister(item);
			
			if (!labelBefore) writeLabel(label, id, displayItem.getValue());
			
			if ("horizontal".equals(type))
				out.write("&nbsp;");
			else if ("vertical".equals(type))
				UiUtil.writeStartEndTag(out, "br");
		}
		
		// Continue
		super.doEndTag(out);
		return EVAL_PAGE;	
	}
	
	protected void writeLabel(UiStdFormCheckboxMultiSelectItemLabelTag label, String id, String value) throws JspException {
		registerSubtag(label);
		label.setId(id);
		label.setValue(value);
		executeStartSubtag(label);
		executeEndTagAndUnregister(label);
	}
}
