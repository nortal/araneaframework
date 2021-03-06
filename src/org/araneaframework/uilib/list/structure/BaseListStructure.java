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

package org.araneaframework.uilib.list.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.araneaframework.uilib.list.structure.order.MultiFieldOrder;

public class BaseListStructure implements Serializable {

  /**
   * Map of {@link ListField}s where the column Ids are the keys and columns are the values.
   */
  protected Map<String, ListField> fields = new HashMap<String, ListField>();

  /**
   * List of {@link ListField}s.
   */
  protected List<ListField> fieldList = new ArrayList<ListField>();

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
  public Map<String, ListField> getFields() {
    return this.fields;
  }

  /**
   * Returns {@link ListField}s.
   * 
   * @return {@link ListField}s.
   */
  public List<ListField> getFieldList() {
    return this.fieldList;
  }

  /**
   * Returns {@link ListField}.
   * 
   * @param id {@link ListField}identifier.
   * @return {@link ListField}.
   */
  public ListField getField(String id) {
    return this.fields.get(id);
  }

  /**
   * Adds a {@link ListField}.
   * 
   * @param column {@link ListField}.
   */
  public void addField(ListField column) {
    this.fields.put(column.getId(), column);
    this.fieldList.add(column);
  }

  /**
   * Clears the {@link ListField}s
   */
  public void clearFields() {
    this.fields = new HashMap<String, ListField>();
    this.fieldList = new ArrayList<ListField>();
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
   * 
   * @param order the {@link ListOrder}.
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
   * @param filter the {@link ListFilter}.
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
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel implements Serializable {

    protected Map<String, ListField.ViewModel> columns = new HashMap<String, ListField.ViewModel>();

    protected List<String> columnIds = new ArrayList<String>();

    protected List<ListField.ViewModel> columnList = new ArrayList<ListField.ViewModel>();

    protected Map<String, Boolean> columnOrders = new HashMap<String, Boolean>();

    /**
     * Takes a snapshot of outer class state.
     */
    protected ViewModel() {
      MultiFieldOrder multiOrder = getListOrder() instanceof MultiFieldOrder ? (MultiFieldOrder) getListOrder() : null;

      for (Iterator<ListField> i = BaseListStructure.this.fieldList.iterator(); i.hasNext();) {
        ListField.ViewModel currentColumn = i.next().getViewModel();
        boolean isOrdered = multiOrder != null && multiOrder.isFiedOrdered(currentColumn.getId());

        this.columnList.add(currentColumn);
        this.columnIds.add(currentColumn.getId());
        this.columns.put(currentColumn.getId(), currentColumn);
        this.columnOrders.put(currentColumn.getId(), isOrdered);
      }
    }

    /**
     * @return Returns the columnList.
     */
    public List<ListField.ViewModel> getColumnList() {
      return this.columnList;
    }

    /**
     * Provides column IDs in the same order as defined in list structure.
     * 
     * @return A list of column IDs.
     */
    public List<String> getColumnIds() {
      return this.columnIds;
    }

    /**
     * @return Returns the columns.
     */
    public Map<String, ListField.ViewModel> getColumns() {
      return this.columns;
    }

    /**
     * Returns <code>true</code> if the column can be ordered.
     * 
     * @param column the column name.
     * @return Returns <code>true</code> if the column can be ordered.
     */
    public boolean isColumnOrdered(String column) {
      return this.columnOrders.get(column).booleanValue();
    }

    /**
     * Provides the map where the list field ID is the key, and a Boolean is a value to indicate whether the column can
     * be ordered or not.
     * 
     * @return A map of list field IDs and Booleans.
     * @since 1.1.5
     */
    public Map<String, Boolean> getColumnOrders() {
      return this.columnOrders;
    }
  }
}
