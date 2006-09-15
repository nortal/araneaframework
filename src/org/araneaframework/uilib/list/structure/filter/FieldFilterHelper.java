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

import org.apache.commons.lang.Validate;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.ListWidget;

/**
 * List filter helper for specified field.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see FilterHelper
 * @see ListWidget
 */
public class FieldFilterHelper {
	
	private final FilterHelper helper;
	private final String fieldId; 
	
	public FieldFilterHelper(FilterHelper helper, String fieldId) {
		Validate.notNull(helper);
		Validate.notNull(fieldId);
		this.helper = helper;
		this.fieldId = fieldId;
	}
	
	public FilterHelper getFilterHelper() {
		return this.helper;
	}
	
	// ========== EQUALS ========== 
	
	// filter with form element
	
	public FilterHelper eq() throws Exception {
		return this.helper.eq(fieldId);
	}
	public FilterHelper eq(String valueId) throws Exception {
		return this.helper.eq(fieldId, valueId);
	}
	public FilterHelper eq(Control control) throws Exception {
		return this.helper.eq(fieldId, control);
	}
	public FilterHelper eq(String valueId, Control control) throws Exception {
		return this.helper.eq(fieldId, valueId, control);
	}
	public FilterHelper eq(FormElement element) throws Exception {
		return this.helper.eq(fieldId, element);
	}
	public FilterHelper eq(String valueId, FormElement element) throws Exception {
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
	
	// ========== LIKE ==========
	
	// filter with form element
	
	public FilterHelper like() throws Exception {
		return this.helper.like(fieldId);
	}
	public FilterHelper like(String valueId) throws Exception {
		return this.helper.like(fieldId, valueId);
	}
	public FilterHelper like(Control control) throws Exception {
		return this.helper.like(fieldId, control);
	}
	public FilterHelper like(String valueId, Control control) throws Exception {
		return this.helper.like(fieldId, valueId, control);
	}
	public FilterHelper like(FormElement element) throws Exception {
		return this.helper.like(fieldId, element);
	}
	public FilterHelper like(String valueId, FormElement element) throws Exception {
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
	
	// ========== RANGE ==========
	
	// filter with form element
	
	public FilterHelper range() throws Exception {
		return this.helper.range(fieldId);
	}
	public FilterHelper range(String lowValueId, String highValueId) throws Exception {
		return this.helper.range(fieldId, lowValueId, highValueId);
	}
	public FilterHelper range(Control lowControl, Control highControl) throws Exception {
		return this.helper.range(fieldId, lowControl, highControl);
	}
	public FilterHelper range(String lowValueId, String highValueId, Control lowControl, Control highControl) throws Exception {
		return this.helper.range(fieldId, lowValueId, highValueId, lowControl, highControl);
	}
	public FilterHelper range(FormElement lowElement, FormElement highElement) throws Exception {
		return this.helper.range(fieldId, lowElement, highElement);
	}
	public FilterHelper range(String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) throws Exception {
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
