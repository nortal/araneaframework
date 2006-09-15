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
import org.araneaframework.uilib.list.structure.ListColumn;


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
		super.init();
		setViewSelector("contract/contractList");
		log.debug("TemplateContractListWidget init called");    

		this.list = initList();
		addWidget("contractList", this.list);
	}
	
	protected ListWidget initList() throws Exception {
		ListWidget temp = new ListWidget();
		temp.setListDataProvider(new TemplateContractListDataProvider());
		temp.addListColumn("id", "#Id");
		temp.addListColumn("company", "#Company");
		temp.addListColumn("person", "#Person");
		temp.addListColumn("notes", "#Notes");
		temp.addListColumn(new ListColumn("dummy"));
		return temp;
	}
	
	private void refreshList() throws Exception {  	
		this.list.getListDataProvider().refreshData();
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
