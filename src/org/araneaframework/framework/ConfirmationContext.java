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

package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Closure;

/**
 * Simple confirmation context - expects immediate user confirmation for going
 * through with some event (expressed as a closure).
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface ConfirmationContext extends Serializable {

  /**
   * Defines the request param name to be used by this context.
   */
  String CONFIRMATION_RESULT_KEY = "araConfirmationContextConfirmationResult";

  /**
   * Creates a condition for the user to confirm (only one confirmation can be
   * used per page). The user will be shown the confirmation message (on page
   * load). If the user chooses "Yes" then the business logic inside the closure
   * will be invoked.
   * 
   * @param onConfirmClosure A closure that invokes the business logic when the
   *            user confirms it.
   * @param message The confirmation message displayed to the user.
   */
  void confirm(Closure onConfirmClosure, String message);

  /**
   * Returns the current confirmation message meant for the user.
   * 
   * @return the current confirmation message.
   */
  String getConfirmationMessage();

}
