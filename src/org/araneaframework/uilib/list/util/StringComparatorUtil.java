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

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.collections.comparators.ComparableComparator;

/**
 * Stirng Comparator utils.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see Comparator
 */
public class StringComparatorUtil {
	/**
	 * Returns the <code>Comparator</code> for not-null <code>String</code>
	 * objects.
	 * 
	 * @param locale
	 *            <code>Locale</code> (can be null).
	 * @param ignoreCase
	 *            whether to ignore case.
	 * @return <code>Comparator</code> for <code>String</code> objects.
	 */
	protected static Comparator stringComparator(Locale locale,
			boolean ignoreCase) {
		
		if (locale != null) {
			Collator collator = Collator.getInstance(locale);
			if (ignoreCase) {
				collator.setStrength(Collator.SECONDARY);				
			} else {
				collator.setStrength(Collator.TERTIARY);
			}
			return collator;
		}
		if (ignoreCase) {
			return String.CASE_INSENSITIVE_ORDER;
		}
		return ComparableComparator.getInstance();
	}
}
