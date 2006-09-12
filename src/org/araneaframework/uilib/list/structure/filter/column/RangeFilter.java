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

import java.util.Comparator;
import java.util.Map;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.list.util.ExpressionUtil;


public abstract class RangeFilter extends BaseRangeFilter {

	private static final long serialVersionUID = 1L;
	
	public static final boolean STRICT_BY_DEFAULT = false;
	
	private Comparator comparator;
	
	// Strict not specified
	
	public static RangeFilter getInstance() {
		return getInstance(null, null, null, STRICT_BY_DEFAULT);
	}

	public static RangeFilter getInstance(String columnId) {
		return getInstance(columnId, null, null);
	}

	public static RangeFilter getInstance(String columnId,
			String lowValueId, String highValueId) {
		return getInstance(columnId, lowValueId, highValueId,
				STRICT_BY_DEFAULT);
	}
	
	// Strict specified

	public static RangeFilter getInstance(boolean strict) {
		return getInstance(null, null, null, strict);
	}

	public static RangeFilter getInstance(String columnId, boolean strict) {
		return getInstance(columnId, null, null, strict);
	}

	public static RangeFilter getInstance(String columnId,
			String lowValueId, String highValueId, boolean strict) {
		RangeFilter filter;
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
}
