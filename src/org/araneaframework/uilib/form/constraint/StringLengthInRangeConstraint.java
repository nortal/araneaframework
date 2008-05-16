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

package org.araneaframework.uilib.form.constraint;

import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * A <code>Constraint</code> that constrains the input length of a
 * {@link FormElement}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StringLengthInRangeConstraint extends BaseFieldConstraint {

  private static final long serialVersionUID = 1L;

  private int rangeStart;

  private int rangeEnd;

  /**
   * Initializes this constraint without binding it with a field. Use
   * {@link #setRangeStart(int)} and {@link #setRangeEnd(int)} to specify valid
   * length criteria, or this constraint will always invalidate the data.
   */
  public StringLengthInRangeConstraint() {}

  /**
   * Creates a <code>String</code> length constraint for given form element.
   * Use {@link #setRangeStart(int)} and {@link #setRangeEnd(int)} to specify
   * valid length criteria, or this constraint will always invalidate the data.
   * 
   * @param field The form element to be constrained.
   */
  public StringLengthInRangeConstraint(FormElement field) {
    super(field);
  }

  /**
   * Creates the constraint, and initializes the allowed length range. The
   * minumum length must be less than the maximum length allowed, or the
   * constraint never validates.
   * <p>
   * If the minimum length is less than 1 then it is not checked.
   * 
   * @param rangeStart The minumum allowed length of the value.
   * @param rangeEnd The maximum allowed length of the value.
   */
  public StringLengthInRangeConstraint(int rangeStart, int rangeEnd) {
    setRangeStart(rangeStart);
    setRangeEnd(rangeEnd);
  }

  /**
   * Checks that the length of the field value is in constrained boundaries.
   */
  protected void validateConstraint() {
    String value = (String) getValue();

    if (value == null) {
      if (rangeStart > 0) {
        addValidationError();
      }
    } else if (value.length() < rangeStart || value.length() > rangeEnd) {
      addValidationError();
    }
  }

  private void addValidationError() {
	addError(
      MessageUtil.localizeAndFormat(
        UiLibMessages.STRING_NOT_IN_RANGE, 
        new Object[] {
          t(getLabel()),
          Integer.toString(rangeStart),
          Integer.toString(rangeEnd)
        },
        getEnvironment()));
  }

  /**
   * Specifies the new maximum length for the value. It must be greater than the
   * minumum length allowed, or the constraint never validates.
   * 
   * @param rangeEnd The new maximum length for the value.
   */
  public void setRangeEnd(int rangeEnd) {
    this.rangeEnd = rangeEnd;
  }

  /**
   * Specifies the new minumum length for the value. It must be less than the
   * maximum length allowed, or the constraint never validates.
   * <p>
   * If this value is less than 1 then the minimum length is not checked.
   * 
   * @param rangeStart The new minumum length for the value.
   */
  public void setRangeStart(int rangeStart) {
    this.rangeStart = rangeStart;
  }

}
