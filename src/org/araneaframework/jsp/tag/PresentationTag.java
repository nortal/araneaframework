/*
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
 */

package org.araneaframework.jsp.tag;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;

/**
 * Attributed base tag.
 * 
 * @author Oleg Mürk
 */
public class PresentationTag extends BaseTag implements AttributedTagInterface {
	protected String style = null;
	protected String styleClass = null;
	protected String baseStyleClass = null;

	protected Map<String, Object> attributes = new HashMap<String, Object>();
	
	@Override
  protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		addContextEntry(AttributedTagInterface.ATTRIBUTED_TAG_KEY, this);
		addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
  public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
	}

	/**
	 * Callback: add attribute.
	 */
	public void addAttribute(String name, String value){
		if (value == null)
			attributes.remove(name);
		else
			attributes.put(name, evaluate("value", value, Object.class));
	}

	// Styles
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 *   description = "Inline style for HTML tag."
	 */
	public void setStyle(String style){
		this.style = evaluate("style", style, String.class);
	}
	
	public String getStyle() {
		return this.style;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 *   description = "CSS class for the tag."
	 */
	public void setStyleClass(String styleClass) {
		if (styleClass != null)
			this.styleClass = evaluate("styleClass", styleClass, String.class);
	}

	/**
	 * Callback: get default css class for tag or <code>null</code>.
	 */
	protected String getStyleClass() {
		return calculateStyleClass(baseStyleClass, styleClass);
	}

	/** @since 1.1 */
	public static final String calculateStyleClass(String baseStyleClass, String styleClass) {
		StringBuffer result = new StringBuffer();
		if (baseStyleClass != null) {
			result.append(baseStyleClass);
			if (styleClass != null)
				result.append(" ");
		}
		if (styleClass != null) {
			result.append(styleClass);
		}

		return result.length() == 0 ? null : result.toString();
	}
}
