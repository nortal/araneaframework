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

package org.araneaframework.backend.list.helper.naming;

import org.araneaframework.uilib.util.NameUtil;

/**
 * Standard naming strategy for field names and column names.
 * <p>
 * Field names are transformed into database column names as following:<br/>
 * <ul>
 * <li>The full name is split into prefix and suffix (following to the last dot).
 * <li>All dots in the prefix and suffix are converted into underscores.
 * <li>Before each upper case letter followed by lower case an underscore is inserted.
 * </ul>
 * Field names are transformed into database column aliases as following:<br/><br/>
 * <ul>
 * <li>All dots are converted into underscores.
 * <li>Before each upper case letter followed by lower case an underscore is inserted.
 * </ul>
 * </p>
 * 
 * @author Rein Raudjärv
 */
public class StandardNamingStrategy implements NamingStrategy {

	// --- Column Name ---

	public String fieldToColumnName(String fieldName) {
		String prefix = resolvePrefix(extractPrefix(fieldName));
		String suffix = resolveSuffix(extractSuffix(fieldName));
		return concatFullname(prefix, suffix);
	}
	
	protected String extractPrefix(String varName) {
		return NameUtil.getLongestPrefix(varName);
	}
	
	protected String extractSuffix(String varName) {
		return NameUtil.getShortestSuffix(varName);
	}

	protected String resolvePrefix(String fieldNamePrefix) {
		return addUnderscores(fieldNamePrefix);
	}

	protected String resolveSuffix(String fieldNameSuffix) {
		return addUnderscores(fieldNameSuffix);
	}

	protected String concatFullname(String prefix, String suffix) {
		return NameUtil.getFullName(prefix, suffix);
	}

	// --- Column Alias ---

	public String fieldToColumnAlias(String fieldName) {
		return addUnderscores(fieldName);
	}

	// --- Utils ---

	/**
	 * Replace all dots with underscores
	 * and add an underscore before upper-case letters
	 * followed by a non-upper-case letter.
	 * <p>
	 * E.g.
	 * <pre>
	 * "a" -> "a"
	 * "a.b" -> "a_b"
	 * "aBc" -> "a_bc"
	 * "aB" -> "aB"
	 * </pre>
	 */
	public static String addUnderscores(String name) {
		if (name == null) {
			return null;
		}

		StringBuffer buf = new StringBuffer( name.replace('.', '_') );
		for (int i=1; i<buf.length()-1; i++) {
			if (
					'_'!=buf.charAt(i-1) &&
					Character.isUpperCase( buf.charAt(i) ) &&
					!Character.isUpperCase( buf.charAt(i+1) )
			)
			{
				buf.insert(i++, '_');
			}
		}
		return buf.toString().toLowerCase();
	}	

}
