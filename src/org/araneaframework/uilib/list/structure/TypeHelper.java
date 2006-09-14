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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.araneaframework.Environment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.util.ComparatorFactory;

/**
 * List fields types and comparators helper.  
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 */
public class TypeHelper {
	
	protected final ListWidget list;
	
	// Configuration
	private Locale locale;
	private boolean ignoreCase = true;
	
	// Map<String,Comparator> - exceptional comparators for fields
	private Map comparators = new HashMap();
	// Map<String,Class> - exceptional types for fields
	private Map types = new HashMap();
	
	public TypeHelper(ListWidget list) {
		Validate.notNull(list);
		this.list = list;
		init(list.getEnvironment());		
	}
	
	protected void init(Environment env) {
		// Locale
		this.locale = ((LocalizationContext)
				env.getEntry(LocalizationContext.class)).getLocale();
	}
	
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public TypeHelper setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	public Locale getLocale() {
		return locale;
	}

	public TypeHelper setLocale(Locale locale) {
		this.locale = locale;
		return this;
	}
		
	// List fields
	
	public TypeHelper addCustomType(String fieldId, Class type) {
		this.types.put(fieldId, type);
		return this;
	}
	
	public TypeHelper addCustomComparator(String fieldId, Comparator comp) {
		this.comparators.put(fieldId, comp);
		return this;
	}
	
	public Class getFieldType(String fieldId) {
		if (this.types.containsKey(fieldId)) {
			return (Class) this.types.get(fieldId);
		}
		return list.getFieldType(fieldId);
	}
	
	public Comparator getFieldComparator(String fieldId) {
		if (this.comparators.containsKey(fieldId)) {
			return (Comparator) this.comparators.get(fieldId);
		}
		return buildComparator(getFieldType(fieldId));
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
		if (Boolean.class.equals(type)) {
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
