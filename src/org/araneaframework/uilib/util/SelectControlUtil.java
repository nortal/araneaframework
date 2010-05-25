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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.Assert;

/**
 * 
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 */
public class SelectControlUtil {

  /**
   * Asserts whether the display item container contains unique items. If the uniqueness check fails, an exception will
   * be thrown.
   * 
   * @param <T> The type of the display container.
   * @param container The container of display items.
   */
  public static <T> void assertUnique(DisplayItemContainer<T> container) {
    List<T> items = new LinkedList<T>(container.getAllItems());

    if (CollectionUtils.isNotEmpty(items)) {
      if (container.getItemGroupProperty() != null) {
        for (T item : container.getAllItems()) {
          List<T> subItems = getChildItems(item, container.getItemChildrenProperty());

          if (subItems != null) {
            items.addAll(subItems);
          }
        }
      }

      assertUnique(items);
    }
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
      Assert.isTrue(!items.contains(item), "The *SelectControl items must have unique values - not like " + item);
    }
  }

  private static <T> void assertUnique(List<T> items) {
    if (CollectionUtils.isNotEmpty(items)) {
      Set<T> uniqueItems = new HashSet<T>(items);
      Assert.isTrue(uniqueItems.size() == items.size(), "The *SelectControl items must have unique values.");
    }
  }

  private static Object getProperty(Object bean, String property) {
    Object value = null;
    if (bean != null && StringUtils.isNotBlank(property)) {
      value = ObjectUtils.toString(BeanUtil.getPropertyValue(bean, property));
    }
    return value;
  }

  private static String getPropertyStr(Object bean, String property) {
    return ObjectUtils.toString(getProperty(bean, property), null);
  }

  /**
   * Provides the value of the given item. The given property of the item will be called to retrieve the value.
   * 
   * @param <T> The type of the bean.
   * @param item The bean containing the value.
   * @param valueProperty The property of the item that provides the value.
   * @return Either the value of the property when reading the property was successful, or <code>null</code>.
   */
  public static <T> String getItemValue(T item, String valueProperty) {
    return getPropertyStr(item, valueProperty);
  }

  /**
   * Provides the values of the given items. The given property of the items will be called to retrieve the values.
   * 
   * @param <T> The type of the bean.
   * @param items The beans containing the values.
   * @param valueProperty The property of the items that provides the value.
   * @return An array of values, which are either the values of the property when reading the properties was successful,
   *         or <code>null</code>s.
   */
  public static <T> String[] getItemsValues(List<T> items, String valueProperty) {
    String[] result = new String[items.size()];
    int i = 0;
    for (T item : items) {
      result[i++] = getPropertyStr(item, valueProperty);
    }
    return result;
  }
  
  /**
   * Resolves the child options of the given "optgroup" bean. The bean is "not required" to have the given property.
   * When the bean has the given property, and it returns an array or {@link List} of sub-beans, which is not empty and
   * contains items of type <code>T</code>. Otherwise returns <code>null</code>.
   * 
   * @param <T> The type of the beans.
   * @param bean The bean to check for child options.
   * @param childrenProperty The bean property to use for fetching child options. 
   * @return A list of child options, or <code>null</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> getChildItems(T bean, String childrenProperty) {
    if (bean == null || childrenProperty == null) {
      return null;
    }

    Class<?> type = BeanUtil.getPropertyType(bean.getClass(), childrenProperty);
    List<?> value = null;

    if (type == List.class || type.getComponentType() == bean.getClass()) {
      Object tmpValue = BeanUtil.getPropertyValue(bean, childrenProperty);

      if (tmpValue != null) {
        if (type.isArray()) {
          T[] arr = (T[]) tmpValue;
          value = Arrays.asList(arr);
        } else {
          value = (List<T>) tmpValue;
        }

        if (value.isEmpty() || !type.isInstance(value.get(0))) {
          value = null;
        }
      }
    }

    return (List<T>) value;
  }

  /**
   * Provides whether the given <code>container</code> contains the given <code>value</code> among its items.
   * 
   * @param <T> The type of container items.
   * @param container The container of items to search.
   * @param value The value to search for.
   * @return A Boolean that is <code>true</code> when the container contains the given value among its items.
   */
  public static <T> boolean containsValue(DisplayItemContainer<T> container, String value) {
    return getSelectItem(container.getAllItems(), container.getItemValueProperty(), value) != null;
  }

  /**
   * Provides whether the given <code>container</code> contains the given <code>value</code> among <i>enabled</i> items.
   * 
   * @param <T> The type of container items.
   * @param container The container of items to search.
   * @param value The value to search for.
   * @return A Boolean that is <code>true</code> when the container contains the given value among <i>enabled</i> items.
   */
  public static <T> boolean containsEnabledValue(DisplayItemContainer<T> container, String value) {
    return getSelectItem(container.getEnabledItems(), container.getItemValueProperty(), value) != null;
  }

  /**
   * Returns an item from the given <code>container</code> by matching it to the given <code>value</code>.
   * 
   * @param <T> The type of container items.
   * @param container The container of items to search.
   * @param value The value that defines which item should be returned.
   * @return An item from the <code>container</code>, or <code>null</code>.
   */
  public static <T> T getSelectItem(DisplayItemContainer<T> container, String value) {
    return getSelectItem(container.getAllItems(), container.getItemValueProperty(), value);
  }

  /**
   * Returns an enabled item from the given <code>container</code> by matching it to the given <code>value</code>.
   * 
   * @param <T> The type of container items.
   * @param container The container of items to search.
   * @param value The value that defines which item should be returned.
   * @return An enabled item from the <code>container</code>, or <code>null</code>.
   */
  public static <T> T getEnabledSelectItem(DisplayItemContainer<T> container, String value) {
    return getSelectItem(container.getEnabledItems(), container.getItemValueProperty(), value);
  }

  /**
   * Returns a subset of all items from the given <code>container</code> by matching them to the given
   * <code>values</code>. The order of returned items will be the same as in the original list.
   * 
   * @param <T> The type of container items.
   * @param container The container of items to search.
   * @param values The values that define, which items are allowed in the result list.
   * @return A subset of items from the <code>container</code>, or <code>null</code>.
   */
  public static <T> List<T> getSelectItems(DisplayItemContainer<T> container, String[] values) {
    return getSelectItems(container.getAllItems(), container.getItemValueProperty(), values);
  }

  /**
   * Returns a subset of all enabled items from the given <code>container</code> by matching them to the given
   * <code>values</code>. The order of returned items will be the same as in the original list.
   * 
   * @param <T> The type of container items.
   * @param container The container of items to search.
   * @param values The values that define, which items are allowed in the result list.
   * @return A subset of enabled items from the <code>container</code>, or <code>null</code>.
   */
  public static <T> List<T> getEnabledSelectItems(DisplayItemContainer<T> container, String[] values) {
    return getSelectItems(container.getEnabledItems(), container.getItemValueProperty(), values);
  }

  private static <T> T getSelectItem(List<T> items, String valueProperty, String value) {
    if (value != null && CollectionUtils.isNotEmpty(items)) {
      value = ObjectUtils.toString(value);

      for (T item : items) {
        String currentValue = getPropertyStr(item, valueProperty);
        if (ObjectUtils.equals(value, currentValue)) {
          return item;
        }
      }
    }
    return null;
  }

  private static <T> List<T> getSelectItems(List<T> items, String valueProperty, String[] values) {
    List<T> results = new LinkedList<T>();
    for (T item : items) {
      if (ArrayUtils.contains(values, getPropertyStr(item, valueProperty))) {
        results.add(item);
        if (results.size() == values.length) {
          break;
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
        String currentValue = getPropertyStr(item, valueProperty);
        if (StringUtils.equals(value, currentValue)) {
          return i.previousIndex();
        }
      }
    }
    return -1;
  }

  /**
   * Resolves the class of type <code>T</code>. When <code>itemClass</code> is not <code>null</code>, it will be
   * returned. Otherwise, when <code>items</code> is not empty, returns the class of its first item.
   * 
   * @param <T> The type of the class.
   * @param itemClass The class of the item, or <code>null</code>.
   * @param items A list of items, which may be empty or even <code>null</code>.
   * @return The resolved class.
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> resolveClass(Class<T> itemClass, List<T> items) {
    Class<T> result = itemClass;
    if (result == null && items != null && !items.isEmpty()) {
      for (T item : items) {
        if (item != null) {
          result = (Class<T>) item.getClass();
          break;
        }
      }
    }
    return result;
  }

}
