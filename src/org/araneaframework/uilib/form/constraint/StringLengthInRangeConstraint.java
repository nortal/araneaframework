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
import org.araneaframework.uilib.util.ErrorUtil;

/**
 * {@link org.araneaframework.uilib.form.Constraint} that allows constraining
 * input length in a {@link FormElement}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StringLengthInRangeConstraint extends BaseFieldConstraint {
  private int rangeStart;
  private int rangeEnd;
  
  public StringLengthInRangeConstraint() {}

  public StringLengthInRangeConstraint(FormElement field) {
    super(field);
  }

  /**
   * Creates the constraint, initializing the corresponding fields.
   * @param rangeStart start of the length range.
   * @param rangeEnd end of the length range.
   */
  public StringLengthInRangeConstraint(int rangeStart, int rangeEnd) {
    setRangeStart(rangeStart);
    setRangeEnd(rangeEnd);
  }
  
  /**
   * Checks that the length of string belongs in constraint boundaries.
   */
  protected void validateConstraint() {
    String value = (String) getValue();

    if (value == null) {
      if (rangeStart == 0)
        return;
      addValidationError();
      return;
    }

    if (value != null && (value.length() < rangeStart || value.length() > rangeEnd))
      addValidationError();
  }

  private void addValidationError() {
	addError(
      ErrorUtil.localizeAndFormat(
        UiLibMessages.STRING_NOT_IN_RANGE, 
        new Object[] {
          t(getLabel()),
          Integer.toString(rangeStart),
          Integer.toString(rangeEnd)
        },
        getEnvironment()));
  }

  public void setRangeEnd(int rangeEnd) {
    this.rangeEnd = rangeEnd;
  }

  public void setRangeStart(int rangeStart) {
    this.rangeStart = rangeStart;
  }
}
