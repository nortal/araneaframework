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
import javax.servlet.jsp.JspException;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 * @jsp.tag
 *   name = "importStyles"
 *   body-content="empty"
 *   description = "Imports CSS files."
 */
public class ImportStylesHtmlTag extends BaseFileImportTag {

  private String media;

  public int doStartTag(Writer out) throws Exception {
    // if filename specified we include the file
    if (includeFileName != null) {
      writeContent(out, includeFileName);
    } else if (includeGroupName != null) {
      writeContent(out, includeGroupName + GROUP_CSS_SUFFIX);
    } else {
      writeContent(out, DEFAULT_GROUP_NAME + GROUP_CSS_SUFFIX);
    }
    return EVAL_BODY_INCLUDE;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The media type the css file should be applied to."
   */
  public void setMedia(String media) throws JspException {
    this.media = (String) evaluate("media", media, String.class);
  }

  protected void writeContent(Writer out, String srcFile) throws Exception {
    srcFile = FileImportUtil.getImportString(srcFile, pageContext.getRequest());
    JspUtil.writeOpenStartTag(out, "link");
    JspUtil.writeAttribute(out, "rel", "stylesheet");
    JspUtil.writeAttribute(out, "type", "text/css");
    JspUtil.writeAttribute(out, "href", srcFile, false);
    JspUtil.writeAttribute(out, "media", this.media);
    JspUtil.writeCloseStartEndTag(out);
    out.write("\n");
  }
}
