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

import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.control.BaseSelectControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * Converts <code>String</code> to <code>Character</code> and back. Note that it's recommended to expect only ASCII
 * characters. Others may be corrupted since they may not fit into {@link Character} data type.
 * 
 * @author Martti Tamm (martt@araneaframework.org)
 * @since 2.0
 */
public class StringToDisplayItemConverter extends BaseConverter<Object, Object> {

  /**
   * Converts <code>String</code> to <code>Character</code>.
   */
  @Override
  public Object convertNotNull(Object data) {
    return convertInternal(data);
  }

  /**
   * Converts <code>Integer</code> to <code>String</code>.
   */
  @Override
  public Object reverseConvertNotNull(Object data) {
    return convertInternal(data);
  }

  // This is a bit hack, but makes providing data to SelectControl more convenient.
  // The data may be given as a DisplayItem or as a String.
  @SuppressWarnings("unchecked")
  protected Object convertInternal(Object data) {
    if (data instanceof DisplayItem) {
      return ((DisplayItem) data).getValue();
    } else {
      BaseSelectControl<DisplayItem, ?> control = (BaseSelectControl<DisplayItem, ?>) getFormElementCtx().getControl();
      return DisplayItemUtil.getSelectedItemByValue(control.getEnabledItems(), ObjectUtils.toString(data));
    }
  }

  /**
   * Returns <code>new StringToIntegerConverter()</code>
   */
  @Override
  public Converter<Object, Object> newConverter() {
    return new StringToDisplayItemConverter();
  }
}
