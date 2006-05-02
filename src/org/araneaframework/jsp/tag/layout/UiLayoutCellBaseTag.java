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
import org.araneaframework.jsp.tag.UiPresentationTag;


/**
 * Layout cell base tag.
 * 
 * @author Oleg Mürk
 */
public class UiLayoutCellBaseTag extends UiPresentationTag implements UiLayoutCellTagInterface {
	protected String width = null;
	protected String height = null;
	
	protected Long colSpan = null;
	protected Long rowSpan = null;
	
	public void setWidth(String width) throws JspException {
		this.width = (String)evaluate("width", width, String.class);
	}
	
	public void setHeight(String height) throws JspException {
		this.height = (String)evaluate("height", height, String.class);
	}
	
	public void setColSpan(String colSpan) throws JspException {
		this.colSpan = (Long)evaluate("colSpan", colSpan, Long.class);
	}
	
	public void setRowSpan(String rowSpan) throws JspException {
		this.rowSpan = (Long)evaluate("rowSpan", rowSpan, Long.class);
	}
}
