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

package org.araneaframework.jsp.tag.layout;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiContainedTagInterface;
import org.araneaframework.jsp.tag.UiStyledWrapperTag;


/**
 * Layout row wrapper tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "row"
 *   body-content = "JSP"
 *   description = "Represents a row in layout."
 */
public class UiLayoutRowWrapperTag extends UiStyledWrapperTag  {
	protected String height;
	protected String cellClass;
	
	protected void init() {
		super.init();
		this.height = null;
	}
	
	
	//
	// Attributes
	//
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Row height." 
	 */
	public void setHeight(String height) throws JspException {
		this.height = (String)evaluate("height", height, String.class);;
	}	
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Default style of cells in this row." 
	 */
	public void setCellClass(String cellClass) throws JspException {
		this.cellClass = (String)evaluate("cellClass", cellClass, String.class);
	}
	
	//
	// Implementation
	//	
	
	/**
	 * Callback: get row tag
	 */
	protected UiContainedTagInterface getTag() throws JspException {
		UiLayoutTagInterface layout = (UiLayoutTagInterface)readAttribute(UiLayoutTagInterface.KEY_REQUEST, PageContext.REQUEST_SCOPE);
		UiLayoutRowTagInterface tag = layout.getRowTag(layout.getRowClass());
		if (layout.getCellClass() != null) tag.setCellClass(layout.getCellClass());
		return tag;	
	}
	
	/**
	 * Callback: configure row tag
	 */	
	protected void configureTag(UiContainedTagInterface tag) throws JspException {	
		super.configureTag(tag);
		
		UiLayoutRowTagInterface rowTag = (UiLayoutRowTagInterface)tag;
		
		if (height != null) rowTag.setHeight(height);
		if (cellClass != null) rowTag.setCellClass(cellClass);
	}
}
