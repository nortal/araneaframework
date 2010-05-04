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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This class is a generalization of controls that have a single <code>String[]</code> request parameter.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class StringArrayRequestControl<T> extends BaseControl<T> {

  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();

  /**
   * @param onChangeEventListener {@link OnChangeEventListener} which is called when the control value is changing.
   * @see StandardControlEventListenerAdapter#addOnChangeEventListener(OnChangeEventListener)
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    this.eventHelper.addOnChangeEventListener(onChangeEventListener);
  }

  public void clearOnChangeEventListeners() {
    this.eventHelper.clearOnChangeEventListeners();
  }

  @Override
  public void setRawValue(T value) {
    super.setRawValue(value);
    this.innerData = getRawValue() != null ? toResponseParameters(getRawValue()) : null;
  }

  @Override
  protected void init() throws Exception {
    super.init();
    setGlobalEventListener(this.eventHelper);
  }

  /**
   * This implementation of method {@link #readFromRequest(HttpInputData)}uses the methods
   * {@link #preprocessRequestParameters(String[])}and {@link #fromRequestParameters(String[])}to read the control from
   * request.
   * 
   * @param request The request data containing the parameters.
   */
  @Override
  protected void readFromRequest(HttpInputData request) {
    String parameterValues[] = request.getParameterValues(getScope().toString());
    this.innerData = preprocessRequestParameters(parameterValues);
    this.isReadFromRequest = this.innerData != null;
  }

  /**
   * Breaks the procedure into two parts: conversion and validation. The conversion is done using method
   * {@link #fromRequestParameters(String[])}and validation using method {@link #validate()}.
   */
  @Override
  public void convert() {
    this.value = this.innerData != null ? fromRequestParameters((String[]) this.innerData) : null;
  }

  /**
   * Checks that the mandatory is satisfied, and if the value is not <code>null</code> calls the
   * {@link #validateNotNull()}method.
   */
  @Override
  public void validate() {
    if (isRead()) { // It means possible value change.

      String[] data = (String[]) this.innerData;
      boolean hasValue = data != null && data.length > 0 && StringUtils.isNotBlank(data[0]);

      if (!hasValue && isMandatory()) { // Check the value for mandatory controls.

        addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.MANDATORY_FIELD, MessageUtil.localize(
            getLabel(), getEnvironment())));

      } else if (getRawValue() != null) { // Check against converted value
        validateNotNull();
      }
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  // *********************************************************************
  // * OVERRIDABLE METHODS
  // *********************************************************************

  /**
   * Empty method for overriding. Should contain custom validating logic. This method is called only if the control
   * value is not null.
   */
  protected void validateNotNull() {
    // Empty method for overriding
  }

  // *********************************************************************
  // * ABSTRACT METHODS
  // *********************************************************************

  /**
   * This method should preprocess the <code>parameterValues</code> and return the processed variant. It may be used to
   * <i>normalize </i> the request making the further parsing of it easier.
   * 
   * @param parameterValues <code>String[]</code>- the values from request.
   * @return the preprocessed values from request.
   */
  protected abstract String[] preprocessRequestParameters(String[] parameterValues);

  /**
   * This method should parse the request parameters (preprocessed with {@link #preprocessRequestParameters(String[])})
   * and produce the control value.
   * 
   * @param parameterValues A not <code>null</code> array of request parameters.
   * @return The control value.
   */
  protected abstract T fromRequestParameters(String[] parameterValues);

  /**
   * This method should return the <code>String[]</code> representation of the control value.
   * 
   * @param controlValue The not <code>null</code> control value to convert into <code>String</code>.
   * @return the <code>String[]</code> representation of the control value.
   */
  protected abstract String[] toResponseParameters(T controlValue);

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Represents a view model of a control with a single array request parameter.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends BaseControl<T>.ViewModel {

    private String[] values;

    private boolean hasOnChangeEventListeners;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.values = (String[]) StringArrayRequestControl.this.innerData;
      this.hasOnChangeEventListeners = StringArrayRequestControl.this.eventHelper.hasOnChangeEventListeners();
    }

    /**
     * Returns control values as an array of values, or as <code>null</code> when no values exist.
     * 
     * @return Control values array, or <code>null</code>.
     */
    public String[] getValues() {
      return this.values;
    }

    /**
     * Returns the first of control values, or <code>null</code> when no values currently exist.
     * 
     * @return the first of control values, or <code>null</code>.
     */
    public String getSimpleValue() {
      return !ArrayUtils.isEmpty(this.values) ? this.values[0] : null;
    }

    /**
     * Returns whether there are registered <code>onChange</code> events.
     * 
     * @return whether there are registered <code>onChange</code> events.
     */
    public boolean isOnChangeEventRegistered() {
      return this.hasOnChangeEventListeners;
    }

    /**
     * Provides whether the given value is a the value of the control.
     * 
     * @param value The value to check.
     * @return <code>true</code> when the given value is the value of this control.
     */
    public boolean containsValue(String value) {
      return ArrayUtils.contains(this.values, value);
    }
  }
}
