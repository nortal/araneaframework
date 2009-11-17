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

import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.util.DisplayItemUtil;


/**
 * This class represents a multi-select control (a.k.a. multi-choice list).
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class MultiSelectControl<T> extends BaseSelectControl<T, List<T>> {

  /**
   * Creates a new instance of MultiSelectControl, and also defines item label and value property names. Note that when
   * select items are defined one-by-one as value-label pairs then the class parameter is also needed to create new
   * instances of items. The property names are required.
   * 
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see SelectControl#SelectControl(Class, String, String)
   */
  public MultiSelectControl(String itemLabelProperty, String itemValueProperty) {
    super(itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines item class and label and value property names. Note
   * that usually the class parameter is not needed. It is needed only when the select values are defined one-by-one
   * (then class is used to create new instances). The property names are required.
   * 
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see SelectControl#SelectControl(String, String)
   */
  public MultiSelectControl(Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    super(itemClass, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines item class and label and value property names. Note
   * that usually the class parameter is not needed. It is needed only when the select values are defined one-by-one
   * (then class is used to create new instances). The property names are required.
   * 
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see SelectControl#SelectControl(String, String)
   */
  public MultiSelectControl(List<T> items, String itemLabelProperty, String itemValueProperty) {
    super(items, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines item class and label and value property names. Note
   * that usually the class parameter is not needed. It is needed only when the select values are defined one-by-one
   * (then class is used to create new instances). The property names are required.
   * 
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see SelectControl#SelectControl(String, String)
   */
  public MultiSelectControl(List<T> items, Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    super(items, itemClass, itemLabelProperty, itemValueProperty);
  }

  @Override
  protected List<T> fromRequestParameters(String[] parameterValues) {
    List<T> items = new LinkedList<T>();
    for (String value : parameterValues) {
      items.add(DisplayItemUtil.getBean(this, value));
    }
    return items;
  }

  @Override
  protected String[] toResponseParameters(List<T> controlValues) {
    List<String> values = new LinkedList<String>();
    for (T value : controlValues) {
      values.add(DisplayItemUtil.getBeanValue(value, this.valueProperty));
    }
    return (String[]) values.toArray();
  }

  public DataType getRawValueType() {
    this.itemClass = DisplayItemUtil.resolveClass(this.itemClass, this.items);
    Assert.notNull(this.itemClass != null,
        "Cannot resolve data type because select item class nor select items provided!");
    return new DataType(List.class, this.itemClass);
  }
}
