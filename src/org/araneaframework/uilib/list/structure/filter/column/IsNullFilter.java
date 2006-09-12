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

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.list.util.ExpressionUtil;


public class IsNullFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;
	
	private Object conditionValue;
	
	// FilterData instance (eanble IS NULL conditionally)

	public static BaseFieldFilter getInstance(Object conditionValue) {
		return getInstance(null, null, conditionValue);
	}

	public static BaseFieldFilter getInstance(String columnId, Object conditionValue) {
		return getInstance(columnId, null, conditionValue);
	}

	public static BaseFieldFilter getInstance(String columnId, String valueId,
			Object conditionValue) {
		IsNullFilter filter = new IsNullFilter();
		filter.setFieldId(columnId);
		filter.setValueId(valueId);
		filter.setConditionValue(conditionValue);
		return filter;
	}
	
	// Constant instance (always enable IS NULL)
	
	public static BaseFieldFilter getConstantInstance() {
		return getConstantInstance(null);
	}

	public static BaseFieldFilter getConstantInstance(String columnId) {
		Constant filter = new Constant();		
		filter.setFieldId(columnId);
		return filter;
	}

	private IsNullFilter() {
		// private
	}

	public Object getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(Object conditionValue) {
		this.conditionValue = conditionValue;
	}

	public Expression buildExpression(Map filterInfo) {
		if (!isActive(filterInfo)) {
			return null;
		}
		if (equals(filterInfo.get(getValueId()), getConditionValue())) {
			return ExpressionUtil.isNull(buildVariableExpression());
		}
		return null;
	}
	
	private static boolean equals(Object o1, Object o2) {
		return o1 == o2 || o1 == null ? o2 == null : o1.equals(o2);
	}
	
	// Constant IsNUll filter
	
	public static class Constant extends BaseFieldFilter {
		protected Constant() {
			// not public
		}
		
		public Expression buildExpression(Map filterInfo) {
			return ExpressionUtil.isNull(buildVariableExpression());
		}		
	}
}
