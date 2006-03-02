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
 * Layout row base tag.
 * 
 * @author Oleg Mürk
 */
public abstract class UiLayoutRowBaseTag extends UiPresentationTag implements UiLayoutRowTagInterface {
	protected String height;
	private String cellClass;

	public void setHeight(String height) throws JspException {
		this.height = (String)evaluate("height", height, String.class);
	}
	
	public void setCellClass(String cellClass) throws JspException {
		this.cellClass = (String)evaluate("cellClass", cellClass, String.class);
	}
	
	public String getCellClass() {
		return cellClass;
	}
	
	//
	// Implementation
	//
	
	protected int before(Writer out) throws Exception {
		super.before(out);	
		pushAttribute(UiLayoutRowTagInterface.KEY_REQUEST, this, PageContext.REQUEST_SCOPE);
		return EVAL_BODY_INCLUDE;
	}	
	
	protected void init() {
		super.init();
		
		height = null;
		cellClass = null;		
	}
}
