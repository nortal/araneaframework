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

package org.araneaframework.uilib.form;

import org.araneaframework.uilib.DataItemTypeViolatedException;

/**
 * This class represents the form element data. It has a type, used by the
 * {@link org.araneaframework.uilib.form.converter.ConverterFactory} to find the appropriate
 * {@link org.araneaframework.uilib.form.converter.BaseConverter} and a value.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class Data implements java.io.Serializable {
  protected String type;
  protected Class typeClass;
  
  protected boolean dirty = false;
  
  protected Object value;  
  protected Object markedBaseValue;
  
  /**
   * Creates <code>DataItem</code> of type <code>type</code>.
   * @param type the type of <code>DataItem</code>
   */
  protected Data(Class typeClass, String type) {   
    this.type = type;
    this.typeClass = typeClass;
  }
  
  public Data(Class typeClass) {
    this.typeClass = typeClass;
    this.type = typeClass.toString().substring(
        typeClass.toString().lastIndexOf('.') + 1);
  }  

  /**
   * Returns <code>DataItem</code> value.
   * @return <code>DataItem</code> value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * Sets <code>DataItem</code> value.
   * @param value <code>DataItem</code> value.
   */
  public void setValue(Object value) {
    if (value != null && !(typeClass.isAssignableFrom(value.getClass())))
      throw new DataItemTypeViolatedException(getValueType(), value.getClass());
    
    this.dirty = true;
    this.value = value;
  }

  /**
   * Returns <code>DataItem</code> type.
   * @return <code>DataItem</code> type.
   */
  public String getValueType() {
    return type;
  }
  
  /**
   * Returns <code>dataItem</code> display string. 
   */
  public String toString() {
    return "DataItem: [Type = " + getValueType() + "; Value = " + value + "]";
  }
  
  /**
   * Invalidates the <code>DataItem</code>.
   */
  public void clean() {
    dirty = false;
  }
  
  /**
   * Returns whether <code>DataItem</code> value has been set.
   * @return whether <code>DataItem</code> value has been set.
   */
  public boolean isDirty() {
    return dirty;
  }
  
  /**
   * Returns a new instance of current DataItem.
   * @return a new instance of current DataItem.
   */
  public Data newData() {
    return new Data(typeClass, type);
  }
  
  /**
   * Marks the current value of the data item as the base state
   * that will be used to determine whether its state has changed in
   * {@link #isStateChanged()}. 
   */
  public void markBaseState() {
  	//TODO: maybe deep copy?
  	markedBaseValue = value;
  }
  
  /**
   * Returns whether data item state has changed after it was marked.
   * @return whether data item state has changed after it was marked.
   */
  public boolean isStateChanged() {  	
  	if (markedBaseValue == null && value == null) return false;
  	else
  		if (markedBaseValue == null || value == null) return true;
  		else return !markedBaseValue.equals(value);
  }
}
