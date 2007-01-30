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
import org.araneaframework.uilib.util.Event;

/**
 * This class represents typed form element data. Type is used by the
 * {@link org.araneaframework.uilib.form.converter.ConverterFactory} to find the appropriate
 * {@link org.araneaframework.uilib.form.Converter} for converting {@link Data} held in 
 * {@link org.araneaframework.uilib.form.FormElement} to plain object type held in 
 * {@link org.araneaframework.uilib.form.Control}. Reverse converting happens much
 * the same, both object type in {@link org.araneaframework.uilib.form.Control} 
 * and supposed {@link org.araneaframework.uilib.form.FormElement} {@link Data} are
 * considered and appropriate {@link org.araneaframework.uilib.form.Converter} 
 * chosen.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class Data implements java.io.Serializable, FormElementAware {
  protected String type;
  protected Class typeClass;
  
  /** @deprecated do not use */
  protected boolean dirty = false;
  
  protected Object value;  
  protected Object markedBaseValue;
  
  protected FormElement feCtx;
  
  /**
   * Creates {@link Data} of type <code>type</code>.
   * @param type the type of {@link Data}
   */
  protected Data(Class typeClass, String type) {   
    this.type = type;
    this.typeClass = typeClass;
  }

  /**
   * Creates {@link Data} for holding objects of given <code>Class</code>.
   * <code>Type</code> is assumed to be simple class name of given Class.
   * @param typeClass the <code>Class</code> of {@link Data} values.
   */
  public Data(Class typeClass) {
    this.typeClass = typeClass;
    this.type = typeClass.toString().substring(
        typeClass.toString().lastIndexOf('.') + 1);
  }  

  /**
   * Returns {@link Data} value.
   * @return {@link Data} value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * Sets {@link Data} value.
   * @param value {@link Data} value.
   */
  public void setValue(Object value) {
    final Object val = value;

    if (value != null && !(typeClass.isAssignableFrom(value.getClass())))
      throw new DataItemTypeViolatedException(getValueType(), value.getClass());
    
    this.value = value;
    
    if (feCtx != null) {
      feCtx.addInitEvent(new Event() {
		public void run() {
          // TODO:this is dangerous in case Data value is set before FE is associated with Control
          if (feCtx.getControl() != null) {
            feCtx.getControl().setRawValue(feCtx.getConverter().reverseConvert(val));
          }
		}
      });
    }
  }

  /**
   * Returns {@link Data} type.
   * @return {@link Data} type.
   */
  public String getValueType() {
    return type;
  }
  
  public String toString() {
    return "Data: [Type = " + getValueType() + "; Value = " + value + "]";
  }
  
  /**
   * Marks the {@link Data} non-dirty.
   * @deprecated not used anymore, do not call
   */
  public void clean() {
    dirty = false;
  }
  
  /**
   * Returns whether {@link Data} value has been set by calling {@link #setValue(Object)}.
   * @return whether {@link Data} value has been set by calling {@link #setValue(Object)}.
   * @deprecated not used anymore, do not call
   */
  public boolean isDirty() {
    return dirty;
  }
  
  /**
   * Returns a new instance of this {@link Data}, value is not set.
   * @return a new instance of current {@link Data}, value is not set.
   */
  public Data newData() {
    return new Data(typeClass, type);
  }
  
  /**
   * Marks the current value of the {@link Data} as the base state
   * that will be used to determine whether its state has changed in
   * {@link #isStateChanged()}. 
   */
  public void markBaseState() {
  	markedBaseValue = value;
  }
  
  /**
   * Restores the value of the {@link Data} from the marked base state.
   */
  public void restoreBaseState() {
    // TODO: maybe deep copy?
    value = markedBaseValue;
  }
  
  /**
   * Returns whether {@link Data} state (value) has changed after it was marked.
   * @return whether  {@link Data} state (value) has changed after it was marked.
   */
  public boolean isStateChanged() {  	
  	if (markedBaseValue == null && value == null) return false;
  	else
  		if (markedBaseValue == null || value == null) return true;
  		else return !markedBaseValue.equals(value);
  }

  public void setFormElementCtx(FormElementContext feCtx) {
    this.feCtx = (FormElement)feCtx; 
  }
}
