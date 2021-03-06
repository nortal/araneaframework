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

package org.araneaframework.jsp.tag.fileimport;

import java.io.Writer;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * 
 * @jsp.tag name = "importScripts" body-content="empty" description = "Imports js files"
 */
public class ImportScriptsHtmlTag extends BaseFileImportTag {

  @Override
  public int doStartTag(Writer out) throws Exception {
    // if filename specified we include the file
    if (this.includeFileName != null) {
      writeContent(out, this.includeFileName);
    } else if (this.includeGroupName != null) {
      writeContent(out, this.includeGroupName + GROUP_JS_SUFFIX);
    } else {
      writeContent(out, DEFAULT_GROUP_NAME + GROUP_JS_SUFFIX);
    }
    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected void writeContent(Writer out, String srcFile) throws Exception {
    writeHtmlScriptsInclude(out, FileImportUtil.getImportString(srcFile, this.pageContext.getRequest()));
  }

  public static void writeHtmlScriptsInclude(Writer out, String srcFile) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeAttribute(out, "src", srcFile, false);
    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEndTag(out, "script");
  }
}
