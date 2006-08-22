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

package org.araneaframework.uilib.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ValidationUtil {
	
	/**
	 * Tries to parse the date according to the given patterns. The patterns correspond to the usual 
	 * {@link SimpleDateFormat} patterns with one addition: one can combine them using "|" so that
	 * if at least one pattern parses the input it will be used.
	 * 
	 * @param dateTimeString date to be parsed.
	 * @param format {@link SimpleDateFormat} patterns with "|".
	 * @return parsed {@link Date} or null if parsing fails.
	 */
	public static ParsedDate parseDate(String dateTimeString, String format) {
		ParsedDate result = null;
		StringTokenizer tokenizer = new StringTokenizer(format, "|");
		
		String[] patterns = new String[tokenizer.countTokens()];
		for (int i = 0; tokenizer.hasMoreTokens(); i++)
			patterns[i] = tokenizer.nextToken();
		
		for (int i = 0; i < patterns.length; i++) {
			Date date = null;
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(patterns[i]);      
			dateFormat.setLenient(false);      
			ParsePosition pos = new ParsePosition(0);
			
			date = dateFormat.parse(dateTimeString, pos);
			
			if (date != null && (pos.getIndex() == dateTimeString.length()))
				result = new ParsedDate(date, patterns[i]);
			
			// introduce the y10k problem && ignore everything B.C
			// needed to escape SimpleDateFormats broken guesswork that can produce corrupt Dates. 
			if (result != null) {
				int year = result.getDate().getYear();
				if (year > 9999 || year < 0)
					result = null;
				
				/* checking just year is not enough b/c some strings like "020110999" "02.01.11500" 
				 still manage to pass through whereas others with same pattern do not - for example 
				 "02.01.13452". Guess it is all about the zeroes. */
				
				// so, check the length too. Means that dd.MM.yyyy does not interpret d.M.yyyy, unless
				// format parameter contains it.
				if (dateTimeString.trim().length() != patterns[i].length())
					result = null;
			}
			
			if (result != null)
				break;
		}
		
		return result;
	}
	
	public static class ParsedDate {
		protected Date date;
		protected String outputPattern;
		
		public ParsedDate(Date date, String outputPattern) {
			this.date = date;
			this.outputPattern = outputPattern;
		}
		
		public Date getDate() {
			return this.date;
		}
		public String getOutputPattern() {
			return this.outputPattern;
		}
	}
	
	/**
	 * Checks whether the string is a valid email.
	 * 
	 * @param emailString supposed email string.
	 * @return whether the string is a valid email.
	 */
	public static boolean isEmail(String emailString) {
		try {
			new InternetAddress(emailString);
			
			if (emailString.indexOf('@') == -1) return false;
		}
		catch (AddressException e) {
			return false;
		}
		
		return true;
	}  
}
