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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class ValidationUtil {

  protected static final Log log = LogFactory.getLog(ValidationUtil.class);

  protected static final int MIN_YEAR = 1;

  protected static final int MAX_YEAR = 9999;

  /**
   * Tries to parse the date according to the given patterns. The patterns
   * correspond to the usual {@link SimpleDateFormat} patterns with one
   * addition: one can combine them using "|" so that if at least one pattern
   * parses the input it will be used.
   *
   * @param dateTimeString date to be parsed.
   * @param format {@link SimpleDateFormat} patterns with "|".
   * @return parsed {@link Date} or null if parsing fails.
   */
  public static ParsedDate parseDate(String dateTimeString, String format) {
    ParsedDate result = null;
    StringTokenizer tokenizer = new StringTokenizer(format, "|");
    boolean useJoda = hasJodaSupport();

    for (int i = 0; tokenizer.hasMoreTokens(); i++) {
      if (useJoda) {
        result = JodaDateUtil.parseJoda(tokenizer.nextToken(), dateTimeString);
      } else {
        result = parseJDK(tokenizer.nextToken(), dateTimeString);
      }
      if (result != null) {
        break;
      }
    }

    return result;
  }

  protected static ParsedDate parseJDK(String pattern, String value) {
    if (log.isTraceEnabled()) {
      log.trace("Using JDK with pattern '" + pattern + "' to parse date '" + value + "'.");
    }

    // Checking just year is not enough b/c some strings like "020110999".
    // "02.01.11500" still manage to pass through whereas others with same
    // pattern do not - for example "02.01.13452". Guess it is all about the
    // zeroes.
    // So, check the length. Means that dd.MM.yyyy does not interpret
    // d.M.yyyy, unless format parameter contains it.
    if (value.trim().length() == pattern.length()) {

      SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
      dateFormat.setLenient(false);

      ParsePosition pos = new ParsePosition(0);
      Date date = dateFormat.parse(value, pos);

      if (date != null && pos.getIndex() == value.length()) {
        // Introduce the y10k problem && ignore everything B.C. Needed to escape
        // SimpleDateFormats broken guesswork that can produce corrupt Dates.
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);

        if (year <= MAX_YEAR && year >= MIN_YEAR) {
          if (log.isTraceEnabled()) {
            log.trace("Parsed Java Date: '" + date + "'.");
          }
          return new ParsedDate(date, pattern);
        }
      }
    }

    return null;
  }

  protected static boolean hasJodaSupport() {
    try {
      Class.forName("org.joda.time.format.DateTimeFormat");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public static class ParsedDate {

    protected Date date;

    protected String outputPattern;

    public ParsedDate(Date date, String outputPattern) {
      this.date = date;
      this.outputPattern = outputPattern;
    }

    public Date getDate() {
      return this.date;
    }

    public String getOutputPattern() {
      return this.outputPattern;
    }

  }

  /**
   * Checks whether the string is a valid email.
   * 
   * @param emailString supposed email string.
   * @return whether the string is a valid email.
   */
  public static boolean isEmail(String emailString) {
    return GenericValidator.isEmail(emailString);
  }

}
