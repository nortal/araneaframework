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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.reader.MapFormReader;
import org.araneaframework.uilib.form.reader.MapFormWriter;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.ListOrder;
import org.araneaframework.uilib.list.structure.ListStructure;
import org.araneaframework.uilib.list.structure.filter.ColumnFilter;
import org.araneaframework.uilib.list.structure.order.ColumnOrder;
import org.araneaframework.uilib.list.util.MapUtil;
import org.araneaframework.uilib.list.util.RecursiveFormUtil;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class is the base widget for lists. It interacts with the user and uses the data from
 * {@link org.araneaframework.uilib.list.dataprovider.ListDataProvider}to make a user view into the list.
 * It uses helper classes to do ordering, filtering and sequencing (breaking the list into pages).
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class ListWidget extends BaseUIWidget {

	private static final long serialVersionUID = 1L;

	protected static final Logger log = Logger.getLogger(ListWidget.class);

	//*******************************************************************
	// FIELDS
	//*******************************************************************

	/**
	 * The filter form name.
	 */
	public static final String FILTER_FORM_NAME = "filterForm";

	public static final String FILTER_BUTTON_ID = "filter";
	public static final String FILTER_CLEAR_BUTTON_ID = "clearFilter";

	/**
	 * The multi-column ordering form name.
	 */
	public static final String ORDER_FORM_NAME = "orderForm";

	protected ListDataProvider listDataProvider;
	protected ListStructure listStructure = new ListStructure();	// should not be accessible by public methods
	protected SequenceHelper sequenceHelper;	// should not be accessible by public methods

	protected FormWidget filterForm;	// is transfomed into filter info Map and vice-versa
	protected OrderInfo orderInfo = new OrderInfo();

	protected List itemRange;
	protected Map requestIdToRow = new HashMap();

	protected String filterButtonLabelId;
	protected String filterClearButtonLabelId;

	//*********************************************************************
	//* CONSTRUCTORS
	//*********************************************************************

	public ListWidget(ListDataProvider listDataProvider, ListStructure listStructure, FormWidget filterForm) throws Exception {  	
		this.listDataProvider = listDataProvider;
		this.listStructure = listStructure;
		this.filterForm = filterForm;
	}

	public ListWidget() {
		this.filterForm = new FormWidget();
	}

	//*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************

	/*
	 * List configuration
	 */	

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
	public ListDataProvider getListDataProvider() {
		return this.listDataProvider;
	}

	/**
	 * Sets the {@link ListDataProvider}used to fill the list with data.
	 * 
	 * @param listDataProvider the {@link ListDataProvider}used to fill the list with data.
	 */
	public void setListDataProvider(ListDataProvider listDataProvider) {
		this.listDataProvider = listDataProvider;
	}

	/**
	 * Returns the filter form.
	 * 
	 * @return the filter form.
	 */
	public FormWidget getFilterForm() {
		return this.filterForm;
	}

	/**
	 * Saves the filter form.
	 */
	public void setFilterForm(FormWidget filterForm) {
		this.filterForm = filterForm;
	}

	/**
	 * Returns {@link ListColumn}s.
	 * 
	 * @return {@link ListColumn}s.
	 */
	public List getListColumns() {
		return this.listStructure.getColumnsList();
	}

	/**
	 * Returns {@link ListColumn}.
	 * 
	 * @param id
	 *            {@link ListColumn}identifier.
	 * @return {@link ListColumn}.
	 */
	public ListColumn getListColumn(String id) {
		return this.listStructure.getColumn(id);
	}

	/**
	 * Returns label of {@link ListColumn}.
	 * 
	 * @param columnId
	 *            {@link ListColumn} identifier.
	 * @return label of {@link ListColumn}.
	 */
	public String getColumnLabel(String columnId) {
		return getListColumn(columnId).getLabel();
	}

	/**
	 * Adds a {@link ListColumn}.
	 * 
	 * @param column
	 *            {@link ListColumn}.
	 */
	public void addListColumn(ListColumn column) {
		this.listStructure.addColumn(column);
	}

	public void addListColumn(String id, String label) {
		this.listStructure.addColumn(id, label);
	}

	public void addListColumn(String id, String label, ColumnOrder columnOrder) {
		this.listStructure.addColumn(id, label, columnOrder, null);
	}

	public void addListColumn(String id, String label, ColumnOrder columnOrder, ColumnFilter columnFilter) {
		this.listStructure.addColumn(id, label, columnOrder, columnFilter);
	}

	/**
	 * Clears the {@link ListColumn}s
	 */
	public void clearColumns() {
		this.listStructure.clearColumns();
	}

	/**
	 * Returns the {@link ListOrder}.
	 * @return the {@link ListOrder}.
	 */
	public ListOrder getListOrder() {
		return this.listStructure.getListOrder();
	}

	/**
	 * Saves the {@link ListOrder}.
	 * 
	 * @param order
	 *            the {@link ListOrder}.
	 */
	public void setListOrder(ListOrder order) {
		this.listStructure.setListOrder(order);
	}

	public void addColumnOrder(ColumnOrder order) {
		this.listStructure.addColumnOrder(order);
	}

	public ColumnOrder getColumnOrder(String column) {
		return this.listStructure.getColumnOrder(column);
	}

	public void clearColumnOrders() {
		this.listStructure.clearColumnOrders();
	}

	/**
	 * Returns the {@link ListFilter}.
	 * 
	 * @return the {@link ListFilter}.
	 */
	public ListFilter getListFilter() {
		return this.listStructure.getListFilter();
	}

	/**
	 * Saves the {@link ListFilter}.
	 * 
	 * @param filter
	 *            the {@link ListFilter}.
	 */
	public void setListFilter(ListFilter filter) {
		this.listStructure.setListFilter(filter);
	}

	public void addFilter(ListFilter subFilter) {
		this.listStructure.addFilter(subFilter);
	}

	public ColumnFilter getColumnFilter(String column) {
		return this.listStructure.getColumnFilter(column);
	}

	public void clearFilters() {
		this.listStructure.clearFilters();
	}

	/*
	 * FormWidget proxy-methods
	 */

	public void setFilterButtonLabel(String labelId) {
		this.filterButtonLabelId = labelId;
		if (isInitialized()) {
			FormElement element = getFilterForm().getElementByFullName(FILTER_BUTTON_ID);		
			element.setLabel(labelId);
		}
	}

	public void setFilterClearButtonLabel(String labelId) {
		this.filterClearButtonLabelId = labelId;
		if (isInitialized()) {
			FormElement element = getFilterForm().getElementByFullName(FILTER_CLEAR_BUTTON_ID);		
			element.setLabel(labelId);
		}
	}	

	public void addFilterFormElement(String id, FormElement element) throws Exception {
		RecursiveFormUtil.addElement(this.filterForm, id, element);
	}

	public void addFilterFormElement(String id, String label, Control control, Data data) throws Exception {
		RecursiveFormUtil.addElement(this.filterForm, id, label, control, data, false);
	}

	public void addFilterFormElement(String id, Control control, Data data) throws Exception {
		addFilterFormElement(id, getColumnLabel(id), control, data);
	}
	
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

	/*
	 * List State reading and modifying
	 */

	/**
	 * Returns the {@link SequenceHelper}used to output pages.
	 * 
	 * @return the {@link SequenceHelper}used to output pages.
	 */
	public SequenceHelper getSequenceHelper() {
		if (this.sequenceHelper == null) {
			throw new RuntimeException("Can not access SequenceHelper, ListWidget must be initialized first");
		}
		return this.sequenceHelper;
	}

	/**
	 * Resets the sequence, starting at first page with all defaults.
	 * 
	 * @throws Exception if item range refreshing doesn't succeed.
	 */
	public void resetSequence() throws Exception {
		this.sequenceHelper = new SequenceHelper(getConfiguration());
	}

	/**
	 * Returns the filter information from filter form.
	 * @return <code>Map</code> containing filter information.
	 */
	public Map getFilterInfo() {
		MapFormReader mapFormReader = new MapFormReader(this.filterForm);
		return mapFormReader.getMap();
	}

	/**
	 * Sets the filter information to list data provider and filter form.
	 * 
	 * @param filterInfo <code>Map</code> containing filter information.
	 * @throws Exception 
	 */
	public void setFilterInfo(Map filterInfo) throws Exception {  	
		if (filterInfo != null) {
			if (isInitialized()) {
				propagateListDataProviderWithFilter(filterInfo);				
			}
			MapFormWriter mapFormWriter = new MapFormWriter();
			mapFormWriter.writeForm(this.filterForm, filterInfo);
		}
	}

	private void propagateListDataProviderWithFilter(Map filterInfo) {
		log.debug("Building FilterExpression for ListDataProvider");
		if (this.listDataProvider != null) {
			ListFilter filter = this.listStructure.getListFilter();
			Expression filterExpr = null;
			if (filter != null) {
				filterExpr = filter.buildExpression(MapUtil.convertToPlainMap(filterInfo));
			}
			this.listDataProvider.setFilterExpression(filterExpr);			
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
	 * @param columnName the name of the column to order by.
	 * @param ascending whether ordering should be ascending.
	 */
	public void setInitialOrder(String columnName, boolean ascending) {
		OrderInfo orderInfo = new OrderInfo();
		OrderInfoField orderInfoField = new OrderInfoField(columnName, ascending);
		orderInfo.addField(orderInfoField);
		setOrderInfo(orderInfo);
	}

	/**
	 * Sets the order information to list data provider and list widget.
	 * 
	 * @param orderInfo <code>OrderInfo</code> containing order information.
	 */
	public void setOrderInfo(OrderInfo orderInfo) {  	
		if (orderInfo != null) {
			if (isInitialized()) {
				propagateListDataProviderWithOrderInfo(orderInfo);				
			}
			this.orderInfo = orderInfo;
		}
	}

	protected void propagateListDataProviderWithOrderInfo(OrderInfo orderInfo) {
		log.debug("Building OrderExpression for ListDataProvider");
		if (this.listDataProvider != null) {
			ListOrder order = this.listStructure.getListOrder();
			ComparatorExpression orderExpr = order != null ? order.buildComparatorExpression(orderInfo) : null;
			this.listDataProvider.setOrderExpression(orderExpr);			
		}
	}
	
	/**
	 * Forces the list data provider to refresh the data.
	 */
	public void forceRefresh() throws Exception {
		this.listDataProvider.refreshData();		
	}

	/**
	 * Refreshes the current item range, reloading the shown items.
	 */
	public void refreshCurrentItemRange() throws Exception {
		log.debug("Refreshing current item range");

		ListItemsData itemRangeData;

		itemRangeData = this.listDataProvider.getItemRange(new Long(this.sequenceHelper
				.getCurrentPageFirstItemIndex()), new Long(this.sequenceHelper.getItemsOnPage()));

		this.itemRange = itemRangeData.getItemRange();
		this.sequenceHelper.setTotalItemCount(itemRangeData.getTotalCount().intValue());
		this.sequenceHelper.validateSequence();
	}

	/**
	 * Returns the current item range.
	 * 
	 * @return the current item range.
	 */
	public List getItemRange() {
		return this.itemRange;
	}

	public Object getRowFromRequestId(String requestId) {	
		return this.requestIdToRow.get(requestId);
	}


	//*******************************************************************
	// WIDGET METHODS
	//*******************************************************************  

	/**
	 * Initilizes the list, initializing contained filter form and the {@link ListDataProvider}and
	 * getting the initial item range.
	 */
	protected void init() throws Exception {
		super.init();

		this.sequenceHelper = new SequenceHelper(getConfiguration());

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

		if (this.filterForm != null) {
			String filterButtonLabelId = this.filterButtonLabelId;
			if (filterButtonLabelId == null) {
				filterButtonLabelId = UiLibMessages.LIST_FILTER_BUTTON_LABEL;
			}
			String clearButtonLabelId = this.filterClearButtonLabelId;
			if (clearButtonLabelId == null) {
				clearButtonLabelId = UiLibMessages.LIST_FILTER_CLEAR_BUTTON_LABEL;
			}

			FormElement filterButton = this.filterForm.addElement(FILTER_BUTTON_ID, filterButtonLabelId, new ButtonControl(), null, false);
			((ButtonControl) (filterButton.getControl())).addOnClickEventListener(new FilterEventHandler());

			FormElement clearButton = this.filterForm.addElement(FILTER_CLEAR_BUTTON_ID, clearButtonLabelId, new ButtonControl(), null, false);
			((ButtonControl) (clearButton.getControl())).addOnClickEventListener(new FilterClearEventHandler());

			this.filterForm.markBaseState();
		}
		else {
			this.filterForm = new FormWidget();
		}                

		//Configuration

		Long defaultListSize = (Long) getConfiguration().getEntry(ConfigurationContext.DEFAULT_LIST_ITEMS_ON_PAGE);
		if (defaultListSize != null) {
			this.sequenceHelper.setItemsOnPage(defaultListSize.longValue());
		}

		addWidget(FILTER_FORM_NAME, this.filterForm);

		log.debug("Initilizing ListWidget.");

		propagateListDataProviderWithOrderInfo(getOrderInfo());
		propagateListDataProviderWithFilter(getFilterInfo());
		
		this.listDataProvider.init();
	}

	/**
	 * Destoys the list and contained data provider and filter form.
	 * @throws Exception 
	 */
	protected void destroy() throws Exception {
		super.destroy();

		log.debug("Destroying ListWidget.");

		listDataProvider.destroy();
	}

	/**
	 * Returns {@link ViewModel}- list widget view model.
	 * 
	 * @return {@link ViewModel}- list widget view model.
	 * @throws Exception 
	 */
	public Object getViewModel() throws Exception {
		return new ViewModel();
	}	  

	protected void handleProcess() throws Exception {
		refreshCurrentItemRange();

		//Making the requestId to row mapping
		requestIdToRow.clear();
		for (ListIterator i = itemRange.listIterator(); i.hasNext();) {
			Object row = i.next();                  
			requestIdToRow.put(Integer.toString(i.previousIndex()), row);
		}    
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
				throw new Exception("Invalid page index provided.");
			}
			sequenceHelper.goToPage(page);
		}
	}

	/**
	 * Handles showing all records.
	 */
	protected class ShowAllEventHandler  extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			sequenceHelper.showFullPages();      
		}
	}

	/**
	 * Handles showing only current records.
	 */
	protected class ShowSliceEventHandler extends StandardEventListener {

		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {      
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

		// listDataProvider.setOrderInfo(orderInfo);   

		filter();
	}

	protected class OrderEventHandler extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			// single column ordering
			log.debug("Processing Order event, with param = " + eventParam);
			if (eventParam.length() > 0) {
				order(eventParam);
				return;
			}

			// multi column ordering
			log.debug("Processing Multi Column Order");    	
			OrderInfo orderInfo = MultiOrderHelper.getOrderInfo(getOrderInfoMap(input.getScopedData()));

			log.debug("Building OrderExpression");
			propagateListDataProviderWithOrderInfo(orderInfo);
		}

		private Map getOrderInfoMap(Map data) {
			Map orderInfoMap = new HashMap();    	
			for (Iterator i = data.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith(ORDER_FORM_NAME)) {
					orderInfoMap.put(key.substring(ORDER_FORM_NAME.length()), data.get(key));
				}
			}
			return orderInfoMap;
		}
	}

	/**
	 * Handles filtering.
	 */
	protected void filter() throws Exception {
		log.debug("Converting and validating FilterForm");
		if (filterForm.convertAndValidate() && filterForm.isStateChanged()) {

			log.debug("Reading FilterInfo");
			MapFormReader mapFormReader = new MapFormReader(filterForm);
			Map filterInfo = mapFormReader.getMap();
			log.debug("FilterInfo: " + filterInfo);

			propagateListDataProviderWithFilter(filterInfo);

			filterForm.markBaseState();
			sequenceHelper.setCurrentPage(0);
		}         
	}

	/**
	 * Handles filter clearing. 
	 */
	protected void clearFilter() {
		clearForm(filterForm);
		propagateListDataProviderWithFilter(new HashMap());
		sequenceHelper.setCurrentPage(0);
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

	protected class FilterEventHandler implements OnClickEventListener {
		public void onClick() throws Exception {
			filter();
		}
	}

	protected class FilterClearEventHandler implements OnClickEventListener {
		public void onClick() throws Exception {
			clearFilter();
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
		protected ViewModel() throws Exception {      
			this.itemRange = ListWidget.this.getItemRange();
			this.sequence = ListWidget.this.sequenceHelper.getViewModel();
			this.listStructure = ListWidget.this.listStructure.getViewModel();
			this.orderInfo = ListWidget.this.getOrderInfo().getViewModel();
			this.filterForm = (FormWidget.ViewModel) ListWidget.this.filterForm._getViewable().getViewModel();
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
