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
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.BackendListDataProvider;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.filter.column.SimpleColumnFilter;


/**
 * This widget is for listing companies.
 * It returns selected company's Id or cancels current call.
 * It also allows a user to add or remove companies if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class CompanyListWidget extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(CompanyListWidget.class);
	private ListWidget list;
	
	public CompanyListWidget() {
		super();
	}
	
	protected void init() throws Exception {
		super.init();
		setViewSelector("company/companyList");
		log.debug("TemplateCompanyListWidget init called");    

		this.list = initList();
		addWidget("companyList", this.list);
	}
	
	protected ListWidget initList() throws Exception {
	    // Create the new list widget whose records are JavaBeans, instances of CompanyMO.
		// CompanyMO has fields named id, name and address.
		BeanListWidget temp = new BeanListWidget(CompanyMO.class);
		// set the data provider for the list
		temp.setListDataProvider(new TemplateCompanyListDataProvider());
		// add the displayed columns to list.
		// addBeanColumn(String id, String label, boolean isOrdered)
		// note that # before the label means that label is treated as unlocalized and outputted as-is
		temp.addBeanColumn("id", "#Id", false);
		//addBeanColumn(String id, String label, boolean isOrdered, ColumnFilter filter, Control control)
		temp.addBeanColumn("name", "#Name", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addBeanColumn("address", "#Address", true, new SimpleColumnFilter.Like(), new TextControl());
		temp.addListColumn(new ListColumn("dummy"));
		return temp;
	}
	
	private void refreshList() throws Exception {  	
		this.list.getListDataProvider().refreshData();
	}
	
	public void handleEventAdd(String eventParameter) throws Exception {
		log.debug("Event 'add' received!");
		
		getFlowCtx().start(new CompanyEditWidget(), null, new FlowContext.Handler() {
			public void onFinish(Object returnValue) throws Exception {
				log.debug("Company added with Id of " + returnValue + " sucessfully");
				// trick to refresh the list data when we suspect it has changed
				refreshList();
			}
			public void onCancel() throws Exception {
			}
		});
	}
	
	public void handleEventRemove(String eventParameter) throws Exception {
		log.debug("Event 'remove' received!");
		Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
		getGeneralDAO().remove(CompanyMO.class, id);
		refreshList();
		log.debug("Company with Id of " + id + " removed sucessfully");
	}
	
	public void handleEventSelect(String eventParameter) throws Exception {
		log.debug("Event 'select' received!");
		Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
		log.debug("Company selected with Id of " + id);
		getFlowCtx().start(new CompanyEditWidget(id), null, null);
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		log.debug("Event 'cancel' received!");
		getFlowCtx().cancel();
	}  
	
	private class TemplateCompanyListDataProvider extends MemoryBasedListDataProvider {
        // Overloading constructor with correct bean type.
        protected TemplateCompanyListDataProvider() {
            super(CompanyMO.class);
        }

        // Overloading the real data loading method. Should
        // return java.util.List containing CompanuMO objects.
        public List loadData() throws Exception {
            // Here, database query is performed and all rows from COMPANY table retrieved.
            // But you could also get the data from parsing some XML file, /dev/random etc.
            // All that matters is that returned List really contains CompanyMO objects.
            return getGeneralDAO().getAll(CompanyMO.class);
        }      
    }
	
	private class BackendCompanyListDataProvider extends BackendListDataProvider {
        protected BackendCompanyListDataProvider() {
        	// Constructor with argument useCache set to false.
        	super(false);
        }

		protected ListItemsData getItemRange(ListQuery query) throws Exception {
			return null;
		}
    }
}
