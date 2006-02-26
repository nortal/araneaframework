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

package org.araneaframework.backend.list.sqlexpr.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.araneaframework.backend.list.SqlExpression;


public abstract class SqlComparableExpression implements SqlExpression {
	protected SqlExpression expr1;

	protected SqlExpression expr2;

	public SqlComparableExpression(SqlExpression expr1, SqlExpression expr2) {
		if (expr1 == null || expr2 == null) {
			throw new RuntimeException("Both arguments must be provided");
		}
		this.expr1 = expr1;
		this.expr2 = expr2;
	}

	public String toSqlString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.expr1.toSqlString());
		sb.append(" ");
		sb.append(getOperator());
		sb.append(" ");
		sb.append(this.expr2.toSqlString());
		return sb.toString();
	}

	protected abstract String getOperator();

	public Object[] getValues() {
		List values = new ArrayList();
		Object[] childValues = this.expr1.getValues();
		if (childValues != null) {
			values.addAll(Arrays.asList(childValues));
		}
		childValues = this.expr2.getValues();
		if (childValues != null) {
			values.addAll(Arrays.asList(childValues));
		}
		return values.toArray();
	}
}
