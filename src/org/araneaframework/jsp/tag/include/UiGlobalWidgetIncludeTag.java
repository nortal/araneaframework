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
import org.araneaframework.Path;
import org.araneaframework.core.Custom;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;


/**
 * Global widget include tag.
 * 
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag
 *   name = "globalWidgetInclude"
 *   body-content = "JSP"
 *   description = "Inludes the global widget rendering it and providing with a context
        that temporary starts from root."
 */
public class UiGlobalWidgetIncludeTag extends UiIncludeBaseTag {
  //
  // Attributes
  //

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true"
	 *   description = "Widget id." 
	 */
	public void setId(String widgetId) throws JspException {
		this.widgetId = (String)evaluateNotNull("widgetId", widgetId, String.class);		
	}				
  
  //
  // Implementation
  //
  
	protected int after(Writer out) throws Exception {
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    
    Path currentScope = output.getScope();
    
	// Call
	Custom.CustomWidget widget = UiWidgetUtil.getWidgetFromContext(widgetId, pageContext);

    while (output.getScope().hasNext())
      output.popScope();
    
    try {
      widget._getWidget().render(output);
    }
    finally {
      for (; currentScope.hasNext(); ) {
        Object next = currentScope.next();
        output.pushScope(next);
      }
    }
		
		// Continue
		super.after(out);
		return EVAL_PAGE;
	}
  
  protected void init() {
    super.init();
    
    widgetId = null;
  }
	
	protected String widgetId;
}
