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

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiPresentationTag;


/**
 * Layout base tag.
 * 
 * @author Oleg Mürk
 */
public abstract class UiLayoutBaseTag extends UiPresentationTag implements UiLayoutTagInterface {
	protected String height;
	protected String width;

	private String rowClass;
	private String cellClass;

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Layout width." 
	 */
	public void setWidth(String width) throws JspException {
		this.width = (String)evaluate("width", width, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Layout height." 
	 */
	public void setHeight(String height) throws JspException {
		this.height = (String)evaluate("height", height, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Default style of rows in this layout." 
	 */
	public void setRowClass(String rowClass) throws JspException {
		this.rowClass = (String)evaluate("rowClass", rowClass, String.class);	
	}
		
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Default style of cells in this layout." 
	 */
	public void setCellClass(String cellClass) throws JspException {
		this.cellClass = (String)evaluate("cellClass", cellClass, String.class);
	}
  
  //
  // Implementation
  //
  		
	protected int before(Writer out) throws Exception {
		super.before(out);
		pushAttribute(UiLayoutTagInterface.KEY_REQUEST, this, PageContext.REQUEST_SCOPE);
		return EVAL_BODY_INCLUDE;
	}	
	
	protected void init() {
		super.init();
		
		height = null;
		width = null;
		rowClass = null;
		cellClass = null;		
	}
	
	protected String getCellClass() {
		return cellClass;
	}

  /**
   * Get row style or <code>null</code>.
   */
  protected String getRowClass() {
    return rowClass;
  }

  /**
   * Get default cell style.
   */
  protected String getDefaultCellStyle() throws JspException {
    return null;
  }

  /**
   * Get default row style.
   */
  protected String getDefaultRowStyle() throws JspException {
    return null;
  }	
}
