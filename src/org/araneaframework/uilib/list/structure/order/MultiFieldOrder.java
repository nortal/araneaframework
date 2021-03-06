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

package org.araneaframework.uilib.list.structure.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.MultiComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.ReverseComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.OrderInfoField;
import org.araneaframework.uilib.list.structure.ListOrder;

public class MultiFieldOrder implements ListOrder {

  private static final Log LOG = LogFactory.getLog(MultiFieldOrder.class);

  protected Map<String, FieldOrder> orders = new HashMap<String, FieldOrder>();

  public void addFieldOrder(FieldOrder order) {
    this.orders.put(order.getFieldId(), order);
  }

  public FieldOrder getFieldOrder(String field) {
    return this.orders.get(field);
  }

  public boolean isFiedOrdered(String field) {
    return getFieldOrder(field) != null;
  }

  public void clearFieldOrders() {
    for (Iterator<FieldOrder> i = this.orders.values().iterator(); i.hasNext();) {
      i.next().destroy();
      i.remove();
    }
  }

  public void init(Environment env) {
    for (ListOrder order : this.orders.values()) {
      order.init(env);
    }
  }

  public void destroy() {
    clearFieldOrders();
  }

  public ComparatorExpression buildComparatorExpression(OrderInfo orderInfo) {
    LOG.debug("Building ComparatorExpression, orderInfo = " + orderInfo.toString());
    if (orderInfo.getFields().isEmpty()) {
      LOG.debug("No filterInfoFields specified, returning null");
      return null;
    }

    MultiComparatorExpression multiExpr = new MultiComparatorExpression();

    for (OrderInfoField orderInfoField : orderInfo.getFields()) {
      FieldOrder columnOrder = this.orders.get(orderInfoField.getId());
      if (columnOrder != null) {
        ComparatorExpression temp = columnOrder.buildComparatorExpression(orderInfo);
        if (!orderInfoField.isAscending()) {
          temp = new ReverseComparatorExpression(temp);
        }
        multiExpr.add(temp);
      }
    }

    return multiExpr;
  }
}
