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
 * A constraint operates on the form elements providing means to define form element validity.
 * 
 * @see org.araneaframework.uilib.form.constraint.BaseConstraint
 */
public interface Constraint extends Serializable {
  /**
   * This method validates the constraint conditions, providing some preconditions and
   * postconditions for the {@link #validate()} method.
   */
  public boolean validate() throws Exception;
  
  /**
   * Returns the validation errors.
   * @return validation errors.
   */
  public Set getErrors();

  /**
   * Clears the the errors produced while validating the constraint.
   */
  public void clearErrors();

  /**
   * Sets the custom error message, that will be used in place of default ones.
   * @param customErrorMessage custom error message
   */
  public void setCustomErrorMessage(String customErrorMessage);

  public void setEnvironment(Environment env);
}
