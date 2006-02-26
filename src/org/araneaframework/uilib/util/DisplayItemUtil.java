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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Represents the items put into {@link org.araneaframework.uilib.form.control.SelectControl}or
 * {@link org.araneaframework.uilib.form.control.MultiSelectControl}.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class DisplayItemUtil implements java.io.Serializable {

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Adds the select items corresponding to the given value and label fields in Value Object.
   * 
   * @param valueObjects <code>Collection</code> of Value Objects.
   * @param valueName the name of the Value Object field corresponding to the value of the select item.
   * @param displayStringName the name of the Value Object field corresponding to the display string of the select item.
   */
  public static void addItemsFromVoCollection(DisplayItemContainer displayItems, Collection valueObjects, String valueName, String displayStringName) {
    if (valueObjects.size() == 0) return;
  	BeanMapper beanMapper = new BeanMapper(valueObjects.iterator().next().getClass());

    for (Iterator i = valueObjects.iterator(); i.hasNext();) {
      Object vo = i.next();
      displayItems.addItem(new DisplayItem(beanMapper.getBeanFieldValue(vo, valueName).toString(), beanMapper.getBeanFieldValue(vo,
          displayStringName).toString()));
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
		for (ListIterator i = displayItems.listIterator(); i.hasNext(); ) {
			DisplayItem item = (DisplayItem) i.next();
			if ((value == null && item.getValue() == null) ||
					value != null && item.getValue() != null && value.equals(item.getValue())) {
				return i.previousIndex();
			}
		}
		
		return -1;
	}      
  
}
