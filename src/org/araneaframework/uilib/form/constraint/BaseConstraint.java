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
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Constraint;

/**
 * Base class for constraints. A {@link org.araneaframework.uilib.form.Constraint} 
 * operates on the form elements or forms providing means to constrain their content.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class BaseConstraint implements java.io.Serializable, Constraint {
  private Environment environment;
  protected String customErrorMessage;
  private Set errors;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************
  
  /* (non-Javadoc)
 * @see org.araneaframework.uilib.form.Constraint#validate()
 */
public boolean validate() throws Exception {
	 // XXX: ASSERT???
    ///Assert.notNull(this, constrainable, "Constraint may only be validated when constrainable is non-null. Make sure that the constraint is associated with a form element or a form!");
    
    clearErrors();
    
    validateConstraint();

    //Putting custom message only
    if (errors != null && !errors.isEmpty() && customErrorMessage != null) {
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
  
  public void setEnvironment(Environment environment) {
    // allow setting of constraint environment only once
    if (this.environment == null)
      this.environment = environment;
  }
  
  public Environment getEnvironment() {
    return environment;
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

  protected String t(String key) {
    LocalizationContext locCtx = 
     (LocalizationContext) getEnvironment().getEntry(LocalizationContext.class);
    return locCtx.localize(key);
  }

  //*********************************************************************
  //* ABSTRACT IMPLEMENTATION METHODS
  //*********************************************************************

  /**
   * This method should validate the constraint conditions adding error messages
   * and add messages about unsatisfied conditions.
   */
  protected abstract void validateConstraint() throws Exception;
}
