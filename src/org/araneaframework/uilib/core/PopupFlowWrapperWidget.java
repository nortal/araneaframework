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


package org.araneaframework.uilib.core;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowContextWidget;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.service.WindowClosingService;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.URLUtil;

/**
 * Wrapper around the flow that is started from new session-thread. It pretends
 * to be {@link org.araneaframework.framework.FlowContext} for wrapped flows and proxies
 * method calls to current <emphasis>real</emphasis> {@link org.araneaframework.framework.FlowContext} 
 * and/or to {@link org.araneaframework.framework.FlowContext} from which wrapped flow was started.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class PopupFlowWrapperWidget extends BaseApplicationWidget implements FlowContextWidget {

  private static final long serialVersionUID = 1L;

  protected Widget child;

  public PopupFlowWrapperWidget(Widget child) {
    this.child = child;
  }
  
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), FlowContext.class, this);
  }

  protected void init() throws Exception {
    super.init();
    addWidget("child", child);
  }

  public void start(Widget flow, Handler handler) {
    start(flow, null, handler);
  }

  public void start(Widget flow) {
    start(flow, null, null);
  }

  public void start(Widget flow, Configurator configurator, Handler handler) {
    getLocalFlowContext().start(flow, configurator, handler);
  }
  
  public void replace(Widget flow) {
    replace(flow, null);
  }

  public void replace(Widget flow, Configurator configurator) {
    // XXX: does not work when this is only widget on call stack
    getLocalFlowContext().replace(flow, configurator);
  }

  public void finish(Object result) {
    ThreadContext threadCtx = EnvironmentUtil.requireThreadContext(getEnvironment());
    getOpenerFlowContext().finish(result);

    try {
      // close the session-thread of popupflow
      Object currentThreadId = threadCtx.getCurrentId();
      getOpenerPopupContext().close(currentThreadId.toString());
      
      Object servingThreadId = getInputData().getGlobalData().get(ThreadContext.THREAD_SERVICE_KEY);

      // if request for closing popup came from the popup window itself
      if (currentThreadId.equals(servingThreadId)) {
        String rndThreadId = RandomStringUtils.randomAlphanumeric(12);
        //popup window is closed with redirect to a page that closes current window and reloads parent.
        threadCtx.addService(rndThreadId, new WindowClosingService(getEnvironment()));
        ((HttpOutputData) getOutputData()).sendRedirect(getResponseURL(getRequestURL(), (String) EnvironmentUtil.requireTopServiceId(getEnvironment()), rndThreadId));
      }
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  public void cancel() {
    // XXX: may not just call openerflowcontext if more than one flow has been started from here 
    getOpenerFlowContext().cancel();
  }

  public boolean isNested() {
    // XXX: should hold that information itself?
    return getLocalFlowContext().isNested();
  }

  public void reset(EnvironmentAwareCallback callback) {
    getLocalFlowContext().reset(callback);
    // XXX: and now what?
  }

  /**
   * @deprecated
   */
  public FlowReference getCurrentReference() {
    return getLocalFlowContext().getCurrentReference();
  }

  public void addNestedEnvironmentEntry(ApplicationWidget scope, Object entryId, Object envEntry) {
    getLocalFlowContext().addNestedEnvironmentEntry(scope, entryId,
        envEntry);
  }

  protected void render(OutputData output) throws Exception {
    child._getWidget().render(output);
  }
  
  private FlowContext getLocalFlowContext() {
    return EnvironmentUtil.getFlowContext(getEnvironment());
  }
  
  protected String getRequestURL() {
    return ((HttpInputData) getInputData()).getContainerURL(); 
  }
  
  protected String getResponseURL(String url, String topServiceId, String threadServiceId) {
    Map m = new HashMap();
    m.put(TopServiceContext.TOP_SERVICE_KEY, topServiceId);
    m.put(ThreadContext.THREAD_SERVICE_KEY, threadServiceId);
    m.put(TransactionContext.TRANSACTION_ID_KEY, TransactionContext.OVERRIDE_KEY);
    return ((HttpOutputData)getOutputData()).encodeURL(URLUtil.parametrizeURI(url, m));
  }
  
  private FlowContext getOpenerFlowContext() {
    ApplicationWidget appWidget = (ApplicationWidget) getPopupContext().getOpener();
    if (appWidget != null) {
      return EnvironmentUtil.getFlowContext(appWidget.getChildEnvironment());
    } else {
      return null;
    }
  }
  
  protected PopupWindowContext getPopupContext() {
    return EnvironmentUtil.getPopupWindowContext(getEnvironment());
  }

  protected PopupWindowContext getOpenerPopupContext() {
    ApplicationWidget appWidget = (ApplicationWidget) getPopupContext().getOpener();
    if (appWidget != null) {
      return EnvironmentUtil.getPopupWindowContext(appWidget.getChildEnvironment());
    } else {
      return null;
    }
  }

  public void setTransitionHandler(TransitionHandler handler) {
    getLocalFlowContext().setTransitionHandler(handler);
  }

  public TransitionHandler getTransitionHandler() {
    return getLocalFlowContext().getTransitionHandler();
  }
}
