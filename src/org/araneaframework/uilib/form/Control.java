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

/**
 * 
 */
package org.araneaframework.uilib.form;

import org.araneaframework.Viewable;
import org.araneaframework.Widget;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public interface Control extends Widget, Viewable, FormElementAware {
  
  /**
   * Returns whether the control data was present in the HTTP request.
   * 
   * @return whether the control data was present in the HTTP request.
   */
  public boolean isRead();

  /**
   * This method should be overriden by the control, returning the type of the value of this
   * control. It is later used in {@link org.araneaframework.uilib.form.converter.ConverterFactory}to
   * determine the {@link org.araneaframework.uilib.form.converter.BaseConverter}used to transfer the values
   * from {@link org.araneaframework.uilib.form.Data}to control and back.
   * 
   * @return the type of the value of this control
   */
  public abstract String getRawValueType();

  /**
   * Returns the value of the control (value read from the request). Type of value depends on the
   * type of control.
   * 
   * @return Returns the value of the control (value read from the request).
   */
  public Object getRawValue();

  /**
   * Sets the control value. Preffered way to set it is using the DataItem.
   * 
   * @param value control value.
   * @see #getRawValue()
   */
  public void setRawValue(Object value);

  /**
   * Converts the data submitted by the user.
   */
  public void convert();
  
  /**
   * Validates the data submitted by the user.
   */
  public void validate();
  
  /**
   * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
   *
   */
  public interface ViewModel {

    /**
     * Returns control type.
     * @return control type.
     */
    public String getControlType();
    
    public boolean isMandatory();
    
    public String getLabel();
    
    public boolean isDisabled();

  }
}
