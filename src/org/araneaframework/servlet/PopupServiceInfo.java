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

package org.araneaframework.servlet;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public interface PopupServiceInfo extends Serializable {
  /**
   * @return Map of &lt;key, value&lt; where <i>keys</i> and <i>values</i>
   *         give some information about popup service.
   */
  public Map getServiceInfo();
	
  /**
   * @return popup service's info translated into String containing URL style parameters. 
   */
  public String toURLParams();

  /**
   * @return popup service's window properties. 
   */  
  public PopupWindowProperties getPopupProperties();
}
