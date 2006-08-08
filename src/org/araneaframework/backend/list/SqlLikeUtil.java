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

package org.araneaframework.backend.list;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.list.util.like.AnyStringWildcardHandler;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;
import org.araneaframework.uilib.list.util.like.RegexpLikeUtil;

/**
 * Helper class for database <code>LIKE</code> expression.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see RegexpLikeUtil
 */
public class SqlLikeUtil {
	
	public static final String SQL_LIKE_ANY_STRING_WILDCARD = "%";
	public static final String SQL_LIKE_ANY_CHAR_WILDCARD = "_";
	
	/**
	 * Converts SQL <code>LIKE</code> mask in custom format (specified by
	 * {@link LikeConfiguration}) into SQL Like standard format.
	 * <p<
	 * The output may contain escape characters. Therefore the result must be
	 * used in format (expression) LIKE (mask) ESCAPE (escapeChar). 
	 * </p>
	 * 
	 * @param mask mask in custom format.
	 * @param config configuration that holds mask custom format and behaivor.
	 * @param escapeChar escape character for SQL syntax.
	 * @return the mask in standard format.
	 */
	public static String convertMask(String mask, LikeConfiguration config, String escapeChar) {
		if (mask == null) {
			return null;
		}
		if (config == null) {
			throw new IllegalArgumentException("Like configuration must be specified");
		}
		if (escapeChar == null) {
			throw new IllegalArgumentException("Escape character must be specified");
		}
		
		// Escape
		mask = StringUtils.replace(mask, escapeChar, escapeChar + escapeChar);				
		if (!ArrayUtils.contains(config.getAnyStringWildcards(), SQL_LIKE_ANY_STRING_WILDCARD)) {				
			mask = StringUtils.replace(mask, SQL_LIKE_ANY_STRING_WILDCARD, escapeChar + SQL_LIKE_ANY_STRING_WILDCARD);
		}
		if (!ArrayUtils.contains(config.getAnyCharWildcards(), SQL_LIKE_ANY_CHAR_WILDCARD)) {
			mask = StringUtils.replace(mask, SQL_LIKE_ANY_CHAR_WILDCARD, escapeChar + SQL_LIKE_ANY_CHAR_WILDCARD);
		}
		
		// Convert wildcards
		mask = replace(mask, config.getAnyStringWildcards(), SQL_LIKE_ANY_STRING_WILDCARD);
		mask = replace(mask, config.getAnyCharWildcards(), SQL_LIKE_ANY_CHAR_WILDCARD);
		
		// Handle any string wildcards at the start and the end  
		boolean startsWith = mask.startsWith(SQL_LIKE_ANY_STRING_WILDCARD);
		boolean endsWith = mask.endsWith(SQL_LIKE_ANY_STRING_WILDCARD);
		
		AnyStringWildcardHandler handler = config.createAnyStringWildcardHandler();
		handler.setStartsWith(startsWith);
		handler.setEndsWith(endsWith);
		if (!startsWith && handler.shouldStartWith()) {
			mask = SQL_LIKE_ANY_STRING_WILDCARD + mask;
		}
		if (startsWith && !handler.shouldStartWith()) {
			mask = mask.substring(SQL_LIKE_ANY_STRING_WILDCARD.length());
		}
		if (!endsWith && handler.shouldEndWith()) {
			mask = mask + SQL_LIKE_ANY_STRING_WILDCARD;
		}
		if (endsWith && !handler.shouldEndWith()) {
			mask = mask.substring(0, mask.length() - SQL_LIKE_ANY_STRING_WILDCARD.length());
		}
		return mask;
	}
	
	/**
	 * <p>Replaces all occurrences of the Strings within another String.</p>
	 * 
     * @param text  text to search and replace in
     * @param repl  the Strings to search for
     * @param with  the String to replace with
     * @return the text with any replacements processed,
	 */
	private static String replace(String test, String[] repl, String with) {
		for (int i = 0; i < repl.length; i++) {
			test = StringUtils.replace(test, repl[i], with);
		}
		return test;
	}
	
}
