/*
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
 */

package org.araneaframework.backend.list.helper.builder.expression;

import org.apache.commons.lang.ArrayUtils;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.SqlLikeUtil;
import org.araneaframework.backend.list.helper.builder.ExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.expression.AlwaysTrueExpression;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;
import org.araneaframework.backend.list.memorybased.expression.LazyExpression;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EndsWithExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.IsNullExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.StartsWithExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.InExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.NotExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.procedure.ProcedureExpression;
import org.araneaframework.backend.list.memorybased.expression.string.ConcatenationExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.backend.list.sqlexpr.SqlAlwaysTrueExpression;
import org.araneaframework.backend.list.sqlexpr.SqlBracketsExpression;
import org.araneaframework.backend.list.sqlexpr.SqlEscapeExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlEqualsExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlGreaterThanExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlIsNullExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlLikeExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlLowerThanExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlValueExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlAndExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlInExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlNotExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlOrExpression;
import org.araneaframework.backend.list.sqlexpr.procedure.SqlProcedureExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlConcatenationExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlUpperExpression;

public class StandardExpressionToSqlExprBuilder extends
    BaseExpressionToSqlExprBuilder {

  public static final String ESCAPE_CHAR = "\\";

  protected VariableResolver mapper;

  protected ValueConverter converter;

  public StandardExpressionToSqlExprBuilder() {
    addTranslator(LazyExpression.class, new LazyTranslator());
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
    addTranslator(StartsWithExpression.class, new StartsWithTranslator());
    addTranslator(EndsWithExpression.class, new EndsWithTranslator());
    addTranslator(ConcatenationExpression.class, new ConcatenationTranslator());
    addTranslator(ProcedureExpression.class, new ProcedureTranslator());
    addTranslator(InExpression.class, new InTranslator());
  }

  public void setConverter(ValueConverter converter) {
    this.converter = converter;
  }

  public void setMapper(VariableResolver mapper) {
    this.mapper = mapper;
  }

  protected Object convertValue(Value value) {
    return this.converter != null ? this.converter.convert(value) : value.getValue();
  }

  protected String resolveVariable(Variable variable) {
    return this.mapper != null ? (String) this.mapper.resolve(variable) : variable.getName();
  }

  // lazy

  class LazyTranslator implements ExprToSqlExprTranslator {
    public SqlExpression translate(Expression expr,
        ExpressionToSqlExprBuilder builder) {
      LazyExpression lazyExpr = (LazyExpression) expr;
      return buildSqlExpression(lazyExpr.getExpression());
    }
  }

  // constant

  class ValueTranslator implements ExprToSqlExprTranslator {
    public SqlExpression translate(Expression expr,
        ExpressionToSqlExprBuilder builder) {
      return new SqlValueExpression(convertValue((Value) expr));
    }
  }

  class VariableTranslator implements ExprToSqlExprTranslator {
    public SqlExpression translate(Expression expr,
        ExpressionToSqlExprBuilder builder) {
      return new SqlStringExpression(resolveVariable((Variable) expr));
    }
  }

  static class AlwaysTrueTranslator implements ExprToSqlExprTranslator {
    public SqlExpression translate(Expression expr,
        ExpressionToSqlExprBuilder builder) {
      return new SqlAlwaysTrueExpression();
    }
  }

  // comparing

  static class EqualsTranslator implements ExprToSqlExprTranslator {
    // TODO check values to be null deeply
    public SqlExpression translate(Expression expr,
        ExpressionToSqlExprBuilder builder) {
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

  static class IsNullTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      return new SqlIsNullExpression(sqlChildren[0]);
    }
  }

  static class ComparedEqualsTranslator extends
      ComparingExprToSqlExprTranslator {
    protected SqlExpression translateComparable(Expression expr,
        SqlExpression sql1, SqlExpression sql2) {
      return new SqlEqualsExpression(sql1, sql2);
    }
  }

  static class GreaterThanTranslator extends ComparingExprToSqlExprTranslator {
    protected SqlExpression translateComparable(Expression expr,
        SqlExpression sql1, SqlExpression sql2) {
      boolean allowEqual = false;
      if (expr instanceof GreaterThanExpression) {
        allowEqual = ((GreaterThanExpression) expr).getAllowsEqual();
      }
      return new SqlGreaterThanExpression(sql1, sql2, allowEqual);
    }
  }

  static class LowerThanTranslator extends ComparingExprToSqlExprTranslator {
    protected SqlExpression translateComparable(Expression expr,
        SqlExpression sql1, SqlExpression sql2) {
      boolean allowEquals = false;
      if (expr instanceof LowerThanExpression) {
        allowEquals = ((LowerThanExpression) expr).getAllowsEqual();
      }
      return new SqlLowerThanExpression(sql1, sql2, allowEquals);
    }
  }

  // logical

  static class AndTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      SqlExpression temp = new SqlAndExpression().setChildren(sqlChildren);
      return sqlChildren.length > 1 ? new SqlBracketsExpression(temp) : temp;
    }
  }

  static class OrTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      SqlExpression temp = new SqlOrExpression().setChildren(sqlChildren);
      return sqlChildren.length > 1 ? new SqlBracketsExpression(temp) : temp;
    }
  }

  static class NotTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      return new SqlNotExpression(sqlChildren[0]);
    }
  }

  // String

  class LikeTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {

      LikeExpression like = (LikeExpression) expr;
      SqlExpression var = sqlChildren[0];
      String value = (String) convertValue(like.getMask());

      SqlExpression mask = new SqlValueExpression(
          SqlLikeUtil.convertMask(value, like.getConfiguration(), ESCAPE_CHAR));

      if (like.getIgnoreCase()) {
        var = new SqlUpperExpression(var);
        mask = new SqlUpperExpression(mask);
      }

      SqlExpression sqlLike = new SqlLikeExpression(var, mask);
      return new SqlEscapeExpression(sqlLike, ESCAPE_CHAR);
    }
  }

  /**
   * @since 1.1.3
   */
  class StartsWithTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      LikeExpression like = (LikeExpression) expr;
      SqlExpression var = sqlChildren[0];
      String value = (String) convertValue(like.getMask());
      SqlExpression mask = new SqlValueExpression(
          SqlLikeUtil.convertStartMask(value, like.getConfiguration(), ESCAPE_CHAR));

      if (like.getIgnoreCase()) {
        var = new SqlUpperExpression(var);
        mask = new SqlUpperExpression(mask);
      }

      SqlExpression sqlLike = new SqlLikeExpression(var, mask);
      return new SqlEscapeExpression(sqlLike, ESCAPE_CHAR);
    }
  }

  /**
   * @since 1.1.3
   */
  class EndsWithTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      LikeExpression like = (LikeExpression) expr;
      SqlExpression var = sqlChildren[0];
      String value = (String) convertValue(like.getMask());
      SqlExpression mask = new SqlValueExpression(
          SqlLikeUtil.convertEndMask(value, like.getConfiguration(), ESCAPE_CHAR));

      if (like.getIgnoreCase()) {
        var = new SqlUpperExpression(var);
        mask = new SqlUpperExpression(mask);
      }

      SqlExpression sqlLike = new SqlLikeExpression(var, mask);
      return new SqlEscapeExpression(sqlLike, ESCAPE_CHAR);
    }
  }

  static class ConcatenationTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      return new SqlConcatenationExpression().setChildren(sqlChildren);
    }
  }

  // procedure

  static class ProcedureTranslator extends CompositeExprToSqlExprTranslator {
    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {
      ProcedureExpression expr0 = (ProcedureExpression) expr;
      return new SqlProcedureExpression(expr0.getName()).setChildren(sqlChildren);
    }
  }

  /**
   * @since 1.1.4
   */
  static class InTranslator extends CompositeExprToSqlExprTranslator {

    protected SqlExpression translateParent(Expression expr,
        SqlExpression[] sqlChildren) {

      SqlExpression[] withoutFirst = (SqlExpression[])
          ArrayUtils.remove(sqlChildren, 0);

      if (withoutFirst.length == 0) {
        return new SqlAlwaysTrueExpression();
      }

      return new SqlInExpression(sqlChildren[0], withoutFirst);
    }

  }

}
