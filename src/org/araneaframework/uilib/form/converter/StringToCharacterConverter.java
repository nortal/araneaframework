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

import org.araneaframework.uilib.form.Converter;

/**
 * Converts <code>String</code> to <code>Character</code> and back. Note that it's recommended to expect only ASCII
 * characters. Others may be corrupted since they may not fit into {@link Character} data type.
 * 
 * @author Martti Tamm (martt@araneaframework.org)
 * @since 2.0
 */
public class StringToCharacterConverter extends BaseConverter<String, Character> {

  /**
   * Converts <code>String</code> to <code>Character</code>.
   */
  @Override
  public Character convertNotNull(String data) {
    return data == null || data.length() == 0 ? null : data.charAt(0);
  }

  /**
   * Converts <code>Integer</code> to <code>String</code>.
   */
  @Override
  public String reverseConvertNotNull(Character data) {
    return data.toString();
  }

  /**
   * Returns <code>new StringToIntegerConverter()</code>
   */
  @Override
  public Converter<String, Character> newConverter() {
    return new StringToCharacterConverter();
  }
}
