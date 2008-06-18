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
import org.araneaframework.backend.list.SqlLikeUtil;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Helper class for emulating database <code>LIKE</code> expression in Java.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see SqlLikeUtil
 */
public class RegexpLikeUtil {
	
	public static final String RE_ANY_STRING_WILDCARD = ".*";
	public static final String RE_ANY_CHAR_WILDCARD = ".";
	public static final String RE_LINE_START = "^";
	public static final String RE_LINE_END = "$";
	
	public static final String[] RE_META_CHARS = {"\\", "(", ")", "[", "]", "{", "}", "|", "^", "$", ".", "*", "+", "*", "?"};
	public static final String RE_ESCAPE_CHAR = "\\";	
	
	/**
	 * Checks if the <code>string</code> matches with the <code>mask</code>
	 * or not.
	 * 
	 * @param string
	 *            the comparable string.
	 * @param customMask
	 *            the mask which the <code>string</code> is compared to.
	 * @param ignoreCase
	 *            whether to compare the <code>string</code> and
	 *            <code>mask</code> case insensitive.
	 * @param config
	 * 			  Like configuration.
	 * @return <code>true</code> if the <code>string</code> matches with the
	 *         <code>mask</code>.
	 */
	public static boolean isLike(String string, String customMask, boolean ignoreCase, LikeConfiguration config) {
		return matches(string, convertMask(customMask, config), ignoreCase);
	}

	/**
     * Checks whether given <code>string</code> starts with given
     * <code>customMask</code> using regular expression.
     * 
     * @param string The string to check
     * @param customMask The mask that must match
     * @param ignoreCase Specifies the case sensitivity of the match.
     * @param config Configuration of the expression.
     * @return <code>true</code>, if given <code>string</code> starts with
     *         given <code>customMask</code>
     * @since 1.1.3
     */
	public static boolean isStartsWith(String string, String customMask, boolean ignoreCase, LikeConfiguration config) {
		return matches(string, RE_LINE_START + convertMask(customMask, config), ignoreCase);
	}

    /**
     * Checks whether given <code>string</code> ends with given
     * <code>customMask</code> using regular expression.
     * 
     * @param string The string to check.
     * @param customMask The mask that must match.
     * @param ignoreCase Specifies the case sensitivity of the match.
     * @param config Configuration of the expression.
     * @return <code>true</code>, if given <code>string</code> ends with
     *         given <code>customMask</code>
     * @since 1.1.3
     */
	public static boolean isEndsWith(String string, String customMask, boolean ignoreCase, LikeConfiguration config) {
		return matches(string, convertMask(customMask, config) + RE_LINE_END, ignoreCase);
	}

	private static String convertMask(String mask, LikeConfiguration config) {
		// Escape all metachars except custom wildcards
		List<String> tmp = new ArrayList<String>(Arrays.asList(RE_META_CHARS));
		tmp.removeAll(Arrays.asList(config.getAnyStringWildcards()));
		tmp.removeAll(Arrays.asList(config.getAnyCharWildcards()));
		String[] metaChars = tmp.toArray(new String[tmp.size()]);
		for (int i = 0; i < metaChars.length; i++) {
			mask = StringUtils.replace(mask, metaChars[i], RE_ESCAPE_CHAR + metaChars[i]);
		}
		
		// Convert wildcards
		mask = replace(mask, config.getAnyStringWildcards(), RE_ANY_STRING_WILDCARD);
		mask = replace(mask, config.getAnyCharWildcards(), RE_ANY_CHAR_WILDCARD);
		
		// Handle wildcards at the start and end
		WildcardHandler handler = config.createWildcardHandler();
		WildcardUtil.setWildcards(handler, mask, RE_ANY_STRING_WILDCARD, RE_ANY_CHAR_WILDCARD);
		if (handler.getStartsWith() != handler.shouldStartWith()) {
			if (handler.getStartsWith() == WildcardHandler.ANY_STRING_WILDCARD) {
				mask = mask.substring(RE_ANY_STRING_WILDCARD.length());
			} else if (handler.getStartsWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = mask.substring(RE_ANY_CHAR_WILDCARD.length());
			}
			
			if (handler.shouldStartWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = RE_ANY_CHAR_WILDCARD + mask;
			}
		}
		if (handler.shouldStartWith() != WildcardHandler.ANY_STRING_WILDCARD) {
			mask = RE_LINE_START + mask;
		}
		if (handler.getEndsWith() != handler.shouldEndWith()) {
			if (handler.getEndsWith() == WildcardHandler.ANY_STRING_WILDCARD) {
				mask = mask.substring(0, mask.length() - RE_ANY_STRING_WILDCARD.length());
			} else if (handler.getEndsWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = mask.substring(0, mask.length() - RE_ANY_CHAR_WILDCARD.length());
			}
			
			if (handler.shouldEndWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = mask + RE_ANY_CHAR_WILDCARD;
			}
		}
		if (handler.shouldEndWith() != WildcardHandler.ANY_STRING_WILDCARD) {
			mask = mask + RE_LINE_END;
		}
		return mask;		
	}
	
	private static boolean matches(String string, String nativeMask, boolean ignoreCase) {
		try {
			return new RE(nativeMask, ignoreCase ? RE.MATCH_CASEINDEPENDENT : RE.MATCH_NORMAL).match(string);
		} catch (RESyntaxException e) {
			throw ExceptionUtil.uncheckException(e);
		}
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
