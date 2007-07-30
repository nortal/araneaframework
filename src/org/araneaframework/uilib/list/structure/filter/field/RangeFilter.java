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

package org.araneaframework.uilib.list.structure.filter.field;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.constraint.RangeConstraint;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.list.util.FormUtil;
import org.araneaframework.uilib.util.Event;


public abstract class RangeFilter extends BaseRangeFilter {

	private static final long serialVersionUID = 1L;
	
	private Comparator comparator;

	public static RangeFilter getInstance(final FilterContext ctx, final String fieldId,
			final String lowValueId, final String highValueId) {
		final RangeFilter filter;
		
		Class type = ctx.getFieldType(fieldId);
		if (java.util.Date.class.equals(type)
				|| java.sql.Date.class.isAssignableFrom(type)) {
			if (ctx.isStrict()) {
				filter = new DateStrict();
			} else {
				filter = new DateNonStrict();
			}			
		} else {
			if (ctx.isStrict()) {
				filter = new Strict();
			} else {
				filter = new NonStrict();
			}			
		}
		
		filter.setFieldId(fieldId);
		filter.setLowValueId(lowValueId);
		filter.setHighValueId(highValueId);
		
		ctx.addInitEvent(new Event() {
			public void run() {
				filter.setComparator(ctx.getFieldComparator(fieldId));
			}			
		});
		
		return filter;
	}
	
	public static void addToForm(FilterContext ctx, String lowId, String highId, FormElement lowElement, FormElement highElement) throws Exception {
		ctx.getForm().addElement(lowId, lowElement);
		ctx.getForm().addElement(highId, highElement);
		FormUtil.addConstraint(ctx.getForm(),
				new RangeConstraint(lowElement, highElement, true));
	}
	
	public static void addToForm(FilterContext ctx, String lowId, String highId, Control lowControl, Control highControl) throws Exception {
		addToForm(ctx, lowId, highId,
				FilterFormUtil.createElement(ctx, lowId, lowControl),
				FilterFormUtil.createElement(ctx, highId, highControl));
	}
	
	public static void addToForm(FilterContext ctx, String lowId, String highId) throws Exception {
		addToForm(ctx, lowId, highId,
				FilterFormUtil.createElement(ctx, lowId),
				FilterFormUtil.createElement(ctx, highId));
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}
	
	// General
	
	static class Strict extends RangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			Expression var = buildVariableExpression();
			return ExpressionUtil.and(
					ExpressionUtil.gt(var, buildLowValueExpression(filterInfo), getComparator()),
					ExpressionUtil.lt(var, buildHighValueExpression(filterInfo), getComparator()));
		}		
	}
	
	static class NonStrict extends RangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			Expression var = buildVariableExpression();
			return ExpressionUtil.and(
					ExpressionUtil.ge(var, buildLowValueExpression(filterInfo), getComparator()),
					ExpressionUtil.le(var, buildHighValueExpression(filterInfo), getComparator()));
		}		
	}
	
	// java.util.Date / java.sql.Date
	
	static class RightStrict extends RangeFilter {
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
	}
	
	static class DateStrict extends RightStrict {
		protected Object convertLowValue(Object value) {
			return nextDay((Date) value);
		}
	}
	
	static class DateNonStrict extends RightStrict {
		protected Object convertHighValue(Object value) {
			return nextDay((Date) value);
		}
	}
	
	private static Date nextDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);			
		return cal.getTime();
	}
}
