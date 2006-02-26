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

package org.araneaframework.uilib.list.structure;

import java.util.Comparator;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.araneaframework.uilib.list.util.ComparatorFactory;
import org.araneaframework.uilib.list.util.comparator.ComparableComparator;
import org.araneaframework.uilib.list.util.comparator.NullComparator;


public abstract class ComparableType {
	
	private static final Logger log = Logger.getLogger(ComparableType.class);

	private Comparator comparator;
	private Class valueType;
	
	private boolean ignoreCase = ComparatorFactory.IGNORE_CASE_BY_DEFAULT;
	private boolean trueFirst = ComparatorFactory.TRUE_FIRST_BY_DEFAULT;
	private boolean nullFirst = ComparatorFactory.NULL_FIRST_BY_DEFAULT;
	private Locale locale = null;

	public Comparator getComparator() {
		chooseComparator();
		return this.comparator;
	}
	
	public boolean isComparatorNatural() {
		chooseComparator();
		
		Comparator comp = this.comparator;
		if (comp instanceof NullComparator) {
			comp = ((NullComparator) comp).getNotNullComparator();
		}
		
		if (comp instanceof ComparableComparator) {
			return true;
		}
		return false;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}
	
	public boolean isIgnoreCase() {
		return this.ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public boolean isTrueFirst() {
		return this.trueFirst;
	}

	public void setTrueFirst(boolean trueFirst) {
		this.trueFirst = trueFirst;
	}

	public boolean isNullFirst() {
		return this.nullFirst;
	}

	public void setNullFirst(boolean nullFirst) {
		this.nullFirst = nullFirst;
	}

	public Class getValueType() {
		return this.valueType;
	}

	public void setValueType(Class valueType) {
		this.valueType = valueType;
	}

	private void chooseComparator() {
		if (this.comparator != null) {
			return;
		}
				
		if (this.valueType != null) {
			if (String.class.isAssignableFrom(this.valueType)) {
				this.comparator = ComparatorFactory.getStringComparator(this.nullFirst,
						this.ignoreCase, this.locale);
				return;
			}
			if (Boolean.class.isAssignableFrom(this.valueType)) {
				this.comparator = ComparatorFactory.getBooleanComparator(this.nullFirst,
						this.trueFirst);
				return;
			}			
		}
		this.comparator = ComparatorFactory.getDefault();
	}
}
