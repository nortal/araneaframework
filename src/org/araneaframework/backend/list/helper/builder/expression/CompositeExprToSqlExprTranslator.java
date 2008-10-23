/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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
import org.araneaframework.backend.list.helper.builder.ExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;

public abstract class CompositeExprToSqlExprTranslator
  implements ExprToSqlExprTranslator {

  public final SqlExpression translate(Expression expr,
      ExpressionToSqlExprBuilder builder) {

    Expression[] children = ((CompositeExpression) expr).getChildren();
    SqlExpression[] sqlChildren = new SqlExpression[children.length];

    for (int i = 0; i < children.length; i++) {
      sqlChildren[i] = builder.buildSqlExpression(children[i]);
    }

    return translateParent(expr, sqlChildren);
  }

  protected abstract SqlExpression translateParent(Expression expr,
      SqlExpression[] sqlChildren);
}
