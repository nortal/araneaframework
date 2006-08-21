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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import org.araneaframework.backend.list.memorybased.expression.procedure.ProcedureExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.ComparableType;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;


public abstract class SimpleProcedureFilter extends ComparableType implements ListFilter {

	private static final long serialVersionUID = 1L;

	protected Procedure procedure = new Procedure();
	protected SubFilter operand;	
		
	public String getProcName() {
		return this.procedure.getName();
	}

	public SimpleProcedureFilter setProcName(String name) {
		this.procedure.setName(name);
		return this;
	}
	
	public SimpleProcedureFilter addProcColumn(String columnId) {
		this.procedure.addColumn(columnId);
		return this;
	}
	
	public SimpleProcedureFilter addProcValue(String filterInfoKey) {
		this.procedure.addValue(filterInfoKey);
		return this;
	}
			
	public SimpleProcedureFilter addProcConstant(Object value) {
		this.procedure.addConstant(value);
		return this;
	}
	
	public SimpleProcedureFilter setColumn(String columnId) {
		this.operand = new Column(columnId);
		return this;
	}
	
	public SimpleProcedureFilter setValue(String filterInfoKey) {
		this.operand = new Value(filterInfoKey);
		return this;
	}
	
	public SimpleProcedureFilter setConstant(Object value) {
		this.operand = new Constant(value);
		return this;
	}
	
	protected void validate() {
		if (this.procedure.getName() == null) {
			throw new RuntimeException("Procedure name must be provided");
		}
		if (this.operand == null) {
			throw new RuntimeException("Column, Value or Constant must be provided as another operand");
		}
	}
	
	public Expression buildExpression(Map filterInfo) {
		if (!isFilterActive(filterInfo)) {
			return new AlwaysTrueExpression();
		}
		return buildAction(filterInfo, buildLeftOperand(filterInfo), buildRightOperand(filterInfo));
	}

	protected boolean isFilterActive(Map filterInfo) {
		validate();
		return this.operand.isActive(filterInfo);
	}

	protected Expression buildLeftOperand(Map filterInfo) {
		return this.procedure.buildExpression(filterInfo);
	}

	protected Expression buildRightOperand(Map filterInfo) {
		return this.operand.buildExpression(filterInfo);
	}
	
	protected abstract Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand);

	
	/*
	 * Nested classes
	 */
	
	protected static class Procedure implements ListFilter {
		
		private static final long serialVersionUID = 1L;
		
		private String name;
		private List params = new ArrayList();
		
		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public void addColumn(String columnId) {
			this.params.add(new Column(columnId));
		}
		
		public void addValue(String filterInfoKey) {
			this.params.add(new Value(filterInfoKey));
		}
				
		public void addConstant(Object value) {
			this.params.add(new Constant(value));
		}

		public boolean isActive(Map filterInfo) {
			return true;
		}
		
		public Expression buildExpression(Map filterInfo) {			
			ProcedureExpression expr = new ProcedureExpression(this.name);
			for (Iterator i = this.params.iterator(); i.hasNext();) {
				ListFilter arg = (ListFilter) i.next();
				expr.add(arg.buildExpression(filterInfo));
			}
			return expr;
		}			
	}

	protected static interface SubFilter extends ListFilter {
		boolean isActive(Map filterInfo);
	}

	protected static class Column implements SubFilter {

		private static final long serialVersionUID = 1L;
		
		private String id;

		public Column(String id) {
			if (id == null) {
				throw new RuntimeException("Column Id must be provided");
			}
			this.id = id;
		}

		public boolean isActive(Map filterInfo) {
			return true;
		}
		
		public Expression buildExpression(Map filterInfo) {
			return new VariableExpression(this.id);
		}
		
	}

	protected static class Value implements SubFilter {

		private static final long serialVersionUID = 1L;
		
		private String filterInfoKey;

		public Value(String filterInfoKey) {
			if (filterInfoKey == null) {
				throw new RuntimeException("FilterInfo key must be provided");
			}			
			this.filterInfoKey = filterInfoKey;
		}

		public boolean isActive(Map filterInfo) {
			return filterInfo.get(this.filterInfoKey) != null;
		}

		public Expression buildExpression(Map filterInfo) {
			return new ValueExpression(filterInfo.get(this.filterInfoKey));
		}
		
	}

	protected static class Constant implements SubFilter {

		private static final long serialVersionUID = 1L;
		
		private Object value;

		public Constant(Object value) {
			this.value = value;
		}

		public boolean isActive(Map filterInfo) {
			return true;
		}

		public Expression buildExpression(Map filterInfo) {
			return new ValueExpression(this.value);
		}
		
	}
	
	/*
	 * Subclasses
	 */
	
	public static class Equals extends SimpleProcedureFilter {
		private static final long serialVersionUID = 1L;
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			if (isComparatorNatural()) {
				return new EqualsExpression(leftOperand, rightOperand);
			}
			return new ComparedEqualsExpression(leftOperand, rightOperand, getComparator());
		}
	}
	
	public static class GreaterThan extends SimpleProcedureFilter {
		private static final long serialVersionUID = 1L;
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new GreaterThanExpression(leftOperand, rightOperand, getComparator());
		}
	}
	
	public static class LowerThan extends SimpleProcedureFilter {
		private static final long serialVersionUID = 1L;
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new LowerThanExpression(leftOperand, rightOperand, getComparator());
		}
	}
	
	public static class GreaterThanOrEquals extends SimpleProcedureFilter {
		private static final long serialVersionUID = 1L;
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new OrExpression().
			add(new GreaterThanExpression(leftOperand, rightOperand, getComparator())).		
			add(new ComparedEqualsExpression(leftOperand, rightOperand, getComparator()));
		}
	}
	
	public static class LowerThanOrEquals extends SimpleProcedureFilter {
		private static final long serialVersionUID = 1L;
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new OrExpression().
			add(new LowerThanExpression(leftOperand, rightOperand, getComparator())).		
			add(new ComparedEqualsExpression(leftOperand, rightOperand, getComparator()));
		}
	}
	
	public static class Like extends SimpleProcedureFilter {
		private static final long serialVersionUID = 1L;
		private LikeConfiguration configuration = new LikeConfiguration();
		public LikeConfiguration getConfiguration() {
			return configuration;
		}
		public Like setConfiguration(LikeConfiguration configuration) {
			this.configuration = configuration;
			return this;
		}
		protected Expression buildAction(Map filterInfo, Expression leftOperand, Expression rightOperand) {
			return new LikeExpression(leftOperand, (ValueExpression) rightOperand, isIgnoreCase(), configuration);
		}
	}
}
