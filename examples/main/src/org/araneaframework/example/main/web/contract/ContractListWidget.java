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
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;


/**
 * This widget is for listing contracts.
 * It returns selected contract's Id or cancels current call.
 * It also allows a user to add or remove contracts if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractListWidget extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;

  protected static final Logger log = Logger.getLogger(ContractListWidget.class);
	
	private ListWidget list;
	
	public ContractListWidget() {
	}
	
	protected void init() throws Exception {
		setViewSelector("contract/contractList");
		log.debug("TemplateContractListWidget init called");    

		initList();
	}
	
	protected void initList() throws Exception {
		list = new ListWidget();
		list.setDataProvider(new TemplateContractListDataProvider());
		list.addField("id", "#Id");
		list.addField("company", "#Company");
		list.addField("person", "#Person");
		list.addField("notes", "#Notes");
		list.addField("dummy", null, false);
		addWidget("contractList", list);
	}
	
	private void refreshList() throws Exception {  	
		this.list.getDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		getFlowCtx().start(new ContractAddEditWidget(), 
				null, 
				new FlowContext.Handler() {
					          private static final long serialVersionUID = 1L;
          public void onFinish(Object returnValue) throws Exception {
						log.debug("Contract added with Id of " + returnValue + " sucessfully");    
						refreshList();
					}
					public void onCancel() throws Exception {
					}
				}
		);
	}
	
	public void handleEventRemove(String eventParameter) throws Exception {
		Long id = ((ContractMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getGeneralDAO().remove(ContractMO.class, id);
		refreshList();
		log.debug("Contract with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventEdit(String eventParameter) throws Exception {
		Long id = ((ContractMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getFlowCtx().start(new ContractAddEditWidget(id), null, null);
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
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
