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

import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.TypeHelper;

/**
 * One list field specific proxy for {@link FilterHelper}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see FilterHelper
 * @see ListWidget
 */
public class FieldFilterHelper extends BaseFilterHelper {
	
	private final FilterHelper helper;
	private final String fieldId; 
	
	/**
	 * Constructs a {@link FieldFilterHelper} for specified {@link FilterHelper}
	 * and field.
	 * 
	 * @param helper filter helper.
	 * @param fieldId list field id.
	 */
	public FieldFilterHelper(FilterHelper helper, String fieldId) {
		super(helper.list);
		
		Assert.notNullParam(helper, "helper");
		Assert.notEmptyParam(fieldId, "fieldId");
		
		this.helper = helper;
		this.fieldId = fieldId;
	}
	
	/**
	 * Returns the {@link FilterHelper} that this {@link FieldFilterHelper} is
	 * based on.
	 * 
	 * @return the filter helper.
	 */
	public FilterHelper getFilterHelper() {
		return this.helper;
	}
	
	/**
	 * Sets the current case sensitivity behaivor. Default is
	 * <code>ignoreCase = true</code>;
	 * 
	 * @param ignoreCase whether to ignore case.
	 */
	public FieldFilterHelper setIgnoreCase(boolean ignoreCase) {
		super._setIgnoreCase(ignoreCase);
		return this;
	}

	/**
	 * Sets the current strickness behaivor.
	 * 
	 * @param strict whether new filters should be strict.
	 */
	public FieldFilterHelper setStrict(boolean strict) {
		super._setStrict(strict);
		return this;
	}
	
	/**
	 * Adds custom label for current field. This can override already defined
	 * label of list field. Those labels are used by new filter form elements
	 * that are automatically created for list filters. 
	 * 
	 * @param labelId label id (not yet resolved).
	 */
	public FieldFilterHelper useCustomLabel(String labelId) {
		super._addCustomLabel(fieldId, labelId);
		return this;
	}

	/**
	 * Defines type for current field.
	 * 
	 * @param type field type.
	 * 
	 * @see TypeHelper#addFieldType(String, Class)
	 */
	public FieldFilterHelper useFieldType(Class type) {
		super._addFieldType(fieldId, type);
		return this;
	}
	
	// ========== EQUALS ========== 
	
	// filter with form element
	
	public FilterHelper eq() {
		return this.helper.eq(fieldId);
	}
	public FilterHelper eq(String valueId) {
		return this.helper.eq(fieldId, valueId);
	}
	public FilterHelper eq(Control control) {
		return this.helper.eq(fieldId, control);
	}
	public FilterHelper eq(String valueId, Control control) {
		return this.helper.eq(fieldId, valueId, control);
	}
	public FilterHelper eq(FormElement element) {
		return this.helper.eq(fieldId, element);
	}
	public FilterHelper eq(String valueId, FormElement element) {
		return this.helper.eq(fieldId, valueId, element);
	}
	
	// filter
	
	public FilterHelper _eq() {
		return this.helper._eq(fieldId);
	}
	public FilterHelper _eq(String valueId) {
		return this.helper._eq(fieldId, valueId);
	}

	// constant filter

	public FilterHelper eqConst(Object value) {
		return this.helper.eqConst(fieldId, value);
	}
	public FilterHelper eqConst(String valueId, Object value) {
		return this.helper.eqConst(fieldId, valueId, value);
	}
	
	// ========== GREATER THAN ========== 
	
	// filter with form element
	
	public FilterHelper gt() {
		return this.helper.gt(fieldId);
	}
	public FilterHelper gt(String valueId) {
		return this.helper.gt(fieldId, valueId);
	}
	public FilterHelper gt(Control control) {
		return this.helper.gt(fieldId, control);
	}
	public FilterHelper gt(String valueId, Control control) {
		return this.helper.gt(fieldId, valueId, control);
	}
	public FilterHelper gt(FormElement element) {
		return this.helper.gt(fieldId, element);
	}
	public FilterHelper gt(String valueId, FormElement element) {
		return this.helper.gt(fieldId, valueId, element);
	}
	
	// filter
	
	public FilterHelper _gt() {
		return this.helper._gt(fieldId);
	}
	public FilterHelper _gt(String valueId) {
		return this.helper._gt(fieldId, valueId);
	}

	// constant filter

	public FilterHelper gtConst(Object value) {
		return this.helper.gtConst(fieldId, value);
	}
	public FilterHelper gtConst(String valueId, Object value) {
		return this.helper.gtConst(fieldId, valueId, value);
	}	
	
	// ========== LOWER THAN ========== 
	
	// filter with form element
	
	public FilterHelper lt() {
		return this.helper.lt(fieldId);
	}
	public FilterHelper lt(String valueId) {
		return this.helper.lt(fieldId, valueId);
	}
	public FilterHelper lt(Control control) {
		return this.helper.lt(fieldId, control);
	}
	public FilterHelper lt(String valueId, Control control) {
		return this.helper.lt(fieldId, valueId, control);
	}
	public FilterHelper lt(FormElement element) {
		return this.helper.lt(fieldId, element);
	}
	public FilterHelper lt(String valueId, FormElement element) {
		return this.helper.lt(fieldId, valueId, element);
	}
	
	// filter
	
	public FilterHelper _lt() {
		return this.helper._lt(fieldId);
	}
	public FilterHelper _lt(String valueId) {
		return this.helper._lt(fieldId, valueId);
	}

	// constant filter

	public FilterHelper ltConst(Object value) {
		return this.helper.ltConst(fieldId, value);
	}
	public FilterHelper ltConst(String valueId, Object value) {
		return this.helper.ltConst(fieldId, valueId, value);
	}

	// ========== LIKE ==========

	// filter with form element

	public FilterHelper like() {
		return this.helper.like(fieldId);
	}
	public FilterHelper like(String valueId) {
		return this.helper.like(fieldId, valueId);
	}
	public FilterHelper like(Control control) {
		return this.helper.like(fieldId, control);
	}
	public FilterHelper like(String valueId, Control control) {
		return this.helper.like(fieldId, valueId, control);
	}
	public FilterHelper like(FormElement element) {
		return this.helper.like(fieldId, element);
	}
	public FilterHelper like(String valueId, FormElement element) {
		return this.helper.like(fieldId, valueId, element);
	}
	
	// filter
	
	public FilterHelper _like() {
		return this.helper._like(fieldId);
	}
	public FilterHelper _like(String valueId) {
		return this.helper._like(fieldId, valueId);
	}

	// constant filter

	public FilterHelper likeConst(Object value) {
		return this.helper.likeConst(fieldId, value);
	}
	public FilterHelper likeConst(String valueId, Object value) {
		return this.helper.likeConst(fieldId, valueId, value);
	}

	// ========== STARTS WITH ==========

	// filter with form element

	/**
	 * Marks that values of this field must start with the value of filter
	 * control. The control (with the same Id as fieldId) will be automatically
	 * created.
	 * 
	 * @return reference to current instance of FilterHelper.
	 * @since 1.1.3
	 */
	public FilterHelper startsWith() {
		return this.helper.startsWith(fieldId);
	}

	/**
	 * Marks that values of this field must start with the value of filter control
	 * whose Id is valueId. The control will be automatically created.
	 * 
	 * @param valueId The Id of the filter Control.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper startsWith(String valueId) {
		return this.helper.startsWith(fieldId, valueId);
	}

	/**
	 * Marks that values of this field must start with the value of given control.
	 * The Id of the control will be the same as fieldId.
	 * 
	 * @param control Custom control for user input.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper startsWith(Control control) {
		return this.helper.startsWith(fieldId, control);
	}

	/**
	 * Marks that values of this field must start with the value of given control
	 * whose Id is <code>valueId</code>.
	 * 
	 * @param valueId The Id of the <code>control</code>.
	 * @param control Custom control for user input.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper startsWith(String valueId, Control control) {
		return this.helper.startsWith(fieldId, valueId, control);
	}

	/**
	 * Marks that values of this field must start with the value of given form
	 * element. The Id of the element will be the same as fieldId.
	 * 
	 * @param element The form element (with label and control) whose value will
	 *          be used to filter the data.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper startsWith(FormElement element) {
		return this.helper.startsWith(fieldId, element);
	}

	/**
	 * Marks that values of this field must start with the value of given form
	 * element.
	 * 
	 * @param valueId The Id of the <code>element</code>.
	 * @param element The form element (with label and control) whose value will
	 *          be used to filter the data.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper startsWith(String valueId, FormElement element) {
		return this.helper.startsWith(fieldId, valueId, element);
	}

	// filter

	/**
	 * Adds filter condition without the form control.
	 * 
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper _startsWith() {
		return this.helper._startsWith(fieldId);
	}

	public FilterHelper _startsWith(String valueId) {
		return this.helper._startsWith(fieldId, valueId);
	}

	// constant filter

	/**
	 * Marks that all values of this field must start with given
	 * <code>value</code>.
	 * 
	 * @param value The (constant String) value that is used in filtering.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper startsWithConst(Object value) {
		return this.helper.startsWithConst(fieldId, value);
	}

	public FilterHelper startsWithConst(String valueId, Object value) {
		return this.helper.startsWithConst(fieldId, valueId, value);
	}

	// ========== ENDS WITH ==========

	// filter with form element

	/**
	 * Marks that values of this field must end with the value of filter control.
	 * The control (with the same Id as fieldId) will be automatically created.
	 * 
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWith() {
		return this.helper.endsWith(fieldId);
	}

	/**
	 * Marks that values of this field must end with the value of filter control
	 * whose Id is valueId. The control will be automatically created.
	 * 
	 * @param valueId The Id of the filter Control.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWith(String valueId) {
		return this.helper.endsWith(fieldId, valueId);
	}

	/**
	 * Marks that values of this field must end with the value of given control.
	 * The Id of the control will be the same as fieldId.
	 * 
	 * @param control Custom control for user input.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWith(Control control) {
		return this.helper.endsWith(fieldId, control);
	}

	/**
	 * Marks that values of this field must end with the value of given control
	 * whose Id is <code>valueId</code>.
	 * 
	 * @param valueId The Id of the <code>control</code>.
	 * @param control Custom control for user input.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWith(String valueId, Control control) {
		return this.helper.endsWith(fieldId, valueId, control);
	}

	/**
	 * Marks that values of this field must end with the value of given form
	 * element. The Id of the element will be the same as fieldId.
	 * 
	 * @param element The form element (with label and control) whose value will
	 *          be used to filter the data.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWith(FormElement element) {
		return this.helper.endsWith(fieldId, element);
	}

	/**
	 * Marks that values of this field must end with the value of given form
	 * element.
	 * 
	 * @param valueId The Id of the <code>element</code>.
	 * @param element The form element (with label and control) whose value will
	 *          be used to filter the data.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWith(String valueId, FormElement element) {
		return this.helper.endsWith(fieldId, valueId, element);
	}

	// filter

	/**
	 * Adds filter condition without the form control.
	 * 
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper _endsWith() {
		return this.helper._endsWith(fieldId);
	}
	public FilterHelper _endsWith(String valueId) {
		return this.helper._endsWith(fieldId, valueId);
	}

	// constant filter

	/**
	 * Marks that all values of this field must end with given <code>value</code>.
	 * 
	 * @param value The (constant String) value that is used in filtering.
	 * @return reference to current instance of FilterHelper.
     * @since 1.1.3
	 */
	public FilterHelper endsWithConst(Object value) {
		return this.helper.endsWithConst(fieldId, value);
	}
	public FilterHelper endsWithConst(String valueId, Object value) {
		return this.helper.endsWithConst(fieldId, valueId, value);
	}

	// ========== IS NULL ========== 
	
	// fiisNuller with form element
	
	public FilterHelper isNull(Object conditionValue) {
		return this.helper.isNull(fieldId, conditionValue);
	}
	public FilterHelper isNull(String valueId, Object conditionValue) {
		return this.helper.isNull(fieldId, valueId, conditionValue);
	}
	public FilterHelper isNull(Object conditionValue, Control control) {
		return this.helper.isNull(fieldId, conditionValue, control);
	}
	public FilterHelper isNull(String valueId, Object conditionValue, Control control) {
		return this.helper.isNull(fieldId, valueId, conditionValue, control);
	}
	public FilterHelper isNull(Object conditionValue, FormElement element) {
		return this.helper.isNull(fieldId, conditionValue, element);
	}
	public FilterHelper isNull(String valueId, Object conditionValue, FormElement element) {
		return this.helper.isNull(fieldId, valueId, conditionValue, element);
	}
	
	// fiisNuller
	
	public FilterHelper _isNull(Object conditionValue) {
		return this.helper._isNull(fieldId, conditionValue);
	}
	public FilterHelper _isNull(String valueId, Object conditionValue) {
		return this.helper._isNull(fieldId, valueId, conditionValue);
	}

	// constant fiisNuller

	public FilterHelper isNullConst() {
		return this.helper.isNullConst(fieldId);
	}
	
	// ========== NOT NULL ========== 
	
	// fiisNuller with form element
	
	public FilterHelper notNull(Object conditionValue) {
		return this.helper.notNull(fieldId, conditionValue);
	}
	public FilterHelper notNull(String valueId, Object conditionValue) {
		return this.helper.notNull(fieldId, valueId, conditionValue);
	}
	public FilterHelper notNull(Object conditionValue, Control control) {
		return this.helper.notNull(fieldId, conditionValue, control);
	}
	public FilterHelper notNull(String valueId, Object conditionValue, Control control) {
		return this.helper.notNull(fieldId, valueId, conditionValue, control);
	}
	public FilterHelper notNull(Object conditionValue, FormElement element) {
		return this.helper.notNull(fieldId, conditionValue, element);
	}
	public FilterHelper notNull(String valueId, Object conditionValue, FormElement element) {
		return this.helper.notNull(fieldId, valueId, conditionValue, element);
	}
	
	// finotNuller
	
	public FilterHelper _notNull(Object conditionValue) {
		return this.helper._notNull(fieldId, conditionValue);
	}
	public FilterHelper _notNull(String valueId, Object conditionValue) {
		return this.helper._notNull(fieldId, valueId, conditionValue);
	}

	// constant finotNuller

	public FilterHelper notNullConst() {
		return this.helper.notNullConst(fieldId);
	}

	// ========== RANGE ==========

	// filter with form element

	public FilterHelper range() {
		return this.helper.range(fieldId);
	}
	public FilterHelper range(String lowValueId, String highValueId) {
		return this.helper.range(fieldId, lowValueId, highValueId);
	}
	public FilterHelper range(Control lowControl, Control highControl) {
		return this.helper.range(fieldId, lowControl, highControl);
	}
	public FilterHelper range(String lowValueId, String highValueId, Control lowControl, Control highControl) {
		return this.helper.range(fieldId, lowValueId, highValueId, lowControl, highControl);
	}
	public FilterHelper range(FormElement lowElement, FormElement highElement) {
		return this.helper.range(fieldId, lowElement, highElement);
	}
	public FilterHelper range(String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) {
		return this.helper.range(fieldId, lowValueId, highValueId, lowElement, highElement);
	}
	
	// filter
	
	public FilterHelper _range() {
		return this.helper._range(fieldId);
	}
	public FilterHelper _range(String lowValueId, String highValueId) {
		return this.helper._range(fieldId, lowValueId, highValueId);
	}
	
}
