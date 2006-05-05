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

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Aranea's row tag, represents one row in a layout.
 * In HTML this tag corresponds to table row -- &lt;tr&gt; with <code>class</code> attribute.
 * 
 * @jsp.tag
 *   name = "row"
 *   body-content = "JSP"
 *   description = "Represents a row in layout."
 *
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class LayoutRowTag extends LayoutRowBaseTag {
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    UiUtil.writeOpenStartTag(out, "tr");
    writeRowAttributes(out);
    UiUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }

  /** Overwrite if other attributes besides <code>styleclass</code> are needed for HTML table row. */
  protected void writeRowAttributes(Writer out) throws Exception {
    addAttribute("class",  getStyleClass());
    UiUtil.writeAttributes(out, attributes);
  }

  protected int doEndTag(Writer out) throws Exception {
    UiUtil.writeEndTag(out, "tr");
    return super.doEndTag(out);
  }
}
