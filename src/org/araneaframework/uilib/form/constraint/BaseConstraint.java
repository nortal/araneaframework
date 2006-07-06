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

package org.araneaframework.uilib.form.constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.FormElement;

/**
 * This class is the base class for form constraints. A constraint operates on the form elements
 * providing means to constrain form element validity. That is using a constraint you can put
 * additional (and/or custom) conditions for the form elements to be valid.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 *  
 */
public abstract class BaseConstraint implements java.io.Serializable, Constraint {

  //*******************************************************************
  // FIELDS
  //*******************************************************************

  private List errors = new ArrayList();
  
  private FormElement field;
  
  protected Environment enviroment;

  protected String customErrorMessage;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Sets the <code>SimpleFormElement</code> that the constraint will operate on (the constraints
   * that operate on more than one field should ignore this function, composite constraints should
   * propagate it down the tree).
   * 
   * @param field <code>SimpleFormElement</code> the field that constraint will operate on.
   */
  public void setField(FormElement field) {
    this.field = field;
  }

  /**
   * This method validates the constraint conditions, providing some preconditions and
   * postconditions for the {@link #validate()}method.
   * @throws Exception 
   */
  public void validate() throws Exception {
    clearErrors();

    validateConstraint();

    //Putting custom message only
    if (getErrors().size() > 0 && customErrorMessage != null) {
    	clearErrors();
    	addError(customErrorMessage);
    }
  }

  /**
   * Returns whether the constraint is satisfied/valid (same that no errors were produced).
   * 
   * @return whether the constraint is satisfied/valid (same that no errors were produced).
   */
  public boolean isValid() {
    return getErrors().size() == 0;
  }

  /**
   * Returns the {@link UiMessage}s produced while validationg the constraint.
   * 
   * @return the {@link UiMessage}s produced while validationg the constraint.
   */
  public List getErrors() {
    return errors;
  }

  /**
   * Clears the the errors produced while validationg the constraint.
   */
  public void clearErrors() {
    errors.clear();
  }
 
  /**
   * Sets the custom {@link UiMessage}, that will be returned instead of the usual ones.
   * 
   * @param customErrorMessage custom {@link UiMessage} that will be returned instead of the
   * usual ones.
   */
  public void setCustomErrorMessage(String customErrorMessage) {
    this.customErrorMessage = customErrorMessage;
  }
  
  public void setEnvironment(Environment enviroment) {
  	this.enviroment = enviroment;
  }
  
  //*********************************************************************
  //* PROTECTED METHODS
  //*********************************************************************
  
  protected void addError(String error) {
  	errors.add(error);
  }
  
  protected void addErrors(Collection errorList) {
  	errors.addAll(errorList);
  }  
  
  protected FormElement getField() {
  	return field;
  }
  
  protected ConfigurationContext getConfiguration() {
  	return (ConfigurationContext) getEnvironment().getEntry(ConfigurationContext.class);
  }
  
  protected Environment getEnvironment() {
  	return this.enviroment;
  }
  
  //*********************************************************************
  //* ABSTRACT IMPLEMENTATION METHODS
  //*********************************************************************

  /**
   * This method should validate the constraint conditions adding {@link UiMessage}s if some
   * condition is not satisfied.
   * @throws Exception 
   */
  protected abstract void validateConstraint() throws Exception;
}
