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
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.structure.filter.advanced.RangeInRangeFilter;
import org.araneaframework.uilib.list.structure.filter.advanced.SqlFunctionFilter;
import org.araneaframework.uilib.list.structure.filter.atomic.Constant;
import org.araneaframework.uilib.list.structure.filter.atomic.Field;
import org.araneaframework.uilib.list.structure.filter.atomic.Value;
import org.araneaframework.uilib.list.structure.filter.field.EqualFilter;
import org.araneaframework.uilib.list.structure.filter.field.GreaterThanFilter;
import org.araneaframework.uilib.list.structure.filter.field.LikeFilter;
import org.araneaframework.uilib.list.structure.filter.field.LowerThanFilter;
import org.araneaframework.uilib.list.structure.filter.field.NullFilter;
import org.araneaframework.uilib.list.structure.filter.field.RangeFilter;

/**
 * List filter helper.  
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see ListWidget
 */
public class FilterHelper extends BaseFilterHelper {
	
	public FilterHelper(ListWidget list) {
		super(list);
	}
	
	// ========== EQUALS ========== 
	
	// filter with form element
	
	public FilterHelper eq(String fieldId) throws Exception {
		return eq(fieldId, fieldId);
	}
	public FilterHelper eq(String fieldId, String valueId) throws Exception {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper eq(String fieldId, Control control) throws Exception {
		return eq(fieldId, fieldId, control);
	}
	public FilterHelper eq(String fieldId, String valueId, Control control) throws Exception {
		_eq(fieldId, valueId);
		EqualFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper eq(String fieldId, FormElement element) throws Exception {
		return eq(fieldId, fieldId, element);
	}
	public FilterHelper eq(String fieldId, String valueId, FormElement element) throws Exception {
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
	
	public FilterHelper gt(String fieldId) throws Exception {
		return gt(fieldId, fieldId);
	}
	public FilterHelper gt(String fieldId, String valueId) throws Exception {
		_gt(fieldId, valueId);
		GreaterThanFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper gt(String fieldId, Control control) throws Exception {
		return gt(fieldId, fieldId, control);
	}
	public FilterHelper gt(String fieldId, String valueId, Control control) throws Exception {
		_gt(fieldId, valueId);
		GreaterThanFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper gt(String fieldId, FormElement element) throws Exception {
		return gt(fieldId, fieldId, element);
	}
	public FilterHelper gt(String fieldId, String valueId, FormElement element) throws Exception {
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
	
	public FilterHelper lt(String fieldId) throws Exception {
		return lt(fieldId, fieldId);
	}
	public FilterHelper lt(String fieldId, String valueId) throws Exception {
		_lt(fieldId, valueId);
		LowerThanFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper lt(String fieldId, Control control) throws Exception {
		return lt(fieldId, fieldId, control);
	}
	public FilterHelper lt(String fieldId, String valueId, Control control) throws Exception {
		_lt(fieldId, valueId);
		LowerThanFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper lt(String fieldId, FormElement element) throws Exception {
		return lt(fieldId, fieldId, element);
	}
	public FilterHelper lt(String fieldId, String valueId, FormElement element) throws Exception {
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
	
	public FilterHelper like(String fieldId) throws Exception {
		return like(fieldId, fieldId);
	}
	public FilterHelper like(String fieldId, String valueId) throws Exception {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper like(String fieldId, Control control) throws Exception {
		return like(fieldId, fieldId, control);
	}
	public FilterHelper like(String fieldId, String valueId, Control control) throws Exception {
		_like(fieldId, valueId);
		LikeFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper like(String fieldId, FormElement element) throws Exception {
		return like(fieldId, fieldId, element);
	}
	public FilterHelper like(String fieldId, String valueId, FormElement element) throws Exception {
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
	
	// ========== IS NULL ========== 
	
	// filter with form element
	
	public FilterHelper isNull(String fieldId, Object conditionValue) throws Exception {
		return isNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper isNull(String fieldId, String valueId, Object conditionValueId) throws Exception {
		_isNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper isNull(String fieldId, Object conditionValue, Control control) throws Exception {
		return isNull(fieldId, fieldId, conditionValue, control);
	}
	public FilterHelper isNull(String fieldId, String valueId, Object conditionValue, Control control) throws Exception {
		_isNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper isNull(String fieldId, Object conditionValue, FormElement element) throws Exception {
		return isNull(fieldId, fieldId, conditionValue, element);
	}
	public FilterHelper isNull(String fieldId, String valueId, Object conditionValue, FormElement element) throws Exception {
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
	
	public FilterHelper notNull(String fieldId, Object conditionValue) throws Exception {
		return isNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper notNull(String fieldId, String valueId, Object conditionValueId) throws Exception {
		_notNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId);
		return this;
	}
	public FilterHelper notNull(String fieldId, Object conditionValue, Control control) throws Exception {
		return isNull(fieldId, fieldId, conditionValue, control);
	}
	public FilterHelper notNull(String fieldId, String valueId, Object conditionValue, Control control) throws Exception {
		_notNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, control);
		return this;
	}
	public FilterHelper notNull(String fieldId, Object conditionValue, FormElement element) throws Exception {
		return isNull(fieldId, fieldId, conditionValue, element);
	}
	public FilterHelper notNull(String fieldId, String valueId, Object conditionValue, FormElement element) throws Exception {
		_notNull(fieldId, valueId);
		NullFilter.addToForm(this, valueId, element);
		return this;
	}
	
	// filter
	
	public FilterHelper _notNull(String fieldId, Object conditionValue) {
		return _isNull(fieldId, fieldId, conditionValue);
	}
	public FilterHelper _notNull(String fieldId, String valueId, Object conditionValue) {
		list.addFilter(NullFilter.getNotNullInstance(this, fieldId, valueId, conditionValue));
		return this;
	}

	// constant filter

	public FilterHelper notNullConst(String fieldId) {
		list.addFilter(NullFilter.getIsNullConstantInstance(this, fieldId));
		return this;
	}	
	
	// ========== RANGE ==========
	
	// filter with form element
	
	public FilterHelper range(String fieldId) throws Exception {
		return range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId));
	}
	public FilterHelper range(String fieldId, String lowValueId, String highValueId) throws Exception {
		_range(fieldId, lowValueId, highValueId);
		RangeFilter.addToForm(this, lowValueId, highValueId);
		return this;
	}
	public FilterHelper range(String fieldId, Control lowControl, Control highControl) throws Exception {
		return range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId), lowControl, highControl);
	}
	public FilterHelper range(String fieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) throws Exception {
		_range(fieldId, lowValueId, highValueId);
		RangeFilter.addToForm(this, lowValueId, highValueId, lowControl, highControl);
		return this;
	}
	public FilterHelper range(String fieldId, FormElement lowElement, FormElement highElement) throws Exception {
		return range(fieldId, getLowValueId(fieldId), getHighValueId(fieldId), lowElement, highElement);
	}
	public FilterHelper range(String fieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) throws Exception {
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
	
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId) throws Exception {
		return fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) throws Exception {
		_fieldRangeInValueRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId);
		return this;
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, Control lowControl, Control highControl) throws Exception {
		return fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowControl, highControl);
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) throws Exception {
		_fieldRangeInValueRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowControl, highControl);
		return this;
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, FormElement lowElement, FormElement highElement) throws Exception {
		return fieldRangeInValueRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowElement, highElement);
	}
	public FilterHelper fieldRangeInValueRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) throws Exception {
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
	
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId) throws Exception {
		return valueRangeInFieldRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) throws Exception {
		_valueRangeInFieldRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId);
		return this;
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, Control lowControl, Control highControl) throws Exception {
		return valueRangeInFieldRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowControl, highControl);
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) throws Exception {
		_valueRangeInFieldRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowControl, highControl);
		return this;
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, FormElement lowElement, FormElement highElement) throws Exception {
		return valueRangeInFieldRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowElement, highElement);
	}
	public FilterHelper valueRangeInFieldRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) throws Exception {
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
	
	public FilterHelper overlapRange(String lowFieldId, String highFieldId) throws Exception {
		return overlapRange(lowFieldId, highFieldId, lowFieldId, highFieldId);
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId) throws Exception {
		_overlapRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId);
		return this;
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, Control lowControl, Control highControl) throws Exception {
		return overlapRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowControl, highControl);
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, Control lowControl, Control highControl) throws Exception {
		_overlapRange(lowFieldId, highFieldId, lowValueId, highFieldId);
		RangeInRangeFilter.addToForm(this, lowValueId, highFieldId, lowControl, highControl);
		return this;
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, FormElement lowElement, FormElement highElement) throws Exception {
		return overlapRange(lowFieldId, highFieldId, lowFieldId, highFieldId, lowElement, highElement);
	}
	public FilterHelper overlapRange(String lowFieldId, String highFieldId, String lowValueId, String highValueId, FormElement lowElement, FormElement highElement) throws Exception {
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
		private List params = new ArrayList();
		
		SqlFunction(String name) {
			this.name = name;
		}
		
		private ExpressionBuilder[] getParams() {
			return (ExpressionBuilder[]) params.toArray(new ExpressionBuilder[params.size()]);
		}
		
		// add params
		
		public SqlFunction addFieldParam(String fieldId) {
			params.add(new Field(fieldId));
			return this;
		}
		
		public SqlFunction addValueParam(String valueId) throws Exception {
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return _addValueParam(valueId);			
		}
		public SqlFunction addValueParam(String valueId, Control control) throws Exception {
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return _addValueParam(valueId);			
		}
		public SqlFunction addValueParam(String valueId, FormElement element) throws Exception {
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
		
		public FilterHelper eqValue(String valueId) throws Exception {
			_eqValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return FilterHelper.this;
		}
		public FilterHelper eqValue(String valueId, Control control) throws Exception {
			_eqValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return FilterHelper.this;
		}
		public FilterHelper eqValue(String valueId, FormElement element) throws Exception {
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
		
		public FilterHelper gtValue(String valueId) throws Exception {
			_gtValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return FilterHelper.this;
		}
		public FilterHelper gtValue(String valueId, Control control) throws Exception {
			_gtValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return FilterHelper.this;
		}
		public FilterHelper gtValue(String valueId, FormElement element) throws Exception {
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
		
		public FilterHelper ltValue(String valueId) throws Exception {
			_ltValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId);
			return FilterHelper.this;
		}
		public FilterHelper ltValue(String valueId, Control control) throws Exception {
			_ltValue(valueId);
			SqlFunctionFilter.addToForm(FilterHelper.this, valueId, control);
			return FilterHelper.this;
		}
		public FilterHelper ltValue(String valueId, FormElement element) throws Exception {
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
