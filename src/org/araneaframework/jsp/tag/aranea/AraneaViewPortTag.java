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
import org.araneaframework.framework.ViewPortContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.jsp.container.UiAraneaWidgetContainer;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.tag.BaseTag;

/**
 * Tag that makes renderable Aranea widget hierarchy accessible to its inner tags
 * in a {@link org.araneaframework.jsp.container.UiWidgetContainer} that can be 
 * found from <code>PageContext</code> under the  
 * {@link org.araneaframework.jsp.container.UiWidgetContainer#KEY}.
 * 
 * @jsp.tag
 *   name = "viewPort"
 *   body-content = "JSP"
 */
public class AraneaViewPortTag extends BaseTag {
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    ViewPortContext viewPortContext = (ViewPortContext) getEnvironment().requireEntry(ViewPortContext.class);
    JspContext config = (JspContext) getEnvironment().requireEntry(JspContext.class);
    
    addContextEntry(UiWidgetContainer.KEY, new UiAraneaWidgetContainer(viewPortContext.getViewPort(), config));  
    
    return EVAL_BODY_INCLUDE;
  }
}
