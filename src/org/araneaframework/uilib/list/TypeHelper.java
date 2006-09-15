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
package org.araneaframework.uilib.list;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.araneaframework.uilib.list.util.ComparatorFactory;

/**
 * List fields types and comparators helper.  
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 */
public class TypeHelper {
	
	// Configuration
	private Locale locale;
	private boolean ignoreCase = true;
	
	// Map<String,Comparator> - custom comparators for fields
	private Map comparators = new HashMap();
	// Map<String,Class> - field types
	private Map types = new HashMap();
	
	public TypeHelper(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		this.locale = locale;
	}
	
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
		
	// List fields
	
	/**
	 * Returns a comparator for the specifeid field.
	 * <p>
	 * First, a custom comparator is returned if found.
	 * <p>
	 * </p>
	 * Otherwise a comparator is tryed to create according to the field type
	 * returned by {@link #getFieldType(String)}.
	 * Also {@link #isIgnoreCase()} and {@link #getLocale()} is considered for
	 * creating the new comparator.
	 * 
	 * @param fieldId
	 *                field Id.
	 * @return comparator for this field.
	 */
	public Comparator getFieldComparator(String fieldId) {
		Comparator result = getCustomComparator(fieldId);
		if (result == null) {
			result = buildComparator(getFieldType(fieldId)); 
		}
		return result;
	}
	
	public void addFieldType(String fieldId, Class type) {
		this.types.put(fieldId, type);
	}
	
	/**
	 * Returns the field type.
	 * <p>
	 * Returns <code>null</code> if no type specifeid for this field or no
	 * such field exists.
	 * 
	 * @param fieldId
	 *                field Id.
	 * @return type of this field.
	 */
	public Class getFieldType(String fieldId) {
		return (Class) this.types.get(fieldId);
	}
	
	public Class removeFieldType(String fieldId) {
		return (Class) this.types.remove(fieldId);
	}

	public void addCustomComparator(String fieldId, Comparator comp) {
		this.comparators.put(fieldId, comp);
	}
	
	public Comparator getCustomComparator(String fieldId) {
		return (Comparator) this.comparators.get(fieldId);
	}
		
	public Comparator removeCustomComparator(String fieldId) {
		return (Comparator) this.comparators.remove(fieldId);
	}
	
	// Comparator
	
	protected Comparator buildComparator(Class type) {
		Validate.notNull(type);
		
		if (String.class.equals(type)) {
			return ComparatorFactory.getStringComparator(
					isNullFirst(),
					isIgnoreCase(),
					getLocale());
		}
		// Boolean is Comparable since Java 1.5
		if (Boolean.class.equals(type) &&
				!Boolean.class.isAssignableFrom(Comparable.class)) {
			return ComparatorFactory.getBooleanComparator(
					isNullFirst(),
					isTrueFirst());
		}
		return ComparatorFactory.getDefault();
	}	
	protected boolean isNullFirst() {
		return ComparatorFactory.NULL_FIRST_BY_DEFAULT;
	}	
	protected boolean isTrueFirst() {
		return ComparatorFactory.TRUE_FIRST_BY_DEFAULT;
	}
}
