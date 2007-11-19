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
package org.araneaframework.uilib.list.util;

import java.util.Map;

import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.ComparatorExpressionFactory;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionFactory;
import org.araneaframework.backend.list.memorybased.compexpr.LazyComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.NopComparatorExpression;
import org.araneaframework.backend.list.memorybased.expression.AlwaysTrueExpression;
import org.araneaframework.backend.list.memorybased.expression.LazyExpression;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.reader.MapFormReader;
import org.araneaframework.uilib.form.reader.MapFormWriter;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.ListOrder;


/**
 * Methods for filter form and
 * creating lazy expressions from list filter, order and corresponding info.
 * 
 * @author Rein Raudjärv
 */
public class ListUtil {
	
	public static Map readFilterInfo(FormWidget form) {
		MapFormReader mapFormReader = new MapFormReader(form);
		Map hierarchyMap = mapFormReader.getMap();
		return MapUtil.convertToPlainMap(hierarchyMap);
	}
	
	public static void writeFilterInfo(FormWidget form, Map info) {
		Map hierarchyMap = MapUtil.convertToHierachyMap(info);
		MapFormWriter mapFormWriter = new MapFormWriter();
		mapFormWriter.writeForm(form, hierarchyMap);
	}

	public static Expression toExpression(final ListFilter filter, final Map info) {
		Expression result = null;
		if (filter != null) {
			ExpressionFactory factory = new ExpressionFactory() {
				private static final long serialVersionUID = 1L;
				public Expression createExpression() {
					Expression expr = filter.buildExpression(info);
					if (expr == null) {
						expr = new AlwaysTrueExpression();
					}
					return expr;
				}
			};
			result = new LazyExpression(factory);
		}
		return result;
	}

	public static ComparatorExpression toComparatorExpression(final ListOrder order, final OrderInfo info) {
		ComparatorExpression result = null;
		if (order != null) {
			ComparatorExpressionFactory factory = new ComparatorExpressionFactory() {
				private static final long serialVersionUID = 1L;
				public ComparatorExpression createComparatorExpression() {
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
	
}
