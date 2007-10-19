/**
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.http.util.JsonObject;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard text input form element tag with rich text editor capabilities.
 * The tag uses a special style class denoted by EDITOR_SELECTOR which makes
 * it possible for the RTE library to locate the area and wrap it. 
 * 
 * @author Toomas Römer
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "richTextarea"
 *   body-content = "JSP"
 *   description = "Form text input field (textarea) wrapped with rich text editor capabilities."
 */
public class FormRichTextAreaHtmlTag extends FormTextareaHtmlTag{
	public static final String EDITOR_SELECTOR = "richTextEditor";
	private static final String MCE_JS = "js/tiny_mce/tiny_mce.js";
	
	protected String getStyleClass() throws JspException  {
		return EDITOR_SELECTOR;
	}

	protected int doStartTag(Writer out) throws Exception {
		ensureScriptLoaded(out);
		
		return super.doStartTag(out);
	}

	private void ensureScriptLoaded(Writer out) throws Exception  {
		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);

		String scriptFile = FileImportUtil.getImportString(MCE_JS, pageContext.getRequest());
		String scriptToExecute = "AraneaTinyMCEInit";
		String scriptLoadCondition = "AraneaTinyMCELoaded";
		String interval = "10";
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.setStringProperty("scriptFile", scriptFile);
		jsonObject.setProperty("scriptToExecute", scriptToExecute);
		jsonObject.setProperty("loadedCondition", scriptLoadCondition);
		jsonObject.setProperty("executionTryInterval", interval);

		out.write("Aranea.ScriptLoader.loadHeadScript("+ jsonObject.toString() + ");");

		JspUtil.writeEndTag(out, "script");
		out.write("\n");
	}
}
