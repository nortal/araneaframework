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

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ThreadContext;

/**
 * Router service that kills child services after specified period of inactivity is over.
 * This implementation checks for child services whose lifetime has expired only when 
 * servicing request. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseExpiringServiceRouterService extends BaseServiceRouterService {
  /** {@link OutputData} key under which expiring service lifetime expectancies are stored. */
  public static final String SERVICE_TTL_MAP = "serviceTTLMap";
  public static final String KEEPALIVE_KEYS = "keepAliveKeys";

  private static final Logger log = Logger.getLogger(BaseExpiringServiceRouterService.class);
  private Map timeCapsules;

  protected void action(Path path, InputData input, OutputData output) throws Exception {
//	  java.util.ConcurrentModificationException
//		at java.util.HashMap$HashIterator.nextEntry(HashMap.java:782)
//		at java.util.HashMap$EntryIterator.next(HashMap.java:824)
//		at org.araneaframework.framework.router.BaseExpiringServiceRouterService.killExpiredServices(BaseExpiringServiceRouterService.java:97)
//		at org.araneaframework.framework.router.BaseExpiringServiceRouterService.action(BaseExpiringServiceRouterService.java:51)
    killExpiredServices(System.currentTimeMillis());
    
    TimeCapsule capsule = (TimeCapsule)getTimeCapsules().get(getServiceId(input));
    Map serviceTTLMap = null;
    if (capsule != null) {
      serviceTTLMap = (Map) output.getAttribute(BaseExpiringServiceRouterService.SERVICE_TTL_MAP);
      if (serviceTTLMap == null) {
        serviceTTLMap = new HashMap();
      }

      serviceTTLMap.put(getServiceKey(), capsule.getTimeToLive());
    }

    if (!isKeepAlive(input)) {
      Map keepAliveKeys = (Map)output.getAttribute(BaseExpiringServiceRouterService.KEEPALIVE_KEYS);
      if (keepAliveKeys == null)
        keepAliveKeys = new HashMap();
      keepAliveKeys.put(getServiceKey(), getKeepAliveKey());
      try {
    	output.pushAttribute(BaseExpiringServiceRouterService.SERVICE_TTL_MAP, serviceTTLMap);
    	output.pushAttribute(BaseExpiringServiceRouterService.KEEPALIVE_KEYS, keepAliveKeys);
    	super.action(path, input, output);
      } finally {
    	output.popAttribute(BaseExpiringServiceRouterService.KEEPALIVE_KEYS);
        output.popAttribute(BaseExpiringServiceRouterService.SERVICE_TTL_MAP);
      }
    } else {
      if (log.isDebugEnabled())
        log.debug(Assert.thisToString(this) + " received keepalive for service '" + getServiceId(input).toString() + "'");
    }

    if (capsule != null)
      capsule.setLastActivity(new Long(System.currentTimeMillis()));
  }

  protected Environment getChildEnvironment(Object serviceId) throws Exception {
    return new StandardEnvironment(super.getChildEnvironment(serviceId), ThreadContext.class, new ServiceRouterContextImpl(serviceId));
  }

  protected void closeService(Object serviceId) {
    super.closeService(serviceId);
    getTimeCapsules().remove(serviceId);
  }

  protected synchronized void killExpiredServices(long now) {
    for (Iterator i = getTimeCapsules().entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      if (((TimeCapsule)entry.getValue()).isExpired(now)) {
        if (log.isDebugEnabled())
          log.debug(Assert.thisToString(this) + " killed expired service '" + entry.getKey().toString() + "'.");
        closeService(entry.getKey());
      }
    }
  }

  public abstract Object getKeepAliveKey();

  protected boolean isKeepAlive(InputData input) {
    return input.getGlobalData().get(getKeepAliveKey()) != null;
  }

  private synchronized Map getTimeCapsules() {
    if (timeCapsules == null)
      timeCapsules = Collections.synchronizedMap(new HashMap());

    return timeCapsules;
  }

  public static class TimeCapsule implements Serializable {
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
  
  protected class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl {
    protected ServiceRouterContextImpl(Object serviceId) {
      super(serviceId);
    }

    public Service addService(Object id, Service service, Long timeToLive) {
      Service result = super.addService(id, service);
      if (timeToLive != null)
        getTimeCapsules().put(id, new TimeCapsule(timeToLive));

      return result;
    }
  }
}
