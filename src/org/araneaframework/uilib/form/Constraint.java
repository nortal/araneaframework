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

import java.util.List;
import org.araneaframework.Environment;

public interface Constraint {

  /**
   * Sets the <code>SimpleFormElement</code> that the constraint will operate on (the constraints
   * that operate on more than one field should ignore this function, composite constraints should
   * propagate it down the tree).
   * 
   * @param field <code>SimpleFormElement</code> the field that constraint will operate on.
   */
  public void setField(FormElement field);

  /**
   * This method validates the constraint conditions, providing some preconditions and
   * postconditions for the {@link #validate()}method.
   * @throws Exception 
   */
  public void validate() throws Exception;

  /**
   * Returns whether the constraint is satisfied/valid (same that no errors were produced).
   * 
   * @return whether the constraint is satisfied/valid (same that no errors were produced).
   */
  public boolean isValid();

  /**
   * Returns the {@link UiMessage}s produced while validationg the constraint.
   * 
   * @return the {@link UiMessage}s produced while validationg the constraint.
   */
  public List getErrors();

  /**
   * Clears the the errors produced while validationg the constraint.
   */
  public void clearErrors();

  /**
   * Sets the custom {@link UiMessage}, that will be returned instead of the usual ones.
   * 
   * @param customErrorMessage custom {@link UiMessage} that will be returned instead of the
   * usual ones.
   */
  public void setCustomErrorMessage(String customErrorMessage);

  public void setEnvironment(Environment enviroment);

}
