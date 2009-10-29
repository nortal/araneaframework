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

package org.araneaframework.uilib.list.util;

import java.sql.Time;

import java.util.Date;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import org.apache.commons.lang.Validate;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.BaseCompositeConstraint;
import org.araneaframework.uilib.form.control.BigDecimalControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Form utils.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * 
 * @see FormWidget
 */
public class FormUtil {

  // Data

  public static <T> Data<T> createData(Class<T> type) {
    return new Data<T>(type);
  }

  // Controls

  @SuppressWarnings("unchecked")
  public static <T> Control<? super T> createControl(Class<T> type) {
    Validate.notNull(type);

    if (String.class.equals(type)) {
      return (Control<? super T>) createTextControl();
    } else if (Number.class.isAssignableFrom(type)) {

      if (BigDecimal.class.isAssignableFrom(type)) {
        return (Control<? super T>) createFloatControl();
      }

      if (BigInteger.class.isAssignableFrom(type) || Long.class.equals(type) || Integer.class.equals(type)
          || Short.class.equals(type) || Byte.class.equals(type)) {
        return (Control<? super T>) createNumberControl();
      }

      return (Control<? super T>) createFloatControl();

    } else if (Date.class.isAssignableFrom(type)) {
      if (Date.class.equals(type) || java.sql.Date.class.isAssignableFrom(type)) {
        return (Control<? super T>) createDateControl();
      }
      if (Time.class.isAssignableFrom(type)) {
        return (Control<? super T>) createTimeControl();
      }
      if (java.sql.Timestamp.class.isAssignableFrom(type)) {
        return (Control<? super T>) createDateTimeControl();
      }
    } else if (Boolean.class.equals(type)) {
      return (Control<? super T>) createCheckboxControl();
    }

    return (Control<? super T>) createTextControl();
  }

  public static Control<String> createTextControl() {
    return new TextControl();
  }

  public static Control<BigInteger> createNumberControl() {
    return new NumberControl();
  }

  public static Control<BigDecimal> createFloatControl() {
    return new BigDecimalControl();
  }

  public static Control<Timestamp> createDateControl() {
    return new DateControl();
  }

  public static Control<Timestamp> createTimeControl() {
    return new TimeControl();
  }

  public static Control<Timestamp> createDateTimeControl() {
    return new DateTimeControl();
  }

  public static Control<Boolean> createCheckboxControl() {
    return new CheckboxControl();
  }

  // Form elements

  public static <C, D> FormElement<C, D> createElement(String label, Control<C> control, Data<D> data, boolean mandatory) {
    FormElement<C, D> result = new FormElement<C, D>();
    result.setLabel(label);
    result.setMandatory(mandatory);
    result.setControl(control);

    if (data != null) {
      result.setData(data);
    }

    return result;
  }

  // Constraints

  /**
   * Adds a constraint to a form.
   * 
   * @param form form.
   * @param constraint constraint.
   */
  public static void addConstraint(FormWidget form, Constraint constraint) {
    Constraint current = form.getConstraint();

    if (current == null) {
      form.setConstraint(constraint);
    } else if (current instanceof AndConstraint) {
      ((AndConstraint) current).addConstraint(constraint);
    } else {
      BaseCompositeConstraint and = new AndConstraint();
      and.addConstraint(current);
      and.addConstraint(constraint);
      form.setConstraint(and);
    }
  }

  /**
   * Converts and validates the form data. It is similar to {@link FormWidget#convertAndValidate()} except that it also
   * throws an exception when there are validation errors.
   * 
   * @param form The form to convert and validate.
   * @throws ValidationFailureException It is thrown when there are validation errors after validating.
   * @since 1.1.4
   */
  public static void convertAndValidate(FormWidget form) throws ValidationFailureException {

    Assert.notNull(form, "The 'form' parameter is required to convert and validate.");

    if (!form.convertAndValidate()) {
      throw new ValidationFailureException("The form '" + form.getScope() + "' has validation errors.");
    }
  }

}
