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
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;


/**
 * This class represents a textbox control that accepts only valid 
 * integer numbers.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class NumberControl extends EmptyStringNullableControl {
  
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
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Trims request parameter.
   */
  protected String preprocessRequestParameter(String parameterValue) {
    String result = super.preprocessRequestParameter(parameterValue);
    return (result == null ? null : result.trim());
  }  
  
  /**
   * Checks that the submitted data is a valid integer number.
   */
  protected Object fromRequest(String parameterValue) {
    BigInteger result = null;
    
    try {
      result = parameterValue == null ? null : new BigInteger(parameterValue);
    }
    catch (NumberFormatException e) {
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NOT_A_NUMBER, 
          MessageUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));             
    }
    
    return result;
  }

  /**
   * 
   */
  protected String toResponse(Object controlValue) {
    return ((BigInteger) controlValue).toString();
  }
  
  /**
   * Checks that the submitted value is in permitted range.
   * 
   */
  protected void validateNotNull() {    
    if (minValue != null && maxValue != null && ((((BigInteger) value).compareTo(minValue) == -1) || ((BigInteger) value).compareTo(maxValue) == 1)) {      
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
    else if (minValue != null && ((BigInteger) value).compareTo(minValue) == -1) {      
      addError(
          MessageUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_GREATER, 
          new Object[] {
              MessageUtil.localize(getLabel(), getEnvironment()),
              minValue.toString(),
          },          
          getEnvironment()));    
    }    
    else if (maxValue != null && ((BigInteger) value).compareTo(maxValue) == 1) {      
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
  public Object getViewModel() {
    return new ViewModel();
  }	  
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends StringArrayRequestControl.ViewModel {

    private BigInteger maxValue;
    private BigInteger minValue;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.maxValue = NumberControl.this.getMaxValue();
      this.minValue = NumberControl.this.getMinValue();
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
  }  
}
