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

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.uilib.list.util.ExpressionUtil;


public abstract class DateRangeFilter extends BaseRangeFilter {

	private static final long serialVersionUID = 1L;
	
	public static final boolean STRICT_BY_DEFAULT = false;
	
	private Comparator comparator;	
	
	// Strict not specified
	
	public static BaseRangeFilter getInstance() {
		return getInstance(null, null, null, STRICT_BY_DEFAULT);
	}

	public static BaseRangeFilter getInstance(String columnId) {
		return getInstance(columnId, null, null);
	}

	public static BaseRangeFilter getInstance(String columnId,
			String lowValueId, String highValueId) {
		return getInstance(columnId, lowValueId, highValueId,
				STRICT_BY_DEFAULT);
	}
	
	// Strict specified

	public static BaseRangeFilter getInstance(boolean strict) {
		return getInstance(null, null, null, strict);
	}

	public static BaseRangeFilter getInstance(String columnId, boolean strict) {
		return getInstance(columnId, null, null, strict);
	}

	public static BaseRangeFilter getInstance(String columnId,
			String lowValueId, String highValueId, boolean strict) {
		BaseRangeFilter filter;
		if (strict) {
			filter = new Strict();
		} else {
			filter = new NonStrict();
		}
		filter.setFieldId(columnId);
		filter.setLowValueId(lowValueId);
		filter.setHighValueId(highValueId);
		return filter;
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}	
	
	public Expression buildExpression(Map filterInfo) {
		if (!isActive(filterInfo)) {
			return null;
		}
		Expression var = buildVariableExpression();
		// Left non-strict and right strict
		return ExpressionUtil.and(
				ExpressionUtil.ge(var, buildLowValueExpression(filterInfo), getComparator()),
				ExpressionUtil.lt(var, buildHighValueExpression(filterInfo), getComparator()));
	}	
	
	protected ValueExpression buildLowValueExpression(Map filterInfo) {
		Object value = filterInfo.get(getLowValueId());
		if (value == null) {
			return null;
		}
		Validate.isTrue(value instanceof Date, "Low value is not a date");
		return ExpressionUtil.value(getLowValueId(),
				convertLowValue((Date) value));
	}
	
	protected ValueExpression buildHighValueExpression(Map filterInfo) {
		Object value = filterInfo.get(getHighValueId());
		if (value == null) {
			return null;
		}
		Validate.isTrue(value instanceof Date, "High value is not a date");
		return ExpressionUtil.value(getHighValueId(),
				convertHighValue((Date) value));
	}
	
	protected Date convertLowValue(Date value) { return value; }
	protected Date convertHighValue(Date value) { return value; }
	
	static class Strict extends DateRangeFilter {
		protected Date convertLowValue(Date value) {
			return nextDay(value);
		}		
	}
	
	static class NonStrict extends DateRangeFilter {
		protected Date convertHighValue(Date value) {
			return nextDay(value);
		}	
	}
	
	static Date nextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);			
		return cal.getTime();
	}	
}
