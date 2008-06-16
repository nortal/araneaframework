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

package org.araneaframework.uilib;

import org.apache.commons.lang.ClassUtils;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * Exception thrown by {@link org.araneaframework.uilib.form.Data} when
 * its data type is not handled correctly.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DataItemTypeViolatedException extends AraneaRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an exception to describe the situation when the
   * {@link org.araneaframework.uilib.form.Data} cannot convert the given value
   * to apropriate format.
   * 
   * @param data The Data instance object.
   * @param formElementCtx The optional form element context (to display scope).
   * @param value The value that was attempted to assign to Data.
   */
  public DataItemTypeViolatedException(Data data,
      FormElementContext formElementCtx, Object value) {
    super("The " + ClassUtils.getShortClassName(data.getClass())
        + (formElementCtx != null ? " for '"
            + formElementCtx.getControl().getScope() + "'" : "")
        + " with type '" + data.getValueType()
        + "' cannot accept values of type '"
        + ClassUtils.getShortClassName(value.getClass()) + "' (value=" + value
        + ")");
  }

}
