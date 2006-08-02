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

import java.text.SimpleDateFormat;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * This class represents a {@link org.araneaframework.uilib.form.control.TimestampControl}, that holds only time - that is
 * it's default pattern is "HH:mm".
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 */
public class TimeControl extends TimestampControl {

	/**
	 * This is the default time format for this control.
	 */
	public final static String DEFAULT_FORMAT = "HH:mm";

	/**
	 * Creates the control initializing the pattern to {@link #DEFAULT_FORMAT}.
	 */
	public TimeControl() {
		super(DEFAULT_FORMAT, DEFAULT_FORMAT);
	}

	/**
	 * Creates the control initializing the pattern to <code>dateTimeFormat</code>.
	 * 
	 * @param dateTimeFormat the custom pattern.
	 */
	public TimeControl(String dateTimeFormat, String defaultTimeOutputFormat) {
		super(dateTimeFormat, defaultTimeOutputFormat);

		confOverridden = true;
	}
	
	/**
	 * Returns "Timestamp".
	 * 
	 * @return "Timestamp".
	 */
	public String getRawValueType() {
		return "Timestamp";
	}
	
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
	public void init() throws Exception {
		super.init();
		if (!confOverridden) {
			String confFormat = (String) getConfiguration().getEntry(ConfigurationContext.CUSTOM_TIME_FORMAT);
			if (confFormat != null) dateTimeFormat = confFormat;

			String confOutputFormat = (String) getConfiguration().getEntry(
					ConfigurationContext.DEFAULT_TIME_OUTPUT_FORMAT);
			if (confOutputFormat != null) currentSimpleDateTimeFormat = new SimpleDateFormat(confOutputFormat);
		}
	}

}
