/*
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
 */

package org.araneaframework.uilib.form;

import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

import org.araneaframework.uilib.util.Event;

/**
 * This class represents typed form element data. Type is used by the
 * {@link org.araneaframework.uilib.form.converter.ConverterFactory} to find the appropriate
 * {@link org.araneaframework.uilib.form.Converter} for converting {@link Data} held in
 * {@link org.araneaframework.uilib.form.FormElement} to plain object type held in
 * {@link org.araneaframework.uilib.form.Control}. Reverse converting happens much the same, both object type in
 * {@link org.araneaframework.uilib.form.Control} and supposed {@link org.araneaframework.uilib.form.FormElement}
 * {@link Data} are considered and appropriate {@link org.araneaframework.uilib.form.Converter} chosen.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class Data<T> implements Serializable, FormElementAware<Object, T> {

  protected String type;

  protected Class<T> typeClass;

  protected T value;

  protected T markedBaseValue;

  protected FormElement<Object, T> feCtx;

  /**
   * Creates {@link Data} of type <code>type</code>.
   * 
   * @param type the type of {@link Data}
   */
  protected Data(Class<T> typeClass, String type) {
    this.typeClass = typeClass;
    this.type = type;
  }

  /**
   * Creates {@link Data} for holding objects of given <code>Class</code>. <code>Type</code> is assumed to be simple
   * class name of given Class.
   * 
   * @param typeClass the <code>Class</code> of {@link Data} values.
   */
  public Data(Class<T> typeClass) {
    this(typeClass, typeClass.getSimpleName());
  }

  /**
   * Returns {@link Data} value.
   * 
   * @return {@link Data} value.
   */
  public T getValue() {
    return this.value;
  }

  /**
   * Sets {@link Data} value.
   * 
   * @param value {@link Data} value.
   */
  public void setValue(T value) {
    setDataValue(value);
    setControlValue(value);
  }

  /**
   * Sets the value of this {@link Data} without modifying underlying {@link Control}. This is used on
   * {@link FormElement} conversion&mdash;which should not affect {@link Control} values read from request.
   * 
   * @since 1.0.12
   */
  public void setDataValue(T value) {
    this.value = value;
  }

  /**
   * Sets the value of {@link Control} that is associated with {@link FormElement} which owns this {@link Data}.
   * 
   * @since 1.0.12
   */
  public void setControlValue(final T value) {
    if (this.feCtx != null) {
      this.feCtx.addInitEvent(new Event() {

        public void run() {
          // TODO:this is dangerous in case Data value is set before FE is associated with Control
          if (feCtx.getControl() != null) {
            feCtx.getControl().setRawValue(feCtx.getConverter().reverseConvert(value));
          }
        }
      });
    }
  }

  /**
   * Returns {@link Data} type.
   * 
   * @return {@link Data} type.
   */
  public String getValueType() {
    return this.type;
  }

  @Override
  public String toString() {
    return "Data: [Type = " + this.type + "; Value = '" + this.value + "']";
  }

  /**
   * Creates a new instance of this {@link Data} of the same type as this instance, however, value is not set.
   * 
   * @return A new instance of current this {@link Data} that is of same type.
   */
  public Data<T> newData() {
    return new Data<T>(this.typeClass, this.type);
  }

  public static <V> Data<V> newInstance(Class<V> clazz) {
    return new Data<V>(clazz);
  }

  /**
   * Marks the current value of the {@link Data} as the base state that will be used to determine whether its state has
   * changed in {@link #isStateChanged()}.
   */
  public void markBaseState() {
    this.markedBaseValue = this.value;
  }

  /**
   * Restores the value of the {@link Data} from the marked base state.
   */
  public void restoreBaseState() {
    // TODO: maybe deep copy?
    setValue(this.markedBaseValue);
  }

  /**
   * Returns whether {@link Data} state (value) has changed after it was marked.
   * 
   * @return whether {@link Data} state (value) has changed after it was marked.
   */
  public boolean isStateChanged() {
    return !ObjectUtils.equals(this.markedBaseValue, this.value);
  }

  public void setFormElementCtx(FormElementContext<Object, T> feCtx) {
    if (this.feCtx != feCtx) {
      this.feCtx = (FormElement<Object, T>) feCtx;
      setValue(getValue());
    }
  }
}
