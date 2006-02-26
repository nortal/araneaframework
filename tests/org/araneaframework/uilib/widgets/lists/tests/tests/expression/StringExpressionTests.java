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

package org.araneaframework.uilib.widgets.lists.tests.tests.expression;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.Expression;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.ExpressionEvaluationException;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.expression.VariableResolver;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.expression.string.ConcatenationExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockValueExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;


public class StringExpressionTests extends TestCase {
	private static final Logger log = Logger
			.getLogger(StringExpressionTests.class);

	private VariableResolver resolver;

	private Expression part1expr;
	private Expression part2expr;
	private Expression part3expr;

	public void setUp() {
		this.resolver = new MockVariableResolver();
		
		this.part1expr = new MockValueExpression("part1");
		this.part2expr = new MockValueExpression("part2");
		this.part3expr = new MockValueExpression("part3");
	}

	public void tearDown() {
		this.resolver = null;
		this.part1expr = null;
		this.part2expr = null;
		this.part3expr = null;
	}

	public void testConcatenationExpression()
			throws ExpressionEvaluationException {
		log.debug("Testing ConcatenationExpression");
		try {
			new ConcatenationExpression().evaluate(this.resolver);
			fail("ConcatenationExpression must throw an exception");
		} catch (Exception e) {
			// normal
		}
		try {
			new ConcatenationExpression().add(this.part1expr).evaluate(
					this.resolver);
		} catch (Exception e) {
			fail("ConcatenationExpression must pass with one child");
		}

		assertEquals("ConcatenationExpression must return part1part2",
				"part1part2", new ConcatenationExpression().add(this.part1expr)
						.add(this.part2expr).evaluate(this.resolver));
		assertEquals("ConcatenationExpression must return part2part1",
				"part2part1", new ConcatenationExpression().add(this.part2expr)
						.add(this.part1expr).evaluate(this.resolver));
		assertEquals("ConcatenationExpression must return part1part2part3",
				"part1part2part3", new ConcatenationExpression().add(
						this.part1expr).add(this.part2expr).add(this.part3expr)
						.evaluate(this.resolver));
	}
}
