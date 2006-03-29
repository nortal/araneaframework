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

package org.araneaframework.jsp.tag.presentation;     

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard link tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "link"
 *   body-content = "JSP"
 *   description = "Usual HTML link, acts as a <i>&lt;a&gt;</i> HTML tag."
 */
public class UiStdLinkTag extends UiLinkBaseTag {
	private String disabledStyleClass;
	
	/// attributes
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "CSS class for disabled link" 
	 */
	public void setDisabledStyleClass(String disabledCssClass) throws JspException {
		this.disabledStyleClass = (String)this.evaluate("disabledStyleClass", disabledCssClass, String.class);
	}

	public String getDisabledStyleClass() {
		return disabledStyleClass;
	}
	
	
	protected void init() {
		super.init();
		styleClass = "aranea-link-button"; 
		disabledStyleClass = "aranea-disabled-link-button";
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		UiUtil.writeOpenStartTag(out, "a");
		UiUtil.writeAttribute(out, "id", id);
		UiUtil.writeAttribute(out, "class", !disabled ? getStyleClass() : getDisabledStyleClass());
		if (!disabled) {
			UiUtil.writeAttribute(out, "href", href);
			UiUtil.writeAttribute(out, "target", target);   
		}
		else
			UiUtil.writeAttribute(out, "href", "javascript:");
		
		UiUtil.writeCloseStartTag_SS(out);
		
		// Continue
		return EVAL_BODY_INCLUDE;
	}
	
	protected int after(Writer out) throws Exception {  
		UiUtil.writeEndTag_SS(out, "a");

		// Continue
		super.after(out);
		return EVAL_PAGE;      
	}
}

