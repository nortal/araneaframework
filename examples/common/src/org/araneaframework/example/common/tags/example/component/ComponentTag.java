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

package org.araneaframework.example.common.tags.example.component;

import java.io.Writer;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @jsp.tag
 *   name = "component"
 *   body-content = "JSP"
 */
public class ComponentTag extends PresentationTag {
	public final static String KEY= "example.component.key";
	public final static String DEFAULT_COMPONENT_STYLE = "component";
	public final static String DEFAULT_COMPONENT_WIDTH_STYLE = "w100p";
	
	protected String widthClass;

	public ComponentTag() {
		styleClass = ComponentTag.DEFAULT_COMPONENT_STYLE;
		widthClass = ComponentTag.DEFAULT_COMPONENT_WIDTH_STYLE;
	}

	@Override
  protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		addContextEntry(ComponentTag.KEY, this);

		JspUtil.writeOpenStartTag(out, "div");
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeCloseStartTag(out);
		
		// second div... maybe should be moved out
		JspUtil.writeOpenStartTag(out, "div");
		JspUtil.writeAttribute(out, "class", widthClass);
		JspUtil.writeCloseStartTag(out);

		return EVAL_BODY_INCLUDE;
	}

	@Override
  protected int doEndTag(Writer out) throws Exception {
		JspUtil.writeEndTag(out, "div");
		JspUtil.writeEndTag(out, "div");
		super.doEndTag(out);
		return EVAL_PAGE;
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 *   description = "CSS class for secondary DIV that is written out by this tag. 
	 *   By default this is w100p - maximum component width is set."
	 */
	public void setWidthClass(String widthClass) {
		this.widthClass = evaluate("widthClass", widthClass, String.class);
	}
}
