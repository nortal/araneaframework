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

import java.util.Collections;
import java.util.Comparator;
import org.araneaframework.core.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
public class MultiSelectControl extends StringArrayRequestControl implements DisplayItemContainer {
  
  private static final long serialVersionUID = 1L;

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
    Assert.notNullParam(item, "item");
    DisplayItemUtil.assertUnique(this.items, item);
    this.items.add(item);
  }
  
  /**
   * Adds a display-items to the element.
   * 
   * @param items the items to be added.
   */
  public void addItems(Collection items) {
    Assert.noNullElementsParam(items, "items");
    DisplayItemUtil.assertUnique(this.items, items);
    this.items.addAll(items);
  }    

  /**
   * Clears the list of select-items.
   */
  public void clearItems() {
    this.items.clear();
  }
  
  public List getDisplayItems() {
  	return this.items;
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
   * 
   * @deprecated use {@link MultiSelectControl#addFromBeanCollection(Collection, String, String)} instead
   */
  public void addDisplayItems(Collection valueObjects, String valueName, String labelName) {
    DisplayItemUtil.addItemsFromBeanCollection(this, valueObjects, valueName, labelName);
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
  public void addFromBeanCollection(Collection beanCollection, String valueName, String displayStringName) {
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
  public void addFromBeanCollection(Collection beanCollection, String valueName, Transformer displayTransformer) {
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
  public void addFromBeanCollection(Collection beanCollection, Transformer valueTransformer, String displayStringName) {
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
  public void addFromBeanCollection(Collection beanCollection, Transformer valueTransformer, Transformer displayTransformer) {
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

  /**
   * Provides a way to sort the items in this <code>MultiSelectControl</code>.
   * The <code>comparator</code> parameter is used to compare
   * <code>DisplayItem</code>s and, therefore, to set the order.
   * 
   * @param comparator Any <code>Comparator</code> that is used to define order
   *          of display items.
   * @since 1.2
   */
  public void sort(Comparator comparator) {
    Collections.sort(this.items, comparator);
  }

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  public ViewModel getViewModel() {
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

  protected void validateNotNull() {
     if (isMandatory() && ((Collection)getRawValue()).isEmpty()) {
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

    private static final long serialVersionUID = 1L;

    private List<DisplayItem> selectItems;

    private Map<String, DisplayItem> selectItemMap = new HashMap<String, DisplayItem>();

    private Set<String> valueSet = new HashSet<String>();

    /**
     * Contains the values as <String,DisplayItem>, where String is the value of
     * the displayItem.
     * 
     * @since 1.2.2
     */
    private Map<String, DisplayItem> valuesMap = new HashMap<String, DisplayItem>();

    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.selectItems = items;

      for (DisplayItem displayItem : selectItems) {
        this.selectItemMap.put(displayItem.getValue(), displayItem);
      }

        String[] values = getValues();
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          this.valueSet.add(values[i]);
          this.valuesMap.put(values[i], getSelectItemByValue(values[i]));
        }
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

    
    public Map<String, DisplayItem> getValuesMap() {
      return this.valuesMap;
    }
  }
}
