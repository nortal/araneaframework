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
import java.util.Arrays;
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
   * @since 2.0
   */
  public static boolean isValueInItems(DisplayItemContainer<?> displayItemContainer, String value) {
    return isValueInItems(displayItemContainer.getAllItems(), displayItemContainer.getItemValueProperty(), value);
  }

  public static boolean isValueInItems(List<DisplayItem> displayItems, String value) {
    boolean contains = false;
    if (displayItems != null) {
      for (DisplayItem item : displayItems) {
        contains = contains || StringUtils.equals(item.getValue(), value);
      }
    }
    return contains;
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param value the value that is controlled.
   * @return whether <code>value</code> is found in the select items.
   */
  public static <T> T getBean(DisplayItemContainer<T> displayItemContainer, String value) {
    if (!StringUtils.isBlank(value)) {
      for (T item : displayItemContainer.getAllItems()) {
        String currentValue = getBeanValue(item, displayItemContainer.getItemValueProperty());
        if (StringUtils.equals(value, currentValue)) {
          return item;
        }
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
  public static <T> List<T> getChildItemsValue(T bean, String childrenProperty) {
    if (bean == null || childrenProperty == null) {
      return null;
    }

    Class<?> type = BeanUtil.getPropertyType(bean.getClass(), childrenProperty);
    List<?> value = null;

    if (type == List.class || type.getComponentType() == bean.getClass()) {
      Object tmpValue = getBeanValue(bean, childrenProperty);

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
   * Returns a display item bean the specified value.
   * 
   * @param items The display items that represent all items to search from.
   * @param valueProperty The name of the value property of the bean <code>B</code>.
   * @param value The value to use for finding the matched display item.
   * @return Single display item bean from <code>items</code>, or <code>null</code>.
   */
  public static <B> B getSelectItem(List<B> items, String valueProperty, Object value) {
    if (value != null && CollectionUtils.isNotEmpty(items)) {
      value = ObjectUtils.toString(value);

      for (B item : items) {
        String currentValue = getBeanValue(item, valueProperty);
        if (ObjectUtils.equals(value, currentValue)) {
          return item;
        }
      }
    }
    return null;
  }

  /**
   * Returns a display item for which a value is equal to the given <code>value</code>.
   * 
   * @param items The display items that represent all items to search from.
   * @param value The value to use for finding the matched display item.
   * @return Single display item from <code>items</code>, or <code>null</code>.
   */
  public static DisplayItem getSelectItem(List<DisplayItem> items, String value) {
    List<DisplayItem> results = getSelectItems(items, new String[] { value });
    return results != null && !results.isEmpty() ? results.get(0) : null;
  }

  /**
   * Returns display items for which a value exists in the given <code>values</code> array.
   * 
   * @param items The display items that represent all selectable items. A subset of these will be returned.
   * @param values The (display items) values for filtering display items.
   * @return A subset of display items that were matched.
   */
  public static List<DisplayItem> getSelectItems(List<DisplayItem> items, String... values) {
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
   * Returns display item index (zero based) by the specified value, or <code>-1</code> when the value is not found.
   * 
   * @param items The display items.
   * @param value display The value to search for in display items.
   * @return The index of the display item with the specified value.
   */
  public static int getValueIndex(List<DisplayItem> items, String value) {
    if (CollectionUtils.isNotEmpty(items)) {
      for (ListIterator<DisplayItem> i = items.listIterator(); i.hasNext();) {
        String currentValue = i.next().getValue();
        if (StringUtils.equals(value, currentValue)) {
          return i.previousIndex();
        }
      }
    }
    return -1;
  }

  /**
   * Returns a subset of <code>items</code> that have values in the <code>values</code> array. Instead of iterating over
   * <code>values</code>, this method iterates over <code>items</code>. This way the order of subset items will be in
   * the same order as in the <code>items</code> list.
   * @param <T> The type of list items.
   * @param items The allowed items. A subset of this list will be returned.
   * @param valueProperty The bean property name of the item to retrieve its value.
   * @param values The values (e.g. from request) that define which items are allowed in the result list.
   * @return A subset of <code>items</code>.
   * @since 2.0
   */
  public static <T> List<T> getSelectedItems(List<T> items, String valueProperty, String[] values) {
    List<T> results = new LinkedList<T>();
    for (T item : items) {
      if (ArrayUtils.contains(values, DisplayItemUtil.getBeanValue(item, valueProperty))) {
        results.add(item);
        if (results.size() == values.length) {
          break;
        }
      }
    }
    return results;
  }

  public static <T> String[] getItemsValues(List<T> items, String valueProperty) {
    String[] result = new String[items.size()];
    int i = 0;
    for (T item : items) {
      result[i++] = getBeanValue(item, valueProperty);
    }
    return result;
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
          List<T> subItems = getChildItemsValue(item, container.getItemChildrenProperty());

          if (subItems != null) {
            items.addAll(subItems);
          }
        }
      }

      assertUnique(items);
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
