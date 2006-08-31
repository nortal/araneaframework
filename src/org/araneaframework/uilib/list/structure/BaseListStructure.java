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

package org.araneaframework.uilib.list.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.uilib.list.structure.order.MultiColumnOrder;

public class BaseListStructure implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(BaseListStructure.class);

	/**
	 * Map of {@link ListColumn}s where the column Ids are the keys and columns
	 * are the values.
	 */
	protected Map columns = new HashMap();

	/**
	 * List of {@link ListColumn}s.
	 */
	protected List columnList = new ArrayList();

	/**
	 * {@link ListOrder} information.
	 */
	protected ListOrder order;

	/**
	 * {@link ListFilter} information.
	 */
	protected ListFilter filter;

	/**
	 * Returns {@link ListColumn}s.
	 * 
	 * @return {@link ListColumn}s.
	 */
	public Map getColumns() {
		return this.columns;
	}

	/**
	 * Returns {@link ListColumn}s.
	 * 
	 * @return {@link ListColumn}s.
	 */
	public List getColumnsList() {
		return this.columnList;
	}

	/**
	 * Returns {@link ListColumn}.
	 * 
	 * @param id
	 *            {@link ListColumn}identifier.
	 * @return {@link ListColumn}.
	 */
	public ListColumn getColumn(String id) {
		return (ListColumn) this.columns.get(id);
	}

	/**
	 * Adds a {@link ListColumn}.
	 * 
	 * @param column
	 *            {@link ListColumn}.
	 */
	public void addColumn(ListColumn column) {
		this.columns.put(column.getId(), column);
		this.columnList.add(column);
	}

	/**
	 * Clears the {@link ListColumn}s
	 */
	public void clearColumns() {
		this.columns = new HashMap();
		this.columnList = new ArrayList();
	}
	
	/**
	 * Returns the {@link ListOrder}.
	 * 
	 * @return the {@link ListOrder}.
	 */
	public ListOrder getListOrder() {
		return this.order;
	}

	/**
	 * Sets the {@link ListOrder}.
	 * @param order
	 *            the {@link ListOrder}.
	 */
	public void setListOrder(ListOrder order) {
		this.order = order;
	}

	/**
	 * Returns the {@link ListFilter}.
	 * 
	 * @return the {@link ListFilter}.
	 */
	public ListFilter getListFilter() {
		return this.filter;
	}

	/**
	 * Saves the {@link ListFilter}.
	 * 
	 * @param filter
	 *            the {@link ListFilter}.
	 */
	public void setListFilter(ListFilter filter) {
		this.filter = filter;
	}

	/**
	 * Returns view model.
	 * 
	 * @return view model.
	 */
	public ViewModel getViewModel() {
		log.debug("Getting ListStructure.ViewModel");
		return new ViewModel();
	}

	/**
	 * View Model.
	 * 
	 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
	 */
	public class ViewModel implements Serializable {

		private static final long serialVersionUID = 1L;

		protected Map columns = new HashMap();
		protected List columnList = new ArrayList();
		protected Map columnOrders = new HashMap();

		/**
		 * Takes a snapshot of outer class state.
		 */
		protected ViewModel() {
			log.debug("Constructing ListStructure.ViewModel");
			MultiColumnOrder multiOrder = getListOrder() instanceof MultiColumnOrder ? (MultiColumnOrder) getListOrder()
					: null;
			if (multiOrder == null) {
				log.debug("MultiColumnOrder not found");
			}

			for (Iterator i = BaseListStructure.this.columnList.iterator(); i
					.hasNext();) {
				ListColumn.ViewModel currentColumn = ((ListColumn) i
						.next()).getViewModel();
				boolean isOrdered = multiOrder != null
						&& multiOrder.isColumnOrdered(currentColumn.getId());

				this.columnList.add(currentColumn);
				this.columns.put(currentColumn.getId(), currentColumn);
				this.columnOrders.put(currentColumn.getId(), new Boolean(
						isOrdered));
			}
		}

		/**
		 * @return Returns the columnList.
		 */
		public List getColumnList() {
			return this.columnList;
		}

		/**
		 * @return Returns the columns.
		 */
		public Map getColumns() {
			return this.columns;
		}

		/**
		 * Returns <code>true</code> if the column can be ordered.
		 * 
		 * @param column
		 *            the column name.
		 * @return Returns <code>true</code> if the column can be ordered.
		 */
		public boolean isColumnOrdered(String column) {
			return ((Boolean) this.columnOrders.get(column)).booleanValue();
		}
	}
}
