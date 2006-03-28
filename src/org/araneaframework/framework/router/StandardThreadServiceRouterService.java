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

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
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
  private static final Logger log = Logger.getLogger(StandardThreadServiceRouterService.class);
  
  /**
   * The key of the thread-service's id in the request.
   */
  public static final String THREAD_SERVICE_KEY = "threadServiceId";
  
  protected Object getServiceId(InputData input) throws Exception {
    return input.getGlobalData().get(THREAD_SERVICE_KEY);
  }

  protected Environment getChildEnvironment(Object serviceId) throws Exception {
    Map entries = new HashMap();    
    entries.put(ThreadContext.class, new ServiceRouterContextImpl(serviceId));
    return new StandardEnvironment(super.getChildEnvironment(serviceId), entries);
  }
  
  private class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl implements ThreadContext {
    protected ServiceRouterContextImpl(Object serviceId) {
      super(serviceId);
    }    
  }

  protected Object getServiceKey() throws Exception {
    return THREAD_SERVICE_KEY;
  }
}
