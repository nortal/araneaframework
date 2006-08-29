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

import org.araneaframework.core.AraneaRuntimeException;

/**
 * Exception thrown by {@link org.araneaframework.uilib.form.Data} when
 * its data type is not handled correctly.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DataItemTypeViolatedException extends AraneaRuntimeException {

  /**
   * @param dataItemType {@link org.araneaframework.uilib.form.Data} type
   * @param valueType value type attempted to assign to Data
   */
  public DataItemTypeViolatedException(String dataItemType, Class valueType) {
    super("The DataItem with type '" + dataItemType + "' cannot accept values of type '" + valueType.getName());
  }
  
}
