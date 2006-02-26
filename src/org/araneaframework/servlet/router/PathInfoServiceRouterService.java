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

package org.araneaframework.servlet.router;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.router.BaseServiceRouterService;
import org.araneaframework.servlet.PathInfoServiceContext;
import org.araneaframework.servlet.ServletInputData;

public class PathInfoServiceRouterService extends BaseServiceRouterService {

  protected Object getServiceId(InputData input) throws Exception {    
    String result = ((ServletInputData) input).getRequest().getPathInfo();
    return result == null ? null : result.substring(1);
  }
  
  protected Object getServiceKey() throws Exception {
    return "pathInfoServiceId";
  }
  
  protected Environment getChildEnvironment(Object serviceId) throws Exception {
    Map entries = new HashMap();    
    entries.put(PathInfoServiceContext.class, new ServiceRouterContextImpl(serviceId));
    return new StandardEnvironment(getEnvironment(), entries);
  }
  
  private class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl implements PathInfoServiceContext {
    protected ServiceRouterContextImpl(Object serviceId) {
      super(serviceId);
    }
  }
}
