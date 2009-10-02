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
import java.util.Calendar;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This class represents a control that has both date and time.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DateTimeControl extends BaseControl<Timestamp> {

  /** @since 1.0.3 */
  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();

  protected DateControl dateControl;

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

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * Returns "Timestamp".
   * 
   * @return "Timestamp".
   */
  public String getRawValueType() {
    return "Timestamp";
  }

  @Override
  public boolean isRead() {
    // if date is not present, control cannot have valid value -- see comment
    // in addTimeToDate() method
    return this.dateControl.isRead();
  }

  /**
   * @since 1.0.3
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    this.eventHelper.addOnChangeEventListener(onChangeEventListener);
  }

  /**
   * @since 1.0.3
   */
  public void clearOnChangeEventListeners() {
    this.eventHelper.clearOnChangeEventListeners();
  }

  // *******************************************************************
  // HELPER METHODS
  // *******************************************************************

  /**
   * Adds two dates assuming the first being date part and other the time. (dd.MM.yyyy and HH:mm:ss accordingly).
   * 
   * @param date the date to add to.
   * @param time the time to be added.
   * @return the sum of the date and the time.
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
    return new Timestamp(dateCalendar.getTime().getTime());
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  @Override
  protected void init() throws Exception {
    super.init();
    setGlobalEventListener(eventHelper);
    addWidget("date", dateControl);
    addWidget("time", timeControl);
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
    if (isMandatory() && !isRead()) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(),
          UiLibMessages.MANDATORY_FIELD,
          MessageUtil.localize(getLabel(), getEnvironment())));
    }
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  @Override
  public ViewModel getViewModel() throws Exception {
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

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Represents a date-time control view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends BaseControl<Timestamp>.ViewModel {

    private String time;

    private String date;

    private DateControl.ViewModel dateViewModel;

    private TimeControl.ViewModel timeViewModel;

    private boolean hasOnChangeEventListeners;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() throws Exception {
      this.dateViewModel = (DateControl.ViewModel) dateControl._getViewable().getViewModel();
      String[] timeInnerData = (String[]) timeControl.innerData;
      this.time = timeInnerData == null ? null : timeInnerData[0];

      this.timeViewModel = (TimeControl.ViewModel) timeControl._getViewable().getViewModel();
      String[] dateInnerData = (String[]) dateControl.innerData;
      this.date = dateInnerData == null ? null : dateInnerData[0];

      this.hasOnChangeEventListeners = eventHelper.hasOnChangeEventListeners();
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
     * Returns date as <code>String</code>.
     * 
     * @return date as <code>String</code>.
     */
    public String getDate() {
      return this.date;
    }

    /**
     * @since 1.0.3
     */
    public boolean isOnChangeEventRegistered() {
      return this.hasOnChangeEventListeners;
    }

    public DateControl.ViewModel getDateViewModel() {
      return this.dateViewModel;
    }

    public TimeControl.ViewModel getTimeViewModel() {
      return this.timeViewModel;
    }
  }
}
