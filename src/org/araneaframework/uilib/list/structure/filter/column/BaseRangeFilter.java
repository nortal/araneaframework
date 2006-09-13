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

package org.araneaframework.uilib.list.structure.filter.column;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.filter.FieldFilter;
import org.araneaframework.uilib.list.util.ExpressionUtil;


public abstract class BaseRangeFilter implements FieldFilter {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_LOW_SUFIX = "_start";
	public static final String DEFAULT_HIGH_SUFIX = "_end";	
	
	// Field
	private String fieldId;
	
	// Values
	private String lowValueId;
	private String highValueId;	
	
	public String getFieldId() {
		return this.fieldId;
	}
	
	public void setFieldId(String fieldId) {
		Validate.notNull(fieldId);
		this.fieldId = fieldId;
		if (this.lowValueId == null) {
			this.lowValueId = fieldId + DEFAULT_LOW_SUFIX;
		}
		if (this.highValueId == null) {
			this.highValueId = fieldId + DEFAULT_HIGH_SUFIX;
		}
	}
	
	public String getHighValueId() {
		return this.highValueId;
	}
	
	public void setHighValueId(String highValueId) {
		if (highValueId != null) {
			this.highValueId = highValueId;			
		}
	}
	
	public String getLowValueId() {
		return this.lowValueId;
	}
	
	public void setLowValueId(String lowValueId) {
		if (lowValueId != null) {
			this.lowValueId = lowValueId;			
		}
	}
	
	// Building expressions
	
	protected boolean isActive(Map filterInfo) {
		return filterInfo.containsKey(getLowValueId()) || 
			filterInfo.containsKey(getHighValueId());
	}
	
	protected VariableExpression buildVariableExpression() {
		return ExpressionUtil.var(getFieldId());
	}
	
	protected ValueExpression buildLowValueExpression(Map filterInfo) {
		Object value = filterInfo.get(getLowValueId());
		if (value == null) {
			return null;
		}
		value = convertLowValue(value);
		return ExpressionUtil.value(getLowValueId(), value);
	}
	
	protected ValueExpression buildHighValueExpression(Map filterInfo) {
		Object value = filterInfo.get(getHighValueId());
		if (value == null) {
			return null;
		}
		value = convertHighValue(value);
		return ExpressionUtil.value(getHighValueId(), value);
	}
	
	protected Object convertLowValue(Object value) { return value; }
	protected Object convertHighValue(Object value) { return value; }			
}
