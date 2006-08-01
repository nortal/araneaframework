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

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.TopServiceContext;

/**
 * A top router service. Enriches the environment with an object
 * of this class under the key StandardTopServiceRouterService.class.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardTopServiceRouterService extends BaseServiceRouterService {
  protected Object getServiceId(InputData input) throws Exception {
    return input.getGlobalData().get(TopServiceContext.TOP_SERVICE_KEY);
  }
  
  protected Object getServiceKey() throws Exception {
    return TopServiceContext.TOP_SERVICE_KEY;
  }

  protected Environment getChildEnvironment(Object serviceId) throws Exception {
    Map entries = new HashMap();    
    entries.put(TopServiceContext.class, new ServiceRouterContextImpl(serviceId));
    return new StandardEnvironment(super.getChildEnvironment(serviceId), entries);
  }
    
  private class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl implements TopServiceContext {
    protected ServiceRouterContextImpl(Object serviceId) {
      super(serviceId);
    }
  }
}
