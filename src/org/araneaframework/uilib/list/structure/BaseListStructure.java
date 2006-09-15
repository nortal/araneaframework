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
import org.araneaframework.uilib.list.structure.order.MultiFieldOrder;

public class BaseListStructure implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(BaseListStructure.class);

	/**
	 * Map of {@link ListField}s where the column Ids are the keys and columns
	 * are the values.
	 */
	protected Map fields = new HashMap();

	/**
	 * List of {@link ListField}s.
	 */
	protected List fieldList = new ArrayList();

	/**
	 * {@link ListOrder} information.
	 */
	protected ListOrder order;

	/**
	 * {@link ListFilter} information.
	 */
	protected ListFilter filter;

	/**
	 * Returns {@link ListField}s.
	 * 
	 * @return {@link ListField}s.
	 */
	public Map getFields() {
		return this.fields;
	}

	/**
	 * Returns {@link ListField}s.
	 * 
	 * @return {@link ListField}s.
	 */
	public List getFieldList() {
		return this.fieldList;
	}

	/**
	 * Returns {@link ListField}.
	 * 
	 * @param id
	 *            {@link ListField}identifier.
	 * @return {@link ListField}.
	 */
	public ListField getField(String id) {
		return (ListField) this.fields.get(id);
	}

	/**
	 * Adds a {@link ListField}.
	 * 
	 * @param column
	 *            {@link ListField}.
	 */
	public void addField(ListField column) {
		this.fields.put(column.getId(), column);
		this.fieldList.add(column);
	}

	/**
	 * Clears the {@link ListField}s
	 */
	public void clearFields() {
		this.fields = new HashMap();
		this.fieldList = new ArrayList();
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
			MultiFieldOrder multiOrder = getListOrder() instanceof MultiFieldOrder ? (MultiFieldOrder) getListOrder()
					: null;
			if (multiOrder == null) {
				log.debug("MultiColumnOrder not found");
			}

			for (Iterator i = BaseListStructure.this.fieldList.iterator(); i
					.hasNext();) {
				ListField.ViewModel currentColumn = ((ListField) i
						.next()).getViewModel();
				boolean isOrdered = multiOrder != null
						&& multiOrder.isFiedOrdered(currentColumn.getId());

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
