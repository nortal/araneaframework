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

package org.araneaframework.uilib.form.control;

import org.araneaframework.uilib.support.DataType;

import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * This class represents a select box (a.k.a. drop-down) control.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class SelectControl<T> extends BaseSelectControl<T, T> {

  /**
   * Creates a new instance of SelectControl, and also defines item label and value property names. Note that when
   * select items are defined one-by-one as value-label pairs then the class parameter is also needed to create new
   * instances of items. The property names are required.
   * 
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see SelectControl(Class, String, String)
   */
  public SelectControl(String itemLabelProperty, String itemValueProperty) {
    super(itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of SelectControl, and also defines item class and label and value property names. Note that
   * usually the class parameter is not needed. It is needed only when the select values are defined one-by-one (then
   * class is used to create new instances). The property names are required.
   * 
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see SelectControl(String, String)
   */
  public SelectControl(Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    super(itemClass, itemLabelProperty, itemValueProperty);
  }

  /**
   * Returns {@link DisplayItem} corresponding to selected element. Current value by which seleced element is determined
   * is reported by the {@link FormElement} to which this {@link SelectControl})belongs. If no {@link FormElement} is
   * associated with {@link SelectControl}, this method returns <code>null</code>.
   * 
   * @return {@link DisplayItem} corresponding to selected element.
   * @since 1.0.5
   */
  public T getSelectedItem() {
    return getFormElementCtx() == null ? null : DisplayItemUtil.getBean(this, (String) getFormElementCtx().getValue());
  }

  @Override
  protected T fromRequestParameters(String[] parameterValues) {
    String value = parameterValues != null && parameterValues.length > 0 ? parameterValues[0] : null;
    return value != null ? DisplayItemUtil.getBean(this, value) : null;
  }

  @Override
  protected String[] toResponseParameters(T controlValue) {
    return new String[] { DisplayItemUtil.getBeanValue(controlValue, this.valueProperty) };
  }

  public DataType getRawValueType() {
    return new DataType(String.class);
  }

}
