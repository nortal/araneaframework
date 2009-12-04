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

package org.araneaframework.uilib.list;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MultiOrderHelper {

  private static final Log LOG = LogFactory.getLog(MultiOrderHelper.class);

  public static OrderInfo getOrderInfo(Map<String, Number> orderInfoMap) {
    Map<Long, OrderInfoField> fieldOrders = new TreeMap<Long, OrderInfoField>();

    for (Map.Entry<String, Number> entry : orderInfoMap.entrySet()) {
      String column = entry.getKey();
      long priority = entry.getValue().longValue();
      if (priority != 0) {
        boolean ascend = priority > 0;
        priority = Math.abs(priority);

        if (LOG.isDebugEnabled()) {
          LOG.debug("Ordered column \"" + column + "\", priority: " + priority + ", " + (ascend ? "asc" : "desc"));
        }

        OrderInfoField orderInfoField = new OrderInfoField(column, ascend);
        fieldOrders.put(priority, orderInfoField);
      }
    }

    OrderInfo orderInfo = new OrderInfo();
    for (OrderInfoField orderInfoField : fieldOrders.values()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Column order '" + orderInfoField.getId() + "', " + (orderInfoField.isAscending() ? "asc" : "desc"));
      }
      orderInfo.addField(orderInfoField);
    }
    return orderInfo;
  }
}
