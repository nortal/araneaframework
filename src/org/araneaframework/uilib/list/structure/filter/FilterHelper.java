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

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.backend.list.memorybased.ExpressionBuilder;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringListData;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.TypeHelper;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.filter.advanced.RangeInRangeFilter;
import org.araneaframework.uilib.list.structure.filter.advanced.SqlFunctionFilter;
import org.araneaframework.uilib.list.structure.filter.atomic.Constant;
import org.araneaframework.uilib.list.structure.filter.atomic.Field;
import org.araneaframework.uilib.list.structure.filter.atomic.Value;
import org.araneaframework.uilib.list.structure.filter.field.EndsWithFilter;
import org.araneaframework.uilib.list.structure.filter.field.EqualFilter;
import org.araneaframework.uilib.list.structure.filter.field.GreaterThanFilter;
import org.araneaframework.uilib.list.structure.filter.field.InFilter;
import org.araneaframework.uilib.list.structure.filter.field.LikeFilter;
import org.araneaframework.uilib.list.structure.filter.field.LowerThanFilter;
import org.araneaframework.uilib.list.structure.filter.field.NullFilter;
import org.araneaframework.uilib.list.structure.filter.field.RangeFilter;
import org.araneaframework.uilib.list.structure.filter.field.StartsWithFilter;
import org.araneaframework.uilib.list.util.FormUtil;

/**
 * Standard list filter helper. This class is used to add <b>filters</b> and
 * their <b>form elements</b> to the {@link ListWidget}.
 * <p>
 * THe following filters can be added to the list:
 * {@link EqualFilter},
 * {@link GreaterThanFilter},
 * {@link LowerThanFilter},
 * {@link LikeFilter},
 * {@link NullFilter},
 * {@link RangeFilter},
 * {@link RangeInRangeFilter},
 * {@link SqlFunctionFilter}.
 * </p>
 * <p>
 * There are many methods for adding each filter and their form elements.
 * Note that some methods start with the <b>_</b> sign. These add filters 
 * without adding form elements to them. Use this only if its inevitable.
 * The default behaivor is to add the filter and its form elements at once to
 * enable filter specific form elements (e.g {@link LikeFilter} uses
 * {@link TextControl} ignoring the field type defined by the list) and avoid
 * many mistakes.
 * </p>
 * <p>
 * Also many filters can be added as constant filters. This means
 * they are just part of the query but do not depend on the user. For example
 * one would like to add constant filter to get only clients from one region
 * without showing this filter to the user or letting it to be modified.
 * </p>
 * <p>
 * In other cases, <b>filters are added with form elements</b> no matter if one
 * pass it within the method arguments or no. For each form element (e.g 2 in
 * case of range filter and 1 in case of like filter) one can pass a form
 * element, just a control or none of these. Passing a control means that the
 * form element is automatically created using the other information available
 * for this filter's field - <b>label</b> and <b>type</b>.
 * Not passing the control either just means that the <b>control</b> is
 * automatically created according the the field type
 * (using {@link FormUtil#createControl(Class)})). However if you like to define
 * label or type for a form element, you can predefine them using
 * {@link #addCustomLabel(String, String)} and
 * {@link #addFieldType(String, Class)} methods.
 * As you could expect, the <b>mandatory</b> is set to <code>false</code> and
 * the <b>initial value</b> is set to null by default.    
 * </p>
 * <p>
 * There is one more aspect for all filter adding methods. For each method, one
 * can define <b>valueId</b>. The term <b>value</b> is used here to mark the
 * blank in the filter (basically the form element). By default the
 * <b>fieldId</b> is also used as the valueId. So it's unneccesary to pass a
 * valueId unless one would like to add more than one filter for one field. 
 * In the case of range filter, there are already two values used for one
 * filter and therefore original fieldId is suffixed to distinguish the two
 * values - low and high. However for more than two range filter for one field,
 * custom value ids must be used as well. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @see ListWidget
 * @see FormWidget
 * @see ListFilter
 */
public class FilterHelper extends BaseFilterHelper {
	
	/**
	 * Constructs a {@link FilterHelper}.
	 * 
	 * @param list list widget.
	 */
	public FilterHelper(ListWidget list) {
		super(list);
	}
	
	/**
	 * Sets the current case sensitivity behaivor.
	 * 
	 * @param ignoreCase whether to ignore case.
	 */
	public FilterHelper setIgnoreCase(boolean ignoreCase) {
		super._setIgnoreCase(ignoreCase);
		return this;
	}
	
//	/**
//	 * Sets the current locale.
//	 * 
//	 * @param locale new locale.
//	 */
//	public FilterHelper setLocale(Locale locale) {
//		super._setLocale(locale);
//		return this;
//	}
	
	/**
	 * Sets the current strickness behaivor.
	 * 
	 * @param strict whether new filters should be strict.
	 */
	public FilterHelper setStrict(boolean strict) {
		super._setStrict(strict);
		return this;
	}
	
	/**
	 * Adds custom label for specified field. This can override already defined
	 * label of list field. Those labels are used by new filter form elements
	 * that are automatically created for list filters. 
	 * 
	 * @param fieldId field id.
	 * @param labelId label id (not yet resolved).
	 */
	public FilterHelper addCustomLabel(String fieldId, String labelId) {
		super._addCustomLabel(fieldId, labelId);
		return this;
	}

	/**
	 * Defines type for specified field.
	 * 
	 * @param fieldId field id.
	 * @param type field type.
	 * 
	 * @see TypeHelper#addFieldType(String, Class)
	 */
	public FilterHelper addFieldType(String fieldId, Class type) {
		super._addFieldType(fieldId, type);
		return this;
	}
	
	// ========== EQUALS ========== 
	
	// filter with form element
	
	public FilterHelper eq(String fieldId) {
		return eq(fieldId, fieldId);
	}
	public FilterHelper eq(String fieldId, String valueId) {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper eq(String fieldId, Control<?> control) {
		return eq(fieldId, fieldId, control);
	}
	public FilterHelper eq(String fieldId, String valueId, Control<?> control) {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper eq(String fieldId, FormElement element) {
		return eq(fieldId, fieldId, element);
	}
	public FilterHelper eq(String fieldId, String valueId, FormElement element) {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _eq(String fieldId) {
		return _eq(fieldId, fieldId);
	}
	public FilterHelper _eq(String fieldId, String valueId) {
		list.addFilter(EqualFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

	public FilterHelper eqConst(String fieldId, Object value) {
		return eqConst(fieldId, fieldId, value);
	}
	public FilterHelper eqConst(String fieldId, String valueId, Object value) {
		list.addFilter(EqualFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}
	
	// ========== GREATER THAN ========== 
	
	// filter with form element
	
	public FilterHelper gt(String fieldId) {
		return gt(fieldId, fieldId);
	}
	public FilterHelper gt(String fieldId, String valueId) {
		_gt(fieldId, valueId);
		GreaterThanFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper gt(String fieldId, Control control) {
		return gt(fieldId, fieldId, control);
	}
	public FilterHelper gt(String fieldId, String valueId, Control control) {
		_gt(fieldId, valueId);
		GreaterThanFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper gt(String fieldId, FormElement element) {
		return gt(fieldId, fieldId, element);
	}
	public FilterHelper gt(String fieldId, String valueId, FormElement element) {
		_gt(fieldId, valueId);
		GreaterThanFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _gt(String fieldId) {
		return _gt(fieldId, fieldId);
	}
	public FilterHelper _gt(String fieldId, String valueId) {
		list.addFilter(GreaterThanFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

	public FilterHelper gtConst(String fieldId, Object value) {
		return gtConst(fieldId, fieldId, value);
	}
	public FilterHelper gtConst(String fieldId, String valueId, Object value) {
		list.addFilter(GreaterThanFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}
	
	// ========== LOWER THAN ========== 
	
	// filter with form element
	
	public FilterHelper lt(String fieldId) {
		return lt(fieldId, fieldId);
	}
	public FilterHelper lt(String fieldId, String valueId) {
		_lt(fieldId, valueId);
		LowerThanFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper lt(String fieldId, Control control) {
		return lt(fieldId, fieldId, control);
	}
	public FilterHelper lt(String fieldId, String valueId, Control control) {
		_lt(fieldId, valueId);
		LowerThanFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper lt(String fieldId, FormElement element) {
		return lt(fieldId, fieldId, element);
	}
	public FilterHelper lt(String fieldId, String valueId, FormElement<?,?> element) {
		_lt(fieldId, valueId);
		LowerThanFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _lt(String fieldId) {
		return _lt(fieldId, fieldId);
	}
	public FilterHelper _lt(String fieldId, String valueId) {
		list.addFilter(LowerThanFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

	public FilterHelper ltConst(String fieldId, Object value) {
		return ltConst(fieldId, fieldId, value);
	}
	public FilterHelper ltConst(String fieldId, String valueId, Object value) {
		list.addFilter(LowerThanFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}
	
	// ========== LIKE ==========
	
	// filter with form element
	
	public FilterHelper like(String fieldId) {
		return like(fieldId, fieldId);
	}
	public FilterHelper like(String fieldId, String valueId) {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper like(String fieldId, Control control) {
		return like(fieldId, fieldId, control);
	}
	public FilterHelper like(String fieldId, String valueId, Control control) {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper like(String fieldId, FormElement element) {
		return like(fieldId, fieldId, element);
	}
	public FilterHelper like(String fieldId, String valueId, FormElement element) {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _like(String fieldId) {
		return _like(fieldId, fieldId);
	}
	public FilterHelper _like(String fieldId, String valueId) {
		list.addFilter(LikeFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

	public FilterHelper likeConst(String fieldId, Object value) {
		return likeConst(fieldId, fieldId, value);
	}
	public FilterHelper likeConst(String fieldId, String valueId, Object value) {
		list.addFilter(LikeFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}

	// ========== STARTS WITH ==========

	// filter with form element

	/**
     * @since 1.1.3
	 */
	public FilterHelper startsWith(String fieldId) {
		return startsWith(fieldId, fieldId);
	}

	/**
     * @since 1.1.3
     */
	public FilterHelper startsWith(String fieldId, String valueId) {
		_startsWith(fieldId, valueId);
		StartsWithFilter.addToForm(this, valueId);
		return this;
	}

	/**
     * @since 1.1.3
     */
	public FilterHelper startsWith(String fieldId, Control control) {
		return startsWith(fieldId, fieldId, control);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper startsWith(String fieldId, String valueId, Control control) {
		_startsWith(fieldId, valueId);
		StartsWithFilter.addToForm(this, valueId, control);
		return this;
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper startsWith(String fieldId, FormElement element) {
		return startsWith(fieldId, fieldId, element);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper startsWith(String fieldId, String valueId, FormElement element) {
		_startsWith(fieldId, valueId);
		StartsWithFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	

    /**
     * @since 1.1.3
     */
	public FilterHelper _startsWith(String fieldId) {
		return _startsWith(fieldId, fieldId);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper _startsWith(String fieldId, String valueId) {
		list.addFilter(StartsWithFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter


    /**
     * @since 1.1.3
     */
	public FilterHelper startsWithConst(String fieldId, Object value) {
		return startsWithConst(fieldId, fieldId, value);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper startsWithConst(String fieldId, String valueId, Object value) {
		list.addFilter(StartsWithFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}

	// ========== ENDS WITH ==========

	// filter with form element

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWith(String fieldId) {
		return endsWith(fieldId, fieldId);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWith(String fieldId, String valueId) {
		_endsWith(fieldId, valueId);
		EndsWithFilter.addToForm(this, valueId);
		return this;
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWith(String fieldId, Control control) {
		return endsWith(fieldId, fieldId, control);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWith(String fieldId, String valueId, Control control) {
		_endsWith(fieldId, valueId);
		EndsWithFilter.addToForm(this, valueId, control);
		return this;
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWith(String fieldId, FormElement element) {
		return endsWith(fieldId, fieldId, element);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWith(String fieldId, String valueId, FormElement element) {
		_endsWith(fieldId, valueId);
		EndsWithFilter.addToForm(this, valueId, element);
		return this;
	}

	// filter
	
    /**
     * @since 1.1.3
     */
	public FilterHelper _endsWith(String fieldId) {
		return _endsWith(fieldId, fieldId);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper _endsWith(String fieldId, String valueId) {
		list.addFilter(EndsWithFilter.getInstance(this, fieldId, valueId));
		return this;
	}

	// constant filter

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWithConst(String fieldId, Object value) {
		return endsWithConst(fieldId, fieldId, value);
	}

    /**
     * @since 1.1.3
     */
	public FilterHelper endsWithConst(String fieldId, String valueId, Object value) {
		list.addFilter(EndsWithFilter.getConstantInstance(this, fieldId, valueId, value));
		return this;
	}

	// ========== IS NULL ========== 
	
	// filter with form element
	
	public FilterHelper isNull(String fieldId, Object conditionValue) {
		return isNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper isNull(String fieldId, String valueId, Object conditionValueId) {
		_isNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper isNull(String fieldId, Object conditionValue, Control control) {
		return isNull(fieldId, fieldId, conditionValue, control);
	}
	public FilterHelper isNull(String fieldId, String valueId, Object conditionValue, Control control) {
		_isNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper isNull(String fieldId, Object conditionValue, FormElement element) {
		return isNull(fieldId, fieldId, conditionValue, element);
	}
	public FilterHelper isNull(String fieldId, String valueId, Object conditionValue, FormElement element) {
		_isNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _isNull(String fieldId, Object conditionValue) {
		return _isNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper _isNull(String fieldId, String valueId, Object conditionValue) {
		list.addFilter(NullFilter.getIdNullInstance(this, fieldId, valueId, conditionValue));
		return this;
	}

	// constant filter

	public FilterHelper isNullConst(String fieldId) {
		list.addFilter(NullFilter.getIsNullConstantInstance(this, fieldId));
		return this;
	}
	
	// ========== NOT NULL ========== 
	
	// filter with form element
	
	public FilterHelper notNull(String fieldId, Object conditionValue) {
		return notNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper notNull(String fieldId, String valueId, Object conditionValueId) {
		_notNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper notNull(String fieldId, Object conditionValue, Control control) {
		return notNull(fieldId, fieldId, conditionValue, control);
	}
	public FilterHelper notNull(String fieldId, String valueId, Object conditionValue, Control control) {
		_notNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper notNull(String fieldId, Object conditionValue, FormElement element) {
		return notNull(fieldId, fieldId, conditionValue, element);
	}
	public FilterHelper notNull(String fieldId, String valueId, Object conditionValue, FormElement element) {
		_notNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _notNull(String fieldId, Object conditionValue) {
		return _notNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper _notNull(String fieldId, String valueId, Object conditionValue) {
		list.addFilter(NullFilter.getNotNullInstance(this, fieldId, valueId, conditionValue));
		return this;
	}

	// constant filter

	public FilterHelper notNullConst(String fieldId) {
		list.addFilter(NullFilter.getNotNullConstantInstance(this, fieldId));
		return this;
	}	
	
	// ========== RANGE ==========
	
	// filter with form element
	
	public FilterHelper range(String fieldId) {
		return range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId));
	}
	public FilterHelper range(String fieldId, String lowValueId, String highValueId) {
		_range(fieldId, lowValueId, highValueId);
		RangeFilter.addToForm(this, lowValueId, highValueId);
		return this;
	}
	public FilterHelper range(String fieldId, Control lowControl, Control highControl) {
		return range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId), lowControl, highControl);
	}
	public FilterHelper range(String fieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) {
		_range(fieldId, lowValueId, highValueId);
		RangeFilter.addToForm(this, lowValueId, highValueId, lowControl, highControl);
		return this;
	}
	public FilterHelper range(String fieldId, FormElement lowElement, FormElement highElement) {
		return range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId), lowElement, highElement);
	}
	public FilterHelper range(String fieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) {
		_range(fieldId, lowValueId, highValueId);
		RangeFilter.addToForm(this, lowValueId, highValueId, lowElement, highElement);
		return this;
	}
	
	// filter
	
	public FilterHelper _range(String fieldId) {
		return _range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId));
	}
	public FilterHelper _range(String fieldId, String lowValueId, String highValueId) {
		list.addFilter(RangeFilter.getInstance(this, fieldId, lowValueId, highValueId));
		return this;
	}

	// ========== FIELD RANGE IN VALUE RANGE ==========
	
	// filter with form element
	
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId) {
		return fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) {
		_fieldRangeInValueRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId);
		return this;
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, Control lowControl, Control highControl) {
		return fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowControl, highControl);
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) {
		_fieldRangeInValueRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowControl, highControl);
		return this;
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, FormElement lowElement, FormElement highElement) {
		return fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowElement, highElement);
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) {
		_fieldRangeInValueRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowElement, highElement);
		return this;
	}
	
	// filter
	
	public FilterHelper _fieldRangeInValueRange(String lowFieldId, String highFieldId) {
		return _fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper _fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) {
		list.addFilter(RangeInRangeFilter.getFieldRangeInValueRangeInstance(this, lowFieldId, highFieldId, lowValueId, highValueId));
		return this;
	}

	// ========== VALUE RANGE IN FIELD RANGE ==========
	
	// filter with form element
	
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId) {
		return valueRangeInFieldRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) {
		_valueRangeInFieldRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId);
		return this;
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, Control lowControl, Control highControl) {
		return valueRangeInFieldRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowControl, highControl);
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) {
		_valueRangeInFieldRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowControl, highControl);
		return this;
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, FormElement lowElement, FormElement highElement) {
		return valueRangeInFieldRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowElement, highElement);
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) {
		_valueRangeInFieldRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowElement, highElement);
		return this;
	}
	
	// filter
	
	public FilterHelper _valueRangeInFieldRange(String lowFieldId, String highFieldId) {
		return _fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper _valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) {
		list.addFilter(RangeInRangeFilter.getValueRangeInFieldRangeInstance(this, lowFieldId, highFieldId, lowValueId, highValueId));
		return this;
	}

	// ========== OVERLAP RANGE ==========
	
	// filter with form element
	
	public FilterHelper overlapRange(String lowFieldId, String highFieldId) {
		return overlapRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) {
		_overlapRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId);
		return this;
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, Control lowControl, Control highControl) {
		return overlapRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowControl, highControl);
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) {
		_overlapRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowControl, highControl);
		return this;
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, FormElement lowElement, FormElement highElement) {
		return overlapRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowElement, highElement);
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) {
		_overlapRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowElement, highElement);
		return this;
	}
	
	// filter
	
	public FilterHelper _overlapRange(String lowFieldId, String highFieldId) {
		return _fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper _overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) {
		list.addFilter(RangeInRangeFilter.getOverlapInstance(this, lowFieldId, highFieldId, lowValueId, highValueId));
		return this;
	}

    // ========== IN ==========

    // filter with form element

    /**
     * @since 1.1.4
     */
    public FilterHelper in(String fieldId) {
        return in(fieldId, fieldId);
    }

    /**
     * @since 1.1.4
     */
    public FilterHelper in(String fieldId, String valueId) {
        _in(fieldId, valueId);
        InFilter.addToForm(this, valueId);
        return this;
    }

    /**
     * @since 1.1.4
     */
    public FilterHelper in(String fieldId, Control control) {
        return in(fieldId, fieldId, control);
    }

    /**
     * @since 1.1.4
     */
    public FilterHelper in(String fieldId, String valueId, Control control) {
        _in(fieldId, valueId);
        InFilter.addToForm(this, valueId, control, new StringListData());
        return this;
    }

    /**
     * @since 1.1.4
     */
    public FilterHelper in(String fieldId, FormElement element) {
        return in(fieldId, fieldId, element);
    }

    /**
     * @since 1.1.4
     */
    public FilterHelper in(String fieldId, String valueId, FormElement element) {
        _in(fieldId, valueId);
        InFilter.addToForm(this, valueId, element);
        return this;
    }

    // filter
    
    /**
     * @since 1.1.4
     */
    public FilterHelper _in(String fieldId) {
        return _in(fieldId, fieldId);
    }

    /**
     * @since 1.1.4
     */
    public FilterHelper _in(String fieldId, String valueId) {
        list.addFilter(InFilter.getInstance(this, fieldId, valueId));
        return this;
    }

    // ========== SQL FUNCTION ==========
	
	public SqlFunction sqlFunction(String name) {
		return new SqlFunction(name);
	}

	/**
	 * SQL Function filter helper.
	 * 
	 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
	 */
	public class SqlFunction {
		private String name;
		private List<ExpressionBuilder> params = new ArrayList<ExpressionBuilder>();
		
		SqlFunction(String name) {
			this.name = name;
		}
		
		private ExpressionBuilder[] getParams() {
			return params.toArray(new ExpressionBuilder[params.size()]);
		}
		
		// add params
		
		public SqlFunction addFieldParam(String fieldId) {
			params.add(new Field(fieldId));
			return this;
		}
		
		public SqlFunction addValueParam(String valueId) {
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return _addValueParam(valueId);			
		}
		public SqlFunction addValueParam(String valueId, Control control) {
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return _addValueParam(valueId);			
		}
		public SqlFunction addValueParam(String valueId, FormElement element) {
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, element);
			return _addValueParam(valueId);
		}
		public SqlFunction _addValueParam(String valueId) {
			params.add(new Value(valueId));
			return this;			
		}
		
		public SqlFunction addConstParam(Object value) {
			return addConstParam(null, value);
		}		
		public SqlFunction addConstParam(String valueId, Object value) {
			params.add(new Constant(valueId, value));
			return this;			
		}
		
		// add EQUALS filter
		
		public FilterHelper eqField(String fieldId) {
			list.addFilter(SqlFunctionFilter.getFieldEqualInstance(FilterHelper.this, fieldId, name, getParams()));
			return FilterHelper.this;
		}
		
		public FilterHelper eqValue(String valueId) {
			_eqValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return FilterHelper.this;
		}
		public FilterHelper eqValue(String valueId, Control control) {
			_eqValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return FilterHelper.this;
		}
		public FilterHelper eqValue(String valueId, FormElement element) {
			_eqValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, element);
			return FilterHelper.this;
		}
		public FilterHelper _eqValue(String valueId) {
			list.addFilter(SqlFunctionFilter.getValueEqualInstance(FilterHelper.this, valueId, name, getParams()));
			return FilterHelper.this;
		}
		
		public FilterHelper eqConst(String valueId) {
			return eqConst(null, valueId);
		}				
		public FilterHelper eqConst(String valueId, Object value) {
			list.addFilter(SqlFunctionFilter.getConstantEqualInstance(FilterHelper.this, valueId, value, name, getParams()));
			return FilterHelper.this;
		}
		
		// add GREATER THAN filter
		
		public FilterHelper gtField(String fieldId) {
			list.addFilter(SqlFunctionFilter.getFieldGreaterThanInstance(FilterHelper.this, fieldId, name, getParams()));
			return FilterHelper.this;
		}
		
		public FilterHelper gtValue(String valueId) {
			_gtValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return FilterHelper.this;
		}
		public FilterHelper gtValue(String valueId, Control control) {
			_gtValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return FilterHelper.this;
		}
		public FilterHelper gtValue(String valueId, FormElement element) {
			_gtValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, element);
			return FilterHelper.this;
		}
		public FilterHelper _gtValue(String valueId) {
			list.addFilter(SqlFunctionFilter.getValueGreaterThanInstance(FilterHelper.this, valueId, name, getParams()));
			return FilterHelper.this;
		}
		
		public FilterHelper gtConst(String valueId) {
			return gtConst(null, valueId);
		}				
		public FilterHelper gtConst(String valueId, Object value) {
			list.addFilter(SqlFunctionFilter.getConstantGreaterThanInstance(FilterHelper.this, valueId, value, name, getParams()));
			return FilterHelper.this;
		}		
		
		// add LOWER THAN filter
		
		public FilterHelper ltField(String fieldId) {
			list.addFilter(SqlFunctionFilter.getFieldLowerThanInstance(FilterHelper.this, fieldId, name, getParams()));
			return FilterHelper.this;
		}
		
		public FilterHelper ltValue(String valueId) {
			_ltValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return FilterHelper.this;
		}
		public FilterHelper ltValue(String valueId, Control control) {
			_ltValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return FilterHelper.this;
		}
		public FilterHelper ltValue(String valueId, FormElement element) {
			_ltValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, element);
			return FilterHelper.this;
		}
		public FilterHelper _ltValue(String valueId) {
			list.addFilter(SqlFunctionFilter.getValueLowerThanInstance(FilterHelper.this, valueId, name, getParams()));
			return FilterHelper.this;
		}
		
		public FilterHelper ltConst(String valueId) {
			return ltConst(null, valueId);
		}				
		public FilterHelper ltConst(String valueId, Object value) {
			list.addFilter(SqlFunctionFilter.getConstantLowerThanInstance(FilterHelper.this, valueId, value, name, getParams()));
			return FilterHelper.this;
		}			
		
	}
	
}
