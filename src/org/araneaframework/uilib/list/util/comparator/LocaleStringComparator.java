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

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

/**
 * Comparator for not-null <code>String</code> values.
 */
class LocaleStringComparator implements Serializable {

	private Collator collator;

	/**
	 * Creates an instance of LocaleStringComparator.
	 * 
	 * @param ignoreCase
	 *               whether to ignore case.
	 * @param locale
	 *               Locale.
	 */
	public LocaleStringComparator(boolean ignoreCase, Locale locale) {
		this.collator = Collator.getInstance(locale);
		this.collator.setStrength(ignoreCase ? Collator.SECONDARY
				: Collator.TERTIARY);
	}

	public int compare(Object o1, Object o2) {
		return this.collator.compare(o1, o2);
	}
	
    public int hashCode() {
        return collator.hashCode();
    }

    public boolean equals(Object object) {
    	return object == this || 
    	(object instanceof LocaleStringComparator && collator.equals(((LocaleStringComparator) object).collator));
    }	
}
