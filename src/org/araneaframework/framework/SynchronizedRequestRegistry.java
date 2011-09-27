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

package org.araneaframework.framework;

import java.io.Serializable;

/**
 * A contract interface for components that take care of propagating actions synchronously and asynchronously (in the
 * context of underlying service, not in the context of user sessions, which are always synchronized). Since most
 * actions are propagated asynchronously (because it does not block other users requests to the underlying service), the
 * contract is about registering actions for synchronized actions (that affect all users).
 * <p>
 * Note that the underlying implementation does not have to guess when to stop propagating registered actions in
 * synchronized manner. It stops doing that once {@link #unregisterSynchronizedAction(String, String)} is called for
 * that action.
 * <p>
 * By the way, Aranea users should not use this interface directly to enable synchronized actions. An action is
 * registered automatically as synchronized when it implements <tt>SynchronizedActionListener</tt>.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @see org.araneaframework.core.action.SynchronizedActionListener
 * @since 2.0
 */
public interface SynchronizedRequestRegistry extends Serializable {

  /**
   * Registers an action with given parent component scope and with given action ID. All action request to the given
   * scope and action listener will be handled in synchronized manner (all other incoming requests will be processed
   * once the the action listener completes).
   * 
   * @param parentScope The scope of the component that has the action listener.
   * @param actionId The simple action ID.
   * @since 2.0
   */
  void registerSynchronizedAction(String parentScope, String actionId);

  /**
   * Unregisters an action with given parent component scope and with given action ID. All action request to the given
   * scope and action listener will be handled asynchronously after calling this method.
   * <p>
   * Note that, by default, all action listeners are processed asynchronously (in the context of the underlying service,
   * not in the context of user sessions, which are always processed synchronously), so calling this method for an
   * action listener that was not already registered with {@link #registerSynchronizedAction(String, String)}, does not
   * change anything.
   * 
   * @param parentScope The scope of the component that has the action listener.
   * @param actionId The simple action ID.
   * @since 2.0
   */
  void unregisterSynchronizedAction(String parentScope, String actionId);

  /**
   * Provides whether the action with given <code>actionId</code> and <code>targetPath</code> is processed in
   * synchronized manner in the context of underlying service.
   * 
   * @param actionId The ID of the action.
   * @param targetPath The scope of the component that contains the action listener.
   * @return A boolean indicating whether the matching action listener is synchronized.
   * @since 2.0
   */
  boolean isSynchronized(String actionId, String targetPath);
}
