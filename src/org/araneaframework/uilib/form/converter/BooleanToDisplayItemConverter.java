/*
 * Copyright 2006 Webmedia Group Ltd. Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */

package org.araneaframework.uilib.form.converter;

import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Converts <code>Boolean</code> to <code>DisplayItem</code> and back.
 * 
 * @author Maksim Boiko <mailto:max@webmedia.ee>
 * @since 2.0
 */
public class BooleanToDisplayItemConverter extends BaseConverter<Boolean, DisplayItem> {

  /**
   * Converts <code>Boolean</code> to <code>DisplayItem</code>.
   */
  @Override
  public DisplayItem convertNotNull(Boolean data) {
    String stringRepresentation = data.booleanValue() ? "Y" : "N";
    return new DisplayItem(stringRepresentation, stringRepresentation);
  }

  /**
   * Converts <code>Integer</code> to <code>String</code>.
   */
  @Override
  public Boolean reverseConvertNotNull(DisplayItem data) {
    return data.getValue().equals("Y") ? Boolean.TRUE : Boolean.FALSE;
  }

  /**
   * Returns <code>new BooleanToDisplayItemConverter()</code>
   */
  @Override
  public Converter<Boolean, DisplayItem> newConverter() {
    return new BooleanToDisplayItemConverter();
  }
}
