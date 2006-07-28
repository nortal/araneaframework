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

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlValueExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockValueConverter;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;
import org.araneaframework.uilib.widgets.lists.tests.tests.expression.ExpressionTests;


public class StandardExprToSqlExprBuilderTests extends TestCase {
	private static final Logger log = Logger.getLogger(ExpressionTests.class);
	
	private StandardExpressionToSqlExprBuilder builder;
	
	private Object testValue = "test_value";

	public void setUp() {
		this.builder = new StandardExpressionToSqlExprBuilder();
		this.builder.setMapper(new MockVariableResolver());
		this.builder.setConverter(new MockValueConverter());
	}

	public void tearDown() {
		this.builder = null;
	}
	
	public void testValueTranslator() {
		log.debug("Testing ValueTranslator");
		Expression expr = null;
		SqlExpression tmp = null;
		try {
			expr = new ValueExpression(this.testValue);			
		}
		catch (Exception e) {
			fail("Constructing ValueExpression failed");
		}
		try {
			tmp = this.builder.buildSqlExpression(expr);			
		}
		catch (Exception e) {
			fail("Building SqlExpression failed");
		}
		
		SqlValueExpression sql = null;
		try {
			sql = (SqlValueExpression) tmp;
		}
		catch (Exception e) {
			fail("Casting into SqlValueExpression failed");
		}
	}

}
