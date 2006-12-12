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
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.WidgetFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;


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
  private BeanListWidget list;
  private boolean editMode = false;
  
  private WidgetFactory addWidgetFactory; 
  
  private IContractDAO contractDAO;

  public CompanyListWidget() {
    this(true, new WidgetFactory() {
      public Widget buildWidget(Environment env) {
        return new CompanyEditWidget();
      }
    });
  }

  public CompanyListWidget(boolean editMode, WidgetFactory addWidgetFactory) {
    super();
    this.addWidgetFactory = addWidgetFactory;
    this.editMode = editMode;
  }

  protected void init() throws Exception {
    setViewSelector("company/companyList");
    log.debug("TemplateCompanyListWidget init called");    

    initList();
  }

  protected void initList() throws Exception {
    // Create the new list widget whose records are JavaBeans, instances of CompanyMO.
    // CompanyMO has fields named id, name and address.
    list = new BeanListWidget(CompanyMO.class);
    addWidget("companyList", this.list);
    // set the data provider for the list
    list.setDataProvider(new TemplateCompanyListDataProvider());
    // add the displayed columns to list.
    // addField(String id, String label, boolean orderable)
    // note that # before the label means that label is treated as unlocalized and outputted as-is
    list.addField("id", "#Id", false);
    // addField(...) returns FieldFilterHelper, like() sets LIKE filter on the column
    list.addField("name", "#Name", true).like();
    list.addField("address", "#Address", true).like();
    list.addField("dummy", null, false);
  }

  public void refreshList() throws Exception {    
    this.list.getDataProvider().refreshData();
  }
  
  protected void enable() throws Exception {
    refreshList();
  }

  
  
  
  
  
  public void handleEventAdd(String eventParameter) throws Exception {        
    getFlowCtx().start(
        addWidgetFactory.buildWidget(getEnvironment()), 
        null, 
        new CompanyHandler());  
  }
  
  private class CompanyHandler implements FlowContext.Handler {
    public void onFinish(Object returnValue) throws Exception {
      CompanyMO company = (CompanyMO) returnValue;
      getMessageCtx().showInfoMessage("Company '" + company.toString() + "' added!");
      
      getGeneralDAO().add(company);
      refreshList();
    }
    public void onCancel() throws Exception {
    }
  }       

  
  
  
  
  
  public void handleEventRemove(String eventParameter) throws Exception {
    Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
    contractDAO.removeByCompanyId(id);
    getGeneralDAO().remove(CompanyMO.class, id);
    refreshList();
    log.debug("Company with Id of " + id + " removed sucessfully");
  }

  public void handleEventSelect(String eventParameter) throws Exception {
    Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
    log.debug("Company selected with Id of " + id);
    if (editMode)
      getFlowCtx().start(new CompanyEditWidget(id), null, new FlowContext.Handler() {
	        private static final long serialVersionUID = 1L;
      public void onFinish(Object returnValue) throws Exception {
	        log.debug("Company added with Id of " + returnValue + " sucessfully");
	        refreshList();
	      }
	      public void onCancel() throws Exception {
	      }
	    });
    else
      getFlowCtx().finish(id);
  }

  public void handleEventEdit(String eventParameter) throws Exception {
    Long id = ((CompanyMO) this.list.getRowFromRequestId(eventParameter)).getId();
    log.debug("Company selected with Id of " + id);
    getFlowCtx().start(new CompanyEditWidget(id), null, new FlowContext.Handler() {
	      private static final long serialVersionUID = 1L;
      public void onFinish(Object returnValue) throws Exception {
	        log.debug("Company added with Id of " + returnValue + " sucessfully");
	        refreshList();
	      }
	      public void onCancel() throws Exception {
	      }
	    }
    );
  }

  public void handleEventCancel(String eventParameter) throws Exception {
    getFlowCtx().cancel();
  }  

  private class TemplateCompanyListDataProvider extends MemoryBasedListDataProvider {
    private static final long serialVersionUID = 1L;

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
  
  public void injectContractDAO(IContractDAO contractDAO) {
    this.contractDAO = contractDAO;
  }
}
