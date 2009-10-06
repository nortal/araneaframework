/**
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
 **/

package org.araneaframework.uilib.util;

import org.joda.time.format.DateTimeFormatter;

import org.joda.time.DateTimeZone;

import org.apache.commons.lang.StringUtils;

import org.joda.time.DateTime;

import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An util class for Joda Date parsing. Use it if you have Joda time API in your
 * classpath.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.1
 */
public abstract class JodaDateUtil {

  protected static final Log log = LogFactory.getLog(JodaDateUtil.class);

  protected static final int MIN_YEAR = ValidationUtil.MIN_YEAR;

  protected static final int MAX_YEAR = ValidationUtil.MAX_YEAR;

  protected static ValidationUtil.ParsedDate parseJoda(String pattern, String value) {
    if (log.isTraceEnabled()) {
      log.trace("Using Joda with pattern '" + pattern + "' to parse date '" + value + "'.");
    }

    if (StringUtils.trimToEmpty(value).length() == pattern.length()) {
      DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
      DateTime date = null;
      int offset = 0;

      while (date == null) {
        try {
          date = formatter.withZone(DateTimeZone.forOffsetHours(offset)).parseDateTime(value);
        } catch (Exception e) {
          if (offset == 24) { // 24 == time zones count.
            break;
          }
          offset++;
          if (log.isTraceEnabled()) {
            log.trace("Trying to parse date with time zone offset " + offset + "...");
          }
        }
      }

      if (date.getYear() >= MIN_YEAR && date.getYear() <= MAX_YEAR) {
        // The DateTime.toDate() does not always return the exact date in a JDK
        // Date object. (Note that it is NOT a Joda Date API bug!) Therefore we
        // copy fields one by one.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, date.year().get());
        cal.set(Calendar.MONTH, date.monthOfYear().get() - 1);
        cal.set(Calendar.DAY_OF_MONTH, date.dayOfMonth().get());
        cal.set(Calendar.HOUR_OF_DAY, date.hourOfDay().get());
        cal.set(Calendar.MINUTE, date.minuteOfHour().get());
        cal.set(Calendar.SECOND, date.secondOfMinute().get());
        cal.set(Calendar.MILLISECOND, date.millisOfSecond().get());

        if (log.isTraceEnabled()) {
          String text = org.joda.time.format.DateTimeFormat.forPattern(pattern).print(date);
          log.trace("Parsed Joda date '" + text + "'; JDK Date version: '"
              + cal.getTime() + "'.");
        }

        return new ValidationUtil.ParsedDate(cal.getTime(), pattern);
      }
    }
    return null;
  }

}
