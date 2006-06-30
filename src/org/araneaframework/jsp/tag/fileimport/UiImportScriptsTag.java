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
import org.araneaframework.servlet.filter.importer.FileImporter;

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
			writeContent(out, includeFileName);
		}
		// if groupname specified we include the group
		else if (includeGroupName != null) {
			writeContent(out, includeGroupName);
		}
		// we default to the default group name
		else {
			writeContent(out, DEFAULT_GROUP_NAME);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	protected void writeContent(Writer out, String srcFile) throws Exception {
		writeHtmlScriptsInclude(out, FileImporter.getImportString(srcFile, pageContext.getRequest(),
				pageContext.getResponse()));
		out.write("\n");
	}
	
	public static void writeHtmlScriptsInclude(Writer out, String srcFile) throws Exception {
		UiUtil.writeOpenStartTag(out, "script");
		UiUtil.writeAttribute(out, "language", "JavaScript1.2");
		UiUtil.writeAttribute(out, "type", "text/javascript");
		UiUtil.writeAttribute(out, "src", srcFile, false);
		UiUtil.writeCloseStartTag(out);
		UiUtil.writeEndTag(out, "script");
		out.write("\n");
	}
}
