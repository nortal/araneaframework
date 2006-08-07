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

package org.araneaframework.uilib.list.util.like;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Helper class for emulating database <code>LIKE</code> expression in Java.
 */
public class LikeUtil {
	
	private static final String ANY_STRING_WILDCARD = ".*";
	private static final String ANY_CHAR_WILDCARD = ".";
	private static final String LINE_START = "^";
	private static final String LINE_END = "$";
	
	private static final String[] META_CHARS = toArray("\\()[]{}|^$.*+?");
	private static final String ESCAPE_CHAR = "\\";	
	
	/**
	 * Checks if the <code>string</code> matches with the <code>mask</code>
	 * or not.
	 * 
	 * @param string
	 *            the comparable string.
	 * @param mask
	 *            the mask which the <code>string</code> is compared to.
	 * @param ignoreCase
	 *            whether to compare the <code>string</code> and
	 *            <code>mask</code> case insensitive.
	 * @param config
	 * 			  Like configuration.
	 * @return <code>true</code> if the <code>string</code> matches with the
	 *         <code>mask</code>.
	 */
	public static boolean isLike(String string, String mask, boolean ignoreCase, LikeConfiguration config) {
		// Escape all metachars except user wildcards
		mask = escape(mask, config);
		
		// Convert wildcards
		mask = replace(mask, config.getAnyStringWildcards(), ANY_STRING_WILDCARD);
		mask = replace(mask, config.getAnyCharWildcards(), ANY_CHAR_WILDCARD);
		
		mask = handleStartEndWildcards(mask, config);
		return matches(string, mask, ignoreCase);
	}
	
	private static String escape(String mask, LikeConfiguration config) {
		String[] metaChars = META_CHARS;
		removeAll(metaChars, config.getAnyStringWildcards());
		removeAll(metaChars, config.getAnyCharWildcards());
		
		for (int i = 0; i < metaChars.length; i++) {
			mask = StringUtils.replace(mask, metaChars[i], ESCAPE_CHAR + metaChars[i]);
		}
		return mask;
	}
	
	private static String handleStartEndWildcards(String mask, LikeConfiguration config) {
		boolean startsWith = mask.startsWith(ANY_STRING_WILDCARD);
		boolean endsWith = mask.endsWith(ANY_STRING_WILDCARD);
		
		AnyStringWildcardHandler handler = config.createAnyStringWildcardHandler();
		handler.setStartsWith(startsWith);
		handler.setEndsWith(endsWith);		
		if (!handler.shouldStartWith()) {
			if (startsWith) {
				mask = mask.substring(ANY_STRING_WILDCARD.length());
			}
			mask = LINE_START + mask;
		}
		if (!handler.shouldEndWith()) {
			if (endsWith) {
				mask = mask.substring(0, mask.length() - ANY_STRING_WILDCARD.length());
			}
			mask = mask + LINE_END;
		}
		return mask;
	}
	
	private static boolean matches(String string, String mask, boolean ignoreCase) {
		try {
			return new RE(mask, ignoreCase ? RE.MATCH_CASEINDEPENDENT : RE.MATCH_NORMAL).match(string);
		} catch (RESyntaxException e) {
			throw ExceptionUtil.uncheckException(e);
		}
	}
	
	/**
	 * <p>Replaces all occurrences of a String within another String.</p>
	 * 
     * @param text  text to search and replace in
     * @param repl  the Strings to search for
     * @param with  the String to replace with
     * @return the text with any replacements processed,
	 */
	public static String replace(String test, String[] repl, String with) {
		for (int i = 0; i < repl.length; i++) {
			test = StringUtils.replace(test, repl[i], with);
		}
		return test;
	}
	
	private static String[] toArray(String string) {
		String[] result = new String[string.length()];
		for (int i = 0; i < string.length(); i++) {
			result[i] = string.substring(i, i+1);			
		}
		return result;
	}
	
	private static Object[] removeAll(Object[] array, Object[] removeArray) {
		List tmp = new ArrayList(Arrays.asList(array));
		tmp.removeAll(Arrays.asList(removeArray));
		return tmp.toArray();
	}	
}
