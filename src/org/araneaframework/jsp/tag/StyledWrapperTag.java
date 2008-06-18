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

package org.araneaframework.jsp.tag;

import javax.servlet.jsp.JspException;


/**
 * Styled wrapper tag.
 * 
 * @author Oleg Mürk
 */
public abstract class StyledWrapperTag extends BaseTagWrapperTag  {
	protected String styleClass;
	
	public StyledWrapperTag() {
		this.styleClass = null;
	}
	
	/**
	 * Callback: configure row tag
	 */
	@Override
  protected void configureTag(ContainedTagInterface tag) throws JspException {
		StyledTagInterface styledTag = (StyledTagInterface)tag; 
		styledTag.setStyleClass(styleClass);
	}

	/* ***********************************************************************************
	 * Tag attributes
	 * ***********************************************************************************/

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Cell css class, defines the way the cell will be rendered." 
	 */
	public void setStyleClass(String styleClass){
		this.styleClass = evaluate("styleClass", styleClass, String.class);
	}
}
