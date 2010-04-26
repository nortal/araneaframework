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

import org.araneaframework.core.AsynchronousActionListener;

import java.io.Serializable;

/**
 * A contract interface for components that take care of propagating actions synchronously and asynchronously. Since
 * most actions are propagated synchronously (because it's safe), the contract is about registering actions for
 * asynchronous actions.
 * <p>
 * Note that the underlying implementation does not have to guess when to stop propagating actions asynchronously. It
 * stops doing that once {@link #unregisterAsynchronousAction(String, String)} is called for that action.
 * <p>
 * By the way, Aranea users should use this interface directly to enable asynchronous actions. The action is registered
 * automatically as asynchronous when it implements {@link AsynchronousActionListener}.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public interface AsynchronousRequestRegistry extends Serializable {

  /**
   * Registers an action with given parent component scope and with given action ID. All action request to the given
   * scope and action listener will be handled asynchronously.
   * 
   * @param parentScope The scope of the component that has the action listener.
   * @param actionId The simple action ID.
   * @since 2.0
   */
  public void registerAsynchronousAction(String parentScope, String actionId);

  /**
   * Unregisters an action with given parent component scope and with given action ID. All action request to the given
   * scope and action listener will be handled synchronously.
   * 
   * @param parentScope The scope of the component that has the action listener.
   * @param actionId The simple action ID.
   * @since 2.0
   */
  public void unregisterAsynchronousAction(String parentScope, String actionId);

  /**
   * Provides whether the action with given <code>actionId</code> and <code>targetPath</code> is asynchronous.
   * 
   * @param actionId The ID of the action.
   * @param targetPath The scope of the component that contains the action listener.
   * @return A boolean indicating whether the matching action listener is asynchronous.
   * @since 2.0
   */
  public boolean isAsynchronous(String actionId, String targetPath);
}
