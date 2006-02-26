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

package org.araneaframework.example.main.web.company;

import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.structure.filter.column.SimpleColumnFilter;


/**
 * This widget is for listing companies.
 * It returns selected company's Id or cancels current call.
 * It also allows a user to add or remove companies if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class CompanyListWidget extends BaseWidget {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Logger.getLogger(CompanyListWidget.class);
	
	private boolean editMode = false;
	
	private ListWidget list;
	
	public CompanyListWidget() {
		super();
	}
	
	/**
	 * @param editMode whether to allow add or remove persons.
	 */
	public CompanyListWidget(boolean editMode) {
		super();
		this.editMode = editMode;
	}
	
	protected void init() throws Exception {
		super.init();
		setViewSelector("company/companyList");
		log.debug("TemplateCompanyListWidget init called");    
		addGlobalEventListener(new ProxyEventListener(this));
		
		this.list = initList();
		addWidget("companyList", this.list);
		
		putViewData("allowAdd", new Boolean(editMode));    
		putViewData("allowRemove", new Boolean(editMode));
	}
	
	protected ListWidget initList() throws Exception {
		BeanListWidget temp = new BeanListWidget(CompanyMO.class);
		temp.setListDataProvider(new TemplateCompanyListDataProvider());
		temp.addBeanColumn("id", "#Id", false);
		temp.addBeanColumn("name", "#Name", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("address", "#Address", true, new SimpleColumnFilter.Like(), new TextControl());
		return temp;
	}
	
	private void refreshList() throws Exception {  	
		this.list.getListDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		log.debug("Event 'add' received!");
		if (!this.editMode) {
			throw new RuntimeException("Event 'add' shoud be called only in edit mode");
		}
		getFlowCtx().start(new CompanyEditWidget(), null, new FlowContext.Handler() {
			public void onFinish(Object returnValue) throws Exception {
				log.debug("Company added with Id of " + returnValue + " sucessfully");    
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
		Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getGeneralDAO().remove(CompanyMO.class, id);
		refreshList();
		log.debug("Company with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventSelect(String eventParameter) throws Exception {
		log.debug("Event 'select' received!");
		Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
		log.debug("Company selected with Id of " + id);
		getFlowCtx().finish(id);
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		log.debug("Event 'cancel' received!");
		getFlowCtx().cancel();
	}  
	
	private class TemplateCompanyListDataProvider extends MemoryBasedListDataProvider {
		private static final long serialVersionUID = 1L;
		
		protected TemplateCompanyListDataProvider() {
			super(CompanyMO.class);
		}
		public List loadData() throws Exception {		
			return getGeneralDAO().getAll(CompanyMO.class);
		}  	
	}
}
