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
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormElementContext;
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
  protected List<DisplayItem> items = new ArrayList<DisplayItem>();

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
   * @param items Collection&lt;DisplayItem&gt; the items to be added.
   */
  public <E extends DisplayItem> void addItems(Collection<E> items) {
    Assert.noNullElementsParam(items, "items");
    
    this.items.addAll(items);
  }  

  /**
   * Creates {@link DisplayItem}s corresponding to beans in <code>beanCollection</code> and adds
   * these to this {@link SelectControl}.
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
   * these to this {@link SelectControl}.
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
   * these to this {@link SelectControl}.
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
   * these to this {@link SelectControl}.
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
   * Returns {@link DisplayItem} corresponding to selected element. Current
   * value by which selected element is determined is reported by the {@link FormElement} 
   * to which this {@link SelectControl})belongs. If no {@link FormElement} is
   * associated with {@link SelectControl}, this method returns <code>null</code>.
   * @return {@link DisplayItem} corresponding to selected element.
   * @since 1.0.5
   */
  public DisplayItem getSelectedItem() {
    FormElementContext<String, Object> ctx = getFormElementCtx();
    if (ctx == null)
      return null;

    int index = getValueIndex((String)ctx.getValue());
    return index >= 0 ? (DisplayItem)getDisplayItems().get(index) : null; 
  }

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Controls that the value submitted by the user is found in the select
   * items list. 
   */
  @Override
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
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  @Override
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
				DisplayItem previousDisplayItem = getDisplayItems().get(valueIndex);
				
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
  public class ViewModel extends StringValueControl.ViewModel {

    private List<DisplayItem> selectItems;
    private Map<String, DisplayItem> selectItemMap = new HashMap<String, DisplayItem>(); 
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.selectItems = items;
      
      for (DisplayItem displayItem : selectItems) {
      	selectItemMap.put(displayItem.getValue(), displayItem);
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
    
    public boolean containsItem(String itemValue) {
      return selectItemMap.get(itemValue) != null;
    }
    
    public String getLabelForValue(String itemValue) {
      DisplayItem selectItemByValue = getSelectItemByValue(itemValue);
      return selectItemByValue != null ? selectItemByValue.getDisplayString() : "";
    }
  }  
}
