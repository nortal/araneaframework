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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.memorybased.BeanVariableResolver;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * This class provides a memory based implementation of the list. It takes care of the filtering, ordering and returning
 * data to the web components. Implementations should override method <code>loadData</code> loading the initial data for
 * the list.
 * <p>
 * Note, that all operations on items are made on the list of "processed", that is ordered and filtered items.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public abstract class MemoryBasedListDataProvider<T> extends BaseListDataProvider<T> {

  private static final Log LOG = LogFactory.getLog(MemoryBasedListDataProvider.class);

  private Set<DataUpdateListener> dataUpdateListeners = new HashSet<DataUpdateListener>(1);

  // *******************************************************************
  // FIELDS
  // *******************************************************************

  protected Class<T> beanClass;

  protected List<T> allData = new ArrayList<T>();

  protected List<T> processedData = new ArrayList<T>();

  protected BeanFilter currentFilter = null;

  protected boolean doFilter = true;

  protected Comparator<T> currentOrder = null;

  protected boolean doOrder = true;

  // *********************************************************************
  // * CONSTRUCTORS
  // *********************************************************************

  /**
   * Creates the class initializing its parameters.
   * 
   * @param beanClass Value Object class.
   */
  protected MemoryBasedListDataProvider(Class<T> beanClass) {
    this.beanClass = beanClass;
  }

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  /**
   * Loads the data using the <code>loadData</code> method, initializes the filtering and ordering and returns the
   * number of items loaded.
   */
  public void init() throws Exception {
    this.allData = loadData();
    this.processedData.addAll(this.allData);
  }

  /**
   * Empty.
   */
  public void destroy() {}

  /**
   * Returns the number of items in the processed list.
   * 
   * @return the number of items in the processed list.
   */
  public Long getItemCount() throws Exception {
    process(this.currentFilter, this.currentOrder, this.allData, this.processedData);
    return new Long(this.processedData.size());
  }

  /**
   * Returns <code>List</code> of all processed items.
   * 
   * @return <code>List</code> of all processed items.
   */
  public ListItemsData<T> getAllItems() throws Exception {
    ListItemsData<T> result = new ListItemsData<T>();

    process(this.currentFilter, this.currentOrder, this.allData, this.processedData);
    result.setItemRange(this.processedData);
    result.setTotalCount(getItemCount());

    return result;
  }

  /**
   * Returns a range of processed items, starting with <code>start</code> (indexing starts at 0) and <code>count</code>
   * items after it.
   * 
   * @param start the start of item range.
   * @param count the count of items in the range.
   */
  public ListItemsData<T> getItemRange(Long start, Long count) throws Exception {
    ListItemsData<T> result = new ListItemsData<T>();

    process(this.currentFilter, this.currentOrder, this.allData, this.processedData);
    result.setItemRange(getSubList(this.processedData, start.intValue(), count == null ? -1 : start.intValue()
        + count.intValue() - 1));
    result.setTotalCount(getItemCount());
    return result;
  }

  /**
   * Returns a processed item by index.
   * 
   * @param index 0-based index of processed item
   * @return processed item.
   */
  public T getItem(Long index) throws Exception {
    process(this.currentFilter, this.currentOrder, this.allData, this.processedData);
    return this.processedData.get(index.intValue());
  }

  /**
   * Sets the list order expression.
   */
  public void setOrderExpression(ComparatorExpression orderExpr) {
    this.doOrder = true;
    if (orderExpr != null) {
      this.currentOrder = new BeanOrder(orderExpr);
    } else {
      this.currentOrder = null;
    }
    notifyDataChangeListeners();
  }

  /**
   * Sets the list filter expression.
   */
  public void setFilterExpression(Expression filterExpr) {
    this.doFilter = true;

    if (filterExpr != null) {
      this.currentFilter = new BeanFilter(filterExpr);
    } else {
      this.currentFilter = null;
    }
    notifyDataChangeListeners();
  }

  /**
   * Refreshes the data, including reordering and refiltering.
   */
  public void refreshData() {
    LOG.debug("Loading all data");
    try {
      this.allData = loadData();
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
    this.doFilter = true;
    this.doOrder = true;

    notifyDataChangeListeners();
  }

  /** @since 1.1 */
  protected void notifyDataChangeListeners() {
    for (DataUpdateListener listener : this.dataUpdateListeners) {
      listener.onDataUpdate();
    }
  }

  public void addDataUpdateListener(DataUpdateListener listener) {
    this.dataUpdateListeners.add(listener);
  }

  public void removeDataUpdateListener(DataUpdateListener listener) {
    this.dataUpdateListeners.remove(listener);
  }

  // *********************************************************************
  // * OVERRIDABLE METHODS
  // *********************************************************************

  /**
   * Processes the list items, filtering and ordering them, if there is need.
   */
  protected void process(BeanFilter beanFilter, Comparator<? super T> beanOrder, List<T> all, List<T> processed) {
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
   * @param beanFilter Bean filter.
   */
  protected void filter(BeanFilter beanFilter, List<T> all, List<T> filtered) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Filtering list items");
    }
    filtered.clear();
    if (beanFilter == null) {
      filtered.addAll(all);
      return;
    }
    for (T element : all) {
      if (beanFilter.suits(element)) {
        filtered.add(element);
      }
    }
  }

  /**
   * Orders the items.
   */
  protected void order(Comparator<? super T> comparator, List<T> ordered) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Ordering list items");
    }
    if (comparator != null) {
      Collections.sort(ordered, comparator);
    }
  }

  /**
   * Gets sublist from the list.
   * 
   * @param records list where the sublist will be originated from.
   * @param start first index of the element to be included to the sublist. List is 0-based.
   * @param end last index of the element to be included to the sublist.
   * 
   * @return sublist of the given list.
   */
  public static <E> List<E> getSubList(List<E> records, int start, int end) {
    int len = end - start + 1;

    List<E> subRecords = null;
    if (start < 0) {
      start = 0;
    }

    end = start + len;
    if (end < 0) {
      subRecords = records;
    } else if (start >= records.size()) {
      subRecords = new ArrayList<E>();
    } else {
      if (end > records.size()) {
        end = records.size();
      }

      List<E> subList = records.subList(start, end);
      ArrayList<E> tmpRecords = new ArrayList<E>();
      for (int i = 0; i < subList.size(); i++) {
        tmpRecords.add(subList.get(i));
      }
      subRecords = tmpRecords;
    }

    return new ArrayList<E>(subRecords);
  }

  // *********************************************************************
  // * INNER CLASSES
  // *********************************************************************

  class BeanOrder implements Comparator<T>, Serializable {

    private ComparatorExpression orderExpr;

    private BeanVariableResolver<T> resolver1;

    private BeanVariableResolver<T> resolver2;

    public BeanOrder(ComparatorExpression orderExpr) {
      this.orderExpr = orderExpr;
      this.resolver1 = new BeanVariableResolver<T>(MemoryBasedListDataProvider.this.beanClass);
      this.resolver2 = new BeanVariableResolver<T>(MemoryBasedListDataProvider.this.beanClass);
    }

    public int compare(T o1, T o2) {
      this.resolver1.setBean(o1);
      this.resolver2.setBean(o2);
      try {
        return this.orderExpr.compare(this.resolver1, this.resolver2);
      } catch (ExpressionEvaluationException e) {
        throw new NestableRuntimeException(e);
      }
    }
  }

  class BeanFilter implements Serializable {

    private Expression filterExpr;

    private BeanVariableResolver<T> resolver;

    public BeanFilter(Expression filterExpr) {
      this.filterExpr = filterExpr;
      this.resolver = new BeanVariableResolver<T>(MemoryBasedListDataProvider.this.beanClass);
    }

    public boolean suits(T bean) {
      this.resolver.setBean(bean);
      try {
        return ((Boolean) this.filterExpr.evaluate(this.resolver)).booleanValue();
      } catch (ExpressionEvaluationException e) {
        throw new NestableRuntimeException(e);
      }
    }
  }

  // *********************************************************************
  // * ABSTRACT METHODS
  // *********************************************************************

  /**
   * A callback method, which should load the data and return it as a list of Beans. It should use for that the
   * <code>params</code> which are passed to it from web component.
   * 
   * @return <code>List</code> of Value Objects.
   */
  public abstract List<T> loadData() throws Exception;
}
