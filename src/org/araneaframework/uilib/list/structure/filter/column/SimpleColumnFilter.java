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
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.ComparableType;
import org.araneaframework.uilib.list.structure.filter.ColumnFilter;


public abstract class SimpleColumnFilter extends ComparableType implements ColumnFilter {

	private static final long serialVersionUID = 1L;
	
	private String columnId;
	private String filterInfoKey;
	
	protected SimpleColumnFilter(String columnId) {
		setColumnId(columnId);
	}
	
	protected SimpleColumnFilter() {
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
		if (this.filterInfoKey == null) {
			this.filterInfoKey = columnId;
		}
	}

	public String getFilterInfoKey() {
		return this.filterInfoKey;
	}

	public void setFilterInfoKey(String filterInfoKey) {
		this.filterInfoKey = filterInfoKey;
	}	
	
	public Expression buildExpression(Map filterInfo) {
		if (!isFilterActive(filterInfo)) {
			return new AlwaysTrueExpression();
		}
		return buildAction(filterInfo, buildLeftOperand(filterInfo), buildRightOperand(filterInfo));
	}

	protected boolean isFilterActive(Map filterInfo) {
		if (this.filterInfoKey == null) {
			throw new RuntimeException("FilterInfo key must be provided"); 
		}
		return filterInfo.get(this.filterInfoKey) != null;
	}

	protected Expression buildLeftOperand(Map filterInfo) {
		if (this.columnId == null) {
			throw new RuntimeException("Column Id must be provided"); 
		}
		return new VariableExpression(this.columnId);
	}

	protected Expression buildRightOperand(Map filterInfo) {
		if (this.filterInfoKey == null) {
			throw new RuntimeException("FilterInfo key must be provided"); 
		}
		return new ValueExpression(this.filterInfoKey, filterInfo.get(this.filterInfoKey));
	}
	
	protected abstract Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand);
	
	/*
	 * Subclasses
	 */
	
	public static class Equals extends SimpleColumnFilter {
		private static final long serialVersionUID = 1L;
		public Equals(String columnId) {
			super(columnId);
		}		
		public Equals() {
			super();
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			if (isComparatorNatural()) {
				return new EqualsExpression(leftOperand, rightOperand);
			}
			return new ComparedEqualsExpression(leftOperand, rightOperand, getComparator());
		}
	}
	
	public static class GreaterThan extends SimpleColumnFilter {
		private static final long serialVersionUID = 1L;
		public GreaterThan(String columnId) {
			super(columnId);
		}		
		public GreaterThan() {
			super();
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new GreaterThanExpression(leftOperand, rightOperand, getComparator());
		}
	}
	
	public static class LowerThan extends SimpleColumnFilter {
		private static final long serialVersionUID = 1L;
		public LowerThan(String columnId) {
			super(columnId);
		}		
		public LowerThan() {
			super();
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new LowerThanExpression(leftOperand, rightOperand, getComparator());
		}
	}
	
	public static class GreaterThanOrEquals extends SimpleColumnFilter {
		private static final long serialVersionUID = 1L;
		public GreaterThanOrEquals(String columnId) {
			super(columnId);
		}		
		public GreaterThanOrEquals() {
			super();
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new OrExpression().
			add(new GreaterThanExpression(leftOperand, rightOperand, getComparator())).		
			add(new ComparedEqualsExpression(leftOperand, rightOperand, getComparator()));
		}
	}
	
	public static class LowerThanOrEquals extends SimpleColumnFilter {
		private static final long serialVersionUID = 1L;
		public LowerThanOrEquals(String columnId) {
			super(columnId);
		}		
		public LowerThanOrEquals() {
			super();
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new OrExpression().
			add(new LowerThanExpression(leftOperand, rightOperand, getComparator())).		
			add(new ComparedEqualsExpression(leftOperand, rightOperand, getComparator()));
		}
	}
	
	public static class Like extends SimpleColumnFilter {
		private static final long serialVersionUID = 1L;
		public Like(String columnId) {
			super(columnId);
		}		
		public Like() {
			super();
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new LikeExpression(leftOperand, (ValueExpression) rightOperand, isIgnoreCase());
		}
	}
}
