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

import java.util.Map;

import org.araneaframework.Viewable;
import org.araneaframework.Widget;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public interface Control extends Widget, Viewable {

  /**
   * Returns control label.
   * 
   * @return control label.
   */
  public String getLabel();

  /**
   * Sets control label.
   * 
   * @param label control label.
   */
  public void setLabel(String label);

  /**
   * * Returns whether the control is mandatory, that is must be inserted by user.
   * 
   * @return whether the control is mandatory, that is must be inserted by user.
   */
  public boolean isMandatory();

  /**
   * Sets whether the control is mandatory, that is must be inserted by user.
   * 
   * @param mandatory whether the control is mandatory, that is must be inserted by user.
   */
  public void setMandatory(boolean mandatory);

  /**
   * Returns whether the control and it's read data is valid.
   * 
   * @return whether the control and it's read data is valid.
   */
  public boolean isValid();

  /**
   * Clears all control errors.
   */
  public void clearErrors();

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
   * Converts and validates the data submitted by the user.
   */
  public void convertAndValidate();
  
  /**
   * Sets whether the control is disabled
   * @param disabled whether the control is disabled
   */
  public void setDisabled(boolean disabled);
  
  /**
   * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
   *
   */
  public interface ViewModel {

    /**
     * Returns attributes.
     * @return attributes.
     */
    public Map getAttributes();

    /**
     * Returns control type.
     * @return control type.
     */
    public String getControlType();

    /**
     * Returns whether the control is mandatory.
     * @return whether the control is mandatory.
     */
    public boolean isMandatory();

    /**
     * Returns control label.
     * @return control label.
     */
    public String getLabel();

    /**
     * Returns whether the control is disabled.
     * @return whether the control is disabled.
     */
    public boolean isDisabled();

  }
}
