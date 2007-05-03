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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.converter.BaseConverter;
import org.araneaframework.uilib.form.converter.ConverterFactory;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.Event;

/**
 * Represents a simple "leaf" form element that holds a {@link Control} and its {@link Data}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FormElement extends GenericFormElement implements FormElementContext {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private List initEvents = new ArrayList();
	
  protected Control control;
  protected Converter converter;
  protected Data data;
  
  protected String label;
  
  private boolean rendered = false;
  private boolean ignoreEvents = true;
  
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
    data.setFormElementCtx(this);
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
      control._getComponent().init(getScope(), getEnvironment());
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
    if (isDisabled() || !isRendered()) {
    	setIgnoreEvents(true);
    	return;
    }
    setIgnoreEvents(false);

    super.update(input);
    
    //There is only point to read from request if we have a control
    if (getControl() != null) {
      //Read the control
      getControl()._getWidget().update(input);
    }
    
    rendered = false;
  }
	  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (!isDisabled() && isRendered()) {
      super.action(path, input, output);
    }
  }

  protected void event(Path path, InputData input) throws Exception {
    if (!path.hasNext() && !isDisabled() && !isIgnoreEvents())
      getControl()._getWidget().event(path, input);
  }
  
  protected void handleAction(InputData input, OutputData output) throws Exception {
    update(input);
    this.rendered = true;
    if (control != null)
      control._getService().action(null, input, output);
  }
  
  public Environment getConstraintEnvironment() {
	return new StandardEnvironment(super.getConstraintEnvironment(), FormElementContext.class, this);
  }

  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   * @throws Exception 
   */
  public Object getViewModel() throws Exception {
    return new ViewModel();
  }
  
  /** @since 1.0.5 */
  public void addInitEvent(Event event) {
    if (isAlive()) {
      event.run();
    } else if (!isInitialized()) {
      if (initEvents == null)
        initEvents = new ArrayList();
      initEvents.add(event);
    }
  }
  
  protected void init() throws Exception {
    super.init();
    
    if (getConverter() == null && getData() != null && getControl() != null)
      setConverter(findConverter());
    
    if (getControl() != null) 
      getControl()._getComponent().init(getScope(), getEnvironment());

    runInitEvents();
  }
  
  protected void destroy() throws Exception {
    if (getControl() != null) 
      getControl()._getComponent().destroy();
  }	  

  /**
   * Uses {@link BaseConverter}to convert the {@link BaseControl}value to the {@link Data}value.
   */
  protected void convertInternal() {
    if (!isAlive())
      return;
    
    Object newDataValue = null;

    //There is only point to convert and set the data if it is present
    if (getData() != null && getControl() != null) {

      getControl().convert();

      //The data should be set only if control is valid
      if (isValid()) {
        //We assume that the convertor is present, if control and data are
        // here
        newDataValue = getConverter().convert(getControl().getRawValue());
      }
    }

    if (getData() != null && isValid()) {
      getData().setValue(newDataValue);
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
	
  /** Called from {@link FormElement#init()} to run queued events.
   * @since 1.0.5 */
  protected void runInitEvents() {
    if (initEvents != null) {
      for (Iterator it = initEvents.iterator(); it.hasNext();) {
        Runnable event = (Runnable) it.next();
        event.run();
      }
    }
    initEvents = null;
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

    /**
     * @since 1.1
     */
    protected boolean isIgnoreEvents() {
      return ignoreEvents;
    }

    /**
     * @since 1.1
     */
    protected void setIgnoreEvents(boolean ignoreEvents) {
      this.ignoreEvents = ignoreEvents;
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