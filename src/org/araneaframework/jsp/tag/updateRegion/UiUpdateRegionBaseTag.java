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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiWidgetUtil;

/**
 * @author Jevgeni kabanov (ekabanov@webmedia.ee)
 */
public class UiUpdateRegionBaseTag extends UiBaseTag {
  protected String id;
  protected String globalId;
  
  protected String fullId;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (id == null && globalId == null)
      throw new UiException("'id' or 'globalId' is required!");

    String contextWidgetId = UiWidgetUtil.getContextWidgetFullId(pageContext);

    fullId = globalId;
    
    if (fullId == null)
    	fullId = contextWidgetId.length() > 0 ? (contextWidgetId + "." + id) : id;

    return EVAL_BODY_INCLUDE;    
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Local id of the update region." 
   */
  public void setId(String id) throws JspException {
    this.id = (String) evaluate("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Global id of the update region." 
   */
  public void setGlobalId(String globalId) throws JspException {
    this.globalId = (String) evaluate("globalId", globalId, String.class);
  }  
}
