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
 * This is basically a copy of StringValueControl that lacks min/maxlength validation.
 * 
 * @author Konstantin Tretjakov
 */
public class HiddenControl extends EmptyStringNullableControl<String> {
 
	//*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************  		
	
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
		  
  @Override
  protected String fromRequest(String parameterValue) {
    return parameterValue;
  }

  @Override
  protected String toResponse(String controlValue) {
    return controlValue;
  }
}
