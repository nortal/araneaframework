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

import java.util.List;
import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.uilib.util.Event;

/**
 * This class represents typed form element data. Type is used by the
 * {@link org.araneaframework.uilib.form.converter.ConverterFactory} to find the appropriate
 * {@link org.araneaframework.uilib.form.Converter} for converting {@link ListData} held in
 * {@link org.araneaframework.uilib.form.FormElement} to plain object type held in
 * {@link org.araneaframework.uilib.form.Control}. Reverse converting happens much the same, both object type in
 * {@link org.araneaframework.uilib.form.Control} and supposed {@link org.araneaframework.uilib.form.FormElement}
 * {@link ListData} are considered and appropriate {@link org.araneaframework.uilib.form.Converter} chosen.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ListData<T> extends Data<List<T>> {

  /**
   * Creates {@link ListData} of list type {@link List}.
   * 
   * @param type The type of {@link ListData} as <code>String</code>.
   */
  protected ListData(String type) {
    super(List.class, type);
  }

  /**
   * Returns {@link ListData} values.
   * 
   * @return {@link ListData} values.
   */
  public List<T> getValue() {
    return this.value;
  }

  /**
   * Sets {@link ListData} value.
   * 
   * @param value {@link ListData} value.
   */
  public void setValue(List<T> value) {
    setDataValue(value);
    setControlValue(value);
  }

  /**
   * Sets the value of this {@link ListData} without modifying underlying {@link Control}. This is used on
   * {@link FormElement} conversion &mdash; which should not affect {@link Control} values read from request.
   * 
   * @since 1.0.12
   */
  public void setDataValue(List<T> value) {
    this.value = value;
  }

  /**
   * Sets the value of {@link Control} that is associated with {@link FormElement} which owns this {@link ListData}.
   * 
   * @since 1.0.12
   */
  public void setControlValue(final List<T> value) {
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
   * Returns {@link ListData} type.
   * 
   * @return {@link ListData} type.
   */
  public String getValueType() {
    return this.type;
  }

  @Override
  public String toString() {
    return "Data: [Type = " + this.type + "; Value = '" + this.value + "']";
  }

  /**
   * Creates a new instance of this {@link ListData} of the same type as this instance, however, value is not set.
   * 
   * @return A new instance of current this {@link ListData} that is of same type.
   */
  public ListData<T> newData() {
    return new ListData<T>(this.type);
  }

  public static <T> ListData<List<T>> newInstance(Class<List<T>> clazz) {
    return new ListData<List<T>>(clazz.getSimpleName());
  }

  /**
   * Marks the current value of the {@link ListData} as the base state that will be used to determine whether its state
   * has changed in {@link #isStateChanged()}.
   */
  public void markBaseState() {
    this.markedBaseValue = this.value;
  }

  /**
   * Restores the value of the {@link ListData} from the marked base state.
   */
  public void restoreBaseState() {
    // TODO: maybe deep copy?
    setValue(this.markedBaseValue);
  }

  /**
   * Returns whether {@link ListData} state (value) has changed after it was marked.
   * 
   * @return whether {@link ListData} state (value) has changed after it was marked.
   */
  public boolean isStateChanged() {
    return !ObjectUtils.equals(this.markedBaseValue, this.value);
  }

  public void setFormElementCtx(FormElementContext<Object, List<T>> feCtx) {
    if (this.feCtx != feCtx) {
      this.feCtx = (FormElement<Object, List<T>>) feCtx;
      setValue(getValue());
    }
  }
}
