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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.support.BeanDisplayItem;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemContainer;
import org.araneaframework.uilib.util.DisplayItemUtil;
import org.araneaframework.uilib.util.SelectControlUtil;

/**
 * This class represents the base functionality of select controls. The generic <code>T</code> is the type of select
 * control item. The generic <code>C</code> is the internal value type used with {@link StringArrayRequestControl}.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @version 2.0
 */
public abstract class BaseSelectControl<T, C> extends StringArrayRequestControl<C> implements DisplayItemContainer<T> {

  /**
   * The class of select items. Used only when values are defined one-by-one.
   * 
   * @see #addItem(String, String)
   */
  protected Class<T> itemClass;

  /**
   * An item property of type String that provides the value for select items.
   */
  protected String valueProperty;

  /**
   * An item property of type String that provides the label for select items.
   */
  protected String labelProperty;

  /**
   * An item property of type Boolean that provides whether the item is group and has child-options.
   */
  protected String groupProperty;

  /**
   * An item property of type Collection that provides the child-options when the item is also a group.
   */
  protected String childrenProperty;

  /**
   * The items contained in this select control.
   */
  protected List<T> items = new LinkedList<T>();

  /**
   * A subset of items that contains disabled items.
   */
  protected List<T> disabledItems = new LinkedList<T>();

  /**
   * A Boolean indicating whether the values must be unique. Default is <code>true</code>.
   */
  protected boolean checkValuesUnique = true;

  /**
   * Creates a new instance of *SelectControl, and also defines item class and label and value property names. Note that
   * usually the class parameter is not needed. It is needed only when the select values are defined one-by-one with
   * {@link #addItem(String, String)} method (then class is used to create new instances). The label and value property
   * names are required.
   * <p>
   * Optional properties for making use of <code>&lt;optgroup&gt;</code>s are <code>itemIsGroupProperty</code> and
   * <code>groupChildrenProperty</code>. The former property, when specified, is checked on every display item, and when
   * the property is <code>true</code>, the display item is considered to represent an <code>&lt;optgroup&gt;</code>
   * (its label property is used for fetching the name of the group; its value property will be ignored). For every
   * <code>&lt;optgroup&gt;</code> item, the <code>groupChildrenProperty</code> will be checked to retrieve items of
   * type <code>T</code> in an array or {@link List} to fetch the <code>&lt;option&gt;</code>s for the group.
   * <p>
   * <b>NB!</b> Specifying <code>itemIsGroupProperty</code> and <code>groupChildrenProperty</code> to a select control
   * can only be done through this constructor!
   * 
   * @param items Predefined select items. May be <code>null</code>.
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The {@link String} property of select item to retrieve the label of select item
   *          (required).
   * @param itemValueProperty The {@link String} property of select item to retrieve the value of select item
   *          (required).
   * @param itemIsGroupProperty The {@link Boolean} property of select item to retrieve the condition value that this
   *          select item is a non-selectable &lt;optgroup&gt; containing other child <code>&lt;option&gt;</code>s
   *          (optional, but mandatory when <code>groupChildrenProperty</code> is provided).
   * @param groupChildrenProperty The array/{@link List}&lt;T&gt; property of a group item to retrieve the child
   *          <code>&lt;option&gt;</code>s to render in the <code>&lt;optgroup&gt;</code> (optional, but mandatory when
   *          <code>itemIsGroupProperty</code> is provided).
   * @see BaseSelectControl#BaseSelectControl(List, String, String, String, String)
   * @see BaseSelectControl#addItem(String, String)
   */
  public BaseSelectControl(List<T> items, Class<T> itemClass, String itemLabelProperty, String itemValueProperty,
      String itemIsGroupProperty, String groupChildrenProperty) {
    Assert.notEmptyParam(this, itemLabelProperty, "itemLabelProperty");
    Assert.notEmptyParam(this, itemValueProperty, "itemValueProperty");
    Assert.isTrue(StringUtils.isEmpty(itemIsGroupProperty) == StringUtils.isEmpty(groupChildrenProperty),
        "Both group and group children properties are required when either of them is provided.");

    this.itemClass = SelectControlUtil.resolveClass(itemClass, items);
    this.labelProperty = itemLabelProperty;
    this.valueProperty = itemValueProperty;
    this.groupProperty = itemIsGroupProperty;
    this.childrenProperty = groupChildrenProperty;

    if (items != null) {
      addItems(items);
    }
  }

  public void addItem(T item) {
    Assert.notNullParam(item, "item");

    if (this.checkValuesUnique) {
      SelectControlUtil.assertUnique(this.items, item);
    }

    this.items.add(item);
  }

  /**
   * A convenient method for cases when select items are defined one-by-one as key-value pairs. An instance of the class
   * (that was given to the constructor) is created, and, using label and value properties, the given label and value
   * are set.
   * 
   * @param label The label for the new item.
   * @param value The value for the new item.
   * @see #addItem(Class, String, String)
   */
  public void addItem(String label, String value) {
    addItem(this.itemClass, label, value);
  }

  /**
   * A convenient method for cases when select items are defined one-by-one. An instance of given class is created, and,
   * using label and value properties, the given label and value are set.
   * 
   * @param itemClass The class that matches the items type of given select control.
   * @param label The label for the new item.
   * @param value The value for the new item.
   * @see #addItem(String, String)
   */
  public void addItem(Class<T> itemClass, String label, String value) {
    Assert.notNullParam(itemClass, "clazz");
    Assert.notNullParam(label, "label");
    try {
      T item = itemClass.newInstance();
      BeanUtil.setPropertyValue(item, this.labelProperty, label);
      BeanUtil.setPropertyValue(item, this.valueProperty, value);
      addItem(item);
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  public void addItems(Collection<T> items) {
    Assert.noNullElementsParam(items, "items");
    this.items.addAll(items);

    if (this.checkValuesUnique) {
      SelectControlUtil.assertUnique(this);
    }
  }

  /**
   * Disables the given select item, which should be among the defined select items. If the given item is not found then
   * it is ignored.
   * 
   * @param item The select item to disable.
   */
  public void disableItem(T item) {
    Assert.notNullParam(this, item, "item");
    disableItems(Collections.singletonList(item));
  }

  /**
   * Disables the given select items, which should be among the defined select items. If any of the given items is not
   * found then it will be ignored.
   * 
   * @param items The select items to disable.
   */
  public void disableItems(Collection<T> items) {
    Assert.notNullParam(this, items, "items");
    this.disabledItems.addAll(items);
  }

  /**
   * Enables the given select item, which should be among the disbaled select items. If the given item is not found then
   * it will be ignored.
   * 
   * @param item The select item to enable.
   */
  public void enableItem(T item) {
    Assert.notNullParam(this, item, "item");
    enableItems(Collections.singletonList(item));
  }

  /**
   * Enables the given select items, which should be among the disabled select items. If any of the given items is not
   * found then it will be ignored.
   * 
   * @param items The select items to disable.
   */
  public void enableItems(Collection<T> items) {
    Assert.notNullParam(this, items, "items");
    this.disabledItems.removeAll(items);
  }

  public void clearItems() {
    this.items.clear();
    this.disabledItems.clear();
  }

  public List<T> getAllItems() {
    return Collections.unmodifiableList(this.items);
  }

  public List<T> getEnabledItems() {
    List<T> enabledItems = new LinkedList<T>(this.items);
    enabledItems.removeAll(this.disabledItems);
    return Collections.unmodifiableList(enabledItems);
  }

  public List<T> getDisabledItems() {
    return Collections.unmodifiableList(this.disabledItems);
  }

  @Deprecated
  public int getValueIndex(String value) {
    return SelectControlUtil.getValueIndex(this.items, this.valueProperty, value);
  }

  public Class<T> getItemType() {
    return this.itemClass;
  }

  public String getItemLabelProperty() {
    return this.labelProperty;
  }

  public String getItemValueProperty() {
    return this.valueProperty;
  }

  public String getItemGroupProperty() {
    return this.groupProperty;
  }

  public String getItemChildrenProperty() {
    return this.childrenProperty;
  }

  /**
   * Provides a way to sort the items in this <code>MultiSelectControl</code>. The <code>comparator</code> parameter is
   * used to compare select items and, therefore, to set the order.
   * 
   * @param comparator Any <code>Comparator</code> that is used to define order of display items.
   * @since 1.2
   */
  public void sort(Comparator<T> comparator) {
    Collections.sort(this.items, comparator);
  }

  /**
   * Provides whether this <code>SelectControl</code> checks whether the values are unique when adding a new value to
   * the current values. By default, values uniqueness check is turned on.
   * 
   * @return A <code>Boolean</code> that is <code>true</code> when values uniqueness check is turned on.
   * @since 2.0
   */
  public boolean isCheckValuesUnique() {
    return this.checkValuesUnique;
  }

  /**
   * Sets whether this <code>SelectControl</code> must check whether the values are unique when adding a new value to
   * the current values. By default, values uniqueness check is turned on.
   * <p>
   * Setting this property to <code>true</code> when this <code>SelectControl</code> already has values will result in a
   * values uniqueness check.
   * 
   * @param checkValuesUnique A <code>Boolean</code> that is <code>true</code> when values uniqueness check must be
   *          turned on.
   * @since 2.0
   */
  public void setCheckValuesUnique(boolean checkValuesUnique) {
    this.checkValuesUnique = checkValuesUnique;
    if (this.checkValuesUnique && !this.items.isEmpty()) {
      SelectControlUtil.assertUnique(this);
    }
  }

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }  

  @Override
  protected String[] preprocessRequestParameters(String[] parameterValues) {
    parameterValues = parameterValues == null ? new String[0] : parameterValues;
    Set<String> currentValues = new HashSet<String>(Arrays.asList(parameterValues));

    // Remove submitted empty values and checks that values are allowed:
    for (Iterator<String> i = currentValues.iterator(); i.hasNext(); ) {
      String value = i.next();
      if (StringUtils.isEmpty(value)) {
        i.remove();
      } else if (!SelectControlUtil.containsEnabledValue(this, value)) {
        throw new SecurityException("The submitted value '" + value + "' is not allowed!");
      }
    }

    if (this.innerData != null) {
      // Handles disabled items
      Set<String> previousDisabledValues = new HashSet<String>(Arrays.asList((String[]) this.innerData));
      Set<String> disabledItemValues = new HashSet<String>();

      for (T item : this.disabledItems) {
        disabledItemValues.add(SelectControlUtil.getItemValue(item, this.valueProperty));
      }

      previousDisabledValues.retainAll(disabledItemValues);
      currentValues.addAll(previousDisabledValues);
    }

    return currentValues.toArray(new String[currentValues.size()]);
  }

  /**
   * Represents a select control view model for the rendering layer.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   * @author Martti Tamm (martt <i>at</i> araneaframework <i>dot</i> org)
   */
  public class ViewModel extends StringArrayRequestControl<C>.ViewModel {

    private List<DisplayItem> selectItems = new LinkedList<DisplayItem>();

    private List<DisplayItem> enabledItems = new LinkedList<DisplayItem>();

    private List<DisplayItem> disabledItems = new LinkedList<DisplayItem>();

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      for (T item : BaseSelectControl.this.items) {
        boolean disabled = BaseSelectControl.this.disabledItems.contains(item);

        BeanDisplayItem<T> option = new BeanDisplayItem<T>(item,
            BaseSelectControl.this.labelProperty,
            BaseSelectControl.this.valueProperty,
            BaseSelectControl.this.groupProperty,
            BaseSelectControl.this.childrenProperty,
            disabled);

        this.selectItems.add(option);
        if (disabled) {
          this.disabledItems.add(option);
        } else {
          this.enabledItems.add(option);
        }
      }
    }

    /**
     * Returns a <code>List</code> of {@link DisplayItem}s.
     * 
     * @return a <code>List</code> of {@link DisplayItem}s.
     */
    public List<DisplayItem> getSelectItems() {
      return this.selectItems;
    }

    /**
     * Returns a <code>List</code> of enabled {@link DisplayItem}s.
     * 
     * @return a <code>List</code> of enabled {@link DisplayItem}s.
     */
    public List<DisplayItem> getEnabledItems() {
      return this.enabledItems;
    }

    /**
     * Returns a <code>List</code> of disabled {@link DisplayItem}s.
     * 
     * @return a <code>List</code> of disabled {@link DisplayItem}s.
     */
    public List<DisplayItem> getDisabledItems() {
      return this.disabledItems;
    }

    public DisplayItem getSelectedItem() {
      String value = super.getSimpleValue();
      return DisplayItemUtil.getItem(this.selectItems, value);
    }

    public List<DisplayItem> getSelectedItems() {
      return new ArrayList<DisplayItem>(DisplayItemUtil.getItems(this.selectItems, (String[]) innerData));
    }

    public DisplayItem getSelectItem(String value) {
      return DisplayItemUtil.getItem(this.selectItems, value);
    }

    public boolean isSelected(String value) {
      return innerData != null && value != null && ArrayUtils.contains((String[]) innerData, value);
    }

    @Override
    public String getSimpleValue() {
      DisplayItem selectedItem = getSelectedItem();
      return selectedItem != null ? selectedItem.getValue() : null;
    }
  }
}
