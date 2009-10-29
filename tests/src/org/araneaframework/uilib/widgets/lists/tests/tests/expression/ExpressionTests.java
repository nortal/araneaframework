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

package org.araneaframework.uilib.widgets.lists.tests.tests.expression;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.procedure.ProcedureExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;


public class ExpressionTests extends TestCase {
	private static final Log LOG = LogFactory.getLog(ExpressionTests.class);

	private VariableResolver resolver;

	@Override
  public void setUp() {
		this.resolver = new MockVariableResolver();
	}

	@Override
  public void tearDown() {
		this.resolver = null;
	}

	public void testValueExpression() {
		LOG.debug("Testing ValueExpression");
		// evaluating
		assertNull("ValueExpression must return null",
				new ValueExpression(null).evaluate(this.resolver));
		assertEquals("ValueExpression must return true", Boolean.TRUE,
				new ValueExpression(Boolean.TRUE).evaluate(this.resolver));
		assertEquals("ValueExpression must return 0", new Long(0),
				new ValueExpression(new Long(0)).evaluate(this.resolver));

		// name
		assertEquals("ValueExpression's name must be 'value1'", "value1",
				new ValueExpression("value1", null).getName());
	}

	public void testVariableExpression() {
		LOG.debug("Testing VariableExpression");
		// name
		try {
			new VariableExpression(null).getName();
			fail("VariableExpression's name can't be null");
		} catch (Exception e) {
			// normal
		}
		assertEquals("VariableExpression's name must be 'var1'", "var1",
				new VariableExpression("var1").getName());

		// evaluating
		assertEquals("VariableExpression must return Tom", "Tom",
				new VariableExpression("var").evaluate(new MockVariableResolver("var", "Tom")));
	}
	
	public void testProcedureExpression() {
		LOG.debug("Testing ProcedureExpression");
		// name
		try {
			new ProcedureExpression(null).getName();
			fail("ProcedureExpression's name can't be null");
		} catch (Exception e) {
			// normal
		}
		assertEquals("ProcedureExpression's name must be 'proc1'", "proc1",
				new ProcedureExpression("proc1").getName());		
		
		// evaluating
		try {
			new ProcedureExpression("proc1").evaluate(this.resolver);
			fail("ProcedureExpression should not be evaluatable");
		} catch (Exception e) {
			// normal
		}
	}
}
