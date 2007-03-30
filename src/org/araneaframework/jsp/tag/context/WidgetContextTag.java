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
import java.util.StringTokenizer;
import org.araneaframework.OutputData;
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

  public static final String CONTEXTWIDGET_KEY = "org.araneaframework.jsp.tag.context.WidgetContextTag.CONTEXTWIDGET";
  private OutputData output = null;
  
  private int pathLength = 0;
  
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (id != null) {
      addContextEntry(CONTEXTWIDGET_KEY, widget);

      output = getOutputData();
      StringTokenizer tokenizer = new StringTokenizer(id, ".");

      pathLength = tokenizer.countTokens();
      if (pathLength == -1) pathLength = 0;

      for (; tokenizer.hasMoreTokens();) {
        String token = tokenizer.nextToken();
        output.pushScope(token);
      }
    }    
    
    return EVAL_BODY_INCLUDE;
  }
  
  protected int doEndTag(Writer out) throws Exception {
    return EVAL_PAGE;
  }
  
  public void doFinally() {
    for (int i = 0; i < pathLength; i++)
      output.popScope();

    super.doFinally();
  }
}
