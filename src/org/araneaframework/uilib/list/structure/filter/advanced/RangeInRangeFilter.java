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

import java.util.Comparator;
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


public abstract class RangeInRangeFilter extends BaseRangeInRangeFilter {

	private static final long serialVersionUID = 1L;
	
	private Comparator comparator;

	public static RangeInRangeFilter getFieldRangeInValueRangeInstance(FilterContext ctx,
			String lowFieldId, String highFieldId,
			String lowValueId, String highValueId) {
		RangeInRangeFilter filter;
		if (ctx.isStrict()) {
			filter = new FieldRangeInValueRangeStrict();
		} else {
			filter = new FieldRangeInValueRangeNonStrict();
		}
		return init(ctx, filter, lowFieldId, highFieldId, lowValueId, highValueId);
	}

	public static RangeInRangeFilter getValueRangeInFieldRangeInstance(FilterContext ctx,
			String lowFieldId, String highFieldId,
			String lowValueId, String highValueId) {
		RangeInRangeFilter filter;		
		if (ctx.isStrict()) {
			filter = new ValueRangeInFieldRangeStrict();
		} else {
			filter = new ValueRangeInFieldRangeNonStrict();
		}		
		return init(ctx, filter, lowFieldId, highFieldId, lowValueId, highValueId);
	}

	public static RangeInRangeFilter getOverlapInstance(FilterContext ctx,
			String lowFieldId, String highFieldId,
			String lowValueId, String highValueId) {
		RangeInRangeFilter filter;		
		if (ctx.isStrict()) {
			filter = new OverlapStrict();
		} else {
			filter = new OverlapNonStrict();
		}		
		return init(ctx, filter, lowFieldId, highFieldId, lowValueId, highValueId);
	}
	
	private static RangeInRangeFilter init(final FilterContext ctx,
			final RangeInRangeFilter filter,
			final String lowFieldId, final String highFieldId,
			final String lowValueId, final String highValueId) {
		filter.setLowFieldId(lowFieldId);
		filter.setHighFieldId(highFieldId);
		filter.setLowValueId(lowValueId);
		filter.setHighValueId(highValueId);
		
		ctx.addInitEvent(new Event() {
			public void run() {
				Comparator low = ctx.getFieldComparator(lowFieldId);
				Comparator high = ctx.getFieldComparator(highFieldId);
				if (low == null ? high == null : low.equals(high)) {
					filter.setComparator(low);
				} else {
					throw new IllegalArgumentException("Low field and high field comparator types must be the same.");				
				}
			}
		});
		
		return filter;
	}
	
	public static void addToForm(FilterContext ctx, String lowId, String highId, FormElement lowElement, FormElement highElement) {
		ctx.getForm().addElement(lowId, lowElement);
		ctx.getForm().addElement(highId, highElement);
		FormUtil.addConstraint(ctx.getForm(),
				new RangeConstraint(lowElement, highElement, true));
	}
	
	public static void addToForm(FilterContext ctx, String lowId, String highId, Control lowControl, Control highControl) {
		addToForm(ctx, lowId, highId,
				FilterFormUtil.createElement(ctx, lowId, lowControl),
				FilterFormUtil.createElement(ctx, highId, highControl));
	}
	
	public static void addToForm(FilterContext ctx, String lowId, String highId) {
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
	
	static class FieldRangeInValueRangeStrict extends RangeInRangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.and(
					ExpressionUtil.gt(buildLowVariableExpression(), buildLowValueExpression(filterInfo), getComparator()),
					ExpressionUtil.lt(buildHighVariableExpression(), buildHighValueExpression(filterInfo), getComparator()));			
		}		
	}
	
	static class FieldRangeInValueRangeNonStrict extends RangeInRangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.and(
					ExpressionUtil.ge(buildLowVariableExpression(), buildLowValueExpression(filterInfo), getComparator()),
					ExpressionUtil.le(buildHighVariableExpression(), buildHighValueExpression(filterInfo), getComparator()));			
		}		
	}
	
	static class ValueRangeInFieldRangeStrict extends RangeInRangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.and(
					ExpressionUtil.lt(buildLowVariableExpression(), buildLowValueExpression(filterInfo), getComparator()),
					ExpressionUtil.gt(buildHighVariableExpression(), buildHighValueExpression(filterInfo), getComparator()));			
		}
	}
	
	static class ValueRangeInFieldRangeNonStrict extends RangeInRangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.and(
					ExpressionUtil.le(buildLowVariableExpression(), buildLowValueExpression(filterInfo), getComparator()),
					ExpressionUtil.ge(buildHighVariableExpression(), buildHighValueExpression(filterInfo), getComparator()));			
		}
	}
	
	static class OverlapStrict extends RangeInRangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.and(
					ExpressionUtil.lt(buildLowVariableExpression(), buildHighValueExpression(filterInfo), getComparator()),
					ExpressionUtil.gt(buildHighVariableExpression(), buildLowValueExpression(filterInfo), getComparator()));			
		}
	}
	
	static class OverlapNonStrict extends RangeInRangeFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.and(
					ExpressionUtil.le(buildLowVariableExpression(), buildHighValueExpression(filterInfo), getComparator()),
					ExpressionUtil.ge(buildHighVariableExpression(), buildLowValueExpression(filterInfo), getComparator()));			
		}
	}
}
