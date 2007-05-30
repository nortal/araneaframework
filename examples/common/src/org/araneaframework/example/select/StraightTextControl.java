package org.araneaframework.example.select;

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

import org.araneaframework.uilib.support.TextType;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;
import org.araneaframework.uilib.util.ValidationUtil;

/**
 * Class that represents a textbox control.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class StraightTextControl extends StraightBaseControl {
  protected TextType textType = TextType.TEXT;
  private Long minLength;
  private Long maxLength;
  private boolean trimValue = false;
  
  
  /**
   * Empty.
   */
  public StraightTextControl() {
    //Empty
  }
    
  /**
   * Makes a text control with specific type.
   * @param textType specific type.
   */
  public StraightTextControl(TextType textType) {
    this.textType = textType;
  }  
  
  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public StraightTextControl(Long minLength, Long maxLength) {
    setMinLength(minLength);
    setMaxLength(maxLength);
  }    
  
  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   * @param trimValue whether the value from request will be trimmed.
   */
  public StraightTextControl(Long minLength, Long maxLength, boolean trimValue) {
    setMinLength(minLength);
    setMaxLength(maxLength);
    setTrimValue(trimValue);
  }   
  
  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * @param textType specific type.
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public StraightTextControl(TextType textType, Long minLength, Long maxLength) {
    this.textType = textType;
    setMinLength(minLength);
    setMaxLength(maxLength);
  }
  
  /**
   * Sets the specific text type.
   * @param textType the specific text type.
   */
  public void setTextType(TextType textType) {
    this.textType = textType;
  }
  
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
  
  //*********************************************************************
  //* INTERNAL INTERFACE
  //*********************************************************************  	
	
  /**
   * In case text control type is other than {@link TextType#TEXT} makes custom checks. 
   */
  protected void validateNotNull() {
    //XXX: super.validateNotNull();
    
    if (textType.equals(TextType.NUMBER_ONLY)) {
    	for (int i = 0; i < ((String) getRawValue()).length(); i++) {
    		if (!Character.isDigit(((String) getRawValue()).charAt(i))) {
          addError(
              MessageUtil.localizeAndFormat(
              UiLibMessages.NOT_A_NUMBER, 
              MessageUtil.localize(getLabel(), getEnvironment()),
              getEnvironment()));           
    			break;
    		}
    	}
    }
    else if (textType.equals(TextType.EMAIL)) {
      if (!ValidationUtil.isEmail((String) getRawValue())) {
        addError(
            MessageUtil.localizeAndFormat(
            UiLibMessages.NOT_AN_EMAIL, 
            MessageUtil.localize(getLabel(), getEnvironment()),
            getEnvironment()));             
      }  
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
  //XXX: StringValueControl.ViewModel
  public class ViewModel extends StraightBaseControl.ViewModel {
    protected String textType;
    
    private Long minLength;
    private Long maxLength;
    
    protected ViewModel() {
        this.minLength = StraightTextControl.this.minLength;
        this.maxLength = StraightTextControl.this.maxLength;
      this.textType = StraightTextControl.this.textType.getName();      
    }
    
    public String getTextType() {
      return textType;
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
