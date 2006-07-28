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
package org.araneaframework.framework.util;

import java.io.Serializable;

/**
 * Aranea services live under different routers. Sometimes need arises
 * to access services directly from URL. Interface implementations
 * should provide means to constructing that URL.
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public interface ServiceInfo extends Serializable {
  /**
   * @return popup service's info translated into String containing URL style parameters. 
   */
  public String toURL();
}
