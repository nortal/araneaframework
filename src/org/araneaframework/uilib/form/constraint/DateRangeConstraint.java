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
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * A default range constraint for date form elements.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class DateRangeConstraint extends RangeConstraint<Timestamp, Date> {

  /**
   * Creates a new date range constraint that checks that form field values are in the right order: the
   * <code>fieldLo</code> is less than <code>fieldHi</code>. It is possible to specify through <code>allowEquals</code>
   * whether the values may be equal or not.
   * 
   * @param fieldLo The value of this field is checked to be less than the value of fieldHi (or <code>null</code>)
   * @param fieldHi The value of this field is checked to be greater than the value of fieldLo (or <code>null</code>)
   * @param allowEquals If this is <code>true</code>, the constraint will be considered satisfied when values of
   *          <code>fieldLo</code> and <code>fieldHi</code> are equal. Otherwise the constraint won't be satisfied in
   *          this case.
   */
  public DateRangeConstraint(FormElement<Timestamp, Date> fieldLo, FormElement<Timestamp, Date> fieldHi,
      boolean allowEquals) {
    super(fieldLo, fieldHi, allowEquals);
  }

  /**
   * Overrides the parent implementation to simplify the comparison.
   */
  @Override
  protected void validateConstraint() {
    Date valueLo = this.fieldLo.getValue();
    Date valueHi = this.fieldHi.getValue();

    if (valueLo == null || valueHi == null) {
      return;
    }

    int comparison = valueLo.compareTo(valueHi);

    if (!this.allowEquals && comparison == 0 || comparison > 0) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.RANGE_CHECK_FAILED, t(this.fieldLo
          .getLabel()), t(this.fieldHi.getLabel())));
    }
  }
  
}
