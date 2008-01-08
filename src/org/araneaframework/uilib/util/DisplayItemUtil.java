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
public class DisplayItemUtil implements java.io.Serializable {

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Adds the select items corresponding to the given value and label fields in Value Object.
   * 
   * @param beanCollection <code>Collection</code> of beans, may not contain <code>null</code>.
   * @param valueName the name of the bean field corresponding to the value of the select item.
   * @param displayStringName the name of the bean field corresponding to the displayed string (label) of the select item.
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItems, Collection beanCollection, String valueName, String displayStringName) {
    Assert.notNullParam(displayItems, "displayItems");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notEmptyParam(valueName, "valueName");
    Assert.notEmptyParam(displayStringName, "displayStringName");
    
    if (beanCollection.size() == 0) return;
    BeanMapper beanMapper = new BeanMapper(beanCollection.iterator().next().getClass());
    
    Transformer valueTransformer = new BeanToPropertyValueTransformer(beanMapper, valueName);
    Transformer displayTransformer = new BeanToPropertyValueTransformer(beanMapper, displayStringName);
    addItemsFromBeanCollection(displayItems, beanCollection, valueTransformer, displayTransformer);
  }

  /** 
   * @since 1.1
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItems, Collection beanCollection, String valueName, Transformer displayTransformer) {
    Assert.notNullParam(displayItems, "displayItems");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notEmptyParam(valueName, "valueName");
    Assert.notNullParam(displayTransformer, "displayTransformer");
    
    if (beanCollection.size() == 0) return;
    BeanMapper beanMapper = new BeanMapper(beanCollection.iterator().next().getClass());
    Transformer valueTransformer = new BeanToPropertyValueTransformer(beanMapper, valueName);
    addItemsFromBeanCollection(displayItems, beanCollection, valueTransformer, displayTransformer);
  }
  
  /** 
   * @since 1.1
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItems, Collection beanCollection, Transformer valueTransformer, String displayStringName) {
    Assert.notNullParam(displayItems, "displayItems");
    Assert.noNullElementsParam(beanCollection, "beanCollection");
    Assert.notNullParam(valueTransformer, "valueTransformer");
    Assert.notEmptyParam(displayStringName, "displayStringName");
    
    if (beanCollection.size() == 0) return;
    BeanMapper beanMapper = new BeanMapper(beanCollection.iterator().next().getClass());
    Transformer displayTransformer = new BeanToPropertyValueTransformer(beanMapper, displayStringName);
    addItemsFromBeanCollection(displayItems, beanCollection, valueTransformer, displayTransformer);
  }

  /**
   * @since 1.1
   */
  public static void addItemsFromBeanCollection(DisplayItemContainer displayItemContainer, Collection beanCollection, Transformer valueTransformer, Transformer displayTransformer) {
    if (beanCollection == null || beanCollection.size() == 0) return;

    Assert.notNullParam(displayItemContainer, "displayItems");
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
  public static boolean isValueInItems(DisplayItemContainer displayItems, String value) {
    return isValueInItems(displayItems.getDisplayItems(), value);
  }
  
  /**
   * Returns whether <code>value</code> is found in the select items.
   * @param value the value that is controlled.
   * @return whether <code>value</code> is found in the select items.
   */
  public static boolean isValueInItems(Collection displayItems, String value) {
    Assert.noNullElementsParam(displayItems, "displayItems");
    
    for (Iterator i = displayItems.iterator(); i.hasNext(); ) {
    	DisplayItem currentItem = (DisplayItem) i.next();
      String currentValue = currentItem.getValue();
      if (value == null && currentValue == null && !currentItem.isDisabled())
        return true;      
      if (value != null && value.equals(currentValue) && !currentItem.isDisabled()) 
        return true;
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
    
		for (ListIterator i = displayItems.listIterator(); i.hasNext(); ) {
			DisplayItem item = (DisplayItem) i.next();
			if ((value == null && item.getValue() == null) ||
					value != null && item.getValue() != null && value.equals(item.getValue())) {
				return i.previousIndex();
			}
		}
		
		return -1;
	}      
  
  private static class BeanToPropertyValueTransformer implements Transformer, Serializable {
	private static final long serialVersionUID = 1L;
	private BeanMapper bm;
    private String propertyName;
    
    public BeanToPropertyValueTransformer(final BeanMapper beanMapper, final String propertyName) {
      this.bm = beanMapper;
      this.propertyName = propertyName;
    }

    public Object transform(Object bean) {
      return bm.getFieldValue(bean, propertyName).toString();
    }
  }
}
