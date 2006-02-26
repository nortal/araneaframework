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
import org.araneaframework.jsp.tag.layout.UiLayoutCellTagInterface;
import org.araneaframework.jsp.tag.layout.UiLayoutRowBaseTag;
import org.araneaframework.jsp.tag.layout.UiStdLayoutCellTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * SAMPLE container footer tag.
 * 
 * @author Oleg Mürk
 * @author Taimo Peelo (taimo@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "containerFooter"
 *   body-content = "JSP"
 */
public class SampleContainerFooterTag extends UiLayoutRowBaseTag {
	protected boolean inTable = true;
	
	protected void init() {
		super.init();
		styleClass = "template-container-footer";
	}	
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		if (inTable) UiUtil.writeStartTag(out, "table");
		UiUtil.writeStartTag(out, "tr");
		UiUtil.writeStartTag(out, "td");
		
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);
		
		return EVAL_BODY_INCLUDE;			
	}		
	
	protected int after(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "div");
		UiUtil.writeEndTag(out, "td");
		UiUtil.writeEndTag(out, "tr");
		if (inTable) UiUtil.writeEndTag(out, "table");
		
		super.after(out);
		return EVAL_PAGE;
	}
	
	public UiLayoutCellTagInterface getCellTag(String style) throws JspException {
		return new UiStdLayoutCellTag();
	}
}
