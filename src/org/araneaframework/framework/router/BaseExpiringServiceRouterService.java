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
import java.util.Date;
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
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ExpiringServiceContext;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * Router service that kills child services after specified period of inactivity
 * is over. This implementation checks for child services whose lifetime has
 * expired only when servicing request.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseExpiringServiceRouterService
  extends BaseServiceRouterService implements ExpiringServiceContext {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(
      BaseExpiringServiceRouterService.class);

  private Map<Object, TimeCapsule> timeCapsules;

  private Map<String, Long> serviceTTLMap;

  public Map getServiceTTLMap() {
    if (serviceTTLMap == null) {
      return null;
    }
    return Collections.unmodifiableMap(serviceTTLMap);
  }

  protected void action(Path path, InputData input, OutputData output)
      throws Exception {
    TimeCapsule capsule = null;

    if (timeCapsules != null) {
      killExpiredServices(System.currentTimeMillis());
      capsule = getTimeCapsules().get(getServiceId(input));
    }

    serviceTTLMap = null;

    if (capsule != null) {
      ExpiringServiceContext esc =
        EnvironmentUtil.getExpiringServiceContext(getEnvironment());

      if (esc != null) {
        serviceTTLMap = esc.getServiceTTLMap();
      }

      if (serviceTTLMap == null) {
        serviceTTLMap = new HashMap<String, Long>();
      }

      serviceTTLMap.put(getKeepAliveKey(), capsule.getTimeToLive());
    }

    if (!isKeepAlive(input)) {
      super.action(path, input, output);
    } else {
      if (log.isDebugEnabled())
        log.debug(Assert.thisToString(this)
            + " received keepalive for service '"
            + getServiceId(input).toString() + "'");
    }

    if (capsule != null) {
      capsule.setLastActivity(new Long(System.currentTimeMillis()));
    }
  }

  protected Environment getChildEnvironment(String serviceId) throws Exception {
    Map entries = new HashMap();
    entries.put(ManagedServiceContext.class,
        new ServiceRouterContextImpl(serviceId));
    entries.put(ExpiringServiceContext.class, this);
    return new StandardEnvironment(super.getChildEnvironment(serviceId),
        entries);
  }

  protected void closeService(Object serviceId) {
    super.closeService(serviceId);
    getTimeCapsules().remove(serviceId);
  }

  protected void killExpiredServices(long now) {
    synchronized (getTimeCapsules()) {
      for (Iterator<Map.Entry<Object, TimeCapsule>> i = getTimeCapsules().entrySet().iterator(); i.hasNext();) {
        Map.Entry<Object, TimeCapsule> entry = i.next();

        if (entry.getValue().isExpired(now)) {
          super.closeService(entry.getKey());
          i.remove();

          if (log.isDebugEnabled()) {
            log.debug(Assert.thisToString(this) + " killed expired service '"
                + entry.getKey().toString() + "'.");
          }
        }
      }
    }
  }

  /**
   * Returns the key which presence in {@link org.araneaframework.InputData}
   * indicates that request is a keepalive request for this
   * {@link BaseExpiringServiceRouterService}.
   * 
   * @return a keepalive key for this {@link BaseExpiringServiceRouterService}
   */
  public abstract String getKeepAliveKey();

  protected boolean isKeepAlive(InputData input) {
    return input.getGlobalData().get(getKeepAliveKey()) != null;
  }

  private synchronized Map<Object, TimeCapsule> getTimeCapsules() {
    if (timeCapsules == null)
      timeCapsules = Collections.synchronizedMap(new HashMap<Object, TimeCapsule>());
    return timeCapsules;
  }

  public static class TimeCapsule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ttl;

    private Long lastActivity;

    public TimeCapsule(Long timeToLive) {
      this.ttl = timeToLive;
      lastActivity = new Long(new Date().getTime());
    }

    public void setLastActivity(Long lastActivity) {
      this.lastActivity = lastActivity;
    }

    public Long getTimeToLive() {
      return this.ttl;
    }

    public boolean isExpired(long time) {
      return (time > lastActivity.longValue() + ttl.longValue());
    }
  }

  protected class ServiceRouterContextImpl
    extends BaseServiceRouterService.ServiceRouterContextImpl {

    private static final long serialVersionUID = 1L;

    protected ServiceRouterContextImpl(String serviceId) {
      super(serviceId);
    }

    @Override
    public Service addService(String id, Service service, Long timeToLive) {
      Service result = super.addService(id, service);
      if (timeToLive != null)
        getTimeCapsules().put(id, new TimeCapsule(timeToLive));
      return result;
    }
  }
}
