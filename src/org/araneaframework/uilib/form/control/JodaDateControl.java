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

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

/**
 * The DateControl for storing the date data in {@link DateTime} object from the  Joda Time API.
 * Basically has the same functionlity as {@link DateControl}, except the data type.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public class JodaDateControl extends JodaBaseControl {

  protected int defaultHour;

  protected int defaultMinute;

  protected int defaultSecond;

  public JodaDateControl() {
    super(DateControl.DEFAULT_FORMAT, DateControl.DEFAULT_FORMAT);
  }

  public JodaDateControl(String dateTimeFormat, String defaultOutputFormat) {
    super(dateTimeFormat, defaultOutputFormat);
  }

  /**
   * Overrides the default time that is set on all dates read from request.
   */
  public void setDefaultTime(int hour, int minute, int second) {
    this.defaultHour = hour;
    this.defaultMinute = minute;
    this.defaultSecond = second;
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  protected DateTime modifyValue(DateTime date) {
    MutableDateTime mutable = date.toMutableDateTime();
    mutable.setHourOfDay(this.defaultHour);
    mutable.setMinuteOfHour(this.defaultMinute);
    mutable.setSecondOfMinute(this.defaultSecond);
    return mutable.toDateTime();
  }
}
