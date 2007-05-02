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

package org.araneaframework.framework.router;

import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.NoSuchServiceException;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.ManagedServiceContext;

/**
 * A router service consists of multiple child services, they form a service map.
 * One of the services is a default one.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class BaseServiceRouterService extends BaseService {
  private static final Logger log = Logger.getLogger(BaseServiceRouterService.class);
  
  private Map serviceMap;
  protected Object defaultServiceId;
  
  /**
   * Sets the service map. Key is the id of the service, value is the service.
   */
  public void setServiceMap(Map serviceMap) {
     this.serviceMap = serviceMap;
  }
  
  /**
   * Sets the default service id. The id is used as a key
   * in the service map.
   */
  public void setDefaultServiceId(Object defaultServiceId) {
    this.defaultServiceId = defaultServiceId;
  }
  
  /**
   * Initialize all the services in the service map with
   * <code>getChildEnvironment(Object serviceId)</code>. The serviceId is the key
   * of the service in the service map. 
   */
  protected void init() throws Exception {
    // adds serviceMap entries as child services
    Iterator ite = serviceMap.entrySet().iterator();
    while(ite.hasNext()) {
      Map.Entry entry = (Map.Entry) ite.next();
      _addComponent(entry.getKey(), (Service) entry.getValue(), getScope(), getChildEnvironment(entry.getKey()));
    }
    // free extra references
    serviceMap = null;
  }
  
  protected void propagate(Message message) throws Exception {
    Iterator ite =  _getChildren().entrySet().iterator();
    while(ite.hasNext()) {
      Map.Entry entry = (Map.Entry) ite.next();
      message.send(null, (Service) entry.getValue());
    }
  }
  
  /**
   * Uses the map to route the request to the service under getServiceId(input). The id of the
   * service is determined by <code>getServiceId(input)</code>. If the service id cannot be
   * determined then the default id is used set via <code>setDefaultServiceId(Object)</code>.
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    Object currentServiceId = getServiceId(input);

    Assert.notNull(this, currentServiceId, 
    		"Router found current service id to be null, which means that it could not be " +
    		"read from request and default value is not defined too.");

    if (_getChildren().containsKey(currentServiceId)) {
      log.debug("Routing action to service '"+currentServiceId+"' under router '" + getClass().getName() + "'");
      ((Service) _getChildren().get(currentServiceId))._getService().action(path, input, output);
    }
    else {
      throw new NoSuchServiceException("Service '" + currentServiceId +"' was not found under router '" + getClass().getName() + "'!");
    }
  }
  
  // Callbacks 
  protected Environment getChildEnvironment(Object serviceId) throws Exception {
    return new StandardEnvironment(getEnvironment(), ManagedServiceContext.class, new ServiceRouterContextImpl(serviceId));
  }
  
  /**
   * Returns the service id of the request. By default returns the parameter value of the request
   * under the key <code>getServiceKey()</code>. Returns <code>defaultServiceId</code> when input
   * has no service information specified.
   */
  protected Object getServiceId(InputData input) throws Exception{
    Object id = getServiceIdFromInput(input);
    if (id == null)
      id = getDefaultServiceId();
    return id;
  }

  /**
   * Returns the service id read from input. 
   */
  protected Object getServiceIdFromInput(InputData input) throws Exception {
    return input.getGlobalData().get(getServiceKey());
  }

  /**
   * Returns the default service id.
   */
  protected Object getDefaultServiceId() {
    return defaultServiceId;
  }

  /**
   * Every service has its own key under which the service service id can be found in the request.
   * This method returns that key. 
   */
  protected abstract Object getServiceKey() throws Exception;
  
  protected void closeService(Object serviceId) {
    ((Service)_getChildren().get(serviceId))._getComponent().destroy();
    _getChildren().remove(serviceId);
  }
  
  protected class ServiceRouterContextImpl implements ManagedServiceContext {
    private Object currentServiceId;
    
    protected ServiceRouterContextImpl(Object serviceId) {
      currentServiceId = serviceId;
    }
    
    public Object getCurrentId() {
      return currentServiceId;
    }
    
    public Service getService(Object id) {
      return (Service)_getChildren().get(id);
    }
    
    public Service addService(Object id, Service service) {
      try {
        _addComponent(id, service, null, getChildEnvironment(id));
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
      return service;
    }
    
    public Service addService(Object id, Service service, Long timeToLive) {
      Service result = addService(id, service);
      if (log.isEnabledFor(Level.WARN)) {
        log.warn(getClass().getName() + 
        		".addService(Object id, Service service, Long timeToLive) ignores timeToLive attribute." +
        		"Just addService(Object id, Service service) should be used.");
      }
      return result;
    }

	public void close(Object id) {
      closeService(id);
    }
  }
}
