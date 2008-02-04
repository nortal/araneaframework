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
import org.araneaframework.example.common.framework.TemplateMenuWidget;
import org.araneaframework.example.main.SecurityContext;
import org.araneaframework.example.main.release.ReleaseWidget;
import org.araneaframework.example.main.release.demos.DemoActionPollWidget;
import org.araneaframework.example.main.release.demos.DemoAutoCompletionWidget;
import org.araneaframework.example.main.release.demos.DemoContextMenuWidget;
import org.araneaframework.example.main.release.demos.DemoNewTabWidget;
import org.araneaframework.example.main.release.demos.FriendlyUpdateDemoWidget;
import org.araneaframework.example.main.release.demos.ModalDialogDemoWidget;
import org.araneaframework.example.main.release.demos.SimpleTreeWidget;
import org.araneaframework.example.main.release.features.EasyAJAXUpdateRegionsWidget;
import org.araneaframework.example.main.release.features.SeamlessFormValidationDemoWidget;
import org.araneaframework.example.main.release.features.SimpleInMemoryEditableList;
import org.araneaframework.example.main.release.features.SimpleListWidget;
import org.araneaframework.example.main.web.FooterWidget;
import org.araneaframework.example.main.web.course.RSSFeedReaderWidget;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.MenuItem;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MenuWidget extends TemplateMenuWidget  {
  private static final long serialVersionUID = 1L;
private MenuItem araneaMenu;

  public MenuWidget(Widget topWidget) throws Exception {
	super(topWidget);
  }

  protected void init() throws Exception {
    super.init();
    
    if (getSecurityContext().getUser() != null)
      getMenu().addMenuItem(new MenuItem("RSS", RSSFeedReaderWidget.class));
    
    addWidget("footer", new FooterWidget());
    addEventListener("logout", new ProxyEventListener(this));
    addEventListener("mainPage", new ProxyEventListener(this));
  }
  
  public void handleEventLogout() throws Exception {
    getSecurityContext().logout();
  }

  private SecurityContext getSecurityContext() {
    return ((SecurityContext) getEnvironment().requireEntry(SecurityContext.class));
  }
  
  public void handleEventMainPage() throws Exception {
    reset(null);
    menu.clearSelection();
  }  
  
  public MenuItem getAraneaMenu() {
	  return araneaMenu;
  }
	
	protected MenuItem buildMenu() throws Exception {
		MenuItem result = new MenuItem();
		araneaMenu = result.addMenuItem(null, new MenuItem("Aranea_1_1", ReleaseWidget.class));
		// Aranea 1.1 features/demos
		araneaMenu.addMenuItem(new MenuItem("Context_Menus", DemoContextMenuWidget.class));
		araneaMenu.addMenuItem(new MenuItem("Easy_AJAX_Update_Regions", EasyAJAXUpdateRegionsWidget.class));
		araneaMenu.addMenuItem(new MenuItem("Cooperative_Form", FriendlyUpdateDemoWidget.class));
		araneaMenu.addMenuItem(new MenuItem("AutoComplete", DemoAutoCompletionWidget.class));
		araneaMenu.addMenuItem(new MenuItem("List", SimpleListWidget.class));
		araneaMenu.addMenuItem(new MenuItem("SimpleEditableList", SimpleInMemoryEditableList.class));
		araneaMenu.addMenuItem(new MenuItem("Modal_Dialog", ModalDialogDemoWidget.class));
		araneaMenu.addMenuItem(new MenuItem("Seamless_Validation", SeamlessFormValidationDemoWidget.class));
		araneaMenu.addMenuItem(new MenuItem("Tabs_Demo", DemoNewTabWidget.class));
		araneaMenu.addMenuItem(new MenuItem("TreeComponent", SimpleTreeWidget.class));
		araneaMenu.addMenuItem(new MenuItem("Serverside_Polling", DemoActionPollWidget.class));


		return result;
	}
  
  protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
    if (ExceptionUtils.getRootCause(e) != null) {
      putViewDataOnce("rootStackTrace", 
          ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
    }        
    putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e)); 
    
    ServletUtil.include("/WEB-INF/jsp/menuError.jsp", this, output);
  }
}
