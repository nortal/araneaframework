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

package org.araneaframework.uilib.util;

import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Utility class for Joda Date parsing. Use it if you have Joda time API in your classpath.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.1
 * @see <a href="http://joda-time.sourceforge.net/" target="_blank">http://joda-time.sourceforge.net/</a>
 */
public abstract class JodaDateUtil {

  protected static final Log LOG = LogFactory.getLog(JodaDateUtil.class);

  protected static final int MIN_YEAR = ValidationUtil.MIN_YEAR;

  protected static final int MAX_YEAR = ValidationUtil.MAX_YEAR;

  /**
   * Parses the pattern into a date object using Joda date and time API. The returned date data will be stored in JDK
   * date objects.
   * 
   * @param pattern The date/time pattern to use when parsing.
   * @param value The value that should match the given pattern.
   * @return The {@link ValidationUtil.ParsedDate} object when parsing was successful, or <code>null</code>.
   */
  public static ValidationUtil.ParsedDate parseJodaJdk(String pattern, String value) {
    ParsedDate parsedDate = parseJoda(pattern, value);
    ValidationUtil.ParsedDate result = null;

    if (parsedDate != null) {
      DateTime dateTime = parsedDate.getDate();
      if (dateTime != null && dateTime.getYear() >= MIN_YEAR && dateTime.getYear() <= MAX_YEAR) {

        // The DateTime.toDate() does not always return the exact date in a JDK Date object. (Note that it is NOT a Joda
        // Date API bug!) Therefore we copy fields one by one.

        Calendar cal = Calendar.getInstance();
        cal.setLenient(false);
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

  /**
   * Parses the pattern into a date object using Joda date and time API. The returned date data will also be stored in
   * Joda API objects.
   * 
   * @param pattern The date/time pattern to use when parsing.
   * @param value The value that should match the given pattern.
   * @return The {@link ParsedDate} object when parsing was successful, or <code>null</code>.
   */
  public static ParsedDate parseJoda(String pattern, String value) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Using Joda with pattern '" + pattern + "' to parse date '" + value + "'.");
    }

    if (value.trim().length() == pattern.length()) {
      try {
        DateTime date = DateTimeFormat.forPattern(pattern).parseDateTime(value);

        if (date != null && date.getYear() >= MIN_YEAR && date.getYear() <= MAX_YEAR) {
          if (LOG.isTraceEnabled()) {
            String text = DateTimeFormat.forPattern(pattern).print(date);
            LOG.trace("Parsed Joda date '" + text + "'.");
          }

          return new ParsedDate(date, pattern);
        }
      } catch (Exception e) {}
    }

    return null;
  }

  /**
   * Represents the parsed date. Unlike {@link ValidationUtil.ParsedDate}, this one works with Joda data objects.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 2.0
   */
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
