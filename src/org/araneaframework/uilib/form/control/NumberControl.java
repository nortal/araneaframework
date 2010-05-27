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

package org.araneaframework.uilib.form.control;

import java.math.BigInteger;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class represents a textbox control that accepts only valid integer numbers.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class NumberControl extends BlankStringNullableControl<BigInteger> implements FilteredInputControl<BigInteger> {

  private InputFilter inputFilter;

  private BigInteger minValue;

  private BigInteger maxValue;

  /**
   * Empty constructor.
   */
  public NumberControl() {}

  /**
   * Makes a number control that has minimum and maximum value.
   * 
   * @param minValue The minimum permitted value.
   * @param maxValue The maximum permitted value.
   */
  public NumberControl(BigInteger minValue, BigInteger maxValue) {
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  /**
   * Sets the maximum permitted value.
   * 
   * @param maxValue The maximum permitted value.
   */
  public void setMaxValue(BigInteger maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minimum permitted value.
   * 
   * @param minValue The minimum permitted value.
   */
  public void setMinValue(BigInteger minValue) {
    this.minValue = minValue;
  }

  /**
   * Returns the maximum permitted value.
   * 
   * @return The maximum permitted value.
   */
  public BigInteger getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the minimum permitted value.
   * 
   * @return The minimum permitted value.
   */
  public BigInteger getMinValue() {
    return minValue;
  }

  public DataType getRawValueType() {
    return new DataType(BigInteger.class);
  }

  public InputFilter getInputFilter() {
    return this.inputFilter;
  }

  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  /**
   * Checks that the submitted data is a valid integer number.
   */
  @Override
  protected BigInteger fromRequest(String parameterValue) {
    BigInteger result = null;

    try {
      result = parameterValue == null ? null : new BigInteger(parameterValue);
    } catch (NumberFormatException e) {
      addErrorWithLabel(UiLibMessages.NOT_INTEGER);
    }

    if (getInputFilter() != null && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
      addErrorWithLabel(getInputFilter().getInvalidInputMessage(), getInputFilter().getCharacterFilter());
    }

    return result;
  }

  @Override
  protected <E extends BigInteger> String toResponse(E controlValue) {
    return controlValue.toString();
  }

  /**
   * Checks that the submitted value is in permitted range.
   */
  @Override
  protected void validateNotNull() {
    boolean lessThanMin = this.minValue == null ? false : getRawValue().compareTo(this.minValue) == -1;
    boolean greaterThanMax = this.maxValue == null ? false : getRawValue().compareTo(this.maxValue) == 1;

    if (lessThanMin || greaterThanMax) {
      addErrorWithLabel(UiLibMessages.NUMBER_NOT_BETWEEN, this.minValue.toString(), this.maxValue.toString());
    } else if (lessThanMin) {
      addErrorWithLabel(UiLibMessages.NUMBER_NOT_GREATER, this.minValue.toString());
    } else if (greaterThanMax) {
      addErrorWithLabel(UiLibMessages.NUMBER_NOT_LESS, this.maxValue.toString());
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * The view model implementation of <code>NumberControl</code>. The view model provides the data for tags to render
   * the control.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends StringArrayRequestControl<BigInteger>.ViewModel {

    private InputFilter inputFilter;

    private BigInteger maxValue;

    private BigInteger minValue;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.maxValue = NumberControl.this.getMaxValue();
      this.minValue = NumberControl.this.getMinValue();
      this.inputFilter = NumberControl.this.getInputFilter();
    }

    /**
     * Returns the maximum permitted value, or <code>null</code>, if not provided.
     * 
     * @return The maximum permitted value, or <code>null</code>, if not provided.
     */
    public BigInteger getMaxValue() {
      return this.maxValue;
    }

    /**
     * Returns the minimum permitted value, or <code>null</code>, if not provided.
     * 
     * @return The minimum permitted value, or <code>null</code>, if not provided.
     */
    public BigInteger getMinValue() {
      return this.minValue;
    }

    /**
     * Provides the input filter settings of this control, or <code>null</code>, if not provided.
     * 
     * @return The input filter settings, or <code>null</code>, if not provided.
     */
    public InputFilter getInputFilter() {
      return this.inputFilter;
    }
  }
}
