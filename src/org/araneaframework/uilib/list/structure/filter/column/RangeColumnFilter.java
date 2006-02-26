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
import org.araneaframework.backend.list.memorybased.expression.AlwaysTrueExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.ComparableType;
import org.araneaframework.uilib.list.structure.filter.ColumnFilter;


public abstract class RangeColumnFilter extends ComparableType implements ColumnFilter {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_START_SUFIX = "_start";
	public static final String DEFAULT_END_SUFIX = "_end";	
	
	private String columnId;
	private String startFilterInfoKey;
	private String endFilterInfoKey;
	
	protected RangeColumnFilter(String columnId) {
		setColumnId(columnId);
	}
	
	protected RangeColumnFilter() {
		// empty constructor
	}
	
	public String getColumnId() {
		return this.columnId;
	}
	
	public void setColumnId(String columnId) {
		if (columnId == null) {
			throw new RuntimeException("Column Id must be not null");
		}
		this.columnId = columnId;
		if (this.startFilterInfoKey == null) {
			this.startFilterInfoKey = columnId + DEFAULT_START_SUFIX;
		}
		if (this.endFilterInfoKey == null) {
			this.endFilterInfoKey = columnId + DEFAULT_END_SUFIX;
		}
	}
	
	public String getEndFilterInfoKey() {
		return this.endFilterInfoKey;
	}
	
	public void setEndFilterInfoKey(String endFilterInfoKey) {
		this.endFilterInfoKey = endFilterInfoKey;
	}
	
	public String getStartFilterInfoKey() {
		return this.startFilterInfoKey;
	}
	
	public void setStartFilterInfoKey(String startFilterInfoKey) {
		this.startFilterInfoKey = startFilterInfoKey;
	}
	
	private void validate() {
		if (this.columnId == null) {
			throw new RuntimeException("Column Id must be provided"); 
		}
		if (this.startFilterInfoKey == null) {
			throw new RuntimeException("Start FilterInfo key must be provided"); 
		}
		if (this.endFilterInfoKey == null) {
			throw new RuntimeException("End FilterInfo key must be provided"); 
		}
		if (this.startFilterInfoKey.equals(this.endFilterInfoKey)) {
			throw new RuntimeException("Start and End FilterInfo keys must be different"); 
		}
	}
	
	public Expression buildExpression(Map filterInfo) {
		validate();
		
		Object startValue = filterInfo.get(this.startFilterInfoKey);
		Object endValue = filterInfo.get(this.endFilterInfoKey);
		
		if (startValue == null && endValue == null) {
			return new AlwaysTrueExpression();
		}
		
		Expression columnExpr = new VariableExpression(this.columnId);
		Expression startValueExpr = startValue != null ? new ValueExpression(startValue) : null;
		Expression endValueExpr = endValue != null ? new ValueExpression(endValue) : null;
		return buildAction(columnExpr, startValueExpr, endValueExpr); 
	}
	
	protected abstract Expression buildAction(Expression var, Expression startValue, Expression endValue);
	
	/*
	 * Subclasses
	 */
	
	public static class Strict extends RangeColumnFilter {
		private static final long serialVersionUID = 1L;		
		public Strict(String columnId) {
			super(columnId);
		}		
		public Strict() {
			super();
		}
		protected Expression buildAction(Expression var, Expression startValue, Expression endValue) {
			AndExpression expr = new AndExpression();
			if (startValue != null) {
				expr.add(new GreaterThanExpression(var, startValue, getComparator()));
			}
			if (endValue != null) {
				expr.add(new LowerThanExpression(var, endValue, getComparator()));
			}
			return expr;
		}
	}
	
	public static class NonStrict extends RangeColumnFilter {
		private static final long serialVersionUID = 1L;
		public NonStrict(String columnId) {
			super(columnId);
		}		
		public NonStrict() {
			super();
		}		
		protected Expression buildAction(Expression var, Expression startValue, Expression endValue) {
			AndExpression expr = new AndExpression();
			if (startValue != null) {
				expr.add(new OrExpression().add(
						new GreaterThanExpression(var, startValue, getComparator())).add(
								new ComparedEqualsExpression(var, startValue, getComparator())));
			}
			if (endValue != null) {
				expr.add(new OrExpression().add(
						new LowerThanExpression(var, endValue, getComparator())).add(
								new ComparedEqualsExpression(var, endValue, getComparator())));
			}
			return expr;
		}
	}
	
}
