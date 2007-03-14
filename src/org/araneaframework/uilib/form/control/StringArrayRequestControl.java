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

import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;


/**
 * This class is a generalization of controls that have a single <code>String[]</code> request
 * parameter.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public abstract class StringArrayRequestControl extends BaseControl {

  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();
    
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * @param onChangeEventListener {@link OnChangeEventListener} which is called when the control value is changing.
   * @see StandardControlEventListenerAdapter#addOnChangeEventListener(OnChangeEventListener)
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    eventHelper.addOnChangeEventListener(onChangeEventListener);
  }
  
  public void clearOnChangeEventListeners() {
    eventHelper.clearOnChangeEventListeners();
  }
  
  public void setRawValue(Object value) {
  	super.setRawValue(value);
    innerData = getRawValue() != null ? toResponseParameters(getRawValue()) : null;
  }
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
  protected void init() throws Exception {
    super.init();
    
    setGlobalEventListener(eventHelper);
  }
  
  /**
   * This implementation of method {@link #readFromRequest(HttpInputData)}uses the
   * methods {@link #preprocessRequestParameters(String[])}and
   * {@link #fromRequestParameters(String[])}to read the control from request.
   */
  protected void readFromRequest(HttpInputData request) {
    String parameterValues[] = request.getParameterValues(request.getScope().toString());
    innerData = preprocessRequestParameters(parameterValues);
    isReadFromRequest = innerData != null;
  }

  /**
   * Breaks the procedure into two parts: conversion and validation. The conversion is done using
   * method {@link #fromRequestParameters(String[])}and validation using method
   * {@link #validate()}.
   */
  public void convert() {
    if (innerData != null)
      value = fromRequestParameters((String[]) innerData);
    else
      value = null;
  }


  /**
   * Checks that the mandatory is satisfied, and if the value is not <code>null</code> calls the
   * {@link #validateNotNull()}method.
   */
  public void validate() {
    if (isMandatory() && !isRead()) {
      boolean hasValue = (innerData != null && ((String[]) innerData).length > 0 && ((String[]) innerData)[0].trim().length() != 0);
      if (!isDisabled() || (isDisabled() && !hasValue))
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.MANDATORY_FIELD, 
          MessageUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));        
    }

    if (value != null) {
      validateNotNull();
    }
  }


  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }

  /**
   * Copies the value to inner data (output value) if control is valid.
   */
  protected void process() throws Exception {
    //XXX: i am keeping this only b/c it was here before change_153 (async form mods) 
    value = null;
  }
  
  //*********************************************************************
  //* OVERRIDABLE METHODS
  //*********************************************************************

  /**
   * Empty method for overriding. Should contain custom validating logic. This method is called
   * only if the control value is not null.
   */
  protected void validateNotNull() {
    //Empty method for overriding
  }
  
  //*********************************************************************
  //* ABSTRACT METHODS
  //*********************************************************************
  
  /**
   * This method should preprocess the <code>parameterValues</code> and return the processed
   * variant. It may be used to <i>normalize </i> the request making the further parsing of it
   * easier.
   * 
   * @param parameterValues <code>String[]</code>- the values from request.
   * @return the preprocessed values from request.
   */
  protected abstract String[] preprocessRequestParameters(String[] parameterValues);

  /**
   * This method should parse the request parameters (preprocessed with
   * {@link #preprocessRequestParameters(String[])}) an produce the control value.
   * 
   * @param parameterValues the request parameters.
   * @return control value.
   */
  protected abstract Object fromRequestParameters(String[] parameterValues);

  /**
   * This method should return the <code>String[]</code> representation of the control value.
   * 
   * @param controlValue the control value.
   * @return the <code>String[]</code> representation of the control value.
   */
  protected abstract String[] toResponseParameters(Object controlValue);	 	  
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************  
  
  /**
   * Represents a view model of a control with a single array request parameter.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends BaseControl.ViewModel {

    private String[] values;
    private boolean hasOnChangeEventListeners;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.values = (String[]) innerData;
      this.hasOnChangeEventListeners = eventHelper.hasOnChangeEventListeners();      
    }         
    
    
    /**
     * Returns control values.
     * @return control values.
     */
    public String[] getValues() {
      return values;
    }

    /**
     * Returns the first of control values.
     * @return the first of control values.
     */
    public String getSimpleValue() {
      return values != null ? values[0] : null;
    }
    
    
    /**
     * Returns whether there are registered <code>onChange</code> events.
     * @return whether there are registered <code>onChange</code> events.
     */
    public boolean isOnChangeEventRegistered() {
      return hasOnChangeEventListeners;
    } 
        
  }  
}
