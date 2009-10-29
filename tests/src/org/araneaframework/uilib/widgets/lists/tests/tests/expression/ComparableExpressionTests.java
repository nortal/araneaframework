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
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.IsNullExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockValueExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;


public class ComparableExpressionTests extends TestCase {
	private static final Log LOG = LogFactory.getLog(ComparableExpressionTests.class);

	private VariableResolver resolver;

	private Expression low;

	private Expression low_copy;

	private Expression high;

	private Expression nullExpr;

	@Override
  public void setUp() {
		this.resolver = new MockVariableResolver();
		this.low = new MockValueExpression(new Long(-10));
		this.low_copy = new MockValueExpression(new Long(-10));
		this.high = new MockValueExpression(new Long(10));
		this.nullExpr = new MockValueExpression(null);
	}

	@Override
  public void tearDown() {
		this.resolver = null;
		this.low = null;
		this.low_copy = null;
		this.high = null;
		this.nullExpr = null;
	}

	public void testEqualsExpression() throws ExpressionEvaluationException {
		LOG.debug("Testing EqualsExpression");
		try {
			new EqualsExpression(null, null).evaluate(this.resolver);
			fail("EqualsExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new EqualsExpression(this.low, null).evaluate(this.resolver);
			fail("EqualsExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new EqualsExpression(null, this.low).evaluate(this.resolver);
			fail("EqualsExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}

		// evaluating
		assertEquals("EqualsExpression must return true", Boolean.TRUE,
				new EqualsExpression(this.low, this.low_copy)
						.evaluate(this.resolver));
		assertEquals("EqualsExpression must return false", Boolean.FALSE,
				new EqualsExpression(this.low, this.high)
						.evaluate(this.resolver));
		assertEquals("EqualsExpression must return true", Boolean.TRUE,
				new EqualsExpression(this.high, this.high)
						.evaluate(this.resolver));
	}

	public void testComparedEqualsExpression()
			throws ExpressionEvaluationException {
		LOG.debug("Testing ComparedEqualsExpression");
		try {
			new ComparedEqualsExpression(null, null).evaluate(this.resolver);
			fail("ComparedEqualsExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new ComparedEqualsExpression(this.low, null)
					.evaluate(this.resolver);
			fail("ComparedEqualsExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new ComparedEqualsExpression(null, this.low)
					.evaluate(this.resolver);
			fail("ComparedEqualsExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}

		// evaluating
		assertEquals("ComparedEqualsExpression must return true", Boolean.TRUE,
				new ComparedEqualsExpression(this.low, this.low_copy)
						.evaluate(this.resolver));
		assertEquals("ComparedEqualsExpression must return false",
				Boolean.FALSE,
				new ComparedEqualsExpression(this.low, this.high)
						.evaluate(this.resolver));
		assertEquals("ComparedEqualsExpression must return true", Boolean.TRUE,
				new ComparedEqualsExpression(this.high, this.high)
						.evaluate(this.resolver));
	}

	public void testGreaterThanExpression()
			throws ExpressionEvaluationException {
		LOG.debug("Testing GreaterThanExpression");
		try {
			new GreaterThanExpression(null, null).evaluate(this.resolver);
			fail("GreaterThanExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new GreaterThanExpression(this.low, null).evaluate(this.resolver);
			fail("GreaterThanExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new GreaterThanExpression(null, this.low).evaluate(this.resolver);
			fail("GreaterThanExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}

		// evaluating
		assertEquals("GreaterThanExpression must return true", Boolean.TRUE,
				new GreaterThanExpression(this.high, this.low)
						.evaluate(this.resolver));
		assertEquals("GreaterThanExpression must return false", Boolean.FALSE,
				new GreaterThanExpression(this.low, this.high)
						.evaluate(this.resolver));
		assertEquals("GreaterThanExpression must return false", Boolean.FALSE,
				new GreaterThanExpression(this.high, this.high)
						.evaluate(this.resolver));
	}

	public void testLowerThanExpression() throws ExpressionEvaluationException {
		LOG.debug("Testing LowerThanExpression");
		try {
			new LowerThanExpression(null, null).evaluate(this.resolver);
			fail("LowerThanExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new LowerThanExpression(this.low, null).evaluate(this.resolver);
			fail("LowerThanExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new LowerThanExpression(null, this.low).evaluate(this.resolver);
			fail("LowerThanExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}

		// evaluating
		assertEquals("LowerThanExpression must return true", Boolean.TRUE,
				new LowerThanExpression(this.low, this.high)
						.evaluate(this.resolver));
		assertEquals("LowerThanExpression must return false", Boolean.FALSE,
				new LowerThanExpression(this.high, this.low)
						.evaluate(this.resolver));
		assertEquals("LowerThanExpression must return false", Boolean.FALSE,
				new LowerThanExpression(this.high, this.high)
						.evaluate(this.resolver));
	}

	public void testIsNullExpression() throws ExpressionEvaluationException {
		LOG.debug("Testing IsNullExpression");
		try {
			new IsNullExpression(null).evaluate(this.resolver);
			fail("IsNullExpression operand can not be null");
		} catch (Exception e) {
			// normal
		}

		// evaluating
		assertEquals("IsNullExpression must return true", Boolean.TRUE,
				new IsNullExpression(this.nullExpr).evaluate(this.resolver));
		assertEquals("IsNullExpression must return false", Boolean.FALSE,
				new IsNullExpression(this.high).evaluate(this.resolver));
	}
}
