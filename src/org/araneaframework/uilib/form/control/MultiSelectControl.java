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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.Transformer;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.DisplayItemContainer;
import org.araneaframework.uilib.util.DisplayItemUtil;
import org.araneaframework.uilib.util.MessageUtil;


/**
 * This class represents a multiselect control (aka list).
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class MultiSelectControl extends StringArrayRequestControl<List<String>> implements DisplayItemContainer {

  //*********************************************************************
  //* FIELDS
  //*********************************************************************  

  /**
   * The {@link org.araneaframework.uilib.util.DisplayItemUtil}.
   */
  protected List<DisplayItem> items = new ArrayList<DisplayItem>();

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
  public <E extends DisplayItem> void addItems(Collection<E> items) {
  	this.items.addAll(items);
  }    

  /**
   * Clears the list of select-items.
   */
  public void clearItems() {
    items.clear();
  }
  
  public List<DisplayItem> getDisplayItems() {
  	return items;
  }
  
	public int getValueIndex(String value) {
		return DisplayItemUtil.getValueIndex(items, value);
	}    

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to this {@link MultiSelectControl}.
   * 
   * @param beanCollection <code>Collection</code> of beans
   * @param valueName name of bean field that determines {@link DisplayItem}s <code>value</code>
   * @param displayStringName name of bean field that determines {@link DisplayItem}s <code>displayString</code>
   * 
   * @since 1.1
   */
  public void addFromBeanCollection(Collection<?> beanCollection, String valueName, String displayStringName) {
    DisplayItemUtil.addItemsFromBeanCollection(this, beanCollection, valueName, displayStringName);
  }

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to this {@link MultiSelectControl}.
   * 
   * @param beanCollection <code>Collection</code> of beans
   * @param valueName name of bean field that determines {@link DisplayItem}s <code>value</code>
   * @param displayTransformer Transformer producing label ({@link DisplayItem}s <code>displayString</code>) for a bean
   * 
   * @since 1.1
   */
  public void addFromBeanCollection(Collection<?> beanCollection, String valueName, Transformer displayTransformer) {
    DisplayItemUtil.addItemsFromBeanCollection(this, beanCollection, valueName, displayTransformer);
  }

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to this {@link MultiSelectControl}.
   * 
   * @param beanCollection <code>Collection</code> of beans
   * @param valueTransformer Transformer producing value ({@link DisplayItem#getValue()}) from a bean.
   * @param displayStringName name of bean field that determines {@link DisplayItem}s <code>displayString</code>
   * 
   * @since 1.1
   */
  public void addFromBeanCollection(Collection<?> beanCollection, Transformer valueTransformer, String displayStringName) {
    DisplayItemUtil.addItemsFromBeanCollection(this, beanCollection, valueTransformer, displayStringName);
  }

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to this {@link MultiSelectControl}.
   * 
   * @param beanCollection <code>Collection</code> of beans
   * @param valueTransformer Transformer producing value ({@link DisplayItem#getValue()}) from a bean.
   * @param displayTransformer Transformer producing label (displayString) from a bean
   * 
   * @since 1.1
   */
  public void addFromBeanCollection(Collection<?> beanCollection, Transformer valueTransformer, Transformer displayTransformer) {
    DisplayItemUtil.addItemsFromBeanCollection(this, beanCollection, valueTransformer, displayTransformer);
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
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }  
  
  /**
   * Removes all empty strings from the <code>String[]</code> request parameters.
   */
  @Override
  protected String[] preprocessRequestParameters(String[] parameterValues) {
	  //Removes submitted empty values	    
	  if (parameterValues != null && parameterValues.length > 0) {
	
	    List<String> processedParameterValues = new ArrayList<String>();
	
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
	  	Set<String> previousDisabledValues = new HashSet<String>(Arrays.asList((String[]) innerData));	  		  	
	  	Set<String> currentValues = new HashSet<String>(parameterValues == null ? new ArrayList<String>() : Arrays.asList(parameterValues));
	  	Set<String> disabledItemValues = new HashSet<String>();
	  	
	  	for (DisplayItem item : items) {
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
  protected String[] convertToStringArray(Collection<String> data) {
    return data.toArray(new String[data.size()]);
  }
  
  
  
   @Override
  protected void validateNotNull() {
     if (isMandatory() && getRawValue().isEmpty()) {
	      addError(
	              MessageUtil.localizeAndFormat(
	              UiLibMessages.MANDATORY_FIELD, 
	              MessageUtil.localize(getLabel(), getEnvironment()),
	              getEnvironment()));
    }
  }

  /**
   * Converts <code>String[]</code> to <code>List&lt;String&gt;</code>.
   */
  @Override
  protected List<String> fromRequestParameters(String[] parameterValues) {
    return Arrays.asList(parameterValues);
  }

  /**
   * Converts <code>List&lt;String&gt;</code> to <code>String[]</code>.
   */
  @Override
  protected String[] toResponseParameters(List<String> controlValue) {
    return convertToStringArray(controlValue);
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
  public class ViewModel extends StringArrayRequestControl<List<String>>.ViewModel {

    private List<DisplayItem> selectItems;
    private Map<String, DisplayItem> selectItemMap = new HashMap<String, DisplayItem>();
    private Set<String> valueSet = new HashSet<String>();
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.selectItems = items;
      
      for (DisplayItem displayItem : selectItems) {
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
    public List<DisplayItem> getSelectItems() {
      return selectItems;
    }
    
		public DisplayItem getSelectItemByValue(String value) {
			return selectItemMap.get(value);
		}
		
		public Set<String> getValueSet() {
			return valueSet;
		}
  }



}
