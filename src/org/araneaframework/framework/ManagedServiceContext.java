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

package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.Service;

/**
 * A managed service context provides the means to have more control over child services. A good example is
 * {@link org.araneaframework.framework.router.StandardThreadServiceRouterService} which handles requests from multiple
 * threads (windows) in the same session by having different children for different windows and the current service ID
 * is determined via <code>getCurrentId()</code>. Also note that there can be many service contexts each taking care of
 * their own "layer".
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface ManagedServiceContext extends Serializable {

  /**
   * Returns the current service context ID.
   * 
   * @return The current service context ID.
   */
  String getCurrentId();

  /**
   * Adds and initializes a child service with the specified ID.
   * 
   * @param id The ID to be registered for the new service (required).
   * @param service The (uninitialized) service to register (required).
   * @return The registered service.
   */
  Service addService(String id, Service service);

  /**
   * Adds a child service with the specified ID, and specifies that service may be killed by service manager after it
   * has been inactive for specified time. When the service is killed or whether it is killed at all is up to the
   * implementation.
   * 
   * @param id The ID to be registered for the new service (required).
   * @param service The (uninitialized) service to register (required).
   * @param timeToLive Optional time-to-live (usually in milliseconds) used when the service supports it. (No error when
   *          a negative value is specified.)
   * @return The registered service.
   */
  Service addService(String id, Service service, Long timeToLive);

  /**
   * Returns a child service by ID.
   * 
   * @param id The ID used used for child service lookup.
   * @return A child service with the specified ID, or <code>null</code> when it was not found.
   */
  Service getService(String id);

  /**
   * Closes the service with provided ID. Nothing happens when no such service exists.
   * 
   * @param id The ID of the service to be closed.
   */
  void close(String id);

}
