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
import java.util.Calendar;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * This class represents a {@link org.araneaframework.uilib.form.control.TimestampControl},
 * that holds only date - that is it's default pattern is "dd.MM.yyyy".
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class DateControl extends TimestampControl {
	protected int defaultHour;
	protected int defaultMinute;
	protected int defaultSecond;

  /**
   * This is the default date format for this control. 
   */
  public final static String DEFAULT_FORMAT = "dd.MM.yyyy";

  /**
   * Creates the control initializing the pattern to {@link #DEFAULT_FORMAT}.
   */
  public DateControl() {
    super(DEFAULT_FORMAT, DEFAULT_FORMAT);
  }

  /**
   * Creates the control initializing the pattern to <code>dateTimeFormat</code>.
   * @param dateTimeFormat the custom pattern.
   */
  public DateControl(String dateTimeFormat, String defaultOutputFormat) {
    super(dateTimeFormat, defaultOutputFormat);
    
    confOverridden = true;
  }
  
  /**
   * Overrides the default time that is set on all dates read from request.
   */
  public void setDefaultTime(int hour, int minute, int second) {
  	this.defaultHour = hour;
  	this.defaultMinute = minute;
  	this.defaultSecond = second;
  }
  
  /**
   * Returns "Timestamp".
   * @return "Timestamp".
   */
  public String getRawValueType() {
    return "Timestamp";
  }

  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	

  protected void init() throws Exception {
	  	super.init();
  		if (!confOverridden) {
  			String confFormat = (String) getConfiguration().getEntry(ConfigurationContext.CUSTOM_DATE_FORMAT);    
  			if (confFormat != null) dateTimeFormat = confFormat;
	    
	    	String confOutputFormat = (String) getConfiguration().getEntry(ConfigurationContext.DEFAULT_DATE_OUTPUT_FORMAT);    
	    	if (confOutputFormat != null) currentSimpleDateTimeFormat = new SimpleDateFormat(confOutputFormat);
  		}
  }	 
  
	protected Object fromRequest(String parameterValue) {
		Timestamp result = (Timestamp) super.fromRequest(parameterValue);
		
		if (result != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(result);
			
			cal.set(Calendar.HOUR_OF_DAY, defaultHour);
			cal.set(Calendar.MINUTE, defaultMinute);
			cal.set(Calendar.SECOND, defaultSecond);
			
			result = new Timestamp(cal.getTimeInMillis());
		}
	
		return result;
	}

}
