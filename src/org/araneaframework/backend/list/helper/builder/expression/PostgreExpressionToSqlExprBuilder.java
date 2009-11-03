/*
 * Copyright 2008 Webmedia Group Ltd.
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

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.SqlLikeUtil;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlLikeExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlValueExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlUpperExpression;

/**
 * SQL expression builder class that is suitable to use with PostgreSQL
 * databases. While other translators work with PostgresSQL, it replaces the
 * LikeExpression
 * 
 * @author Roman Tekhov
 * @since 1.1.3
 */
public class PostgreExpressionToSqlExprBuilder extends StandardExpressionToSqlExprBuilder {

  public PostgreExpressionToSqlExprBuilder() {
    addTranslator(LikeExpression.class, new PostgreLikeTranslator());
  }

  protected class PostgreLikeTranslator extends CompositeExprToSqlExprTranslator {

    @Override
    protected SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {

      LikeExpression like = (LikeExpression) expr;
      SqlExpression var = sqlChildren[0];
      String value = (String) convertValue(like.getMask());
      SqlExpression mask = new SqlValueExpression(SqlLikeUtil.convertMask(value, like.getConfiguration(), ESCAPE_CHAR));

      if (like.getIgnoreCase()) {
        var = new SqlUpperExpression(var);
        mask = new SqlUpperExpression(mask);
      }

      return new SqlLikeExpression(var, mask);
    }
  }
}
