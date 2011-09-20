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

package org.araneaframework.uilib.form;

import java.io.Serializable;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.framework.MessageContext.MessageData;

/**
 * A constraint operates on some form {@link org.araneaframework.Component} providing means for defining the conditions
 * under which {@link org.araneaframework.Component} is considered valid. During validation, {@link Constraint} may
 * produce appropriate error messages.
 * 
 * @see org.araneaframework.uilib.form.constraint.BaseConstraint
 */
public interface Constraint extends Serializable {

  /**
   * This method validates this {@link Constraint} conditions and returns whether they are valid (fulfilled).
   * 
   * @return A Boolean that is <code>true</code> when the constraint is valid.
   */
  boolean validate() throws Exception;

  /**
   * Provides the validation error messages produced while validating this {@link Constraint}. When no errors were
   * produced, returns an empty collection.
   * 
   * @return A set of produced validation error messages, or an empty set when no errors were produced.
   */
  Set<MessageData> getErrors();

  /**
   * Clears the the error messages produced while validating this {@link Constraint}.
   */
  void clearErrors();

  /**
   * Sets the custom error message, that will be used in place of default ones when this {@link Constraint} does not
   * hold. When the custom error message is set to <code>null</code>, the default error messages will be produced again
   * on errors.
   * 
   * @param customErrorMessage A custom error message, or <code>null</code> to disable custom message.
   */
  void setCustomErrorMessage(String customErrorMessage);

  /**
   * Sets the <code>Environment</code> of this <code>Constraint</code>. Environment should come from whatever
   * {@link org.araneaframework.Component} that this <code>Constraint</code> is operating on.
   * <p>
   * The <code>Environment</code> of a <code>Constraint</code> may be set to non-null value only once, further calls are
   * ignored. Application programmer typically never calls this method as <code>Environment</code> is propagated
   * seamlessly.
   * 
   * @param environment The <code>Environment</code> for this constraint to use.
   */
  void setEnvironment(Environment environment);

}
