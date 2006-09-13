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
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.structure.filter.column.EqualFilter;
import org.araneaframework.uilib.list.structure.filter.column.LikeFilter;
import org.araneaframework.uilib.list.util.ComparatorFactory;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;

/**
 * List filter helper.  
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 */
public class FilterHelper implements FilterContext {
	
	private final ListWidget list;
	
	// Configuration that can be changed during filters creation
	private Locale locale;
	private boolean ignoreCase;
	
	private LikeConfiguration likeConfiguration;
	
	// Map<String,String> - exceptional labels for fields
	private Map labels = new HashMap();
	// Map<String,Comparator> - exceptional comparators for fields
	private Map comparators = new HashMap();
	// Map<String,Class> - exceptional types for fields
	private Map types = new HashMap();
	
	public FilterHelper(ListWidget list) {
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

	public FilterContext setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	public Locale getLocale() {
		return locale;
	}

	public FilterContext setLocale(Locale locale) {
		this.locale = locale;
		return this;
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
		return list.getFilterForm();
	}
		
	// List fields
	
	public FilterContext addCustomLabel(String fieldId, String labelId) {
		this.labels.put(fieldId, labelId);
		return this;
	}
	
	public FilterContext addCustomType(String fieldId, Class type) {
		this.types.put(fieldId, type);
		return this;
	}
	
	public FilterContext addCustomComparator(String fieldId, Comparator comp) {
		this.comparators.put(fieldId, comp);
		return this;
	}
	
	public String getFieldLabel(String fieldId) {
		if (this.labels.containsKey(fieldId)) {
			return (String) this.labels.get(fieldId);
		}
		return list.getColumnLabel(fieldId);
	}
	
	public Class getFieldType(String fieldId) {
		if (this.types.containsKey(fieldId)) {
			return (Class) this.types.get(fieldId);
		}
		return list.getColumnType(fieldId);
	}
	
	public Comparator getComparator(String fieldId) {
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
	
	// ========== EQUALS ========== 
	
	// filter with form element
	
	public FilterContext eq(String fieldId) throws Exception {
		return eq(fieldId, fieldId);
	}
	public FilterContext eq(String fieldId, String valueId) throws Exception {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId);
		return this;
	}
	public FilterContext eq(String fieldId, Control control) throws Exception {
		return eq(fieldId, fieldId, control);
	}
	public FilterContext eq(String fieldId, String valueId, Control control) throws Exception {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterContext eq(String fieldId, FormElement element) throws Exception {
		return eq(fieldId, fieldId, element);
	}
	public FilterContext eq(String fieldId, String valueId, FormElement element) throws Exception {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterContext _eq(String fieldId) {
		return _eq(fieldId, fieldId);
	}
	public FilterContext _eq(String fieldId, String valueId) {
		list.addFilter(EqualFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

	public FilterContext eqConst(String fieldId, Object value) {
		return eqConst(fieldId, fieldId, value);
	}
	public FilterContext eqConst(String fieldId, String valueId, Object value) {
		list.addFilter(EqualFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}
	
	// ========== LIKE ========== 
	
	// filter with form element
	
	public FilterContext like(String fieldId) throws Exception {
		return like(fieldId, fieldId);
	}
	public FilterContext like(String fieldId, String valueId) throws Exception {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId);
		return this;
	}
	public FilterContext like(String fieldId, Control control) throws Exception {
		return like(fieldId, fieldId, control);
	}
	public FilterContext like(String fieldId, String valueId, Control control) throws Exception {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterContext like(String fieldId, FormElement element) throws Exception {
		return like(fieldId, fieldId, element);
	}
	public FilterContext like(String fieldId, String valueId, FormElement element) throws Exception {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterContext _like(String fieldId) {
		return _like(fieldId, fieldId);
	}
	public FilterContext _like(String fieldId, String valueId) {
		list.addFilter(LikeFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

	public FilterContext likeConst(String fieldId, Object value) {
		return likeConst(fieldId, fieldId, value);
	}
	public FilterContext likeConst(String fieldId, String valueId, Object value) {
		list.addFilter(LikeFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}
}
