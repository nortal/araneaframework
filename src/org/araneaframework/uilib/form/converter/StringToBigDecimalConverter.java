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

package org.araneaframework.uilib.form.converter;

import java.math.BigDecimal;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * Converts <code>String</code> to <code>BigDecimal</code> and back.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class StringToBigDecimalConverter extends BaseConverter<String, BigDecimal> {

  /**
   * Converts <code>String</code> to <code>BigDecimal</code>.
   */
  @Override
  public BigDecimal convertNotNull(String data) {
    BigDecimal result = null;
    try {
      result = new BigDecimal(data);
    } catch (NumberFormatException e) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(),
          UiLibMessages.NOT_A_NUMBER,
          MessageUtil.localize(getLabel(), getEnvironment())));
    }
    return result;
  }

  /**
   * Converts <code>BigDecimal</code> to <code>String</code>.
   */
  @Override
  public String reverseConvertNotNull(BigDecimal data) {
    return data.toString();
  }

  /**
   * Returns <code>new StringToBigDecimalConverter()</code>
   */
  @Override
  public Converter<String, BigDecimal> newConverter() {
    return new StringToBigDecimalConverter();
  }
}
