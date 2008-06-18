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

package org.araneaframework.jsp.tag.uilib.head;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.basic.ElementHtmlTag;
import org.araneaframework.jsp.tag.uilib.form.element.text.FormRichTextAreaHtmlTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * The rich text editor (tinyMCE at the moment) requires a global
 * initialization and configuration. This tag initializes the editor
 * and configures it with default values. Additional configuration
 * options can be given via nested attribute tags.
 * 
 * @author Toomas Römer
 * 
 * @jsp.tag
 *   name = "richTextAreaInit"
 *   body-content = "JSP"
 *   description = "Initializes configures the richtext area component."
 */
public class RichTextAreaInitializationHtmlTag extends ElementHtmlTag {
	public static final String KEY = "org.araneaframework.jsp.tag.uilib.head.KEY";

	@Override
  protected int doStartTag(Writer out) throws Exception {
		setName("script");

		super.doStartTag(out);
		
		JspUtil.writeAttribute(out, "language", "javascript");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);
		out.write("var AraneaTinyMCEInit = function() { tinyMCE.init({\n");

		setDefaultSettings();
		
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Adds default settings to the configuration. All the options
	 * set in this method can be overridden via nested attribute tags.
	 */
	protected void setDefaultSettings() {
		attributes.put("editor_selector", FormRichTextAreaHtmlTag.EDITOR_SELECTOR);
		
		attributes.put("mode", "textareas");
		attributes.put("theme", "simple");
		attributes.put("strict_loading_mode", Boolean.TRUE);
	}

	@Override
  protected int doEndTag(Writer out) throws Exception {
		writeAttributes(out);

		out.write("}); " + "};\n");
		JspUtil.writeEndTag(out, "script");

		return EVAL_PAGE;
	}
	
	@Override
  protected void writeAttributes(Writer out) throws Exception {
		Iterator<Map.Entry<String, Object>> ite = attributes.entrySet().iterator();
		
		while(ite.hasNext()) {
			Map.Entry<String, Object> entry = ite.next();
			
			StringBuffer buf = new StringBuffer("\t");
			buf.append(entry.getKey());
			buf.append(" : ");
			buf.append(entry.getValue().toString());
			if (ite.hasNext())
				buf.append(",\n");
			
			out.write(buf.toString());
		}
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 */
	@Override
  public void setName(String name) throws JspException {
		super.setName(name);
	}
}
