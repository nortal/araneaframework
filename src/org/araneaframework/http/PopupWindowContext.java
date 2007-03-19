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
import java.util.Map;
import org.araneaframework.Message;
import org.araneaframework.Service;
import org.araneaframework.Widget;
import org.araneaframework.http.support.PopupWindowProperties;

/**
 * Interface for manipulating popup windows (each popup window 
 * corresponding to server-side &quot;thread&quot;).
 * 
 * @author Taimo Peelo
 */
public interface PopupWindowContext extends Serializable {

  /** Popup closing key, when session-thread in a window receives response containing 
   * that key, it should close and take serverside service with it. */
  public static final String POPUPS_CLOSE_KEY = "popupClose";
  
  /**
   * Registers new thread-level service server-side and gives started service means of communicating with
   * its opener and vice-versa.
   * @param startMessage - message sent to newly created service (thread).
   * @param properties - properties specifying behaviour and appearance of creatable popup window.
   * @param opener - widget that is registered as opener of created thread.
   * @return threadlevel id of created and registered service
   */
  public String open(Message startMessage, PopupWindowProperties properties, Widget opener);
  
  /** 
   * Method for registering already created service under {@link org.araneaframework.framework.ThreadContext} as popup.
   * @param service to register.
   * @param properties properties specifying behaviour and appearance of creatable popup window. 
   * @param opener - widget that is registered as opener of created thread.
   * @return ID of created service.
   */
  public String open(Service service, PopupWindowProperties properties, Widget opener);
  
  public String openMounted(String url, PopupWindowProperties properties);
  
  /** 
   * Opens given URL in a new popup window.
   * @param url URL to be opened in the popup window
   * @param properties properties specifying behaviour and appearance of creatable popup window. 
   */
  public void open(String url, PopupWindowProperties properties);

  /**
   * Closes the server side thread service (serving client side popup).
   * @param id thread (popup) ID to close.
   * @return whether service with given thread id was closed. 
   */
  public boolean close(String id) throws Exception;
  
  /**
   * Returns the widget that opened calling thread-level service (popup).
   * @return opener of thread-level service, <code>null</code> when calling service does not have registered opener.
   */
  public Widget getOpener();
  
  public Map getPopups();
}
