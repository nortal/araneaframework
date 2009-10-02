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

package org.araneaframework.uilib.form.constraint;

import java.io.Serializable;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * Base class for constraints. A {@link org.araneaframework.uilib.form.Constraint} operates on the form elements or
 * forms providing means to constrain their content.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class BaseConstraint implements Serializable, Constraint {

  private Environment environment;

  private Set<String> errors;

  /**
   * Holds the custom error message for this constraint.
   */
  protected String customErrorMessage;

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  public boolean validate() throws Exception {
    clearErrors();
    validateConstraint();

    // Putting custom message only
    if (!CollectionUtils.isEmpty(this.errors) && this.customErrorMessage != null) {
      clearErrors();
      addError(this.customErrorMessage);
    }

    return isValid();
  }

  /**
   * Returns whether the constraint is satisfied/valid. Constraint is valid when no validation errors were produced.
   */
  public boolean isValid() {
    // XXX: should it throw NotValidatedYetException if called before validation
    return CollectionUtils.isEmpty(this.errors);
  }

  public Set<String> getErrors() {
    if (this.errors == null)
      this.errors = new HashSet<String>();
    return this.errors;
  }

  public void clearErrors() {
    this.errors = null;
  }

  public void setCustomErrorMessage(String customErrorMessage) {
    this.customErrorMessage = customErrorMessage;
  }

  public void setEnvironment(Environment environment) {
    // allow setting of constraint environment only once
    if (this.environment == null) {
      this.environment = environment;
    }
  }

  /**
   * Provides the <code>Environment</code> of the constraint.
   * 
   * @return the <code>Environment</code> of the constraint.
   */
  public Environment getEnvironment() {
    return this.environment;
  }

  // *********************************************************************
  // * PROTECTED METHODS
  // *********************************************************************

  /**
   * Adds the given error message to the errors list to be displayed later.
   * 
   * @param error an error message
   */
  protected void addError(String error) {
    getErrors().add(error);
  }

  /**
   * Adds the given error messages to the errors list to be displayed later.
   * 
   * @param errorList A list of error messages (<code>String</code>s).
   */
  protected void addErrors(Collection<String> errorList) {
    getErrors().addAll(errorList);
  }

  /**
   * Finds the <code>ConfigurationContext</code> from the <code>Environment</code>.
   * 
   * @return the <code>ConfigurationContext</code> from the <code>Environment</code>.
   */
  protected ConfigurationContext getConfiguration() {
    return UilibEnvironmentUtil.getConfiguration(getEnvironment());
  }

  /**
   * Translates the given message key according to the <code>LocalizationContext</code>.
   * 
   * @param key The key to find the correct message.
   * @return The localized message from the <code>LocalizationContext</code>.
   */
  protected String t(String key) {
    return EnvironmentUtil.getLocalizationContext(getEnvironment()).localize(key);
  }

  // *********************************************************************
  // * ABSTRACT IMPLEMENTATION METHODS
  // *********************************************************************

  /**
   * This method should validate the constraint conditions adding error messages and add messages about unsatisfied
   * conditions.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected abstract void validateConstraint() throws Exception;
}
