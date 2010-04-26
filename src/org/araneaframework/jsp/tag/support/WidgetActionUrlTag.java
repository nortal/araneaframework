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

package org.araneaframework.jsp.tag.support;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.ApplicationService;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;

/**
 * Widget action URL tag. Makes available <code>widgetActionUrl</code> EL variable that can be used to fetch data from
 * widget's action.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "widgetActionUrl"
 *  body-content = "JSP"
 *  description = "Provides page scope variable <i>widgetActionUrl</i> &mdash; URL which points to widget's action."
 */
public class WidgetActionUrlTag extends BaseWidgetTag {

  public static final String WIDGET_ACTION_URL_KEY = "widgetActionUrl";

  protected String actionId;

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(WIDGET_ACTION_URL_KEY, getWidgetActionUrl());
    return EVAL_BODY_INCLUDE;
  }

  protected String getWidgetActionUrl() {
    Map<String, String> m = new HashMap<String, String>();
    m.put(TransactionContext.TRANSACTION_ID_KEY, TransactionContext.OVERRIDE_KEY);
    m.put(TopServiceContext.TOP_SERVICE_KEY, EnvironmentUtil.requireTopServiceId(getEnvironment()));
    m.put(ThreadContext.THREAD_SERVICE_KEY, EnvironmentUtil.requireThreadServiceId(getEnvironment()));
    m.put(ApplicationService.ACTION_PATH_KEY, this.fullId);

    if (this.actionId != null) {
      m.put(ApplicationService.ACTION_HANDLER_ID_KEY, this.actionId);
    }

    return getHttpOutputData().encodeURL(URLUtil.parameterizeURI(getHttpInputData().getContainerURL(), m));
  }

  @Override
  public void doFinally() {
    super.doFinally();
    this.actionId = null;
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "required" description = "The ID of an 'action' to call."
   */
  public void setActionId(String actionId) throws JspException {
    this.actionId = evaluateNotNull("actionId", actionId, String.class);
  }

}
