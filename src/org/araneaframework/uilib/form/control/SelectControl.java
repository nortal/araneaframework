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

package org.araneaframework.uilib.form.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemContainer;
import org.araneaframework.uilib.util.DisplayItemUtil;


/**
 * This class represents a selectbox (aka dropdown) control.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class SelectControl extends StringValueControl  implements DisplayItemContainer {

  /**
   * A list of {@link DisplayItem}s.
   */
  protected List items = new ArrayList();

  /**
   * Adds a select-item to the element.
   * 
   * @param item the item to be added.
   */
  public void addItem(DisplayItem item) {
    Assert.notNullParam(item, "item");
    
    items.add(item);
  }
  
  /**
   * Adds a display-items to the element.
   * 
   * @param items the items to be added.
   */
  public void addItems(Collection items) {
    Assert.noNullElementsParam(items, "items");
    
    this.items.addAll(items);
  }  
  
  /**
   * Adds the display-items corresponding to the given value and label fields in Value Object.
   * 
   * @param valueObjects <code>Collection</code> of Value Objects.
   * @param valueName the name of the Value Object field corresponding to the value of the select
   * item.
   * @param labelName the name of the Value Object field corresponding to the label of the select
   * item.
   */
  public void addDisplayItems(Collection valueObjects, String valueName, String labelName) {
    DisplayItemUtil.addItemsFromVoCollection(this, valueObjects, valueName, labelName);
  }    

  /**
   * Clears the list of select-items.
   */
  public void clearItems() {
    items.clear();
  }
    
	public List getDisplayItems() {
		return items;
	}    
	
	public int getValueIndex(String value) {
		return DisplayItemUtil.getValueIndex(items, value);
	}	
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Controls that the value submitted by the user is found in the select
   * items list. 
   */
  protected void validateNotNull() {
    super.validateNotNull();
    
    String data = innerData == null ? null : ((String[]) innerData)[0];
    
    if (!DisplayItemUtil.isValueInItems(SelectControl.this, data)) 
      throw new SecurityException("A value '" + data + "' not found in the list has been submitted to a select control!");
  }

  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }

  protected String preprocessRequestParameter(String parameterValue) {
    //TODO: refactor ugly hack
    
    parameterValue = super.preprocessRequestParameter(parameterValue);
    
    if (parameterValue != null && !DisplayItemUtil.isValueInItems(SelectControl.this, parameterValue)) 
      throw new SecurityException("A value '" + parameterValue + "' not found in the list has been submitted to a select control!");
	  
    //Handles disabled DisplayItems      
	  String[] previousValues = (String[]) innerData;		  	    	   
    
	  if (previousValues != null && previousValues.length == 1) {
			int valueIndex = getValueIndex(previousValues[0]);
			
			if (valueIndex != -1) {
				DisplayItem previousDisplayItem = (DisplayItem) getDisplayItems().get(valueIndex);
				
				if (previousDisplayItem.isDisabled() && parameterValue == null)
					return previousDisplayItem.getValue();
			}
	  }
		
		return parameterValue;
  }
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends StringArrayRequestControl.ViewModel {

    private List selectItems;
    private Map selectItemMap = new HashMap(); 
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.selectItems = items;
      
      for (Iterator i = selectItems.iterator(); i.hasNext(); ) {
      	DisplayItem displayItem = (DisplayItem) i.next();
      	selectItemMap.put(displayItem.getValue(), displayItem);
      }      
                 
    }         
    
    /**
     * Returns a <code>List</code> of {@link DisplayItem}s.
     * @return a <code>List</code> of {@link DisplayItem}s.
     */
    public List getSelectItems() {
      return selectItems;
    }
    
		public DisplayItem getSelectItemByValue(String value) {
			return (DisplayItem) selectItemMap.get(value);
		}    
    
    public boolean containsItem(String itemValue) {
      return selectItemMap.get(itemValue) != null;
    }
    
    public String getLabelForValue(String itemValue) {
      DisplayItem selectItemByValue = getSelectItemByValue(itemValue);
      return selectItemByValue != null ? selectItemByValue.getDisplayString() : "";
    }
  }  
}
