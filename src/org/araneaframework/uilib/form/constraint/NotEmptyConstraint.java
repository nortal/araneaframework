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
 * This constraint checks that the <code>String</code> value of given form
 * field would not be empty.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class NotEmptyConstraint extends BaseFieldConstraint {

  private static final long serialVersionUID = 1L;

  /**
   * Specifies whether to trim a <code>String</code> value before checking its
   * lenght. Default is false.
   */
  protected boolean trim = false; 

  /**
   * Creates a new constraint without binding it to a form field.
   */
  public NotEmptyConstraint() {}

  /**
   * Creates a new constraint without binding it to a form field, and specifies
   * whether to trim a <code>String</code> value when checking its lenght.
   * 
   * @param trim <code>true</code>, if the <code>String</code> value should
   *            be trimmed before checking its length.
   */
  public NotEmptyConstraint(boolean trim) {
    this.trim = trim;
  }

  /**
   * A constructor that binds given constraint to a form field.
   * 
   * @param field The form element that this constraint should be bound to.
   */
  public NotEmptyConstraint(FormElement field) {
    super(field);
  }

  /**
   * A constructor that binds given constraint to a form field, and specifies
   * whether to trim a <code>String</code> value when checking its lenght.
   * 
   * @param field The form element that this constraint should be bound to.
   * @param trim <code>true</code>, if the <code>String</code> value should
   *            be trimmed before checking its length.
   */
  public NotEmptyConstraint(FormElement field, boolean trim) {
    super(field);
    this.trim = trim;
  }

  /**
   * Checks that the <code>String</code> value would not be empty.
   */
  public void validateConstraint() {
    boolean empty = getValue() == null;

    if (!empty && trim && getValue() instanceof String) {
      String value = (String) getValue();
      empty = (value == null || value.length() == 0);
    }

    if (empty) {
      addError(MessageUtil.localizeAndFormat(UiLibMessages.ELEMENT_EMPTY,
          t(getLabel()), getEnvironment()));
    }
  }

}
