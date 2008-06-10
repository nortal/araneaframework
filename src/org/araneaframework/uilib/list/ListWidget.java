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

package org.araneaframework.uilib.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.Path;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.core.StandardPath;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.ConfigurationContext;
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
import org.araneaframework.uilib.util.Event;

/**
 * This class is the base widget for lists. It interacts with the user and uses the data from
 * {@link org.araneaframework.uilib.list.dataprovider.ListDataProvider}to make a user view into the list.
 * It uses helper classes to do ordering, filtering and sequencing (breaking the list into pages).
 * <p>
 * Note that {@link ListWidget} must be initialized before it can be
 * configured.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Rein Raudjärv
 */
public class ListWidget extends BaseUIWidget implements ListContext {

	private static final long serialVersionUID = 1L;

	protected static final Log log = LogFactory.getLog(ListWidget.class);

    public static final String LIST_CHECK_SCOPE = "checked";

    public static final String LIST_RADIO_SCOPE = "radio";

    public static final String LIST_CHECK_VALUE = "checked";

    //*******************************************************************
	// FIELDS
	//*******************************************************************

	/** The filter form id. */
	public static final String FILTER_FORM_NAME = "form";

	/** The filter button id */
	public static final String FILTER_BUTTON_ID = "filter";
	/** The rest filter button id */
	public static final String FILTER_RESET_BUTTON_ID = "clearFilter";

	/** The multi-ordering form name. */
	public static final String ORDER_FORM_NAME = "orderForm";

	protected ListStructure listStructure;							// should not be accessible by public methods
	protected ListDataProvider dataProvider;

	protected TypeHelper typeHelper;
	protected FilterHelper filterHelper; 
	protected SequenceHelper sequenceHelper;						// should not be accessible by public methods	

	protected FormWidget form = new FormWidget();					// is transfomed into filter info Map and vice-versa
	protected OrderInfo orderInfo = new OrderInfo();

	protected List itemRange;
	protected Map requestIdToRow = new HashMap();
	protected List selectedItems = new LinkedList();
	
	private List initEvents = new ArrayList();
	
	private boolean changed = true;
    private boolean selectFromMultiplePages = false;
	private DataProviderDataUpdateListener dataProviderDataUpdateListener = new DataProviderDataUpdateListener();

	//*********************************************************************
	//* CONSTRUCTOR
	//*********************************************************************
	
	/**
	 * Creates a new {@link ListWidget} instance. 
	 */
	public ListWidget() {
		typeHelper = createTypeHelper();
		filterHelper = createFilterHelper();
		listStructure = createListStructure();
	}

  //*********************************************************************
  //* LIST ROW CHECK BOXES AND RADIO BUTTONS 
  //*********************************************************************

  /**
   * Reads information about selected check boxes and radio buttons, and stores
   * this information. It is possible to make all selected check boxes (from
   * previous pages as well) stored (see
   * {@link #setSelectFromMultiplePages(boolean)} for more information).
   * 
   * @since 1.1.3
   * @see #setSelectFromMultiplePages(boolean)
   * @see #getSelectedRows()
   * @see #getSelectedRow()
   */
  protected void update(InputData input) throws Exception {
    super.update(input);

    String listPath = getScope().toPath().toString();

    // 1. Selected check boxes.

    // Path is used to read only those value-names that start with given prefix:
    Path path = new StandardPath(listPath + "." + LIST_CHECK_SCOPE);

    Map listData = input.getScopedData(path);

    List rowKeys = new LinkedList();

    // Now we read index numbers of selected rows:
    for (Iterator i = listData.entrySet().iterator(); i.hasNext();) {
      Map.Entry reqParam = (Map.Entry) i.next();

      if (reqParam.getKey() != null
          && LIST_CHECK_VALUE.equals(reqParam.getValue())) {
        rowKeys.add(new Integer(reqParam.getKey().toString()));
      }
    }

    // Sort the rows in the order displayed on the page:
    Collections.sort(rowKeys);

    if (log.isDebugEnabled()) {
      log.debug("List [" + getScope().getId()
          + "]: Collect selected list rows from multiple pages: "
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
    for (Iterator i = rowKeys.iterator(); i.hasNext(); ) {
      Object rowItem = getRowFromRequestId(i.next().toString());
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
      String rowKey = (String) listData.get(LIST_RADIO_SCOPE);

      if (rowKey != null) {
        rowKey = rowKey.substring(listPath.length() + LIST_RADIO_SCOPE.length() + 2);
        Object rowItem = getRowFromRequestId(rowKey);
        
        // In case of radio buttons, we discard the previous value,
        // because only one row can be selected:
        if (!this.selectedItems.contains(rowItem)) {
          this.selectedItems.clear();
          this.selectedItems.add(rowItem);
        }

        putViewData(LIST_RADIO_SCOPE, rowKey);
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("List [" + getScope().getId()
          + "]: Number of selected list rows: " + this.selectedItems.size());
    }
  }

  /**
   * Specifies whether selected rows (check boxes) should be collected from only
   * one page or from multiple pages. Default is from one page (<code>false</code>).
   * <p>
   * If the selected rows are collected from all pages then the selected objects
   * are remembered in an instance variable. The
   * <code>&lt;ui:listCheckBox/&gt;</code> tag renders them checked when the
   * user returns to the previous page. It uses the <code>equals()</code>
   * method to compare whether the row is among selected rows or not.
   * <p>
   * The default is to select rows only from the current page because it is
   * memory efficient, especially when a database (backend) data provider is
   * used.
   * 
   * @param selectFromMultiplePages If <code>true</code> then rows can be
   *            collected from multiple pages. If <code>false</code> then rows
   *            can be collected from the current page.
   */
  public void setSelectFromMultiplePages(boolean selectFromMultiplePages) {
    this.selectFromMultiplePages = selectFromMultiplePages;
  }

  /**
   * Provides whether the selected items are collected from multiple pages
   * (returns <code>true</code>) or only from the current page (returns
   * <code>false</code>).
   * <p>
   * It changes only the behaviour of the check boxes.
   * 
   * @return whether the selected items are collected from multiple pages or
   *         not.
   * @see #setSelectFromMultiplePages(boolean)
   */
  public boolean isSelectFromMultiplePages() {
    return this.selectFromMultiplePages;
  }

  /**
   * Collects the selected rows. The objects are not necessarily in the same
   * order as in the list.
   * <p>
   * This method does not distinguish how rows were selected (i.e. check box vs.
   * radio button).
   * 
   * @return A list of row objects.
   * @see #setSelectFromMultiplePages(boolean)
   * @since 1.1.3
   */
  public List getSelectedRows() {
    return this.selectedItems;
  }

  /**
   * Collects one single selected row (for example, in case of radio button
   * list). If there is more than one select row, an exception will be thrown.
   * In case when no rows is selected, <code>null</code> will be returned.
   * <p>
   * This method does not distinguish how rows were selected (i.e. check box vs.
   * radio button).
   * 
   * @return A single selected row object.
   * @see #setSelectFromMultiplePages(boolean)
   * @since 1.1.3
   */
  public Object getSelectedRow() {
    Assert.isTrue(this.selectedItems.size() <= 1,
        "Selected rows count was expected to be not more than one.");

    Object rowItem = null;

    if (this.selectedItems.size() == 1) {
      rowItem = this.selectedItems.get(0);
    }

    return rowItem;
  }

  /**
   * Resets the selected rows so they wouldn't appear selected on the next page
   * load.
   */
  public void resetSelectedRows() {
    this.selectedItems = new LinkedList();
    removeViewData(LIST_CHECK_SCOPE);
    removeViewData(LIST_RADIO_SCOPE);
  }

    //*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************

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
	public ListDataProvider getDataProvider() {
		return this.dataProvider;
	}

	/**
	 * Sets the {@link ListDataProvider}used to fill the list with data.
	 * 
	 * @param dataProvider the {@link ListDataProvider}used to fill the list with data.
	 */
	public void setDataProvider(ListDataProvider dataProvider) {
		if (this.dataProvider != null)
			this.dataProvider.removeDataUpdateListener(dataProviderDataUpdateListener);

		this.dataProvider = dataProvider;
		this.dataProvider.addDataUpdateListener(dataProviderDataUpdateListener);

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
		return filterHelper;
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
	 * Returns the {@link FieldFilterHelper} used to help with adding filters
	 * for specified field.
	 * 
	 * @return the {@link FieldFilterHelper} used to help with adding filters
	 * for specified field.
	 */
	public FieldFilterHelper getFilterHelper(String fieldId) {
		return new FieldFilterHelper(filterHelper, fieldId);
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
	 * @param orderableByDefault whether all fields are added orderable by
	 * default.
	 */
	public void setOrderableByDefault(boolean orderableByDefault) {
		getListStructure().setOrderableByDefault(orderableByDefault);
	}	

	/**
	 * Returns {@link ListField}s.
	 * 
	 * @return {@link ListField}s.
	 */
	public List getFields() {
		return getListStructure().getFieldList();
	}

	/**
	 * Returns {@link ListField}.
	 * 
	 * @param id
	 *            {@link ListField}identifier.
	 * @return {@link ListField}.
	 */
	public ListField getField(String id) {
		return getListStructure().getField(id);
	}

	/**
	 * Returns label of {@link ListField}.
	 * 
	 * @param columnId
	 *            {@link ListField} identifier.
	 * @return label of {@link ListField}.
	 */
	public String getFieldLabel(String columnId) {
		ListField field = getField(columnId);
		return field == null ? null : field.getLabel();
	}

	/**
	 * Adds a list field.
	 * <p>
	 * The added field is orderable if {@link #isOrderableByDefault()}
	 * returns <code>true</code>.
	 * 
	 * @param id
	 *            list field Id.
	 * @param label
	 *            list field label.
	 */
	public FieldFilterHelper addField(String id, String label) {
		getListStructure().addField(id, label);
		return getFilterHelper(id);
	}

	/**
	 * Adds a list field.
	 * 
	 * @param id
	 *            list field Id.
	 * @param label
	 *            list field label.
	 * @param orderable
	 *            whether this list field should be orderable or not. 
	 */
	public FieldFilterHelper addField(String id, String label, boolean orderable) {
		getListStructure().addField(id, label, orderable);
		return getFilterHelper(id);
	}

	/**
	 * Adds a list field.
	 * <p>
	 * The added field is orderable if {@link #isOrderableByDefault()}
	 * returns <code>true</code>.
	 * 
	 * @param id
	 *            list field Id.
	 * @param label
	 *            list field label.
	 * @param type
	 *            list field type.
	 */
	public FieldFilterHelper addField(String id, String label, Class type) {
		getListStructure().addField(id, label, type);
		return getFilterHelper(id);
	}

	/**
	 * Adds a list field.
	 * 
	 * @param id
	 *            list field Id.
	 * @param label
	 *            list field label.
	 * @param type
	 *            list field type.
	 * @param orderable
	 *            whether this list field should be orderable or not. 
	 */	
	public FieldFilterHelper addField(String id, String label, Class type, boolean orderable) {
		getListStructure().addField(id, label, type, orderable);
		return getFilterHelper(id);
	}

    /**
     * Adds a non-orderable field that is not bound to any column. The label is
     * not mandatory. This method is convenient to use when one wants to create
     * a check box or radio button column to allow user mark the rows.
     * 
     * @param id list field Id. Is required, and must be distinguished.
     * @param label list field label. If provided, will be used as the label of
     *            the column.
     */
    public void addEmptyField(String id, String label) {
        getListStructure().addField(id, label, false);
    }

    /**
	 * Adds a list field order.
	 * 
	 * @param order
	 *           list field order.
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
	 * @param filter
	 *           list filter.
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
	 * Returns type of list field. Returns null if no such field or type for
	 * this field is available.
	 * 
	 * @param fieldId
	 *            field identifier.
	 * @return field type
	 */
	public Class getFieldType(String fieldId) {
		return this.typeHelper.getFieldType(fieldId);
	}

	/**
	 * Returns {@link Comparator} for the specified field.
	 */
	public Comparator getFieldComparator(String fieldId) {
		return this.typeHelper.getFieldComparator(fieldId);
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
	 * @return how many items will be displayed on one page.
	 */
	public long getItemsOnPage() {
		return getSequenceHelper().getItemsOnPage();
	}

	/**
	 * Sets how many items will be displayed on one page.
	 * @param itemsOnPage how many items will be displayed on one page.
	 */
	public void setItemsOnPage(long itemsOnPage) {
		getSequenceHelper().setItemsOnPage(itemsOnPage);
	}

	/**
	 * Sets the page which will be displayed. Page index is 0-based.
	 * 
	 * @param currentPage
	 * index of the page.
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
		getSequenceHelper().showFullPages();
	}

	/**
	 * Collapses the list, showing only the current page.
	 */
	public void showDefaultPages() {
		getSequenceHelper().showDefaultPages();
	}

	/* ========== List State reading and modifying ========== */	

	/**
	 * Returns the filter information from filter form.
	 * @return <code>Map</code> containing filter information.
	 */
	public Map getFilterInfo() {
		return ListUtil.readFilterInfo(this.form);
	}

	/**
	 * Sets the filter information to list data provider and filter form.
	 * 
	 * @param filterInfo <code>Map</code> containing filter information.
	 */
	public void setFilterInfo(Map filterInfo) {  	
		if (filterInfo != null) {
			if (isInitialized()) {
				propagateListDataProviderWithFilter(filterInfo);				
			}
			ListUtil.writeFilterInfo(this.form, filterInfo);
		}
	}

	private void propagateListDataProviderWithFilter(Map filterInfo) {
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
		if (this.dataProvider == null)
			throw new IllegalStateException("DataProvider was NULL in ListWidget.refresh().");

		try {
			this.dataProvider.refreshData();
	        this.selectedItems.clear();
		}
		catch (Exception e) {
			ExceptionUtil.uncheckException(e);
		}
		fireChange();
	}

	/**
	 * Refreshes the current item range, reloading the shown items.
	 */
	public void refreshCurrentItemRange() {
		if (this.dataProvider == null)
			throw new IllegalStateException("DataProvider was NULL in ListWidget.refreshCurrentItemRange().");

		ListItemsData itemRangeData;
		
		try {
			itemRangeData = this.dataProvider.getItemRange(new Long(this.sequenceHelper
					.getCurrentPageFirstItemIndex()), new Long(this.sequenceHelper.getItemsOnPage()));
		}
		catch (Exception e) {
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
	public List getItemRange() {
		if (itemRange == null || this.checkChanged() || sequenceHelper.checkChanged() || typeHelper.checkChanged() || filterHelper.checkChanged()) {
			refreshCurrentItemRange();

            // trigger all checks to reliably reset the change status of the list. 
			this.checkChanged();
			sequenceHelper.checkChanged();
			typeHelper.checkChanged();
			filterHelper.checkChanged();
		}

		return this.itemRange;
	}

	/**
	 * Returns row object according to the request identifier.
	 * 
	 * @param requestId request identifier.
	 * @return list row object.
	 */
	public Object getRowFromRequestId(String requestId) {	
		return this.requestIdToRow.get(requestId);
	}


	//*******************************************************************
	// WIDGET METHODS
	//*******************************************************************  
	
	public void addInitEvent(Event event) {
		if (isAlive()) {
			event.run();
		} else if (!isInitialized()){
			if (initEvents == null)
				initEvents = new ArrayList();
			initEvents.add(event);
		}		
	}
	
	protected void runInitEvents() {
		if (initEvents != null) {
			for (Iterator it = initEvents.iterator(); it.hasNext();) {
				Runnable event = (Runnable) it.next();
				event.run();
			}
		}
		initEvents = null;
	}
	
	/**
     * Initilizes the list, initializing contained filter form and the
     * {@link org.araneaframework.uilib.list.dataprovider.ListDataProvider}and
     * getting the initial item range.
     */
	protected void init() throws Exception {
		this.sequenceHelper = createSequenceHelper();

		addEventListener("nextPage", new NextPageEventHandler());
		addEventListener("previousPage", new PreviousPageEventHandler());
		addEventListener("nextBlock", new NextBlockEventHandler());
		addEventListener("previousBlock", new PreviousBlockEventHandler());
		addEventListener("firstPage", new FirstPageEventHandler());
		addEventListener("lastPage", new LastPageEventHandler());
		addEventListener("jumpToPage", new JumpToPageEventHandler());
		addEventListener("showAll", new ShowAllEventHandler());
		addEventListener("showSlice", new ShowSliceEventHandler());
		addEventListener("order", new OrderEventHandler());

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
		return new SequenceHelper(getConfiguration());
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

	protected void initFilterForm() throws Exception {
		if (this.form == null) {
			this.form = new FormWidget();
		}

		FormElement filterButton = this.form.addElement(FILTER_BUTTON_ID, UiLibMessages.LIST_FILTER_BUTTON_LABEL, new ButtonControl(), null, false);
		((ButtonControl) (filterButton.getControl())).addOnClickEventListener(new FilterEventHandler());

		FormElement clearButton = this.form.addElement(FILTER_RESET_BUTTON_ID, UiLibMessages.LIST_FILTER_CLEAR_BUTTON_LABEL, new ButtonControl(), null, false);
		((ButtonControl) (clearButton.getControl())).addOnClickEventListener(new FilterClearEventHandler());

		this.form.markBaseState();

		addWidget(FILTER_FORM_NAME, this.form);		
	}

	protected void initSequenceHelper() {
		Long defaultListSize = (Long) getConfiguration().getEntry(ConfigurationContext.DEFAULT_LIST_ITEMS_ON_PAGE);
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
	 * Destoys the list and contained data provider and filter form.
	 * @throws Exception 
	 */
	protected void destroy() throws Exception {
        if (this.dataProvider != null)
        	this.dataProvider.destroy();
        
        if (this.listStructure != null)
        	this.listStructure.destroy();
        
        if (this.filterHelper != null)
        	this.filterHelper.destroy();
        
        if (this.typeHelper != null)
        	this.typeHelper.destroy();
	}

	/**
	 * Returns {@link ViewModel}- list widget view model.
	 * 
	 * @return {@link ViewModel}- list widget view model.
	 */
	public Object getViewModel() {
		return new ViewModel();
	}

	//*******************************************************************
	// EVENT HANDLERS
	//*******************************************************************

	/**
	 * Handles page advancing.
	 */
	protected class NextPageEventHandler extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.goToNextPage();
		}
	}

	/**
	 * Handles page preceeding.
	 */
	protected class PreviousPageEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.goToPreviousPage();
		}
	}

	/**
	 * Handles block advancing.
	 */
	protected class NextBlockEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.goToNextBlock();
		}
	}

	/**
	 * Handles block preceeding.
	 */
	protected class PreviousBlockEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.goToPreviousBlock();
		}
	}

	/**
	 * Handles going to first page
	 */
	protected class FirstPageEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.goToFirstPage();
		}
	}

	/**
	 * Handles going to last page
	 */
	protected class LastPageEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.goToLastPage();
		}
	}

	/**
	 * Handles going to any page by number.
	 */
	protected class JumpToPageEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			int page;
			try {
				page = Integer.parseInt(eventParam);
			}
			catch (Exception e) {
				throw new AraneaRuntimeException("Invalid page index provided.", e);
			}
			sequenceHelper.goToPage(page);
		}
	}

	/**
	 * Handles showing all records.
	 */
	protected class ShowAllEventHandler  extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
		  filter();
      sequenceHelper.showFullPages();
		}
	}

	/**
	 * Handles showing only current records.
	 */
	protected class ShowSliceEventHandler extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
		  filter();
      sequenceHelper.showDefaultPages();
		}
	}

	/**
	 * Handles single column ordering.
	 */  
	protected void order(String fieldName) throws Exception {	  
		log.debug("Processing Single Column Order");    	

		boolean ascending = true;

		List orderFields = orderInfo.getFields();
		OrderInfoField currentOrderField = (OrderInfoField) (orderFields.size() > 0 ? orderFields.get(0) : null);
		if (currentOrderField != null) {
			if (currentOrderField.getId().equals(fieldName) && currentOrderField.isAscending()) {
				ascending = false;
			}
		}

		orderInfo.clearFields();
		orderInfo.addField(new OrderInfoField(fieldName, ascending));

		propagateListDataProviderWithOrderInfo(orderInfo);		

		// XXX: why is this commented code here?
		// listDataProvider.setOrderInfo(orderInfo);   

		filter();
	}

	protected class OrderEventHandler extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			// single column ordering
			if (eventParam.length() > 0) {
				order(eventParam);
				return;
			}

			// multi column ordering
			OrderInfo orderInfo = MultiOrderHelper.getOrderInfo(getOrderInfoMap(input.getScopedData(getScope().toPath())));

			propagateListDataProviderWithOrderInfo(orderInfo);
		}

		private Map getOrderInfoMap(Map data) {
			Map orderInfoMap = new HashMap();    	
			for (Iterator i = data.entrySet().iterator(); i.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				String key = (String) entry.getKey();
				if (key.startsWith(ORDER_FORM_NAME)) {
					orderInfoMap.put(key.substring(ORDER_FORM_NAME.length()), entry.getValue());
				}
			}
			return orderInfoMap;
		}
	}
	
	
	/**
	 * Creates mapping between rows and request ids.
   * 
   * @since 1.1
	 */
	protected void makeRequestIdToRowMapping() {
		if (this.dataProvider == null)
			return;

		requestIdToRow.clear();
		for (ListIterator i = itemRange.listIterator(); i.hasNext();) {
			Object row = i.next();
			requestIdToRow.put(Integer.toString(i.previousIndex()), row);
		}
	}

	/**
	 * Handles filtering.
	 */
	protected void filter() throws Exception {
		if (form.convertAndValidate() && form.isStateChanged()) {

			Map filterInfo = ListUtil.readFilterInfo(form);

			propagateListDataProviderWithFilter(filterInfo);

			form.markBaseState();
			sequenceHelper.setCurrentPage(0);
			
			fireChange();
		}
	}

	/**
	 * Handles filter clearing. 
	 */
	protected void clearFilter() {
		clearForm(form);
		propagateListDataProviderWithFilter(Collections.EMPTY_MAP);
		sequenceHelper.setCurrentPage(0);
		fireChange();
	}

	protected static void clearForm(FormWidget compositeFormElement) {
		for (Iterator i = compositeFormElement.getElements().values().iterator(); i.hasNext();) {
			GenericFormElement element = (GenericFormElement) i.next();

			if (element instanceof FormElement) {
				((FormElement) element).setValue(null);
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
		boolean result = changed;
		changed = false;
		return result;
	}
	
  /** 
   * @since 1.1
   */
	protected void fireChange() {
		changed = true;
	}

	protected class FilterEventHandler implements OnClickEventListener {
		public void onClick() throws Exception {
			filter();
			resetSelectedRows();
		}
	}

	protected class FilterClearEventHandler implements OnClickEventListener {
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

	//*********************************************************************
	//* VIEW MODEL
	//*********************************************************************

	/**
	 * Represents a list widget view model.
	 * 
	 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
	 *  
	 */
	public class ViewModel extends BaseApplicationWidget.ViewModel {

		private static final long serialVersionUID = 1L;

		private List itemRange;
		private SequenceHelper.ViewModel sequence;
		private ListStructure.ViewModel listStructure;
		private OrderInfo.ViewModel orderInfo;
		private FormWidget.ViewModel filterForm;

		/**
		 * Takes a snapshot of outer class state.
		 * @throws Exception 
		 */
		protected ViewModel() {      
			this.itemRange = ListWidget.this.getItemRange();
			this.sequence = ListWidget.this.sequenceHelper.getViewModel();
			this.listStructure = ListWidget.this.listStructure.getViewModel();
			this.orderInfo = ListWidget.this.getOrderInfo().getViewModel();
			this.filterForm = (FormWidget.ViewModel) ListWidget.this.form._getViewable().getViewModel();
		}

		/**
		 * Returns item range.
		 * 
		 * @return item range.
		 */
		public List getItemRange() {
			return itemRange;
		}

		/**
		 * Returns sequence helper.
		 * 
		 * @return sequence helper.
		 */
		public SequenceHelper.ViewModel getSequence() {
			return sequence;
		}

		/**
		 * Returns list structure.
		 * 
		 * @return list structure.
		 */
		public ListStructure.ViewModel getListStructure() {
			return listStructure;
		}

		/**
		 * Returns order info.
		 * 
		 * @return order info.
		 */
		public OrderInfo.ViewModel getOrderInfo() {
			return orderInfo;
		}

		/**
		 * Returns filter form view model.
		 * 
		 * @return filter form view model.
		 */
		public FormWidget.ViewModel getFilterForm() {
			return filterForm;
		}
	}
}
