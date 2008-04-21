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
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;
import org.araneaframework.uilib.list.util.like.RegexpLikeUtil;
import org.araneaframework.uilib.list.util.like.WildcardHandler;
import org.araneaframework.uilib.list.util.like.WildcardUtil;

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
	public static String convertMask(String mask, LikeConfiguration config,
			String escapeChar) {
		if (mask == null) {
			return null;
		}
		if (config == null) {
			throw new IllegalArgumentException("Like configuration must be specified");
		}
		if (escapeChar == null) {
			throw new IllegalArgumentException("Escape character must be specified");
		}

		mask = escapeMask(mask, config, escapeChar);

		// Handle wildcards at the start and end
		WildcardHandler handler = getHandler(mask, config);
		mask = startMask(handler, mask);
		mask = endMask(handler, mask);

		return mask;
	}

	public static String convertStartMask(String mask, LikeConfiguration config,
			String escapeChar) {
		if (mask == null) {
			return null;
		}
		if (config == null) {
			throw new IllegalArgumentException("Like configuration must be specified");
		}
		if (escapeChar == null) {
			throw new IllegalArgumentException("Escape character must be specified");
		}

		mask = escapeMask(mask, config, escapeChar);

		// Handle wildcards at the start:
		return startMask(getHandler(mask, config), mask);
	}

	public static String convertEndMask(String mask, LikeConfiguration config,
			String escapeChar) {
		if (mask == null) {
			return null;
		}
		if (config == null) {
			throw new IllegalArgumentException("Like configuration must be specified");
		}
		if (escapeChar == null) {
			throw new IllegalArgumentException("Escape character must be specified");
		}

		mask = escapeMask(mask, config, escapeChar);

		// Handle wildcards at the start:
		return endMask(getHandler(mask, config), mask);
	}

	/**
	 * Changes the mask so that it would start with a wildcard symbol.
	 * @param handler A handler for this mask to check wildcards.
	 * @param mask A mask to change to include wildcard in the beginning.
	 * @return A mask that contains a wildcard in the beginning.
	 */
	protected static String startMask(WildcardHandler handler, String mask) {
		Assert.notNull(handler, "WildcardHandler is requiered.");
		Assert.notNull(mask, "Mask is required.");

		if (handler.getStartsWith() != handler.shouldStartWith()) {
			if (handler.getStartsWith() == WildcardHandler.ANY_STRING_WILDCARD) {
				mask = mask.substring(SQL_LIKE_ANY_STRING_WILDCARD.length());
			} else if (handler.getStartsWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = mask.substring(SQL_LIKE_ANY_CHAR_WILDCARD.length());
			}

			if (handler.shouldStartWith() == WildcardHandler.ANY_STRING_WILDCARD) {
				mask = SQL_LIKE_ANY_STRING_WILDCARD + mask;
			} else if (handler.shouldStartWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = SQL_LIKE_ANY_CHAR_WILDCARD + mask;
			}
		}
		return mask;
	}

	/**
	 * Changes the mask so that it would end with a wildcard symbol.
	 * @param handler A handler for this mask to check wildcards.
	 * @param mask A mask to change to include wildcard in the end.
	 * @return A mask that contains a wildcard in the end.
	 */
	protected static String endMask(WildcardHandler handler, String mask) {
		Assert.notNull(handler, "WildcardHandler is requiered.");
		Assert.notNull(mask, "Mask is required.");

		if (handler.getEndsWith() != handler.shouldEndWith()) {
			if (handler.getEndsWith() == WildcardHandler.ANY_STRING_WILDCARD) {
				mask = mask.substring(0, mask.length()
						- SQL_LIKE_ANY_STRING_WILDCARD.length());
			} else if (handler.getEndsWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = mask.substring(0, mask.length()
						- SQL_LIKE_ANY_CHAR_WILDCARD.length());
			}

			if (handler.shouldEndWith() == WildcardHandler.ANY_STRING_WILDCARD) {
				mask = mask + SQL_LIKE_ANY_STRING_WILDCARD;
			} else if (handler.shouldEndWith() == WildcardHandler.ANY_CHAR_WILDCARD) {
				mask = mask + SQL_LIKE_ANY_CHAR_WILDCARD;
			}
		}
		return mask;
	}

	/**
	 * Prepares the mask by escaping any included wildcard symbols.
	 * 
	 * @param mask The mask to check and modify.
	 * @param config <code>LikeConfiguration</code> with wildcard information.
	 * @param escapeChar Escape expression to use to escape the wildcard symbols.
	 * @return Escaped mask.
	 */
	protected static String escapeMask(String mask, LikeConfiguration config, String escapeChar) {
		// Escape
		mask = StringUtils.replace(mask, escapeChar, escapeChar + escapeChar);

		if (!ArrayUtils.contains(config.getAnyStringWildcards(), SQL_LIKE_ANY_STRING_WILDCARD)) {
			mask = StringUtils.replace(mask, SQL_LIKE_ANY_STRING_WILDCARD, escapeChar
					+ SQL_LIKE_ANY_STRING_WILDCARD);
		}

		if (!ArrayUtils.contains(config.getAnyCharWildcards(), SQL_LIKE_ANY_CHAR_WILDCARD)) {
			mask = StringUtils.replace(mask, SQL_LIKE_ANY_CHAR_WILDCARD, escapeChar
					+ SQL_LIKE_ANY_CHAR_WILDCARD);
		}

		// Convert wildcards
		mask = replace(mask, config.getAnyStringWildcards(), SQL_LIKE_ANY_STRING_WILDCARD);
		mask = replace(mask, config.getAnyCharWildcards(), SQL_LIKE_ANY_CHAR_WILDCARD);

		return mask;
	}

	protected static WildcardHandler getHandler(String mask,
			LikeConfiguration config) {

		WildcardHandler handler = config.createWildcardHandler();
		WildcardUtil.setWildcards(handler, mask, SQL_LIKE_ANY_STRING_WILDCARD,
				SQL_LIKE_ANY_CHAR_WILDCARD);
		return handler;
	}

	/**
	 * <p>Replaces all occurrences of the Strings within another String.</p>
	 * 
	 * @param text text to search and replace in
	 * @param repl the Strings to search for
	 * @param with the String to replace with
	 * @return the text with any replacements processed,
	 */
	private static String replace(String test, String[] repl, String with) {
		for (int i = 0; i < repl.length; i++) {
			test = StringUtils.replace(test, repl[i], with);
		}
		return test;
	}

}
