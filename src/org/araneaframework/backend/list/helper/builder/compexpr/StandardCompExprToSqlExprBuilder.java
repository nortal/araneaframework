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

package org.araneaframework.backend.list.helper.builder.compexpr;

import java.util.Comparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.CompExprToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.compexpr.LazyComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.MultiComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.NopComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.ReverseComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.VariableComparatorExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;
import org.araneaframework.backend.list.sqlexpr.order.SqlAscendingExpression;
import org.araneaframework.backend.list.sqlexpr.order.SqlDescendingExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlUpperExpression;
import org.araneaframework.uilib.list.util.comparator.NullComparator;
import org.araneaframework.uilib.list.util.comparator.StringComparator;

public class StandardCompExprToSqlExprBuilder extends BaseCompExprToSqlExprBuilder {

  static final Log LOG = LogFactory.getLog(StandardCompExprToSqlExprBuilder.class);

  protected VariableResolver mapper;

  public StandardCompExprToSqlExprBuilder() {
    addTranslator(NopComparatorExpression.class, new NopComparatorTranslator());
    addTranslator(LazyComparatorExpression.class, new LazyComparatorTranslator());
    addTranslator(MultiComparatorExpression.class, new MultiComparatorTranslator());
    addTranslator(VariableComparatorExpression.class, new VariableComparatorTranslator());
    addTranslator(ReverseComparatorExpression.class, new ReverseComparatorTranslator());
  }

  public void setMapper(VariableResolver mapper) {
    this.mapper = mapper;
  }

  protected String resolveVariable(Variable variable) {
    return this.mapper != null ? (String) this.mapper.resolve(variable) : variable.getName();
  }

  // NopComparatorExpression must be the root
  static class NopComparatorTranslator implements CompExprToSqlExprTranslator {

    public SqlExpression translate(ComparatorExpression expr, CompExprToSqlExprBuilder builder) {
      return null;
    }
  }

  class LazyComparatorTranslator implements CompExprToSqlExprTranslator {

    public SqlExpression translate(ComparatorExpression expr, CompExprToSqlExprBuilder builder) {
      LazyComparatorExpression lazyExpr = (LazyComparatorExpression) expr;
      return buildSqlExpression(lazyExpr.getComparatorExpression());
    }

  }

  static class MultiComparatorTranslator extends CompositeCompExprToSqlExprTranslator {

    @Override
    protected SqlExpression translateParent(ComparatorExpression expr, SqlExpression[] sqlChildren) {
      return new SqlCollectionExpression().setChildren(sqlChildren);
    }

  }

  class VariableComparatorTranslator implements CompExprToSqlExprTranslator {

    public SqlExpression translate(ComparatorExpression expr, CompExprToSqlExprBuilder builder) {
      LOG.debug("Translating VariableComparatorExpression");
      VariableComparatorExpression compExpr = (VariableComparatorExpression) expr;
      return new SqlAscendingExpression(translateVariableComparatorInternal(compExpr));
    }

  }

  class ReverseComparatorTranslator implements CompExprToSqlExprTranslator {

    public SqlExpression translate(ComparatorExpression expr, CompExprToSqlExprBuilder builder) {

      LOG.debug("Translating ReverseComparatorExpression");
      ReverseComparatorExpression parent = (ReverseComparatorExpression) expr;
      VariableComparatorExpression compExpr = (VariableComparatorExpression) parent.getChildren()[0];

      return new SqlDescendingExpression(translateVariableComparatorInternal(compExpr));
    }
  }

  private SqlExpression translateVariableComparatorInternal(VariableComparatorExpression compExpr) {
    SqlExpression temp = new SqlStringExpression(resolveVariable(compExpr));
    Comparator<?> comparator = compExpr.getComparator();

    if (comparator instanceof NullComparator) {
      comparator = ((NullComparator) comparator).getNotNullComparator();
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Comparator: " + comparator);
    }

    if (StringComparator.class.isAssignableFrom(comparator.getClass())) {
      if (((StringComparator) comparator).getIgnoreCase()) {
        temp = new SqlUpperExpression(temp);
      }
    }

    return temp;
  }

}
