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

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.UiUtil;


/**
 * Standard button link form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "linkButton"
 *   body-content = "JSP"
 *   description = "HTML link, represents UiLib "ButtonControl"."
 */
public class UiStdFormLinkButtonTag extends UiStdFormButtonBaseTag {
	
	protected void init() {
		super.init();
		baseStyleClass = "aranea-link";
	}
	
	//
	// Implementation
	//  
	
	protected int before(Writer out) throws Exception {
		super.before(out);

		// Write button tag             
		UiUtil.writeOpenStartTag(out, "a");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeAttribute(out, "id", this.getScopedFullFieldId());
		UiUtil.writeAttribute(out, "href", "javascript:");    
		UiUtil.writeAttribute(out, "label", localizedLabel);
		UiUtil.writeAttribute(out, "tabindex", tabindex);
		
		if (events) {
			writeEventAttribute(out);
		}

		UiUtil.writeAttributes(out, attributes);
		UiUtil.writeCloseStartTag_SS(out);
		
		// Continue
		return EVAL_BODY_INCLUDE;    
	}    
	
	protected int after(Writer out) throws Exception {
		if (showLabel)
			UiUtil.writeEscaped(out, localizedLabel);

		UiUtil.writeEndTag(out, "a");
		
		// Continue
		super.after(out);
		return EVAL_PAGE;
	}  
	
	protected boolean writeEventAttribute(Writer out) throws IOException, JspException {
		if (viewModel.isOnClickEventRegistered())
			this.writeEventAttributeForUiEvent(out, "onclick", id, "onClicked", validateOnEvent, onClickPrecondition,
					updateRegionNames);

		return viewModel.isOnClickEventRegistered();
	}
}
