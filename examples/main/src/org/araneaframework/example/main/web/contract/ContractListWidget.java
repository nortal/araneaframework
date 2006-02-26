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

package org.araneaframework.example.main.web.contract;

import java.util.List;

import org.apache.log4j.Logger;
import org.araneaframework.OutputData;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.util.ServletUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;


/**
 * This widget is for listing contracts.
 * It returns selected contract's Id or cancels current call.
 * It also allows a user to add or remove contracts if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractListWidget extends BaseWidget {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Logger.getLogger(ContractListWidget.class);
	
	private boolean editMode = false;
	
	private ListWidget list;
	
	public ContractListWidget() {
		super();
	}
	
	/**
	 * @param editMode whether to allow add or remove persons.
	 */
	public ContractListWidget(boolean editMode) {
		this.editMode = editMode;
	}
	
	protected void init() throws Exception {
		super.init();
		setViewSelector("contract/contractList");
		log.debug("TemplateContractListWidget init called");    
		addGlobalEventListener(new ProxyEventListener(this));
		
		this.list = initList();
		addWidget("contractList", this.list);
		
		putViewData("allowAdd", new Boolean(editMode));    
		putViewData("allowRemove", new Boolean(editMode));
	}
	
	protected ListWidget initList() throws Exception {
		ListWidget temp = new ListWidget();
		temp.setListDataProvider(new TemplateContractListDataProvider());
		temp.addListColumn("id", "#Id");
		temp.addListColumn("company", "#Company");
		temp.addListColumn("person", "#Person");
		temp.addListColumn("notes", "#Notes");
		return temp;
	}
	
	private void refreshList() throws Exception {  	
		this.list.getListDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		log.debug("Event 'add' received!");
		if (!editMode) {
			throw new RuntimeException("Event 'add' shoud be called only in edit mode");
		}
		getFlowCtx().start(new ContractEditWidget(), null, new FlowContext.Handler() {
			public void onFinish(Object returnValue) throws Exception {
				log.debug("Contract added with Id of " + returnValue + " sucessfully");    
				refreshList();
			}
			public void onCancel() throws Exception {
			}
		});
	}
	
	public void handleEventRemove(String eventParameter) throws Exception {
		log.debug("Event 'remove' received!");
		if (!editMode) {
			throw new RuntimeException("Event 'remove' shoud be called only in edit mode");
		}
		Long id = ((ContractMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getGeneralDAO().remove(ContractMO.class, id);
		refreshList();
		log.debug("Contract with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventSelect(String eventParameter) throws Exception {
		log.debug("Event 'select' received!");
		Long id = ((ContractMO) this.list.getRowFromRequestId(eventParameter)).getId();
		log.debug("Contract selected with Id of " + id);
		getFlowCtx().finish(id);
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		log.debug("Event 'cancel' received!");
		getFlowCtx().cancel();
	}  
	
	private class TemplateContractListDataProvider extends MemoryBasedListDataProvider {
		private static final long serialVersionUID = 1L;
		
		protected TemplateContractListDataProvider() {
			super(ContractMO.class);
		}
		public List loadData() throws Exception {		
			return getGeneralDAO().getAll(ContractMO.class);
		}  	
	}
}
