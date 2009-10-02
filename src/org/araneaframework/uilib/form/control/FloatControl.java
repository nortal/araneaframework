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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This class represents a textbox control that accepts only valid floating-point numbers.
 * 
 * This class does not support localization. It does not use @link NumberFormat class to parse and format @link
 * BigDecimal objects because @link NumberFormat would convert @link BigDecimal objects into doubles.
 * 
 * To customize parsing and formatting one could create a subclass of it and override @link #createBigDecimal(String)
 * and @link #toString(BigDecimal) methods. To use the subclass in JSPs also another JSP Tag must be created to use this
 * implementation and configure validation script.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
// FIXME why the hell class is called FloatControl when it works with BigDecimals?
public class FloatControl extends EmptyStringNullableControl<BigDecimal> implements FilteredInputControl<BigDecimal> {

  private InputFilter inputFilter;

  private BigDecimal minValue;

  private BigDecimal maxValue;

  private Integer maxScale;

  /**
   * Empty.
   */
  public FloatControl() {
  // Empty
  }

  /**
   * Makes a float control that has minimum, maximum value and maximum scale.
   * 
   * @param minValue minimum permitted value.
   * @param maxValue maximum permitted value.
   * @param maxScale maximum permitted scale.
   */
  public FloatControl(BigDecimal minValue, BigDecimal maxValue, Integer maxScale) {
    setMinValue(minValue);
    setMaxValue(maxValue);
    setMaxScale(maxScale);
  }

  /**
   * Makes a float control that has minimum and maximum value.
   * 
   * @param minValue minimum permitted value.
   * @param maxValue maximum permitted value.
   */
  public FloatControl(BigDecimal minValue, BigDecimal maxValue) {
    this(minValue, maxValue, null);
  }

  /**
   * Sets the maximum value.
   * 
   * @param maxValue maximum value.
   */
  public void setMaxValue(BigDecimal maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minimum value.
   * 
   * @param minValue minimum value.
   */
  public void setMinValue(BigDecimal minValue) {
    this.minValue = minValue;
  }

  /**
   * Sets the maximum scale.
   * 
   * @param maxScale maximum scale.
   */
  public void setMaxScale(Integer maxScale) {
    if (maxScale != null && maxScale.intValue() < 0) {
      throw new IllegalArgumentException("Maximum scale cannot be negative");
    }
    this.maxScale = maxScale;
  }

  /**
   * Returns the maxValue.
   * 
   * @return the maxValue.
   */
  public BigDecimal getMaxValue() {
    return this.maxValue;
  }

  /**
   * Returns the minValue.
   * 
   * @return the minValue.
   */
  public BigDecimal getMinValue() {
    return this.minValue;
  }

  /**
   * Returns the maximum scale.
   * 
   * @return the maximum scale.
   */
  public Integer getMaxScale() {
    return this.maxScale;
  }

  /**
   * Returns "BigDecimal".
   * 
   * @return "BigDecimal".
   */
  public String getRawValueType() {
    return "BigDecimal";
  }

  /** @since 1.0.11 */
  public InputFilter getInputFilter() {
    return inputFilter;
  }

  /** @since 1.0.11 */
  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  /**
   * Trims request parameter.
   */
  @Override
  protected String preprocessRequestParameter(String parameterValue) {
    String result = super.preprocessRequestParameter(parameterValue);
    return result == null ? null : result.trim();
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
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NOT_A_NUMBER, MessageUtil.localize(
          getLabel(), getEnvironment())));
    }

    if (getInputFilter() != null && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), getInputFilter().getInvalidInputMessage(), MessageUtil.localize(
          getLabel(), getEnvironment()), getInputFilter().getCharacterFilter()));
    }

    return result;
  }

  @Override
  protected <E extends BigDecimal> String toResponse(E controlValue) {
    return toString(controlValue);
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
   * Converts BigDecimal into String. This method can be overrided in subclasses.
   * 
   * @param dec BigDecimal object
   * @return String object
   */
  protected String toString(BigDecimal dec) {
    return dec == null ? null : dec.toString();
  }

  /**
   * Checks that the submitted value is in permitted range.
   * 
   */
  @Override
  protected void validateNotNull() {
    // minimum and maximum permitted values
    if (this.minValue != null && this.maxValue != null
        && ((getRawValue().compareTo(this.minValue) == -1) || getRawValue().compareTo(this.maxValue) == 1)) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_BETWEEN, new Object[] {
          MessageUtil.localize(getLabel(), getEnvironment()), this.minValue.toString(), maxValue.toString() }));

    } else if (this.minValue != null && getRawValue().compareTo(this.minValue) == -1) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_GREATER, new Object[] {
          MessageUtil.localize(getLabel(), getEnvironment()), this.minValue.toString(), }));

    } else if (this.maxValue != null && getRawValue().compareTo(this.maxValue) == 1) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_LESS, new Object[] {
          MessageUtil.localize(getLabel(), getEnvironment()), this.maxValue.toString(), }));
    }

    // maximum permitted scale
    if (this.maxScale != null && getRawValue().scale() > this.maxScale.intValue()) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.SCALE_NOT_LESS, new Object[] {
          MessageUtil.localize(getLabel(), getEnvironment()), this.maxScale.toString(), }));
    }
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
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

    public InputFilter getInputFilter() {
      return this.inputFilter;
    }
  }
}
