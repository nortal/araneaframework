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

import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.TopServiceContext;

/**
 * A top router service. Enriches the environment with an object of this class under the key
 * StandardTopServiceRouterService.class.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardTopServiceRouterService extends BaseExpiringServiceRouterService {

  /**
   * Creates a new top-level expiring service router.
   * 
   * @see TopServiceContext
   */
  public StandardTopServiceRouterService() {
    super(TopServiceContext.TOP_SERVICE_KEY, TopServiceContext.KEEPALIVE_KEY);
  }

  /**
   * Enhances the child service environment with the {@link TopServiceContext} entry.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Environment getChildEnvironment(String serviceId) {
    return new StandardEnvironment(super.getChildEnvironment(serviceId), TopServiceContext.class,
        new ServiceRouterContextImpl(serviceId));
  }

  private class ServiceRouterContextImpl extends BaseExpiringServiceRouterService.ServiceRouterContextImpl implements
      TopServiceContext {

    protected ServiceRouterContextImpl(String serviceId) {
      super(serviceId);
    }
  }
}
