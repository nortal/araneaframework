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

import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * A <code>Constraint</code> that constrains the input length of a {@link FormElement}.
 * <p>
 * Usually form element text controls already have this kind of constraint built-in. However, when a text control does
 * not have it built-in, this constraint can be used instead.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StringLengthInRangeConstraint extends BaseFieldConstraint<String, String> {

  private int rangeStart;

  private int rangeEnd;

  /**
   * Initializes this constraint without binding it with a field. Use {@link #setRangeStart(int)} and
   * {@link #setRangeEnd(int)} to specify valid length criteria, or this constraint will always invalidate the data.
   */
  public StringLengthInRangeConstraint() {}

  /**
   * Creates a <code>String</code> length constraint for given form element. Use {@link #setRangeStart(int)} and
   * {@link #setRangeEnd(int)} to specify valid length criteria, or this constraint will always invalidate the data.
   * 
   * @param field The form element to be constrained.
   */
  public StringLengthInRangeConstraint(FormElement<String, String> field) {
    super(field);
  }

  /**
   * Creates the constraint, and initializes the allowed length range. The minimum length must be less than the maximum
   * length allowed, or the constraint never validates.
   * <p>
   * If the minimum length is less than 1 then it is not checked.
   * 
   * @param rangeStart The minimum allowed length of the value.
   * @param rangeEnd The maximum allowed length of the value.
   */
  public StringLengthInRangeConstraint(int rangeStart, int rangeEnd) {
    setRangeStart(rangeStart);
    setRangeEnd(rangeEnd);
  }

  /**
   * Checks that the length of the field value is in constrained boundaries.
   */
  @Override
  protected void validateConstraint() {
    int length = StringUtils.length(getValue());
    if (length < this.rangeStart || length > this.rangeEnd) {
      addValidationError();
    }
  }

  private void addValidationError() {
    addError(UiLibMessages.STRING_NOT_IN_RANGE, t(getLabel()), this.rangeStart, this.rangeEnd);
  }

  /**
   * Specifies the new maximum length for the value. It must be greater than the minumum length allowed, or the
   * constraint never validates.
   * 
   * @param rangeEnd The new maximum length for the value.
   */
  public void setRangeEnd(int rangeEnd) {
    this.rangeEnd = rangeEnd;
  }

  /**
   * Specifies the new minimum length for the value. It must be less than the maximum length allowed, or the constraint
   * never validates.
   * <p>
   * If this value is less than 1 then the minimum length is not checked.
   * 
   * @param rangeStart The new minimum length for the value.
   */
  public void setRangeStart(int rangeStart) {
    this.rangeStart = rangeStart;
  }

}
