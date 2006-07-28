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

package org.araneaframework.jsp.tag.updateRegion;

import java.io.Writer;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @jsp.tag
 *   name = "updateRegion"
 *   body-content = "JSP"
 *   description = "Defines an update region for asynchronous updating using AJAX request. Use its "id" to select the updated regions
               using the "updateRegions" attribute of elements capable of sending events. This attribute accepts a comma-separated
               list of regions to be updated with the request."
 * @author Jevgeni kabanov (ekabanov@webmedia.ee)
 */
public class UiUpdateRegionCommonTag extends UiUpdateRegionBaseTag {

  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    UiUtil.writeOpenStartTag(out, "span");
    UiUtil.writeAttribute(out, "id",  fullId);
    UiUtil.writeCloseStartTag(out);
    
    out.write("<!--BEGIN:" + fullId + "-->");
    
    return EVAL_BODY_INCLUDE;
  }    
  
  protected int doEndTag(Writer out) throws Exception {
	out.write("<!--END:" + fullId + "-->");
    UiUtil.writeEndTag(out, "span");
    
    return super.doEndTag(out);
  }
}
