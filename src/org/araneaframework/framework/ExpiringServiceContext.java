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
import java.util.Map;

/**
 * Router service that kills child services after specified period of inactivity
 * is over. BaseExpiringServiceRouterService implementation checks for child
 * services whose lifetime has expired only when servicing request.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface ExpiringServiceContext extends Serializable {

  /**
   * Returns all expiring services. Keys of the Map are services' keepalive id's
   * (String). Values are services' time to live in milliseconds (Long).
   */
  public Map<String, Long> getServiceTTLMap();

}
