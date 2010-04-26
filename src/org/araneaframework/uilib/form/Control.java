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
import org.araneaframework.Scope;
import org.araneaframework.Viewable;
import org.araneaframework.Widget;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.support.DataType;

/**
 * {@link Control} is the widget that does the actual parsing and reading of the request parameters. It corresponds to
 * the controls found in HTML forms, like textbox, textarea, select-box, button &hellip;
 * 
 * {@link Control} is meant to be used inside {@link FormElement} that provides type safety and additional
 * {@link Constraint}s to request data.
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
   * This method should be overridden by the control, returning the type of the value of this control. It is later used
   * in {@link org.araneaframework.uilib.form.converter.ConverterFactory} to determine the
   * {@link org.araneaframework.uilib.form.converter.BaseConverter} used to transfer the values from
   * {@link org.araneaframework.uilib.form.Data}to control and back.
   * 
   * @return The type of the value of this control as specified in the given <code>DataType</code> object.
   */
  public abstract DataType getRawValueType();

  /**
   * Returns the value of the control (value read from the request). Type of value depends on the type of control.
   * 
   * @return Returns the value of the control (value read from the request).
   */
  public T getRawValue();

  /**
   * Sets the control value. It is usually set by {@link org.araneaframework.uilib.form.Converter} when value of
   * {@link FormElement} (this is stored in {@link Data}) that owns this {@link BaseControl} changes.
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

  /**
   * Provides whether this control is disabled. Disabled controls do not process incoming requests.
   * 
   * @return A boolean that is <code>true</code> when this control is disabled.
   * @since 1.1
   */
  public boolean isDisabled();

  /**
   * The view model for a control. The view model is a snapshot of control data that will be used for rendering it.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public interface ViewModel extends Serializable {

    /**
     * Provides the control's type, which is basically the control's simple class name.
     * 
     * @return The control's type.
     */
    public String getControlType();

    /**
     * Provides whether filling in this control with a non-empty value is mandatory for the user to reach a valid form.
     * 
     * @return A boolean that is <code>true</code> when this control is mandatory.
     */
    public boolean isMandatory();

    /**
     * Provides the label ID that can be used resolve a label for control.
     * 
     * @return The control's label ID.
     */
    public String getLabel();

    /**
     * Provides whether this control is disabled. Disabled controls do not process incoming requests.
     * 
     * @return A boolean that is <code>true</code> when this control is disabled.
     */
    public boolean isDisabled();

    /**
     * Provides the scope of this control. The latter can be used to uniquely identify the control's location in the
     * internal hierarchy of current Aranea.
     * 
     * @return The scope object of this control.
     * @since 1.1
     */
    public Scope getScope();
  }
}
