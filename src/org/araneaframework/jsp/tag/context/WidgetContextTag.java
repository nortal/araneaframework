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

package org.araneaframework.jsp.tag.context;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.WidgetTag;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "widgetContext"
 *   body-content = "JSP"
 *   description = "Initializes the widget context."
 */
public class WidgetContextTag extends WidgetTag {

  public static final String CONTEXT_WIDGET_KEY = "org.araneaframework.jsp.tag.context.WidgetContextTag.CONTEXTWIDGET";
  
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    /*
     * XXX id == null usage is deprecated -- you no longer need to put a
     * <widgetContext> tag without an id at the top of your JSP.
     */
    
    if (id != null) {
      addContextEntry(CONTEXT_WIDGET_KEY, widget);
    }
    
    return EVAL_BODY_INCLUDE;
  }
  
  protected int doEndTag(Writer out) throws Exception {
    return EVAL_PAGE;
  }
}
