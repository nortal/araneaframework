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


public abstract class GreaterThanFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;
	
	public static final boolean STRICT_BY_DEFAULT = false;
	
	private Comparator comparator;

	// FilterData instance

	public static GreaterThanFilter getInstance() {
		return getInstance(null, null, STRICT_BY_DEFAULT);
	}

	public static GreaterThanFilter getInstance(String columnId) {
		return getInstance(columnId, null, STRICT_BY_DEFAULT);
	}

	public static GreaterThanFilter getInstance(String columnId, String valueId) {
		return getInstance(columnId, valueId, STRICT_BY_DEFAULT);
	}
	
	public static GreaterThanFilter getInstance(boolean strict) {
		return getInstance(null, null, strict);
	}

	public static GreaterThanFilter getInstance(String columnId, boolean strict) {
		return getInstance(columnId, null, strict);
	}

	public static GreaterThanFilter getInstance(String columnId, String valueId,
			boolean strict) {
		GreaterThanFilter filter;
		if (strict) {
			filter = new Strict();
		} else {
			filter = new NonStrict();
		}
		filter.setFieldId(columnId);
		filter.setValueId(valueId);
		return filter;
	}	
	
	// Constant instance
	
	public static GreaterThanFilter getConstantInstance(Object value) {
		return getConstantInstance(null, null, value, STRICT_BY_DEFAULT);
	}

	public static GreaterThanFilter getConstantInstance(String columnId,
			Object value) {
		return getConstantInstance(columnId, null, value, STRICT_BY_DEFAULT);
	}

	public static GreaterThanFilter getConstantInstance(String columnId,
			String valueId, Object value) {
		return getConstantInstance(columnId, valueId, value, STRICT_BY_DEFAULT);
	}
	
	public static GreaterThanFilter getConstantInstance(Object value,
			boolean strict) {
		return getConstantInstance(null, null, value, strict);
	}

	public static GreaterThanFilter getConstantInstance(String columnId,
			Object value, boolean strict) {
		return getConstantInstance(columnId, null, value, strict);
	}

	public static GreaterThanFilter getConstantInstance(String columnId,
			String valueId, Object value, boolean strict) {
		GreaterThanFilter filter;
		if (strict) {
			filter = new Strict();
		} else {
			filter = new NonStrict();
		}
		filter.setFieldId(columnId);
		filter.setValueId(valueId);
		filter.setValue(value);
		return filter;
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}	
	
	static class Strict extends GreaterThanFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.gt(
					buildVariableExpression(),
					buildValueExpression(filterInfo),
					getComparator());
		}
	}
	
	static class NonStrict extends GreaterThanFilter {
		public Expression buildExpression(Map filterInfo) {
			if (!isActive(filterInfo)) {
				return null;
			}
			return ExpressionUtil.ge(
					buildVariableExpression(),
					buildValueExpression(filterInfo),
					getComparator());
		}
	}	
}
