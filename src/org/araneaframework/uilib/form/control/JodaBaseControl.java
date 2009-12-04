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

import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.util.MessageUtil;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class holds the default functionality for Joda*Controls.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public abstract class JodaBaseControl extends TimestampControl {

  public JodaBaseControl(String dateTimeFormat, String defaultOutputFormat) {
    super(dateTimeFormat, defaultOutputFormat);
    this.confOverridden = true;
  }

  /**
   * Returns the value class name ({@link DateTime}).
   * 
   * @return The value class name ({@link DateTime}).
   */
  public String getRawValueType() {
    return "DateTime";
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  protected void init() throws Exception {
    super.init();

    if (!this.confOverridden) {
      ConfigurationContext confCtx = UilibEnvironmentUtil.getConfiguration(getEnvironment());
      this.dateTimeInputPattern = getInputPattern(confCtx);
      this.dateTimeOutputPattern = getOutputPattern(confCtx);
    }
  }

  private String getConf(ConfigurationContext confCtx, String key, String value) {
    if (confCtx != null && confCtx.getEntry(key) != null) {
      value = (String) confCtx.getEntry(key);
    }
    return value;
  }

  protected String getInputPattern(ConfigurationContext confCtx) {
    return getConf(confCtx, ConfigurationContext.CUSTOM_DATE_FORMAT, this.dateTimeInputPattern);
  }

  protected String getOutputPattern(ConfigurationContext confCtx) {
    return getConf(confCtx, ConfigurationContext.DEFAULT_DATE_OUTPUT_FORMAT,
        this.dateTimeOutputPattern);
  }

  protected Object fromRequest(String parameterValue) {
    DateTime date = getInputFormatter().parseDateTime(parameterValue);

    if (date != null) {
      date = modifyValue(date);

    } else {
      addWrongTimeFormatError();

      if (parameterValue != null && getInputFilter() != null
          && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
        addError(
            MessageUtil.localizeAndFormat(
            getInputFilter().getInvalidInputMessage(),
            MessageUtil.localize(getLabel(), getEnvironment()),
            getInputFilter().getCharacterFilter(),
            getEnvironment()));
      }
    }

    return date;
  }

  protected String toResponse(Object controlValue) {
    return getOutputFormatter().print((DateTime) controlValue);
  }

  protected DateTimeFormatter getInputFormatter() {
    return DateTimeFormat.forPattern(this.dateTimeInputPattern);
  }

  protected DateTimeFormatter getOutputFormatter() {
    return DateTimeFormat.forPattern(this.dateTimeOutputPattern);
  }

  protected DateTime modifyValue(DateTime date) {
    return date;
  }
}
