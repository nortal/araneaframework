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
 * An exception thrown when a required environment entry was not found, i.e. environment returned <code>null</code> for
 * given key.
 * 
 * @see org.araneaframework.Environment
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class NoSuchEnvironmentEntryException extends AraneaRuntimeException {

  /**
   * Creates a new exception for an environment entry with given <tt>key</tt> not being found.
   * 
   * @param key The used environment entry key that has no value.
   */
  public NoSuchEnvironmentEntryException(Class<?> key) {
    super("Environment entry with key '" + key + "' is missing!");
  }
}
