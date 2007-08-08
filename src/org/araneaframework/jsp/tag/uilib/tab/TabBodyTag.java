/**
* Copyright 2006 Webmedia Group Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**/
package org.araneaframework.jsp.tag.uilib.tab;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.include.WidgetIncludeTag;
import org.araneaframework.jsp.tag.updateregion.UpdateRegionHtmlTag;
import org.araneaframework.uilib.tab.TabContainerWidget;

/** 
 *  @jsp.tag
 *   name = "tabBody"
 *   body-content = "JSP"
 *   description = "This tag includes current tab." 
 *   
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public class TabBodyTag extends WidgetIncludeTag {

  private static final String TAB_BODY_UPDATE_REGION_ID = "tab-body";
  
  private UpdateRegionHtmlTag updateRegion;

  protected int doStartTag(Writer out) throws Exception {   
    String tabContainerId = (String) requireContextEntry(TabContainerTag.TABS_CONTAINER_KEY);
    setId(tabContainerId + "." + TabContainerWidget.SELECTED_TAB_KEY);
    
    updateRegion = new UpdateRegionHtmlTag();
    registerSubtag(updateRegion);
    updateRegion.setId(TAB_BODY_UPDATE_REGION_ID);
    executeStartSubtag(updateRegion);
    
    super.doStartTag(out);
    
    return SKIP_BODY;
  }
  
  protected int doEndTag(Writer out) throws Exception {
    super.doEndTag(out);
    
    executeEndTagAndUnregister(updateRegion);
    return EVAL_PAGE;
  }
}
