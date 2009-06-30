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

import org.araneaframework.uilib.form.FormElementContext;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class represents a control that has both date and time. This control is meant to be used
 * with Joda Time API. The expexted date data type is {@link DateTime}.
 * 
 * The functionality basically extends {@link DateTimeControl} for compatibility without adding any
 * special features.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public class JodaDateTimeControl extends DateTimeControl {

  private static final long serialVersionUID = 1L;

  // *******************************************************************
  // FIELDS
  // *******************************************************************

  protected JodaDateControl dateControl;

  protected JodaTimeControl timeControl;

  // *******************************************************************
  // CONSTRUCTORS
  // *******************************************************************

  /**
   * Creates both {@link TimeControl}and {@link DateControl}with default parameters.
   */
  public JodaDateTimeControl() {
    this.dateControl = new JodaDateControl();
    this.timeControl = new JodaTimeControl();
  }

  /**
   * Creates {@link JodaDateTimeControl} consisting of specified {@link DateControl} and
   * {@link TimeControl}.
   * 
   * @since 1.0.3
   */
  public JodaDateTimeControl(JodaDateControl dateControl, JodaTimeControl timeControl) {
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
  public JodaDateTimeControl(String dateFormat, String timeFormat, String defaultDateOutputFormat,
      String defaultTimeOutputFormat) {
    this.dateControl = new JodaDateControl(dateFormat, defaultDateOutputFormat);
    this.timeControl = new JodaTimeControl(timeFormat, defaultTimeOutputFormat);
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * Returns the value class name ({@link DateTime}).
   * 
   * @return The value class name ({@link DateTime}).
   */
  public String getRawValueType() {
    return "DateTime";
  }

  public boolean isRead() {
    // if date isn't present, control can't have valid value - see comment in addTimeToDate() method
    return this.dateControl.isRead();
  }

  // *******************************************************************
  // HELPER METHODS
  // *******************************************************************

  /**
   * Adds two dates assuming the first being date part and other the time. (dd.MM.yyyy and HH:mm:ss
   * accordingly).
   * 
   * @param date the date to add to.
   * @param time the time to be added.
   * @return the sum of the date and the time.
   */
  private DateTime addTimeToDate(DateTime date, DateTime time) {
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

    MutableDateTime mutable = date.toMutableDateTime();
    mutable.setHourOfDay(time.hourOfDay().get());
    mutable.setMinuteOfHour(time.minuteOfHour().get());
    mutable.setSecondOfMinute(time.secondOfMinute().get());
    return mutable.toDateTime();
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  protected void init() throws Exception {
    super.init();

    setGlobalEventListener(this.eventHelper);

    addWidget("date", this.dateControl);
    addWidget("time", this.timeControl);
  }

  public void convert() {
    this.dateControl.convert();
    this.timeControl.convert();

    // Reading control data
    if (getFormElementCtx().isValid() && isRead()) {
      this.value = addTimeToDate((DateTime) this.dateControl.getRawValue(),
          (DateTime) this.timeControl.getRawValue());
    } else {
      this.value = null;
    }
  }

  public Object getViewModel() throws Exception {
    return new ViewModel();
  }

  public void setRawValue(Object value) {
    // mark composite control dirty
    super.setRawValue(null);
    this.dateControl.setRawValue(value);
    this.timeControl.setRawValue(value);
  }

  public void setFormElementCtx(FormElementContext formElementContext) {
    super.setFormElementCtx(formElementContext);
    this.dateControl.setFormElementCtx(formElementContext);
    this.timeControl.setFormElementCtx(formElementContext);
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Extends the {@link DateTimeControl.ViewModel} for {@link JodaDateControl} and
   * {@link JodaTimeControl} integration.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 1.2.3
   */
  public class ViewModel extends DateTimeControl.ViewModel {

    public ViewModel() throws Exception {
      this.dateViewModel = (DateControl.ViewModel) dateControl._getViewable().getViewModel();
      this.date = dateControl.innerData == null ? null : ((String[]) dateControl.innerData)[0];

      this.timeViewModel = (TimeControl.ViewModel) timeControl._getViewable().getViewModel();
      this.time = timeControl.innerData == null ? null : ((String[]) timeControl.innerData)[0];

      this.hasOnChangeEventListeners = eventHelper.hasOnChangeEventListeners();
      parseTime();
    }

    protected DateTime getDateTime() {
      DateTimeFormatter fmt = DateTimeFormat.forPattern(TimeControl.DEFAULT_FORMAT);
      try {
        return fmt.parseDateTime(this.timeViewModel.getSimpleValue());
      } catch (Exception e) {}
      return null;
    }

    protected void parseTime() throws Exception {
      DateTime time = getDateTime();
      if (time != null) {
        this.hour = new Integer(time.getHourOfDay());
        this.minute = new Integer(time.getMinuteOfHour());
        this.second = new Integer(time.getSecondOfMinute());
      }
    }
  }
}
