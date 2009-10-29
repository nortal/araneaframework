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

package org.araneaframework.http;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.Message;
import org.araneaframework.Service;
import org.araneaframework.Widget;
import org.araneaframework.http.support.PopupWindowProperties;

/**
 * An interface for manipulating popup windows (each popup window corresponding to server-side &quot;thread&quot;). It
 * is mainly used for opening new windows client-side. To make it work, one must also register popups client side using
 * the &lt;ui:registerPopups/&gt; tag.
 * 
 * @author Taimo Peelo
 * @see org.araneaframework.jsp.tag.basic.PopupRegistrationHtmlTag
 */
public interface PopupWindowContext extends Serializable {

  /**
   * Popup closing key, when session-thread in a window receives response containing that key, it should close and take
   * a server-side service with it.
   */
  public static final String POPUPS_CLOSE_KEY = "popupClose";

  /**
   * Registers a new thread-level service server-side and gives started service means of communicating with its opener
   * and vice-versa.
   * 
   * @param startMessage The message sent to the newly created service (thread).
   * @param properties The properties specifying behaviour and appearance of the creatable popup window.
   * @param opener The widget that is registered as an opener of the created thread.
   * @return The thread level ID of the created and registered service.
   */
  public String open(Message startMessage, PopupWindowProperties properties, Widget opener);

  /**
   * A method for registering already created service under {@link org.araneaframework.framework.ThreadContext} as a
   * popup.
   * 
   * @param service The service to register.
   * @param properties The properties specifying behaviour and appearance of creatable popup window.
   * @param opener The widget that is registered as an opener of the created thread.
   * @return The ID of the created service.
   */
  public String open(Service service, PopupWindowProperties properties, Widget opener);

  /**
   * Opens the URL for a mounted service. The <code>properties</code> may be specified to specify behavior and
   * appearance of the creatable popup window.
   * 
   * @param url The URL of a mounted service to be opened in the popup window.
   * @param properties The properties specifying behaviour and appearance of creatable popup window.
   * @return The ID of the created service.
   */
  public String openMounted(String url, PopupWindowProperties properties);

  /**
   * Opens given URL in a new popup window.
   * 
   * @param url The URL to be opened in the popup window.
   * @param properties properties specifying behaviour and appearance of creatable popup window.
   */
  public void open(String url, PopupWindowProperties properties);

  /**
   * Closes the server-side thread service (serving client-side popup).
   * 
   * @param id The thread (popup) ID to close.
   * @return A Boolean indicating whether the service with given thread ID was closed.
   */
  public boolean close(String id) throws Exception;

  /**
   * Returns the widget that opened the calling thread-level service (popup).
   * 
   * @return The opener of the thread-level service, <code>null</code> when calling service does not have registered
   *         opener.
   */
  public Widget getOpener();

  /**
   * Provides all opened and unclosed popups.
   * 
   * @return A <code>Map</code> of popups where the keys are IDs of the threads (<code>String</code>s) and the value are
   *         instances of {@link PopupServiceInfo}.
   * @since 1.1
   */
  public Map<String, PopupServiceInfo> getPopups();
}
