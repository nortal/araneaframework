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

import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * This constraint checks that the value is between two others.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class StringLengthInRangeConstraint extends BaseFieldConstraint {

  private int rangeStart;
  private int rangeEnd;
  
  /**
   * Creates the class, initializing the corresponding fields.
   * @param rangeStart start of the length range.
   * @param rangeEnd end of the length range.
   */
  public StringLengthInRangeConstraint(int rangeStart, int rangeEnd) {
    super();
    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;
  }
  
  /**
   * Checks that the value is between two others.
   */
  protected void validateConstraint() {
    String value = (String) getValue();
    if (value != null && (value.length() < rangeStart || value.length() > rangeEnd)) {
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
  }
}
