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

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.SerializationUtils;
import org.araneaframework.uilib.support.DataType;
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
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class Data<T> implements Serializable, FormElementAware<Object, T> {

  protected DataType type;

  protected T value;

  protected T markedBaseValue;

  protected FormElement<Object, T> feCtx;

  /**
   * Creates a new <code>Data</code> instance for given <code>type</code>.
   * 
   * @param type The type of object(s) that this data represents.
   * @since 2.0
   */
  protected Data(DataType type) {
    this.type = type;
  }

  /**
   * Creates a new <code>Data</code> instance for type <code>collectionClass</code>&lt;<code>typeClass</code>&gt;.
   * 
   * @param collectionClass An optional collection type, if this data represents a collection of <code>typeClass</code> objects.
   * @param typeClass The type of object that this data represents.
   * @since 2.0
   */
  protected Data(Class<? extends Collection<T>> collectionClass, Class<T> typeClass) {
    this(new DataType(collectionClass, typeClass));
  }

  /**
   * Creates a new <code>Data</code> instance for type <code>typeClass</code>.
   * 
   * @param typeClass The type of object that this data represents.
   */
  public Data(Class<T> typeClass) {
    this(null, typeClass);
  }

  /**
   * Provides the current value of this <code>Data</code>.
   * 
   * @return The current value of this <code>Data</code>.
   */
  public T getValue() {
    return this.value;
  }

  /**
   * Sets the value of this <code>Data</code> and the associated {@link Control}.
   * 
   * @param value The new value for this <code>Data</code>.
   */
  public void setValue(T value) {
    setDataValue(value);
    setControlValue(value);
  }

  /**
   * Sets the value of this <code>Data</code> without modifying underlying {@link Control}. This is used on
   * {@link FormElement} conversion when {@link Control} values read from request should not be affected.
   * 
   * @param value The new value for this <code>Data</code>.
   * @since 1.0.12
   */
  public void setDataValue(T value) {
    this.value = value;
  }

  /**
   * Sets the value of {@link Control} that is associated with {@link FormElement} which owns this <code>Data</code>.
   * 
   * @param value The new value for the associated {@link Control}.
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
   * Provides the type of this <code>Data</code>.
   * 
   * @return The type of this <code>Data</code>.
   */
  public DataType getValueType() {
    return this.type;
  }

  @Override
  public String toString() {
    return "Data (Type=[" + this.type + "];Value=[" + this.value + "])";
  }

  /**
   * Creates a new instance of this <code>Data</code> of the same type as this instance, however, value is not set.
   * 
   * @return A new instance of current this {@link Data} that is of same type.
   */
  public Data<T> newData() {
    return new Data<T>(this.type.clone());
  }

  /**
   * A factory method to create instances of <code>Data</code> for handling objects of given class.
   * 
   * @param <V> The type of the <code>clazz</code> and of the new <code>Data</code> object.
   * @param clazz The type of the data that the new <code>Data</code> will hold.
   * @return The new instance of <code>Data</code>.
   */
  public static <V> Data<V> newInstance(Class<V> clazz) {
    return new Data<V>(clazz);
  }

  /**
   * A factory method to create instances of <code>Data</code> for handling objects of given classes.
   * 
   * @param <V> The type of the <code>clazz</code> and of the new <code>Data</code> object.
   * @param collectionClazz A collection class indicating that the data will store data for collections of given type.
   * @param clazz The type of the data that the new <code>Data</code> will hold.
   * @return The new instance of <code>Data</code>.
   * @since 2.0
   */
  public static <V> Data<V> newInstance(Class<? extends Collection<V>> collectionClazz, Class<V> clazz) {
    return new Data<V>(collectionClazz, clazz);
  }

  /**
   * Marks the current value of the {@link Data} as the base state that will be used to determine whether its state has
   * changed in {@link #isStateChanged()}.
   */
  public void markBaseState() {
    this.markedBaseValue = copyValue();
  }

  /**
   * Restores the value of the {@link Data} from the marked base state.
   */
  public void restoreBaseState() {
    setValue(this.markedBaseValue);
  }

  /**
   * This method makes a copy of current value based on the type information. If the value is serializable, it is also
   * copied, otherwise the same instance is returned.
   * 
   * @return A copy of the value if this <code>Data</code>, or just the value as it currently is.
   * @since 2.0
   */
  @SuppressWarnings("unchecked")
  protected T copyValue() {
    return this.type.isSerializable() ? (T) SerializationUtils.clone((Serializable) this.value) : this.value;
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
