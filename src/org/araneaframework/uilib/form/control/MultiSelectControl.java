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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemContainer;
import org.araneaframework.uilib.util.DisplayItemUtil;


/**
 * This class represents a multiselect control (aka list).
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class MultiSelectControl extends StringArrayRequestControl implements DisplayItemContainer {

  //*********************************************************************
  //* FIELDS
  //*********************************************************************  

  /**
   * The {@link org.araneaframework.uilib.util.DisplayItemUtil}.
   */
  protected List items = new ArrayList();

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************  
  
  /**
   * Adds a display-item to the element.
   * 
   * @param item the item to be added.
   */
  public void addItem(DisplayItem item) {
    items.add(item);
  }
  
  /**
   * Adds a display-items to the element.
   * 
   * @param items the items to be added.
   */
  public void addItems(Collection items) {
  	this.items.addAll(items);
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

  public boolean isRead() {
    return ((String[]) innerData).length > 0;
  }
  
  /**
   * Returns "List&lt;String&gt;".
   * 
   * @return "List&lt;String&gt;".
   */
  public String getRawValueType() {
    return "List<String>";
  }
  
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }  
  
  /**
   * Removes all empty strings from the <code>String[]</code> request parameters.
   */
  protected String[] preprocessRequestParameters(String[] parameterValues) {
	  //Removes submitted empty values	    
	  if (parameterValues != null && parameterValues.length > 0) {
	
	    List processedParameterValues = new ArrayList();
	
	    for (int i = 0; i < parameterValues.length; i++) {
	      if (parameterValues[i] != null && !"".equals(parameterValues[i])) {
	        processedParameterValues.add(parameterValues[i]);
	      }
	    }
	
	    parameterValues = convertToStringArray(processedParameterValues);
	  }
	  else {
	    parameterValues = new String[] {};
	  }
	  	    
	  if (parameterValues != null) {
	    for (int i = 0; i < parameterValues.length; i++) {
	      
	      if (!DisplayItemUtil.isValueInItems(MultiSelectControl.this, parameterValues[i]))
	        throw new SecurityException("A value '" + parameterValues[i]
	            + "' not found in the list has been submitted to a multiselect select control!");
	    }	  	      
	  }
	  
	  if (innerData != null) {
	    //Handles disabled DisplayItems	    
	  	Set previousDisabledValues = new HashSet(Arrays.asList((Object[]) innerData));	  		  	
	  	Set currentValues = new HashSet(parameterValues == null ? new ArrayList() : Arrays.asList(parameterValues));
	  	Set disabledItemValues = new HashSet();
	  	
	  	for (Iterator i = items.iterator(); i.hasNext();) {
				DisplayItem item = (DisplayItem) i.next();
				if (item.isDisabled()) disabledItemValues.add(item.getValue());
			}
	  	
	  	previousDisabledValues.retainAll(disabledItemValues);	  		  	
	  	currentValues.addAll(previousDisabledValues);
	  	
	  	parameterValues = convertToStringArray(currentValues);	    	    
	  }
	  return parameterValues;
  }

  /**
   * Converts the given <code>List&lt;String&gt;</code> to a <code>String[]</code>.
   * 
   * @param data <code>List&lt;String&gt;</code>.
   * @return <code>String[]</code>.
   */
  protected String[] convertToStringArray(Collection data) {
    String[] result = new String[data.size()];

    Iterator i = data.iterator();
    int j = 0;
    
    while (i.hasNext()) {
    	result[j] = (String) i.next();
    	j++;
    }
    
    return result;
  }



  /**
   * Converts <code>String[]</code> to <code>List&lt;String&gt;</code>.
   */
  protected Object fromRequestParameters(String[] parameterValues) {
    return Arrays.asList(parameterValues);
  }

  /**
   * Converts <code>List&lt;String&gt;</code> to <code>String[]</code>.
   */
  protected String[] toResponseParameters(Object controlValue) {
    return convertToStringArray((Collection) controlValue);
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * Represents a multiselect control view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends StringArrayRequestControl.ViewModel {

    private List selectItems;
    private Map selectItemMap = new HashMap();
    private Set valueSet = new HashSet();
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.selectItems = items;
      
      for (Iterator i = selectItems.iterator(); i.hasNext(); ) {
      	DisplayItem displayItem = (DisplayItem) i.next();
      	selectItemMap.put(displayItem.getValue(), displayItem);
      }

  		String[] values = getValues();
  		if (values != null) {		
  			for(int i = 0; i < values.length; i++)
  				valueSet.add(values[i]);
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
		
		public Set getValueSet() {
			return valueSet;
		}
  }
}
