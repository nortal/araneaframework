/*
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
 */

package org.araneaframework.jsp.util;

import org.araneaframework.Path;

import java.util.StringTokenizer;
import javax.servlet.jsp.PageContext;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.context.WidgetContextTag;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class JspWidgetUtil {

  /**
   * @since 1.1
   */
  public static ApplicationWidget getContextWidget(PageContext pageContext) {
    return (ApplicationWidget) pageContext.getRequest().getAttribute(WidgetContextTag.CONTEXT_WIDGET_KEY);
  }

  public static String getContextWidgetFullId(PageContext pageContext) {
    return getContextWidget(pageContext).getScope().toString();
  }

	public static ApplicationWidget traverseToSubWidget(ApplicationWidget root, String path) throws AraneaJspException {		
    ApplicationWidget widget = root;
		
    if ("".equals(path))
      throw new AraneaJspException("Trying to traverse to a widget with an empty path!");
    
		// Traverse    
		for(StringTokenizer tokenizer = new StringTokenizer(path, Path.SEPARATOR); tokenizer.hasMoreElements();) {
			String token = tokenizer.nextToken();
					
			widget = (ApplicationWidget) widget._getComposite().getChildren().get(token);
			if (widget == null)
				throw new AraneaJspException("Failed to traverse widget with path '" + path + "' because widget '" + token + "' was not found");
		}
		
		// Complete
		return widget;		
	}	
}
