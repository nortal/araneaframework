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

import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.SessionServiceContext;

/**
 * A {@link org.araneaframework.framework.router.BaseServiceRouterService} which handles sessions.
 * Enriches the environment with an object of this class under the key
 * SessionServiceContext.class.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardSessionServiceRouterService extends BaseServiceRouterService {
  @Override
  protected Environment getChildEnvironment(String serviceId) throws Exception {
    return new StandardEnvironment(super.getChildEnvironment(serviceId), SessionServiceContext.class, new ServiceRouterContextImpl(serviceId));
  }
  
  private class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl implements SessionServiceContext {
    protected ServiceRouterContextImpl(String serviceId) {
      super(serviceId);
    }
  }

  @Override
  protected String getServiceKey() throws Exception {
    return SessionServiceContext.SESSION_SERVICE_KEY;
  }
}
