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

package org.araneaframework.uilib.widgets.lists.tests.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.Resolver;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.filter.column.SimpleColumnFilter;
import org.araneaframework.uilib.list.structure.filter.composite.AndFilter;
import org.araneaframework.uilib.list.util.converter.BooleanToStringConverter;


public class SimpleListFilterTest extends TestCase {
	private static final Logger log = Logger
			.getLogger(SimpleListFilterTest.class);

	public void testListFilterBuilder() throws ExpressionEvaluationException {
		// build filter
		ListFilter filter = new AndFilter().addFilter(
				new SimpleColumnFilter.Equals("name")).addFilter(
				new SimpleColumnFilter.GreaterThanOrEquals("name")).addFilter(
				new SimpleColumnFilter.Equals("licenseToKill"));

		// build expression
		Map data = new HashMap();
		data.put("name", "James Bond");
		data.put("age", new Long(25));
		data.put("licenseToKill", Boolean.TRUE);
		Expression expr = filter.buildExpression(data);

		// evaluate expression in memory
		Object value = expr.evaluate(new Resolver() {
			public Object resolve(String variableName) {
				if (variableName.equals("name")) {
					return "James Bond";
				}
				if (variableName.equals("age")) {
					return new Long(30);
				}
				if (variableName.equals("licenseToKill")) {
					return Boolean.TRUE;
				}
				return null;
			}
		});
		assertEquals(value, Boolean.TRUE);

		// evaluate expression in memory
		value = expr.evaluate(new Resolver() {
			public Object resolve(String variableName) {
				if (variableName.equals("name")) {
					return "Bond, James";
				}
				return null;
			}
		});
		assertEquals(value, Boolean.FALSE);

		// build sql expression
		StandardExpressionToSqlExprBuilder builder = new StandardExpressionToSqlExprBuilder();
		builder.setMapper(new Resolver() {
			public Object resolve(String variableName) {
				if ("name".equals(variableName)) {
					return "AGENT.NAME";
				}
				if ("age".equals(variableName)) {
					return "AGENT.AGE";
				}
				if ("licenseToKill".equals(variableName)) {
					return "AGENT.KILLER";
				}
				return null;
			}
		});
		builder.setConverter(new ValueConverter() {
			public Object convert(Value value) {
				if ("licenseToKill".equals(value.getName())) {
					return new BooleanToStringConverter("Y", "N")
							.convert(value.getValue());
				}
				return value.getValue();
			}
		});
		SqlExpression sqlExpr = builder.buildSqlExpression(expr);
		String sqlString = sqlExpr.toSqlString();
		Object[] values = sqlExpr.getValues();
		log.info("SQL string: " + sqlString);
		log.info("SQL values: " + Arrays.asList(values));
	}
}
