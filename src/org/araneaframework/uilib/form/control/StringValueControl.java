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

import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * This class represents controls, that have a value of type <code>String</code>
 * and a single request parameter of same type. 
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public abstract class StringValueControl extends EmptyStringNullableControl {
  
  private Long minLength;
  private Long maxLength;
  
  private boolean trimValue = false;

  /**
   * Sets the maximum length.
   * @param maxLength maximum length.
   */
  public void setMaxLength(Long maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * Sets the minimum length.
   * @param minLength minimum length.
   */
  public void setMinLength(Long minLength) {
    this.minLength = minLength;
  }
  
  /**
   * Sets whether the value from request will be trimmed.
   * @param trimValue whether the value from request will be trimmed.
   */
	public void setTrimValue(boolean trimValue) {
		this.trimValue = trimValue;
	}
	
  /**
   * Returns "String".
   * @return "String".
   */
  public String getRawValueType() {
    return "String";
  }  
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
  
  /**
   * Direct copy.
   */
  protected Object fromRequest(String parameterValue) {
  	if (parameterValue != null && trimValue)
  		return parameterValue.trim();
  	
    return parameterValue;
  }

  /**
   * Direct copy.
   */
  protected String toResponse(Object controlValue) {
    return (String) controlValue;
  }
  
  /**
   * Checks that the value (<code>String</code>) length is between the given values.
   */
  protected void validateNotNull() {   
    if (minLength != null && ((String) value).length() < minLength.longValue()) {      
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.STRING_TOO_SHORT, 
          ErrorUtil.localize(getLabel(), getEnvironment()),
          minLength.toString(),
          getEnvironment()));        
    }
    
    if (maxLength != null && ((String) value).length() > maxLength.longValue()) {  
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.STRING_TOO_LONG, 
          ErrorUtil.localize(getLabel(), getEnvironment()),
          maxLength.toString(),
          getEnvironment()));          
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

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************  	
  
  /**
   * @author <a href="mailto:olegm@webmedia.ee">Oleg Mürk</a>
   */
  public class ViewModel extends StringArrayRequestControl.ViewModel {
 
    private Long minLength;
    private Long maxLength;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.minLength = StringValueControl.this.minLength;
      this.maxLength = StringValueControl.this.maxLength;
    }         
        
    
    /**
     * Returns the minimum length.
     * @return the minimum length.
     */
    public Long getMinLength() {
      return minLength;
    }
    
    /**
     * Returns the maximum length.
     * @return the maximum length.
     */
    public Long getMaxLength() {
      return maxLength;
    }
  }
}
