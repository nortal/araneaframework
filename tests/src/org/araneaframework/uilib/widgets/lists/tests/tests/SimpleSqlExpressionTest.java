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

package org.araneaframework.uilib.widgets.lists.tests.tests;

import java.util.Arrays;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.util.ComparatorFactory;
import org.araneaframework.uilib.list.util.converter.BooleanToStringConverter;

public class SimpleSqlExpressionTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(SimpleSqlExpressionTest.class);

  public void testSqlExpressionBuilder() throws ExpressionEvaluationException {
    // build expression
    Expression expr = new AndExpression().add(
        new ComparedEqualsExpression(new VariableExpression("name"), new ValueExpression<String>("James Bond"),
            ComparatorFactory.getStringComparator(false, true, null))).add(
        new GreaterThanExpression(new VariableExpression("age"), new ValueExpression<Long>(25L))).add(
        new EqualsExpression(new VariableExpression("licenseToKill"), new ValueExpression<Boolean>(true)));

    // evaluate expression in memory
    Object value = expr.evaluate(new VariableResolver() {

      public Object resolve(Variable variable) {
        if (variable.getName().equals("name")) {
          return "James Bond";
        } else if (variable.getName().equals("age")) {
          return 30L;
        } else if (variable.getName().equals("licenseToKill")) {
          return true;
        }
        return null;
      }
    });
    assertEquals(value, Boolean.TRUE);

    // evaluate expression in memory
    value = expr.evaluate(new VariableResolver() {

      public Object resolve(Variable variable) {
        if (variable.getName().equals("name")) {
          return "Bond, James";
        }
        return null;
      }
    });
    assertEquals(value, Boolean.FALSE);

    // build SQL expression
    StandardExpressionToSqlExprBuilder builder = new StandardExpressionToSqlExprBuilder();
    builder.setMapper(new VariableResolver() {

      public Object resolve(Variable variable) {
        if ("name".equals(variable.getName())) {
          return "AGENT.NAME";
        } else if ("age".equals(variable.getName())) {
          return "AGENT.AGE";
        } else if ("licenseToKill".equals(variable.getName())) {
          return "AGENT.KILLER";
        }
        return null;
      }
    });

    builder.setConverter(new ValueConverter() {

      @SuppressWarnings("unchecked")
      public <S, D> D convert(Value<S> value) {
        if ("licenseToKill".equals(value.getName())) {
          return (D) BooleanToStringConverter.YN_UPPER_CONVERTER.convert((Boolean) value.getValue());
        }
        return (D) value.getValue();
      }
    });

    SqlExpression sqlExpr = builder.buildSqlExpression(expr);
    String sqlString = sqlExpr.toSqlString();
    Object[] values = sqlExpr.getValues();

    LOG.info("SQL string: " + sqlString);
    LOG.info("SQL values: " + Arrays.asList(values));
  }
}
