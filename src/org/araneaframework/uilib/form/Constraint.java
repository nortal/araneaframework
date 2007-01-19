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

package org.araneaframework.uilib.form;

import java.io.Serializable;
import java.util.Set;
import org.araneaframework.Environment;


/**
 * A constraint operates on some form {@link org.araneaframework.Component} providing means 
 * for defining the conditions under which {@link org.araneaframework.Component} is considered
 * valid. During validation, {@link Constraint} may produce appropriate error messages.
 * 
 * @see org.araneaframework.uilib.form.constraint.BaseConstraint
 */
public interface Constraint extends Serializable {
  /**
   * This method validates this {@link Constraint} conditions.
   */
  public boolean validate() throws Exception;
  
  /**
   * Returns the validation errors produced while validating this {@link Constraint}.
   * @return validation errors.
   */
  public Set getErrors();

  /**
   * Clears the the errors produced while validating this {@link Constraint}.
   */
  public void clearErrors();

  /**
   * Sets the custom error message, that will be used in place of default ones when
   * this {@link Constraint} does not hold.
   * 
   * @param customErrorMessage custom error message
   */
  public void setCustomErrorMessage(String customErrorMessage);

  /**
   * Sets the {@link org.araneaframework.Environment} of this {@link Constraint}.
   * Environment should come from whatever {@link org.araneaframework.Component} that this
   * {@link Constraint} is operating on.
   * 
   * {@link Constraint} {@link org.araneaframework.Environment} may be set to non-null 
   * value only once, further calls are ignored. Application programmer typically never
   * calls this method as {@link org.araneaframework.Environment} is propagated seamlessly.
   * 
   * @param environment 
   */
  public void setEnvironment(Environment environment);
}
