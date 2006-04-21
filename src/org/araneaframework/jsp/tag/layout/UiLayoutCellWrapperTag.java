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
import org.araneaframework.jsp.tag.UiContainedTagInterface;
import org.araneaframework.jsp.tag.UiStyledWrapperTag;


/**
 * Layout cell wrapper tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "cell"
 *   body-content = "JSP"
 *   description = "Represents a cell in layout."
 */
public class UiLayoutCellWrapperTag extends UiStyledWrapperTag {
	protected String width;
	protected String height;
	
	protected String colSpan;
	protected String rowSpan;
	
	protected void init() {
		super.init();
		
		width = null;
		height = null;
		
		colSpan = null;
		rowSpan = null;
	} 
	
	//
	// Attributes
	//
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Cell width." 
	 */
	public void setWidth(String width) throws JspException {
		this.width = width;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Cell height." 
	 */
	public void setHeight(String height) throws JspException {
		this.height = height;
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Cell colspan, same as in HTML." 
	 */
	public void setColSpan(String colSpan) throws JspException {
		this.colSpan = colSpan;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Cell rowspan, same as in HTML." 
	 */
	public void setRowSpan(String rowSpan) throws JspException {
		this.rowSpan = rowSpan;
	}
	
	//
	// Implementation
	//
	
	/**
	 * Callback: get cell tag
	 */
	protected UiContainedTagInterface getTag() throws JspException {
		UiLayoutRowTagInterface row = (UiLayoutRowTagInterface)requireContextEntry(UiLayoutRowTagInterface.KEY_REQUEST);
		//UiLayoutTagInterface layout = (UiLayoutTagInterface)readAttribute(UiLayoutTagInterface.KEY_REQUEST, PageContext.REQUEST_SCOPE);
		return row.getCellTag(row.getCellClass());
	}
	
	/**
	 * Callback: configure tag
	 */
	protected void configureTag(UiContainedTagInterface tag) throws JspException {
		super.configureTag(tag);

		UiLayoutCellTagInterface cellTag = (UiLayoutCellTagInterface)tag; 
		
		if (width != null) cellTag.setWidth(width);
		if (height != null) cellTag.setHeight(height);				
		if (colSpan != null) cellTag.setColSpan(colSpan);
		if (rowSpan != null) cellTag.setRowSpan(rowSpan);
		if (styleClass != null) cellTag.setStyleClass(styleClass);
	}
}
