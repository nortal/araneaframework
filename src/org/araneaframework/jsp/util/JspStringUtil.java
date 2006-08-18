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

package org.araneaframework.jsp.util;

/**
 * Some simple utility routines for Strings.
 * 
 * @author Konstantin Tretyakov
 */
public abstract class JspStringUtil {
	/**
	 * Given a string and an accesskey, finds the first appearance of 
	 * accessKey  in that string, and surrounds it with &lt;u&gt;..&lt/u&gt;.
	 * Returns the result.
	 * 
	 * If accessKey is null, is not present in the string or its length is not 1,
	 * returns the string unmodified.
	 * 
	 * Assumes that given string has no HTML elements in it. Does case-insensitive matching.
	 * 
	 * @param s       String in which the access key is to be underlined. May be null.
	 * @param accessKey Access key to underline. May be null.
	 * @return Given string with accesskey underlined (if possible).
	 */
	public static String underlineAccessKey(String s, String accessKey) {
		if (s == null || accessKey == null || accessKey.length() != 1) return s;
		char lo = accessKey.toLowerCase().charAt(0);
		char hi = accessKey.toUpperCase().charAt(0);
		int pos = -1;
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) == lo || s.charAt(i) == hi) {
				pos = i;
				break;
			}
		}
		if (pos == -1) return s;
		String result = s.substring(0, pos) + 
					"<u>" + s.charAt(pos) + "</u>" +
					s.substring(pos + 1);
		return result;
	}
}
