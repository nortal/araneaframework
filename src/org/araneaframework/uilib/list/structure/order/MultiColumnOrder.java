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
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.MultiComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.ReverseComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.OrderInfoField;
import org.araneaframework.uilib.list.structure.ListOrder;


public class MultiColumnOrder implements ListOrder {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(MultiColumnOrder.class);
	
	protected Map columnOrders = new HashMap();

	public void addColumnOrder(FieldOrder order) {
		this.columnOrders.put(order.getFieldId(), order);
	}

	public FieldOrder getColumnOrder(String column) {
		return (FieldOrder) this.columnOrders.get(column);
	}

	public boolean isColumnOrdered(String column) {
		return getColumnOrder(column) != null;
	}

	public void clearColumnOrders() {
		this.columnOrders = new HashMap();
	}

	public ComparatorExpression buildComparatorExpression(OrderInfo orderInfo) {
		log.debug("Building ComparatorExpression, orderInfo = " + orderInfo.toString());
		if (orderInfo.getFields().size() == 0) {
			log.debug("No filterInfoFields specified, returning null");
			return null;
		}
		
		MultiComparatorExpression multiExpr = new MultiComparatorExpression();

		Iterator i = orderInfo.getFields().iterator();
		while (i.hasNext()) {
			OrderInfoField orderInfoField = (OrderInfoField) i.next();
			FieldOrder columnOrder = (FieldOrder) this.columnOrders
					.get(orderInfoField.getId());
			if (columnOrder != null) {
				ComparatorExpression temp = columnOrder
						.buildComparatorExpression(orderInfo);
				if (!orderInfoField.isAscending()) {
					temp = new ReverseComparatorExpression(temp);
				}
				multiExpr.add(temp);
			}
		}

		return multiExpr;
	}
}
