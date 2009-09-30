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

package org.araneaframework.jsp.tag.updateregion;

import javax.servlet.jsp.JspException;
import java.io.Writer;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Defines the update region in the HTML page that can be updated via AJAX requests.
 * Should be used when not updating content inside HTML <code>table</code> rows.
 * 
 * @jsp.tag
 *   name = "updateRegion"
 *   body-content = "JSP"
 *   description = "Defines an update region for asynchronous updating using AJAX request. Use its "id" to select the updated regions
               using the "updateRegions" attribute of elements capable of sending events. This attribute accepts a comma-separated
               list of regions to be updated with the request."
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class UpdateRegionHtmlTag extends BaseUpdateRegionTag {

  /**
   * The name of the tag that wraps the update region. Client-side scripts don't
   * depend on the tag type, they just require the element to have an ID.
   * @since 1.2.1
   */
  protected String tag = "span";

  /**
   * @since 1.2.1
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Provides a way to use custom HTML tag (default: SPAN). Does not break client-side scripts. Use with caution!"
   */
  public void setTag(String tag) throws JspException {
    this.tag = evaluateNotNull("tag", tag, String.class);
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    JspUtil.writeOpenStartTag(out, this.tag);
    JspUtil.writeAttribute(out, "id",  fullId);
    JspUtil.writeCloseStartTag(out);
    
    out.write("<!--BEGIN:" + fullId + "-->");
    
    return EVAL_BODY_INCLUDE;
  }    
  
  protected int doEndTag(Writer out) throws Exception {
    out.write("<!--END:" + fullId + "-->");
    
    JspUtil.writeEndTag(out, this.tag);
    
    return super.doEndTag(out);
  }
}
