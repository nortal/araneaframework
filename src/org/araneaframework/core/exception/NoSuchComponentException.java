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

package org.araneaframework.core.exception;


/**
 * Thrown to indicate that a component does not exist in the set of
 * (child)components. The exception is thrown when:
 * <ul>
 * <li>Routing an event to a specific child component that does not exist.</li>
 * <li>Calling <code>disableComponent(Object key)</code> when the object does
 * not exist.</li>
 * <li>Calling <code>enableComponent(Object key)</code> when the object does
 * not exist in the set of disabled components.</li>
 * <li>Calling <code>removeComponent(Object key)</code> when the object does
 * not exist in the set of child components. </li>
 * </ul>
 * <p>
 * Services and Widgets have corresponding exceptions,
 * {@link org.araneaframework.core.exception.NoSuchServiceException} and
 * {@link org.araneaframework.core.exception.NoSuchWidgetException} respectively.
 * </p>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class NoSuchComponentException extends AraneaRuntimeException {

  public NoSuchComponentException(Object key) {
    super("No such component '" + key + "'");
  }
}
