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

package org.araneaframework.jsp.tag.form;

import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.container.StandardContainerWidget;
import org.araneaframework.http.ClientStateContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ClientStateUtil;
import org.araneaframework.jsp.util.JspUtil;


/**
 * System form tag. System form maps into HTML form. 
 * Canonical use in Aranea - surrounding all body elements. 
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "systemForm"
 *   description = "Puts an HTML <i>form</i> tag with parameters needed by Aranea."
 */
public class AraneaSystemFormHtmlTag extends BaseSystemFormHtmlTag {  
  private JspContext config;
  
  protected int doStartTag(Writer out) throws Exception {
    config = (JspContext) getEnvironment().requireEntry(JspContext.class);

    super.doStartTag(out);
    
    // Hidden fields: preset
    JspUtil.writeHiddenInputElement(out, TopServiceContext.TOP_SERVICE_KEY, ClientStateUtil.requireTopServiceId(getEnvironment()).toString());
    JspUtil.writeHiddenInputElement(out, ThreadContext.THREAD_SERVICE_KEY, ClientStateUtil.requireThreadServiceId(getEnvironment()).toString());
    JspUtil.writeHiddenInputElement(out, TransactionContext.TRANSACTION_ID_KEY, ClientStateUtil.requireTransactionId(getEnvironment()).toString());
    
    String clientState = ClientStateUtil.getClientState(getEnvironment());
    if (clientState != null) {
      JspUtil.writeHiddenInputElement(out, ClientStateContext.CLIENT_STATE, clientState);
    }
    String clientStateVersion = ClientStateUtil.getClientStateVersion(getEnvironment());
    if (clientStateVersion != null) {
      JspUtil.writeHiddenInputElement(out, ClientStateContext.CLIENT_STATE_VERSION, clientStateVersion);
    }
    
    // Hidden fields: to be set
    JspUtil.writeHiddenInputElement(out, ApplicationWidget.EVENT_HANDLER_ID_KEY, "");
    JspUtil.writeHiddenInputElement(out, StandardContainerWidget.EVENT_PATH_KEY, "");
    JspUtil.writeHiddenInputElement(out, ApplicationWidget.EVENT_PARAMETER_KEY, "");

    // Continue
    return EVAL_BODY_INCLUDE;
  }
  
  /* ***********************************************************************************
   * Implementation of SystemForm abstract methods
   * ***********************************************************************************/

  protected String getAcceptCharset() {
    return config.getSubmitCharset();
  }

  protected String getFormAction() {
    return ((HttpServletRequest) pageContext.getRequest()).getContextPath() + ((HttpServletRequest) pageContext.getRequest()).getServletPath();
  }
}
