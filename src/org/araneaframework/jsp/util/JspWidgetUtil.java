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

import java.util.StringTokenizer;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Path;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.context.WidgetContextTag;

/**
 * Utility methods regarding widgets in JSP layer.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class JspWidgetUtil {

  /**
   * Returns the widget for which this tag is rendered.
   * 
   * @param pageContext The current JSP page context.
   * @return The current context widget.
   * @since 1.1
   */
  public static ApplicationWidget getContextWidget(PageContext pageContext) {
    return (ApplicationWidget) pageContext.getRequest().getAttribute(WidgetContextTag.CONTEXT_WIDGET_KEY);
  }

  /**
   * Returns the full ID of the widget for which this tag is rendered.
   * 
   * @param pageContext The current JSP page context.
   * @return The full ID of the current context widget.
   */
  public static String getContextWidgetFullId(PageContext pageContext) {
    return getContextWidget(pageContext).getScope().toString();
  }

  /**
   * Given a widget (root) and a (tree) path relative to that widget (tree root), returns the child widget according to
   * the path. Fails with an exception when either root or path or any child widget on the path is null.
   * 
   * @param root The required widget, which is the starting point for traversing.
   * @param path The path to follow. The syntax of the path is expected to be the same as
   *          <code>widget.getScope().toString()</code> returns.
   * @return The matching child widget.
   * @throws AraneaJspException In case the root, the path or any path-matching child widget is null.
   */
  public static ApplicationWidget traverseToSubWidget(ApplicationWidget root, String path) throws AraneaJspException {
    if (root == null) {
      throw new AraneaJspException("The 'root' widget must not be null!");
    } else if (StringUtils.isEmpty(path)) {
      throw new AraneaJspException("Trying to traverse to a widget with an empty path!");
    }

    // Traverse
    for (StringTokenizer tokenizer = new StringTokenizer(path, Path.SEPARATOR); tokenizer.hasMoreElements();) {
      String token = tokenizer.nextToken();

      root = (ApplicationWidget) root._getComposite().getChildren().get(token);
      if (root == null)
        throw new AraneaJspException("Failed to traverse widget with path '" + path + "' because widget '" + token
            + "' was not found");
    }

    // Complete
    return root;
  }
}
