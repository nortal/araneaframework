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

package org.araneaframework.uilib.form.converter;

import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Converter;

/**
 * Converts <code>String</code> to <code>Boolean</code> and back.
 * <p>
 * Since 2.0, this class completely replaces <code>BooleanToYNConverter</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StringToBooleanConverter extends BaseConverter<String, Boolean> {

  private String[] allowedTrueValues = { "true", "yes", "y" };

  private String trueToString = "true";

  private String falseToString = "false";

  /**
   * Creates a Boolean to String converter with default settings.
   */
  public StringToBooleanConverter() {}

  /**
   * Creates a Boolean to String converter where <code>true</code> will be rendered as <code>trueToString</code> and
   * <code>false</code> will be rendered as <code>falseToString</code>. The value of <code>trueToString</code> must be
   * included in the array of allowed <code>true</code>-values!
   * 
   * @param trueToString A non-empty <code>String</code> value this converter will use for <code>true</code> value.
   * @param falseToString A non-empty <code>String</code> value this converter will use for <code>false</code> value.
   * @since 2.0
   */
  public StringToBooleanConverter(String trueToString, String falseToString) {
    Assert.notEmptyParam(this, trueToString, "trueToString");
    Assert.notEmptyParam(this, falseToString, "falseToString");
    Assert.isTrue(contains(this.allowedTrueValues, trueToString),
        "The 'trueToString' value is missing from the array of 'allowedTrueValues'!");

    this.trueToString = trueToString;
    this.falseToString = falseToString;
  }

  /**
   * Creates a Boolean to String converter that accepts given <code>true</code> values. Other values will be resolved to
   * <code>false</code>. The array won't be checked for uniqueness nor for presence of values, however, the value of
   * {@link #trueToString} must be included.
   * 
   * @param allowedTrueValues A non-<code>null</code> array of allowed <code>true</code> values.
   * @since 2.0
   */
  public StringToBooleanConverter(String[] allowedTrueValues) {
    Assert.notNullParam(this, allowedTrueValues, "allowedTrueValues");
    Assert.isTrue(contains(allowedTrueValues, this.trueToString),
        "The 'trueToString' value is missing from the array of 'allowedTrueValues'!");

    this.allowedTrueValues = allowedTrueValues;
  }

  /**
   * Creates a Boolean to String converter where <code>true</code> will be rendered as <code>trueToString</code> and
   * <code>false</code> will be rendered as <code>falseToString</code>.
   * <p>
   * The <code>allowedTrueValues</code> parameter specifies, which values will be resolved (case-insensitive) to
   * <code>true</code>. Other values will be resolved to <code>false</code>. The array won't be checked for uniqueness
   * nor for presence of values, however, the value of {@link #trueToString} must be included.
   * 
   * @param trueToString A non-empty <code>String</code> value this converter will use for <code>true</code> value.
   * @param falseToString A non-empty <code>String</code> value this converter will use for <code>false</code> value.
   * @param allowedTrueValues A non-<code>null</code> array of allowed <code>true</code> values.
   * @since 2.0
   */
  public StringToBooleanConverter(String trueToString, String falseToString, String[] allowedTrueValues) {
    Assert.notEmptyParam(this, trueToString, "trueToString");
    Assert.notEmptyParam(this, falseToString, "falseToString");
    Assert.notNullParam(this, allowedTrueValues, "allowedTrueValues");

    Assert.isTrue(contains(allowedTrueValues, trueToString),
        "The 'trueToString' value is missing from the array of 'allowedTrueValues'!");

    this.trueToString = trueToString;
    this.falseToString = falseToString;
    this.allowedTrueValues = allowedTrueValues;
  }

  /**
   * The method checks whether the given <code>array</code> contains <code>value</code> (case-insensitive) value.
   * 
   * @param array A non-<code>null</code> array of <code>String</code>s.
   * @param value A non-<code>null</code> and case-insensitive value to search for.
   * @return A <code>Boolean</code> that is <code>true</code> when the <code>array</code> contains <code>value</code>.
   * @since 2.0
   */
  protected static boolean contains(String[] array, String value) {
    for (String arrayValue : array) {
      if (value.equalsIgnoreCase(arrayValue)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Converts <code>String</code> to <code>Boolean</code>.
   */
  @Override
  public Boolean convertNotNull(String data) {
    return contains(this.allowedTrueValues, data);
  }

  /**
   * Converts <code>Boolean</code> to <code>String</code>.
   */
  @Override
  public String reverseConvertNotNull(Boolean data) {
    return data ? this.trueToString : this.falseToString;
  }

  /**
   * Returns <code>new StringToBooleanConverter()</code>.
   */
  @Override
  public Converter<String, Boolean> newConverter() {
    return new StringToBooleanConverter(this.trueToString, this.falseToString, this.allowedTrueValues);
  }
}
