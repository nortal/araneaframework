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

import java.math.BigInteger;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;

/**
 * This constraint checks that the value is between two others.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class NumberInRangeConstraint extends BaseFieldConstraint {
  private BigInteger rangeStart;
  private BigInteger rangeEnd;
  
  public NumberInRangeConstraint() {}
  public NumberInRangeConstraint(FormElement field) { super(field); }
  
  public NumberInRangeConstraint(BigInteger start, BigInteger end) {
    rangeStart = start;
    rangeEnd = end;
  }
  
  /**
   * Checks that the value is between two others.
   */
  protected void validateConstraint() {
    // XXX: should it crash like this, when field contains nothing at all??? 
    BigInteger value = new BigInteger(getValue().toString());
    
    if (rangeStart != null && rangeEnd != null && ((value.compareTo(rangeStart) == -1) || value.compareTo(rangeEnd) == 1)) {      
      addError(
          ErrorUtil.localizeAndFormat(
            UiLibMessages.NUMBER_NOT_BETWEEN, 
            new Object[] {
                t(getLabel()),
                rangeStart.toString(),
                rangeEnd.toString()
            },
            getEnvironment()));     
    }      
    else if (rangeStart != null && value.compareTo(rangeStart) == -1) {
      addError(
          ErrorUtil.localizeAndFormat(
            UiLibMessages.NUMBER_NOT_GREATER, 
            new Object[] {
                t(getLabel()),
                rangeStart.toString(),
            },
            getEnvironment()));       
    }    
    else if (rangeEnd != null && value.compareTo(rangeEnd) == 1) {      
      addError(
          ErrorUtil.localizeAndFormat(
            UiLibMessages.NUMBER_NOT_LESS, 
            new Object[] {
                t(getLabel()),
                rangeEnd.toString()
            },
            getEnvironment()));
    }      
  }
  
  /**
   * Start of range.
   * @param rangeStart Start of range.
   */
  public void setRangeStart(BigInteger rangeStart) {
    this.rangeStart = rangeStart;
  }
  
  /**
   * End of range.
   * @param rangeEnd End of range.
   */
  public void setRangeEnd(BigInteger rangeEnd) {
    this.rangeEnd = rangeEnd;
  }  
}
