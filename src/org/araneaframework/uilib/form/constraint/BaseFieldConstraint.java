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

import org.araneaframework.Environment;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoSuchEnvironmentEntryException;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * Base implementation of the <code>Constraint</code> that is associated with
 * some {@link org.araneaframework.uilib.form.FormElement}. Most constraints
 * may find it useful to extend this form-field-related implementation to
 * define a new constraint.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseFieldConstraint<C, D> extends BaseConstraint {

  // The form field that this constraint applies to.
  private FormElement<C, D> field;

  /**
   * An empty constructor that does not bind this constraint with a form element. The only way to bind a constraint with
   * a form field, is by a constructor.)
   */
  public BaseFieldConstraint() {}

  // Constraints environment should always be set to field environment.
  // however there is no guarantee that field has been initialized when
  // constructor is called, so the environment may be missing crucial entries.
  // Just setting field constraint works (then constraints environment is set when
  // field is initialized). That would break constraint previously set however.

  /**
   * A constructor that binds this constraint with given form element (<code>field</code>)
   * 
   * @param field The form element to bind this constraint with.
   */
  public BaseFieldConstraint(FormElement<C, D> field) {
    Assert.notNullParam(this, field, "field");
    this.field = field;
  }

  /**
   * Returns the {@link FormElement} that this <code>Constraint</code> is constraining.
   * 
   * @return constrained {@link FormElement}
   */
  @SuppressWarnings("unchecked")
  protected FormElementContext<C, D> getField() {
    if (this.field != null) {
      return this.field;
    }

    FormElementContext<C, D> result;
    try {
      result = getEnvironment().requireEntry(FormElementContext.class);
    } catch (NoSuchEnvironmentEntryException e) {
      throw new FieldConstraintException(Assert.thisToString(this) + " could not determine FormElementContext, this "
          + "is probably caused by applying field constraint to something other than FormElement.", e);
    }
    return result;
  }

  @Override
  public Environment getEnvironment() {
    return this.field == null ? super.getEnvironment() : field.getConstraintEnvironment();
  }

  /**
   * Provides the label of the constraint field.
   * 
   * @return the label of the constraint field.
   */
  protected String getLabel() {
    return getField().getLabel();
  }

  /**
   * Provides the value of the constraint field.
   * 
   * @return the value of the constraint field.
   */
  protected D getValue() {
    return getField().getValue();
  }

  /**
   * Specifies whether the constraint field was read.
   * 
   * @return a Boolean indicating whether the constraint field was read.
   */
  public boolean isRead() {
    return getField().isRead();
  }

  /**
   * Specifies whether the constraint field is disabled.
   * 
   * @return a Boolean indicating whether the constraint field is disabled.
   */
  public boolean isDisabled() {
    return getField().isDisabled();
  }

  /**
   * Specifies whether the constraint field is mandatory.
   * 
   * @return a Boolean indicating whether the constraint field is mandatory.
   */
  public boolean isMandatory() {
    return getField().isMandatory();
  }

  /**
   * Exception thrown when {@link org.araneaframework.uilib.form.FormElement} associated with
   * {@link BaseFieldConstraint} could not be determined.
   */
  public static class FieldConstraintException extends AraneaRuntimeException {

    /**
     * Creates the exception without any message or other <code>Throwable</code>.
     */
    public FieldConstraintException() {
      super();
    }

    /**
     * Creates the exception with a descriptive message and the <code>Throwable</code> that was caught.
     * 
     * @param message A descriptive message to help solve this issue.
     * @param cause A <code>Throwable</code> that was caught.
     */
    public FieldConstraintException(String message, Throwable cause) {
      super(message, cause);
    }

    /**
     * Creates the exception with a descriptive message.
     * 
     * @param message A descriptive message to help solve this issue.
     */
    public FieldConstraintException(String message) {
      super(message);
    }

    /**
     * Creates the exception with the <code>Throwable</code> that was caught.
     * 
     * @param cause A <code>Throwable</code> that was caught.
     */
    public FieldConstraintException(Throwable cause) {
      super(cause);
    }

  }

}
