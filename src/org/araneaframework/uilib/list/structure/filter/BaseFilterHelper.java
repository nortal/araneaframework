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

import org.araneaframework.uilib.util.UilibEnvironmentUtil;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.araneaframework.Environment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.TypeHelper;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.util.Event;

/**
 * Base implementation of list filter helper. Filter helper is used to add
 * filters and their form elements to the {@link ListWidget}.
 * <p>
 * This class handles configuration for new filter such as strictness (e.g equal
 * is not allowed for greater than filter) as well as allow to predefine custom
 * labels for fields that are not added to the standard list of list fields but
 * are used in filter form elements instead.
 * </p>
 * <p>
 * The handling of field types and their comparator is proxied to
 * {@link TypeHelper} of the list widget. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 * @see FilterHelper
 * @see TypeHelper
 */
public abstract class BaseFilterHelper implements FilterContext, Serializable {

	/** Field id suffix for range filter's low/start/min value */
	public static final String LOW_SUFFIX = "_start"; 
	/** Field id suffix for range filter's high/end/max value */
	public static final String HIGH_SUFFIX = "_end"; 

	/** The associated list */
	protected final ListWidget list;

	private boolean strict = false;	

	// Map<String,String> - custom labels for fields
	private Map labels = new HashMap();
	
	private boolean changed = true;
	
	/**
	 * Constructs a {@link BaseFilterHelper}.
	 * 
	 * @param list list.
	 */
	public BaseFilterHelper(ListWidget list) {
		this(list, true);
	}
	
	private BaseFilterHelper(ListWidget list, boolean createCopy) {
		Validate.notNull(list);
		this.list = list;
	}
	
	public void init(Environment env) throws Exception {}
	
	public void destroy() throws Exception {}
	
	public void addInitEvent(Event event) {
		this.list.addInitEvent(event);
	}

	/**
	 * Returns the current case sensitivity behaivor.
	 * 
	 * @return the current case sensitivity behaivor.
	 */
	public boolean isIgnoreCase() {
		return getTypeHelper().isIgnoreCase();
	}

	/**
	 * Sets the current case sensitivity behaivor.
	 * 
	 * @param ignoreCase whether to ignore case.
	 */
	protected void _setIgnoreCase(boolean ignoreCase) {
		TypeHelper helper = getTypeHelper();
		if (helper.isIgnoreCase() != ignoreCase) {
			getTypeHelper().setIgnoreCase(ignoreCase);
			fireChange();
		}
	}

	/**
	 * Returns the current locale.
	 * 
	 * @return the current locale.
	 */
	public Locale getLocale() {
		return getTypeHelper().getLocale();
	}
	
//	/**
//	 * Sets the current locale.
//	 * 
//	 * @param locale new locale.
//	 */
//	protected void _setLocale(Locale locale) {
//		getTypeHelper().setLocale(locale);
//	}

	/**
	 * Returns whether new filters should be strict.
	 * <p>
	 * E.g. when adding e GreaterThan filter, strict does not allow two values
	 * to be equal.
	 * 
	 * @return whether new filters should be strict.
	 */
	public boolean isStrict() {
		return strict;
	}

	/**
	 * Sets the current strickness behaivor.
	 * 
	 * @param strict whether new filters should be strict.
	 */
	protected void _setStrict(boolean strict) {
		if (strict != this.strict) {
			this.strict = strict;
			fireChange();
		}
	}

	// General

	/**
	 * Returns the associated {@link TypeHelper}.
	 * 
	 * @return TypeHelper.
	 */
	protected TypeHelper getTypeHelper() {
		return this.list.getTypeHelper();
	}

	/**
	 * Retrieves the global configuration context.
	 * 
	 * @return the global configuration context.
	 */
	public ConfigurationContext getConfiguration() {
		return UilibEnvironmentUtil.getConfiguration(this.list.getEnvironment());
	}

	/**
	 * Returns the localization context.
	 * 
	 * @return the localization context.
	 */
	protected LocalizationContext getL10nCtx() {		
		return (LocalizationContext) this.list.getEnvironment().requireEntry(LocalizationContext.class);
	}

	/**
	 * Returns the filter form.
	 * 
	 * @return the filter form.
	 */
	public FormWidget getForm() {
		return list.getForm();
	}

	// List fields

	/**
	 * Adds custom label for specified field. This can override already defined
	 * label of list field. Those labels are used by new filter form elements
	 * that are automatically created for list filters. 
	 * 
	 * @param fieldId field id.
	 * @param labelId label id (not yet resolved).
	 */
	protected void _addCustomLabel(String fieldId, String labelId) {
		this.labels.put(fieldId, labelId);
		fireChange();
	}
	/**
	 * Retrieves label for the specified field. If there are now custom label
	 * defined, one is retrieved from list fields defined in {@link ListWidget}.
	 * <p>
	 * Range filter's low and high labels are automatically generated based on
	 * the original field itself.
	 */
	public String getFieldLabel(String fieldId) {
		String result = (String) this.labels.get(fieldId);
		if (result == null) {
			result = list.getFieldLabel(fieldId);
		}

		if (result == null) {
			if (fieldId.endsWith(LOW_SUFFIX)) {
				String listFieldId = getFieldIdFromLowValueId(fieldId);
				String fieldLabel = (String) this.labels.get(listFieldId);
				if (fieldLabel == null) fieldLabel = list.getFieldLabel(listFieldId);

				result = FilterFormUtil.getLabelForLowField(getL10nCtx(), fieldLabel);
			} else if (fieldId.endsWith(HIGH_SUFFIX)) {
				String listFieldId = getFieldIdFromHighValueId(fieldId);
				String fieldLabel = (String) this.labels.get(listFieldId);
				if (fieldLabel == null) fieldLabel = list.getFieldLabel(listFieldId);
				
				result = FilterFormUtil.getLabelForHighField(getL10nCtx(), fieldLabel);
			}
		}
		return result;
	}

	/**
	 * Defines type for specified field.
	 * 
	 * @param fieldId field id.
	 * @param type field type.
	 * 
	 * @see TypeHelper#addFieldType(String, Class)
	 */
	protected void _addFieldType(String fieldId, Class type) {
		getTypeHelper().addFieldType(fieldId, type);
		fireChange();
	}
	/**
	 * Retrieves type for the specified field.
	 * <p>
	 * If type is not defined for range filter's low/high value, the
	 * range filter field type is returned.
	 * </p>
	 * <p>
	 * Otherwise {@link TypeHelper#getFieldType(String)} is just called.
	 * </p>
	 * 
	 * @see TypeHelper#getFieldType(String)
	 */
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

	/**
	 * Adds custom comparator for the specified field. This just proxies the
	 * call to TypeHelper#addCustomComparator(String, Comparator).
	 * 
	 * @param fieldId field id.
	 * @param comp custom comparator.
	 * 
	 * @see TypeHelper#addCustomComparator(String, Comparator)
	 */
	public void addCustomComparator(String fieldId, Comparator comp) {
		getTypeHelper().addCustomComparator(fieldId, comp);
		fireChange();
	}
	/**
	 * Retrieves comparator for the specified field. This just proxies the call
	 * to {@link TypeHelper#getFieldComparator(String)}.
	 * 
	 * @see TypeHelper#getFieldComparator(String)
	 */
	public Comparator getFieldComparator(String fieldId) {
		Comparator result = getTypeHelper().getFieldComparator(fieldId);
		if (result == null) {
			if (fieldId.endsWith(LOW_SUFFIX)) {				
				result = getFieldComparator(getFieldIdFromLowValueId(fieldId));
			} else if (fieldId.endsWith(HIGH_SUFFIX)) {
				result = getFieldComparator(getFieldIdFromHighValueId(fieldId));
			}
		}
		return result;
	}

	// Value ids

	/**
	 * Transforms the field id into value id.
	 * <p>
	 * The same string is returned. 
	 * 
	 * @param fieldId field id.
	 * @return value id.
	 */
	public String getValueId(String fieldId) {
		return fieldId;
	}
	/**
	 * Transform the field id into range filter's low value id.
	 * <p>
	 * The {@link #LOW_SUFFIX} is appended to the field id.
	 * 
	 * @param fieldId field id.
	 * @return low value id.
	 */
	public String getLowValueId(String fieldId) {
		return fieldId + LOW_SUFFIX;
	}
	/**
	 * Transform the field id into range filter's high value id.
	 * <p>
	 * The {@link #HIGH_SUFFIX} is appended to the field id.
	 * 
	 * @param fieldId field id.
	 * @return high value id.
	 */
	public String getHighValueId(String fieldId) {
		return fieldId + HIGH_SUFFIX;
	}
	
	/**
	 * Transforms the value id into field id.
	 * <p>
	 * The same string is returned. 
	 * 
	 * @param valueId value id.
	 * @return field id.
	 */
	public String getFieldId(String valueId) {
		return valueId;
	}
	/**
	 * Transforms the range filter's low value id into field id.
	 * <p>
	 * The {@link #LOW_SUFFIX} is removed from the end.
	 * 
	 * @param lowValueId low value id.
	 * @return field id.
	 */
	public String getFieldIdFromLowValueId(String lowValueId) {
		return StringUtils.substringBeforeLast(lowValueId, LOW_SUFFIX);
	}
	/**
	 * Transforms the range filter's high value id into field id.
	 * <p>
	 * The {@link #HIGH_SUFFIX} is removed from the end.
	 * 
	 * @param highValueId high value id.
	 * @return field id.
	 */
	public String getFieldIdFromHighValueId(String highValueId) {
		return StringUtils.substringBeforeLast(highValueId, HIGH_SUFFIX);
	}
	
  /** 
   * @since 1.1
   */
	protected void fireChange() {
		changed = true;
	}
	
	/**
	 * Returns whether the basic configuration that specifies which items are
	 * shown has changed since last call to this {@link BaseFilterHelper}'s {@link BaseFilterHelper#checkChanged()} 
	 * method.
   * 
   * @since 1.1
	 */
	public boolean checkChanged() {
		boolean result = changed;
		changed = false;
		return result;
	}
}
