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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.basic.UiAttributedTagInterface;

/**
 * Attributed base tag.
 * 
 * @author Oleg Mürk
 */
public class UiPresentationTag extends UiBaseTag implements UiAttributedTagInterface {
	protected String styleClass = null;
	protected Map attributes;
	
	public UiPresentationTag() {
		attributes = new HashMap();
	}

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);

		// Register
		addContextEntry(UiAttributedTagInterface.ATTRIBUTED_TAG_KEY_REQUEST, this);

		// Continue
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Callback: add attribute.
	 */
	public void addAttribute(String name, String value) throws JspException {
		attributes.put(name, evaluate("value", value, Object.class));
	}

	// Styles 
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 *   description = "CSS class for tag"
	 */
	public void setStyleClass(String styleClass) throws JspException {
		if (styleClass != null)
			this.styleClass = (String) evaluate("styleClass", styleClass, String.class);
	}

	/**
	 * Callback: get default css class for tag or <code>null</code>.
	 */
	protected String getStyleClass()  {
		return styleClass;
	}
}
