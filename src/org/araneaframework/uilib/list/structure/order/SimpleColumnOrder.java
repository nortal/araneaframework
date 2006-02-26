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

import java.util.Comparator;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.VariableComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ComparableType;


public class SimpleColumnOrder extends ComparableType implements ColumnOrder {

	private static final long serialVersionUID = 1L;

	private String columnId;
	
	public SimpleColumnOrder(String columnId, Comparator comparator) {
		setColumnId(columnId);
		setComparator(comparator);
	}

	public SimpleColumnOrder(Comparator comparator) {
		setComparator(comparator);
	}

	public SimpleColumnOrder() {
		// for bean creation
	}

	public String getColumnId() {
		return this.columnId;
	}
	
	public void setColumnId(String id) {
		this.columnId = id;
	}

	public ComparatorExpression buildComparatorExpression(OrderInfo orderInfo) {
		if (this.columnId == null) {
			throw new RuntimeException("Column Id must be provided"); 
		}
		return new VariableComparatorExpression(getColumnId(), getComparator());
	}
}
