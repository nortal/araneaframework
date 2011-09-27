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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.ExpiringServiceContext;
import org.araneaframework.framework.ManagedServiceContext;

/**
 * Router service that kills child services after specified period of inactivity is over. This implementation checks for
 * child services whose lifetime has expired only when servicing request.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseExpiringServiceRouterService extends BaseServiceRouterService implements
    ExpiringServiceContext {

  private static final Log LOG = LogFactory.getLog(BaseExpiringServiceRouterService.class);

  private Map<String, TimeCapsule> timeCapsules;

  private Map<String, Long> serviceTTLMap;

  private final String keepaliveKey;

  /**
   * Initializes the base expiring service router with given keys for child-service ID and keep-alive values lookup from
   * input data.
   * 
   * @param serviceKey The key that is used for child-service key lookup from input data.
   * @param keepaliveKey The key that is used for keep-alive marker lookup from input data.
   */
  public BaseExpiringServiceRouterService(String serviceKey, String keepaliveKey) {
    super(serviceKey);
    this.keepaliveKey = keepaliveKey;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Long> getServiceTTLMap() {
    return this.serviceTTLMap == null ? null : Collections.unmodifiableMap(this.serviceTTLMap);
  }

  /**
   * Returns the key which presence in input data indicates that request is a keep-alive request for this expiring
   * service router.
   * 
   * @return The keep-alive input data key for this expiring service router.
   */
  public final String getKeepAliveKey() {
    return this.keepaliveKey;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    TimeCapsule capsule = null;

    if (!getTimeCapsules().isEmpty()) {
      killExpiredServices(System.currentTimeMillis());
      capsule = getTimeCapsules().get(getServiceId(input));
    }

    this.serviceTTLMap = null;

    if (capsule != null) {
      ExpiringServiceContext esc = getEnvironment().getEntry(ExpiringServiceContext.class);

      if (esc != null) {
        this.serviceTTLMap = esc.getServiceTTLMap();
      }

      if (this.serviceTTLMap == null) {
        this.serviceTTLMap = new HashMap<String, Long>();
      }

      this.serviceTTLMap.put(getKeepAliveKey(), capsule.getTimeToLive());
    }

    if (!isKeepAlive(input)) {
      super.action(path, input, output);
    } else if (LOG.isDebugEnabled()) {
      LOG.debug(Assert.thisToString(this) + " received keep-alive for service '" + getServiceId(input) + "'.");
    }

    if (capsule != null) {
      capsule.setLastActivityNow();
    }
  }

  @Override
  protected Environment getChildEnvironment(String serviceId) {
    Map<Class<?>, Object> entries = new HashMap<Class<?>, Object>(2);
    entries.put(ManagedServiceContext.class, new ServiceRouterContextImpl(serviceId));
    entries.put(ExpiringServiceContext.class, this);
    return new StandardEnvironment(super.getEnvironment(), entries);
  }

  @Override
  protected void closeService(String serviceId) {
    super.closeService(serviceId);
    getTimeCapsules().remove(serviceId);
  }

  /**
   * Kills expired service based on the given time-stamp.
   * 
   * @param now The time-stamp in milliseconds used for determining whether services have expired.
   */
  protected void killExpiredServices(long now) {
    synchronized (getTimeCapsules()) {
      for (Iterator<Map.Entry<String, TimeCapsule>> i = getTimeCapsules().entrySet().iterator(); i.hasNext();) {
        Map.Entry<String, TimeCapsule> entry = i.next();

        if (entry.getValue().isExpired(now)) {
          super.closeService(entry.getKey());

          i.remove();

          if (LOG.isDebugEnabled()) {
            LOG.debug(Assert.thisToString(this) + " killed expired service '" + entry.getKey() + "'.");
          }
        }
      }
    }
  }

  /**
   * Checks whether the input data contains a keep-alive parameter. Note that when there is a value for that parameter,
   * it won't have any effect.
   * 
   * @param input Input data for the service.
   * @return A Boolean that is <code>true</code> when the input data contains a keep-alive parameter.
   */
  protected final boolean isKeepAlive(InputData input) {
    return input.getGlobalData().containsKey(this.keepaliveKey);
  }

  /**
   * Returns <tt>TimeCapsule</tt>s as a not <code>null</code> unmodifiable map.
   * 
   * @return An unmodifiable map of <tt>TimeCapsule</tt>s.
   */
  private synchronized Map<String, TimeCapsule> getTimeCapsules() {
    if (this.timeCapsules == null) {
      this.timeCapsules = Collections.synchronizedMap(new HashMap<String, TimeCapsule>(0));
    }
    return this.timeCapsules;
  }

  /**
   * Helper class for storing information about a time-to-live value of a service and to check whether the service has
   * expired.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public static class TimeCapsule implements Serializable {

    private final long ttl;

    private long lastActivity;

    /**
     * Creates a new <tt>TimeCapsule</tt> with given time-to-live information.
     * 
     * @param timeToLive A time-to-live value in milliseconds (all integer values accepted).
     */
    public TimeCapsule(long timeToLive) {
      this.ttl = timeToLive;
      setLastActivityNow();
    }

    /**
     * Updates the last activity time-stamp to the current time-stamp.
     */
    public void setLastActivityNow() {
      this.lastActivity = System.currentTimeMillis();
    }

    /**
     * Provides the time-to-live value used by this helper.
     * 
     * @return A time-to-live value in milliseconds.
     */
    public long getTimeToLive() {
      return this.ttl;
    }

    /**
     * Checks whether this <tt>TimeCapsule</tt> is expired.
     * 
     * @param time The time-stamp in milliseconds against which this object is tested.
     * @return A Boolean that is <code>true</code> when this <tt>TimeCapsule</tt> is expired in terms of the given
     *         time-stamp.
     */
    public boolean isExpired(long time) {
      return time > this.lastActivity + this.ttl;
    }
  }

  /**
   * Extends parent service router context with expiring service support.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  protected class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl {

    /**
     * Creates a new service router context for given child service ID.
     * 
     * @param serviceId The child service ID.
     */
    protected ServiceRouterContextImpl(String serviceId) {
      super(serviceId);
    }

    @Override
    public Service addService(String id, Service service, Long timeToLive) {
      Service result = super.addService(id, service);
      if (timeToLive != null) {
        getTimeCapsules().put(id, new TimeCapsule(timeToLive));
      }
      return result;
    }
  }
}
