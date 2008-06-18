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
 * This class represents a textarea control.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class TextareaControl extends StringValueControl {
  
  /**
   * Empty.
   */
  public TextareaControl() {
    //Empty
  }  
  
  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public TextareaControl(Long minLength, Long maxLength) {    
    setMinLength(minLength);
    setMaxLength(maxLength);
  }    
  
  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   * @param trimValue whether the value from request will be trimmed.
   */
  public TextareaControl(Long minLength, Long maxLength, boolean trimValue) {    
    setMinLength(minLength);
    setMaxLength(maxLength);
    setTrimValue(trimValue);
  }   
  
  //*********************************************************************
  //* INTERNAL INTERFACE
  //*********************************************************************  	
	
  /**
   * Takes away &lt;cr&gt; added by Intenet Explorer.
   */
  @Override
  protected String preprocessRequestParameter(String parameterValue) {
    String superProcessed = super.preprocessRequestParameter(parameterValue);
    
    if (superProcessed == null) return null;
    
    StringBuffer stripped = new StringBuffer(superProcessed);
    int lastCr = stripped.toString().lastIndexOf("\r");
    while (lastCr != -1) {
      stripped.delete(lastCr, lastCr + 1);
      lastCr = stripped.toString().lastIndexOf("\r");
    }
    return stripped.toString();    
  } 		  
}
