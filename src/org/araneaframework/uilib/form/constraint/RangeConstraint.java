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

import java.text.Collator;
import java.util.Locale;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * Given two form elements, it checks that their values are one after another. It assumes that the values of both form
 * elements are of the same type and {@link Comparable} (so most data types should be supported, including
 * {@link java.lang.String} and {@link java.util.Date}).
 * 
 * @author <a href="mailto:kt@webmedia.ee">Konstantin Tretyakov</a>
 */
public class RangeConstraint<C, D> extends BaseConstraint {

  /**
   * Specifies whether the values may be equal or not.
   */
  protected boolean allowEquals;

  /**
   * Specifies the form field that must have higher value. The value may also be <code>null</code>.
   */
  protected FormElement<C, D> fieldLo;

  /**
   * Specifies the form field that must have higher value. The value may also be <code>null</code>.
   */
  protected FormElement<C, D> fieldHi;

  /**
   * Creates a new range constraint that checks that form field values are in the right order: the <code>fieldLo</code>
   * is less than <code>fieldHi</code>. It is possible to specify through <code>allowEquals</code> whether the values
   * may be equal or not.
   * 
   * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or <code>null</code>)
   * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or <code>null</code>)
   * @param allowEquals If this is <code>true</code>, the constraint will be considered satisfied when values of
   *          <code>fieldLo</code> and <code>fieldHi</code> are equal. Otherwise the constraint won't be satisfied in
   *          this case.
   */
  public RangeConstraint(FormElement<C, D> fieldLo, FormElement<C, D> fieldHi, boolean allowEquals) {
    this.allowEquals = allowEquals;
    this.fieldHi = fieldHi;
    this.fieldLo = fieldLo;
  }

  /**
   * Makes sure that either any of the form elements has a <code>null</code> value, or the low field value is less then
   * high field value.
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void validateConstraint() {
    D valueLo = fieldLo.getData().getValue();
    D valueHi = fieldHi.getData().getValue();

    // If any of the values is null, we stay quiet no matter what.
    if (valueLo == null || valueHi == null) {
      return;
    }

    boolean loExtendsHi;

    if (valueHi.getClass().isAssignableFrom(valueLo.getClass())) {
      loExtendsHi = true;
    } else if (valueLo.getClass().isAssignableFrom(valueHi.getClass())) {
      loExtendsHi = false;
    } else {
      throw new AraneaRuntimeException("RangeConstraint can be used only with fields of compatible types.");
    }

    // Will be -1, 0, or 1 depending on whether sLo is <, = or > than sHi.
    int comparison = 0;

    // Strings are handled separately because we have to compare them in given locale.
    if (valueLo instanceof String && valueHi instanceof String) {
      Collator collator = Collator.getInstance(resolveLocale());
      comparison = collator.compare((String) valueLo, (String) valueHi);

    } else if (valueLo instanceof Comparable<?> && valueHi instanceof Comparable<?>) {
      if (loExtendsHi) {
        comparison = ((Comparable<D>) valueLo).compareTo(valueHi);
      } else {
        comparison = -1 * ((Comparable<D>) valueHi).compareTo(valueLo);
      }

    } else { // Objects are not comparable
      throw new AraneaRuntimeException("RangeConstraint expects fields of java.lang.Comparable type");
    }

    if (comparison > 0 || (!allowEquals && comparison == 0)) {
      addErrorMessage();
    }
  }

  /**
   * Adds an error message that the data did not fulfill the range constraint.
   * 
   * @since 2.0
   */
  protected void addErrorMessage() {
    addError(UiLibMessages.RANGE_CHECK_FAILED, t(this.fieldLo.getLabel()), t(this.fieldHi.getLabel()));
  }

  /**
   * Resolves the current locale.
   * 
   * @return The resolved locale, which is not <code>null</code>.
   * @since 2.0
   */
  protected Locale resolveLocale() {
    Locale result = Locale.getDefault();

    LocalizationContext locCtx = getEnvironment().getEntry(LocalizationContext.class);
    if (locCtx != null) {
      result = locCtx.getLocale();
    }

    return result;
  }
}
