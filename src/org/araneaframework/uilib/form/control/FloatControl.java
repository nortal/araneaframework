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

import java.math.BigDecimal;
import java.text.NumberFormat;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.core.util.Assert;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class represents a text box control that accepts only valid floating-point numbers.
 * <p>
 * This class does not support localization. It does not use {@link NumberFormat} class to parse and format
 * {@link BigDecimal} objects because {@link NumberFormat} would convert {@link BigDecimal} objects into doubles.
 * <p>
 * To customize parsing and formatting one could create a subclass of it and override {@link #createBigDecimal(String)}
 * and {@link #toString(BigDecimal)} methods. To use the subclass in JSPs, also another JSP Tag must be created to use
 * this implementation and configure validation script.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class FloatControl extends BlankStringNullableControl<BigDecimal> implements FilteredInputControl<BigDecimal> {

  private InputFilter inputFilter;

  private BigDecimal minValue;

  private BigDecimal maxValue;

  private Integer maxScale;

  /**
   * Default and empty constructor.
   */
  public FloatControl() {}

  /**
   * Makes a <code>BigDecimal</code> control that has both minimum and maximum values and the maximum scale.
   * 
   * @param minValue The minimum permitted value.
   * @param maxValue The maximum permitted value.
   * @param maxScale The maximum permitted scale.
   */
  public FloatControl(BigDecimal minValue, BigDecimal maxValue, Integer maxScale) {
    setMinValue(minValue);
    setMaxValue(maxValue);
    setMaxScale(maxScale);
  }

  /**
   * Makes a <code>BigDecimal</code> control that has both minimum and maximum values.
   * 
   * @param minValue The minimum permitted value.
   * @param maxValue The maximum permitted value.
   */
  public FloatControl(BigDecimal minValue, BigDecimal maxValue) {
    this(minValue, maxValue, null);
  }

  /**
   * Sets the maximum value that this control accepts. If <code>maxValue</code> is <code>null</code> then no maximum
   * value check will be done.
   * 
   * @param maxValue The maximum value or <code>null</code>.
   */
  public void setMaxValue(BigDecimal maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minimum value that this control accepts. If <code>minValue</code> is <code>null</code> then no minimum
   * value check will be done.
   * 
   * @param minValue The minimum value or <code>null</code>.
   */
  public void setMinValue(BigDecimal minValue) {
    this.minValue = minValue;
  }

  /**
   * Sets the maximum scale that this control accepts. If <code>maxScale</code> is <code>null</code> then no scale check
   * will be done.
   * 
   * @param maxScale The maximum scale or <code>null</code>.
   */
  public void setMaxScale(Integer maxScale) {
    Assert.isTrue(maxScale == null || maxScale.intValue() >= 0, "Maximum scale cannot be negative");
    this.maxScale = maxScale;
  }

  /**
   * Returns the maximum input value allowed or <code>null</code>, if none is specified.
   * 
   * @return The maximum input value allowed or <code>null</code>, if none is specified.
   */
  public BigDecimal getMaxValue() {
    return this.maxValue;
  }

  /**
   * Returns the minimum input value allowed or <code>null</code>, if none is specified.
   * 
   * @return The minimum input value allowed or <code>null</code>, if none is specified.
   */
  public BigDecimal getMinValue() {
    return this.minValue;
  }

  /**
   * Returns the maximum scale value allowed or <code>null</code>, if none is specified.
   * 
   * @return The maximum scale value allowed or <code>null</code>, if none is specified.
   */
  public Integer getMaxScale() {
    return this.maxScale;
  }

  public DataType getRawValueType() {
    return new DataType(BigDecimal.class);
  }

  public InputFilter getInputFilter() {
    return this.inputFilter;
  }

  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  /**
   * Checks that the submitted data is a valid floating-point number.
   * 
   */
  @Override
  protected BigDecimal fromRequest(String parameterValue) {
    BigDecimal result = null;

    try {
      result = createBigDecimal(parameterValue);
    } catch (NumberFormatException e) {
      addErrorWithLabel(UiLibMessages.NOT_A_NUMBER);
    }

    if (getInputFilter() != null && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
      addErrorWithLabel(getInputFilter().getInvalidInputMessage(), getInputFilter().getCharacterFilter());
    }

    return result;
  }

  @Override
  protected <E extends BigDecimal> String toResponse(E controlValue) {
    return toString(controlValue);
  }

  /**
   * Converts BigDecimal into String. This method can be overridden in subclasses.
   * 
   * @param dec The <code>BigDecimal</code> object to convert.
   * @return The <code>String</code> representation of <code>dec</code> or <code>null</code>, when <code>dec</code> is
   *         <code>null</code>.
   */
  protected String toString(BigDecimal dec) {
    return dec == null ? null : dec.toString();
  }

  /**
   * Converts String into BigDecimal. This method can be overrided in subclasses.
   * 
   * @param str String object
   * @return BigDecimal object
   * @throws NumberFormatException <tt>str</tt> is not a valid representation of a BigDecimal
   */
  protected BigDecimal createBigDecimal(String str) throws NumberFormatException {
    return str == null ? null : new BigDecimal(str);
  }

  /**
   * Checks that the submitted value is in permitted range.
   */
  @Override
  protected void validateNotNull() {
    boolean lessThanMin = this.minValue == null ? false : getRawValue().compareTo(this.minValue) == -1;
    boolean greaterThanMax = this.maxValue == null ? false : getRawValue().compareTo(this.maxValue) == 1;

    // minimum and maximum permitted values
    if (lessThanMin || greaterThanMax) {
      addErrorWithLabel(UiLibMessages.NUMBER_NOT_BETWEEN, this.minValue, this.maxValue);
    } else if (lessThanMin) {
      addErrorWithLabel(UiLibMessages.NUMBER_NOT_GREATER, this.minValue);
    } else if (greaterThanMax) {
      addErrorWithLabel(UiLibMessages.NUMBER_NOT_LESS, this.maxValue);
    }

    // maximum permitted scale
    if (this.maxScale != null && getRawValue().scale() > this.maxScale.intValue()) {
      addErrorWithLabel(UiLibMessages.SCALE_NOT_LESS, this.maxScale);
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * The view model implementation of <code>BigDecimalControl</code>. The view model provides the data for tags to
   * render the control.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends StringArrayRequestControl<BigDecimal>.ViewModel {

    private InputFilter inputFilter;

    private BigDecimal maxValue;

    private BigDecimal minValue;

    private Integer maxScale;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.maxValue = FloatControl.this.getMaxValue();
      this.minValue = FloatControl.this.getMinValue();
      this.maxScale = FloatControl.this.getMaxScale();
      this.inputFilter = FloatControl.this.getInputFilter();
    }

    /**
     * Returns maximum permitted value.
     * 
     * @return maximum permitted value.
     */
    public BigDecimal getMaxValue() {
      return this.maxValue;
    }

    /**
     * Returns minimum permitted value.
     * 
     * @return minimum permitted value.
     */
    public BigDecimal getMinValue() {
      return this.minValue;
    }

    /**
     * Returns maximum permitted scale.
     * 
     * @return maximum permitted scale.
     */
    public Integer getMaxScale() {
      return this.maxScale;
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
