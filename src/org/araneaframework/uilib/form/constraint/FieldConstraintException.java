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

package org.araneaframework.uilib.form.constraint;

import org.araneaframework.core.exception.AraneaRuntimeException;

/**
 * Exception thrown when a <code>(Generic)FormElement</code> associated with a <code>Constraint</code> could not be
 * determined.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FieldConstraintException extends AraneaRuntimeException {

  /**
   * Creates the exception without any message or other <code>Throwable</code>.
   */
  public FieldConstraintException() {
    super();
  }

  /**
   * Creates the exception with a descriptive message and the <code>Throwable</code> that was caught.
   * 
   * @param message A descriptive message to help solve this issue.
   * @param cause A <code>Throwable</code> that was caught.
   */
  public FieldConstraintException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates the exception with a descriptive message.
   * 
   * @param message A descriptive message to help solve this issue.
   */
  public FieldConstraintException(String message) {
    super(message);
  }

  /**
   * Creates the exception with the <code>Throwable</code> that was caught.
   * 
   * @param cause A <code>Throwable</code> that was caught.
   */
  public FieldConstraintException(Throwable cause) {
    super(cause);
  }
}
