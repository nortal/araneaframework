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

import java.math.BigInteger;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;


/**
 * This class represents a textbox control that accepts only valid 
 * integer numbers.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class NumberControl extends EmptyStringNullableControl<BigInteger> implements FilteredInputControl<BigInteger> {
  private InputFilter inputFilter;
  private BigInteger minValue;
  private BigInteger maxValue;

  /**
   * Empty.
   */
  public NumberControl() {
    //Empty
  }
  
  /**
   * Makes a number control that has minimum and maximum value.
   * 
   * @param minValue minimum permitted value.
   * @param maxValue maximum permitted value.
   */
  public NumberControl(BigInteger minValue, BigInteger maxValue) {
    super();
    this.minValue = minValue;
    this.maxValue = maxValue;
  }  

  /**
   * Sets the maximum permitted value.
   * @param maxValue maximum permitted value.
   */
  public void setMaxValue(BigInteger maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minimum permitted value.
   * @param minValue minimum permitted value.
   */
  public void setMinValue(BigInteger minValue) {
    this.minValue = minValue;
  }

  /**
   * Returns the maximum permitted value.
   * @return the maximum permitted value.
   */
  public BigInteger getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the minimum permitted value.
   * @return the minimum permitted value.
   */
  public BigInteger getMinValue() {
    return minValue;
  }
  
  /**
   * Returns "BigInteger".
   * @return "BigInteger".
   */
  public String getRawValueType() {
    return "BigInteger";
  }
  
  /** @since 1.0.11 */
  public InputFilter getInputFilter() {
    return inputFilter;
  }
  
  /** @since 1.0.11 */
  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Trims request parameter.
   */
  @Override
  protected String preprocessRequestParameter(String parameterValue) {
    String result = super.preprocessRequestParameter(parameterValue);
    return (result == null ? null : result.trim());
  }  
  
  /**
   * Checks that the submitted data is a valid integer number.
   */
  @Override
  protected BigInteger fromRequest(String parameterValue) {
    BigInteger result = null;
    
    try {
      result = parameterValue == null ? null : new BigInteger(parameterValue);
    }
    catch (NumberFormatException e) {
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NOT_INTEGER, 
          MessageUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));             
    }
    
    if (getInputFilter() != null && !StringUtils.containsOnly(parameterValue, getInputFilter().getCharacterFilter())) {
    	addError(
    		MessageUtil.localizeAndFormat(
    		getInputFilter().getInvalidInputMessage(), 
    		MessageUtil.localize(getLabel(), getEnvironment()), 
    		getInputFilter().getCharacterFilter(), 
    		getEnvironment()));
    }
    
    return result;
  }

  @Override
  protected <E extends BigInteger> String toResponse(E controlValue) {
    return controlValue.toString();
  }
  
  /**
   * Checks that the submitted value is in permitted range.
   * 
   */
  @Override
  protected void validateNotNull() {    
    if (minValue != null && maxValue != null && ((getRawValue().compareTo(minValue) == -1) || getRawValue().compareTo(maxValue) == 1)) {      
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_BETWEEN, 
          new Object[] {
              MessageUtil.localize(getLabel(), getEnvironment()),
              minValue.toString(),
              maxValue.toString()
          },          
          getEnvironment()));     
    }      
    else if (minValue != null && getRawValue().compareTo(minValue) == -1) {      
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_GREATER, 
          new Object[] {
              MessageUtil.localize(getLabel(), getEnvironment()),
              minValue.toString(),
          },          
          getEnvironment()));    
    }    
    else if (maxValue != null && getRawValue().compareTo(maxValue) == 1) {      
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_LESS, 
          new Object[] {
              MessageUtil.localize(getLabel(), getEnvironment()),
              maxValue.toString(),
          },          
          getEnvironment()));  
    }
  }
  
  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }	  
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends StringArrayRequestControl<BigInteger>.ViewModel {
    private InputFilter inputFilter;
    private BigInteger maxValue;
    private BigInteger minValue;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.maxValue = NumberControl.this.getMaxValue();
      this.minValue = NumberControl.this.getMinValue();
      this.inputFilter = NumberControl.this.getInputFilter();
    }       
    
    /**
     * Returns maximum permitted value.
     * @return maximum permitted value.
     */
    public BigInteger getMaxValue() {
      return this.maxValue;
    }
    
    /**
     * Returns minimum permitted value.
     * @return minimum permitted value.
     */
    public BigInteger getMinValue() {
      return this.minValue;
    }

    public InputFilter getInputFilter() {
      return inputFilter;
    }
  }  
}
