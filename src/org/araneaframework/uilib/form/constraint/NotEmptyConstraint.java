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
 * This constraint checks that the <code>String</code> is not empty.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class NotEmptyConstraint extends BaseFieldConstraint {
  public NotEmptyConstraint() {
  }
	
  public NotEmptyConstraint(FormElement field) {
    super(field);
  }

  /**
	 * Checks that the <code>String</code> is not empty.
	 */
  public void validateConstraint() {
    if (!isRead()) {
    	addError(
    	    MessageUtil.localizeAndFormat(
      			UiLibMessages.ELEMENT_EMPTY, 
            t(getLabel()),
            getEnvironment()));
    }
  }
}
