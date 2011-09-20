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

import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockExpression;

public class ExpressionUtilTests extends TestCase {

  private static final Log LOG = LogFactory.getLog(ExpressionUtilTests.class);

  public static final String EMPTY_STRING = "";

  public static final String NOT_EMPTY_STRING = "string";

  private Expression expr;

  @Override
  public void setUp() {
    this.expr = new MockExpression();
  }

  @Override
  public void tearDown() {
    this.expr = null;
  }

  public void testVarValue() {
    LOG.debug("Testing creating of VALUE expressions");

    // Variable expression
    try {
      ExpressionUtil.var(null);
      fail("Variable name must be provided");
    } catch (IllegalArgumentException e) {
      // ignore
    }
    try {
      ExpressionUtil.var(EMPTY_STRING);
      fail("Variable name must be provided");
    } catch (IllegalArgumentException e) {
      // ignore
    }
    assertNotNull(ExpressionUtil.var(NOT_EMPTY_STRING));

    // Value expression
    assertNull(ExpressionUtil.value(null));
    assertNotNull(ExpressionUtil.value(Boolean.TRUE));

    // Nullable value expression
    assertNotNull(ExpressionUtil.nullValue());
  }

  public void testEq() {
    LOG.debug("Testing creating of EQUALS expressions");

    // Equals expression
    assertNull(ExpressionUtil.eq(null, null, null));
    assertNull(ExpressionUtil.eq(this.expr, null, null));
    assertNull(ExpressionUtil.eq(null, this.expr, null));
    assertNotNull(ExpressionUtil.eq(this.expr, this.expr, null));
  }

  public void testAndOr() {
    LOG.debug("Testing creating of AND and OR expressions");

    // Testing with empty array
    Expression[] emptyArray = new Expression[0];
    assertNull(ExpressionUtil.and(emptyArray));
    assertNull(ExpressionUtil.or(emptyArray));

    // Testing with empty collection
    List<Expression> emptyCollection = Collections.emptyList();
    assertNull(ExpressionUtil.and(emptyCollection));
    assertNull(ExpressionUtil.or(emptyCollection));

    // Testing with collection with 1 element
    Expression expr = new MockExpression();
    Expression[] collection = new Expression[] { expr };

    Expression and = ExpressionUtil.and(collection);
    assertNotNull(and);
    assertTrue(and instanceof AndExpression);
    assertTrue(((AndExpression) and).getChildren().length == 1);

    Expression or = ExpressionUtil.or(collection);
    assertNotNull(or);
    assertTrue(or instanceof OrExpression);
    assertTrue(((OrExpression) or).getChildren().length == 1);
  }
}
