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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class represents a control that has both date and time.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DateTimeControl extends BaseControl<Timestamp> {

  /**
   * The event adapter that helps to register and invoke events.
   * 
   * @since 1.0.3
   */
  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();

  /**
   * The date control that handles the date part of the input.
   */
  protected DateControl dateControl;

  /**
   * The time control that handles the time part of the input.
   */
  protected TimeControl timeControl;

  /**
   * Creates both {@link TimeControl}and {@link DateControl}with default parameters.
   */
  public DateTimeControl() {
    this(new DateControl(), new TimeControl());
  }

  /**
   * Creates {@link DateTimeControl} consisting of specified {@link DateControl} and {@link TimeControl}.
   * 
   * @since 1.0.3
   */
  public DateTimeControl(DateControl dateControl, TimeControl timeControl) {
    this.dateControl = dateControl;
    this.timeControl = timeControl;
  }

  /**
   * Creates {@link TimeControl}with <code>timeFormat</code> parameter and {@link DateControl}with
   * <code>dateFormat</code> parameter.
   * 
   * @param dateFormat {@link java.text.SimpleDateFormat}pattern.
   * @param timeFormat {@link java.text.SimpleDateFormat}pattern.
   */
  public DateTimeControl(String dateFormat, String timeFormat, String defaultDateOutputFormat,
      String defaultTimeOutputFormat) {
    this(new DateControl(dateFormat, defaultDateOutputFormat), new TimeControl(timeFormat, defaultTimeOutputFormat));
  }

  public DataType getRawValueType() {
    return new DataType(Timestamp.class);
  }

  @Override
  public boolean isRead() {
    // if date is not present, control cannot have valid value (see comment in addTimeToDate() method).
    return this.dateControl.isRead();
  }

  /**
   * Adds a {@link OnChangeEventListener}, which is called when the control value is changing.
   * 
   * @param onChangeEventListener {@link OnChangeEventListener}, which is called when the control value is changing.
   * @since 1.0.3
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    this.eventHelper.addOnChangeEventListener(onChangeEventListener);
  }

  /**
   * Removes all registered <code>onChange</code> event listeners.
   * 
   * @since 1.0.3
   */
  public void clearOnChangeEventListeners() {
    this.eventHelper.clearOnChangeEventListeners();
  }

  /**
   * Adds two dates assuming the first being date part and other the time. ("dd.MM.yyyy" and "HH:mm:ss" accordingly).
   * 
   * @param date The date to add to.
   * @param time The time to be added.
   * @return The sum of the date and the time.
   */
  private Timestamp addTimeToDate(Timestamp date, Timestamp time) {
    // if date is null, it means that date part is completely cleared
    // and when we just return time, it means that Control now holds a
    // bogus Date since 'Jan 01 00:00:00 EET 1970'.
    // problem was described in task 336 and forum topic
    // http://forum.araneaframework.org/viewtopic.php?t=128

    // date is null, discard the time
    if (date == null) {
      return null;
    } else if (time == null) {
      return date;
    }

    Calendar dateCalendar = getCalendarInstance();
    Calendar timeCalendar = getCalendarInstance();
    dateCalendar.setTime(date);
    timeCalendar.setTime(time);
    dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
    dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
    dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
    dateCalendar.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND));
    return new Timestamp(dateCalendar.getTime().getTime());
  }

  @Override
  protected void init() throws Exception {
    super.init();
    setGlobalEventListener(this.eventHelper);
    addWidget("date", this.dateControl);
    addWidget("time", this.timeControl);
  }

  /**
   * Used by {@link DateTimeControl#convert()} to acquire <code>Calendar</code> instance for converting
   * <code>Date</code> and <code>Time</code> values read from request to single <code>TimeStamp</code>.
   * 
   * @return <code>Calendar</code> using the default time zone and default locale.
   * @since 1.0.3
   */
  protected Calendar getCalendarInstance() {
    return Calendar.getInstance();
  }

  /**
   * 
   */
  @Override
  public void convert() {
    this.dateControl.convert();
    this.timeControl.convert();

    // Reading control data
    if (getFormElementCtx().isValid() && isRead()) {
      this.value = addTimeToDate(this.dateControl.getRawValue(), this.timeControl.getRawValue());
    } else {
      this.value = null;
    }
  }

  @Override
  public void validate() {
    if (isMandatory() && this.value == null) {
      addErrorWithLabel(UiLibMessages.MANDATORY_FIELD);
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  @Override
  public void setRawValue(Timestamp value) {
    // mark composite control dirty
    super.setRawValue(null);
    this.dateControl.setRawValue(value);
    this.timeControl.setRawValue(value);
  }

  @Override
  public void setFormElementCtx(FormElementContext<Timestamp, Object> formElementContext) {
    super.setFormElementCtx(formElementContext);
    this.dateControl.setFormElementCtx(formElementContext);
    this.timeControl.setFormElementCtx(formElementContext);
  }

  /**
   * Represents a date-time control view model, which provides the data for tags to render the control.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends BaseControl<Timestamp>.ViewModel {

    protected String time;

    protected String date;

    protected DateControl.ViewModel dateViewModel;

    protected TimeControl.ViewModel timeViewModel;

    protected boolean hasOnChangeEventListeners;

    protected String timeOutputPattern;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.dateViewModel = (DateControl.ViewModel) dateControl._getViewable().getViewModel();
      String[] timeInnerData = (String[]) DateTimeControl.this.timeControl.innerData;
      this.time = timeInnerData == null ? null : timeInnerData[0];

      this.timeViewModel = (TimeControl.ViewModel) timeControl._getViewable().getViewModel();
      String[] dateInnerData = (String[]) DateTimeControl.this.dateControl.innerData;
      this.date = dateInnerData == null ? null : dateInnerData[0];

      this.hasOnChangeEventListeners = DateTimeControl.this.eventHelper.hasOnChangeEventListeners();
      this.timeOutputPattern = DateTimeControl.this.timeControl.dateTimeOutputPattern;
    }

    /**
     * Returns time as <code>String</code>.
     * 
     * @return time as <code>String</code>.
     */
    public String getTime() {
      return this.time;
    }

    /**
     * Provides the hour (0-23) part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed hour value as string.
     * @since 2.0
     */
    public String getHourOfDay() {
      return readDateValue(this.time, this.timeOutputPattern, Calendar.HOUR_OF_DAY, 0);
    }

    /**
     * Provides the hour (0-11) part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed hour value as string.
     * @since 2.0
     */
    public String getHour() {
      return readDateValue(this.time, this.timeOutputPattern, Calendar.HOUR_OF_DAY, 0);
    }

    /**
     * Provides the minute part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed minute value as string.
     * @since 2.0
     */
    public String getMinutes() {
      return readDateValue(this.time, this.timeOutputPattern, Calendar.MINUTE, 0);
    }

    /**
     * Provides the seconds part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed seconds value as string.
     * @since 2.0
     */
    public String getSeconds() {
      return readDateValue(this.time, this.timeOutputPattern, Calendar.SECOND, 0);
    }

    /**
     * Returns date as <code>String</code>.
     * 
     * @return date as <code>String</code>.
     */
    public String getDate() {
      return this.date;
    }

    /**
     * Parses a specific value (specified by <code>calendarField</code>) from the given date/time <code>value</code>
     * using the given <code>pattern</code>. When the given <code>value</code> is null or an exception occurs then the
     * <code>defaultValue</code> will be returned as string. The calendar field is expected to be a value of a constant
     * in {@link Calendar} class.
     * 
     * @param value The date/time value as string. May be <code>null</code> or an empty string.
     * @param pattern The date/time pattern to use for parsing the <code>value</code>.
     * @param calendarField A constant from {@link Calendar} class.
     * @param defaultValue The default value to use when parsing fails. It will be converted into string.
     * @return The date/time field value as string.
     * @since 2.0
     */
    protected String readDateValue(String value, String pattern, int calendarField, int defaultValue) {
      int parsedValue = defaultValue;

      if (StringUtils.isNotBlank(value)) {
        try {
          Calendar cal = Calendar.getInstance();
          Date time = new SimpleDateFormat(pattern).parse(value);
          cal.setTime(time);
          parsedValue = cal.get(calendarField);
        } catch (ParseException e) {
          // OK, we use the default value.
        }
      }

      return Integer.toString(parsedValue);
    }

    /**
     * Provides whether this date-time control has any bound "onChange" event listeners.
     * 
     * @return A <code>Boolean</code> indicating whether this control has any bound "onChange" event listeners.
     * @since 1.0.3
     */
    public boolean isOnChangeEventRegistered() {
      return this.hasOnChangeEventListeners;
    }

    /**
     * Provides the view model of date control.
     * 
     * @return The view model of date control.
     */
    public DateControl.ViewModel getDateViewModel() {
      return this.dateViewModel;
    }

    /**
     * Provides the view model of time control.
     * 
     * @return The view model of time control.
     */
    public TimeControl.ViewModel getTimeViewModel() {
      return this.timeViewModel;
    }
  }
}
