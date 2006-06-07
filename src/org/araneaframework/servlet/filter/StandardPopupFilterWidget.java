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

package org.araneaframework.servlet.filter;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.Widget;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.framework.messages.StandardFlowContextResettingMessage;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.framework.router.StandardTopServiceRouterService;
import org.araneaframework.servlet.PopupServiceInfo;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardPopupFilterWidget extends BaseFilterWidget implements PopupWindowContext {
  private static final Logger log = Logger.getLogger(StandardPopupFilterWidget.class);
  
  /** Maps of popups where keys are service IDs(==popup IDs) and values 
   * <code>StandardPopupFilterWidget.PopupServiceInfo</code>. Used for rendering popups.*/ 
  protected Map popups = new HashMap();
  /** Used to keep track of popups that have been opened from thread and not yet explicitly closed. */
  protected Map allPopups = new HashMap();

  protected ServiceFactory threadServiceFactory;

  public void setThreadServiceFactory(ServiceFactory factory) {
    this.threadServiceFactory = factory;
  }
  
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), PopupWindowContext.class, this);
  }
  
  public String openDetached(Message startMessage, PopupWindowProperties properties) throws Exception {
    String threadId = getRandomServiceId();
    String topServiceId = (String) getTopServiceCtx().getCurrentId();

    Service service = threadServiceFactory.buildService(getEnvironment());
    startThreadPopupService(threadId, service);

    if (startMessage != null)
      startMessage.send(null, service);
    
    //add new, not yet opened popup to popup map
    popups.put(threadId, new StandardPopupServiceInfo(topServiceId, threadId, properties, getRequestURL()));
    allPopups.put(threadId, popups.get(threadId));
    
    log.debug("Popup service with identifier '" + threadId + "' was created.");
    return threadId;
  }

  public String openAttached(Message startMessage, PopupWindowProperties properties, Widget caller) throws Exception {
    String threadId = getRandomServiceId();
    String topServiceId = (String) getTopServiceCtx().getCurrentId();

    Service service = threadServiceFactory.buildService(getEnvironment());
    startThreadPopupService(threadId, service);

    if (startMessage != null)
      startMessage.send(null, service);
    
    //add new, not yet opened popup to popup map
    popups.put(threadId, new StandardPopupServiceInfo(topServiceId, threadId, properties, getRequestURL()));
    allPopups.put(threadId, popups.get(threadId));
    
    log.debug("Popup service with identifier '" + threadId + "' was created.");
    return threadId;
  }

  
  public String openDetached(Service service, PopupWindowProperties properties) throws Exception {
    String threadId = getRandomServiceId();
    String topServiceId = (String) getTopServiceCtx().getCurrentId();

    startThreadPopupService(threadId, service);
    popups.put(threadId, new StandardPopupServiceInfo(topServiceId, threadId, properties, getRequestURL()));
    allPopups.put(threadId, popups.get(threadId));
    
    log.debug("Popup service with identifier '" + threadId + "' was registered.");
    return threadId;
  }
  
  public String openDetached(Widget flow, PopupWindowProperties properties) throws Exception {
    String threadId = getRandomServiceId();
    String topServiceId = (String) getTopServiceCtx().getCurrentId();
    
    Service service = threadServiceFactory.buildService(getEnvironment());
    startThreadPopupService(threadId, service);

    if (flow != null) {
      new StandardFlowContextResettingMessage(flow).send(null, service);
    }

    popups.put(threadId, new StandardPopupServiceInfo(topServiceId, threadId, properties, getRequestURL()));
    allPopups.put(threadId, popups.get(threadId));

    return threadId;
  }

  public void open(final String url, final PopupWindowProperties properties) throws Exception {
    popups.put(url, new PopupServiceInfo() {
      public PopupWindowProperties getPopupProperties() {
        return properties;
      }

      public String toURL() {
        return url;
      }
    });
  }

  public boolean closeDetached(String id) throws Exception {
    if (!allPopups.containsKey(id)) {
      log.warn("Attempt to close non-owned, unopened or already closed popup service with ID +'" + id + "'.");
      return false;
    }

    try {
      getServiceCtx(ThreadContext.class).close(id);
    } catch (Exception e) {
      log.warn("Attempt to close registered popup service with ID +'" + id + "' has failed with exception : ." + e);
      return false;
    } finally {
      allPopups.remove(id);
    }

    if (log.isDebugEnabled())
      log.debug("Popup service with identifier '" + id + "' was closed");
    return true;
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    super.action(path, input, output);
  }
  
  protected void event(Path path, InputData input) throws Exception {
    if (input.getGlobalData().containsKey(PopupWindowContext.POPUPS_CLOSE_KEY)) {
      ThreadContext threadCtx = (ThreadContext)getEnvironment().getEntry(ThreadContext.class);
      Object id = threadCtx.getCurrentId();
      threadCtx.close(id);
    } else {
      super.event(path, input);
    }
  }

  /** 
   * Popups are rendered by pushing <code>Map &lt;String serviceId, PopupServiceInfo serviceInfo&gt;</code>
   * into output under the key {@link org.araneaframework.servlet.PopupWindowContext}.POPUPS_KEY
   */
  protected void render(OutputData output) throws Exception {
    output.pushAttribute(POPUPS_KEY, popups);

    try {
      super.render(output);
    }
    finally {
      output.popAttribute(POPUPS_KEY);
    }

    popups = new HashMap();
  }
  
  protected void startThreadPopupService(String id, Service service) throws Exception {
    startPopupService(id, service, ThreadContext.class);
  }
  
  protected void startPopupService(String id, Service service, Class serviceContext) throws Exception {
    getServiceCtx(serviceContext).addService(id, service);
  }
  
  protected String getRandomServiceId() {
    return RandomStringUtils.random(8, true, true);
  }
  
  protected ManagedServiceContext getServiceCtx(Class contextClass) {
    return (ManagedServiceContext)(getEnvironment().requireEntry(contextClass));
  }
  
  protected TopServiceContext getTopServiceCtx() {
    return ((TopServiceContext)getEnvironment().requireEntry(TopServiceContext.class));
  }
  
  protected String getRequestURL() {
    return ((HttpServletRequest)((ServletInputData)getCurrentInput()).getRequest()).getRequestURL().toString();
  }

  public class StandardPopupServiceInfo implements PopupServiceInfo {
    private String topServiceId;
    private String threadId;
    private PopupWindowProperties popupProperties;
    
    private String requestUrl;
    
    public StandardPopupServiceInfo(String topServiceId, String threadId, PopupWindowProperties popupProperties, String requestUrl) {
      this.topServiceId = topServiceId;
      this.threadId = threadId;
      this.popupProperties = popupProperties;
      this.requestUrl = requestUrl;
    }

    public PopupWindowProperties getPopupProperties() {
      return popupProperties;
    }

    public String toURL() {
      StringBuffer url = new StringBuffer(requestUrl != null ? requestUrl : "");
      url.append('?').append((StandardTopServiceRouterService.TOP_SERVICE_KEY + "=")).append(topServiceId);
      if (threadId != null) {
        url.append("&" + StandardThreadServiceRouterService.THREAD_SERVICE_KEY + "=");
        url.append(threadId);
      }
      return url.toString();
    }
  }
}
