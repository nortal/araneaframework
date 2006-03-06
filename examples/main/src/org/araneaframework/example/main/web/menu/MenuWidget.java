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

package org.araneaframework.example.main.web.menu;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.SecurityContext;
import org.araneaframework.example.main.web.company.CompanyChooseAndEditWidget;
import org.araneaframework.example.main.web.company.CompanyEditWidget;
import org.araneaframework.example.main.web.contract.ContractAddEditWidget;
import org.araneaframework.example.main.web.contract.ContractListWidget;
import org.araneaframework.example.main.web.demo.DemoCheckboxList;
import org.araneaframework.example.main.web.demo.DemoDisplayForm;
import org.araneaframework.example.main.web.demo.DemoDisplayableEditableList;
import org.araneaframework.example.main.web.demo.DemoFormList;
import org.araneaframework.example.main.web.demo.DemoInMemoryEditableList;
import org.araneaframework.example.main.web.error.EventErrorWidget;
import org.araneaframework.example.main.web.error.InitErrorWidget;
import org.araneaframework.example.main.web.error.RenderErrorWidget;
import org.araneaframework.example.main.web.list.SimpleSubBeanListWidget;
import org.araneaframework.example.main.web.person.PersonChooseAndEditWidget;
import org.araneaframework.example.main.web.person.PersonEditableListWidget;
import org.araneaframework.example.main.web.sample.FormComplexConstraintDemoWidget;
import org.araneaframework.example.main.web.sample.SimpleFormWidget;
import org.araneaframework.example.main.web.sample.SimpleListWidget;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.util.ServletUtil;
import org.araneaframework.template.framework.TemplateMenuWidget;
import org.araneaframework.uilib.core.MenuItem;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class MenuWidget extends TemplateMenuWidget  {
	public MenuWidget(Widget topWidget) throws Exception {
		super(topWidget);
	}
  
  protected void init() throws Exception {
    super.init();
    
    addEventListener("logout", new ProxyEventListener(this));
    addEventListener("mainPage", new ProxyEventListener(this));
  }
  
  public void handleEventLogout() throws Exception {
    ((SecurityContext) getEnvironment().requireEntry(SecurityContext.class)).logout();
  }
  
  public void handleEventMainPage() throws Exception {
    reset(null);
    menu.clearSelection();
  }  
	
	protected MenuItem buildMenu() throws Exception {
		MenuItem result = new MenuItem();
		
		result.addMenuItem(null, new MenuItem("Management")); {
			result.addMenuItem("Management", new MenuItem("Persons"));
			result.addMenuItem("Management.Persons", new MenuItem("View_Add", PersonChooseAndEditWidget.class));
			result.addMenuItem("Management.Persons", new MenuItem("Editable_List_Memory", PersonEditableListWidget.Memory.class));
			result.addMenuItem("Management.Persons", new MenuItem("Editable_List_Backend", PersonEditableListWidget.Backend.class));
			
			result.addMenuItem("Management", new MenuItem("Companies"));
			result.addMenuItem("Management.Companies", new MenuItem("Add", CompanyEditWidget.class));
			result.addMenuItem("Management.Companies", new MenuItem("View_Edit", CompanyChooseAndEditWidget.class));
			
			result.addMenuItem("Management", new MenuItem("Contracts"));
			result.addMenuItem("Management.Contracts", new MenuItem("View_Edit", ContractListWidget.class));
			result.addMenuItem("Management.Contracts", new MenuItem("Add", ContractAddEditWidget.class));
		}
		
    // Another way of adding menuitems is available
    MenuItem sampleMenu = result.addMenuItem(new MenuItem("Samples")); {
      sampleMenu.addMenuItem(new MenuItem("Simple_Form", SimpleFormWidget.class));
      sampleMenu.addMenuItem(new MenuItem("Simple_List", SimpleListWidget.class));
      sampleMenu.addMenuItem(new MenuItem("Search_Form", FormComplexConstraintDemoWidget.class));
    }
    
    // Another way of adding menuitems is available
    MenuItem errorMenu = result.addMenuItem(new MenuItem("Error")); {
      errorMenu.addMenuItem(new MenuItem("Error_on_init", InitErrorWidget.class));
      errorMenu.addMenuItem(new MenuItem("Error_on_event", EventErrorWidget.class));
      errorMenu.addMenuItem(new MenuItem("Error_on_render", RenderErrorWidget.class));
    }    
		
		result.addMenuItem(null, new MenuItem("Demos")); {
			result.addMenuItem("Demos", new MenuItem("Display_Form", DemoDisplayForm.class));
			result.addMenuItem("Demos", new MenuItem("Editable_List", DemoFormList.class));
			result.addMenuItem("Demos", new MenuItem("In_memory_editable_list", DemoInMemoryEditableList.class));
			result.addMenuItem("Demos", new MenuItem("Editable_checkbox_list", DemoCheckboxList.class));
			result.addMenuItem("Demos", new MenuItem("Displayable_editable_list", DemoDisplayableEditableList.class));
		}
		
		result.addMenuItem(null, new MenuItem("Lists")); {
			result.addMenuItem("Lists", new MenuItem("Contacts_SubBeanList", SimpleSubBeanListWidget.class));
		}
		
		return result;
	}
  
  protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
    if (ExceptionUtils.getRootCause(e) != null) {
      putViewDataOnce("rootStackTrace", 
          ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
    }        
    putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e)); 
    
    ServletUtil.include("/WEB-INF/jsp/menuError.jsp", getEnvironment(), 
        (ServletOutputData) output);
  }
}
