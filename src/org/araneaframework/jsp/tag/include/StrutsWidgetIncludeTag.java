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

package org.araneaframework.jsp.tag.include;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.OutputData;
import org.araneaframework.integration.struts.StrutsWidget;


/**
 * Widget include tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "strutsWidgetInclude"
 *   body-content = "JSP"
 *   description = "The JSP specified by the path given in <i>page</i> is included as the widget with id specified in <i>id</i>."
 */
public class StrutsWidgetIncludeTag extends BaseIncludeTag {
  protected String widgetId;
  
  public StrutsWidgetIncludeTag() {
    widgetId = null;
  }
  
  protected int doEndTag(Writer out) throws Exception {
    if (widgetId.indexOf('.') != -1)
      throw new JspException("Struts widget includes cannot contain dots in the widget id!");
    
    out.flush();
    
    OutputData output = getOutputData();
    StrutsWidget sw = 
      (StrutsWidget) output.getAttribute(StrutsWidget.STRUTS_WIDGET_KEY);
    sw.renderWidget(widgetId, output);
        
    return EVAL_PAGE;
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Widget id." 
   */
  public void setId(String widgetId) throws JspException {
    this.widgetId = (String)evaluateNotNull("widgetId", widgetId, String.class);    
  }
}
