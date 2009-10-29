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

import java.math.BigInteger;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This constraint checks that the number value would be inside a given range.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class NumberInRangeConstraint extends BaseFieldConstraint {

  private BigInteger rangeStart;

  private BigInteger rangeEnd;

  /**
   * A constructor that expects constraint settings to be specified after initialization.
   */
  public NumberInRangeConstraint() {}

  /**
   * A constructor that binds given constraint to a form field. The ranges should be provided using
   * {@link #setRangeStart(BigInteger)} and {@link #setRangeEnd(BigInteger)}.
   * 
   * @param field The form element that this constraint should be bound to.
   */
  public NumberInRangeConstraint(FormElement<?, ?> field) {
    super(field);
  }

  /**
   * A constructor that initializes the range limits. The latter ones also accept <code>null</code> values.
   * <p>
   * Of course, the start value should be less than end value, or the validation would fail.
   * 
   * @param start The start value of the range.
   * @param end The end value of the range.
   */
  public NumberInRangeConstraint(BigInteger start, BigInteger end) {
    this.rangeStart = start;
    this.rangeEnd = end;
  }

  /**
   * Checks that the number value would be inside a given range.
   */
  @Override
  protected void validateConstraint() {
    if (getValue() == null) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_BETWEEN, t(getLabel()),
          this.rangeStart.toString(), this.rangeEnd.toString()));
      return;
    }

    BigInteger value = new BigInteger(getValue().toString());
    boolean lessThan = this.rangeStart != null ? value.compareTo(this.rangeStart) == -1 : false;
    boolean greaterThan = this.rangeEnd != null ? value.compareTo(this.rangeEnd) == 1 : false;

    if (lessThan && greaterThan) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_BETWEEN, t(getLabel()),
          this.rangeStart.toString(), this.rangeEnd.toString()));
    } else if (lessThan) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_GREATER, t(getLabel()),
          this.rangeStart.toString()));
    } else if (greaterThan) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NUMBER_NOT_LESS, t(getLabel()),
          this.rangeEnd.toString()));
    }
  }

  /**
   * Sets the start value of the range.
   * 
   * @param rangeStart The start value of the range.
   */
  public void setRangeStart(BigInteger rangeStart) {
    this.rangeStart = rangeStart;
  }

  /**
   * Sets the end value of the range.
   * 
   * @param rangeEnd The end value of the range.
   */
  public void setRangeEnd(BigInteger rangeEnd) {
    this.rangeEnd = rangeEnd;
  }

}
