/*
 * Copyright 2006 Webmedia Group Ltd. Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.araneaframework.uilib.util;

import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

/**
 * An util class for Joda Date parsing. Use it if you have Joda time API in your classpath.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.1
 */
public abstract class JodaDateUtil {

  protected static final Log LOG = LogFactory.getLog(JodaDateUtil.class);

  protected static final int MIN_YEAR = ValidationUtil.MIN_YEAR;

  protected static final int MAX_YEAR = ValidationUtil.MAX_YEAR;

  public static ValidationUtil.ParsedDate parseJodaJdk(String pattern, String value) {
    ParsedDate parsedDate = parseJoda(pattern, value);
    ValidationUtil.ParsedDate result = null;

    if (parsedDate != null) {
      DateTime dateTime = parsedDate.getDate();
      if (dateTime != null && dateTime.getYear() >= MIN_YEAR && dateTime.getYear() <= MAX_YEAR) {

        // The DateTime.toDate() does not always return the exact date in a JDK Date object. (Note that it is NOT a Joda
        // Date API bug!) Therefore we copy fields one by one.

        Calendar cal = Calendar.getInstance();
        cal.setLenient(true);
        cal.set(Calendar.YEAR, dateTime.year().get());
        cal.set(Calendar.MONTH, dateTime.monthOfYear().get() - 1);
        cal.set(Calendar.DAY_OF_MONTH, dateTime.dayOfMonth().get());
        cal.set(Calendar.HOUR_OF_DAY, dateTime.hourOfDay().get());
        cal.set(Calendar.MINUTE, dateTime.minuteOfHour().get());
        cal.set(Calendar.SECOND, dateTime.secondOfMinute().get());
        cal.set(Calendar.MILLISECOND, dateTime.millisOfSecond().get());

        if (LOG.isTraceEnabled()) {
          String text = DateTimeFormat.forPattern(pattern).print(dateTime);
          LOG.trace("Parsed Joda date '" + text + "'; JDK Date version: '" + cal.getTime() + "'.");
        }

        result = new ValidationUtil.ParsedDate(cal.getTime(), pattern);
      }
    }

    return result;
  }

  public static ParsedDate parseJoda(String pattern, String value) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Using Joda with pattern '" + pattern + "' to parse date '" + value + "'.");
    }

    if (value.trim().length() == pattern.length()) {
      int offset = 0;
      DateTime date = null;
      while (date == null) {
        try {
          date = DateTimeFormat.forPattern(pattern).withZone(DateTimeZone.forOffsetHours(offset)).parseDateTime(value);
        } catch (Exception e) {
          if (offset == 24) {
            return null;
          }
          offset++;
          if (LOG.isTraceEnabled()) {
            LOG.trace("Trying to parse date with offset " + offset + "...");
          }
        }
      }

      if (date != null && date.getYear() >= MIN_YEAR && date.getYear() <= MAX_YEAR) {
        if (LOG.isTraceEnabled()) {
          String text = DateTimeFormat.forPattern(pattern).print(date);
          LOG.trace("Parsed Joda date '" + text + "'.");
        }

        return new ParsedDate(date, pattern);
      }
    }

    return null;
  }

  public static class ParsedDate {

    protected DateTime date;

    protected String outputPattern;

    public ParsedDate(DateTime date, String outputPattern) {
      this.date = date;
      this.outputPattern = outputPattern;
    }

    public DateTime getDate() {
      return this.date;
    }

    public String getOutputPattern() {
      return this.outputPattern;
    }

  }
}
