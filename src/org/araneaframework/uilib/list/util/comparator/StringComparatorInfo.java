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

package org.araneaframework.uilib.list.util.comparator;

import java.util.Locale;

/**
 * String comparator description interface.
 */
public class StringComparatorInfo implements ComparatorInfo {
	
	private final Locale locale;
	private final boolean ignoreCase;
	
	/**
	 * Creates new instance of {@link StringComparatorInfo}.
	 * 
	 * @param locale <code>Locale</code>.
	 * @param ignoreCase whether the <code>Comparator</code> is case
	 *         insensitive.
	 */
	public StringComparatorInfo(Locale locale, boolean ignoreCase) {
		this.locale = locale;
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Returns <code>true</code> if the <code>Comparator</code> is case
	 * insensitive.
	 * 
	 * @return <code>true</code> if the <code>Comparator</code> is case
	 *         insensitive.
	 */
	public boolean getIgnoreCase() {
		return this.ignoreCase;
	}

	/**
	 * Returns the <code>Locale</code> of the <code>Comparator</code>.
	 * 
	 * @return the <code>Locale</code> of the <code>Comparator</code>.
	 */
	public Locale getLocale() {
		return this.locale;
	}
}
