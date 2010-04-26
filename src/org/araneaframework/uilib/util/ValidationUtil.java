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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

/**
 * Provides data validation helper and date-time parsing methods.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class ValidationUtil {

  protected static final Log LOG = LogFactory.getLog(ValidationUtil.class);

  /**
   * The minimum year the date parsing functionality accepts.
   */
  protected static final int MIN_YEAR = 1;

  /**
   * The maximum year the date parsing functionality accepts.
   */
  protected static final int MAX_YEAR = 9999;

  /**
   * Tries to parse the date according to the given patterns. The patterns correspond to the usual
   * {@link SimpleDateFormat} patterns with one addition: one can combine them using "|" so that if at least one pattern
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
        result = JodaDateUtil.parseJodaJdk(tokenizer.nextToken(), dateTimeString);
      } else {
        result = parseJDK(tokenizer.nextToken(), dateTimeString);
      }
      if (result != null) {
        break;
      }
    }

    return result;
  }

  /**
   * Parses the date <code>value</code> using the given <code>pattern</code> in the standard JDK way.
   * 
   * @param pattern The date/time pattern to use when parsing.
   * @param value The value that should match the given pattern.
   * @return The {@link ParsedDate} object when parsing was successful, or <code>null</code>.
   */
  protected static ParsedDate parseJDK(String pattern, String value) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("Using JDK with pattern '" + pattern + "' to parse date '" + value + "'.");
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
          if (LOG.isTraceEnabled()) {
            LOG.trace("Parsed Java Date: '" + date + "'.");
          }
          return new ParsedDate(date, pattern);
        }
      }
    }

    return null;
  }

  /**
   * Detects whether the class-path contains a Joda API.
   * 
   * @return A Boolean that is <code>true</code> when the class-path contains a Joda API.
   */
  protected static boolean hasJodaSupport() {
    try {
      Class.forName("org.joda.time.format.DateTimeFormat");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  /**
   * The class storing parsed date information.
   */
  public static class ParsedDate {

    protected Date date;

    protected String outputPattern;

    /**
     * Constructs a new instance using the given parsed <code>date</code> and the pattern that was used to parse it.
     * 
     * @param date The parsed date.
     * @param outputPattern Informational: the pattern that was used to parse the date. 
     */
    public ParsedDate(Date date, String outputPattern) {
      this.date = date;
      this.outputPattern = outputPattern;
    }

    /**
     * Provides the parsed date.
     * 
     * @return The parsed date.
     */
    public Date getDate() {
      return this.date;
    }

    /**
     * Provides the pattern that the parsed date matched.
     * 
     * @return The pattern that the parsed date matched.
     */
    public String getOutputPattern() {
      return this.outputPattern;
    }

  }

  /**
   * Validates whether the given <code>numberString</code> contains only numbers. This check validates only not empty
   * <code>String</code>s (returns always <code>true</code> otherwise).
   * 
   * @param numberString The <code>String</code> that must contain only unicode digits.
   * @return <code>true</code>, if the string is <code>null</code>, empty (""), or contains only unicode digits.
   * @since 2.0
   */
  public static boolean isNumeric(String numberString) {
    return numberString != null && StringUtils.isNumeric(numberString);
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
