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
import org.araneaframework.framework.container.StandardViewPortWidget;
import org.araneaframework.jsp.container.UiAraneaWidgetContainer;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;
import org.araneaframework.servlet.filter.StandardJspFilterService;

/**
 * @jsp.tag
 *   name = "viewPort"
 *   body-content = "JSP"
 */
public class UiAraneaViewPortTag extends UiBaseTag {
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    StandardViewPortWidget rootWidget = 
      (StandardViewPortWidget) output.getAttribute(StandardViewPortWidget.VIEW_PORT_WIDGET_KEY);
    StandardJspFilterService.JspConfiguration config = 
      (StandardJspFilterService.JspConfiguration) output.getAttribute(
          StandardJspFilterService.JSP_CONFIGURATION_KEY);

    addContextEntry(
        UiWidgetContainer.REQUEST_CONTEXT_KEY, 
        new UiAraneaWidgetContainer(rootWidget, config));  
    
    return EVAL_BODY_INCLUDE;
  }
}
