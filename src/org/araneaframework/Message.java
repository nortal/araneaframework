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

package org.araneaframework;

import java.io.Serializable;

/**
 * Contract interface for messages that can be sent down a component hierarchy to interact with its descendant
 * components.
 * <p>
 * It can be used in three ways, whichever is preferred or more suitable:
 * <ul>
 * <li><code>message.send(subComponentId, currentComponent)</code>
 * <li><code>currentComponent.propagate(message)</code> (this uses the protected propagate() method)
 * <li><code>currentComponent._getInterface().propagate(message)</code>  (this uses the public propagate() method)
 * </ul>
 * 
 * @see Component.Interface#propagate(Message)
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Message extends Serializable {

  /**
   * Sends the message object down a component hierarchy tree where root node is <code>component</code>.
   * 
   * @param id The ID of a component in the hierarchy that should react to this message (optional).
   * @param component The component that should start propagating this message to its descendants (required).
   */
  void send(Object id, Component component);
}
