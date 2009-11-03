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

package org.araneaframework.uilib.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Represents the items put into {@link org.araneaframework.uilib.form.control.SelectControl}or
 * {@link org.araneaframework.uilib.form.control.MultiSelectControl}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class DisplayItemUtil implements Serializable {

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param value the value that is controlled.
   * @return whether <code>value</code> is found in the select items.
   */
  public static boolean isValueInItems(DisplayItemContainer<?> displayItemContainer, String value) {
    return isValueInItems(displayItemContainer.getAllItems(), displayItemContainer.getItemValueProperty(), value);
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param value the value that is controlled.
   * @return whether <code>value</code> is found in the select items.
   */
  public static <T> T getBean(DisplayItemContainer<T> displayItemContainer, String value) {
    for (T item : displayItemContainer.getAllItems()) {
      String currentValue = getBeanValue(item, displayItemContainer.getItemValueProperty());
      if (StringUtils.equals(value, currentValue)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Retrieves a value from given bean using the given property. Therefore, the bean must have the appropriate getter.
   * 
   * @param bean Any kind of bean with getters.
   * @param property The JavaBean property name that the bean has.
   * @return The value of the bean getter coerced into <code>String</code>
   * @since 2.0.
   */
  public static String getBeanValue(Object bean, String property) {
    String value = null;
    if (bean != null) {
      value = ObjectUtils.toString(BeanUtil.getPropertyValue(bean, property));
    }
    return value;
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param items The items that should be checked whether given <code>value</code> is one of them.
   * @param valueProperty The property of an item to retrieve its value (converted to String).
   * @param value The value that an item is expected to have.
   * @return whether <code>value</code> is found in the select items.
   */
  public static <T> boolean isValueInItems(Collection<T> items, String valueProperty, String value) {
    if (CollectionUtils.isNotEmpty(items)) {
      for (T item : items) {
        String currentValue = getBeanValue(item, valueProperty);
        if (StringUtils.equals(value, currentValue)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns display item label by the specified value.
   * 
   * @param items display items.
   * @param value display item value.
   * @return display item label by the specified value.
   */
  public static <T> DisplayItem getSelectedItemByValue(List<DisplayItem> items, String valueProperty, T value) {
    if (CollectionUtils.isNotEmpty(items)) {
      for (DisplayItem item : items) {
        String currentValue = getBeanValue(item, valueProperty);
        if (ObjectUtils.equals(item.getValue(), currentValue)) {
          return item;
        }
      }
    }
    return null;
  }

  /**
   * Returns display items for which a value exists in the given <code>values</code> array.
   * 
   * @param items The display items that represent all selectable items. A subset of these will be returned.
   * @param values The (display items) values for filtering display items.
   * @return A subset of display items that were matched.
   */
  public static List<DisplayItem> getSelectedItems(List<DisplayItem> items, String[] values) {
    List<DisplayItem> results = new LinkedList<DisplayItem>();
    if (CollectionUtils.isNotEmpty(items)) {
      for (DisplayItem item : items) {
        if (ArrayUtils.contains(values, item.getValue())) {
          results.add(item);
        }
      }
    }
    return results;
  }

  /**
   * Returns display item index by the specified value.
   * 
   * @param items display items.
   * @param value display item value.
   * @return display item index by the specified value.
   */
  public static <T> int getValueIndex(List<T> items, String valueProperty, String value) {
    if (CollectionUtils.isNotEmpty(items)) {
      for (ListIterator<T> i = items.listIterator(); i.hasNext();) {
        T item = i.next();
        String currentValue = getBeanValue(item, valueProperty);
        if (StringUtils.equals(value, currentValue)) {
          return i.previousIndex();
        }
      }
    }
    return -1;
  }

  /**
   * Checks whether the <code>item</code> (that is not yet added to <code>items</code>) would not have an other item
   * with the same value already in the <code>items</code> list.
   * <p>
   * If that constraint is violated, an assertion exception will be thrown.
   * 
   * @param items The display items of the control
   * @param item An item to be added
   */
  public static <T> void assertUnique(List<T> items, T item) {
    if (CollectionUtils.isNotEmpty(items)) {
      Assert.isTrue(!items.contains(item), "The *SelectControl items must have different values - not like " + item);
    }
  }

  /**
   * Checks whether the <code>items</code> would contain unique values. If that constraint is violated, an assertion
   * exception will be thrown.
   * 
   * @param items The display items of the control
   */
  public static <T> void assertUnique(List<T> items) {
    if (CollectionUtils.isNotEmpty(items)) {
      Set<T> uniqueItems = new HashSet<T>(items);
      Assert.isTrue(uniqueItems.size() == items.size(), "The *SelectControl items must have unique values.");
    }
  }

  public static <T> void addItemsFromBeanCollection(SelectControl<DisplayItem> select, List<T> params,
      Transformer labelTransformer, Transformer valueTransformer) {
    for (T param : params) {
      String label = ObjectUtils.toString(labelTransformer.transform(param));
      String value = ObjectUtils.toString(valueTransformer.transform(param));
      select.addItem(label, value);
    }
  }

}
