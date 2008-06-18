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
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.StringExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlUpperExpression;

public abstract class ComparingExprToSqlExprTranslator extends
		CompositeExprToSqlExprTranslator {
	
	@Override
  protected final SqlExpression translateParent(Expression expr, SqlExpression[] sqlChildren) {
		if (((StringExpression) expr).getIgnoreCase()) {
			return translateComparable(expr,
					new SqlUpperExpression(sqlChildren[0]),
					new SqlUpperExpression(sqlChildren[1]));
		}
		return translateComparable(expr, sqlChildren[0], sqlChildren[1]);

	}
	protected abstract SqlExpression translateComparable(Expression expr, SqlExpression sql1, SqlExpression sql2);
}
