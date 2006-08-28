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

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.constraint.BaseConstraint;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.converter.BaseConverter;
import org.araneaframework.uilib.form.converter.ConverterFactory;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;


/**
 * Represents a simple "leaf" form element that holds a control and its data.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class FormElement extends GenericFormElement implements FormElementContext {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  protected Control control;
  protected Converter converter;
  protected Data data;
  
  protected String label;
  
  private boolean rendered = false;
  
  protected boolean mandatory = false;
  protected boolean disabled;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Returns control label.
   * 
   * @return control label.
   */
  public String getLabel() {
    return this.label;
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
   * Returns {@link BaseConverter}.
   * 
   * @return the {@link BaseConverter}.
   */
  public Converter getConverter() {
    return converter;
  }

  /**
   * Sets {@link BaseConverter}.
   * 
   * @param converter The {@link BaseConverter}to set.
   */
  public void setConverter(Converter converter) {
    this.converter = converter;
    
    if (converter != null)
      converter.setFormElementCtx(this);
  }

  /**
   * After setting the constraint sets the constraint field.
   * 
   * @see BaseConstraint#setField(FormElement)
   */
  public void setConstraint(Constraint constraint) {
    super.setConstraint(constraint);
    
    if (constraint != null && constraint instanceof FormElementAware) 
      ((FormElementAware) constraint).setFormElementCtx(this);
  }

  /**
   * Returns {@link Data}.
   * 
   * @return {@link Data}.
   */
  public Data getData() {
    return data;
  }

  /**
   * Sets {@link Data}.
   * 
   * @param data {@link Data}.
   */
  public void setData(Data data) {
    this.data = data;
  }

  /**
   * Returns {@link Control}.
   * 
   * @return {@link Control}.
   */
  public Control getControl() {
    return control;
  }

  /**
   * Sets {@link Control}.
   * 
   * @param control {@link Control}.
   * @throws Exception 
   */
  public void setControl(Control control) throws Exception {
    Assert.notNullParam(control, "control");
    
    this.control = control;
    
    control.setFormElementCtx(this);
    
    if (isInitialized())
      control._getComponent().init(getEnvironment());
  }

  /**
   * Finds a {@link BaseConverter}corresponding to current control and data item.
   * 
   * @throws ConverterNotFoundException if converter cannot be found.
   */
  public Converter findConverter() throws ConverterNotFoundException {
    ConfigurationContext confCtx = 
      (ConfigurationContext) getEnvironment().requireEntry(ConfigurationContext.class);
    return ConverterFactory.getInstance(confCtx).findConverter(
        getControl().getRawValueType(), 
        getData().getValueType(), 
        getEnvironment());
  }

  /**
   * Returns whether the form element was present in the last request.
   * 
   * @return whether the form element was present in the last request.
   */
  public boolean isRead() {
    return (getControl() != null && getControl().isRead());
  }
  
  public void setDisabled(boolean disabled) {
  	this.disabled = disabled;
  }
	
	public boolean isDisabled() {
	  return this.disabled;
	}	  

  public void markBaseState() {
    if (getData() != null)
      getData().markBaseState();
  }
  
  public void restoreBaseState() {
    if (getData() != null)
      getData().restoreBaseState();
  }
  
  public boolean isStateChanged() {
    if (getData() != null)
      return getData().isStateChanged();
    return false;
  }    
  
  public Object getValue() {
    if (getData() != null)
      return data.getValue();
    return null;
  }

  public void setValue(Object value) {
    if (getData() != null)
      getData().setValue(value);
  } 

  public boolean isMandatory() {
    return this.mandatory;
  }
  
  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }
  
  //*********************************************************************
  //* OVERRIDABLE METHODS
  //*********************************************************************

  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
  
  protected void update(InputData input) throws Exception {
    if (isDisabled() || !isRendered()) return;
	  
    super.update(input);
    
    //There is only point to read from request if we have a control
    if (getControl() != null) {
      //Read the control
      getControl()._getWidget().update(input);
    }
  }

  protected void handleProcess() throws Exception {
    super.handleProcess();
	this.rendered = false;
  }
	  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (!isDisabled() && isRendered())
      super.action(path, input, output);
  }

  protected void event(Path path, InputData input) throws Exception {
    if (!path.hasNext() && !isDisabled() && isRendered())
      getControl()._getWidget().event(path, input);
  }

  /**
   * Copies the value from data item to control if data item is valid.
   */
  protected void process() throws Exception {
    if (getControl() != null) {
      if (getData() != null) {
        if (getData().isDirty()) {
          getControl().setRawValue(getConverter().reverseConvert(getData().getValue()));      
        }
        
        getData().setValue(null);
        getData().clean();
      }      

      getControl()._getWidget().process();
    }
    
    super.process();    
  }
  
  protected void handleAction(InputData input, OutputData output) throws Exception {
    //TODO: is it really this way?
    update(input);
    if (control != null)
      control._getService().action(null, input, output);
    // XXX: render state should not be updated on action!!
    boolean savedRenderState = this.rendered;
    process();
    //XXX: quickfix, messy - look for (or create) better place to put rendering state reset instead of process().
    this.rendered = savedRenderState;
  }
  
  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   * @throws Exception 
   */
  public Object getViewModel() throws Exception {
    return new ViewModel();
  }	  
  
  protected void init() throws Exception {
    super.init();
    
    if (getConverter() == null && getData() != null && getControl() != null)
      setConverter(findConverter());
    
    if (getControl() != null) 
      getControl()._getComponent().init(getEnvironment());
  }
  
  protected void destroy() throws Exception {
    if (getControl() != null) 
      getControl()._getComponent().destroy();
  }	  

  /**
   * Uses {@link BaseConverter}to convert the {@link BaseControl}value to the {@link Data}value.
   */
  protected void convertInternal() {  	
  	//It is dangerous to overwrite dirty data
  	if (getData() != null && getData().isDirty()) return;
  	
    //There is only point to convert and set the data if it is present
    if (getData() != null && getControl() != null) {

      getControl().convert();

      //The data should be set only if control is valid
      if (isValid()) {
        //We assume that the convertor is present, if control and data are
        // here
        Object newDataValue = getConverter().convert(getControl().getRawValue());
        getData().setValue(newDataValue);
        getData().clean();
      }
    }

    //Reseting data, if element is not valid.
    if (getData() != null && !isValid()) {
      getData().setValue(null);
      getData().clean();
    }
  }
	
  protected boolean validateInternal() throws Exception {
    if (getControl() != null)
      getControl().validate();
    
    return super.validateInternal();
  }
  
	public void accept(String id, FormElementVisitor visitor) {
		visitor.visit(id, this);
	}
	
	  /**
	   * Returns whether this {@link GenericFormElement} was rendered
	   * in response. Only formelements that were rendered should be read from request.
	   * @return whether this {@link GenericFormElement} was rendered
	   */
	  public boolean isRendered() {
	    return this.rendered;
	  }
	  
	  /**
	   * Marks status of this {@link GenericFormElement} rendered.
	   */
	  public void rendered() {
	    this.rendered = true;
	  }
	
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * Represents a simple form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends GenericFormElement.ViewModel {

    private Control.ViewModel control;
    private String label;
    private boolean valid;
    private Object value;
    protected boolean mandatory;
    
    /**
     * Takes an outer class snapshot.     
     * @throws Exception 
     */
    public ViewModel() throws Exception {
      this.control = (Control.ViewModel) FormElement.this.getControl()._getViewable().getViewModel();
      this.label = FormElement.this.getLabel();
      this.valid = FormElement.this.isValid();
      this.value = FormElement.this.getData() != null ? FormElement.this.getData().getValue() : null;
      this.mandatory = FormElement.this.mandatory;
    }    
    
    /**
     * Returns control.
     * @return control.
     */
    public Control.ViewModel getControl() {
      return this.control;
    }

    /**
     * Returns label.
     * @return label.
     */
    public String getLabel() {
      return this.label;
    }
    
    /**
     * Returns whether the element is valid.
     * @return whether the element is valid.
     */
    public boolean isValid() {
      return valid;
    }
    
    public boolean isMandatory() {
      return this.mandatory;
    }

    public Object getValue() {
      return this.value;
    }
  }
}