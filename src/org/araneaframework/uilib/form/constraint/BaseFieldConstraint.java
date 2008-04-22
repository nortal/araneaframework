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

import org.araneaframework.Environment;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoSuchEnvironmentEntryException;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * {@link org.araneaframework.uilib.form.Constraint} that is associated with
 * some {@link org.araneaframework.uilib.form.FormElement}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseFieldConstraint extends BaseConstraint {
  private FormElement field;

  public BaseFieldConstraint() {
  }
  
  // Constraints environment should always be set to field environment.
  // however there is no guarantee that field has been initialized when
  // constructor is called, so the environment may be missing crucial entries.
  // Just setting field constraint works (then constraints environment is set when 
  // field is initialized). That would break constraint previously set however.
  public BaseFieldConstraint(FormElement field) {
    Assert.notNullParam(this, field, "field");
    this.field = field;
  }

  /**
   * Returns the {@link FormElement} that 
   * this {@link org.araneaframework.uilib.form.Constraint} is constraining.
   * 
   * @return constrained {@link FormElement}
   */
  protected FormElementContext getField() {
    if (field != null)
      return field;

    FormElementContext result;
    try {
      result = (FormElementContext)getEnvironment().requireEntry(FormElementContext.class);
    } catch (NoSuchEnvironmentEntryException e) {
      throw new FieldConstraintException(Assert.thisToString(this) + " could not determine FormElementContext, this is probably caused by applying field constraint to something other than FormElement.", e);
    }
    return result;
  }

  public Environment getEnvironment() {
    if (field == null)
	  return super.getEnvironment();
    return field.getConstraintEnvironment();
  }

  protected String getLabel() {
    return getField().getLabel();
  }

  protected Object getValue() {
    return getField().getValue();
  }

  public boolean isRead() {
    return getField().isRead();
  }

  public boolean isDisabled() {
    return getField().isDisabled();
  }

  public boolean isMandatory() {
    return getField().isMandatory();
  }

  public boolean isReadOnly() {
    return getField().isReadOnly();
  }

  /**
   * Exception thrown when {@link org.araneaframework.uilib.form.FormElement} associated with 
   * {@link BaseFieldConstraint} could not be determined.
   */
  public static class FieldConstraintException extends AraneaRuntimeException {
    public FieldConstraintException() {
      super();
    } 

    public FieldConstraintException(String message, Throwable cause) {
      super(message, cause);
    }

    public FieldConstraintException(String message) {
      super(message);
    }

    public FieldConstraintException(Throwable cause) {
      super(cause);
    }
  }
}
