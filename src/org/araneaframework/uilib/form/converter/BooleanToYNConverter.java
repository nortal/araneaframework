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
 * Convert the <code>Boolean</code> to <code>String</code>, true - "Y", false -
 * "N" and back.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class BooleanToYNConverter extends BaseConverter<Boolean, String> {
  /**
   * Converts <code>Boolean</code> to <code>Y|N</code>.
   */
  @Override
  public String convertNotNull(Boolean data) {
    return data.booleanValue() ? "Y" : "N";
  }

  /**
	 *  Converts <code>String</code> "Y" to <code>Boolean.TRUE</code> and 
	 *  any other <code>String</code>, including "N" to <code>Boolean.FALSE</code>. 
	 */
  @Override
  public Boolean reverseConvertNotNull(String data) {
    return data.equals("Y") ? Boolean.TRUE : Boolean.FALSE;
  }

  /**
   * Returns a <code>new BooleanToYNConverter()</code>.
   */
  @Override
  public Converter<Boolean, String> newConverter() {
    return new BooleanToYNConverter();
  }
}
