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

package org.araneaframework;

import java.io.Serializable;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.RoutedMessage;

/**
 * {@link Message} interface allows to send any events to any component in the component hierarchy.
 * 
 * @see BroadcastMessage
 * @see RoutedMessage
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Message extends Serializable {
  /**
   * Used to send message into component hierarchy whose root node is <code>component</code>.
   * Propagation within the component hierarchy is done by {@link org.araneaframework.Component.Interface#propagate(Message)} method.
   * 
   * @param id id of component in hierarchy that should react to this {@link Message}.
   * @param component {@link Component} who should start propagating the {@link Message} further
   */
  public void send(Object id, Component component);
}
