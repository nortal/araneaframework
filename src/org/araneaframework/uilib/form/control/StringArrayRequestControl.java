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

package org.araneaframework.uilib.form.control;

import org.apache.commons.lang.StringUtils;

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
 */
public abstract class StringArrayRequestControl extends BaseControl {

  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  /**
   * Registers a new <code>onChangeEventListener</code> to this control.
   * 
   * @param onChangeEventListener An {@link OnChangeEventListener}, which is called when the control
   *          value is changing.
   * @see StandardControlEventListenerAdapter#addOnChangeEventListener(OnChangeEventListener)
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    this.eventHelper.addOnChangeEventListener(onChangeEventListener);
  }

  /**
   * Removes all <code>onChangeEventListener</code>s that are registered to this control.
   * 
   * @see StandardControlEventListenerAdapter#clearOnChangeEventListeners()
   */
  public void clearOnChangeEventListeners() {
    this.eventHelper.clearOnChangeEventListeners();
  }

  public void setRawValue(Object value) {
    super.setRawValue(value);
    this.innerData = getRawValue() != null ? toResponseParameters(getRawValue()) : null;
  }

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************

  protected void init() throws Exception {
    super.init();
    setGlobalEventListener(this.eventHelper);
  }

  /**
   * This implementation of method {@link #readFromRequest(HttpInputData)}uses the
   * methods {@link #preprocessRequestParameters(String[])}and
   * {@link #fromRequestParameters(String[])}to read the control from request.
   */
  protected void readFromRequest(HttpInputData request) {
    String parameterValues[] = request.getParameterValues(getScope().toString());
    this.innerData = preprocessRequestParameters(parameterValues);
    this.isReadFromRequest = this.innerData != null;
  }

  /**
   * Breaks the procedure into two parts: conversion and validation. The conversion is done using
   * method {@link #fromRequestParameters(String[])}and validation using method {@link #validate()}.
   */
  public void convert() {
    this.value = this.innerData != null ? fromRequestParameters((String[]) this.innerData) : null;
  }

  /**
   * Checks that the mandatory is satisfied, and if the value is not <code>null</code> calls the
   * {@link #validateNotNull()}method.
   */
  public void validate() {
    if (isMandatory() && !isRead()) {
      String[] values = (String[]) this.innerData;
      boolean hasValue = values != null && values.length > 0 && !StringUtils.isBlank(values[0]);

      if (!isDisabled() || (isDisabled() && !hasValue)) {
        addError(MessageUtil.localizeAndFormat(UiLibMessages.MANDATORY_FIELD,
            MessageUtil.localize(getLabel(), getEnvironment()), getEnvironment()));
      }
    }

    if (getRawValue() != null) {
      validateNotNull();
    }
  }

  /**
   * Returns the <code>ViewModel</code> for rendering phase by providing the data through the
   * <code>ViewModel</code>.
   * 
   * @return The {@link ViewModel} of this control.
   */
  public Object getViewModel() {
    return new ViewModel();
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
   * @param parameterValues The <code>String[]</code> values from request.
   * @return The preprocessed values from request.
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
   * @param controlValue The control value.
   * @return The <code>String[]</code> representation of the control value.
   */
  protected abstract String[] toResponseParameters(Object controlValue);	 	  

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************  

  /**
   * Represents a view model of a control with a single array request parameter.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
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
     * 
     * @return control values.
     */
    public String[] getValues() {
      return this.values;
    }

    /**
     * Returns the first value from control values.
     * 
     * @return the first value from control values.
     */
    public String getSimpleValue() {
      return this.values != null ? this.values[0] : null;
    }

    /**
     * Returns whether there are registered <code>onChange</code> events.
     * 
     * @return whether there are registered <code>onChange</code> events.
     */
    public boolean isOnChangeEventRegistered() {
      return this.hasOnChangeEventListeners;
    }

  }
}
