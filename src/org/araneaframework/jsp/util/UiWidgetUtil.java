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
import org.araneaframework.core.Standard;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class UiWidgetUtil {
    public static String getContextWidgetFullId(PageContext pageContext) throws JspException  {
      return getWidgetFullIdFromContext(null, pageContext);
    }
  
	public static String getWidgetFullIdFromContext(String widgetId, PageContext pageContext) throws JspException {
		//Get widget id and view model from context
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
		
		//Widget name given
		if (widgetId != null)
			return NameUtil.getFullName(output.getScope().toString(), widgetId);
		//Current widget
		else
			return output.getScope().toString();
	}
	
    public static Standard.StandardWidgetInterface getContextWidgetFromContext(PageContext pageContext) throws JspException  {
      return getWidgetFromContext(null, pageContext);
    }    
    
	public static Standard.StandardWidgetInterface getWidgetFromContext(String widgetId, PageContext pageContext) throws JspException {
      UiWidgetContainer container = 
        (UiWidgetContainer) UiUtil.requireContextEntry(pageContext,
            UiWidgetContainer.REQUEST_CONTEXT_KEY, PageContext.REQUEST_SCOPE);
      
      String widgetFullId = getWidgetFullIdFromContext(widgetId, pageContext);
      
			return UiWidgetUtil.traverseToWidget(container, widgetFullId);
			
	}
	
	public static Standard.StandardWidgetInterface traverseToWidget(UiWidgetContainer container, String path) throws UiException {
		String pathStart = NameUtil.getNamePrefix(path);
		String pathEnd = NameUtil.getNameSuffix(path);
		
        Standard.StandardWidgetInterface widget = (Standard.StandardWidgetInterface) container.getWidgets().get(pathStart);
		if (widget == null)
			throw new UiException("Failed to traverse to widget with path '" + path + "' because widget '" + pathStart + "' was not found");
				
		if (!"".equals(pathEnd)) 
			widget = traverseToSubWidget(widget, pathEnd);
		
		return widget;
	}
	
	public static Standard.StandardWidgetInterface traverseToSubWidget(Standard.StandardWidgetInterface root, String path) throws UiException {		
      Standard.StandardWidgetInterface widget = root;
		
    if ("".equals(path))
      throw new UiException("Trying to traverse to a widget with an empty path!");
    
		// Traverse    
		for(StringTokenizer tokenizer = new StringTokenizer(path, "."); tokenizer.hasMoreElements();) {
			String token = tokenizer.nextToken();
					
			widget = (Standard.StandardWidgetInterface) widget._getComposite().getChildren().get(token);
			if (widget == null)
				throw new UiException("Failed to traverse widget with path '" + path + "' because widget '" + token + "' was not found");
		}
		
		// Complete
		return widget;		
	}	
}
