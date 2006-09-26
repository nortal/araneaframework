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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
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
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.reader.MapFormReader;
import org.araneaframework.uilib.form.reader.MapFormWriter;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.structure.ListField;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.ListOrder;
import org.araneaframework.uilib.list.structure.ListStructure;
import org.araneaframework.uilib.list.structure.filter.FieldFilterHelper;
import org.araneaframework.uilib.list.structure.filter.FilterHelper;
import org.araneaframework.uilib.list.structure.order.FieldOrder;
import org.araneaframework.uilib.list.util.MapUtil;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class is the base widget for lists. It interacts with the user and uses the data from
 * {@link org.araneaframework.uilib.list.dataprovider.ListDataProvider}to make a user view into the list.
 * It uses helper classes to do ordering, filtering and sequencing (breaking the list into pages).
 * <p>
 * Note that {@link ListWidget} must be initialized before it can be
 * configured.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class ListWidget extends BaseUIWidget implements ListContext {

	private static final long serialVersionUID = 1L;

	protected static final Logger log = Logger.getLogger(ListWidget.class);

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

	protected FormWidget form;										// is transfomed into filter info Map and vice-versa
	protected OrderInfo orderInfo = new OrderInfo();

	protected List itemRange;
	protected Map requestIdToRow = new HashMap();

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
		assertInitialized();
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
	 * @throws Exception 
	 */
	public void setDataProvider(ListDataProvider dataProvider) throws Exception {
		this.dataProvider = dataProvider;
		initDataProvider();
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
	 * 
	 * @throws Exception if item range refreshing doesn't succeed.
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
		MapFormReader mapFormReader = new MapFormReader(this.form);
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
			mapFormWriter.writeForm(this.form, filterInfo);
		}
	}

	private void propagateListDataProviderWithFilter(Map filterInfo) {
		log.debug("Building FilterExpression for ListDataProvider");
		if (this.dataProvider != null) {
			ListFilter filter = getListStructure().getListFilter();
			Expression filterExpr = null;
			if (filter != null) {
				filterExpr = filter.buildExpression(MapUtil.convertToPlainMap(filterInfo));
			}
			this.dataProvider.setFilterExpression(filterExpr);			
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
		this.orderInfo = orderInfo;
		propagateListDataProviderWithOrderInfo(orderInfo);				
	}

	protected void propagateListDataProviderWithOrderInfo(OrderInfo orderInfo) {
		ListOrder order = getListStructure().getListOrder();
		ComparatorExpression orderExpr = order != null ? order.buildComparatorExpression(orderInfo) : null;
		this.dataProvider.setOrderExpression(orderExpr);			
	}
	
	/**
	 * Forces the list data provider to refresh the data.
	 */
	public void refresh() throws Exception {
		this.dataProvider.refreshData();		
	}

	/**
	 * Refreshes the current item range, reloading the shown items.
	 */
	public void refreshCurrentItemRange() throws Exception {
		ListItemsData itemRangeData;

		itemRangeData = this.dataProvider.getItemRange(new Long(this.sequenceHelper
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

	/**
	 * Initilizes the list, initializing contained filter form and the {@link ListDataProvider}and
	 * getting the initial item range.
	 */
	protected void init() throws Exception {
		log.debug("Initilizing ListWidget.");
		
		this.sequenceHelper = createSequenceHelper();
		this.typeHelper = createTypeHelper();
		this.listStructure = createListStructure();
		this.filterHelper = createFilterHelper();	

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
	}
	
	protected SequenceHelper createSequenceHelper() {
		return new SequenceHelper(getConfiguration());
	}	
	protected TypeHelper createTypeHelper() {
		return new TypeHelper(getL10nCtx().getLocale());
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
		propagateListDataProviderWithOrderInfo(getOrderInfo());
		propagateListDataProviderWithFilter(getFilterInfo());		
		this.dataProvider.init();
	}
	
	/**
	 * Destoys the list and contained data provider and filter form.
	 * @throws Exception 
	 */
	protected void destroy() throws Exception {
		log.debug("Destroying ListWidget.");
		dataProvider.destroy();
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
		if (this.dataProvider != null) {
			refreshCurrentItemRange();

			//Making the requestId to row mapping
			requestIdToRow.clear();
			for (ListIterator i = itemRange.listIterator(); i.hasNext();) {
				Object row = i.next();                  
				requestIdToRow.put(Integer.toString(i.previousIndex()), row);
			}
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
	 * Handles filtering.
	 */
	protected void filter() throws Exception {
		log.debug("Converting and validating FilterForm");
		if (form.convertAndValidate() && form.isStateChanged()) {

			log.debug("Reading FilterInfo");
			MapFormReader mapFormReader = new MapFormReader(form);
			Map filterInfo = mapFormReader.getMap();
			log.debug("FilterInfo: " + filterInfo);

			propagateListDataProviderWithFilter(filterInfo);

			form.markBaseState();
			sequenceHelper.setCurrentPage(0);
		}         
	}

	/**
	 * Handles filter clearing. 
	 */
	protected void clearFilter() {
		clearForm(form);
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
	
	protected void assertInitialized() throws IllegalStateException {
		if (!isInitialized()) {
			throw new IllegalStateException("ListWidget is not initialized");
		}
	}
}
