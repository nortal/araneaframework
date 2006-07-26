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
import java.util.Map;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.jsp.tag.aranea.UiAraneaRootTag;
import org.araneaframework.jsp.tag.basic.UiAttributedTagInterface;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.ThreadCloningContext;
import org.araneaframework.servlet.util.ClientStateUtil;

/**
 * Standard button tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "basicLinkButton"
 *   body-content = "JSP"
 *   description = "Represents a link with an onClick JavaScript action."
 */
public class UiStdLinkButtonTag extends UiButtonBaseTag {
  {
    baseStyleClass = "aranea-link-button"; 
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    StringBuffer url = getRequestURL();
    ServletOutputData output = (ServletOutputData) requireContextEntry(UiAraneaRootTag.OUTPUT_DATA_KEY);
    Map attributes = (Map) output.getAttribute(ClientStateUtil.SYSTEM_FORM_STATE);
    Object currentThreadId = attributes.get(StandardThreadServiceRouterService.THREAD_SERVICE_KEY);

    url.append("?").append(StandardThreadServiceRouterService.THREAD_SERVICE_KEY).append("=").append(ThreadCloningContext.CLONING_THREAD_KEY);
    url.append("&").append(ThreadCloningContext.CLONABLE_THREAD_KEY).append("=").append(currentThreadId);
    
    addContextEntry(UiAttributedTagInterface.HTML_ELEMENT_KEY, id);

    UiUtil.writeOpenStartTag(out, "a");
    UiUtil.writeAttribute(out, "id", id);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "style", getStyle());
    UiUtil.writeAttribute(out, "href", url.toString());
    UiUtil.writeAttribute(out, "onclick", onclick);    
    UiUtil.writeCloseStartTag_SS(out);

    return EVAL_BODY_INCLUDE;    
  }

  protected int doEndTag(Writer out) throws Exception {  
    if (labelId != null)            
      UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, labelId));
    UiUtil.writeEndTag(out, "a"); 

    super.doEndTag(out);
    return EVAL_PAGE;      
  }
}
