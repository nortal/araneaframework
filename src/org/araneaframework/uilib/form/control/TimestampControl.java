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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ValidationUtil;
import org.araneaframework.uilib.util.ValidationUtil.ParsedDate;

/**
 * This class represents a generalization of controls that have a value of type <code>Timestamp</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class TimestampControl extends BlankStringNullableControl<Timestamp> implements
    FilteredInputControl<Timestamp> {

  /**
   * The date-time format used by the control for parsing request data.
   */
  protected String dateTimeInputPattern;

  /**
   * The date-time format used by the control for outputting date-time value for rendering.
   */
  protected String dateTimeOutputPattern;

  /**
   * Controls whether a custom format has been specified.
   */
  protected boolean confOverridden;

  /**
   * The custom input filter for this control. This can be enforced both client-side and server-side.
   */
  private InputFilter inputFilter;

  /**
   * Creates the control setting its date format patterns to the ones provided.
   * 
   * @param dateTimeInputFormat A date input parsing pattern to be used with {@link SimpleDateFormat}.
   * @param dateTimeOutputPattern A date output pattern to be used with {@link SimpleDateFormat}.
   */
  public TimestampControl(String dateTimeInputFormat, String dateTimeOutputPattern) {
    this.dateTimeInputPattern = dateTimeInputFormat;
    this.dateTimeOutputPattern = dateTimeOutputPattern;
  }

  public InputFilter getInputFilter() {
    return this.inputFilter;
  }

  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  public DataType getRawValueType() {
    return new DataType(Timestamp.class);
  }

  @Override
  protected Timestamp fromRequest(String parameterValue) {
    ValidationUtil.ParsedDate result = parseDate(parameterValue);

    if (result != null) {
      this.dateTimeOutputPattern = result.getOutputPattern();
      return new Timestamp(result.getDate().getTime());
    }

    addWrongTimeFormatError();

    if (parameterValue != null && getInputFilter() != null
        && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
      addErrorWithLabel(getInputFilter().getInvalidInputMessage(), getInputFilter().getCharacterFilter());
    }

    return null;
  }

  /**
   * Adds an error message to indicate that the input date could not be parsed with our pattern.
   * 
   * @since 1.1
   */
  protected void addWrongTimeFormatError() {
    addErrorWithLabel(UiLibMessages.WRONG_DATE_FORMAT, this.dateTimeInputPattern);
  }

  /**
   * Used by {@link TimestampControl#fromRequest(String)} to convert value read from request to a <code>Date</code> in
   * default <code>TimeZone</code> and <code>Locale</code>.
   * 
   * @param parameterValue The request parameter that should be parsed.
   * @return The <code>ParsedDate</code> object containg the information about parsed value.
   * @since 1.0.3
   */
  protected ParsedDate parseDate(String parameterValue) {
    return ValidationUtil.parseDate(parameterValue, this.dateTimeInputPattern);
  }

  @Override
  protected String toResponse(Timestamp controlValue) {
    return new SimpleDateFormat(this.dateTimeOutputPattern).format(controlValue);
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  // *********************************************************************
  // * VIEWMODEL
  // *********************************************************************

  /**
   * The view model implementation of <code>TimestampControl</code>. The view model provides the data for tags to render
   * the control.
   */
  public class ViewModel extends StringArrayRequestControl<Timestamp>.ViewModel {

    private String dateTimeOutputPattern;

    private InputFilter inputFilter;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.dateTimeOutputPattern = TimestampControl.this.dateTimeOutputPattern;
      this.inputFilter = TimestampControl.this.getInputFilter();
    }

    /**
     * Provides the formatter using the specified output pattern formatter.
     * 
     * @return The formatter that can be use for rendering the values.
     */
    public SimpleDateFormat getCurrentSimpleDateTimeFormat() {
      return new SimpleDateFormat(this.dateTimeOutputPattern);
    }

    /**
     * The filtering settings of this control that are used for validating the input. If <code>null</code> then no input
     * filtering should be done.
     * 
     * @return The input filtering settings, or <code>null</code>.
     */
    public InputFilter getInputFilter() {
      return this.inputFilter;
    }
  }
}
