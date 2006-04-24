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

package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "eventLinkButton"
 *   body-content = "JSP"
 *   description = "Represents a link with an onClick JavaScript event."
 */
public class UiStdEventLinkButtonTag extends UiEventButtonBaseTag {

	protected void init() {
		super.init();
		styleClass = "aranea-link-button"; 
	}
	
	protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);          
    
    UiUtil.writeOpenStartTag(out, "a");
    UiUtil.writeAttribute(out, "id", id);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "href", "javascript:");
    if (eventId != null)
      UiStdWidgetCallUtil.writeEventAttributeForEvent(
          pageContext,
          out, 
          "onclick", 
          systemFormId,  
          contextWidgetId, 
          eventId, 
          eventParam, 
          onClickPrecondition,
          updateRegionNames);       
    UiUtil.writeCloseStartTag_SS(out);    
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }    
    
  protected int doEndTag(Writer out) throws Exception {
    
    if (localizedLabel != null)
      UiUtil.writeEscaped(out, localizedLabel);
    
    UiUtil.writeEndTag_SS(out, "a"); 
    
    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }  
}
