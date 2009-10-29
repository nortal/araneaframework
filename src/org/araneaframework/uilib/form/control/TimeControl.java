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

import static org.araneaframework.uilib.util.ConfigurationUtil.getCustomTimeFormat;

import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class represents a {@link org.araneaframework.uilib.form.control.TimestampControl}, that holds only time - that
 * is it's default pattern is "HH:mm".
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 */
public class TimeControl extends TimestampControl {

  /**
   * This is the default time format for this control.
   */
  public final static String DEFAULT_FORMAT = "HH:mm";

  /**
   * Creates the control initializing the pattern to {@link #DEFAULT_FORMAT}.
   */
  public TimeControl() {
    super(DEFAULT_FORMAT, DEFAULT_FORMAT);
  }

  /**
   * Creates the control initializing the time input pattern to <code>dateTimeFormat</code> and the time output pattern
   * to <code>defaultTimeOutputFormat</code>.
   * 
   * @param dateTimeFormat The custom date input pattern.
   * @param defaultTimeOutputFormat The custom date output format.
   */
  public TimeControl(String dateTimeFormat, String defaultTimeOutputFormat) {
    super(dateTimeFormat, defaultTimeOutputFormat);
    this.confOverridden = true;
  }

  public void init() throws Exception {
    super.init();
    if (!this.confOverridden) {
      this.dateTimeInputPattern = getCustomTimeFormat(getEnvironment(), true, this.dateTimeInputPattern);
      this.dateTimeOutputPattern = getCustomTimeFormat(getEnvironment(), false, this.dateTimeOutputPattern);
    }
  }

  /**
   * This method inserts the message when parsing fails. This override sends the correct message.
   * 
   * @since 1.1
   */
  protected void addWrongTimeFormatError() {
    addErrorWithLabel(UiLibMessages.WRONG_TIME_FORMAT, this.dateTimeInputPattern);
  }
}
