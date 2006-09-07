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
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Constrainable;
import org.araneaframework.uilib.form.Constraint;

/**
 * This class is the base class for form constraints. A constraint operates on the form elements
 * providing means to define form element validity. That is using a constraint you can put
 * additional (and/or custom) conditions for the form elements to be valid.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class BaseConstraint implements java.io.Serializable, Constraint {

  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private Constrainable constrainable;

  protected String customErrorMessage;
  
  private Set errors;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************
  public boolean validate() throws Exception {
    Assert.notNull(this, constrainable, "Constraint may only be validated when constrainable is non-null. Make sure that the constraint is associated with a form element or a form!");
    
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
   * Returns whether the constraint is satisfied/valid. Constraint is valid
   * when no validation errors were produced.
   */
  public boolean isValid() {
    //XXX: should it throw NotValidatedYetException if called before validation
    return errors == null || errors.size() == 0;
  }

  public Set getErrors() {
    if (errors == null)
      errors = new HashSet();
    return errors;
  }

  public void clearErrors() {
    errors = null;
  }

  public void setCustomErrorMessage(String customErrorMessage) {
    this.customErrorMessage = customErrorMessage;
  }
  
  public void constrain(Constrainable constrainable) {
    this.constrainable = constrainable;
  }
  
  protected Constrainable getConstrainable() {
    return this.constrainable;
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
  
  //*********************************************************************
  //* ABSTRACT IMPLEMENTATION METHODS
  //*********************************************************************

  /**
   * This method should validate the constraint conditions adding error messages
   * and add messages about unsatisfied conditions.
   */
  protected abstract void validateConstraint() throws Exception;
  
  /**
   * Should be used for {@link Constrainable} type checks instead 
   * {@link Assert#isInstanceOf(Class, Object, String)} to provide better error message.
   * 
   * @param klass Class which instance <code>constrainable</code> should be
   * @param constrainable
   */
  protected void LazyTypeAssert(Class klass, Constrainable constrainable) {
    try {
      Assert.isInstanceOf(klass, constrainable, null);
    } catch (IllegalArgumentException e) {
      String simpleClassName = this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
      throw new IllegalArgumentException(simpleClassName + " can only constrain FormElements!");
    }
  }
}
