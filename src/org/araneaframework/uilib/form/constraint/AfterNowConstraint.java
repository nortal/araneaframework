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

import java.sql.Timestamp;
import java.util.Date;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * Constraint that fails when checked {@link java.util.Date} is not after the current moment.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class AfterNowConstraint extends BaseFieldConstraint<Timestamp, Date> {

  @Override
  protected void validateConstraint() {
    if (getValue() != null && new Date().after(getValue())) {
      addError(UiLibMessages.DATE_BEFORE_NOW, t(getLabel()));
    }
  }
}
