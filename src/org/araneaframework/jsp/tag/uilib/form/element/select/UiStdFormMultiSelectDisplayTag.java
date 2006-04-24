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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseDisplayTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "multiSelectDisplay"
 *   body-content = "JSP"
 *   description = "Form multiselect display field, represents UiLib "MultiSelectControl"."
 */
public class UiStdFormMultiSelectDisplayTag extends UiFormElementBaseDisplayTag {
	protected static final String NEWLINE_SEPARATOR_CODE ="\\n";
	
	protected String separator;
	
	protected void init() {
		super.init();
		styleClass = "aranea-multi-select-display";
		separator = ",&nbsp;";
	}	
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The separator between list items, can be any string and '\n', meaning a newline (by default ', ')." 
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}    
	
	
	//
	// Implementation
	//
	
	protected int doEndTag(Writer out) throws Exception {				
		MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);
		
		UiUtil.writeOpenStartTag(out, "span");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);
		
		List selectedItems = new ArrayList(viewModel.getSelectItems());
		for (Iterator i = selectedItems.iterator(); i.hasNext();) {
			DisplayItem displayItem = (DisplayItem) i.next();
			if (!viewModel.getValueSet().contains(displayItem.getValue())) i.remove();
		}
		
		for (Iterator i = selectedItems.iterator(); i.hasNext();) {
			DisplayItem displayItem = (DisplayItem) i.next();
			
			UiUtil.writeEscaped(out, displayItem.getDisplayString());
			if (i.hasNext()) writeSeparator(out);
		}
		
		return super.doEndTag(out);  
	}
	
	protected void writeSeparator(Writer out) throws IOException, UiException {
		if (NEWLINE_SEPARATOR_CODE.equals(separator))      
			UiUtil.writeStartEndTag(out, "br");
		else 
			out.write(separator);      
	}	
}
