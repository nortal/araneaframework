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
import java.util.List;
import java.util.Set;


public interface Constraint extends Serializable, GenericFormElementAware{

  /**
   * This method validates the constraint conditions, providing some preconditions and
   * postconditions for the {@link #validate()}method.
   * @throws Exception 
   */
  public boolean validate() throws Exception;
  
  /**
   * Returns the {@link UiMessage}s produced while validationg the constraint.
   * 
   * @return the {@link UiMessage}s produced while validationg the constraint.
   */
  public Set getErrors();

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
}
