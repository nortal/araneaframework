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

import org.apache.commons.beanutils.ConstructorUtils;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * Converts <code>String</code> to <code>BigInteger</code> and back.
 * 
 * @author Martti Tamm (martt@araneaframework.org)
 * @since 2.0
 */
public class StringToNumberConverter<T extends Number> extends BaseConverter<String, T> {

  private Class<T> numberClass;

  public StringToNumberConverter(Class<T> numberClass) {
    this.numberClass = numberClass;
  }

  /**
   * Converts <code>String</code> to <code>Integer</code>.
   */
  @Override
  @SuppressWarnings("unchecked")
  public T convertNotNull(String data) {
    T result = null;
    try {
      result = (T) ConstructorUtils.invokeExactConstructor(this.numberClass, data);
    } catch (Exception e) {
      addErrorWithLabel(UiLibMessages.NOT_A_NUMBER);
    }
    return result;
  }

  /**
   * Converts <code>Integer</code> to <code>String</code>.
   */
  @Override
  public String reverseConvertNotNull(T data) {
    return data.toString();
  }

  /**
   * Returns <code>new StringToIntegerConverter()</code>
   */
  @Override
  public Converter<String, T> newConverter() {
    return new StringToNumberConverter<T>(this.numberClass);
  }
}
