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

import javax.servlet.http.HttpServletRequest;

import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.filter.StandardServletFileImportFilterService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 * @jsp.tag
 *   name = "importScripts"
 *   body-content="empty"
 *   description = "Imports js files"
 */
public class UiImportScriptsTag extends UiImportFileTag {
	public static final String DEFAULT_GROUP_NAME = "defaultScripts";
	
	public int doStartTag(Writer out) throws Exception {
		// if filename specified we include the file
		if (includeFileName != null) {
			writeContent(out,
					StandardServletFileImportFilterService.IMPORTER_FILE_NAME+"="+includeFileName);
		}
		else if (includeGroupName != null) {
			writeContent(out, 
					StandardServletFileImportFilterService.IMPORTER_GROUP_NAME+"="+includeGroupName);
		}
		else {
			writeContent(out, 
					StandardServletFileImportFilterService.IMPORTER_GROUP_NAME+"="+DEFAULT_GROUP_NAME);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	protected void writeContent(Writer out, String keyValue) throws Exception {
		StringBuffer url = ((HttpServletRequest)pageContext.getRequest()).getRequestURL();
		writeHtmlScriptsInclude(out, keyValue, url);
		out.write("\n");
	}
	
	public static void writeHtmlScriptsInclude(Writer out, String keyValue, StringBuffer prefix) throws Exception {
		UiUtil.writeOpenStartTag(out, "script");
		UiUtil.writeAttribute(out, "language", "JavaScript1.2");
		UiUtil.writeAttribute(out, "type", "text/javascript");
		UiUtil.writeAttribute(out, "src", prefix.append("?").append(keyValue), false);
		UiUtil.writeCloseStartTag(out);
		UiUtil.writeEndTag(out, "script");
	}
}
