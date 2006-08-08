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

package org.araneaframework.backend.list.sqlexpr;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlLikeExpression;

/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see SqlLikeExpression
 */
public class SqlEscapeExpression implements SqlExpression {
	private SqlExpression expr;
	private String escapeChar;
	public SqlEscapeExpression(SqlExpression expr, String escapeChar) {
		if (expr == null) {
			throw new IllegalArgumentException("SqlExpression must be provided");
		}
		if (escapeChar == null) {
			throw new IllegalArgumentException("Escape character must be provided");
		}
		this.expr = expr;
		this.escapeChar = escapeChar;
	}
	public String toSqlString() {
		return new StringBuffer(this.expr.toSqlString()).append(" ESCAPE '").append(escapeChar).append("'").toString(); 		
	}
	public Object[] getValues() {
		return this.expr.getValues();
	}
}
