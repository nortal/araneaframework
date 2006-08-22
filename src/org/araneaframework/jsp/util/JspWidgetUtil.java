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

package org.araneaframework.jsp.util;

import java.util.StringTokenizer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class JspWidgetUtil {
    public static String getContextWidgetFullId(PageContext pageContext) throws JspException  {
      return getWidgetFullIdFromContext(null, pageContext);
    }
  
	public static String getWidgetFullIdFromContext(String widgetId, PageContext pageContext) throws JspException {
		//Get widget id and view model from context
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          OutputData.OUTPUT_DATA_KEY);
		
		//Widget name given
		if (widgetId != null)
			return NameUtil.getFullName(output.getScope().toString(), widgetId);
		//Current widget
		else
			return output.getScope().toString();
	}
	
    public static ApplicationWidget getContextWidgetFromContext(PageContext pageContext) throws JspException  {
      return getWidgetFromContext(null, pageContext);
    }    
    
	public static ApplicationWidget getWidgetFromContext(String widgetId, PageContext pageContext) throws JspException {
      UiWidgetContainer container = 
        (UiWidgetContainer) JspUtil.requireContextEntry(pageContext,
            UiWidgetContainer.REQUEST_CONTEXT_KEY);
      
      String widgetFullId = getWidgetFullIdFromContext(widgetId, pageContext);
      
			return JspWidgetUtil.traverseToWidget(container, widgetFullId);
			
	}
	
	public static ApplicationWidget traverseToWidget(UiWidgetContainer container, String path) throws AraneaJspException {
		String pathStart = NameUtil.getNamePrefix(path);
		String pathEnd = NameUtil.getNameSuffix(path);
		
        ApplicationWidget widget = (ApplicationWidget) container.getWidgets().get(pathStart);
		if (widget == null)
			throw new AraneaJspException("Failed to traverse to widget with path '" + path + "' because widget '" + pathStart + "' was not found");
				
		if (!"".equals(pathEnd)) 
			widget = traverseToSubWidget(widget, pathEnd);
		
		return widget;
	}
	
	public static ApplicationWidget traverseToSubWidget(ApplicationWidget root, String path) throws AraneaJspException {		
      ApplicationWidget widget = root;
		
    if ("".equals(path))
      throw new AraneaJspException("Trying to traverse to a widget with an empty path!");
    
		// Traverse    
		for(StringTokenizer tokenizer = new StringTokenizer(path, "."); tokenizer.hasMoreElements();) {
			String token = tokenizer.nextToken();
					
			widget = (ApplicationWidget) widget._getComposite().getChildren().get(token);
			if (widget == null)
				throw new AraneaJspException("Failed to traverse widget with path '" + path + "' because widget '" + token + "' was not found");
		}
		
		// Complete
		return widget;		
	}	
}
