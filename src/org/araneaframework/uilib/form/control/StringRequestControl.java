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
 * This class represents a further concretezation of {@link org.araneaframework.uilib.form.control.StringArrayRequestControl},
 * that is it represents controls, that have a single <code>String</code> request parameter.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public abstract class StringRequestControl extends StringArrayRequestControl {

  //*********************************************************************
  // FIELDS
  //*********************************************************************  

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
  
  /**
   * This is just a proxy to {@link #fromRequest(String)}.
   */
  protected Object fromRequestParameters(String[] parameterValues) {
    return fromRequest(parameterValues[0]);
  }
  
  /**
   * This is just a proxy to {@link #preprocessRequestParameter(String)}.
   */
  protected String[] preprocessRequestParameters(String[] parameterValues) {
    String result = parameterValues == null ? preprocessRequestParameter(null) : preprocessRequestParameter(parameterValues[0]);
    return result == null ? null : new String[] {result};
  }
  
  /**
   * This is just a proxy to {@link #toResponse(Object)}.
   */
  protected String[] toResponseParameters(Object controlValue) {
    String result = toResponse(controlValue);
    return result == null ? null : new String[] {result};        
  }
  
  //*********************************************************************
  //* ABSTRACT METHODS
  //*********************************************************************    
  
  /**
   * @see StringArrayRequestControl#preprocessRequestParameters(String[])
   */
  protected abstract String preprocessRequestParameter(String parameterValue);

  /**
   * @see StringArrayRequestControl#fromRequestParameters(String[])
   */
  protected abstract Object fromRequest(String parameterValue);

  /**
   * @see StringArrayRequestControl#toResponseParameters(Object)
   */
  protected abstract String toResponse(Object controlValue);    
}
