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
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Represents the items put into {@link org.araneaframework.uilib.form.control.SelectControl}or
 * {@link org.araneaframework.uilib.form.control.MultiSelectControl}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class DisplayItemUtil implements Serializable {

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds these to the provided
   * <code>displayItemContainer</code>.
   * 
   * @param displayItemContainer The container for new created {@link DisplayItem}s.
   * @param beanCollection A <code>Collection</code> of beans, may not contain <code>null</code>s.
   * @param valueProperty The name of the bean field corresponding to the (submitted) value of the select item.
   * @param labelProperty The name of the bean field corresponding to the label of the select item.
   */
  @SuppressWarnings("unchecked")
  public static <T> void addItemsFromBeanCollection(DisplayItemContainer<DisplayItem> displayItemContainer,
      Collection<T> beanCollection, String valueProperty, String labelProperty) {

    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notEmptyParam(valueProperty, "valueName");
    Assert.notEmptyParam(labelProperty, "displayStringName");

    if (beanCollection.isEmpty()) {
      return;
    }

    BeanMapper<T> beanMapper = new BeanMapper<T>((Class<T>) beanCollection.iterator().next().getClass());

    Transformer valueTransformer = new BeanToPropertyValueTransformer(beanMapper, valueProperty);
    Transformer labelTransformer = new BeanToPropertyValueTransformer(beanMapper, labelProperty);

    addItemsFromBeanCollection(displayItemContainer, beanCollection, valueTransformer, labelTransformer);
  }


  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds these to the provided
   * <code>displayItemContainer</code>.
   * 
   * @param displayItemContainer The container for new created {@link DisplayItem}s.
   * @param beanCollection A <code>Collection</code> of beans, may not contain <code>null</code>s.
   * @param valueProperty The name of the bean field corresponding to the (submitted) value of the select item.
   * @param labelTransformer The transformer producing label ({@link DisplayItem#getLabel()}) value from a bean.
   * @since 1.1
   */
  @SuppressWarnings("unchecked")
  public static <T> void addItemsFromBeanCollection(DisplayItemContainer<DisplayItem> displayItemContainer,
      Collection<T> beanCollection, String valueProperty, Transformer labelTransformer) {

    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notEmptyParam(valueProperty, "valueProperty");
    Assert.notNullParam(labelTransformer, "labelTransformer");

    if (beanCollection.isEmpty()) {
      return;
    }

    BeanMapper<T> beanMapper = new BeanMapper<T>((Class<T>) beanCollection.iterator().next().getClass());
    Transformer valueTransformer = new BeanToPropertyValueTransformer(beanMapper, valueProperty);

    addItemsFromBeanCollection(displayItemContainer, beanCollection, valueTransformer, labelTransformer);
  }

  /** 
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds these to the provided
   * <code>displayItemContainer</code>.
   * 
   * @param displayItemContainer The container for new created {@link DisplayItem}s.
   * @param beanCollection A <code>Collection</code> of beans, may not contain <code>null</code>s.
   * @param valueTransformer The transformer producing value ({@link DisplayItem#getValue()}) from a bean.
   * @param labelProperty The name of the bean field corresponding to the label of the select item.
   * @since 1.1
   */
  @SuppressWarnings("unchecked")
  public static <T> void addItemsFromBeanCollection(DisplayItemContainer<DisplayItem> displayItemContainer,
      Collection<T> beanCollection, Transformer valueTransformer, String labelProperty) {

    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notNullParam(valueTransformer, "valueTransformer");
    Assert.notEmptyParam(labelProperty, "labelProperty");

    if (beanCollection.isEmpty()) {
      return;
    }

    BeanMapper<T> beanMapper = new BeanMapper<T>((Class<T>) beanCollection.iterator().next().getClass());
    Transformer labelTransformer = new BeanToPropertyValueTransformer(beanMapper, labelProperty);

    addItemsFromBeanCollection(displayItemContainer, beanCollection, valueTransformer, labelTransformer);
  }

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds these to the provided
   * <code>displayItemContainer</code>.
   *
   * @param displayItemContainer the container for created {@link DisplayItem}s
   * @param beanCollection <code>Collection</code> of beans, may not contain <code>null</code>.
   * @param valueTransformer The transformer producing value ({@link DisplayItem#getValue()}) from a bean.
   * @param labelTransformer The transformer producing label ({@link DisplayItem#getLabel()}) value from a bean.
   * @since 1.1
   */
  public static <T> void addItemsFromBeanCollection(DisplayItemContainer<DisplayItem> displayItemContainer,
      Collection<T> beanCollection, Transformer valueTransformer, Transformer labelTransformer) {

    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notNullParam(valueTransformer, "valueTransformer");
    Assert.notNullParam(labelTransformer, "labelTransformer");

    for (T item : beanCollection) {
      String value = (String) valueTransformer.transform(item);
      String label = (String) labelTransformer.transform(item);
      displayItemContainer.addItem(new DisplayItem(value, label));
    }
  }

  /**
   * Returns display items for which a value exists in the given <code>values</code> array.
   * 
   * @param items The display items that represent all selectable items. A subset of these will be returned.
   * @param values The (display items) values for filtering display items.
   * @return A subset of display items that were matched.
   */
  public static Collection<DisplayItem> getItems(Collection<DisplayItem> items, String... values) {
    Collection<DisplayItem> results = new LinkedList<DisplayItem>();
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
   * Returns a display item from the given collection by the given <code>value</code>.
   * 
   * @param items A collection of {@link DisplayItem}s.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>null</code>, too.
   * @return The found display item or <code>null</code>.
   */
  public static DisplayItem getItem(Collection<DisplayItem> items, String value) {
    DisplayItem result = null;

    if (!StringUtils.isBlank(value)) {
      for (DisplayItem item : items) {
        if (StringUtils.equals(value, item.getValue())) {
          result = item;
          break;
        }
      }
    }

    return result;
  }

  /**
   * Returns a display item from the given container by the given <code>value</code>.
   * 
   * @param container A container of {@link DisplayItem}s.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>null</code>, too.
   * @return The found display item or <code>null</code>.
   */
  public static DisplayItem getItem(DisplayItemContainer<DisplayItem> container, String value) {
    Assert.notNullParam(container, "container");
    return getItem(container.getAllItems(), value);
  }

  /**
   * Returns an enabled display item from the given container by the given <code>value</code>.
   * 
   * @param container A container of {@link DisplayItem}s.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>null</code>, too.
   * @return The found enabled display item or <code>null</code>.
   */
  public static DisplayItem getEnabledItem(DisplayItemContainer<DisplayItem> container, String value) {
    Assert.notNullParam(container, "container");
    return getItem(container.getEnabledItems(), value);
  }

  /**
   * Returns whether the <code>value</code> is found in the select items.
   * 
   * @param displayItemContainer A container of {@link DisplayItem}s.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>false</code>.
   * @return A Boolean that is <code>true</code> when the value was found.
   * @deprecated Since 2.0: use {@link #containsItem(DisplayItemContainer, String)} instead.
   */
  @Deprecated
  public static boolean isValueInItems(DisplayItemContainer<DisplayItem> displayItemContainer, String value) {
    return containsItem(displayItemContainer, value);
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param displayItems A collection of display items to use for searching.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>false</code>.
   * @return A Boolean that is <code>true</code> when the value was found.
   * @deprecated Since 2.0: use {@link #containsItem(Collection, String) instead.
   */
  @Deprecated
  public static boolean isValueInItems(Collection<DisplayItem> displayItems, String value) {
    return getItem(displayItems, value) != null;
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param container A container of {@link DisplayItem}s.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>false</code>.
   * @return The found display item or <code>null</code>.
   * @return A Boolean that is <code>true</code> when the value was found.
   */
  public static boolean containsItem(DisplayItemContainer<DisplayItem> container, String value) {
    return getItem(container, value) != null;
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param displayItems A collection of display items to use for searching.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>false</code>.
   * @return A Boolean that is <code>true</code> when the value was found.
   */
  public static boolean containsItem(Collection<DisplayItem> displayItems, String value) {
    return getItem(displayItems, value) != null;
  }

  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param container A container of {@link DisplayItem}s.
   * @param value The value that will be searched for. If <code>null</code>, the result will be <code>false</code>.
   * @return The found display item or <code>null</code>.
   * @return A Boolean that is <code>true</code> when the value was found.
   */
  public static boolean containsEnabledItem(DisplayItemContainer<DisplayItem> container, String value) {
    return getEnabledItem(container, value) != null;
  }

  /**
   * Returns display item label by the specified value.
   * 
   * @param displayItems A collection of display items to use for searching.
   * @param value The display item value to search for.
   * @return The display item label by the specified value, or an empty string.
   */
  public static String getLabelForValue(Collection<DisplayItem> displayItems, String value) {
    Assert.noNullElementsParam(displayItems, "displayItems");

    for (DisplayItem item : displayItems) {
      if (StringUtils.equals(value, item.getValue())) {
        return item.getLabel();
      }
    }

    return "";
  }

  /**
   * Returns display item index (zero based) by the specified value, or <code>-1</code> when the value is not found.
   * 
   * @param items The display items.
   * @param value display The value to search for in display items.
   * @return The index of the display item with the specified value.
   */
  public static int getValueIndex(Collection<DisplayItem> items, String value) {
    if (CollectionUtils.isNotEmpty(items)) {
      int index = -1;

      for (DisplayItem item : items) {
        index++;
        if (StringUtils.equals(value, item.getValue())) {
          break;
        }
      }
    }
    return -1;
  }

  public static String[] getItemsValues(Collection<DisplayItem> items) {
    String[] result = new String[items.size()];
    int i = 0;
    for (DisplayItem item : items) {
      result[i++] = item.getValue();
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
   * @param optionalItem An item to be added
   */
  private static <T> void assertUnique(Collection<T> items, T optionalItem) {
    if (CollectionUtils.isNotEmpty(items)) {
      Set<T> uniqueItems = new HashSet<T>(items);
      int inc = 0;

      Assert.isTrue(uniqueItems.size() == items.size(), "The DisplayItemContainer items are not unique!");

      if (optionalItem != null) {
        uniqueItems.add(optionalItem);
        inc++;
      }

      Assert.isTrue(uniqueItems.size() == items.size() + inc, "The item '" + optionalItem
          + "' is already contained by DisplayItemContainer.");
    }
  }

  /**
   * Asserts whether the display item container contains unique items. If the uniqueness check fails, an exception will
   * be thrown.
   * 
   * @param <T> The type of the display container.
   * @param container The container of display items.
   */
  public static void assertUnique(DisplayItemContainer<DisplayItem> container, DisplayItem optionalItem) {
    List<DisplayItem> items = new LinkedList<DisplayItem>(container.getAllItems());

    if (CollectionUtils.isNotEmpty(items)) {
      for (DisplayItem item : items) {
        if (item.isGroup() && CollectionUtils.isNotEmpty(item.getChildOptions())) {
          items.addAll(item.getChildOptions());
        }
      }

      assertUnique(items, optionalItem);
    }
  }

  /**
   * A tranformer that converts converts given object into a String value using the given property of the bean.
   */
  private static class BeanToPropertyValueTransformer implements Transformer, Serializable {

    private final BeanMapper<?> bm;

    private final String propertyName;

    public BeanToPropertyValueTransformer(BeanMapper<?> beanMapper, String propertyName) {
      Assert.notNullParam(this, beanMapper, "beanMapper");
      Assert.notNullParam(this, propertyName, "propertyName");

      this.bm = beanMapper;
      this.propertyName = propertyName;
    }

    public Object transform(Object bean) {
      return ObjectUtils.toString(this.bm.getProperty(bean, this.propertyName), null);
    }
  }
}
