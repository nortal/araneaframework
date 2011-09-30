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

package org.araneaframework.framework.router;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.exception.NoSuchServiceException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.ManagedServiceContext;

/**
 * A router service has multiple child services, they form a service map. One of the services is a default one.
 * <p>
 * This class builds the base functionality for all router services.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public abstract class BaseServiceRouterService extends BaseService {

  private static final Log LOG = LogFactory.getLog(BaseServiceRouterService.class);

  private Map<String, Service> serviceMap;

  private String defaultServiceId;

  private final String serviceKey;

  /**
   * Initializes the base service router with given input data key.
   * 
   * @param serviceKey The key that is used for child-service key lookup from input data.
   */
  public BaseServiceRouterService(String serviceKey) {
    this.serviceKey = serviceKey;
    Assert.notNullParam(this, serviceKey, "serviceKey");
  }

  /**
   * Method for specifying the map of service IDs and corresponding services.
   * 
   * @param serviceMap A map of services by their IDs.
   */
  public void setServiceMap(Map<String, Service> serviceMap) {
    this.serviceMap = serviceMap;
  }

  /**
   * Method for specifying the ID of the default service to serve. The ID is used as a key in the service map.
   * 
   * @param defaultServiceId The ID of default service.
   */
  public void setDefaultServiceId(String defaultServiceId) {
    this.defaultServiceId = defaultServiceId;
  }

  /**
   * Initialize all the services in the service map with <code>getChildEnvironment(Object serviceId)</code>. The
   * serviceId is the key of the service in the service map.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void init() throws Exception {
    // adds serviceMap entries as child services
    if (this.serviceMap != null) {
      for (Map.Entry<String, Service> entry : this.serviceMap.entrySet()) {
        _addComponent(entry.getKey(), entry.getValue(), getScope(), getChildEnvironment(entry.getKey()));
      }
      // free extra references
      this.serviceMap = null;
    }
  }

  @Override
  protected void propagate(Message message) throws Exception {
    for (Component child : _getChildren().values()) {
      message.send(null, child);
    }
  }

  /**
   * Uses the map to route the request to the service under getServiceId(input). The id of the service is determined by
   * <code>getServiceId(input)</code>. If the service id cannot be determined then the default id is used set via
   * <code>setDefaultServiceId(Object)</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    Object currentServiceId = getServiceId(input);

    Assert.notNull(this, currentServiceId, "Router found current service id to be null, which means that it could "
        + "not be read from request and default value is not defined too.");

    if (_getChildren().containsKey(currentServiceId)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Routing action to service '" + currentServiceId + "' under router '" + getClass().getName() + "'");
      }

      ((Service) _getChildren().get(currentServiceId))._getService().action(path, input, output);
    } else {
      throw new NoSuchServiceException("Service '" + currentServiceId + "' was not found under router '"
          + getClass().getName() + "'!");
    }
  }

  // Callbacks

  /**
   * Creates an environment for the service with given ID.
   * 
   * @param serviceId The ID of the service for which the environment will be created.
   * @return The created environment for the child service.
   */
  protected Environment getChildEnvironment(String serviceId) {
    return new StandardEnvironment(getEnvironment(), ManagedServiceContext.class, new ServiceRouterContextImpl(
        serviceId));
  }

  /**
   * Resolves the service ID value from the input data, or returns the default service ID when no such ID is found.
   * 
   * @param input Input data for the service.
   * @return The ID of service to serve.
   */
  protected final String getServiceId(InputData input) {
    String id = getServiceIdFromInput(input);
    return id == null ? this.defaultServiceId : id;
  }

  /**
   * Resolves the service ID value from the input data.
   * 
   * @param input Input data for the service.
   * @return The ID of a service from input data.
   */
  protected String getServiceIdFromInput(InputData input) {
    return input.getGlobalData().get(this.serviceKey);
  }

  /**
   * Returns the ID of the default service to serve.
   * 
   * @return The default service ID.
   */
  protected final String getDefaultServiceId() {
    return this.defaultServiceId;
  }

  /**
   * Closes and destroys the service with given ID. Nothing happens when the service does not exist.
   * 
   * @param serviceId The ID for looking up the service to destroy.
   */
  protected void closeService(String serviceId) {
    Service targetService = (Service) _getChildren().get(serviceId);
    if (targetService != null) { // Make sure it exists!
      targetService._getComponent().destroy();
      _getChildren().remove(serviceId);
    }
  }

  /**
   * A managed service context implementation for router service child services.
   * 
   * @author Toomas Römer (toomas@webmedia.ee)
   */
  protected class ServiceRouterContextImpl implements ManagedServiceContext {

    private final String currentServiceId;

    /**
     * Creates a new service router context for given child service ID.
     * 
     * @param serviceId The child service ID.
     */
    protected ServiceRouterContextImpl(String serviceId) {
      this.currentServiceId = serviceId;
    }

    /**
     * {@inheritDoc}
     */
    public String getCurrentId() {
      return this.currentServiceId;
    }

    /**
     * {@inheritDoc}
     */
    public Service getService(String id) {
      return (Service) _getChildren().get(id);
    }

    /**
     * {@inheritDoc}
     */
    public Service addService(String id, Service service) {
      try {
        _addComponent(id, service, null, getChildEnvironment(id));
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
      return service;
    }

    /**
     * {@inheritDoc}
     */
    public Service addService(String id, Service service, Long timeToLive) {
      if (LOG.isWarnEnabled()) {
        LOG.warn(getClass().getName() + ".addService(String id, Service service, Long timeToLive) ignores timeToLive "
            + "attribute. Just addService(String id, Service service) should be used.");
      }
      return addService(id, service);
    }

    /**
     * {@inheritDoc}
     */
    public void close(String id) {
      closeService(id);
    }
  }
}
