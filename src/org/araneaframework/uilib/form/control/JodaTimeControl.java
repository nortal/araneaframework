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

import org.araneaframework.uilib.support.UiLibMessages;
import org.joda.time.DateTime;

/**
 * The DateControl for storing the date data in {@link DateTime} object from the  Joda Time API.
 * Basically has the same functionlity as {@link DateControl}, except the data type.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public class JodaTimeControl extends JodaBaseControl {

  public JodaTimeControl() {
    super(TimeControl.DEFAULT_FORMAT, TimeControl.DEFAULT_FORMAT);
  }

  public JodaTimeControl(String dateTimeFormat, String defaultOutputFormat) {
    super(dateTimeFormat, defaultOutputFormat);
  }

  @Override
  protected void addWrongTimeFormatError() {
    addErrorWithLabel(UiLibMessages.WRONG_TIME_FORMAT, this.dateTimeInputPattern);
  }
}
