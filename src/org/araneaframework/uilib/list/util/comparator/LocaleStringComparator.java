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

import java.io.IOException;
import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;
import org.araneaframework.core.Assert;

/**
 * Not-null comparator that compares <code>String</code> values according to
 * the <code>Locale</code> and case sensitive option.
 */
public class LocaleStringComparator implements StringComparator, Serializable {
	private boolean ignoreCase;
	private Locale locale;

	private transient Collator collator;

	public LocaleStringComparator(boolean ignoreCase, Locale locale) {
		Assert.notNullParam(locale, "locale");

		this.ignoreCase = ignoreCase;
		this.locale = locale;

		initCollator();
	}
	
	private void initCollator() {
		this.collator = Collator.getInstance(locale);
		this.collator.setStrength(ignoreCase ? Collator.SECONDARY
				: Collator.TERTIARY);
	}

	public boolean getIgnoreCase() {
		return this.ignoreCase;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public int compare(Object o1, Object o2) {
		return this.collator.compare(o1, o2);
	}
	
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    	in.defaultReadObject();
    	initCollator();
    }
}
