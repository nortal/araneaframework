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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * This class is a control generalization that provides methods common to all HTML form controls.
 * The methods include XML output and error handling.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public abstract class BaseControl extends StandardPresentationWidget implements java.io.Serializable, Control {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  
  protected Object value;
  protected Object innerData;
  
  protected boolean mandatory = false;
  protected boolean dirty = false;
  protected boolean disabled;
  
  protected String label;

  private Set errors = new HashSet();
  
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Returns control label.
   * 
   * @return control label.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets control label.
   * 
   * @param label control label.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * * Returns whether the control is mandatory, that is must be inserted by user.
   * 
   * @return whether the control is mandatory, that is must be inserted by user.
   */
  public boolean isMandatory() {
    return this.mandatory;
  }

  /**
   * Sets whether the control is mandatory, that is must be inserted by user.
   * 
   * @param mandatory whether the control is mandatory, that is must be inserted by user.
   */
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  /**
   * Returns whether the control and it's read data is valid.
   * 
   * @return whether the control and it's read data is valid.
   */
  public boolean isValid() {
    return errors.size() == 0;
  }
  
  public void addError(String error) {
    errors.add(ErrorUtil.showError(error, getEnvironment()));
  }

  /**
   * Clears all control errors.
   */
  public void clearErrors() {
    errors.clear();   
  }
  
  /**
   * Returns the value of the control (value read from the request). Type of value depends on the
   * type of control.
   * 
   * @return Returns the value of the control (value read from the request).
   */
  public Object getRawValue() {
    return value;
  }

  /**
   * Sets the control value. Preffered way to set it is using the DataItem.
   * 
   * @param value control value.
   * @see #getRawValue()
   */
  public void setRawValue(Object value) {   
    dirty = true;
    BaseControl.this.value = value;
  }

  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   * @throws Exception 
   */
  public Object getViewModel() throws Exception {
    return new ViewModel();
  }
  
  /**
   * Sets whether the control is disabled
   * @param disabled whether the control is disabled
   */
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }
  
  /**
   * Returns whether the control is disabled.
   * @return whether the control is disabled
   */
  public boolean isDisabled() {
    return disabled;
  }
  
  //*********************************************************************
  //* ABSTRACT METHODS
  //*********************************************************************

  /**
   * Returns whether the control data was present in the HTTP request.
   * 
   * @return whether the control data was present in the HTTP request.
   */
  public abstract boolean isRead();  
  
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
   * Converts the control value from the one read from request to the one required by the control
   * value type.
   */
  public abstract void convertAndValidate();   
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  
  
  protected void readFromRequest(String controlName, HttpServletRequest request) {}
  
  protected void update(InputData input) throws Exception {
    super.update(input);
    
    if (!disabled)
      readFromRequest(input.getScope().toString(), ((ServletInputData) input).getRequest());
  }
  
  protected void handleEvent(InputData input) throws Exception {
    if (!disabled)
      super.handleEvent(input);
  }

public Widget.Interface _getWidget() {
    return new WidgetImpl();
  }
  
  protected class WidgetImpl extends BaseWidget.WidgetImpl {
    public void update(InputData input) throws Exception {
      clearErrors();
      
      super.update(input);
      
      dirty = false;
    }
    
    public void process() throws Exception {
      if (!dirty) return; 
      
      super.process();
    }
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * Represents a general control view model.
   * 
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
   * 
   */
  public class ViewModel implements Control.ViewModel {
    
    protected Map attributes;
    protected String controlType;
    protected boolean mandatory;
    protected boolean disabled;
    protected String label;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      String className = BaseControl.this.getClass().getName();
      className = className.substring(className.lastIndexOf(".") + 1);      
      this.controlType = className;
      
      this.mandatory = BaseControl.this.mandatory;
      this.disabled = BaseControl.this.disabled;
      
      this.label = BaseControl.this.getLabel();
    }
    
    /**
     * Returns attributes.
     * @return attributes.
     */
    public Map getAttributes() {
      return attributes;
    }
    
    /**
     * Returns control type.
     * @return control type.
     */
    public String getControlType() {
      return controlType;     
    }
    
    /**
     * Returns whether the control is mandatory.
     * @return whether the control is mandatory.
     */
    public boolean isMandatory() {
      return mandatory;
    }
    
    /**
     * Returns control label.
     * @return control label.
     */
    public String getLabel() {
      return label;
    }
    /**
     * Returns whether the control is disabled.
     * @return whether the control is disabled.
     */
    public boolean isDisabled() {
      return disabled;
    }
  }
}
