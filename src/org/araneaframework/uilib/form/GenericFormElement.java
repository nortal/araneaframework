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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.ConfigurationUtil;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * Represents a general form element, a node in form element hierarchy.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class GenericFormElement extends BaseApplicationWidget {

  /**
   * The ID of the action that request form element background (AJAX) validation.
   * @since 1.1
   */
  public static final String SEAMLESS_VALIDATION_ACTION_ID = "bgValidate";

  protected Constraint constraint;

  protected Map<Object, Object> properties;

  protected boolean converted;

  protected boolean validated;

  protected Boolean backgroundValidation;

  private Set<String> errors;

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  @Override
  protected void init() throws Exception {
    super.init();
    if (this.constraint != null) {
      this.constraint.setEnvironment(getConstraintEnvironment());
    }
  }

  /**
   * Returns all properties of the element as a map (string -&gt; string).
   * 
   * @return all properties as a map.
   */
  public Map<Object, Object> getProperties() {
    if (this.properties == null) {
      this.properties = new HashMap<Object, Object>();
    }
    return this.properties;
  }

  /**
   * Method for setting property values.
   * 
   * @param key name of the property to be set.
   * @param value value for the property.
   */
  public void setProperty(Object key, Object value) {
    Assert.notNullParam(key, "key");
    getProperties().put(key, value);
  }

  /**
   * Returns the value of the property.
   * 
   * @param key the name of the property to get.
   * @return the value of the property, <code>null</code> if property is not defined.
   */
  public Object getProperty(Object key) {
    Assert.notNullParam(key, "key");
    return getProperties().get(key);
  }

  /**
   * Returns element constraint.
   * 
   * @return element constraint.
   */
  public Constraint getConstraint() {
    return this.constraint;
  }

  /**
   * Sets element constraint.
   * 
   * @param constraint The constraint to set.
   */
  public void setConstraint(Constraint constraint) {
    this.constraint = constraint;
    if (constraint != null && isInitialized()) {
      constraint.setEnvironment(getConstraintEnvironment());
    }
  }

  public Environment getConstraintEnvironment() {
    return getEnvironment();
  }

  /**
   * Returns whether the element is valid.
   * 
   * @return whether the element is valid.
   */
  public boolean isValid() {
    return CollectionUtils.isEmpty(this.errors);
  }

  /**
   * Converts the element value from control to data item and validates the value.
   * 
   * @return whether the element is valid.
   */
  public boolean convertAndValidate() {
    convert();
    return validate();
  }

  /**
   * Converts the value from {@link Control}s to {@link Data}.
   */
  public void convert() {
    try {
      this.converted = false;
      this.validated = false;
      if (!isAlive()) {
        return;
      }
      clearErrors();
      convertInternal();
      this.converted = isValid();
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Validates the element.
   * 
   * @return whether the element is valid.
   */
  public boolean validate() {
    boolean valid = false;
    try {
      this.validated = false;
      this.validated = validateInternal();
      return this.validated;
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
    return valid;
  }

  /**
   * Returns whether last evaluation (converting and optional validating) has succeeded.
   * 
   * @return whether last evaluation (converting and optional validating) has succeeded.
   */
  public boolean isEvaluated() {
    return this.converted && this.validated;
  }

  /**
   * Since 1.1 this returns an immutable Set.
   */
  public Set<String> getErrors() {
    return Collections.unmodifiableSet(getMutableErrors());
  }

  public void addError(String error) {
    Assert.notEmptyParam(error, "error");
    getMutableErrors().add(error);
  }

  public void addErrors(Set<String> errors) {
    Assert.notNullParam(errors, "errors");
    for (String error : errors) {
      addError(error);
    }
  }

  /**
   * Clears element errors.
   */
  public void clearErrors() {
    this.errors = null;
  }

  public Object getValue() {
    return null;
  }

  /** @since 1.1 */
  public void setBackgroundValidation(boolean backgroundValidation) {
    this.backgroundValidation = backgroundValidation;
  }

  /** @since 1.1 */
  public boolean isBackgroundValidation() {
    boolean bgValidate = false;

    if (this.backgroundValidation != null) {
      bgValidate = this.backgroundValidation.booleanValue();
    } else {
      FormContext fctx = UilibEnvironmentUtil.getFormContext(getEnvironment());
      if (fctx != null) {
        bgValidate = fctx.isBackgroundValidation();
      } else {
        bgValidate = ConfigurationUtil.isBackgroundFormValidationEnabled(getEnvironment());
      }
    }

    return bgValidate;
  }

  // *********************************************************************
  // * ABSTRACT METHODS
  // *********************************************************************

  /**
   * Marks the current value of the data item as the base state that will be used to determine whether its state has
   * changed in {@link #isStateChanged()}.
   */
  public abstract void markBaseState();

  /**
   * Restores the value of the data item from the marked base state.
   */
  public abstract void restoreBaseState();

  /**
   * Returns whether data item state has changed after it was marked.
   * 
   * @return whether data item state has changed after it was marked.
   */
  public abstract boolean isStateChanged();

  /**
   * Sets whether the element is disabled.
   * 
   * @param disabled whether the element is disabled.
   */
  public abstract void setDisabled(boolean disabled);

  /**
   * Returns whether the element is disabled.
   * 
   * @return whether the element is disabled.
   */
  public abstract boolean isDisabled();

  /**
   * Accepts the visitor.
   */
  public abstract void accept(String id, FormElementVisitor visitor);

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  /** @since 1.1 this method is protected (private before 1.1). */
  protected MessageContext getMessageCtx() {
    return getEnvironment().requireEntry(MessageContext.class);
  }

  /**
   * Converts the element value from control to expected data type.
   * 
   * @throws Exception
   */
  protected abstract void convertInternal() throws Exception;

  /**
   * Validates the element.
   * 
   * @return whether the element is valid.
   * @throws Exception
   */
  protected boolean validateInternal() throws Exception {
    if (getConstraint() != null && isValid()) {
      getConstraint().validate();
      addErrors(getConstraint().getErrors());
      getConstraint().clearErrors();
    }
    return isValid();
  }

  /**
   * Provides the current errors in a <code>Set</code> that can be modified
   * 
   * @since 1.1
   */
  protected Set<String> getMutableErrors() {
    if (this.errors == null) {
      this.errors = new HashSet<String>();
    }
    return this.errors;
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * This class represents a form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends BaseApplicationWidget.ViewModel {

    private Map<Object, Object> properties;

    /**
     * Takes a outer class snapshot.
     */
    public ViewModel() {
      Map<Object, Object> m = GenericFormElement.this.properties;
      this.properties = m == null ? null : Collections.unmodifiableMap(m);
    }

    /**
     * Returns form element properties.
     * 
     * @return form element properties.
     */
    public Map<Object, Object> getProperties() {
      return this.properties;
    }
  }
}
