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

package org.araneaframework.uilib.list.dataprovider;

import java.util.Map;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListStructure;
import org.araneaframework.uilib.list.util.ListUtil;

/**
 * Data provider base implementation that accepts expressions.
 * 
 * @see ListDataProvider
 * @see MemoryBasedListDataProvider
 * @see BackendListDataProvider
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public abstract class BaseListDataProvider<T> implements ListDataProvider<T> {

	protected ListStructure listStructure;
	protected Map<String, Object> filterInfo;
	protected OrderInfo orderInfo;
	
	public void setListStructure(ListStructure listStructure) {
		this.listStructure = listStructure;
	}

	public final void setFilterInfo(Map<String, Object> filterInfo) {
		this.filterInfo = filterInfo;
		setFilterExpression(ListUtil.toExpression(listStructure.getListFilter(), filterInfo));
	}

	public final void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
		setOrderExpression(ListUtil.toComparatorExpression(listStructure.getListOrder(), orderInfo));
	}

}
