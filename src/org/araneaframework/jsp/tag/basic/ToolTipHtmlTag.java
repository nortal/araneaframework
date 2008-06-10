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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @since 1.1
 * @jsp.tag
 *   name = "tooltip"
 *   body-content = "empty"
 *   description = 
 *   "Defines tooltip that is shown when end-user hovers over element to which the tooltip was attached."
 */
public class ToolTipHtmlTag extends BaseTag {
	protected String element;
	protected String text;
	protected String options;

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);

		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag_SS(out);
		out.write("new Tip($('" + element + "'), \"" + text + "\"");
		if (options != null)
		out.write(", " + options);
		out.write(");");
		JspUtil.writeEndTag(out, "script");
		return SKIP_BODY;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   rtexprvalue = "true"
	 *   required = "true"
     *   description = "HTML element id to which tooltip should be attached." 
	 */
	public void setElement(String element) throws JspException {
		this.element = (String) evaluate("element", element, String.class);
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
     *   rtexprvalue = "true"
	 *   required = "true"
	 *   description = "Tooltip content." 
	 */
	public void setText(String text) throws JspException {
		this.text = (String) evaluate("text", text, String.class);
	}
	
	 /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   rtexprvalue = "true"
   *   required = "false"
   *   description = "Options for tooltip (including tooltip classname, title, etc -- see prototip.js for details)." 
   */
	public void setOptions(String options) throws JspException {
	  this.options = (String) evaluate("options", options, String.class);
	}
}
