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

package org.araneaframework.backend.list.helper.builder.expression;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.Resolver;
import org.araneaframework.backend.list.memorybased.expression.AlwaysTrueExpression;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.IsNullExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.NotExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.procedure.ProcedureExpression;
import org.araneaframework.backend.list.memorybased.expression.string.ConcatenationExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.backend.list.sqlexpr.SqlAlwaysTrueExpression;
import org.araneaframework.backend.list.sqlexpr.SqlBracketsExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlEqualsExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlGreaterThanExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlIsNullExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlLikeExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlLowerThanExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlValueExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlAndExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlNotExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlOrExpression;
import org.araneaframework.backend.list.sqlexpr.procedure.SqlProcedureExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlConcatenationExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlUpperExpression;

public class StandardExpressionToSqlExprBuilder extends BaseExpressionToSqlExprBuilder {
	
	protected Resolver mapper;
	
	protected ValueConverter converter; 
	
	public StandardExpressionToSqlExprBuilder() {
		addTranslator(ValueExpression.class, new ValueTranslator());
		addTranslator(VariableExpression.class, new VariableTranslator());
		addTranslator(AlwaysTrueExpression.class, new AlwaysTrueTranslator());
		addTranslator(EqualsExpression.class, new EqualsTranslator());
		addTranslator(IsNullExpression.class, new IsNullTranslator());
		addTranslator(ComparedEqualsExpression.class, new ComparedEqualsTranslator());
		addTranslator(GreaterThanExpression.class, new GreaterThanTranslator());
		addTranslator(LowerThanExpression.class, new LowerThanTranslator());
		addTranslator(AndExpression.class, new AndTranslator());
		addTranslator(OrExpression.class, new OrTranslator());
		addTranslator(NotExpression.class, new NotTranslator());
		addTranslator(LikeExpression.class, new LikeTranslator());
		addTranslator(ConcatenationExpression.class, new ConcatenationTranslator());
		addTranslator(ProcedureExpression.class, new ProcedureTranslator());
	}
	
	public void setConverter(ValueConverter converter) {
		this.converter = converter;
	}

	public void setMapper(Resolver mapper) {
		this.mapper = mapper;
	}

	protected Object convertValue(Value value) {
		return this.converter != null ? this.converter.convert(value) : value.getValue();
	}
	
	protected String resolveVariable(String variable) {
		return this.mapper != null ? (String) this.mapper.resolve(variable) : variable;
	}
	
	// constant
	
	class ValueTranslator implements ExprToSqlExprTranslator {
		public SqlExpression translate(Expression expr, ExpressionToSqlExprBuilder builder) {
			return new SqlValueExpression(convertValue((Value) expr));
		}
	}
	
	class VariableTranslator implements ExprToSqlExprTranslator {
		public SqlExpression translate(Expression expr, ExpressionToSqlExprBuilder builder) { 
			return new SqlStringExpression(resolveVariable(((VariableExpression) expr).getName()));
		}
	}	
	
	class AlwaysTrueTranslator implements ExprToSqlExprTranslator {
		public SqlExpression translate(Expression expr, ExpressionToSqlExprBuilder builder) {
			return new SqlAlwaysTrueExpression();
		}
	}
	
	// comparing	

	class EqualsTranslator implements ExprToSqlExprTranslator  {
		// TODO check values to be null deeply
		public SqlExpression translate(Expression expr, ExpressionToSqlExprBuilder builder) {
			Expression[] children = ((CompositeExpression) expr).getChildren();
			Expression leftExpr = children[0];
			Expression rightExpr = children[1];
			if (ValueExpression.class.isAssignableFrom(rightExpr.getClass())
					&& ((ValueExpression) rightExpr).getValue() == null) {
				return new SqlIsNullExpression(builder.buildSqlExpression(leftExpr));
			}
			if (ValueExpression.class.isAssignableFrom(leftExpr.getClass())
					&& ((ValueExpression) leftExpr).getValue() == null) {
				return new SqlIsNullExpression(builder.buildSqlExpression(rightExpr));
			}
			return new SqlEqualsExpression(builder.buildSqlExpression(leftExpr), 
					builder.buildSqlExpression(rightExpr));
		}
	}	
	
	class IsNullTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			return new SqlIsNullExpression(sqlChildren[0]);
		}
	}
	
	class ComparedEqualsTranslator extends ComparingExprToSqlExprTranslator {
		protected SqlExpression translateComparable(Expression expr, SqlExpression sql1, SqlExpression sql2) {
			return new SqlEqualsExpression(sql1, sql2);
		}
	}
	
	class GreaterThanTranslator extends ComparingExprToSqlExprTranslator {
		protected SqlExpression translateComparable(Expression expr, SqlExpression sql1, SqlExpression sql2) {
			return new SqlGreaterThanExpression(sql1, sql2);
		}
	}
	
	class LowerThanTranslator extends ComparingExprToSqlExprTranslator {
		protected SqlExpression translateComparable(Expression expr, SqlExpression sql1, SqlExpression sql2) {
			return new SqlLowerThanExpression(sql1, sql2);
		}
	}
	
	// logical
	
	class AndTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			SqlExpression temp = new SqlAndExpression().setChildren(sqlChildren);
			return sqlChildren.length > 1 ? new SqlBracketsExpression(temp) : temp;
		}
	}
	
	class OrTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			SqlExpression temp = new SqlOrExpression().setChildren(sqlChildren);
			return sqlChildren.length > 1 ? new SqlBracketsExpression(temp) : temp;
		}
	}
	
	class NotTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			return new SqlNotExpression(sqlChildren[0]);
		}
	}
	
	// String	
	
	class LikeTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			LikeExpression like = (LikeExpression) expr;			
			SqlExpression var = sqlChildren[0];
			SqlExpression mask = new SqlValueExpression(
					"%" + convertValue(like.getMask()) + "%");
			if (like.getIgnoreCase()) {
				var = new SqlUpperExpression(var);
				mask = new SqlUpperExpression(mask);
			}
			return new SqlLikeExpression(var, mask);
		}
	}
	
	class ConcatenationTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			return new SqlConcatenationExpression().setChildren(sqlChildren);
		}
	}
	
	// procedure
	
	class ProcedureTranslator extends CompositeExprToSqlExprTranslator {
		protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
			ProcedureExpression expr0 = (ProcedureExpression) expr;
			return new SqlProcedureExpression(expr0.getName()).setChildren(sqlChildren);
		}
	}
}
