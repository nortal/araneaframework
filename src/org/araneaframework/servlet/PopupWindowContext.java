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
import org.araneaframework.Message;
import org.araneaframework.Service;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * Interface for manipulating popup windows (each popup window 
 * corresponding to thread- or application-level service server-side).
 * 
 * @author Taimo Peelo
 */

public interface PopupWindowContext extends Serializable {
	/** keys for accessing the popup maps from viewmodels */
	public static final String POPUPS_KEY = "popupWindows";

	/** closing key for popups, if window receives response containing that key, it should close and take serverside service with it. */
	public static final String POPUPS_CLOSE_KEY = "popupClose";

	/**
	 * Method for registering a new thread-level service server-side, meant to open in popup window on client side.
	 * @param idPrefix - prefix for service id that will be associated with created window.
	 * @param properties - properties specifying behaviour and appearance of creatable popup window. 
	 * @param startMessage - message sent to newly created service (thread).
	 * @return full ID of created service (thread).
	 */
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage) throws Exception;
	
	/**
	 * Method for registering a new service server-side, meant to open in popup window on client side.
	 * @param idPrefix - prefix for service id that will be associated with created window.
	 * @param properties - properties specifying behaviour and appearance of creatable popup window. 
	 * @param startMessage - message sent to newly created thread- or application-level service.
	 * @param serviceContext - Some context class deriving from <code>ManagedServiceContext</code>.
	 * @return full ID of created service (thread).
	 */
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage, Class serviceContext) throws Exception;
	
	/** 
	 * Method for registering already created service under {@link org.araneaframework.framework.ThreadContext} as popup.
	 * @param idPrefix prefix for service id that will be associated with created window
	 * @param properties properties specifying behaviour and appearance of creatable popup window. 
	 * @return full ID of created service.
	 */
	public String open(String idPrefix, Service service, PopupWindowProperties properties) throws Exception;
	
	/** 
	 * Method for registering already created service under given serviceContext as popup.
	 * @param idPrefix prefix for service id that will be associated with created window
	 * @param properties properties specifying behaviour and appearance of creatable popup window.
	 * @param serviceContext some context class deriving from <code>ManagedServiceContext</code>.
	 * @return full ID of created service.
	 */
	public String open(String idPrefix, Service service, PopupWindowProperties properties, Class serviceContext) throws Exception;

	/**
	 * Closes the server side thread service (serving client side popup).
	 * @param id thread (popup) ID to close.
	 * @param serviceType either <code>PopupWindowContext.THREAD_POPUP</code> or <code>PopupWindowContext.APPLICATION_POPUP</code>.
	 * @return whether service with given thread id was closed. 
	 */
	public boolean close(String id, Class serviceContext) throws Exception;
}
