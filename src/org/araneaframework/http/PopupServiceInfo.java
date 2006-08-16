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

package org.araneaframework.http;

import java.io.Serializable;
import org.araneaframework.http.support.PopupWindowProperties;

/**
 * Encapsulates info about popup window properties and URL where 
 * servicing session-thread lives.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface PopupServiceInfo extends Serializable {
  /**
   * @return popup service's info translated into String containing URL style parameters. 
   */
  public String toURL();
  /**
   * @return popup service's window properties. 
   */  
  public PopupWindowProperties getPopupProperties();
}
