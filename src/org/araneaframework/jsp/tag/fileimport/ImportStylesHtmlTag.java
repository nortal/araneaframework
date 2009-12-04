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
 * @jsp.tag name = "importStyles" body-content="empty" description = "Imports CSS files."
 */
public class ImportStylesHtmlTag extends BaseFileImportTag {

  private String media;

  @Override
  public int doStartTag(Writer out) throws Exception {
    // if filename specified we include the file
    if (this.includeFileName != null) {
      writeContent(out, this.includeFileName);
    } else if (this.includeGroupName != null) {
      writeContent(out, this.includeGroupName + GROUP_CSS_SUFFIX);
    } else {
      writeContent(out, DEFAULT_GROUP_NAME + GROUP_CSS_SUFFIX);
    }
    return EVAL_BODY_INCLUDE;
  }

  @Override
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

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The media type the css file should be applied to."
   */
  public void setMedia(String media) {
    this.media = evaluate("media", media, String.class);
  }
}
