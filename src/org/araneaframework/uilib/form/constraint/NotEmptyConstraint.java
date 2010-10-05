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
 * This constraint checks that the <code>String</code> value of given form field would not be empty.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class NotEmptyConstraint<C, D> extends BaseFieldConstraint<C, D> {

  /**
   * Specifies whether to trim a <code>D</code> value before checking its length. Default is false.
   */
  protected boolean trim;

  /**
   * Creates a new constraint without binding it to a form field.
   */
  public NotEmptyConstraint() {}

  /**
   * Creates a new constraint without binding it to a form field, and specifies whether to trim a <code>D</code>
   * value when checking its length.
   * 
   * @param trim <code>true</code>, if the <code>D</code> value should be trimmed before checking its length.
   */
  public NotEmptyConstraint(boolean trim) {
    this.trim = trim;
  }

  /**
   * A constructor that binds given constraint to a form field.
   * 
   * @param field The form element that this constraint should be bound to.
   */
  public NotEmptyConstraint(FormElement<C, D> field) {
    super(field);
  }

  /**
   * A constructor that binds given constraint to a form field, and specifies whether to trim a <code>D</code>
   * value when checking its length.
   * 
   * @param field The form element that this constraint should be bound to.
   * @param trim <code>true</code>, if the <code>D</code> value should be trimmed before checking its length.
   */
  public NotEmptyConstraint(FormElement<C, D> field, boolean trim) {
    super(field);
    this.trim = trim;
  }

  /**
   * Checks that the <code>D</code> value would not be empty.
   */
  @Override
  public void validateConstraint() {
    D value = getValue();
    boolean empty = value == null;

    if (!empty && value instanceof String) {
      String valueStr = (String) value;
      empty = this.trim ? StringUtils.isBlank(valueStr) : StringUtils.isEmpty(valueStr);
    }

    if (empty) {
      addError(UiLibMessages.ELEMENT_EMPTY, t(getLabel()));
    }
  }

}
