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
  private static final Logger log = Logger.getLogger(BaseExpiringServiceRouterService.class);
  private Map timeCapsules;

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    killExpiredServices(System.currentTimeMillis());

	super.action(path, input, output);

    Object serviceId = getServiceId(input);
    TimeCapsule capsule = (TimeCapsule)getTimeCapsules().get(serviceId);

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
  
  private synchronized Map getTimeCapsules() {
    if (timeCapsules == null)
      timeCapsules = Collections.synchronizedMap(new HashMap());

    return timeCapsules;
  }

  public static class TimeCapsule implements Serializable {
    private Long ttl;
    private Long lastActivity;
    
    public TimeCapsule(Long ttl) {
      this.ttl = ttl;
      lastActivity = new Long(new Date().getTime());
    }
    
    public void setLastActivity(Long lastActivity) {
      this.lastActivity = lastActivity;
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
