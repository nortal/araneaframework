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

import static org.araneaframework.uilib.util.ConfigurationUtil.getCustomDateFormat;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * This class represents a {@link org.araneaframework.uilib.form.control.TimestampControl}, that holds only date - that
 * is it's default pattern is "dd.MM.yyyy".
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DateControl extends TimestampControl {

  protected int defaultHour;

  protected int defaultMinute;

  protected int defaultSecond;

  /**
   * This is the default date format for this control.
   */
  public final static String DEFAULT_FORMAT = "dd.MM.yyyy";

  /**
   * Creates the control initializing the pattern to {@link #DEFAULT_FORMAT}.
   */
  public DateControl() {
    super(DEFAULT_FORMAT, DEFAULT_FORMAT);
  }

  /**
   * Creates the control initializing the pattern to <code>dateTimeFormat</code>.
   * 
   * @param dateTimeFormat the custom pattern.
   */
  public DateControl(String dateTimeFormat, String defaultOutputFormat) {
    super(dateTimeFormat, defaultOutputFormat);
    this.confOverridden = true;
  }

  /**
   * Overrides the default time that is set on all dates read from request.
   */
  public void setDefaultTime(int hour, int minute, int second) {
    this.defaultHour = hour;
    this.defaultMinute = minute;
    this.defaultSecond = second;
  }

  protected void init() throws Exception {
    super.init();
    if (!this.confOverridden) {
      this.dateTimeInputPattern = getCustomDateFormat(getEnvironment(), true, this.dateTimeInputPattern);
      this.dateTimeOutputPattern = getCustomDateFormat(getEnvironment(), false, this.dateTimeOutputPattern);
    }
  }

  @Override
  protected Timestamp fromRequest(String parameterValue) {
    Timestamp result = super.fromRequest(parameterValue);

    if (result != null) {
      Calendar cal = getCalendarInstance();
      cal.setTime(result);
      cal.set(Calendar.HOUR_OF_DAY, this.defaultHour);
      cal.set(Calendar.MINUTE, this.defaultMinute);
      cal.set(Calendar.SECOND, this.defaultSecond);
      result = new Timestamp(cal.getTime().getTime());
    }

    return result;
  }

  /**
   * Used by {@link DateControl#fromRequest(String)} to acquire <code>Calendar</code> instance for converting value read
   * from request to <code>TimeStamp</code>
   * 
   * @return <code>Calendar</code> using the default time zone and default locale.
   * @since 1.0.3
   */
  protected Calendar getCalendarInstance() {
    return Calendar.getInstance();
  }
}
