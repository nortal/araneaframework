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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.support.BeanDisplayItem;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.DisplayItemContainer;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * This class represents the base functionality of select controls. The generic T is the type of select control item.
 * The generic C is the internal value type used with {@link StringArrayRequestControl}.
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
  protected boolean valuesUnique = true;

  /**
   * Creates a new instance of *SelectControl, and also defines item label and value property names. Note that when
   * select items are defined one-by-one as value-label pairs then the class parameter is also needed to create new
   * instances of items. The property names are required.
   * 
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see BaseSelectControl#BaseSelectControl(Class, String, String)
   */
  public BaseSelectControl(String itemLabelProperty, String itemValueProperty) {
    this(null, null, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of *SelectControl, and also defines item label and value property names. Note that when
   * select items are defined one-by-one as value-label pairs then the class parameter is also needed to create new
   * instances of items. The property names are required.
   * 
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see BaseSelectControl#BaseSelectControl(Class, String, String)
   */
  public BaseSelectControl(List<T> items, String itemLabelProperty, String itemValueProperty) {
    this(items, null, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of *SelectControl, and also defines item class and label and value property names. Note that
   * usually the class parameter is not needed. It is needed only when the select values are defined one-by-one (then
   * class is used to create new instances). The property names are required.
   * 
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see BaseSelectControl#BaseSelectControl(String, String)
   */
  public BaseSelectControl(Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    this(null, itemClass, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of *SelectControl, and also defines item class and label and value property names. Note that
   * usually the class parameter is not needed. It is needed only when the select values are defined one-by-one (then
   * class is used to create new instances). The property names are required.
   * 
   * @param items Predefined select items. May be <code>null</code>.
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see BaseSelectControl#BaseSelectControl(String, String)
   */
  public BaseSelectControl(List<T> items, Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    Assert.notNullParam(this, itemLabelProperty, "itemLabelProperty");
    Assert.notNullParam(this, itemValueProperty, "itemValueProperty");

    this.items = items == null ? this.items : items;
    this.itemClass = DisplayItemUtil.resolveClass(itemClass, items);
    this.labelProperty = itemLabelProperty;
    this.valueProperty = itemValueProperty;
  }

  /**
   * Adds a new item to select choices.
   * 
   * @param item The item to be added.
   */
  public void addItem(T item) {
    Assert.notNullParam(item, "item");
    DisplayItemUtil.assertUnique(this.items, item);
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
      PropertyUtils.setProperty(item, this.labelProperty, label);
      PropertyUtils.setProperty(item, this.valueProperty, value);
      addItem(item);
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Adds a display-items to the element.
   * 
   * @param items the items to be added.
   */
  public void addItems(Collection<T> items) {
    Assert.noNullElementsParam(items, "items");
    this.items.addAll(items);
    //DisplayItemUtil.assertUnique(this.items);
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
   * Enables the given select item, which should be among the defined select items. If the given item is not found then
   * it will be ignored.
   * 
   * @param item The select item to enable.
   */
  public void enableItem(T item) {
    Assert.notNullParam(this, item, "item");
    enableItems(Collections.singletonList(item));
  }

  /**
   * Enables the given select items, which should be among the defined select items. If any of the given items is not
   * found then it will be ignored.
   * 
   * @param items The select items to disable.
   */
  public void enableItems(Collection<T> items) {
    Assert.notNullParam(this, items, "items");
    this.disabledItems.removeAll(items);
  }

  /**
   * Clears the list of select-items.
   */
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

  public int getValueIndex(String value) {
    return DisplayItemUtil.getValueIndex(this.items, this.valueProperty, value);
  }

  public String getItemLabelProperty() {
    return this.labelProperty;
  }

  public String getItemValueProperty() {
    return this.valueProperty;
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
      } else if (!DisplayItemUtil.isValueInItems(BaseSelectControl.this, value)) {
        throw new SecurityException("The submitted value '" + value + "' was not found among the select items!");
      }
    }

    if (this.innerData != null) {
      // Handles disabled items
      Set<String> previousDisabledValues = new HashSet<String>(Arrays.asList((String[]) this.innerData));
      Set<String> disabledItemValues = new HashSet<String>();

      for (T item : this.disabledItems) {
        disabledItemValues.add(DisplayItemUtil.getBeanValue(item, this.valueProperty));
      }

      previousDisabledValues.retainAll(disabledItemValues);
      currentValues.addAll(previousDisabledValues);
    }

    return currentValues.toArray(new String[currentValues.size()]);
  }

  @Override
  protected void validateNotNull() {
    String[] data = this.innerData == null ? null : ((String[]) this.innerData);

    if (isMandatory() && (data == null || data.length == 0)) {
      addErrorWithLabel(UiLibMessages.MANDATORY_FIELD);
    }
  }

  /**
   * Represents a select control view model for the rendering layer.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   * @author Martti Tamm (martt <i>at</i> araneaframework <i>dot</i> org)
   */
  public class ViewModel extends StringArrayRequestControl<C>.ViewModel {

    protected List<DisplayItem> selectItems = new LinkedList<DisplayItem>();

    protected List<DisplayItem> enabledItems = new LinkedList<DisplayItem>();

    protected List<DisplayItem> disabledItems = new LinkedList<DisplayItem>();

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      initItems();
    }

    protected void initItems() {
      for (T item : BaseSelectControl.this.items) {
        boolean isDisabled = BaseSelectControl.this.disabledItems.contains(item);

        BeanDisplayItem<T> option = new BeanDisplayItem<T>(item,
            BaseSelectControl.this.labelProperty,
            BaseSelectControl.this.valueProperty,
            BaseSelectControl.this.groupProperty,
            BaseSelectControl.this.childrenProperty,
            isDisabled);

        this.selectItems.add(option);
        if (isDisabled) {
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
      return DisplayItemUtil.getSelectedItemByValue(this.selectItems, value);
    }

    public List<DisplayItem> getSelectedItems() {
      return DisplayItemUtil.getSelectedItems(this.selectItems, (String[]) innerData);
    }

    public DisplayItem getSelectItem(String value) {
      return DisplayItemUtil.getSelectedItemByValue(this.selectItems, value);
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
