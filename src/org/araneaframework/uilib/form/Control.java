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

/**
 * 
 */
package org.araneaframework.uilib.form;

import org.araneaframework.uilib.support.DataType;

import java.io.Serializable;
import org.araneaframework.Scope;
import org.araneaframework.Viewable;
import org.araneaframework.Widget;
import org.araneaframework.uilib.form.control.BaseControl;

/**
 * {@link Control} is the widget that does the actual parsing and reading of the request 
 * parameters. It corresponds to the controls found in HTML forms, like textbox, 
 * textarea, selectbox, button &hellip;
 * 
 * {@link Control} is meant to be used inside {@link FormElement} that provides
 * type safety and additional {@link Constraint}s to request data.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Control<T> extends Widget, Viewable, FormElementAware<T, Object> {
  
  /**
   * Returns whether the control data was present in the HTTP request.
   * 
   * @return whether the control data was present in the HTTP request.
   */
  public boolean isRead();

  /**
   * This method should be overridden by the control, returning the type of the value of this
   * control. It is later used in {@link org.araneaframework.uilib.form.converter.ConverterFactory} to
   * determine the {@link org.araneaframework.uilib.form.converter.BaseConverter} used to transfer the values
   * from {@link org.araneaframework.uilib.form.Data}to control and back.
   * 
   * @return The type of the value of this control as specified in the given <code>DataType</code> object.
   */
  public abstract DataType getRawValueType();

  /**
   * Returns the value of the control (value read from the request). Type of value depends on the
   * type of control.
   * 
   * @return Returns the value of the control (value read from the request).
   */
  public T getRawValue();

  /**
   * Sets the control value. It is usually set by {@link org.araneaframework.uilib.form.Converter} when
   * value of {@link FormElement} (this is stored in {@link Data}) that owns this {@link BaseControl} changes.
   * 
   * @param value control value.
   * @see #getRawValue()
   */
  public void setRawValue(T value);

  /**
   * Converts the data submitted by the user.
   */
  public void convert();
  
  /**
   * Validates the data submitted by the user.
   */
  public void validate();
  
  /** @since 1.1 */
  public boolean isDisabled();
  
  /**
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public interface ViewModel extends Serializable {
    /**
     * Returns control type.
     * @return control type.
     */
    public String getControlType();
    
    public boolean isMandatory();
    
    public String getLabel();
    
    public boolean isDisabled();

    /** @since 1.1 */
    public Scope getScope();
  }
}
