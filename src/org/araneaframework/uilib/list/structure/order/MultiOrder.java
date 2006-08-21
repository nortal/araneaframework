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

package org.araneaframework.uilib.list.structure.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.MultiComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.ReverseComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListContext;
import org.araneaframework.uilib.list.structure.ListOrder;


/**
 * Composite ListOrder.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class MultiOrder implements ListOrder {

	protected Map orders = new HashMap();
	
	public void addOrder(String fieldId, ListOrder order) {
		orders.put(fieldId, order);
	}
	
	public void init(ListContext context) {
		Validate.notEmpty(orders, "No orders specified");
	}

	public ComparatorExpression buildExpression(OrderInfo orderInfo) {
		Validate.notNull(orderInfo, "No ordering info specified");
		if (orderInfo.getFields().size() == 0) {
			return null;
		}
		
		MultiComparatorExpression multiExpr = new MultiComparatorExpression();
		for (Iterator i = orderInfo.getFields().entrySet().iterator(); i.hasNext(); ) {
			Entry entry = (Entry) i.next();			
			String fieldId = (String) entry.getKey();
			ListOrder order = (ListOrder) orders.get(fieldId);
			Validate.notNull(order, "No order '" + fieldId + "' found");
			multiExpr.add(reverse(order.buildExpression(orderInfo),
					OrderInfo.DESCENDING.equals(entry.getValue())));
		}
		return multiExpr;
	}
	
	protected ComparatorExpression reverse(ComparatorExpression expr,
			boolean reverse) {
		return reverse ? new ReverseComparatorExpression(expr) : expr;
	}
}
