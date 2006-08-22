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
import org.araneaframework.Service;

/**
 * A managed service context provides the means to have more control over
 * child services. A good example is {@link org.araneaframework.framework.router.StandardThreadServiceRouterService}
 * which handles requests from multiple threads(windows) in the same session by having
 * different children for different windows and the current service id is determined via
 * <code>getCurrentId()</code>.
 * <br><br>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface ManagedServiceContext extends Serializable {
  /**
   * Returns the id of the current service. 
   */
  public Object getCurrentId();
  
  /**
   * Adds a child service service with the specified id.
   */
  public Service addService(Object id, Service service);
  
  /**
  * @return a child service with the specified id.
  */
  public Service getService(Object id);
  
  /**
   * Closes the service under the key id.
   */
  public void close(Object id);
}
