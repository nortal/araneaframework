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

package org.araneaframework.uilib.form.constraint;

import java.util.Calendar;
import java.util.Date;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * Constraint that fails when checked {@link java.util.Date} is not after the current date.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 */
public class AfterTodayConstraint extends BaseFieldConstraint {

  /**
   * Specifies whether the current date is included (allowed) or not.
   */
  protected boolean allowToday;

  /**
   * Whether today should be allowed as valid.
   * 
   * @param allowToday when set, all dates from today are considered valid.
   */
  public AfterTodayConstraint(boolean allowToday) {
    this.allowToday = allowToday;
  }

  @Override
  protected void validateConstraint() {
    if (getValue() == null) {
      return;
    }

    Calendar today = Calendar.getInstance();

    if (this.allowToday) {
      today.set(Calendar.HOUR_OF_DAY, 0);
      today.set(Calendar.MINUTE, 0);
      today.set(Calendar.SECOND, 0);
      today.set(Calendar.MILLISECOND, 0);
    } else {
      today.set(Calendar.HOUR_OF_DAY, 23);
      today.set(Calendar.MINUTE, 59);
      today.set(Calendar.SECOND, 59);
      today.set(Calendar.MILLISECOND, 999);
    }

    if (today.getTime().compareTo((Date) getValue()) == 1) {
      if (!this.allowToday) {
        addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.DATE_BEFORE_TODAY, t(getLabel())));
      } else {
        addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.DATE_BEFORE_TODAY_TODAY_ALLOWED,
            t(getLabel())));
      }
    }
  }
}
