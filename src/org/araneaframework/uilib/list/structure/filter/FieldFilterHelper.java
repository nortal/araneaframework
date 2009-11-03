/*
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
 */

package org.araneaframework.uilib.list.structure.filter;

import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.TypeHelper;

/**
 * One list field specific proxy for {@link FilterHelper}.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * 
 * @see FilterHelper
 * @see ListWidget
 */
public class FieldFilterHelper extends BaseFilterHelper {

  private final FilterHelper helper;

  private final String fieldId;

  /**
   * Constructs a {@link FieldFilterHelper} for specified {@link FilterHelper} and field.
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
   * Returns the {@link FilterHelper} that this {@link FieldFilterHelper} is based on.
   * 
   * @return the filter helper.
   */
  public FilterHelper getFilterHelper() {
    return this.helper;
  }

  /**
   * Sets the current case sensitivity behaivor. Default is <code>ignoreCase = true</code>;
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
   * Adds custom label for current field. This can override already defined label of list field. Those labels are used
   * by new filter form elements that are automatically created for list filters.
   * 
   * @param labelId label id (not yet resolved).
   */
  public FieldFilterHelper useCustomLabel(String labelId) {
    super._addCustomLabel(this.fieldId, labelId);
    return this;
  }

  /**
   * Defines type for current field.
   * 
   * @param type field type.
   * 
   * @see TypeHelper#addFieldType(String, Class)
   */
  public FieldFilterHelper useFieldType(Class<?> type) {
    super._addFieldType(this.fieldId, type);
    return this;
  }

  // ========== EQUALS ==========

  // filter with form element

  public FilterHelper eq() {
    return this.helper.eq(this.fieldId);
  }

  public FilterHelper eq(String valueId) {
    return this.helper.eq(this.fieldId, valueId);
  }

  public FilterHelper eq(Control<?> control) {
    return this.helper.eq(this.fieldId, control);
  }

  public FilterHelper eq(String valueId, Control<?> control) {
    return this.helper.eq(this.fieldId, valueId, control);
  }

  public FilterHelper eq(FormElement<?, ?> element) {
    return this.helper.eq(this.fieldId, element);
  }

  public FilterHelper eq(String valueId, FormElement<?, ?> element) {
    return this.helper.eq(this.fieldId, valueId, element);
  }

  // filter

  public FilterHelper _eq() {
    return this.helper._eq(this.fieldId);
  }

  public FilterHelper _eq(String valueId) {
    return this.helper._eq(this.fieldId, valueId);
  }

  // constant filter

  public FilterHelper eqConst(Object value) {
    return this.helper.eqConst(this.fieldId, value);
  }

  public FilterHelper eqConst(String valueId, Object value) {
    return this.helper.eqConst(this.fieldId, valueId, value);
  }

  // ========== GREATER THAN ==========

  // filter with form element

  public FilterHelper gt() {
    return this.helper.gt(this.fieldId);
  }

  public FilterHelper gt(String valueId) {
    return this.helper.gt(this.fieldId, valueId);
  }

  public FilterHelper gt(Control<?> control) {
    return this.helper.gt(this.fieldId, control);
  }

  public FilterHelper gt(String valueId, Control<?> control) {
    return this.helper.gt(this.fieldId, valueId, control);
  }

  public FilterHelper gt(FormElement<?, ?> element) {
    return this.helper.gt(this.fieldId, element);
  }

  public FilterHelper gt(String valueId, FormElement<?, ?> element) {
    return this.helper.gt(this.fieldId, valueId, element);
  }

  // filter

  public FilterHelper _gt() {
    return this.helper._gt(this.fieldId);
  }

  public FilterHelper _gt(String valueId) {
    return this.helper._gt(this.fieldId, valueId);
  }

  // constant filter

  public FilterHelper gtConst(Object value) {
    return this.helper.gtConst(this.fieldId, value);
  }

  public FilterHelper gtConst(String valueId, Object value) {
    return this.helper.gtConst(this.fieldId, valueId, value);
  }

  // ========== LOWER THAN ==========

  // filter with form element

  public FilterHelper lt() {
    return this.helper.lt(this.fieldId);
  }

  public FilterHelper lt(String valueId) {
    return this.helper.lt(this.fieldId, valueId);
  }

  public FilterHelper lt(Control<?> control) {
    return this.helper.lt(this.fieldId, control);
  }

  public FilterHelper lt(String valueId, Control<?> control) {
    return this.helper.lt(this.fieldId, valueId, control);
  }

  public FilterHelper lt(FormElement<?, ?> element) {
    return this.helper.lt(this.fieldId, element);
  }

  public FilterHelper lt(String valueId, FormElement<?, ?> element) {
    return this.helper.lt(this.fieldId, valueId, element);
  }

  // filter

  public FilterHelper _lt() {
    return this.helper._lt(this.fieldId);
  }

  public FilterHelper _lt(String valueId) {
    return this.helper._lt(this.fieldId, valueId);
  }

  // constant filter

  public FilterHelper ltConst(Object value) {
    return this.helper.ltConst(this.fieldId, value);
  }

  public FilterHelper ltConst(String valueId, Object value) {
    return this.helper.ltConst(this.fieldId, valueId, value);
  }

  // ========== LIKE ==========

  // filter with form element

  public FilterHelper like() {
    return this.helper.like(this.fieldId);
  }

  public FilterHelper like(String valueId) {
    return this.helper.like(this.fieldId, valueId);
  }

  public FilterHelper like(Control<?> control) {
    return this.helper.like(this.fieldId, control);
  }

  public FilterHelper like(String valueId, Control<?> control) {
    return this.helper.like(this.fieldId, valueId, control);
  }

  public FilterHelper like(FormElement<?, ?> element) {
    return this.helper.like(this.fieldId, element);
  }

  public FilterHelper like(String valueId, FormElement<?, ?> element) {
    return this.helper.like(this.fieldId, valueId, element);
  }

  // filter

  public FilterHelper _like() {
    return this.helper._like(this.fieldId);
  }

  public FilterHelper _like(String valueId) {
    return this.helper._like(this.fieldId, valueId);
  }

  // constant filter

  public FilterHelper likeConst(Object value) {
    return this.helper.likeConst(this.fieldId, value);
  }

  public FilterHelper likeConst(String valueId, Object value) {
    return this.helper.likeConst(this.fieldId, valueId, value);
  }

  // ========== STARTS WITH ==========

  // filter with form element

  /**
   * Marks that values of this field must start with the value of filter control. The control (with the same Id as
   * fieldId) will be automatically created.
   * 
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWith() {
    return this.helper.startsWith(this.fieldId);
  }

  /**
   * Marks that values of this field must start with the value of filter control whose Id is valueId. The control will
   * be automatically created.
   * 
   * @param valueId The Id of the filter Control.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWith(String valueId) {
    return this.helper.startsWith(this.fieldId, valueId);
  }

  /**
   * Marks that values of this field must start with the value of given control. The Id of the control will be the same
   * as fieldId.
   * 
   * @param control Custom control for user input.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWith(Control<?> control) {
    return this.helper.startsWith(this.fieldId, control);
  }

  /**
   * Marks that values of this field must start with the value of given control whose Id is <code>valueId</code>.
   * 
   * @param valueId The Id of the <code>control</code>.
   * @param control Custom control for user input.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWith(String valueId, Control<?> control) {
    return this.helper.startsWith(this.fieldId, valueId, control);
  }

  /**
   * Marks that values of this field must start with the value of given form element. The Id of the element will be the
   * same as fieldId.
   * 
   * @param element The form element (with label and control) whose value will be used to filter the data.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWith(FormElement<?, ?> element) {
    return this.helper.startsWith(this.fieldId, element);
  }

  /**
   * Marks that values of this field must start with the value of given form element.
   * 
   * @param valueId The Id of the <code>element</code>.
   * @param element The form element (with label and control) whose value will be used to filter the data.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWith(String valueId, FormElement<?, ?> element) {
    return this.helper.startsWith(this.fieldId, valueId, element);
  }

  // filter

  /**
   * Adds filter condition without the form control.
   * 
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper _startsWith() {
    return this.helper._startsWith(this.fieldId);
  }

  public FilterHelper _startsWith(String valueId) {
    return this.helper._startsWith(this.fieldId, valueId);
  }

  // constant filter

  /**
   * Marks that all values of this field must start with given <code>value</code>.
   * 
   * @param value The (constant String) value that is used in filtering.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper startsWithConst(Object value) {
    return this.helper.startsWithConst(this.fieldId, value);
  }

  public FilterHelper startsWithConst(String valueId, Object value) {
    return this.helper.startsWithConst(this.fieldId, valueId, value);
  }

  // ========== ENDS WITH ==========

  // filter with form element

  /**
   * Marks that values of this field must end with the value of filter control. The control (with the same Id as
   * fieldId) will be automatically created.
   * 
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper endsWith() {
    return this.helper.endsWith(this.fieldId);
  }

  /**
   * Marks that values of this field must end with the value of filter control whose Id is valueId. The control will be
   * automatically created.
   * 
   * @param valueId The Id of the filter Control.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper endsWith(String valueId) {
    return this.helper.endsWith(this.fieldId, valueId);
  }

  /**
   * Marks that values of this field must end with the value of given control. The Id of the control will be the same as
   * fieldId.
   * 
   * @param control Custom control for user input.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper endsWith(Control<?> control) {
    return this.helper.endsWith(this.fieldId, control);
  }

  /**
   * Marks that values of this field must end with the value of given control whose Id is <code>valueId</code>.
   * 
   * @param valueId The Id of the <code>control</code>.
   * @param control Custom control for user input.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper endsWith(String valueId, Control<?> control) {
    return this.helper.endsWith(this.fieldId, valueId, control);
  }

  /**
   * Marks that values of this field must end with the value of given form element. The Id of the element will be the
   * same as fieldId.
   * 
   * @param element The form element (with label and control) whose value will be used to filter the data.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper endsWith(FormElement<?, ?> element) {
    return this.helper.endsWith(this.fieldId, element);
  }

  /**
   * Marks that values of this field must end with the value of given form element.
   * 
   * @param valueId The Id of the <code>element</code>.
   * @param element The form element (with label and control) whose value will be used to filter the data.
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper endsWith(String valueId, FormElement<?, ?> element) {
    return this.helper.endsWith(this.fieldId, valueId, element);
  }

  // filter

  /**
   * Adds filter condition without the form control.
   * 
   * @return reference to current instance of FilterHelper.
   * @since 1.1.3
   */
  public FilterHelper _endsWith() {
    return this.helper._endsWith(this.fieldId);
  }

  public FilterHelper _endsWith(String valueId) {
    return this.helper._endsWith(this.fieldId, valueId);
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
    return this.helper.endsWithConst(this.fieldId, value);
  }

  public FilterHelper endsWithConst(String valueId, Object value) {
    return this.helper.endsWithConst(this.fieldId, valueId, value);
  }

  // ========== IS NULL ==========

  // fiisNuller with form element

  public FilterHelper isNull(Object conditionValue) {
    return this.helper.isNull(this.fieldId, conditionValue);
  }

  public FilterHelper isNull(String valueId, Object conditionValue) {
    return this.helper.isNull(this.fieldId, valueId, conditionValue);
  }

  public FilterHelper isNull(Object conditionValue, Control<?> control) {
    return this.helper.isNull(this.fieldId, conditionValue, control);
  }

  public FilterHelper isNull(String valueId, Object conditionValue, Control<?> control) {
    return this.helper.isNull(this.fieldId, valueId, conditionValue, control);
  }

  public FilterHelper isNull(Object conditionValue, FormElement<?, ?> element) {
    return this.helper.isNull(this.fieldId, conditionValue, element);
  }

  public FilterHelper isNull(String valueId, Object conditionValue, FormElement<?, ?> element) {
    return this.helper.isNull(this.fieldId, valueId, conditionValue, element);
  }

  // fiisNuller

  public FilterHelper _isNull(Object conditionValue) {
    return this.helper._isNull(this.fieldId, conditionValue);
  }

  public FilterHelper _isNull(String valueId, Object conditionValue) {
    return this.helper._isNull(this.fieldId, valueId, conditionValue);
  }

  // constant fiisNuller

  public FilterHelper isNullConst() {
    return this.helper.isNullConst(this.fieldId);
  }

  // ========== NOT NULL ==========

  // fiisNuller with form element

  public FilterHelper notNull(Object conditionValue) {
    return this.helper.notNull(this.fieldId, conditionValue);
  }

  public FilterHelper notNull(String valueId, Object conditionValue) {
    return this.helper.notNull(this.fieldId, valueId, conditionValue);
  }

  public FilterHelper notNull(Object conditionValue, Control<?> control) {
    return this.helper.notNull(this.fieldId, conditionValue, control);
  }

  public FilterHelper notNull(String valueId, Object conditionValue, Control<?> control) {
    return this.helper.notNull(this.fieldId, valueId, conditionValue, control);
  }

  public FilterHelper notNull(Object conditionValue, FormElement<?, ?> element) {
    return this.helper.notNull(this.fieldId, conditionValue, element);
  }

  public FilterHelper notNull(String valueId, Object conditionValue, FormElement<?, ?> element) {
    return this.helper.notNull(this.fieldId, valueId, conditionValue, element);
  }

  // finotNuller

  public FilterHelper _notNull(Object conditionValue) {
    return this.helper._notNull(this.fieldId, conditionValue);
  }

  public FilterHelper _notNull(String valueId, Object conditionValue) {
    return this.helper._notNull(this.fieldId, valueId, conditionValue);
  }

  // constant finotNuller

  public FilterHelper notNullConst() {
    return this.helper.notNullConst(this.fieldId);
  }

  // ========== RANGE ==========

  // filter with form element

  public FilterHelper range() {
    return this.helper.range(this.fieldId);
  }

  public FilterHelper range(String lowValueId, String highValueId) {
    return this.helper.range(this.fieldId, lowValueId, highValueId);
  }

  public <C> FilterHelper range(Control<C> lowControl, Control<C> highControl) {
    return this.helper.range(this.fieldId, lowControl, highControl);
  }

  public <C> FilterHelper range(String lowValueId, String highValueId, Control<C> lowControl, Control<C> highControl) {
    return this.helper.range(this.fieldId, lowValueId, highValueId, lowControl, highControl);
  }

  public <C, D> FilterHelper range(FormElement<C, D> lowElement, FormElement<C, D> highElement) {
    return this.helper.range(this.fieldId, lowElement, highElement);
  }

  public <C, D> FilterHelper range(String lowValueId, String highValueId, FormElement<C, D> lowElement,
      FormElement<C, D> highElement) {
    return this.helper.range(this.fieldId, lowValueId, highValueId, lowElement, highElement);
  }

  // filter

  public FilterHelper _range() {
    return this.helper._range(this.fieldId);
  }

  public FilterHelper _range(String lowValueId, String highValueId) {
    return this.helper._range(this.fieldId, lowValueId, highValueId);
  }

  // ============ IN ============

  // filter with form element

  /** @since 1.1.4 */
  public FilterHelper in() {
    return this.helper.in(this.fieldId);
  }

  /** @since 1.1.4 */
  public FilterHelper in(String valueId) {
    return this.helper.in(this.fieldId, valueId);
  }

  /** @since 1.1.4 */
  public FilterHelper in(Control<?> control) {
    return this.helper.in(this.fieldId, control);
  }

  /** @since 1.1.4 */
  public FilterHelper in(String valueId, Control<?> control) {
    return this.helper.in(this.fieldId, valueId, control);
  }

  /** @since 1.1.4 */
  public FilterHelper in(FormElement<?, ?> element) {
    return this.helper.in(this.fieldId, element);
  }

  /** @since 1.1.4 */
  public FilterHelper in(String valueId, FormElement<?, ?> element) {
    return this.helper.in(this.fieldId, valueId, element);
  }

  // filter

  /** @since 1.1.4 */
  public FilterHelper _in() {
    return this.helper._in(this.fieldId);
  }

  /** @since 1.1.4 */
  public FilterHelper _in(String valueId) {
    return this.helper._in(this.fieldId, valueId);
  }

}
