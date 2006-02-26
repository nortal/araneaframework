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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.OutputData;
import org.araneaframework.core.Standard;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "widgetContext"
 *   body-content = "JSP"
 *   description = "Initializes the widget context."
 */
public class UiWidgetContextTag extends UiBaseTag {
  public final static String WIDGET_CONTEXT_ID_KEY_REQUEST = "contextWidgetId";
  public final static String WIDGET_CONTEXT_VIEW_MODEL_KEY_REQUEST = "contextWidget";

  private String widgetId;
  private Standard.StandardWidgetInterface widget;
  private Standard.WidgetViewModel viewModel;
  private OutputData output;
  private int pathLength = 0;

  /**
   * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Widget id."
   */
  public void setId(String widgetId) throws JspException {
    this.widgetId = (String) evaluateNotNull("widgetId", widgetId, String.class);
  }

  protected int before(Writer out) throws Exception {
    super.before(out);

    output = (OutputData) pageContext.getRequest().getAttribute(
        StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    if (widgetId != null) {
      StringTokenizer tokenizer = new StringTokenizer(widgetId, ".");

      pathLength = tokenizer.countTokens();
      if (pathLength == -1) pathLength = 0;

      for (; tokenizer.hasMoreTokens();) {
        String token = tokenizer.nextToken();
        output.pushScope(token);
      }
    }

    widget = UiWidgetUtil.getWidgetFromContext(null, pageContext);
    viewModel = (Standard.WidgetViewModel) widget._getViewable().getViewModel();
    widgetId = UiWidgetUtil.getWidgetFullIdFromContext(null, pageContext);

    pushAttribute(WIDGET_CONTEXT_ID_KEY_REQUEST, widgetId, PageContext.REQUEST_SCOPE);
    pushAttribute(WIDGET_CONTEXT_VIEW_MODEL_KEY_REQUEST, viewModel, PageContext.REQUEST_SCOPE);

    return EVAL_BODY_INCLUDE;
  }

  protected int after(Writer out) throws Exception {
    for (int i = 0; i < pathLength; i++)
      output.popScope();

    super.after(out);
    return EVAL_PAGE;
  }

  protected void init() {
    super.init();

    widgetId = null;
    widget = null;
    viewModel = null;
    output = null;
    pathLength = 0;
  }
}
