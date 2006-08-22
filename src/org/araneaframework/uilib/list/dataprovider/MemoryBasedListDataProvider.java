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

package org.araneaframework.uilib.list.dataprovider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.memorybased.BeanVariableResolver;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.model.ListItemsData;


/**
 * This class provides a memory based implementation of the list. It takes care
 * of the filtering, ordering and returning data to the web components.
 * Implementations should override method <code>loadData</code> loading the
 * initial data for the list.
 * <p>
 * Note, that all operations on items are made on the list of "processed", that
 * is ordered and filtered items.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public abstract class MemoryBasedListDataProvider implements ListDataProvider {

	private static final Logger log = Logger.getLogger(MemoryBasedListDataProvider.class);
	
	// *******************************************************************
	// FIELDS
	// *******************************************************************

	protected Class beanClass;

	protected List allData = new ArrayList();

	protected List processedData = new ArrayList();

	protected BeanFilter currentFilter = null;

	protected boolean doFilter = true;

	protected Comparator currentOrder = null;

	protected boolean doOrder = true;

	// *********************************************************************
	// * CONSTRUCTORS
	// *********************************************************************

	/**
	 * Creates the class initializing its parameters.
	 * 
	 * @param beanClass
	 *            Value Object class.
	 */
	protected MemoryBasedListDataProvider(Class beanClass) {
		this.beanClass = beanClass;
	}

	// *********************************************************************
	// * PUBLIC METHODS
	// *********************************************************************

	/**
	 * Loads the data using the <code>loadData</code> method, initializes the
	 * filtering and ordering and returns the number of items loaded.
	 */
	public void init() throws Exception {
		this.allData = loadData();
		this.processedData.addAll(this.allData);
	}

	/**
	 * Empty.
	 */
	public void destroy() {
	}

	/**
	 * Returns the number of items in the processed list.
	 * 
	 * @return the number of items in the processed list.
	 */
	public Long getItemCount() throws Exception {
		process(this.currentFilter, this.currentOrder, this.allData,
				this.processedData);
		return new Long(this.processedData.size());
	}

	/**
	 * Returns <code>List</code> of all processed items.
	 * 
	 * @return <code>List</code> of all processed items.
	 */
	public ListItemsData getAllItems() throws Exception {
		ListItemsData result = new ListItemsData();

		process(this.currentFilter, this.currentOrder, this.allData,
				this.processedData);
		result.setItemRange(this.processedData);
		result.setTotalCount(getItemCount());

		return result;
	}

	/**
	 * Returns a range of processed items, starting with <code>start</code>
	 * (indexing starts at 0) and <code>count</code> items after it.
	 * 
	 * @param start
	 *            the start of item range.
	 * @param count
	 *            the count of items in the range.
	 */
	public ListItemsData getItemRange(Long start, Long count) throws Exception {
		ListItemsData result = new ListItemsData();

		process(this.currentFilter, this.currentOrder, this.allData,
				this.processedData);
		result.setItemRange(getSubList(this.processedData, start.intValue(),
				count == null ? -1 : start.intValue() + count.intValue() - 1));
		result.setTotalCount(getItemCount());
		return result;
	}

	/**
	 * Returns a processed item by index.
	 * 
	 * @param index
	 *            0-based index of processed item
	 * @return processed item.
	 */
	public Object getItem(Long index) throws Exception {
		process(this.currentFilter, this.currentOrder, this.allData,
				this.processedData);
		return this.processedData.get(index.intValue());
	}

	/**
	 * Saves the list order. Transforms the order information into
	 * {@link VoOrder}using
	 * {@link OrderFactory#getVoOrder(Class, String, boolean)}and marks that
	 * order has changed.
	 */
	public void setOrderExpression(ComparatorExpression orderExpr) {
		this.doOrder = true;
		if (orderExpr != null) {
			this.currentOrder = new BeanOrder(orderExpr);
		} else {
			this.currentOrder = null;
		}
	}

	/**
	 * Transforms the filter information into {@link VoFilter}using
	 * {@link VoFilterBuilderVisitor}.
	 * 
	 * @throws Exception
	 */
	public void setFilterExpression(Expression filterExpr) {
		this.doFilter = true;

		if (filterExpr != null) {
			this.currentFilter = new BeanFilter(filterExpr);
		} else {
			this.currentFilter = null;
		}
	}

	/**
	 * Refreshes the data, including reordering and refiltering.
	 */
	public void refreshData() throws Exception {
		log.debug("Loading all data");
		this.allData = loadData();
		this.doFilter = true;
		this.doOrder = true;
	}

	// *********************************************************************
	// * OVERRIDABLE METHODS
	// *********************************************************************

	/**
	 * Processes the list items, filtering and ordering them, if there is need.
	 * 
	 * @param voFilter
	 *            Value Object filter.
	 * @param voOrder
	 *            Value Object order.
	 */
	protected void process(BeanFilter beanFilter, Comparator beanOrder,
			List all, List processed) {
		if (this.doFilter) {
			filter(beanFilter, all, processed);

			this.doFilter = false;
			this.doOrder = true;
		}
		if (this.doOrder) {
			order(beanOrder, processed);
			this.doOrder = false;
		}
	}

	/**
	 * Filters the items.
	 * 
	 * @param beanFilter
	 *            Bean filter.
	 */
	protected void filter(BeanFilter beanFilter, List all, List filtered) {
		log.debug("Filtering list itmes");		
		filtered.clear();
		if (beanFilter == null) {
			filtered.addAll(all);
			return;
		}
		for (Iterator i = all.iterator(); i.hasNext();) {
			Object vo = i.next();
			if (beanFilter.suits(vo)) {
				filtered.add(vo);
			}
		}
	}

	/**
	 * Orders the items.
	 * 
	 * @param voOrder
	 *            Value Object order.
	 */
	protected void order(Comparator comparator, List ordered) {
		log.debug("Ordering list itmes");
		if (comparator != null) {
			Collections.sort(ordered, comparator);
		}
	}

	/**
	 * Gets sublist from the list.
	 * 
	 * @param records
	 *            list where the sublist will be originated from.
	 * @param start
	 *            first index of the element to be included to the sublist. List
	 *            is 0-based.
	 * @param end
	 *            last index of the element to be included to the sublist.
	 * 
	 * @return sublist of the given list.
	 */
	public static List getSubList(List records, int start, int end) {
		int len = end - start + 1;

		List subRecords = null;
		if (start < 0) {
			start = 0;
		}

		end = start + len;
		if (end < 0) {
			subRecords = records;
		} else if (start >= records.size()) {
			subRecords = new ArrayList();
		} else {
			if (end > records.size()) {
				end = records.size();
			}

			List subList = records.subList(start, end);
			ArrayList tmpRecords = new ArrayList();
			for (int i = 0; i < subList.size(); i++) {
				tmpRecords.add(subList.get(i));
			}
			subRecords = tmpRecords;
		}

		return new ArrayList(subRecords);
	}

	// *********************************************************************
	// * INNER CLASSES
	// *********************************************************************

	class BeanOrder implements Comparator, Serializable {
		
		private static final long serialVersionUID = 1L;

		private ComparatorExpression orderExpr;
		private BeanVariableResolver resolver1;
		private BeanVariableResolver resolver2;

		public BeanOrder(ComparatorExpression orderExpr) {
			this.orderExpr = orderExpr;
			this.resolver1 = new BeanVariableResolver(
					MemoryBasedListDataProvider.this.beanClass);
			this.resolver2 = new BeanVariableResolver(
					MemoryBasedListDataProvider.this.beanClass);
		}

		public int compare(Object o1, Object o2) {
			this.resolver1.setBean(o1);
			this.resolver2.setBean(o2);
			try {
				return this.orderExpr.compare(this.resolver1, this.resolver2);
			} catch (ExpressionEvaluationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	class BeanFilter implements Serializable {

		private static final long serialVersionUID = 1L;

		private Expression filterExpr;

		private BeanVariableResolver resolver;

		public BeanFilter(Expression filterExpr) {
			this.filterExpr = filterExpr;
			this.resolver = new BeanVariableResolver(
					MemoryBasedListDataProvider.this.beanClass);
		}

		public boolean suits(Object bean) {
			this.resolver.setBean(bean);
			try {
				return ((Boolean) this.filterExpr.evaluate(this.resolver))
						.booleanValue();
			} catch (ExpressionEvaluationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	// *********************************************************************
	// * ABSTRACT METHODS
	// *********************************************************************

	/**
	 * A callback method, which should load the data and return it as a list of
	 * Beans. It should use for that the <code>params</code> which are
	 * passed to it from web component.
	 * 
	 * @return <code>List</code> of Value Objects.
	 */
	public abstract List loadData() throws Exception;
}
