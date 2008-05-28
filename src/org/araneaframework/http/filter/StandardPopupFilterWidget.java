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

package org.araneaframework.http.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.PopupServiceInfo;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.UpdateRegionProvider;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.http.util.JsonArray;
import org.araneaframework.http.util.JsonObject;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardPopupFilterWidget extends BaseFilterWidget implements PopupWindowContext, UpdateRegionProvider {
  private static final Log log = LogFactory.getLog(StandardPopupFilterWidget.class);

  /** @since 1.1 */
  public static final String POPUP_REGION_KEY = "popups";
  
  /** Maps of popups where keys are service IDs(==popup IDs) and values 
   * <code>StandardPopupFilterWidget.PopupServiceInfo</code>. Used for rendering popups.*/ 
  protected Map popups = new HashMap();
  /** Used to keep track of popups that have been opened from thread and not yet known to be closed. */
  protected Map allPopups = new HashMap();
  /** Widget that opened this popup. Only applicable to threads. */
  protected Widget opener = null;
  /** */
  protected List renderedPopups = new ArrayList();

  /** When creating new popup with unspecified service, popup is acquired from the factory. */
  protected ServiceFactory threadServiceFactory;
  
  public void setThreadServiceFactory(ServiceFactory factory) {
    this.threadServiceFactory = factory;
  }

  /* ************************************************************************************
   * PopupWindowContext interface methods
   * ************************************************************************************/
  public String open(Message startMessage, PopupWindowProperties properties) throws Exception {
    return open(startMessage, properties, null);
  }

  public String open(Message startMessage, PopupWindowProperties properties, Widget opener) {
    String threadId = getRandomServiceId();
    String topServiceId = (String) getTopServiceCtx().getCurrentId();

    Service service = threadServiceFactory.buildService(getEnvironment());
    startThreadPopupService(threadId, service);

    //add new, not yet opened popup to popup map
    popups.put(threadId, new StandardPopupServiceInfo(topServiceId, threadId, properties, getRequestURL()));
    allPopups.put(threadId, popups.get(threadId));
    
    if (log.isDebugEnabled())
        log.debug("Popup service with identifier '" + threadId + "' was created.");
    
    OpenerRegistrationMessage msg = new OpenerRegistrationMessage(opener);
    try {
      if (opener != null)
        msg.send(null, service);
    } finally {
      if (opener != null && !msg.isDelivered())
        log.error("Opener registration message delivery failed.");
    }

    //TODO when exception happens here, should we kill serving session thread
    // and not open popup window at all?
    if (startMessage != null)
      startMessage.send(null, service);

    return threadId;
  }

  public String open(Service service, PopupWindowProperties properties, Widget opener) {
    Assert.notNullParam(this, service, "service");

    String threadId = getRandomServiceId();
    String topServiceId = (String) getTopServiceCtx().getCurrentId();

    startThreadPopupService(threadId, service);
    popups.put(threadId, new StandardPopupServiceInfo(topServiceId, threadId, properties, getRequestURL()));
    allPopups.put(threadId, popups.get(threadId));
    
    if (log.isDebugEnabled())
      log.debug("Popup service with identifier '" + threadId + "' was created.");
    return threadId;
  }
  
  public String openMounted(final String url, final PopupWindowProperties properties) {
    Assert.notEmptyParam(this, url, "url");
    final String threadId = getRandomServiceId();

    Service service = threadServiceFactory.buildService(getEnvironment());
    startThreadPopupService(threadId, service);

    //add new, not yet opened popup to popup map
    popups.put(threadId, new PopupServiceInfo() {
      public PopupWindowProperties getPopupProperties() {
        return properties;
      }

      public String toURL() {
        //XXX: Should I use something more generic here?
        return url  + "?" + ThreadContext.THREAD_SERVICE_KEY + "=" + threadId;
      }
    });
    allPopups.put(threadId, popups.get(threadId));
    
    if (log.isDebugEnabled())
      log.debug("Popup service with identifier '" + threadId + "' was created for mounted URL '" + url + "'.");
    return threadId;
  }
  
  public void open(final String url, final PopupWindowProperties properties) {
    Assert.notEmptyParam(this, url, "url");
    popups.put(getRandomServiceId(), new PopupServiceInfo() {
      public PopupWindowProperties getPopupProperties() {
        return properties;
      }

      public String toURL() {
        return url;
      }
    });
  }

  public boolean close(String id) {
    Assert.notEmptyParam(this, id, "id");
    if (!allPopups.containsKey(id)) {
      log.warn("Attempt to close non-owned, unopened or already closed popup service with ID '" + id + "'.");
      return false;
    }

    try {
      closePopupThread(id);
    } catch (Exception e) {
      log.warn("Attempt to close registered popup service with ID '" + id + "' has failed with exception : ." + e);
      return false;
    } finally {
      removePopup(id);
    }

    if (log.isDebugEnabled())
      log.debug("Popup service with identifier '" + id + "' was closed");
    return true;
  }

  /**
   * Only removes the popup from the view of this {@link StandardPopupFilterWidget}, retains the servicing session thread.
   * @since 1.1
   */
  public void removePopup(String id) {
    allPopups.remove(id);
    popups.remove(id);
  }
  
   public void renderPopup(String id) {
     renderedPopups.add(id);
   }

  /**
   * @since 1.1
   */
  protected void closePopupThread(String id) {
    getServiceCtx(ThreadContext.class).close(id);
  }
  
  public Widget getOpener() {
    return this.opener;
  }
  
  public Map getPopups() {
    return Collections.unmodifiableMap(popups);
  }
  
  /* ************************************************************************************
   * Widget methods
   * ************************************************************************************/
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), PopupWindowContext.class, this);
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    super.action(path, input, output);
  }

  protected void update(InputData input) throws Exception {
    removeRenderedPopups();
    super.update(input);
  }

  protected void event(Path path, InputData input) throws Exception {
    if (input.getGlobalData().containsKey(PopupWindowContext.POPUPS_CLOSE_KEY)) {
      ThreadContext threadCtx = (ThreadContext)getEnvironment().getEntry(ThreadContext.class);
      Object id = threadCtx.getCurrentId();
      threadCtx.close(id);
      if (log.isDebugEnabled())
        log.debug("Closed thread with id : " + id);
    } else {
      super.event(path, input);
    }
  }

  /** 
   * Popups are rendered by pushing <code>Map &lt;String serviceId, PopupServiceInfo serviceInfo&gt;</code>
   * into output under the key {@link org.araneaframework.http.PopupWindowContext}.POPUPS_KEY
   */
  protected void render(OutputData output) throws Exception {
    removeRenderedPopups();

    super.render(output);
  }

  protected void removeRenderedPopups() {
    for (Iterator i = renderedPopups.iterator(); i.hasNext(); ) {
      popups.remove(i.next());
    }
    renderedPopups.clear();
  }
  
  /* ************************************************************************************
   * Internal methods
   * ************************************************************************************/
  protected void startThreadPopupService(String id, Service service) {
    startPopupService(id, service, ThreadContext.class);
  }
  
  protected void startPopupService(String id, Service service, Class serviceContext) {
    getServiceCtx(serviceContext).addService(id, service);
  }
  
  protected String getRandomServiceId() {
    return RandomStringUtils.randomAlphanumeric(8);
  }
  
  protected ManagedServiceContext getServiceCtx(Class contextClass) {
    return (ManagedServiceContext)(getEnvironment().requireEntry(contextClass));
  }
  
  protected TopServiceContext getTopServiceCtx() {
    return ((TopServiceContext)getEnvironment().requireEntry(TopServiceContext.class));
  }
  
  protected String getRequestURL() {
    HttpInputData input = (HttpInputData) getInputData();
    return ((HttpOutputData) input.getOutputData()).encodeURL(input.getContainerURL());
  }
  
  /* ************************************************************************************
   * PUBLIC INNER CLASSES
   * ************************************************************************************/
  /**
   * Message that registers opener as creator of the popup thread.
   */
  public static class OpenerRegistrationMessage implements Message {
	private boolean delivered= false;
    private Widget opener;

    public OpenerRegistrationMessage(Widget opener) {
      this.opener = opener;
    }

    public void send(Object id, Component component) {
      try {
        if (component instanceof StandardPopupFilterWidget) {
          execute(component);
        } else {
          component._getComponent().propagate(this);
        }
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }

    protected void execute(Component component) throws Exception {
      StandardPopupFilterWidget w = (StandardPopupFilterWidget) component;
      w.opener = opener;
      delivered = true;
    }
    
    public boolean isDelivered() {
      return delivered;
    }
  }

  public static class StandardPopupServiceInfo implements PopupServiceInfo {
    private boolean overrideTransaction = true;
    private String topServiceId;
    private String threadServiceId;
    private String requestUrl;
    private PopupWindowProperties popupProperties;

    public StandardPopupServiceInfo(String topServiceId, String threadId, PopupWindowProperties popupProperties, String requestUrl) {
      this.topServiceId = topServiceId;
      this.threadServiceId = threadId;
      this.requestUrl = requestUrl;
      this.popupProperties = popupProperties;
    }

    public String getTopServiceId() {
      return topServiceId;
    }

    public String getThreadServiceId() {
      return threadServiceId;
    }
    
    /** @since 1.0.4 */
    public void setTransactionOverride(boolean b) {
      this.overrideTransaction = b;
    }

    public String toURL() {
      StringBuffer url = new StringBuffer(requestUrl != null ? requestUrl : "");
      url.append('?').append((TopServiceContext.TOP_SERVICE_KEY + "=")).append(topServiceId);
      if (threadServiceId != null) {
        url.append("&" + ThreadContext.THREAD_SERVICE_KEY + "=");
        url.append(threadServiceId);
      }

      if (overrideTransaction)
        url.append('&').append((TransactionContext.TRANSACTION_ID_KEY + "=")).append(TransactionContext.OVERRIDE_KEY);
      return url.toString();
    }

    public PopupWindowProperties getPopupProperties() {
      return popupProperties;
    }
  }

  /* ************************************************************************************
   * Internal inner classes
   * ************************************************************************************/

  /**
   * @since 1.1
   */
  public Map getRegions() {
    Map popups = getPopups();
    if (popups == null || popups.isEmpty())
      return null;

    // clear popup list
    this.popups = new HashMap();

    JsonArray popupArray = new JsonArray();
    for (Iterator i = popups.entrySet().iterator(); i.hasNext(); ) {
      JsonObject popupObject = new JsonObject();
      Map.Entry popup = (Map.Entry) i.next();
      String serviceId = (String) popup.getKey();
      PopupServiceInfo serviceInfo = (PopupServiceInfo) popup.getValue();
      popupObject.setStringProperty("popupId", serviceId);
      popupObject.setStringProperty("windowProperties", serviceInfo.getPopupProperties() != null ? serviceInfo.getPopupProperties().toString() : "");
      popupObject.setStringProperty("url", serviceInfo.toURL());
      popupArray.append(popupObject.toString());
      renderedPopups.add(popup.getKey());
    }
    Map regions = new HashMap();
    regions.put(POPUP_REGION_KEY, popupArray.toString());
    return regions;
  }

}
