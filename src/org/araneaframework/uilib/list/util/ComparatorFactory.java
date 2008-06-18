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

import java.util.Comparator;
import java.util.Locale;
import org.araneaframework.uilib.list.util.comparator.BooleanComparator;
import org.araneaframework.uilib.list.util.comparator.ComparableComparator;
import org.araneaframework.uilib.list.util.comparator.IgnoreCaseComparator;
import org.araneaframework.uilib.list.util.comparator.LocaleStringComparator;
import org.araneaframework.uilib.list.util.comparator.NullComparator;


/**
 * Factory class for constructing <code>Comparator</code> objects.
 */
public class ComparatorFactory {

	/**
	 * Whether the <code>null</code> value should be ordered before not-null
	 * values by default.
	 */
	public static final boolean NULL_FIRST_BY_DEFAULT = true;

	/**
	 * Whether the <code>String</code> objects <code>Comparator</code>
	 * should be case insensitve by default.
	 */
	public static final boolean IGNORE_CASE_BY_DEFAULT = true;

	/**
	 * Whether the <code>Boolean</code> objects <code>Comparator</code>
	 * should order the <code>true</code> value before the <code>false</code>
	 * value by default.
	 */
	public static final boolean TRUE_FIRST_BY_DEFAULT = true;

	/**
	 * Returns default <code>Comparator</code> for comparing all objects
	 * including <code>null</code> references.
	 * 
	 * @return default <code>Comparator</code> for comparing all objects
	 *         including <code>null</code> references.
	 */
	public static Comparator getDefault() {
		return getDefault(NULL_FIRST_BY_DEFAULT);
	}

	/**
	 * Returns default <code>Comparator</code> for comparing all objects
	 * including <code>null</code> values.
	 * 
	 * @param nullFirst
	 *            whether the <code>null</code> value is ordered before
	 *            not-null values.
	 * @return default <code>Comparator</code> for comparing all objects
	 *         including <code>null</code> references.
	 */
	public static Comparator getDefault(boolean nullFirst) {
		return new NullComparator(ComparableComparator.INSTANCE, nullFirst);
	}

	/**
	 * Returns the <code>Comparator</code> for <code>String</code> objects
	 * including <code>null</code> values.
	 * 
	 * @param nullFirst
	 *            whether the <code>null</code> value is ordered before
	 *            not-null values.
	 * @param ignoreCase
	 *            whether the Comparator should be case insensitive.
	 * @param locale
	 *            the <code>Locale</code> of the Comparator or
	 *            <code>null</code> if the Comparator should not be
	 *            Locale-specific.
	 * @return the <code>Comparator</code> for <code>String</code> objects
	 *         including <code>null</code> values.
	 */
	public static Comparator<String> getStringComparator(boolean nullFirst,
			boolean ignoreCase, Locale locale) {
		return new NullComparator<String>(
				getNotNullStringComparator(ignoreCase, locale), nullFirst);
	}

	/**
	 * Returns the <code>Comparator</code> for <code>String</code> objects
	 * excluding <code>null</code> values.
	 * 
	 * @param ignoreCase
	 *            whether the Comparator should be case insensitive.
	 * @param locale
	 *            the <code>Locale</code> of the Comparator or
	 *            <code>null</code> if the Comparator should not be
	 *            Locale-specific.
	 * @return the <code>Comparator</code> for <code>String</code> objects
	 *         excluding <code>null</code> values.
	 */
	protected static Comparator<String> getNotNullStringComparator(boolean ignoreCase,
			Locale locale) {
		if (locale != null) {
			return new LocaleStringComparator(ignoreCase, locale);
		}
		if (ignoreCase) {
			return IgnoreCaseComparator.INSTANCE;
		}
		return ComparableComparator.INSTANCE;
	}

	/**
	 * Returns the <code>Comparator</code> for <code>Boolean</code> objects
	 * including <code>null</code> values.
	 * 
	 * @param nullFirst
	 *            whether the <code>null</code> value is ordered before
	 *            not-null values.
	 * @param trueFirst
	 *            whether the <code>true</code> value should be ordered before
	 *            the <code>false</code> value.
	 * @return the <code>Comparator</code> for <code>Boolean</code> objects
	 *         including <code>null</code> values.
	 */
	public static Comparator<Boolean> getBooleanComparator(boolean nullFirst,
			boolean trueFirst) {
		return new NullComparator<Boolean>(trueFirst ? BooleanComparator.TRUE_FIRST
				: BooleanComparator.FALSE_FIRST, nullFirst);
	}
}
