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

package org.araneaframework.template.tags.container;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.layout.UiLayoutBaseTag;
import org.araneaframework.jsp.tag.layout.UiLayoutRowTagInterface;
import org.araneaframework.jsp.tag.layout.UiStdLayoutRowTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Template container form body tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "containerFormBody"
 *   body-content = "JSP"
 */
public class SampleContainerFormBodyTag extends UiLayoutBaseTag {
	public final static String STYLE_CLASS = "template-container-form-body";
	
	protected void init() {
		super.init();
		styleClass = SampleContainerFormBodyTag.STYLE_CLASS;
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		UiUtil.writeStartTag(out, "tr");
		UiUtil.writeStartTag(out, "td");
		UiUtil.writeOpenStartTag(out, "table");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);

		return EVAL_BODY_INCLUDE;
	}		
	
	protected int after(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "table");
		UiUtil.writeEndTag(out, "td");
		UiUtil.writeEndTag(out, "tr");
		
		super.after(out);
		return EVAL_PAGE;     
	}

	public UiLayoutRowTagInterface getRowTag(String style) throws JspException {
		return new UiStdLayoutRowTag(getStyleClass(), getCellClass());
	} 
}
