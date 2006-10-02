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

import java.math.BigInteger;
import org.araneaframework.uilib.form.Converter;


/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class BigIntegerToLongConverter extends BaseConverter {
  /**
   * Returns a <code>new BigIntegerToLongConverter()</code>.
   */
  public Converter newConverter() {
    return new BigIntegerToLongConverter();
  }
  
  /**
   * Converts <code>BigInteger</code> to <code>Long</code>.
   */
  protected Object convertNotNull(Object data) {
    return new Long(((BigInteger) data).longValue());
  }
  
  /**
   * Converts <code>Long</code> to <code>BigInteger</code>.
   */
  protected Object reverseConvertNotNull(Object data) {
    return new BigInteger(((Long) data).toString());
  }

}
