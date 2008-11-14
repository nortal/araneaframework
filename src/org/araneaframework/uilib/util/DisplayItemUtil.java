/**
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
**/

package org.araneaframework.uilib.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections.Transformer;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Represents the items put into {@link org.araneaframework.uilib.form.control.SelectControl}or
 * {@link org.araneaframework.uilib.form.control.MultiSelectControl}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DisplayItemUtil implements Serializable {

  private static final long serialVersionUID = 1L;
  
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to provided <code>displayItemContainer</code>.
   * 
   * @param displayItemContainer the container for created {@link DisplayItem}s
   * @param beanCollection <code>Collection</code> of beans, may not contain <code>null</code>.
   * @param valueName the name of the bean field corresponding to the (submitted) value of the select item.
   * @param displayStringName the name of the bean field corresponding to the displayed string (label) of the select item.
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItemContainer, Collection beanCollection, String valueName, String displayStringName) {
    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notEmptyParam(valueName, "valueName");
    Assert.notEmptyParam(displayStringName, "displayStringName");
    
    if (beanCollection.size() == 0) return;
    BeanMapper beanMapper = new BeanMapper(beanCollection.iterator().next().getClass());
    
    Transformer valueTransformer = new BeanToPropertyValueTransformer(beanMapper, valueName);
    Transformer displayTransformer = new BeanToPropertyValueTransformer(beanMapper, displayStringName);
    addItemsFromBeanCollection(displayItemContainer, beanCollection, valueTransformer, displayTransformer);
  }

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to provided <code>displayItemContainer</code>.
   * 
   * @param displayItemContainer the container for created {@link DisplayItem}s
   * @param beanCollection <code>Collection</code> of beans, may not contain <code>null</code>.
   * @param valueName the name of the bean field corresponding to the (submitted) value of the select item.
   * @param displayTransformer Transformer producing label (displayString) from a bean
   * 
   * @since 1.1
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItemContainer, Collection beanCollection, String valueName, Transformer displayTransformer) {
    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notEmptyParam(valueName, "valueName");
    Assert.notNullParam(displayTransformer, "displayTransformer");
    
    if (beanCollection.size() == 0) return;
    BeanMapper beanMapper = new BeanMapper(beanCollection.iterator().next().getClass());
    Transformer valueTransformer = new BeanToPropertyValueTransformer(beanMapper, valueName);
    addItemsFromBeanCollection(displayItemContainer, beanCollection, valueTransformer, displayTransformer);
  }
  
  /** 
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to provided <code>displayItemContainer</code>.
   * 
   * @param displayItemContainer the container for created {@link DisplayItem}s
   * @param beanCollection <code>Collection</code> of beans, may not contain <code>null</code>.
   * @param displayStringName the name of the bean field corresponding to the displayed string (label) of the select item.
   * @param valueTransformer Transformer producing value ({@link DisplayItem#getValue()}) from a bean.
   * 
   * @since 1.1
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItemContainer, Collection beanCollection, Transformer valueTransformer, String displayStringName) {
    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notNullParam(valueTransformer, "valueTransformer");
    Assert.notEmptyParam(displayStringName, "displayStringName");
    
    if (beanCollection.size() == 0) return;
    BeanMapper beanMapper = new BeanMapper(beanCollection.iterator().next().getClass());
    Transformer displayTransformer = new BeanToPropertyValueTransformer(beanMapper, displayStringName);
    addItemsFromBeanCollection(displayItemContainer, beanCollection, valueTransformer, displayTransformer);
  }

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to provided <code>displayItemContainer</code>.
   *
   * @param displayItemContainer the container for created {@link DisplayItem}s
   * @param beanCollection <code>Collection</code> of beans, may not contain <code>null</code>.
   * @param valueTransformer Transformer producing value ({@link DisplayItem#getValue()}) from a bean.
   * @param displayTransformer Transformer producing label (displayString) from a bean
   * 
   * @since 1.1
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItemContainer, Collection beanCollection, Transformer valueTransformer, Transformer displayTransformer) {
    if (beanCollection == null || beanCollection.size() == 0) return;

    Assert.notNullParam(displayItemContainer, "displayItemContainer");
    Assert.notNullParam(valueTransformer, "valueTransformer");
    Assert.notNullParam(displayTransformer, "displayTransformer");

    for (Iterator i = beanCollection.iterator(); i.hasNext();) {
      Object vo = i.next();
      displayItemContainer.addItem(new DisplayItem((String)valueTransformer.transform(vo), (String)displayTransformer.transform(vo)));
    }
  }
  
  /**
   * Returns whether <code>value</code> is found in the select items.
   * @param value the value that is controlled.
   * @return whether <code>value</code> is found in the select items.
   */
  public static boolean isValueInItems(DisplayItemContainer displayItemContainer, String value) {
    return isValueInItems(displayItemContainer.getDisplayItems(), value);
  }
  
  /**
   * Returns whether <code>value</code> is found in the select items.
   * 
   * @param displayItems The items that should be checked whether given
   *          <code>value</code> is one of them.
   * @param value the value that is controlled.
   * @param ignoreStatus Whether this algorithm should ignore whether the
   *          currentItem is disabled or not.
   * @return whether <code>value</code> is found in the select items.
   */
  public static boolean isValueInItems(Collection displayItems, String value) {
    Assert.noNullElementsParam(displayItems, "displayItems");

    for (Iterator i = displayItems.iterator(); i.hasNext();) {
      DisplayItem currentItem = (DisplayItem) i.next();
      String currentValue = currentItem.getValue();
      boolean disabled = currentItem.isDisabled();

      if (value == null && currentValue == null && !disabled) {
        return true;
      }
      if (value != null && value.equals(currentValue) && !disabled) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns display item label by the specified value.
   * 
   * @param displayItems display items.
   * @param value display item value.
   * @return display item label by the specified value.
   */
  public static String getLabelForValue(Collection displayItems, String value) {
    Assert.noNullElementsParam(displayItems, "displayItems");
    
    for (Iterator i = displayItems.iterator(); i.hasNext(); ) {
      DisplayItem item = (DisplayItem)i.next();
      String currentValue = item.getValue();
      if (value == null && currentValue == null)
        return item.getDisplayString();
      if (value != null && value.equals(currentValue)) 
        return item.getDisplayString();
    }
    return "";
  }

  /**
   * Returns display item index by the specified value.
   * 
   * @param displayItems display items.
   * @param value display item value.
   * @return display item index by the specified value.
   */
  public static int getValueIndex(List displayItems, String value) {
    Assert.noNullElementsParam(displayItems, "displayItems");
    for (ListIterator i = displayItems.listIterator(); i.hasNext();) {
      DisplayItem item = (DisplayItem) i.next();
      if ((value == null && item.getValue() == null) || value != null
          && item.getValue() != null && value.equals(item.getValue())) {
        return i.previousIndex();
      }
    }
    return -1;
  }      

  /**
   * Checks whether the <code>item</code> (that is not yet added to
   * <code>items</code>) would not have an other item with the same value
   * already in the <code>items</code> list.
   * <p>
   * If that constraint is violated, an assertion exception will be thrown.
   * 
   * @param items The display items of the control
   * @param item An item to be added
   */
  public static void assertUnique(List items, DisplayItem item) {
    Assert.isTrue(!items.contains(item), "The DisplayItems of the"
        + "SelectControl must have different values - not like with '"
        + item.getValue() + "'.");
  }

  /**
   * Checks whether the <code>itemsToAdd</code> (that are not yet added to
   * <code>items</code>) would not have an other item with the same value
   * already in the <code>items</code> list.
   * <p>
   * If that constraint is violated, an assertion exception will be thrown.
   * 
   * @param items The display items of the control
   * @param itemsToAdd Items to be added
   */
  public static void assertUnique(List items, Collection itemsToAdd) {
    for (Iterator i = itemsToAdd.iterator(); i.hasNext();) {
      DisplayItem item = (DisplayItem) i.next();
      Assert.isTrue(!items.contains(item), "The DisplayItems of the"
          + "SelectControl must have different values - not like with '"
          + item.getValue() + "'.");
    }
  }

  private static class BeanToPropertyValueTransformer implements Transformer, Serializable {
	private static final long serialVersionUID = 1L;
	private final BeanMapper bm;
    private final String propertyName;
    
    public BeanToPropertyValueTransformer(final BeanMapper beanMapper, final String propertyName) {
      this.bm = beanMapper;
      this.propertyName = propertyName;
    }

    public Object transform(Object bean) {
      return bm.getFieldValue(bean, propertyName).toString();
    }
  }
}
