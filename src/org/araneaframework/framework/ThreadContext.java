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

package org.araneaframework.framework;

import java.io.Serializable;

/**
 * A context for supporting multiple threads (windows) inside a single session. When
 * a user opens a new window there needs to be means to have a different set of child
 * services and widgets for the new window but the session should remain the same.
 * <p>
 * {@link org.araneaframework.framework.router.StandardThreadServiceRouterService} adds a
 * ThreadContext.class to the environment for the children to be able to access.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface ThreadContext extends ManagedServiceContext, Serializable {
  /**
  * The key of the thread-service's id in the request.
  */
  public static final String THREAD_SERVICE_KEY = "araThreadServiceId";
  public static final String KEEPALIVE_KEY = "threadServiceKeepAlive";
}
