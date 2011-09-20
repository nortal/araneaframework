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
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.NotExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockValueExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockVariableResolver;

public class LogicalExpressionTests extends TestCase {

  private static final Log LOG = LogFactory.getLog(LogicalExpressionTests.class);

  private VariableResolver resolver;

  private Expression trueExpr;

  private Expression falseExpr;

  private Expression notBoolExpr;

  @Override
  public void setUp() {
    this.resolver = new MockVariableResolver();

    this.trueExpr = new MockValueExpression<Boolean>(true);
    this.falseExpr = new MockValueExpression<Boolean>(false);
    this.notBoolExpr = new MockValueExpression<Long>(0L);
  }

  @Override
  public void tearDown() {
    this.resolver = null;
    this.trueExpr = null;
    this.falseExpr = null;
    this.notBoolExpr = null;
  }

  public void testAndExpression() throws ExpressionEvaluationException {
    LOG.debug("Testing AndExpression");
    try {
      new AndExpression().evaluate(this.resolver);
      fail("AndExpression must throw an exception, because it has no child expressions");
    } catch (IllegalArgumentException e) {
      // normal
    }
    try {
      new AndExpression().add(this.trueExpr).evaluate(this.resolver);
    } catch (ExpressionEvaluationException e) {
      fail("AndExpression must pass with one child");
    }
    try {
      new AndExpression().add(this.notBoolExpr).add(this.trueExpr).add(this.trueExpr).evaluate(this.resolver);
      fail("AndExpression must throw an exception");
    } catch (Exception e) {
      LOG.info("Exception while evaluating resolver.", e);
    }

    assertEquals("AndExpression must return true", Boolean.TRUE,
        new AndExpression().add(this.trueExpr).add(this.trueExpr).evaluate(this.resolver));
    assertEquals("AndExpression must return false", Boolean.FALSE,
        new AndExpression().add(this.falseExpr).add(this.trueExpr).evaluate(this.resolver));
    assertEquals("AndExpression must return false", Boolean.FALSE,
        new AndExpression().add(this.trueExpr).add(this.falseExpr).evaluate(this.resolver));
    assertEquals("AndExpression must return false", Boolean.FALSE,
        new AndExpression().add(this.falseExpr).add(this.falseExpr).evaluate(this.resolver));
  }

  public void testOrExpression() throws ExpressionEvaluationException {
    LOG.debug("Testing OrExpression");
    try {
      new OrExpression().evaluate(this.resolver);
      fail("OrExpression must throw an exception");
    } catch (IllegalArgumentException e) {
      // normal
    }
    try {
      new OrExpression().add(this.trueExpr).evaluate(this.resolver);
    } catch (ExpressionEvaluationException e) {
      fail("OrExpression must pass with one child");
    }
    try {
      new OrExpression().add(this.notBoolExpr).add(this.trueExpr).add(this.trueExpr).evaluate(this.resolver);
      fail("OrExpression must throw an exception");
    } catch (Exception e) {
      LOG.info("Exception while evaluating resolver.", e);
    }

    assertEquals("OrExpression must return true", Boolean.TRUE, new OrExpression().add(this.trueExpr)
        .add(this.trueExpr).evaluate(this.resolver));
    assertEquals("OrExpression must return true", Boolean.TRUE,
        new OrExpression().add(this.falseExpr).add(this.trueExpr).evaluate(this.resolver));
    assertEquals("OrExpression must return true", Boolean.TRUE,
        new OrExpression().add(this.trueExpr).add(this.falseExpr).evaluate(this.resolver));
    assertEquals("OrExpression must return false", Boolean.FALSE,
        new OrExpression().add(this.falseExpr).add(this.falseExpr).evaluate(this.resolver));
  }

  public void testNotExpression() throws ExpressionEvaluationException {
    LOG.debug("Testing NotExpression");
    try {
      new NotExpression(null).evaluate(this.resolver);
      fail("NotExpression must throw an exception");
    } catch (IllegalArgumentException e) {
      // normal
    }
    try {
      new NotExpression(this.notBoolExpr).evaluate(this.resolver);
      fail("NotExpression must throw an exception");
    } catch (Exception e) {
      LOG.info("Exception while evaluating expression.", e);
    }

    assertEquals("NotExpression must return false", Boolean.FALSE,
        new NotExpression(this.trueExpr).evaluate(this.resolver));
    assertEquals("NotExpression must return true", Boolean.TRUE,
        new NotExpression(this.falseExpr).evaluate(this.resolver));
  }
}
