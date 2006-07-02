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

package org.araneaframework.example.main.web.person;

import java.util.List;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.filter.column.RangeColumnFilter;
import org.araneaframework.uilib.list.structure.filter.column.SimpleColumnFilter;


/**
 * This widget is for listing persons.
 * It returns selected person's Id or cancels current call.
 * It also allows a user to add or remove persons if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class PersonListWidget extends TemplateBaseWidget {
	
	private static final long serialVersionUID = 1L;

	protected static final Logger log = Logger.getLogger(PersonListWidget.class);
	
	private boolean editMode = false;
	private boolean selectOnly = false;
	
	private ListWidget list;
	
	public PersonListWidget() {
		super();
	}
	
	/**
	 * @param editMode whether to allow add or remove persons.
	 */
	public PersonListWidget(boolean editMode) {
		super();
		this.editMode = editMode;
	}
	
	protected void init() throws Exception {
		super.init();
		setViewSelector("person/personList");
		
		this.list = initList();
		addWidget("personList", this.list);
	}
	
	protected ListWidget initList() throws Exception {
		BeanListWidget temp = new BeanListWidget(PersonMO.class);
		temp.setListDataProvider(new TemplatePersonListDataProvider());
		temp.addBeanColumn("id", "#Id", false);
		temp.addBeanColumn("name", "#First name", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("surname", "#Last name", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("phone", "#Phone no", true, new SimpleColumnFilter.Like(), new TextControl());
		
		RangeColumnFilter rangeFilter = new RangeColumnFilter.DateNonStrict();
		temp.addBeanColumn("birthdate", "#Birthdate", true, rangeFilter, null);
		temp.addFilterFormElement(rangeFilter.getStartFilterInfoKey(), "#Birthdate Start", new DateControl(), new DateData());
		temp.addFilterFormElement(rangeFilter.getEndFilterInfoKey(), "#Birthdate End", new DateControl(), new DateData());
		
		// The dummy column without label (in list rows, some listRowLinkButton's will be written there).
		// Needed to write out componentListHeader with correct number of columns. 
		temp.addListColumn(new ListColumn("dummy"));
		return temp;
	}
	
	protected void refreshList() throws Exception {  	
		this.list.getListDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		log.debug("Event 'add' received!");

		getFlowCtx().start(new PersonAddEditWidget(), null, new FlowContext.Handler() {
			private static final long serialVersionUID = 1L;
			
			public void onFinish(Object returnValue) throws Exception {
				log.debug("Person added with Id of " + returnValue + " sucessfully");    
				refreshList();
			}
			public void onCancel() throws Exception {
				// ignore call cancel
			}
		});
	}
	
	public void handleEventRemove(String eventParameter) throws Exception {
		log.debug("Event 'remove' received!");
		if (!this.editMode) {
			throw new RuntimeException("Event 'remove' shoud be called only in edit mode");
		}
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getContractDAO().removeByPersonId(id);
		getGeneralDAO().remove(PersonMO.class, id);
		refreshList();
		log.debug("Person with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventSelect(String eventParameter) throws Exception {
		log.debug("Event 'select' received!");
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		if (!selectOnly) {
			PersonViewWidget newFlow = new PersonViewWidget(id);
			getFlowCtx().start(newFlow, null, null);
		} else {
			getFlowCtx().finish(id);
		}
	}
	
	public void handleEventEdit(String eventParameter) throws Exception {
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		PersonAddEditWidget newFlow = new PersonAddEditWidget(id);

		getFlowCtx().start(newFlow, null, new FlowContext.Handler() {
			private static final long serialVersionUID = 1L;
			
			public void onFinish(Object returnValue) throws Exception {
				refreshList();
			}
			public void onCancel() throws Exception {
			}
		});
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		log.debug("Event 'cancel' received!");
		getFlowCtx().cancel();
	}
	
	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
	}	
	
	private class TemplatePersonListDataProvider extends MemoryBasedListDataProvider {
		protected TemplatePersonListDataProvider() {
			super(PersonMO.class);
		}
		public List loadData() throws Exception {		
			return getGeneralDAO().getAll(PersonMO.class);
		}  	
	}
}
