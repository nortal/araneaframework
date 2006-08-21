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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.araneaframework.uilib.list.contrib.structure.ListColumn;
import org.araneaframework.uilib.list.contrib.structure.order.MultiColumnOrder;

/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class ListStructure implements Serializable {

	private static final Logger log = Logger.getLogger(ListStructure.class);

	/**
	 * {@link ListField} objects.. 
	 */
	protected LinkedHashMap columns = new LinkedHashMap(); 
	
	/**
	 * (@link ListOrder) information.
	 */
	protected ListOrder order;

	/**
	 * (@link ListFilter) information.
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
		return new ArrayList(this.columns.values());
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
	 * Adds a {@link ListColumnInterface}.
	 * 
	 * @param column
	 *            {@link ListColumnInterface}.
	 */
	public void addColumn(ListColumn column) {
		this.columns.put(column.getId(), column);
		this.columnList.add(column);
	}

	/**
	 * Clears the {@link ListColumnInterface}s
	 */
	public void clearColumns() {
		this.columns = new HashMap();
		this.columnList = new ArrayList();
	}
	
	/**
	 * Returns the (@link ListOrder).
	 * 
	 * @return the (@link ListOrder).
	 */
	public ListOrder getListOrder() {
		return this.order;
	}

	/**
	 * Saves the (@link ListOrder).
	 * 
	 * @param filter
	 *            the (@link ListOrder).
	 */
	public void setListOrder(ListOrder order) {
		this.order = order;
	}

	/**
	 * Returns the (@link ListFilter).
	 * 
	 * @return the (@link ListFilter).
	 */
	public ListFilter getListFilter() {
		return this.filter;
	}

	/**
	 * Saves the (@link ListFilter).
	 * 
	 * @param filter
	 *            the (@link ListFilter).
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
	 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
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

			for (Iterator i = ListStructure.this.columnList.iterator(); i
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
