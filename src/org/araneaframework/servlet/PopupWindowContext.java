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
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * Interface for manipulating popup windows (each popup window 
 * corresponding to server-side threads).
 * 
 * @author Taimo Peelo
 */

public interface PopupWindowContext extends Serializable {
	public static final int THREAD_POPUP=1;
	public static final int APPLICATION_POPUP=2;
	
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
	 * @param serviceType - either <code>PopupWindowContext.THREAD_POPUP</code> or <code>PopupWindowContext.APPLICATION_POPUP</code>.
	 * @return full ID of created service (thread).
	 */
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage, int serviceType) throws Exception;
	
	/** 
	 * Method for registering already created service with given thread ID as popup.
	 * @param id service (thread) ID
	 * @param properties properties specifying behaviour and appearance of creatable popup window. 
	 * @return given thread ID.
	 */
	public String open(String id, PopupWindowProperties properties) throws Exception;
	
	/** 
	 * Method for registering already created service with given thread- or application-level service ID as popup.
	 * @param id service (thread) ID
	 * @param properties properties specifying behaviour and appearance of creatable popup window.
	 * @param serviceType either <code>PopupWindowContext.THREAD_POPUP</code> or <code>PopupWindowContext.APPLICATION_POPUP</code>.
	 * @return given thread ID.
	 */
	public String open(String id, PopupWindowProperties properties, int serviceType) throws Exception;

	/**
	 * Closes the server side service (serving client side popup).
	 * @param id thread (popup) ID to close. 
	 */
	public void close(String id) throws Exception;
}
