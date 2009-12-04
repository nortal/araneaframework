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

import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.JodaDateUtil;
import org.araneaframework.uilib.util.JodaDateUtil.ParsedDate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class holds the default functionality for Joda*Controls.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public abstract class JodaBaseControl extends BlankStringNullableControl<DateTime> implements
  FilteredInputControl<DateTime> {

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
  protected boolean confOverridden = false;

  /**
   * The custom input filter for this control. This can be enforced both client-side and server-side.
   */
  private InputFilter inputFilter;

  public JodaBaseControl(String dateTimeFormat, String defaultOutputFormat) {
    this.dateTimeInputPattern = dateTimeFormat;
    this.dateTimeOutputPattern = defaultOutputFormat;
    this.confOverridden = true;
  }

  public InputFilter getInputFilter() {
    return this.inputFilter;
  }

  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  public DataType getRawValueType() {
    return new DataType(DateTime.class);
  }

  @Override
  protected DateTime fromRequest(String parameterValue) {
    ParsedDate result = parseDate(parameterValue);
    DateTime resultDateTime = result.getDate();

    if (resultDateTime != null) {
      resultDateTime = modifyValue(resultDateTime);
    }

    if (resultDateTime != null) {
      return resultDateTime;
    }

    addWrongTimeFormatError();

    if (parameterValue != null && getInputFilter() != null
        && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
      addErrorWithLabel(getInputFilter().getInvalidInputMessage(), getInputFilter().getCharacterFilter());
    }

    return null;
  }

  @Override
  protected String toResponse(DateTime controlValue) {
    return controlValue != null ? getOutputFormatter().print(controlValue) : "";
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
    return JodaDateUtil.parseJoda(parameterValue, this.dateTimeInputPattern);
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
  public class ViewModel extends StringArrayRequestControl<DateTime>.ViewModel {

    private String dateTimeOutputPattern;

    private InputFilter inputFilter;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.dateTimeOutputPattern = JodaBaseControl.this.dateTimeOutputPattern;
      this.inputFilter = JodaBaseControl.this.getInputFilter();
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

  protected DateTimeFormatter getOutputFormatter() {
    return DateTimeFormat.forPattern(this.dateTimeOutputPattern);
  }

  protected DateTime modifyValue(DateTime date) {
    return date;
  }
}
