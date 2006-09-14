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
package org.araneaframework.uilib.list.structure.filter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.araneaframework.Environment;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.structure.TypeHelper;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;

/**
 * Base implementation of list filter helper.  
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 */
public abstract class BaseFilterHelper implements FilterContext {
	
	public static final String LOW_SUFFIX = "_start"; 
	public static final String HIGH_SUFFIX = "_end"; 
	
	protected final ListWidget list;
	protected final TypeHelper typeHelper;
	
	private boolean strict = false;	
	private LikeConfiguration likeConfiguration;
	
	// Map<String,String> - exceptional labels for fields
	private Map labels = new HashMap();
	
	public BaseFilterHelper(ListWidget list) {
		Validate.notNull(list);
		this.list = list;
		this.typeHelper = list.getTypeHelper();
	}
	
	public boolean isIgnoreCase() {
		return typeHelper.isIgnoreCase();
	}

	public FilterContext setIgnoreCase(boolean ignoreCase) {
		typeHelper.setIgnoreCase(ignoreCase);
		return this;
	}

	public Locale getLocale() {
		return typeHelper.getLocale();
	}

	public FilterContext setLocale(Locale locale) {
		typeHelper.setLocale(locale);
		return this;
	}
	
	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean stirct) {
		this.strict = stirct;
	}

	public LikeConfiguration getLikeConfiguration() {
		return likeConfiguration;
	}
	
	public void setLikeConfiguration(LikeConfiguration likeConfiguration) {
		this.likeConfiguration = likeConfiguration;
	}
	
	// General
	
	public Environment getEnvironment() {
		return list.getEnvironment();
	}

	public FormWidget getForm() {
		return list.getForm();
	}
		
	// List fields
	
	public BaseFilterHelper addCustomLabel(String fieldId, String labelId) {
		this.labels.put(fieldId, labelId);
		return this;
	}
	
	public BaseFilterHelper addCustomType(String fieldId, Class type) {
		typeHelper.addCustomType(fieldId, type);
		return this;
	}
	
	public BaseFilterHelper addCustomComparator(String fieldId, Comparator comp) {
		typeHelper.addCustomComparator(fieldId, comp);
		return this;
	}
	
	public String getFieldLabel(String fieldId) {
		if (this.labels.containsKey(fieldId)) {
			return (String) this.labels.get(fieldId);
		}
		return list.getFieldLabel(fieldId);
	}
	
	public Class getFieldType(String fieldId) {
		return typeHelper.getFieldType(fieldId);
	}
	
	public Comparator getFieldComparator(String fieldId) {
		return typeHelper.getFieldComparator(fieldId);
	}
	
	// Value ids
	
	public String getValueId(String fieldId) {
		return fieldId;
	}
	public String getLowValueId(String fieldId) {
		return fieldId + LOW_SUFFIX;
	}
	public String getHighValueId(String fieldId) {
		return fieldId + HIGH_SUFFIX;
	}
}
