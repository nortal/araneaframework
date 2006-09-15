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

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.TypeHelper;
import org.araneaframework.uilib.list.util.FilterFormUtil;

/**
 * Base implementation of list filter helper.  
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 */
public abstract class BaseFilterHelper implements FilterContext, Serializable {

	public static final String LOW_SUFFIX = "_start"; 
	public static final String HIGH_SUFFIX = "_end"; 

	protected final ListWidget list;

	private boolean strict = false;	

	// Map<String,String> - custom labels for fields
	private Map labels = new HashMap();

	public BaseFilterHelper(ListWidget list) {
		Validate.notNull(list);
		this.list = list;
	}

	public boolean isIgnoreCase() {
		return getTypeHelper().isIgnoreCase();
	}

	public FilterContext setIgnoreCase(boolean ignoreCase) {
		getTypeHelper().setIgnoreCase(ignoreCase);
		return this;
	}

	public Locale getLocale() {
		return getTypeHelper().getLocale();
	}

	public FilterContext setLocale(Locale locale) {
		getTypeHelper().setLocale(locale);
		return this;
	}

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean stirct) {
		this.strict = stirct;
	}

	// General

	protected TypeHelper getTypeHelper() {
		return this.list.getTypeHelper();
	}

	public ConfigurationContext getConfiguration() {
		return this.list.getConfiguration();
	}

	protected LocalizationContext getL10nCtx() {
		return (LocalizationContext) this.list.getEnvironment().requireEntry(LocalizationContext.class);
	}

	public FormWidget getForm() {
		return list.getForm();
	}

	// List fields

	public BaseFilterHelper addCustomLabel(String fieldId, String labelId) {
		this.labels.put(fieldId, labelId);
		return this;
	}	
	public String getFieldLabel(String fieldId) {
		String result = (String) this.labels.get(fieldId);
		if (result == null) {
			result = list.getFieldLabel(fieldId);
		}
		if (result == null) {
			if (fieldId.endsWith(LOW_SUFFIX)) {				
				result = FilterFormUtil.getLabelForLowField(getL10nCtx(),
						getFieldIdFromLowValueId(fieldId));
			} else if (fieldId.endsWith(HIGH_SUFFIX)) {
				result = FilterFormUtil.getLabelForHighField(getL10nCtx(),
						getFieldIdFromHighValueId(fieldId));
			}
		}
		return result;
	}

	public BaseFilterHelper addFieldType(String fieldId, Class type) {
		getTypeHelper().addFieldType(fieldId, type);
		return this;
	}
	public Class getFieldType(String fieldId) {
		Class result = getTypeHelper().getFieldType(fieldId);
		if (result == null) {
			if (fieldId.endsWith(LOW_SUFFIX)) {				
				result = getFieldType(getFieldIdFromLowValueId(fieldId));
			} else if (fieldId.endsWith(HIGH_SUFFIX)) {
				result = getFieldType(getFieldIdFromHighValueId(fieldId));
			}
		}
		return result;
	}

	public BaseFilterHelper addCustomComparator(String fieldId, Comparator comp) {
		getTypeHelper().addCustomComparator(fieldId, comp);
		return this;
	}	
	public Comparator getFieldComparator(String fieldId) {
		return getTypeHelper().getFieldComparator(fieldId);
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
	
	public String getFieldIdFromLowValueId(String lowValueId) {
		return StringUtils.substringBeforeLast(lowValueId, LOW_SUFFIX);
	}
	public String getFieldIdFromHighValueId(String highValueId) {
		return StringUtils.substringBeforeLast(highValueId, HIGH_SUFFIX);
	}
}
