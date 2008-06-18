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

package org.araneaframework.uilib.form.converter;

import org.araneaframework.uilib.form.Converter;

/**
 * Convert the <code>Boolean</code> to <code>Long</code>, true - 1, false -
 * 0 and back
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class BooleanToLongConverter extends BaseConverter<Boolean, Long> {

  /**
   * Converts <code>Boolean</code> to <code>Long</code>.
   */
  @Override
  public Long convertNotNull(Boolean data) {
    return data.booleanValue() ? new Long(1) : new Long(0);
  }

  /**
	 *  Converts <code>Long</code> to <code>Boolean</code>. 
	 */
  @Override
  public Boolean reverseConvertNotNull(Long data) {
    return data.longValue() == 0 ? Boolean.FALSE : Boolean.TRUE ;
  }

  /**
   * Returns a <code>new BooleanToLong10Converter()</code>.
   */
  @Override
  public Converter<Boolean, Long> newConverter() {
    return new BooleanToLongConverter();
  }
}
