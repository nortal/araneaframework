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

package org.araneaframework.example.common.tags.example.component;

import java.io.Writer;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @jsp.tag
 *   name = "componentActions"
 *   body-content = "JSP"
 *   description = "Submit buttons should be put inside."
 */
public class ComponentActionsTag extends PresentationTag {
	public final static String COMPONENT_ACTION_STYLE_CLASS = "actions";

	public ComponentActionsTag() {
		styleClass = ComponentActionsTag.COMPONENT_ACTION_STYLE_CLASS;
	}

	@Override
  protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		JspUtil.writeOpenStartTag(out, "div");
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeCloseStartTag(out);
		
		return EVAL_BODY_INCLUDE;
	}

	@Override
  protected int doEndTag(Writer out) throws Exception {
		JspUtil.writeEndTag(out, "div");
		super.doEndTag(out);
		return EVAL_PAGE;
	}
}
