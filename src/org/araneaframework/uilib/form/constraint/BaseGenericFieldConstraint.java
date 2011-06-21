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
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoSuchEnvironmentEntryException;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.form.GenericFormElement;

/**
 * Base implementation of the <code>Constraint</code> that is associated with some
 * {@link org.araneaframework.uilib.form.GenericFormElement}. Most constraints may find it useful to extend this
 * form-field-related implementation to define a new constraint.
 * <p>
 * This constraint is very similar to {@link BaseFieldConstraint} but is more suited to be the base class for
 * constraints that do not care about {@link FormElement} data types.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public abstract class BaseGenericFieldConstraint extends BaseConstraint {

  // The form field that this constraint applies to.
  private GenericFormElement field;

  /**
   * An empty constructor that does not bind this constraint with a form element. The only way to bind a constraint with
   * a form field, is by a constructor.)
   */
  public BaseGenericFieldConstraint() {}

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
  public BaseGenericFieldConstraint(GenericFormElement field) {
    Assert.notNullParam(this, field, "field");
    this.field = field;
  }

  /**
   * Returns the {@link FormElement} that this <code>Constraint</code> is constraining.
   * 
   * @return constrained {@link FormElement}
   */
  protected GenericFormElement getField() {
    if (this.field != null) {
      return this.field;
    }

    GenericFormElement result;
    try {
      result = (GenericFormElement) getEnvironment().requireEntry(FormElementContext.class);
    } catch (NoSuchEnvironmentEntryException e) {
      throw new FieldConstraintException(Assert.thisToString(this) + " could not determine FormElementContext, this "
          + "is probably caused by applying field constraint to something other than FormElement.", e);
    }
    return result;
  }

  @Override
  public Environment getEnvironment() {
    return this.field == null ? super.getEnvironment() : this.field.getConstraintEnvironment();
  }

  /**
   * Provides the value of the constraint field.
   * 
   * @return the value of the constraint field.
   */
  protected Object getValue() {
    return getField().getValue();
  }

  /**
   * Specifies whether the constraint field is disabled.
   * 
   * @return a Boolean indicating whether the constraint field is disabled.
   */
  public boolean isDisabled() {
    return getField().isDisabled();
  }
}
