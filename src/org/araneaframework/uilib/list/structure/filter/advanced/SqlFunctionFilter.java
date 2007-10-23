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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionBuilder;
import org.araneaframework.backend.list.memorybased.expression.LaxyExpressionIterator;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.BaseFilter;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.structure.filter.atomic.Constant;
import org.araneaframework.uilib.list.structure.filter.atomic.Field;
import org.araneaframework.uilib.list.structure.filter.atomic.Value;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;


public abstract class SqlFunctionFilter extends BaseFilter {

	private static final long serialVersionUID = 1L;
	
	// creating filter

	public static SqlFunctionFilter getFieldEqualInstance(
			FilterContext ctx, String fieldId,
			String functionName, ExpressionBuilder[] functionParams) {		
		return initField(createEqual(), ctx, fieldId, functionName, functionParams);
	}

	public static SqlFunctionFilter getFieldGreaterThanInstance(
			FilterContext ctx, String fieldId,
			String functionName, ExpressionBuilder[] functionParams) {	
		return initField(createGreaterThan(ctx), ctx, fieldId, functionName, functionParams);
	}	

	public static SqlFunctionFilter getFieldLowerThanInstance(
			FilterContext ctx, String fieldId,
			String functionName, ExpressionBuilder[] functionParams) {	
		return initField(createLowerThan(ctx), ctx, fieldId, functionName, functionParams);
	}
	
	public static SqlFunctionFilter getValueEqualInstance(
			FilterContext ctx, String valueId,
			String functionName, ExpressionBuilder[] functionParams) {		
		return initValue(createEqual(), ctx, valueId, functionName, functionParams);
	}

	public static SqlFunctionFilter getValueGreaterThanInstance(
			FilterContext ctx, String valueId,
			String functionName, ExpressionBuilder[] functionParams) {	
		return initValue(createGreaterThan(ctx), ctx, valueId, functionName, functionParams);
	}	

	public static SqlFunctionFilter getValueLowerThanInstance(
			FilterContext ctx, String valueId,
			String functionName, ExpressionBuilder[] functionParams) {	
		return initValue(createLowerThan(ctx), ctx, valueId, functionName, functionParams);
	}
	
	public static SqlFunctionFilter getConstantEqualInstance(
			FilterContext ctx, String valueId, Object value,
			String functionName, ExpressionBuilder[] functionParams) {		
		return initConstant(createEqual(), ctx, valueId, value, functionName, functionParams);
	}

	public static SqlFunctionFilter getConstantGreaterThanInstance(
			FilterContext ctx, String valueId, Object value,
			String functionName, ExpressionBuilder[] functionParams) {	
		return initConstant(createGreaterThan(ctx), ctx, valueId, value, functionName, functionParams);
	}	

	public static SqlFunctionFilter getConstantLowerThanInstance(
			FilterContext ctx, String valueId, Object value,
			String functionName, ExpressionBuilder[] functionParams) {	
		return initConstant(createLowerThan(ctx), ctx, valueId, value, functionName, functionParams);
	}	
	
	private static SqlFunctionFilter createEqual() {
		return new Equal();
	}
	
	private static SqlFunctionFilter createGreaterThan(FilterContext ctx) {
		SqlFunctionFilter filter;
		if (ctx.isStrict()) {
			filter = new GreaterThan();
		} else {
			filter = new GreaterThanOrEquals();
		}
		return filter;
	}
	
	private static SqlFunctionFilter createLowerThan(FilterContext ctx) {
		SqlFunctionFilter filter;
		if (ctx.isStrict()) {
			filter = new LowerThan();
		} else {
			filter = new LowerThanOrEquals();
		}		
		return filter;
	}	
	
	private static SqlFunctionFilter initField(SqlFunctionFilter filter,
			FilterContext ctx, String fieldId,
			String functionName, ExpressionBuilder[] functionParams) {
		filter.setOperand(new Field(fieldId));
		return init(filter, ctx, fieldId, functionName, functionParams);
	}
	
	private static SqlFunctionFilter initValue(SqlFunctionFilter filter,
			FilterContext ctx, String valueId,
			String functionName, ExpressionBuilder[] functionParams) {
		filter.setOperand(new Value(valueId));
		return init(filter, ctx, valueId, functionName, functionParams);
	}
	
	private static SqlFunctionFilter initConstant(SqlFunctionFilter filter,
			FilterContext ctx, String valueId, Object value,
			String functionName, ExpressionBuilder[] functionParams) {
		filter.setOperand(new Constant(valueId, value));
		return init(filter, ctx, valueId, functionName, functionParams);
	}	
	
	private static SqlFunctionFilter init(SqlFunctionFilter filter,
			FilterContext ctx, String fieldId,
			String functionName, ExpressionBuilder[] functionParams) {
		filter.setComparator(ctx.getFieldComparator(fieldId));
		filter.setProcedure(new Procedure(functionName, functionParams));
		return filter;
	}	
	
	// add to form
	
	public static void addToForm(FilterContext ctx, String id, FormElement element) {
		ctx.getForm().addElement(id, element);
	}
	
	public static void addToForm(FilterContext ctx, String id, Control control) {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control));
	}
	
	public static void addToForm(FilterContext ctx, String id) {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id));
	}

	private ExpressionBuilder procedure;
	private ExpressionBuilder operand;
	
	private Comparator comparator;
	
	public ExpressionBuilder getOperand() {
		return operand;
	}

	public void setOperand(ExpressionBuilder operand) {
		this.operand = operand;
	}

	public ExpressionBuilder getProcedure() {
		return procedure;
	}

	public void setProcedure(ExpressionBuilder procedure) {
		this.procedure = procedure;
	}

	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}
	
	static class Procedure implements ExpressionBuilder {		
		private static final long serialVersionUID = 1L;
		
		private String name;
		private ExpressionBuilder[] params;
		
		public Procedure(String name, ExpressionBuilder[] params) {
			this.name = name;
			this.params = params;
		}

		public Expression buildExpression(Map data) {
			return ExpressionUtil.sqlFunction(name,
					new LaxyExpressionIterator(
							Arrays.asList(params).iterator(),
							data));
		}
	}

	public static class Equal extends SqlFunctionFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.eq(
					getOperand().buildExpression(data),
					getProcedure().buildExpression(data),
					getComparator());
		}
	}
	
	public static class GreaterThan extends SqlFunctionFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.gt(
					getOperand().buildExpression(data),
					getProcedure().buildExpression(data),
					getComparator());
		}
	}
	
	public static class LowerThan extends SqlFunctionFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.lt(
					getOperand().buildExpression(data),
					getProcedure().buildExpression(data),
					getComparator());
		}
	}
	
	public static class GreaterThanOrEquals extends SqlFunctionFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.ge(
					getOperand().buildExpression(data),
					getProcedure().buildExpression(data),
					getComparator());
		}
	}
	
	public static class LowerThanOrEquals extends SqlFunctionFilter {
		public Expression buildExpression(Map data) {
			return ExpressionUtil.le(
					getOperand().buildExpression(data),
					getProcedure().buildExpression(data),
					getComparator());
		}
	}
}
