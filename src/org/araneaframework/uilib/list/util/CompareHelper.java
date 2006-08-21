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

import org.apache.commons.collections.comparators.BooleanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.Validate;
import org.araneaframework.uilib.ConfigurationContext;


/**
 * {@link Comparator} building utils.
 */
public class CompareHelper {

	/**
	 * Whether the <code>null</code> value should be ordered after not-null
	 * values by default.
	 */
	public static final boolean NULLS_ARE_HIGH_BY_DEFAULT = true;

	/**
	 * Whether the <code>Boolean</code> objects <code>Comparator</code>
	 * should order the <code>true</code> value before the <code>false</code>
	 * value by default.
	 */
	public static final boolean TRUE_FIRST_BY_DEFAULT = false;

	/**
	 * Whether the <code>String</code> objects <code>Comparator</code>
	 * should be case insensitve by default.
	 */
	public static final boolean IGNORE_CASE_BY_DEFAULT = true;
	
	private Class type;
	private Locale locale;

	private boolean nullsAreHigh = NULLS_ARE_HIGH_BY_DEFAULT;
	private boolean trueFirst = TRUE_FIRST_BY_DEFAULT;
	private boolean ignoreCase = IGNORE_CASE_BY_DEFAULT;
	
	/**
	 * Initializes <code>ComparatorHelper</code>.
	 * 
	 * @param type Object <code>type</code> of Comparator.
	 * @param conf configuration (can be null).
	 * @param locale current Locale (can be null).
	 * @param customIgnoreCase whether to compare case insensitive (can be null).
	 */
	public CompareHelper(Class type, ConfigurationContext conf, Locale locale, Boolean ignoreCase) {
		Validate.notNull(type, "No type specified");
		
		this.type = type;
		this.locale = locale;
		if (ignoreCase != null) {
			this.ignoreCase = ignoreCase.booleanValue();
		}
		
		if (conf != null) {			
			if (conf.getEntry(ConfigurationContext.COMPARATOR_NULLS_ARE_HIGH) != null) {
				nullsAreHigh = ((Boolean) conf.getEntry(ConfigurationContext.COMPARATOR_NULLS_ARE_HIGH)).booleanValue();
			}
			if (conf.getEntry(ConfigurationContext.COMPARATOR_TRUE_FIRST) != null) {
				trueFirst = ((Boolean) conf.getEntry(ConfigurationContext.COMPARATOR_TRUE_FIRST)).booleanValue();
			}
			if (ignoreCase == null && conf.getEntry(ConfigurationContext.COMPARATOR_IGNORE_CASE) != null) {
				this.ignoreCase = ((Boolean) conf.getEntry(ConfigurationContext.COMPARATOR_IGNORE_CASE)).booleanValue();
			}
		}
	}

	
	/**
	 * Initializes <code>ComparatorHelper</code>.
	 * 
	 * @param type Object <code>type</code> of Comparator.
	 * @param conf configuration (can be null).
	 * @param locale current Locale (can be null).
	 */
	public CompareHelper(Class type, ConfigurationContext conf, Locale locale) {
		this(type, conf, locale, null);
	}
	
	/**
	 * Returns {@link Comparator} for the <code>type</code>.
	 * 
	 * @return {@link Comparator} for the <code>type</code>.
	 */
	public Comparator getComparator() {
		Comparator notNullComparator = null;
		if (type != null) {
			if (String.class.equals(type.getClass())) {
				notNullComparator = StringComparatorUtil.stringComparator(locale, ignoreCase);
			}
			if (Boolean.class.equals(type.getClass())) {
				notNullComparator = new BooleanComparator(trueFirst);
			}
		}
		if (notNullComparator == null) {
			notNullComparator = new ComparableComparator();
		}
		return new NullComparator(notNullComparator, nullsAreHigh);
	}
	
	/**
	 * Returns whether the {@link Comparator} of the <code>type</code> is
	 * {@link ComparableComparator} (it may also be contained in
	 * {@link NullComparator}).
	 * 
	 * @return whether the {@link Comparator} of the <code>type</code> is
	 * {@link ComparableComparator}
	 */
	public boolean isComparableComparator() {
		if (String.class.equals(type.getClass())) {
			return locale == null && ignoreCase == false;
		}
		if (Boolean.class.equals(type.getClass())) {
			return false;
		}
		return true;
	}
}
