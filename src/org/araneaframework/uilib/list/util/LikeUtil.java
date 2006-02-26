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

package org.araneaframework.uilib.list.util;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * Helper class for emulating database <code>LIKE</code> expression in Java.
 */
public class LikeUtil {
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
	 * @return <code>true</code> if the <code>string</code> matches with the
	 *         <code>mask</code>.
	 */
	public static boolean isLike(String string, String mask, boolean ignoreCase) {
		try {
			// ? --> .
			RE re = new RE("\\?");
			String a = re.subst(mask, ".");
			// * --> .*
			re = new RE("\\*");
			String b = re.subst(a, ".*");
			// % --> .*
			re = new RE("\\%");
			String c = re.subst(b, ".*");
			// end with $ if started with .*
			if (c.startsWith(".*")) {
				c = c + "$";
			}
			// start with ^ if ended with .*
			if (c.endsWith(".*")) {
				c = "^" + c;
			}
			// choose level according to case insensitivity
			re = new RE(c, ignoreCase ? RE.MATCH_CASEINDEPENDENT
					: RE.MATCH_NORMAL);
			// return the matching result of regular expression
			return re.match(string);
		} catch (RESyntaxException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
