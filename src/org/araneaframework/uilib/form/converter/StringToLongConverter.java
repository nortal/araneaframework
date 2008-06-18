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
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;


/**
 * Converts <code>String</code> to <code>Long</code> and back.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class StringToLongConverter extends BaseConverter<String, Long> {

  /**
   * Converts <code>String</code> to <code>Long</code>.
   */
  @Override
  public Long convertNotNull(String data) {
    Long result = null;
    try {
      result = new Long(data);
    }
    catch (NumberFormatException e) {
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NOT_A_NUMBER, 
          MessageUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));        
    }
    return result;
  }

  /**
   * Converts <code>Long</code> to <code>String</code>.
   */
  @Override
  public String reverseConvertNotNull(Long data) {
    return data.toString();
  }
  
  /**
   * Returns <code>new StringToLongConverter()</code>.
   */
  @Override
  public Converter<String, Long> newConverter() {
    return new StringToLongConverter();
  }

}
