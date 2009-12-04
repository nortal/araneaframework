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
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockValueExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;

public class LikeExpressionTests extends TestCase {

  private static final Log LOG = LogFactory.getLog(LikeExpressionTests.class);

  private VariableResolver resolver;

  private Expression tom;

  private Expression jerry;

  private Value<String> om;

  private Value<String> to;

  private LikeConfiguration config;

  @Override
  public void setUp() {
    this.resolver = new MockVariableResolver();

    this.tom = new MockValueExpression<String>("Tom");
    this.jerry = new MockValueExpression<String>("Jerry");
    this.om = new MockValueExpression<String>("om");
    this.to = new MockValueExpression<String>("to");

    this.config = new LikeConfiguration();
  }

  @Override
  public void tearDown() {
    this.resolver = null;
    this.tom = null;
    this.jerry = null;
    this.om = null;
    this.to = null;
    this.config = null;
  }

  public void testLikeExpression() throws ExpressionEvaluationException {
    LOG.debug("Testing LikeExpression");
    boolean ignoreCase = false;
    try {
      new LikeExpression(null, null, ignoreCase, this.config).evaluate(this.resolver);
      fail("LikeExpression operands can not be nulls");
    } catch (Exception e) {
      // normal
    }
    try {
      new LikeExpression(this.tom, null, ignoreCase, this.config).evaluate(this.resolver);
      fail("LikeExpression operands can not be nulls");
    } catch (Exception e) {
      // normal
    }
    try {
      new LikeExpression(null, this.om, ignoreCase, this.config).evaluate(this.resolver);
      fail("LikeExpression operands can not be nulls");
    } catch (Exception e) {
      // normal
    }

    // evaluating
    assertEquals("LikeExpression must return true", Boolean.TRUE, new LikeExpression(this.tom, this.om, false,
        this.config).evaluate(this.resolver));
    assertEquals("LikeExpression must return true", Boolean.TRUE, new LikeExpression(this.tom, this.to, true,
        this.config).evaluate(this.resolver));
    assertEquals("LikeExpression must return false", Boolean.FALSE, new LikeExpression(this.tom, this.to, false,
        this.config).evaluate(this.resolver));
    assertEquals("LikeExpression must return false", Boolean.FALSE, new LikeExpression(this.jerry, this.om, false,
        this.config).evaluate(this.resolver));
  }
}
