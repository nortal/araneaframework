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
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.jsp.tag.BaseTag;

/**
 * Service action url tag. Makes available <code>serviceActionUrl</code> EL variable that can be used to fetch data from
 * a service.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * 
 * @jsp.tag name = "serviceActionUrl" body-content = "JSP" description = "Service action url tag.<br/>
 *          Makes available following page scope variables:
 *          <ul>
 *          <li><i>serviceActionUrl</i> - URL which points to service.
 *          </ul>
 *          "
 */
public class ServiceActionUrlTag extends BaseTag {

  public static final String WIDGET_ACTION_URL_KEY = "serviceActionUrl";

  protected String id;

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
    m.put(ThreadContext.THREAD_SERVICE_KEY, this.id);
    return getHttpOutputData().encodeURL(URLUtil.parametrizeURI(getHttpInputData().getContainerURL(), m));
  }

  @Override
  public void doFinally() {
    super.doFinally();
    this.id = null;
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "true" description = "Service id."
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

}
