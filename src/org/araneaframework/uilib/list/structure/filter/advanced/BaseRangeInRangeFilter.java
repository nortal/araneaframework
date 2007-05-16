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

package org.araneaframework.uilib.list.structure.filter.advanced;

import java.util.Map;
import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.filter.BaseFilter;
import org.araneaframework.uilib.list.util.ExpressionUtil;


public abstract class BaseRangeInRangeFilter extends BaseFilter {
	
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String lowFieldId;
	private String highFieldId;
	
	// Values
	private String lowValueId;
	private String highValueId;	
	
	public String getLowFieldId() {
		return this.lowFieldId;
	}
	
	public void setLowFieldId(String lowFieldId) {
		Validate.notNull(lowFieldId);
		this.lowFieldId = lowFieldId;
	}
	
	public String getHighFieldId() {
		return highFieldId;
	}

	public void setHighFieldId(String highFieldId) {
		Validate.notNull(highFieldId);
		this.highFieldId = highFieldId;
	}

	public String getHighValueId() {
		return this.highValueId;
	}
	
	public void setHighValueId(String highValueId) {
		this.highValueId = highValueId;			
	}
	
	public String getLowValueId() {
		return this.lowValueId;
	}
	
	public void setLowValueId(String lowValueId) {
		this.lowValueId = lowValueId;			
	}
	
	// Building expressions
	
	protected boolean isActive(Map filterInfo) {
		return filterInfo.containsKey(getLowValueId()) || 
			filterInfo.containsKey(getHighValueId());
	}
	
	protected VariableExpression buildLowVariableExpression() {
		return ExpressionUtil.var(getLowFieldId());
	}
	
	protected VariableExpression buildHighVariableExpression() {
		return ExpressionUtil.var(getHighFieldId());
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
