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
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * Interface for manipulating popup windows (each popup window 
 * corresponding to server-side &quot;thread&quot;).
 * 
 * @author Taimo Peelo
 */

public interface PopupContext extends Serializable {
  /** keys for accessing the popup maps from viewmodels */
  public static final String POPUPS_KEY = "popupWindows";

  /** closing key for popups, if window receives response containing that key, it should close and take serverside service with it. */
  public static final String POPUPS_CLOSE_KEY = "popupClose";
  
  /** 
   * Opens given URL in a new popup window.
   * @param url URL to be opened in the popup window
   * @param properties properties specifying behaviour and appearance of creatable popup window. 
   */
  public void open(String url, PopupWindowProperties properties) throws Exception;

  /**
   * Closes the server side thread service (serving client side popup).
   * @param id thread (popup) ID to close.
   * @return whether service with given thread id was closed. 
   */
  public boolean close(String id) throws Exception;
}
