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

import java.math.BigDecimal;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * This class represents a textbox control that accepts only valid 
 * floating-point numbers.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FloatControl extends EmptyStringNullableControl {
  
  private BigDecimal minValue;
  private BigDecimal maxValue;

  /**
   * Empty.
   */
  public FloatControl() {
    //Empty
  }    
  
  /**
   * Makes a float control that has minimum and maximum value.
   * 
   * @param minValue minimum permitted value.
   * @param maxValue maximum permitted value.
   */
  public FloatControl(BigDecimal minValue, BigDecimal maxValue) {
    super();
    this.minValue = minValue;
    this.maxValue = maxValue;
  }    

  /**
   * Sets the maximum value.
   * @param maxValue maximum value.
   */
  public void setMaxValue(BigDecimal maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the minimum value.
   * @param minValue minimum value.
   */
  public void setMinValue(BigDecimal minValue) {
    this.minValue = minValue;
  }

  /**
   * Returns the maxValue.
   * @return the maxValue.
   */
  public BigDecimal getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the minValue.
   * @return the minValue.
   */
  public BigDecimal getMinValue() {
    return minValue;
  }
  
  /**
   * Returns "BigDecimal".
   * @return "BigDecimal".
   */
  public String getRawValueType() {
    return "BigDecimal";
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
   * Checks that the submitted data is a valid floating-point number.
   * 
   */
  protected Object fromRequest(String parameterValue) {
    BigDecimal result = null;
    
    try {
      result = new BigDecimal(parameterValue);
    }
    catch (NumberFormatException e) {
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.NOT_A_NUMBER, 
          ErrorUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));          
    }
    
    return result;
  }

  /**
   * 
   */
  protected String toResponse(Object controlValue) {
    return ((BigDecimal) controlValue).toString();
  }
  
  /**
   * Checks that the submitted value is in permitted range.
   * 
   */
  protected void validateNotNull() {        
    if (minValue != null && maxValue != null && ((((BigDecimal) value).compareTo(minValue) == -1) || ((BigDecimal) value).compareTo(maxValue) == 1)) {
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_BETWEEN, 
          new Object[] {
              ErrorUtil.localize(getLabel(), getEnvironment()),
              minValue.toString(),
              maxValue.toString()
          },          
          getEnvironment()));           
    }      
    else if (minValue != null && ((BigDecimal) value).compareTo(minValue) == -1) {      
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_GREATER, 
          new Object[] {
              ErrorUtil.localize(getLabel(), getEnvironment()),
              minValue.toString(),
          },          
          getEnvironment()));       
    }    
    else if (maxValue != null && ((BigDecimal) value).compareTo(maxValue) == 1) {
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.NUMBER_NOT_LESS, 
          new Object[] {
              ErrorUtil.localize(getLabel(), getEnvironment()),
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
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
   * 
   */
  public class ViewModel extends StringArrayRequestControl.ViewModel {
    
    private BigDecimal maxValue;
    private BigDecimal minValue;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.maxValue = FloatControl.this.getMaxValue();
      this.minValue = FloatControl.this.getMinValue();
    }       
    
    /**
     * Returns maximum permitted value.
     * @return maximum permitted value.
     */
    public BigDecimal getMaxValue() {
      return this.maxValue;
    }
    
    /**
     * Returns minimum permitted value.
     * @return minimum permitted value.
     */
    public BigDecimal getMinValue() {
      return this.minValue;
    }  
  }
}
