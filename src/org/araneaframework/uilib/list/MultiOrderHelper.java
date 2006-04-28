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

package org.araneaframework.uilib.list;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

public class MultiOrderHelper {
	private static final Logger log = Logger.getLogger(MultiOrderHelper.class);

	public static OrderInfo getOrderInfo(Map orderInfoMap) {
		Map fieldOrders = new TreeMap();

		for (Iterator i = orderInfoMap.keySet().iterator(); i.hasNext();) {
			String column = (String) i.next();
			long value = Long.parseLong(orderInfoMap.get(column).toString());
			if (value != 0) {
				boolean ascending = value > 0;
				Long priority = new Long(Math.abs(value));
				if (log.isDebugEnabled()) {
					log.debug("Ordered column \"" + column + "\", priority: "
							+ priority + ", " + (ascending ? "asc" : "desc"));
				}

				OrderInfoField orderInfoField = new OrderInfoField(column,
						ascending);
				fieldOrders.put(priority, orderInfoField);
			}
		}

		OrderInfo orderInfo = new OrderInfo();
		for (Iterator i = fieldOrders.values().iterator(); i.hasNext();) {
			OrderInfoField orderInfoField = (OrderInfoField) i.next();
			if (log.isDebugEnabled()) {
				log.debug("Column order \"" + orderInfoField.getId() + "\", "
						+ (orderInfoField.isAscending() ? "asc" : "desc"));
			}
			orderInfo.addField(orderInfoField);
		}
		return orderInfo;
	}
}
