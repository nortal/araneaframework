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

import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;


public abstract class NullFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;
	
	public static NullFilter getIdNullInstance(FilterContext ctx,
			String fieldId, String valueId, Object conditionValue) {
		IsNull filter = new IsNull();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setConditionValue(conditionValue);
		return filter;
	}
	
	public static NullFilter getIsNullConstantInstance(FilterContext ctx,
			String fieldId) {
		IsNullConstant filter = new IsNullConstant();
		filter.setFieldId(fieldId);
		return filter;
	}
	
	public static NullFilter getNotNullInstance(FilterContext ctx,
			String fieldId, String valueId, Object conditionValue) {
		NotNull filter = new NotNull();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setConditionValue(conditionValue);
		return filter;
	}
	
	public static NullFilter getNotNullConstantInstance(FilterContext ctx,
			String fieldId) {
		NotNullConstant filter = new NotNullConstant();
		filter.setFieldId(fieldId);
		return filter;
	}
	
	public static void addToForm(FilterContext ctx, String id, FormElement element) throws Exception {
		ctx.getForm().addElement(id, element);
	}
	
	public static void addToForm(FilterContext ctx, String id, Control control) throws Exception {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control));
	}

	public static void addToForm(FilterContext ctx, String id) throws Exception {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id));
	}
	
	static abstract class ConditionValue extends NullFilter {
		private Object conditionValue;
		
		public Object getConditionValue() {
			return conditionValue;
		}

		public void setConditionValue(Object conditionValue) {
			this.conditionValue = conditionValue;
		}
	}
	
	static class IsNull extends ConditionValue {		
		public Expression buildExpression(Map data) {
			if (equals(data.get(getValueId()), getConditionValue())) {
				return ExpressionUtil.isNull(buildVariableExpression());
			}
			return null;
		}
	}
	
	static class IsNullConstant extends NullFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.isNull(buildVariableExpression());
		}
	}
	
	static class NotNull extends ConditionValue {		
		public Expression buildExpression(Map data) {
			if (equals(data.get(getValueId()), getConditionValue())) {
				return ExpressionUtil.notNull(buildVariableExpression());
			}
			return null;
		}
	}
	
	static class NotNullConstant extends NullFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.notNull(buildVariableExpression());
		}
	}
	
	static boolean equals(Object o1, Object o2) {
		return o1 == o2 || o1 == null ? o2 == null : o1.equals(o2);
	}
	
}
