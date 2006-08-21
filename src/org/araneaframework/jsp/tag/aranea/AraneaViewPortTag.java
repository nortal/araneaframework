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

package org.araneaframework.jsp.tag.aranea;

import java.io.Writer;
import org.araneaframework.OutputData;
import org.araneaframework.framework.ViewPortContext;
import org.araneaframework.framework.container.StandardContainerWidget;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.filter.StandardJspFilterService;
import org.araneaframework.jsp.container.UiAraneaWidgetContainer;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.tag.BaseTag;

/**
 * @jsp.tag
 *   name = "viewPort"
 *   body-content = "JSP"
 */
public class AraneaViewPortTag extends BaseTag {
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    OutputData output = getOutputData(); 
    StandardContainerWidget rootWidget = 
      (StandardContainerWidget) output.getAttribute(ViewPortContext.VIEW_PORT_WIDGET_KEY);
    StandardJspFilterService.JspConfiguration config = 
      (StandardJspFilterService.JspConfiguration) output.getAttribute(
          JspContext.JSP_CONFIGURATION_KEY);

    addContextEntry(
        UiWidgetContainer.REQUEST_CONTEXT_KEY, 
        new UiAraneaWidgetContainer(rootWidget, config));  
    
    return EVAL_BODY_INCLUDE;
  }
}
