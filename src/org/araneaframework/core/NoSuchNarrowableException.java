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

package org.araneaframework.core;

/**
 * Thrown to indicate that such a Narrowable does not exist in the extensions of
 * the object.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class NoSuchNarrowableException extends AraneaRuntimeException {

  private static final long serialVersionUID = 1L;

  public NoSuchNarrowableException(Class target) {
    super("Target " + target.getName() + " does not exist!");
  }
}
