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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ThreadContext;

/**
 * A {@link org.araneaframework.framework.router.BaseServiceRouterService} which handles threads.
 * Enriches the environment with an object of this class under the key
 * StandardThreadServiceRouterService.class.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardThreadServiceRouterService extends BaseServiceRouterService {
  private Map timeCapsules;

  protected Object getServiceId(InputData input) throws Exception {
    return input.getGlobalData().get(ThreadContext.THREAD_SERVICE_KEY);
  }

  protected Environment getChildEnvironment(Object serviceId) throws Exception {
    return new StandardEnvironment(super.getChildEnvironment(serviceId), ThreadContext.class, new ServiceRouterContextImpl(serviceId));
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    long now = new Date().getTime();

    for (Iterator i = getTimeCapsules().entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      TTLCapsule capsule = (TTLCapsule)entry.getValue();
      if (capsule.isExpired(now)) {
    	 closeService(entry.getKey());
      }
    }

    super.action(path, input, output);
  }
  
  private class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl implements ThreadContext {
    protected ServiceRouterContextImpl(Object serviceId) {
      super(serviceId);
    }
    
    public Service addService(Object id, Service service, Long timeToLive) {
      Service result = addService(id, service);
      if (timeToLive != null) {
        timeCapsules.put(id, new TTLCapsule(timeToLive));
      }
      return result;
    }
  }

  protected Object getServiceKey() throws Exception {
    return ThreadContext.THREAD_SERVICE_KEY;
  }
  
  synchronized protected Map getTimeCapsules() {
    if (timeCapsules == null)
      timeCapsules = new HashMap();

    return timeCapsules;
  }
}
