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

package org.araneaframework.backend.list.model;

import java.io.Serializable;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;


public class ListQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long itemRangeStart;	
	protected Long itemRangeCount;
	
	protected Expression filterExpression;	
	protected ComparatorExpression orderExpression;
	
	public Expression getFilterExpression() {
		return this.filterExpression;
	}
	public void setFilterExpression(Expression filterExpression) {
		this.filterExpression = filterExpression;
	}
	public Long getItemRangeStart() {
		return this.itemRangeStart;
	}
	public void setItemRangeStart(Long itemRangeStart) {
		this.itemRangeStart = itemRangeStart;
	}
	public Long getItemRangeCount() {
		return this.itemRangeCount;
	}
	public void setItemRangeCount(Long itemRangeCount) {
		this.itemRangeCount = itemRangeCount;
	}
	public ComparatorExpression getOrderExpression() {
		return this.orderExpression;
	}
	public void setOrderExpression(ComparatorExpression orderExpression) {
		this.orderExpression = orderExpression;
	}
}
