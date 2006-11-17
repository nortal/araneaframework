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
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockValueExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;


public class LikeExpressionTests extends TestCase {
	private static final Logger log = Logger
			.getLogger(LikeExpressionTests.class);

	private VariableResolver resolver;

	private Expression tom;
	private Expression jerry;
	
	private Value om;
	private Value tm;
	
	private LikeConfiguration config;

	public void setUp() {
		this.resolver = new MockVariableResolver();
		
		this.tom = new MockValueExpression("Tom");
		this.jerry = new MockValueExpression("Jerry");
		this.om = new MockValueExpression("om");
		this.tm = new MockValueExpression("t.m");
		
		this.config = new LikeConfiguration();
	}

	public void tearDown() {
		this.resolver = null;
		this.tom = null;
		this.jerry = null;
		this.om = null;
		this.tm = null;
		this.config = null;
	}

	public void testLikeExpression() throws ExpressionEvaluationException {
		log.debug("Testing LikeExpression");
		boolean ignoreCase = false;
		try {
			new LikeExpression(null, null, ignoreCase, config).evaluate(this.resolver);
			fail("LikeExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new LikeExpression(this.tom, null, ignoreCase, config)
					.evaluate(this.resolver);
			fail("LikeExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}
		try {
			new LikeExpression(null, this.om, ignoreCase, config)
					.evaluate(this.resolver);
			fail("LikeExpression operands can not be nulls");
		} catch (Exception e) {
			// normal
		}

		// evaluating
		assertEquals("LikeExpression must return true", Boolean.TRUE,
				new LikeExpression(this.tom, this.om, false, config)
						.evaluate(this.resolver));
		assertEquals("LikeExpression must return true", Boolean.TRUE,
				new LikeExpression(this.tom, this.tm, true, config)
						.evaluate(this.resolver));
		assertEquals("LikeExpression must return false", Boolean.FALSE,
				new LikeExpression(this.tom, this.tm, false, config)
						.evaluate(this.resolver));
		assertEquals("LikeExpression must return false", Boolean.FALSE,
				new LikeExpression(this.jerry, this.om, false, config)
						.evaluate(this.resolver));
	}
}
