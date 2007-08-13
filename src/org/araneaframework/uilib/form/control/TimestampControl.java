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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;
import org.araneaframework.uilib.util.ValidationUtil;
import org.araneaframework.uilib.util.ValidationUtil.ParsedDate;


/**
 * This class represents a generalization of controls that have a value
 * of type <code>Timestamp</code>. 
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class TimestampControl extends EmptyStringNullableControl implements FilteredInputControl {
  
  //*********************************************************************
  // FIELDS
  //*********************************************************************    

  /**
   * The date-time format used by the element for parsing.
   */
  protected String dateTimeInputPattern;
  protected String dateTimeOutputPattern;

  protected boolean confOverridden = false;
  
  private String characterFilter;
  
  //*********************************************************************
  // CONSTRUCTORS
  //*********************************************************************     

  /**
   * Creates the control setting it's date format pattern to the one provided.
   * 
   * @param dateTimeFormat date format pattern for {@link SimpleDateFormat}.
   */
  public TimestampControl(String dateTimeFormat, String defaultOutputFormat) {
    this.dateTimeInputPattern = dateTimeFormat;
    this.dateTimeOutputPattern = defaultOutputFormat;
  }  

  public String getCharacterFilter() {
    return characterFilter;
  }

  public void setCharacterFilter(String characterFilter) {
    this.characterFilter = characterFilter;
  }

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	

  /**
   * Parses the parameter using the <code>dateTimeFormat</code>, 
   * trying to get <code>Timestamp</code>. Adds a 
   * <code>error.form.date.wrongformat</code> error message if
   * it fails.
   */
  protected Object fromRequest(String parameterValue) {
  	ValidationUtil.ParsedDate result = parseDate(parameterValue);
    
    if (result != null) {
      dateTimeOutputPattern = result.getOutputPattern();
    	return new Timestamp(result.getDate().getTime());
    }
    
    addError(
        MessageUtil.localizeAndFormat(
        UiLibMessages.WRONG_DATE_FORMAT, 
        MessageUtil.localize(getLabel(), getEnvironment()),
        dateTimeInputPattern,
        getEnvironment()));          
    
    return null;
  }
  
  /**
   * Used by {@link TimestampControl#fromRequest(String)} to convert value
   * read from request to a <code>Date</code> in default <code>TimeZone</code>
   * and <code>Locale</code>.
   * 
   * @since 1.0.3
   */
  protected ParsedDate parseDate(String parameterValue) {
    return ValidationUtil.parseDate(parameterValue, dateTimeInputPattern);
  }

  /**
   * Formats the value using <code>dateTimeFormat</code>.
   */
  protected String toResponse(Object controlValue) {    
    return new SimpleDateFormat(dateTimeOutputPattern).format((Timestamp) controlValue);
  }
  
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************  	
  
  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }

  //*********************************************************************
  //* VIEWMODEL
  //*********************************************************************  	
  
  public class ViewModel extends EmptyStringNullableControl.ViewModel {
    private String dateTimeOutputPattern;
    private String characterFilter;
    
    /**
     * Takes an outer class snapshot.     
     */
    public ViewModel() {
      this.dateTimeOutputPattern = TimestampControl.this.dateTimeOutputPattern;
      this.characterFilter = TimestampControl.this.getCharacterFilter();
    }
    
    public SimpleDateFormat getCurrentSimpleDateTimeFormat() {
      return new SimpleDateFormat(dateTimeOutputPattern);
    }
    
    public String getCharacterFilter() {
      return this.characterFilter;
    }
  }
}
