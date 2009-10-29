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

package org.araneaframework.uilib.list.util;

import java.util.Comparator;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.ComparatorExpressionFactory;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionFactory;
import org.araneaframework.backend.list.memorybased.compexpr.LazyComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.NopComparatorExpression;
import org.araneaframework.backend.list.memorybased.expression.AlwaysTrueExpression;
import org.araneaframework.backend.list.memorybased.expression.LazyExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.reader.MapFormReader;
import org.araneaframework.uilib.form.reader.MapFormWriter;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.ListOrder;
import org.araneaframework.uilib.list.structure.order.MultiFieldOrder;
import org.araneaframework.uilib.list.structure.order.SimpleFieldOrder;
import org.araneaframework.uilib.list.util.comparator.NullComparator;
import org.araneaframework.uilib.list.util.comparator.StringComparator;

/**
 * Utility methods for Aranea Lists.
 * <p>
 * There are following methods:
 * <ul>
 * <li><b>Filter form</b> to <b>filter info</b> transformation and vice-versa</li>
 * <li>Creating lazy expressions from list filter, order and corresponding info</li>
 * <li>Reading ListOrder</li>
 * </ul>
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public abstract class ListUtil {

  /**
   * Retrieves filter info corresponding to the <code>form</code> specified.
   */
  public static Map<String, Object> readFilterInfo(FormWidget form) {
    MapFormReader mapFormReader = new MapFormReader(form);
    Map<String, Object> hierarchyMap = mapFormReader.getMap();
    return MapUtil.convertToPlainMap(hierarchyMap);
  }

  /**
   * Applies filter info into the <code>form</code> specified.
   */
  public static void writeFilterInfo(FormWidget form, Map<String, Object> info) {
    Map<String, Object> hierarchyMap = MapUtil.convertToHierachyMap(info);
    MapFormWriter mapFormWriter = new MapFormWriter();
    mapFormWriter.writeForm(form, hierarchyMap);
  }

  /**
   * Creates a lazy-initialized {@link Expression} corresponding to the static {@link ListFilter} and request-dependent
   * <code>info</code>.
   * <p>
   * Expression is actually created when {@link Expression#evaluate(VariableResolver)} is first invoked.
   */
  public static Expression toExpression(final ListFilter filter, final Map<String, Object> info) {
    Expression result = null;
    if (filter != null) {
      ExpressionFactory factory = new ExpressionFactory() {

        public Expression createExpression() {
          // Build the expression
          Expression expr = filter.buildExpression(info);
          if (expr == null) {
            expr = new AlwaysTrueExpression();
          }
          return expr;
        }
      };
      result = new LazyExpression<Object>(factory);
    }
    return result;
  }

  /**
   * Creates a lazy-initialized {@link ComparatorExpression} corresponding to the static {@link ListOrder} and
   * request-dependent <code>info</code>.
   * <p>
   * ComparatorExpression is actually created when
   * {@link ComparatorExpression#compare(VariableResolver, VariableResolver)} is first invoked.
   */
  public static ComparatorExpression toComparatorExpression(final ListOrder order, final OrderInfo info) {
    ComparatorExpression result = null;
    if (order != null) {
      ComparatorExpressionFactory factory = new ComparatorExpressionFactory() {

        public ComparatorExpression createComparatorExpression() {
          // Build the expression
          ComparatorExpression expr = order.buildComparatorExpression(info);
          if (expr == null) {
            return new NopComparatorExpression();
          }
          return expr;
        }
      };
      result = new LazyComparatorExpression(factory);
    }
    return result;
  }

  /**
   * Returns static ordering info about specified field <code>id</code>.
   */
  public static SimpleFieldOrder getFieldOrder(ListOrder listOrder, String id) {
    if (listOrder instanceof SimpleFieldOrder) {
      // Whole ordering is about one field
      SimpleFieldOrder fieldOrder = (SimpleFieldOrder) listOrder;
      return id.equals(fieldOrder.getFieldId()) ? fieldOrder : null;
    } else if (listOrder instanceof MultiFieldOrder) {
      // There are multiple fields that can be ordered
      MultiFieldOrder multiOrder = (MultiFieldOrder) listOrder;
      return (SimpleFieldOrder) multiOrder.getFieldOrder(id);
    }
    return null; // not found
  }

  /**
   * Returns <code>true</code> if the corresponding <code>fieldOrder</code> is comparing {@link String} objects ignoring
   * their case.
   */
  public static boolean isIgnoreCase(SimpleFieldOrder fieldOrder) {
    Comparator<?> comparator = fieldOrder.getComparator();

    // Unwrap NullComparator
    if (comparator instanceof NullComparator) {
      comparator = ((NullComparator) comparator).getNotNullComparator();
    }

    if (comparator instanceof StringComparator) {
      return ((StringComparator) comparator).getIgnoreCase();
    }
    return false; // no String -> no case
  }

}
