/**
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.jsp.tag.uilib.tab;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.uilib.tab.TabContainerWidget;

/**
 * @jsp.tag 
 *  name = "tabs" 
 * 	body-content = "empty" 
 *  description = "Writes out tabs' labels and the active tab content."
 * 
 *  @author Taimo Peelo (taimo@araneaframework.org)
 *  
 *  @see TabContainerWidget
 *  @see TabContainerHtmlTag
 *  @see TabBodyTag
 *  
 *  @since 1.1
 */
public class TabsHtmlTag extends BaseTag {
	protected String id;
	
	protected int doStartTag(Writer out) throws Exception {
		TabContainerHtmlTag tabContainerHtmlTag = new TabContainerHtmlTag();
		tabContainerHtmlTag.setId(id);
		registerAndExecuteStartTag(tabContainerHtmlTag);
		
		TabBodyTag tabBodyTag = new TabBodyTag();
		registerAndExecuteStartTag(tabBodyTag);
		executeEndTagAndUnregister(tabBodyTag);

		executeEndTagAndUnregister(tabContainerHtmlTag);
		
		return SKIP_BODY;
	}

	protected int doEndTag(Writer out) throws Exception {
		return super.doEndTag(out);
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
     *   rtexprvalue = "true"
	 *   required = "true"
	 *   description = "Id of Uilib TabContainerWidget" 
	 */
	public void setId(String id) throws JspException {
		this.id = (String) evaluateNotNull("id", id, String.class);
	}
}
