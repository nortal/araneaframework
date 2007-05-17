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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;


/**
 * This widget is for listing persons.
 * It returns selected person's Id or cancels current call.
 * It also allows a user to add or remove persons if it's set to edit mode.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class PersonListWidget extends TemplateBaseWidget {
	
	private static final long serialVersionUID = 1L;

	protected static final Log log = LogFactory.getLog(PersonListWidget.class);
	
	private boolean editMode = false;
	private boolean selectOnly = false;
  
  private IContractDAO contractDAO;
	
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
		setViewSelector("person/personList");		
		initList();
	}
	
	protected void initList() throws Exception {
		this.list = new BeanListWidget(PersonMO.class);
		list.setDataProvider(new TemplatePersonListDataProvider());
		list.addField("id", "#Id");
		list.setOrderableByDefault(true);
		list.addField("name", "#First name").like();
		list.addField("surname", "#Last name").setIgnoreCase(false).like();
		list.addField("phone", "#Phone no").like();
		list.addField("birthdate", "#Birthdate").range();
		list.addField("salary", "#Salary").range();
		
		list.setInitialOrder("name", true);
		
		// The dummy column without label (in list rows, some listRowLinkButton's will be written there).
		// Needed to write out componentListHeader with correct number of columns. 
		list.addField("dummy", null, false);

		addWidget("personList", this.list);		
	}
	
	protected void refreshList() throws Exception {  	
		this.list.getDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		getFlowCtx().start(new PersonAddEditWidget(), new FlowContext.Handler() {
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
		if (!this.editMode) {
			throw new RuntimeException("Event 'remove' shoud be called only in edit mode");
		}
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
    contractDAO.removeByPersonId(id);
		getGeneralDAO().remove(PersonMO.class, id);
		refreshList();
		log.debug("Person with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventSelect(String eventParameter) throws Exception {
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		if (!selectOnly) {
			PersonViewWidget newFlow = new PersonViewWidget(id);
			getFlowCtx().start(newFlow);
		} else {
			getFlowCtx().finish(id);
		}
	}
	
	public void handleEventEdit(String eventParameter) throws Exception {
		Long id = ((PersonMO) this.list.getRowFromRequestId(eventParameter)).getId();
		PersonAddEditWidget newFlow = new PersonAddEditWidget(id);

		getFlowCtx().start(newFlow, new FlowContext.Handler() {
			private static final long serialVersionUID = 1L;
			
			public void onFinish(Object returnValue) throws Exception {
				refreshList();
			}
			public void onCancel() throws Exception {
			}
		});
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		getFlowCtx().cancel();
	}
	
	public void setSelectOnly(boolean selectOnly) {
		this.selectOnly = selectOnly;
	}	
	
	private class TemplatePersonListDataProvider extends MemoryBasedListDataProvider {
		    private static final long serialVersionUID = 1L;
    protected TemplatePersonListDataProvider() {
			super(PersonMO.class);
		}
		public List loadData() throws Exception {		
			return getGeneralDAO().getAll(PersonMO.class);
		}  	
	}
  
  public void injectContractDAO(IContractDAO contractDAO) {
    this.contractDAO = contractDAO;
  }
}
