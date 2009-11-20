/*
 * Copyright 2006 Webmedia Group Ltd. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.araneaframework.uilib.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.EventListener;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.structure.ListField;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.ListStructure;
import org.araneaframework.uilib.list.structure.filter.FieldFilterHelper;
import org.araneaframework.uilib.list.structure.filter.FilterHelper;
import org.araneaframework.uilib.list.structure.order.FieldOrder;
import org.araneaframework.uilib.list.util.ListUtil;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ConfigurationUtil;
import org.araneaframework.uilib.util.Event;

/**
 * This class is the base widget for lists. It interacts with the user and uses the data from {@link ListDataProvider}to
 * make a user view into the list. It uses helper classes to do ordering, filtering and sequencing (breaking the list
 * into pages).
 * <p>
 * Note that {@link ListWidget} must be initialized before it can be configured.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Rein Raudjärv
 */
public class ListWidget<T> extends BaseUIWidget implements ListContext {

  protected static final Log LOG = LogFactory.getLog(ListWidget.class);

  public static final String LIST_CHECK_SCOPE = "checked";

  public static final String LIST_RADIO_SCOPE = "radio";

  public static final String LIST_CHECK_VALUE = "checked";

  /**
   * The filter form id.
   */
  public static final String FILTER_FORM_NAME = "form";

  /**
   * The filter button id
   */
  public static final String FILTER_BUTTON_ID = "filter";

  /**
   * The rest filter button id
   */
  public static final String FILTER_RESET_BUTTON_ID = "clearFilter";

  /**
   * The multi-ordering form name.
   */
  public static final String ORDER_FORM_NAME = "orderForm";

  protected ListStructure listStructure; // should not be accessible by public

  // methods
  protected ListDataProvider<T> dataProvider;

  protected TypeHelper typeHelper;

  protected FilterHelper filterHelper;

  protected SequenceHelper sequenceHelper; // should not be accessible by

  // public methods
  protected FormWidget form = new FormWidget(); // is transformed into filter

  // info Map and vice-versa
  protected OrderInfo orderInfo = new OrderInfo();

  protected List<T> itemRange;

  protected Map<String, T> requestIdToRow = new TreeMap<String, T>();

  protected List<T> selectedItems = new LinkedList<T>();

  private List<Runnable> initEvents = new ArrayList<Runnable>();

  private boolean changed = true;

  private boolean selectFromMultiplePages = false;

  private DataProviderDataUpdateListener dataProviderDataUpdateListener = new DataProviderDataUpdateListener();

  private Map<String, EventListener> listEventListeners = new HashMap<String, EventListener>();

  /**
   * This an initial value for whether the list should show full pages. It is set by {@link #showDefaultPages()} and
   * {@link #showFullPages()} methods if the sequence helper is not set, and read by the {@link #init()} method while
   * initializing the sequence helper.
   * <p>
   * This behavior could be revised in the future.
   * 
   * @since 1.2.2
   */
  private Boolean showFullPages;

  // *********************************************************************
  // * CONSTRUCTOR
  // *********************************************************************
  /**
   * Creates a new {@link ListWidget} instance.
   */
  public ListWidget() {
    this.typeHelper = createTypeHelper();
    this.filterHelper = createFilterHelper();
    this.listStructure = createListStructure();
    initEventListeners();
  }

  private void initEventListeners() {
    this.listEventListeners.put("nextPage", new NextPageEventHandler());
    this.listEventListeners.put("previousPage", new PreviousPageEventHandler());
    this.listEventListeners.put("nextBlock", new NextBlockEventHandler());
    this.listEventListeners.put("previousBlock", new PreviousBlockEventHandler());
    this.listEventListeners.put("firstPage", new FirstPageEventHandler());
    this.listEventListeners.put("lastPage", new LastPageEventHandler());
    this.listEventListeners.put("jumpToPage", new JumpToPageEventHandler());
    this.listEventListeners.put("showAll", new ShowAllEventHandler());
    this.listEventListeners.put("showSlice", new ShowSliceEventHandler());
    this.listEventListeners.put("order", new OrderEventHandler());
  }

  /**
   * Returns all registered list event listeners in a map that can be modified. Note that changes to the provided event
   * listeners map are relevant until this list is initialized. After that, the changes won't have any effect.
   * 
   * @return A modifiable map of event listeners that this list initializes in {@link #init()}.
   * @since 2.0
   */
  public Map<String, EventListener> getListEventListeners() {
    return this.listEventListeners;
  }

  // *********************************************************************
  // * LIST ROW CHECK BOXES AND RADIO BUTTONS
  // *********************************************************************

  /**
   * Reads information about selected check boxes and radio buttons, and stores this information. It is possible to make
   * all selected check boxes (from previous pages as well) stored (see {@link #setSelectFromMultiplePages(boolean)} for
   * more information).
   * 
   * @since 1.1.3
   * @see #setSelectFromMultiplePages(boolean)
   * @see #getSelectedRows()
   * @see #getSelectedRow()
   */
  @Override
  protected void update(InputData input) throws Exception {
    super.update(input);

    HttpInputData input2 = (HttpInputData) input;

    input2.pushPathPrefix(LIST_CHECK_SCOPE);

    // 1. Selected check boxes.
    // Path is used to read only those value-names that start with given prefix:
    Map<String, String> listData = input2.getScopedData(getScope().toPath());
    List<Integer> rowKeys = new LinkedList<Integer>();

    // Now we read index numbers of selected rows:
    for (Map.Entry<String, String> reqParam : listData.entrySet()) {
      if (reqParam.getKey() != null && LIST_CHECK_VALUE.equals(reqParam.getValue())) {
        rowKeys.add(new Integer(reqParam.getKey().toString()));
      }
    }

    // Sort the rows in the order displayed on the page:
    Collections.sort(rowKeys);
    if (LOG.isDebugEnabled()) {
      LOG.debug("List [" + getScope().getId() + "]: Collect selected list rows from multiple pages: "
          + this.selectFromMultiplePages);
    }

    // If the items may be collected from multiple pages then we must remove
    // only the rows currently visible (because some of them may have been
    // selected before, but now may be no more checked.)
    // If the items may be collected from only the current page then we must
    // remove the previously collected items.
    if (this.selectFromMultiplePages) {
      this.selectedItems.removeAll(this.itemRange);
    } else {
      this.selectedItems.clear();
    }

    // Let's store the selected rows:
    for (Integer key : rowKeys) {
      T rowItem = getRowFromRequestId(key.toString());
      if (!this.selectedItems.contains(rowItem)) {
        this.selectedItems.add(rowItem);
      }
    }

    // We make these visible for the list check box tag, so that
    // they could be rendered checked.
    putViewData(LIST_CHECK_SCOPE, this.selectedItems);

    // 2. Selected radio button:
    // Path is used to read only those value-names that start with given prefix:
    listData = input.getScopedData(getScope().toPath());

    // In case of radio buttons, we expect only one value.
    if (!listData.isEmpty()) {
      listData.get(LIST_RADIO_SCOPE);
      String rowKey = listData.get(LIST_RADIO_SCOPE);

      if (rowKey != null) {
        rowKey = rowKey.substring(LIST_RADIO_SCOPE.length() + 2);
        T rowItem = getRowFromRequestId(rowKey);
        // In case of radio buttons, we discard the previous value,
        // because only one row can be selected:
        if (!this.selectedItems.contains(rowItem)) {
          this.selectedItems.clear();
          this.selectedItems.add(rowItem);
        }
        putViewData(LIST_RADIO_SCOPE, rowKey);
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("List [" + getScope().getId() + "]: Number of selected list rows: " + this.selectedItems.size());
    }
  }

  /**
   * Specifies whether selected rows (check boxes) should be collected from only one page or from multiple pages.
   * Default is from one page (<code>false</code>).
   * <p>
   * If the selected rows are collected from all pages then the selected objects are remembered in an instance variable.
   * The <code>&lt;ui:listCheckBox/&gt;</code> tag renders them checked when the user returns to the previous page. It
   * uses the <code>equals()</code> method to compare whether the row is among selected rows or not.
   * <p>
   * The default is to select rows only from the current page because it is memory efficient, especially when a database
   * (backend) data provider is used.
   * 
   * @param selectFromMultiplePages If <code>true</code> then rows can be collected from multiple pages. If
   *          <code>false</code> then rows can be collected from the current page.
   */
  public void setSelectFromMultiplePages(boolean selectFromMultiplePages) {
    this.selectFromMultiplePages = selectFromMultiplePages;
  }

  /**
   * Provides whether the selected items are collected from multiple pages (returns <code>true</code>) or only from the
   * current page (returns <code>false</code>).
   * <p>
   * It changes only the behaviour of the check boxes.
   * 
   * @return whether the selected items are collected from multiple pages or not.
   * @see #setSelectFromMultiplePages(boolean)
   */
  public boolean isSelectFromMultiplePages() {
    return this.selectFromMultiplePages;
  }

  /**
   * Collects the selected rows. The objects are not necessarily in the same order as in the list.
   * <p>
   * This method does not distinguish how rows were selected (i.e. check box vs. radio button).
   * 
   * @return A list of row objects.
   * @see #setSelectFromMultiplePages(boolean)
   * @since 1.1.3
   */
  public List<T> getSelectedRows() {
    return this.selectedItems;
  }

  /**
   * Collects one single selected row (for example, in case of radio button list). If there is more than one select row,
   * an exception will be thrown. In case when no rows is selected, <code>null</code> will be returned.
   * <p>
   * This method does not distinguish how rows were selected (i.e. check box vs. radio button).
   * 
   * @return A single selected row object.
   * @see #setSelectFromMultiplePages(boolean)
   * @since 1.1.3
   */
  public T getSelectedRow() {
    Assert.isTrue(this.selectedItems.size() <= 1, "Selected rows count was expected to be not more than one.");
    T rowItem = null;
    if (this.selectedItems.size() == 1) {
      rowItem = this.selectedItems.get(0);
    }
    return rowItem;
  }

  /**
   * Resets the selected rows so they wouldn't appear selected on the next page load.
   */
  public void resetSelectedRows() {
    this.selectedItems = new LinkedList<T>();
    removeViewData(LIST_CHECK_SCOPE);
    removeViewData(LIST_RADIO_SCOPE);
  }

  /* ========== List configuration ========== */

  /**
   * Returns the {@link ListStructure}used to describe the list.
   * 
   * @return the {@link ListStructure}used to describe the list.
   */
  public ListStructure getListStructure() {
    return this.listStructure;
  }

  /**
   * Saves the {@link ListStructure}used to fill the list with data.
   */
  public void setListStructure(ListStructure listStructure) {
    this.listStructure = listStructure;
  }

  /**
   * Returns the {@link ListDataProvider}used to fill the list with data.
   * 
   * @return the {@link ListDataProvider}used to fill the list with data.
   */
  public ListDataProvider<T> getDataProvider() {
    return this.dataProvider;
  }

  /**
   * Sets the {@link ListDataProvider}used to fill the list with data.
   * 
   * @param dataProvider the {@link ListDataProvider}used to fill the list with data.
   */
  public void setDataProvider(ListDataProvider<T> dataProvider) {
    if (this.dataProvider != null) {
      this.dataProvider.removeDataUpdateListener(this.dataProviderDataUpdateListener);
    }

    this.dataProvider = dataProvider;
    this.dataProvider.addDataUpdateListener(this.dataProviderDataUpdateListener);

    try {
      if (isInitialized()) {
        initDataProvider();
      }
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }

    fireChange();
  }

  /**
   * Returns the {@link TypeHelper} used to help with field types.
   * 
   * @return the {@link TypeHelper} used to help with field types.
   */
  public TypeHelper getTypeHelper() {
    return this.typeHelper;
  }

  /**
   * Sets the {@link TypeHelper} used to help with field types.
   * 
   * @param typeHelper {@link TypeHelper} used to help with field types.
   */
  public void setTypeHelper(TypeHelper typeHelper) {
    this.typeHelper = typeHelper;
  }

  /**
   * Returns the {@link FilterHelper} used to help with adding filters.
   * 
   * @return the {@link FilterHelper} used to help with adding filters.
   */
  public FilterHelper getFilterHelper() {
    return this.filterHelper;
  }

  /**
   * Sets the {@link FilterHelper} used to help with adding filters.
   * 
   * @param filterHelper {@link FilterHelper} used to help with adding filters.
   */
  public void setFilterHelper(FilterHelper filterHelper) {
    this.filterHelper = filterHelper;
  }

  /**
   * Returns the {@link FieldFilterHelper} used to help with adding filters for specified field.
   * 
   * @return the {@link FieldFilterHelper} used to help with adding filters for specified field.
   */
  public FieldFilterHelper getFilterHelper(String fieldId) {
    return new FieldFilterHelper(this.filterHelper, fieldId);
  }

  /**
   * Returns the {@link SequenceHelper}used to output pages.
   * 
   * @return the {@link SequenceHelper}used to output pages.
   */
  public SequenceHelper getSequenceHelper() {
    return this.sequenceHelper;
  }

  /**
   * Resets the sequence, starting at first page with all defaults.
   */
  public void resetSequence() {
    this.sequenceHelper = createSequenceHelper();
  }

  /* ========== FormWidget proxy methods ========== */

  /**
   * Returns the filter form.
   * 
   * @return the filter form.
   */
  public FormWidget getForm() {
    return this.form;
  }

  /**
   * Saves the filter form.
   */
  public void setForm(FormWidget form) {
    this.form = form;
  }

  /**
   * Sets the filter button label.
   * 
   * @param label custom label Id.
   */
  public void setFilterButtonLabel(String label) {
    getForm().getElementByFullName(FILTER_BUTTON_ID).setLabel(label);
  }

  /**
   * Sets the filter reset button label.
   * 
   * @param label custom label Id.
   */
  public void setFilterResetButtonLabel(String label) {
    getForm().getElementByFullName(FILTER_RESET_BUTTON_ID).setLabel(label);
  }

  /* ========== ListStructure Proxy methods ========== */

  /**
   * Returns <code>true</code> if all fields are added orderable by default.
   * 
   * @return <code>true</code> if all fields are added orderable by default.
   */
  public boolean isOrderableByDefault() {
    return getListStructure().isOrderableByDefault();
  }

  /**
   * Sets whether all fields are added orderable by default.
   * 
   * @param orderableByDefault whether all fields are added orderable by default.
   */
  public void setOrderableByDefault(boolean orderableByDefault) {
    getListStructure().setOrderableByDefault(orderableByDefault);
  }

  /**
   * Returns {@link ListField}s.
   * 
   * @return {@link ListField}s.
   */
  public List<ListField> getFields() {
    return getListStructure().getFieldList();
  }

  /**
   * Returns {@link ListField}.
   * 
   * @param id {@link ListField}identifier.
   * @return {@link ListField}.
   */
  public ListField getField(String id) {
    return getListStructure().getField(id);
  }

  /**
   * Returns label of {@link ListField}.
   * 
   * @param columnId {@link ListField} identifier.
   * @return label of {@link ListField}.
   */
  public String getFieldLabel(String columnId) {
    ListField field = getField(columnId);
    return field == null ? null : field.getLabel();
  }

  /**
   * Adds a list field.
   * <p>
   * The added field is orderable if {@link #isOrderableByDefault()} returns <code>true</code>.
   * 
   * @param id list field Id.
   * @param label list field label.
   */
  public FieldFilterHelper addField(String id, String label) {
    getListStructure().addField(id, label);
    return getFilterHelper(id);
  }

  /**
   * Adds a list field.
   * 
   * @param id list field Id.
   * @param label list field label.
   * @param orderable whether this list field should be orderable or not.
   */
  public FieldFilterHelper addField(String id, String label, boolean orderable) {
    getListStructure().addField(id, label, orderable);
    return getFilterHelper(id);
  }

  /**
   * Adds a list field.
   * <p>
   * The added field is orderable if {@link #isOrderableByDefault()} returns <code>true</code>.
   * 
   * @param id list field Id.
   * @param label list field label.
   * @param type list field type.
   */
  public FieldFilterHelper addField(String id, String label, Class<?> type) {
    getListStructure().addField(id, label, type);
    return getFilterHelper(id);
  }

  /**
   * Adds a list field.
   * 
   * @param id list field Id.
   * @param label list field label.
   * @param type list field type.
   * @param orderable whether this list field should be orderable or not.
   */
  public FieldFilterHelper addField(String id, String label, Class<?> type, boolean orderable) {
    getListStructure().addField(id, label, type, orderable);
    return getFilterHelper(id);
  }

  /**
   * Adds a non-orderable field that is not bound to any column. The label is not mandatory. This method is convenient
   * to use when one wants to create a check box or radio button column to allow user mark the rows.
   * 
   * @param id list field Id. Is required, and must be distinguished.
   * @param label list field label. If provided, will be used as the label of the column.
   */
  public void addEmptyField(String id, String label) {
    getListStructure().addField(id, label, false);
  }

  /**
   * Adds a non-orderable field that is not bound to any column. The label won't be provided. This method is convenient
   * to use when one wants to create a check box or radio button column to allow user mark the rows.
   * 
   * @param id list field Id. Is required, and must be distinguished.
   * @since 2.0
   */
  public void addEmptyField(String id) {
    addEmptyField(id, null);
  }

  /**
   * Adds a list field order.
   * 
   * @param order list field order.
   */
  public void addFilter(FieldOrder order) {
    getListStructure().addOrder(order);
  }

  /**
   * Removes all list orders.
   */
  public void clearOrders() {
    getListStructure().clearOrders();
  }

  /**
   * Adds a list filter.
   * 
   * @param filter list filter.
   */
  public void addFilter(ListFilter filter) {
    getListStructure().addFilter(filter);
  }

  /**
   * Removes all list filters.
   */
  public void clearFilters() {
    getListStructure().clearFilters();
  }

  /* ========== TypeHelper Proxy methods ========== */
  /**
   * Returns type of list field. Returns null if no such field or type for this field is available.
   * 
   * @param fieldId field identifier.
   * @return field type
   */
  @SuppressWarnings("unchecked")
  public Class getFieldType(String fieldId) {
    return this.typeHelper.getFieldType(fieldId);
  }

  /**
   * Returns {@link Comparator} for the specified field.
   */
  @SuppressWarnings("unchecked")
  public Comparator getFieldComparator(String fieldId) {
    return this.typeHelper.getFieldComparator(fieldId);
  }

  /**
   * Specifies {@link Comparator} for the specified field.
   * 
   * @since 1.1.4
   */
  public void setFieldComparator(String fieldId, Comparator<?> comparator) {
    this.typeHelper.addCustomComparator(fieldId, comparator);
  }

  /**
   * Returns the Locale used by memory-based filters and orders.
   */
  public Locale getLocale() {
    return this.typeHelper.getLocale();
  }

  /**
   * Returns whether new filters and orders are case insensitive.
   */
  public boolean isIgnoreCase() {
    return this.typeHelper.isIgnoreCase();
  }

  /* ========== SequenceHelper Proxy methods ========== */

  /**
   * Returns how many items will be displayed on one page.
   * 
   * @return how many items will be displayed on one page.
   */
  public long getItemsOnPage() {
    return getSequenceHelper().getItemsOnPage();
  }

  /**
   * Sets how many items will be displayed on one page.
   * 
   * @param itemsOnPage how many items will be displayed on one page.
   */
  public void setItemsOnPage(long itemsOnPage) {
    getSequenceHelper().setItemsOnPage(itemsOnPage);
  }

  /**
   * Sets the page which will be displayed. Page index is 0-based.
   * 
   * @param currentPage index of the page.
   */
  public void setCurrentPage(long currentPage) {
    getSequenceHelper().setCurrentPage(currentPage);
  }

  /**
   * Gets first item to be displayed on the current page.
   * 
   * @return index of the first element from the list to be displayed.
   */
  public long getCurrentPageFirstItemIndex() {
    return getSequenceHelper().getCurrentPageFirstItemIndex();
  }

  /**
   * Gets last item to be displayed on the current page.
   * 
   * @return index of the last element from the list to be displayed.
   */
  public long getCurrentPageLastItemIndex() {
    return getSequenceHelper().getCurrentPageLastItemIndex();
  }

  /**
   * Expands the list showing all items.
   */
  public void showFullPages() {
    if (getSequenceHelper() != null) {
      getSequenceHelper().showFullPages();
    } else {
      this.showFullPages = true;
    }
  }

  /**
   * Collapses the list, showing only the current page.
   */
  public void showDefaultPages() {
    if (getSequenceHelper() != null) {
      getSequenceHelper().showDefaultPages();
    } else {
      this.showFullPages = false;
    }
  }

  /* ========== List State reading and modifying ========== */
  /**
   * Returns the filter information from filter form.
   * 
   * @return <code>Map</code> containing filter information.
   */
  public Map<String, Object> getFilterInfo() {
    return ListUtil.readFilterInfo(this.form);
  }

  /**
   * Sets the filter information to list data provider and filter form.
   * 
   * @param filterInfo <code>Map</code> containing filter information.
   */
  public void setFilterInfo(Map<String, Object> filterInfo) {
    if (filterInfo != null) {
      if (isInitialized()) {
        propagateListDataProviderWithFilter(filterInfo);
      }
      ListUtil.writeFilterInfo(this.form, filterInfo);
    }
  }

  private void propagateListDataProviderWithFilter(Map<String, Object> filterInfo) {
    if (this.dataProvider != null) {
      this.dataProvider.setFilterInfo(filterInfo);
    }
  }

  /**
   * Returns the order info.
   * 
   * @return the order info.
   */
  public OrderInfo getOrderInfo() {
    return this.orderInfo;
  }

  /**
   * Sets the initial order of the list.
   * 
   * @param fieldId the name of the column to order by.
   * @param ascending whether ordering should be ascending.
   */
  public void setInitialOrder(String fieldId, boolean ascending) {
    OrderInfo orderInfo = new OrderInfo();
    OrderInfoField orderInfoField = new OrderInfoField(fieldId, ascending);
    orderInfo.addField(orderInfoField);
    setOrderInfo(orderInfo);
  }

  /**
   * Sets the order information to list data provider and list widget.
   * 
   * @param orderInfo <code>OrderInfo</code> containing order information.
   */
  public void setOrderInfo(OrderInfo orderInfo) {
    this.orderInfo = orderInfo;
    if (isInitialized()) {
      propagateListDataProviderWithOrderInfo(orderInfo);
    }
  }

  protected void propagateListDataProviderWithOrderInfo(OrderInfo orderInfo) {
    if (this.dataProvider != null) {
      this.dataProvider.setOrderInfo(orderInfo);
    }
    fireChange();
  }

  /**
   * Forces the list data provider to refresh the data.
   */
  public void refresh() {
    Assert.notNull(this.dataProvider, "DataProvider was NULL in ListWidget.refresh().");
    try {
      this.dataProvider.refreshData();
      this.selectedItems.clear();
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
    fireChange();
  }

  /**
   * Refreshes the current item range, reloading the shown items.
   */
  public void refreshCurrentItemRange() {
    Assert.notNull(this.dataProvider, "DataProvider was NULL in ListWidget.refreshCurrentItemRange().");
    ListItemsData<T> itemRangeData;
    try {
      itemRangeData = this.dataProvider.getItemRange(this.sequenceHelper.getCurrentPageFirstItemIndex(),
          this.sequenceHelper.getItemsOnPage());
    } catch (Exception e) {
      throw new AraneaRuntimeException(e);
    }
    this.itemRange = itemRangeData.getItemRange();
    this.sequenceHelper.setTotalItemCount(itemRangeData.getTotalCount().intValue());
    this.sequenceHelper.validateSequence();
    makeRequestIdToRowMapping();
  }

  /**
   * Returns the current item range.
   * 
   * @return the current item range.
   */
  public List<T> getItemRange() {
    if (this.itemRange == null || this.checkChanged() || this.sequenceHelper.checkChanged()
        || this.typeHelper.checkChanged() || this.filterHelper.checkChanged()) {
      refreshCurrentItemRange();
      // trigger all checks to reliably reset the change status of the list.
      this.checkChanged();
      this.sequenceHelper.checkChanged();
      this.typeHelper.checkChanged();
      this.filterHelper.checkChanged();
    }
    return this.itemRange;
  }

  /**
   * Returns row object according to the request identifier.
   * 
   * @param requestId request identifier.
   * @return list row object.
   */
  public T getRowFromRequestId(String requestId) {
    return this.requestIdToRow.get(requestId);
  }

  // *******************************************************************
  // WIDGET METHODS
  // *******************************************************************

  public void addInitEvent(Event event) {
    if (isAlive()) {
      event.run();
    } else if (!isInitialized()) {
      if (this.initEvents == null) {
        this.initEvents = new ArrayList<Runnable>();
      }
      this.initEvents.add(event);
    }
  }

  protected void runInitEvents() {
    if (this.initEvents != null) {
      for (Runnable runnable : this.initEvents) {
        runnable.run();
      }
    }
    this.initEvents = null;
  }

  /**
   * Initializes the list, initializing contained filter form and the
   * {@link org.araneaframework.uilib.list.dataprovider.ListDataProvider}and getting the initial item range.
   */
  @Override
  protected void init() throws Exception {
    this.sequenceHelper = createSequenceHelper();

    // Registers all defined event listeners for this list.
    for (Map.Entry<String, EventListener> event : this.listEventListeners.entrySet()) {
      addEventListener(event.getKey(), event.getValue());
    }

    initFilterForm();
    initSequenceHelper();
    this.typeHelper.init(getEnvironment());
    this.filterHelper.init(getEnvironment());
    this.listStructure.init(getEnvironment());
    runInitEvents();
    if (getDataProvider() != null) {
      initDataProvider();
    }
  }

  protected SequenceHelper createSequenceHelper() {
    SequenceHelper seqHelper = new SequenceHelper(getConfiguration());
    if (this.showFullPages != null) {
      if (this.showFullPages.booleanValue()) {
        seqHelper.showFullPages();
      } else {
        seqHelper.showDefaultPages();
      }
    }
    return seqHelper;
  }

  protected TypeHelper createTypeHelper() {
    return new TypeHelper();
  }

  protected ListStructure createListStructure() {
    return new ListStructure(getTypeHelper());
  }

  protected FilterHelper createFilterHelper() {
    return new FilterHelper(this);
  }

  protected void initFilterForm() {
    if (this.form == null) {
      this.form = new FormWidget();
    }

    ButtonControl button = new ButtonControl(new FilterEventHandler());
    this.form.addElement(FILTER_BUTTON_ID, UiLibMessages.LIST_FILTER_BUTTON_LABEL, button);

    button = new ButtonControl(new FilterClearEventHandler());
    this.form.addElement(FILTER_RESET_BUTTON_ID, UiLibMessages.LIST_FILTER_CLEAR_BUTTON_LABEL, button);

    this.form.markBaseState();
    addWidget(FILTER_FORM_NAME, this.form);
  }

  protected void initSequenceHelper() {
    Long defaultListSize = ConfigurationUtil.getDefaultListItemsOnPage(getConfiguration());
    if (defaultListSize != null) {
      this.sequenceHelper.setItemsOnPage(defaultListSize.longValue());
    }
  }

  protected void initDataProvider() throws Exception {
    this.dataProvider.setListStructure(getListStructure());
    propagateListDataProviderWithOrderInfo(getOrderInfo());
    propagateListDataProviderWithFilter(getFilterInfo());
    this.dataProvider.init();
  }

  /**
   * Destroys the list and contained data provider and filter form.
   * 
   * @throws Exception
   */
  @Override
  protected void destroy() throws Exception {
    if (this.dataProvider != null) {
      this.dataProvider.destroy();
    }
    if (this.listStructure != null) {
      this.listStructure.destroy();
    }
    if (this.filterHelper != null) {
      this.filterHelper.destroy();
    }
    if (this.typeHelper != null) {
      this.typeHelper.destroy();
    }
  }

  /**
   * Returns {@link ViewModel}- list widget view model.
   * 
   * @return {@link ViewModel}- list widget view model.
   */
  @Override
  public Object getViewModel() {
    return new ViewModel();
  }

  // *******************************************************************
  // EVENT HANDLERS
  // *******************************************************************

  /**
   * Handles page advancing.
   */
  protected class NextPageEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      ListWidget.this.sequenceHelper.goToNextPage();
    }
  }

  /**
   * Handles page preceeding.
   */
  protected class PreviousPageEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      ListWidget.this.sequenceHelper.goToPreviousPage();
    }
  }

  /**
   * Handles block advancing.
   */
  protected class NextBlockEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      ListWidget.this.sequenceHelper.goToNextBlock();
    }
  }

  /**
   * Handles block preceeding.
   */
  protected class PreviousBlockEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      ListWidget.this.sequenceHelper.goToPreviousBlock();
    }
  }

  /**
   * Handles going to first page
   */
  protected class FirstPageEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      ListWidget.this.sequenceHelper.goToFirstPage();
    }
  }

  /**
   * Handles going to last page
   */
  protected class LastPageEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      ListWidget.this.sequenceHelper.goToLastPage();
    }
  }

  /**
   * Handles going to any page by number.
   */
  protected class JumpToPageEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      int page;
      try {
        page = Integer.parseInt(eventParam);
      } catch (Exception e) {
        throw new AraneaRuntimeException("Invalid page index provided.", e);
      }
      ListWidget.this.sequenceHelper.goToPage(page);
    }
  }

  /**
   * Handles showing all records.
   */
  protected class ShowAllEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      filter();
      ListWidget.this.sequenceHelper.showFullPages();
    }
  }

  /**
   * Handles showing only current records.
   */
  protected class ShowSliceEventHandler extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      filter();
      ListWidget.this.sequenceHelper.showDefaultPages();
    }
  }

  /**
   * Handles single column ordering.
   */
  protected void order(String fieldName) throws Exception {
    LOG.debug("Processing Single Column Order");
    boolean ascending = true;

    List<OrderInfoField> orderFields = this.orderInfo.getFields();
    OrderInfoField currentOrder = orderFields.size() > 0 ? orderFields.get(0) : null;
    ascending = currentOrder == null || !currentOrder.getId().equals(fieldName) || !currentOrder.isAscending();

    this.orderInfo.clearFields();
    this.orderInfo.addField(new OrderInfoField(fieldName, ascending));
    propagateListDataProviderWithOrderInfo(this.orderInfo);
    filter();
  }

  protected class OrderEventHandler extends StandardEventListener {

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      // single column ordering
      if (eventParam.length() > 0) {
        order(eventParam);
        return;
      }

      // multi column ordering
      OrderInfo orderInfo = MultiOrderHelper.getOrderInfo(getOrderInfoMap(input.getScopedData(getScope().toPath())));
      propagateListDataProviderWithOrderInfo(orderInfo);
    }

    private Map<String, Number> getOrderInfoMap(Map<String, String> data) {
      Map<String, Number> orderInfoMap = new HashMap<String, Number>();
      for (Map.Entry<String, String> entry : data.entrySet()) {
        String key = entry.getKey();
        if (key.startsWith(ORDER_FORM_NAME)) {
          orderInfoMap.put(key.substring(ORDER_FORM_NAME.length()), Integer.parseInt(entry.getValue()));
        }
      }
      return orderInfoMap;
    }
  }

  /**
   * Creates mapping between rows and request IDs.
   * 
   * @since 1.1
   */
  protected void makeRequestIdToRowMapping() {
    if (this.itemRange != null) {
      this.requestIdToRow.clear();

      int index = 0;

      for (T row : this.itemRange) {
        this.requestIdToRow.put(Integer.toString(index++), row);
      }
    }
  }

  /**
   * Handles filtering.
   */
  protected void filter() throws Exception {
    if (this.form.convertAndValidate() && this.form.isStateChanged()) {
      Map<String, Object> filterInfo = ListUtil.readFilterInfo(this.form);
      propagateListDataProviderWithFilter(filterInfo);
      this.form.markBaseState();
      this.sequenceHelper.setCurrentPage(0);
      fireChange();
    }
  }

  /**
   * Handles filter clearing.
   */
  @SuppressWarnings("unchecked")
  protected void clearFilter() {
    clearForm(this.form);
    propagateListDataProviderWithFilter(Collections.EMPTY_MAP);
    this.sequenceHelper.setCurrentPage(0);
    fireChange();
  }

  protected static void clearForm(FormWidget compositeFormElement) {
    for (GenericFormElement element : compositeFormElement.getElements().values()) {
      if (element instanceof FormElement<?, ?>) {
        ((FormElement<?, ?>) element).setValue(null);
        element.markBaseState();
      } else if (element instanceof FormWidget) {
        clearForm((FormWidget) element);
      }
    }
  }

  /**
   * @since 1.1
   */
  protected boolean checkChanged() {
    boolean result = this.changed;
    this.changed = false;
    return result;
  }

  /**
   * @since 1.1
   */
  protected void fireChange() {
    this.changed = true;
  }

  protected class FilterEventHandler implements OnClickEventListener {

    private static final long serialVersionUID = 1L;

    public void onClick() throws Exception {
      filter();
      resetSelectedRows();
    }
  }

  protected class FilterClearEventHandler implements OnClickEventListener {

    private static final long serialVersionUID = 1L;

    public void onClick() throws Exception {
      clearFilter();
      resetSelectedRows();
    }
  }

  protected class DataProviderDataUpdateListener implements ListDataProvider.DataUpdateListener {

    public void onDataUpdate() {
      fireChange();
    }
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************
  /**
   * Represents a list widget view model.
   * 
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
   */
  public class ViewModel extends BaseApplicationWidget.ViewModel {

    private List<T> itemRange;

    private SequenceHelper.ViewModel sequence;

    private ListStructure.ViewModel listStructure;

    private OrderInfo.ViewModel orderInfo;

    private FormWidget.ViewModel filterForm;

    /**
     * Takes a snapshot of outer class state.
     * 
     * @throws Exception
     */
    protected ViewModel() {
      this.itemRange = ListWidget.this.getItemRange();
      this.sequence = ListWidget.this.sequenceHelper.getViewModel();
      this.listStructure = ListWidget.this.listStructure.getViewModel();
      this.orderInfo = ListWidget.this.getOrderInfo().getViewModel();
      this.filterForm = (FormWidget.ViewModel) ListWidget.this.form._getViewable().getViewModel();
      makeRequestIdToRowMapping();
    }

    /**
     * Returns item range.
     * 
     * @return item range.
     */
    public List<T> getItemRange() {
      return this.itemRange;
    }

    /**
     * Returns sequence helper.
     * 
     * @return sequence helper.
     */
    public SequenceHelper.ViewModel getSequence() {
      return this.sequence;
    }

    /**
     * Returns list structure.
     * 
     * @return list structure.
     */
    public ListStructure.ViewModel getListStructure() {
      return this.listStructure;
    }

    /**
     * Returns order info.
     * 
     * @return order info.
     */
    public OrderInfo.ViewModel getOrderInfo() {
      return this.orderInfo;
    }

    /**
     * Returns filter form view model.
     * 
     * @return filter form view model.
     */
    public FormWidget.ViewModel getFilterForm() {
      return this.filterForm;
    }
  }
}
