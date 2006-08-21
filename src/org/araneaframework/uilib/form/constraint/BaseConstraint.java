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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.GenericFormElementContext;

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

  private GenericFormElementContext gfeCtx;

  protected String customErrorMessage;
  
  private Set errors;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * This method validates the constraint conditions, providing some preconditions and
   * postconditions for the {@link #validate()}method.
   * @throws Exception 
   */
  public boolean validate() throws Exception {
    Assert.notNull(this, getGenericFormElementCtx(), "Generic form element context must be assigned to the constraint before it can function! " +
        "Make sure that the constraint is associated with a form element or a form!");
    
    clearErrors();
    
    validateConstraint();

    //Putting custom message only
    if (errors != null && customErrorMessage != null) {
    	clearErrors();
    	addError(customErrorMessage);
    }
    
    return isValid();
  }

  /**
   * Returns whether the constraint is satisfied/valid (same that no errors were produced).
   * 
   * @return whether the constraint is satisfied/valid (same that no errors were produced).
   */
  public boolean isValid() {
    return errors == null || errors.size() == 0;
  }

  /**
   * Returns the {@link UiMessage}s produced while validationg the constraint.
   * 
   * @return the {@link UiMessage}s produced while validationg the constraint.
   */
  public Set getErrors() {
    if (errors == null)
      errors = new HashSet();
    return errors;
  }

  /**
   * Clears the the errors produced while validationg the constraint.
   */
  public void clearErrors() {
    errors = null;
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
  
  public void setGenericFormElementCtx(GenericFormElementContext feCtx) {
    this.gfeCtx = feCtx;
  }
  
  public GenericFormElementContext getGenericFormElementCtx() {
    return this.gfeCtx;
  }
  
  protected String t(String key) {
    LocalizationContext locCtx = 
      (LocalizationContext) getEnvironment().getEntry(LocalizationContext.class);
    return locCtx.localize(key);
  } 
  //*********************************************************************
  //* PROTECTED METHODS
  //*********************************************************************
  
  protected void addError(String error) {
    getErrors().add(error);
  }
  
  protected void addErrors(Collection errorList) {
    getErrors().addAll(errorList);
  }  
  
  protected ConfigurationContext getConfiguration() {
  	return (ConfigurationContext) getEnvironment().getEntry(ConfigurationContext.class);
  }
  
  protected Environment getEnvironment() {
  	return gfeCtx.getEnvironment();
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
