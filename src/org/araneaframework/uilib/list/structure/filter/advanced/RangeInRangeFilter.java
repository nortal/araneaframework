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

import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.ComparableType;
import org.araneaframework.uilib.list.structure.ListFilter;


public abstract class RangeInRangeFilter extends ComparableType implements ListFilter {
	
	private static final long serialVersionUID = 1L;
	
	private String startColumnId;
	private String endColumnId;
	private String startFilterInfoKey;
	private String endFilterInfoKey;
	
	protected RangeInRangeFilter(String startColumnId, String endColumnId) {
		setStartColumnId(startColumnId);
		setEndColumnId(endColumnId);
	}
	
	protected RangeInRangeFilter() {
		// empty constructor
	}	
	
	public String getEndColumnId() {
		return this.endColumnId;
	}
	
	public RangeInRangeFilter setEndColumnId(String endColumnId) {
		if (endColumnId == null) {
			throw new RuntimeException("Column Id must be not null");
		}
		this.endColumnId = endColumnId;
		if (this.endColumnId == null) {
			this.endColumnId = endColumnId;
		}
		return this;
	}
	
	public String getEndFilterInfoKey() {
		return this.endFilterInfoKey;
	}
	
	public RangeInRangeFilter setEndFilterInfoKey(String endFilterInfoKey) {
		this.endFilterInfoKey = endFilterInfoKey;
		return this;
	}
	
	public String getStartColumnId() {
		return this.startColumnId;
	}
	
	public RangeInRangeFilter setStartColumnId(String startColumnId) {
		if (startColumnId == null) {
			throw new RuntimeException("Column Id must be not null");
		}
		this.startColumnId = startColumnId;
		if (this.startFilterInfoKey == null) {
			this.startFilterInfoKey = startColumnId;
		}
		return this;
	}
	
	public String getStartFilterInfoKey() {
		return this.startFilterInfoKey;
	}
	
	public RangeInRangeFilter setStartFilterInfoKey(String startFilterInfoKey) {
		this.startFilterInfoKey = startFilterInfoKey;
		return this;
	}
	
	private void validate() {
		if (this.startColumnId == null) {
			throw new RuntimeException("Start Column Id must be provided"); 
		}
		if (this.endColumnId == null) {
			throw new RuntimeException("End Column Id must be provided"); 
		}
		if (this.startFilterInfoKey == null) {
			throw new RuntimeException("Start FilterInfo key must be provided"); 
		}
		if (this.endFilterInfoKey == null) {
			throw new RuntimeException("End FilterInfo key must be provided"); 
		}
		if (this.startColumnId.equals(this.endColumnId)) {
			throw new RuntimeException("Start and End Column Ids must be different"); 
		}
		if (this.startFilterInfoKey.equals(this.endFilterInfoKey)) {
			throw new RuntimeException("Start and End FilterInfo keys must be different"); 
		}
	}
	
	public Expression buildExpression(Map filterInfo) {
		validate();
		
		Expression startVar = new VariableExpression(this.startColumnId);
		Expression endVar = new VariableExpression(this.endColumnId);
		Expression startVal = new ValueExpression(filterInfo.get(this.startFilterInfoKey));
		Expression endVal = new ValueExpression(filterInfo.get(this.endFilterInfoKey));
		return buildAction(startVar, endVar, startVal, endVal); 
	}
	
	protected abstract Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal);
	
	/*
	 * Subclasses
	 */
	
	public static class ColumnRangeInValueRangeStrict extends RangeInRangeFilter {
		private static final long serialVersionUID = 1L;
		public ColumnRangeInValueRangeStrict(String startColumnId, String endColumnId) {
			super(startColumnId, endColumnId);
		}		
		public ColumnRangeInValueRangeStrict() {
			super();
		}		
		protected Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal) {
			return new AndExpression().add(
					new GreaterThanExpression(startVar, startVal)).add(
							new LowerThanExpression(endVar, endVal));
		}
	}
	
	public static class ColumnRangeInValueRangeNonStrict extends RangeInRangeFilter {
		private static final long serialVersionUID = 1L;
		public ColumnRangeInValueRangeNonStrict(String startColumnId, String endColumnId) {
			super(startColumnId, endColumnId);
		}		
		public ColumnRangeInValueRangeNonStrict() {
			super();
		}
		protected Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal) {
			AndExpression expr = new AndExpression();
			expr.add(new OrExpression().add(
					new GreaterThanExpression(startVar, startVal)).add(
							new EqualsExpression(startVar, startVal)));			
			expr.add(new OrExpression().add(
					new LowerThanExpression(endVar, endVal)).add(
							new EqualsExpression(endVar, endVal)));
			return expr;
		}
		
	}
	
	public static class ValueRangeInColumnRangeStrict extends RangeInRangeFilter {
		private static final long serialVersionUID = 1L;
		public ValueRangeInColumnRangeStrict(String startColumnId, String endColumnId) {
			super(startColumnId, endColumnId);
		}		
		public ValueRangeInColumnRangeStrict() {
			super();
		}
		protected Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal) {
			return new AndExpression().add(
					new LowerThanExpression(startVar, startVal)).add(
							new GreaterThanExpression(endVar, endVal));
		}
	}
	
	public static class ValueRangeInColumnRangeNonStrict extends RangeInRangeFilter {
		private static final long serialVersionUID = 1L;
		public ValueRangeInColumnRangeNonStrict(String startColumnId, String endColumnId) {
			super(startColumnId, endColumnId);
		}		
		public ValueRangeInColumnRangeNonStrict() {
			super();
		}
		protected Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal) {
			AndExpression expr = new AndExpression();
			expr.add(new OrExpression().add(
					new LowerThanExpression(startVar, startVal)).add(
							new EqualsExpression(startVar, startVal)));			
			expr.add(new OrExpression().add(
					new GreaterThanExpression(endVar, endVal)).add(
							new EqualsExpression(endVar, endVal)));
			return expr;
		}
	}
	
	public static class RangesOverlapStrict extends RangeInRangeFilter {
		private static final long serialVersionUID = 1L;
		public RangesOverlapStrict(String startColumnId, String endColumnId) {
			super(startColumnId, endColumnId);
		}		
		public RangesOverlapStrict() {
			super();
		}
		protected Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal) {
			return new AndExpression().add(
					new LowerThanExpression(startVar, endVal)).add(
							new GreaterThanExpression(endVar, startVal));
		}
	}
	
	public static class RangesOverlapNonStrict extends RangeInRangeFilter {
		private static final long serialVersionUID = 1L;
		public RangesOverlapNonStrict(String startColumnId, String endColumnId) {
			super(startColumnId, endColumnId);
		}		
		public RangesOverlapNonStrict() {
			super();
		}
		protected Expression buildAction(Expression startVar, Expression endVar, Expression startVal, Expression endVal) {
			AndExpression expr = new AndExpression();
			expr.add(new OrExpression().add(
					new LowerThanExpression(startVar, endVal)).add(
							new EqualsExpression(startVar, endVal)));			
			expr.add(new OrExpression().add(
					new GreaterThanExpression(endVar, startVal)).add(
							new EqualsExpression(endVar, startVal)));
			return expr;			
		}
	}
}
