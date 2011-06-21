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
import org.araneaframework.uilib.form.FormElement;

/**
 * A default range constraint for String form elements. The {@link Collator} functionality will be used for comparison.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 * @see Collator
 */
public class StringRangeConstraint extends RangeConstraint<String, String> {

  /**
   * The locale to use for comparing strings. Defaults to the current application locale.
   */
  protected Locale locale;

  /**
   * The decomposition mode parameter that will be passed to {@link Collator#setDecomposition(int)}. When left to
   * <code>-1</code> then the property won't be set.
   * 
   * @see Collator
   */
  protected int decomposition;

  /**
   * The decomposition mode parameter that will be passed to {@link Collator#setStrength(int)}. When left to
   * <code>-1</code> then the property won't be set.
   * 
   * @see Collator
   */
  protected int strength;

  /**
   * Creates a new string range constraint that checks that form field values are in the right order: the
   * <code>fieldLo</code> is less than <code>fieldHi</code>. It is possible to specify through <code>allowEquals</code>
   * whether the values may be equal or not.
   * 
   * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or <code>null</code>)
   * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or <code>null</code>)
   * @param allowEquals If this is <code>true</code>, the constraint will be considered satisfied when values of
   *          <code>fieldLo</code> and <code>fieldHi</code> are equal. Otherwise the constraint won't be satisfied in
   *          this case.
   */
  public StringRangeConstraint(FormElement<String, String> fieldLo, FormElement<String, String> fieldHi,
      boolean allowEquals) {
    this(fieldLo, fieldHi, allowEquals, null, -1, -1);
  }

  /**
   * Creates a new string range constraint that checks that form field values are in the right order: the
   * <code>fieldLo</code> is less than <code>fieldHi</code>. It is possible to specify through <code>allowEquals</code>
   * whether the values may be equal or not.
   * 
   * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or <code>null</code>)
   * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or <code>null</code>)
   * @param allowEquals If this is <code>true</code>, the constraint will be considered satisfied when values of
   *          <code>fieldLo</code> and <code>fieldHi</code> are equal. Otherwise the constraint won't be satisfied in
   *          this case.
   * @param locale The locale to use for comparing strings. Defaults to the current application locale, when
   *          <code>null</code>.
   */
  public StringRangeConstraint(FormElement<String, String> fieldLo, FormElement<String, String> fieldHi,
      boolean allowEquals, Locale locale) {
    this(fieldLo, fieldHi, allowEquals, locale, -1, -1);
  }

  /**
   * Creates a new string range constraint that checks that form field values are in the right order: the
   * <code>fieldLo</code> is less than <code>fieldHi</code>. It is possible to specify through <code>allowEquals</code>
   * whether the values may be equal or not.
   * 
   * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or <code>null</code>)
   * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or <code>null</code>)
   * @param allowEquals If this is <code>true</code>, the constraint will be considered satisfied when values of
   *          <code>fieldLo</code> and <code>fieldHi</code> are equal. Otherwise the constraint won't be satisfied in
   *          this case.
   * @param locale The locale to use for comparing strings. Defaults to the current application locale, when
   *          <code>null</code>.
   */
  public StringRangeConstraint(FormElement<String, String> fieldLo, FormElement<String, String> fieldHi,
      boolean allowEquals, Locale locale, int decomposition) {
    this(fieldLo, fieldHi, allowEquals, locale, decomposition, -1);
  }

  /**
   * Creates a new string range constraint that checks that form field values are in the right order: the
   * <code>fieldLo</code> is less than <code>fieldHi</code>. It is possible to specify through <code>allowEquals</code>
   * whether the values may be equal or not.
   * 
   * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or <code>null</code>)
   * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or <code>null</code>)
   * @param allowEquals If this is <code>true</code>, the constraint will be considered satisfied when values of
   *          <code>fieldLo</code> and <code>fieldHi</code> are equal. Otherwise the constraint won't be satisfied in
   *          this case.
   * @param locale The locale to use for comparing strings. Defaults to the current application locale, when
   *          <code>null</code>.
   * @param decomposition The decomposition type used when comparing <code>String</code>s. See
   *          {@link Collator#getDecomposition()} for more information.
   * @param strength The minimum level of difference considered significant during comparison. See
   *          {@link Collator#setStrength(int)} for more information.
   * @see Collator
   */
  public StringRangeConstraint(FormElement<String, String> fieldLo, FormElement<String, String> fieldHi,
      boolean allowEquals, Locale locale, int decomposition, int strength) {
    super(fieldLo, fieldHi, allowEquals);
    this.locale = locale;
    this.decomposition = decomposition;
    this.strength = strength;
  }

  @Override
  protected void validateConstraint() {
    String valueLo = this.fieldLo.getValue();
    String valueHi = this.fieldHi.getValue();

    if (valueLo == null || valueHi == null) {
      return;
    }

    int comparison = createCollator().compare(valueLo, valueHi);

    if (!this.allowEquals && comparison == 0 || comparison > 0) {
      addErrorMessage();
    }
  }

  /**
   * Creates <code>Collator</code> and configures it.
   * 
   * @return The created <code>Collator</code>.
   */
  protected Collator createCollator() {
    Collator collator = Collator.getInstance(resolveLocale());
    if (this.decomposition != -1) {
      collator.setDecomposition(this.decomposition);
    }
    if (this.strength != -1) {
      collator.setDecomposition(this.strength);
    }
    return collator;
  }

  @Override
  protected Locale resolveLocale() {
    return this.locale != null ? this.locale : super.resolveLocale();
  }
}
