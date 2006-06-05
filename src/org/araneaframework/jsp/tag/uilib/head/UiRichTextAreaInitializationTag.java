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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.araneaframework.jsp.tag.basic.UiElementTag;
import org.araneaframework.jsp.tag.fileimport.UiImportScriptsTag;
import org.araneaframework.jsp.tag.uilib.form.element.text.UiStdFormRichTextAreaTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.filter.StandardServletFileImportFilterService;

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
public class UiRichTextAreaInitializationTag extends UiElementTag {
	public static final String KEY = "org.araneaframework.jsp.tag.uilib.head.KEY";
	private static final String MCE_JS = "js/tiny_mce/tiny_mce.js";
	private static final String MCE_DEBUG_JS = "js/tiny_mce/tiny_mce_src.js";
	
	protected int doStartTag(Writer out) throws Exception {
		UiImportScriptsTag.writeHtmlScriptsInclude(out, 
				StandardServletFileImportFilterService.IMPORTER_FILE_NAME+"="+MCE_DEBUG_JS,
				((HttpServletRequest)pageContext.getRequest()).getRequestURL());
		
		setName("script");
		
		super.doStartTag(out);
		
		UiUtil.writeAttribute(out, "language", "javascript");
		UiUtil.writeAttribute(out, "type", "text/javascript");
		UiUtil.writeCloseStartTag(out);
		out.write("tinyMCE.init({\n");
		
		setDefaultSettings();
		
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Adds default settings to the configuration. All the options
	 * set in this method can be overridden via nested attribute tags.
	 */
	protected void setDefaultSettings() {
		attributes.put("editor_selector", UiStdFormRichTextAreaTag.EDITOR_SELECTOR);
		
		attributes.put("mode", "textareas");
		attributes.put("theme", "simple");
	}

	protected int doEndTag(Writer out) throws Exception {
		writeAttributes(out);
		out.write("});\n");
		
		UiUtil.writeEndTag(out, "script");
		
		return EVAL_PAGE;
	}
	
	protected void writeAttributes(Writer out) throws Exception {
		Iterator ite = attributes.entrySet().iterator();
		
		while(ite.hasNext()) {
			Map.Entry entry = (Map.Entry)ite.next();
			
			StringBuffer buf = new StringBuffer("\t");
			buf.append(entry.getKey());
			buf.append(" : \"");
			buf.append(entry.getValue());
			buf.append("\"");
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
	public void setName(String name) throws JspException {
		super.setName(name);
	}
}
