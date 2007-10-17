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

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElementContext;


/**
 * This class is a control generalization that provides methods common to all HTML form controls.
 * The methods include XML output and error handling.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public abstract class BaseControl extends BaseApplicationWidget implements java.io.Serializable, Control {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  
  protected Object value;
  protected Object innerData;
  protected boolean isReadFromRequest = false;

  private FormElementContext feCtx;
  
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

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
   * Sets the raw control value (as it was read from request/written to response).
   * It is usually set by {@link org.araneaframework.uilib.form.Converter} when
   * value of {@link FormElement} that owns this {@link BaseControl} changes.
   * 
   * @param value control value.
   * @see #getRawValue()
   */
  public void setRawValue(Object value) {   
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
  
  public void setFormElementCtx(FormElementContext formElementContext) {
    this.feCtx = formElementContext;
  }
  
  public FormElementContext getFormElementCtx() {
    return feCtx;
  }
  
  /**
   * By default the control is considered read if it has a not null data read from request.
   */
  public boolean isRead() {
    return isReadFromRequest;
  }
  
  //*********************************************************************
  //* OVERRIDABLE METHODS
  //*********************************************************************  
  
  public void convertAndValidate() {
    convert();
    validate();
  }
  
  public void convert() {
  }
  
  public void validate() {
  }
  
  protected void readFromRequest(HttpInputData request) {}
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  
  
  protected void init() throws Exception {
    super.init();
    
    Assert.notNull(this, getFormElementCtx(), "Form element context must be assigned to the control before it can be initialized! " +
        "Make sure that the control is associated with a form element!");
    
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (!isDisabled())
      super.action(path, input, output);
  }

  protected void update(InputData input) throws Exception {
    super.update(input);
    
    if (!isDisabled())
      readFromRequest((HttpInputData) input);
  }
  
  protected void handleEvent(InputData input) throws Exception {
    if (!isDisabled())
      super.handleEvent(input);
  }
  
  /**
   * Returns control label.
   * 
   * @return control label.
   */
  protected String getLabel() {
    return feCtx.getLabel();
  }

  /**
   * * Returns whether the control is mandatory, that is must be inserted by user.
   * 
   * @return whether the control is mandatory, that is must be inserted by user.
   */
  protected boolean isMandatory() {
    return feCtx.isMandatory();
  }
  
  protected void addError(String error) {
    feCtx.addError(error);
  }
  
  /**
   * Returns whether the control is disabled.
   * @return whether the control is disabled
   */
  protected boolean isDisabled() {
    return feCtx.isDisabled();
  }
  
  protected boolean isValid() {
    return feCtx.isValid();
  }

  public Widget.Interface _getWidget() {
	  return new WidgetImpl();
  }

  protected class WidgetImpl extends BaseWidget.WidgetImpl {
    public void update(InputData input) {
    	isReadFromRequest = false;
		super.update(input);
	}
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * Represents a general control view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   */
  public class ViewModel implements Control.ViewModel {
    protected String controlType;
    protected boolean mandatory;
    protected boolean disabled;
    protected String label;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      String className = BaseControl.this.getClass().getName();
      // Recognizes Controls that are defined as (anonymous) nested classes.
      // Prior to 1.5 getDeclaringClass() does not exist, so just look for '$'.
      if (className.indexOf('$') != -1)
        className = BaseControl.this.getClass().getSuperclass().getName();
      className = className.substring(className.lastIndexOf(".") + 1);
      this.controlType = className;
      
      this.mandatory = BaseControl.this.isMandatory();
      this.disabled = BaseControl.this.isDisabled();
      
      this.label = BaseControl.this.getLabel();
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
