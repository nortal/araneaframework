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

import org.araneaframework.core.AraneaRuntimeException;

/**
 * Thrown to indicate that there are no elements in the Path to pop.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class EmptyCallStackException extends AraneaRuntimeException {
  public EmptyCallStackException() {
    super("Trying to cancel/end call with an empty call stack!");
  }
}
