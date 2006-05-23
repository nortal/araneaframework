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

package org.araneaframework.jsp.tag.fileimport;

import java.io.Writer;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.filter.StandardServletFileImportFilterService;
import org.araneaframework.servlet.filter.importer.JsFileImporter;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 * @jsp.tag
 *   name = "importScripts"
 *   body-content="empty"
 *   description = "Imports js files"
 */
public class UiImportScriptsTag extends UiImportFileTag {
	
	public int doStartTag(Writer out) throws Exception {
		// if filename specified we include the file, if not we include all js files
		if (includeFileName != null) {
			writeHtmlInclude(out,
					JsFileImporter.INCLUDE_JS_FILE_KEY+"="+includeFileName);
		}
		else {
			writeHtmlInclude(out, 
					JsFileImporter.INCLUDE_JS_KEY+"=true");
		}
		
		// if no filename specified and includetemplates is specified we include the
		// template js files also
		if (includeFileName == null && includeTemplates != null) {
			writeHtmlInclude(out, 
					JsFileImporter.INCLUDE_TMPLT_JS_KEY+"=true");
		}
		return EVAL_BODY_INCLUDE;
	}
		
	protected void writeHtmlInclude(Writer out, String keyValue) throws Exception {
		StringBuffer buf = new StringBuffer(StandardServletFileImportFilterService.IMPORTER_TYPE_KEY);
		buf.append("=");
		buf.append(JsFileImporter.TYPE);
		buf.append("&");
		buf.append(keyValue);
		
		UiUtil.writeOpenStartTag(out, "script");
		UiUtil.writeAttribute(out, "language", "JavaScript1.2");
		UiUtil.writeAttribute(out, "type", "text/javascript");
		UiUtil.writeAttribute(out, "src", "?"+buf.toString(), false);
		UiUtil.writeCloseStartTag(out);
		UiUtil.writeEndTag(out, "script");
	}
}
