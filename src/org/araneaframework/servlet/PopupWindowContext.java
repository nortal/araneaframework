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
	/**
	 * Method for registering a new service server-side, meant to open in popup window on client side.
	 * @param id - prefix for service id that will be associated with created window.
	 * @param properties - properties specifying behaviour and appearance of creatable popup window. 
	 * @param startMessage - message sent to newly created service.
	 * @return id of created service (thread).
	 */
	public String open(String id, PopupWindowProperties properties, Message startMessage) throws Exception;
	
	/** 
	 * Method for registering already created service with given thread ID as popup.
	 * @param id service (thread) ID
	 * @param properties properties specifying behaviour and appearance of creatable popup window. 
	 * @return given thread ID.
	 */
	public String open(String id, PopupWindowProperties properties) throws Exception;

	/**
	 * Closes the server side service (serving client side popup).
	 * @param id thread (popup) ID to close. 
	 */
	public void close(String id) throws Exception;
}
