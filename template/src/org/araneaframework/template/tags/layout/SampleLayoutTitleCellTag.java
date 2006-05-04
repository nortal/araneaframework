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

package org.araneaframework.template.tags.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.layout.UiLayoutCellWrapperTag;
import org.araneaframework.jsp.util.UiUtil;


/**
 * SAMPLE layout title cell tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "titleCell"
 *   body-content = "JSP"
 */
public class SampleLayoutTitleCellTag extends UiLayoutCellWrapperTag {
	protected String titleStyle;

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", "template-layout");
		UiUtil.writeCloseStartTag(out);
		
		UiUtil.writeStartTag(out, "table");
		UiUtil.writeStartTag(out, "tr");
		
		UiUtil.writeStartTag(out, "td");
		
		return EVAL_BODY_INCLUDE;			
	}		
	
	protected int doEndTag(Writer out) throws Exception {	
		UiUtil.writeEndTag(out, "td");
		UiUtil.writeEndTag(out, "tr");
		UiUtil.writeEndTag(out, "table");
		UiUtil.writeEndTag(out, "div");
		
		return super.doEndTag(out);
	}
	
	public void setTitleStyle(String titleStyle) throws JspException {
		this.titleStyle = (String)evaluate("titleStyle", titleStyle, String.class);
	}
	
	public String getStyle(String prefix) {
		return null;
	}
}
