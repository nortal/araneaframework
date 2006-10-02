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


/**
 * This control represents a checkbox.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class CheckboxControl extends StringRequestControl{   
  
  /**
   * Returns "Boolean".
   * @return "Boolean".
   */
  public String getRawValueType() {
    return "Boolean";
  }
	
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * This method preprocesses request, interpreting it as following:
   * <ul>
   * <li>if there's a not null request parameter, it's substituted with "true"
   * <li>if there isn't a request parameter, it's substituted with "false"
   * </ul>
   */
  protected String preprocessRequestParameter(String parameterValue) {
    if (parameterValue == null) {
      return "false";
    }
    else {
      return "true";
    }
  }  
  
  /**
   * This method makes a <code>Boolean</code> out of a <code>String</code>.
   */
  protected Object fromRequest(String parameterValue) {
    return Boolean.valueOf(parameterValue);
  }
  
  /**
   * This method makes a <code>String</code> out of a <code>Boolean</code>.
   */
  protected String toResponse(Object controlValue) {
    return ((Boolean) controlValue).booleanValue() ? "true" : "false";
  }
}
