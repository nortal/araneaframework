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

package org.araneaframework.template.tags.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * SAMPLE layout cell separator tag.
 * 
 * @author Marko Muts
 * 
 * @jsp.tag
 *   name = "cellSeparator"
 *   body-content = "empty"
 */
public class SampleLayoutCellSeparatorTag extends UiBaseTag {
  protected String rowSpan;
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    UiUtil.writeOpenStartTag(out, "td");
    UiUtil.writeAttribute(out, "rowspan", rowSpan);
    UiUtil.writeCloseStartTag(out);
    UiUtil.writeOpenStartTag(out, "img");
    UiUtil.writeAttribute(out, "src",  "gfx/1.gif");
    UiUtil.writeAttribute(out, "width", "10");  
    UiUtil.writeAttribute(out, "height", "1");  
    UiUtil.writeCloseStartEndTag(out);  
    UiUtil.writeEndTag(out, "td");  
    
    return EVAL_BODY_INCLUDE;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   */
  public void setRowSpan(String rowSpan) throws JspException {
    this.rowSpan = (String)evaluate("rowSpan", rowSpan, String.class);
  }
}
