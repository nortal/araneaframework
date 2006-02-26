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
import org.araneaframework.core.Standard;
import org.araneaframework.jsp.tag.context.UiWidgetContextTag;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;

/**
 * Widget include tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "widgetInclude"
 *   body-content = "JSP"
 *   description = "The JSP specified by the path given in <i>page</i> is included as 
					the widget with id specified in <i>id</i>."
 */
public class UiWidgetIncludeTag extends UiIncludeBaseTag {
  //
  // Attributes
  //

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Widget id." 
	 */
	public void setId(String widgetId) throws JspException {
		this.widgetId = (String)evaluateNotNull("widgetId", widgetId, String.class);		
	}	
			
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Path to JSP." 
	 */
	public void setPage(String page) throws JspException {
		this.page = (String)evaluate("page", page, String.class);
	}
  
  //
  // Implementation
  //  
	protected int after(Writer out) throws Exception {   
		// Call
		Standard.StandardWidgetInterface widget = UiWidgetUtil.getWidgetFromContext(widgetId, pageContext);
		
    UiWidgetContextTag widgetContextTag = new UiWidgetContextTag();
    
    registerSubtag(widgetContextTag);
    widgetContextTag.setId(widgetId);
    executeStartSubtag(widgetContextTag);
    
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    
    try {
  		if (page == null) {
		  out.flush();
  		  widget._getWidget().render(output);
      }
      else {		
        include("/widgets/" + page);
      }
    }
    finally {		
      executeEndTagAndUnregister(widgetContextTag);
    }
		
		// Continue
		super.after(out);
		return EVAL_PAGE;
	}
  
  protected void init() {
    super.init();
    
    widgetId = null;
    page = null;
  }
	
	protected String widgetId;
	protected String page;
}
