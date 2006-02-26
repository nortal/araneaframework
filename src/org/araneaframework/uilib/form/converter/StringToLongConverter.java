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

import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * Converts <code>String</code> to <code>Long</code> and back.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class StringToLongConverter extends BaseConverter {

  /**
   * Converts <code>String</code> to <code>Long</code>.
   */
  public Object convertNotNull(Object data) {
    Object result = null;
    try {
      result = new Long((String) data);
    }
    catch (NumberFormatException e) {
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.NOT_A_NUMBER, 
          ErrorUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));        
    }
    return result;
  }

  /**
   * Converts <code>Long</code> to <code>String</code>.
   */
  public Object reverseConvertNotNull(Object data) {
    return data.toString();
  }
  
  /**
   * Returns <code>new StringToLongConverter()</code>.
   */
  public BaseConverter newConverter() {
    return new StringToLongConverter();
  }

}
