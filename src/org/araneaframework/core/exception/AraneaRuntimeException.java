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

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * A general Aranea specific runtime exception that can be nested. This is also the base runtime exception class used by
 * Aranea framework.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class AraneaRuntimeException extends NestableRuntimeException {

  /**
   * Creates a new Aranea-specific runtime exception.
   */
  public AraneaRuntimeException() {
    super();
  }

  /**
   * Creates a new Aranea-specific runtime exception.
   * 
   * @param message A message for the exception.
   */
  public AraneaRuntimeException(String message) {
    super(message);
  }

  /**
   * Creates a new Aranea-specific runtime exception.
   * 
   * @param cause A source exception to be wrapped by this exception.
   */
  public AraneaRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new Aranea-specific runtime exception.
   * 
   * @param message A message for the exception.
   * @param cause A source exception to be wrapped by this exception.
   */
  public AraneaRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
