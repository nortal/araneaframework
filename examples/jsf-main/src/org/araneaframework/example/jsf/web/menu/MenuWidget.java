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

package org.araneaframework.example.jsf.web.menu;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.common.framework.TemplateMenuWidget;
import org.araneaframework.example.jsf.SecurityContext;
import org.araneaframework.example.jsf.web.FooterWidget;
import org.araneaframework.example.jsf.web.demo.DemoAutoCompletionWidget;
import org.araneaframework.example.jsf.web.demo.DemoComplexForm;
import org.araneaframework.example.jsf.web.demo.DemoDisplayForm;
import org.araneaframework.example.jsf.web.demo.DemoFileUpload;
import org.araneaframework.example.jsf.web.demo.DemoMultiSelect;
import org.araneaframework.example.jsf.web.demo.DemoRadioSelect;
import org.araneaframework.example.jsf.web.demo.DemoRichTextForm;
import org.araneaframework.example.jsf.web.jsf.WelcomeJSFWidget;
import org.araneaframework.example.jsf.web.jsf.flowtest.JsfFlowTestWidget;
import org.araneaframework.example.jsf.web.jsf.guessNumber.GuessNumberWidget;
import org.araneaframework.example.jsf.web.jsf.helloDuke.TripleDukeWidget;
import org.araneaframework.example.jsf.web.jsf.helloDuke.HelloDukeWidget;
import org.araneaframework.example.jsf.web.misc.AjaxRequestErrorWidget;
import org.araneaframework.example.jsf.web.misc.EventErrorWidget;
import org.araneaframework.example.jsf.web.misc.InitErrorWidget;
import org.araneaframework.example.jsf.web.misc.RedirectingWidget;
import org.araneaframework.example.jsf.web.misc.RenderErrorWidget;
import org.araneaframework.example.jsf.web.sample.FormComplexConstraintDemoWidget;
import org.araneaframework.example.jsf.web.sample.SamplePopupWidget;
import org.araneaframework.example.jsf.web.sample.SimpleFormWidget;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.MenuItem;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MenuWidget extends TemplateMenuWidget  {
  private static final long serialVersionUID = 1L;

  public MenuWidget(Widget topWidget) throws Exception {
	super(topWidget);
  }

  protected void init() throws Exception {
    super.init();
    
    addWidget("footer", new FooterWidget());
    addEventListener("logout", new ProxyEventListener(this));
    addEventListener("mainPage", new ProxyEventListener(this));
  }
  
  public boolean isNested() {
	return super.isNested() && callStack.size() != 1;
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
		
    // Another way of adding menuitems is available
    MenuItem sampleMenu = result.addMenuItem(new MenuItem("Demos")); {
      sampleMenu.addMenuItem(new MenuItem("Simple"));
      sampleMenu.addMenuItem("Simple", new MenuItem("Simple_Form", SimpleFormWidget.class));
      sampleMenu.addMenuItem("Simple", new MenuItem("Search_Form", FormComplexConstraintDemoWidget.class));
      sampleMenu.addMenuItem("Simple", new MenuItem("Popup_Example", SamplePopupWidget.class));
      sampleMenu.addMenuItem("Simple", new MenuItem("MultiSelect", DemoMultiSelect.class));
      sampleMenu.addMenuItem("Simple", new MenuItem("RadioSelect", DemoRadioSelect.class));
      
      MenuItem advDemos = sampleMenu.addMenuItem(new MenuItem("Advanced"));
      sampleMenu.addMenuItem("Advanced", new MenuItem("File_Upload", DemoFileUpload.class));
      sampleMenu.addMenuItem("Advanced", new MenuItem("Complex_Form", DemoComplexForm.class));
      sampleMenu.addMenuItem("Advanced", new MenuItem("Rich_Text_Editor", DemoRichTextForm.class));
      advDemos.addMenuItem(new MenuItem("AutoComplete", DemoAutoCompletionWidget.class));
      
      MenuItem formListMenu = sampleMenu.addMenuItem(new MenuItem("Form_Lists"));
      formListMenu.addMenuItem(new MenuItem("Display_Form", DemoDisplayForm.class));
    } 
    
    MenuItem errorMenu = result.addMenuItem(new MenuItem("Misc")); {
      errorMenu.addMenuItem(new MenuItem("Error_on_init", InitErrorWidget.class));
      errorMenu.addMenuItem(new MenuItem("Error_on_event", EventErrorWidget.class));
      errorMenu.addMenuItem(new MenuItem("Error_on_render", RenderErrorWidget.class));
      errorMenu.addMenuItem(new MenuItem("Error_on_ajax_request", AjaxRequestErrorWidget.class));
      errorMenu.addMenuItem(new MenuItem("Redirecting", RedirectingWidget.class));
    }   
    
    	result.addMenuItem((String)null, JSFMenu());
		
		return result;
	}

	protected MenuItem JSFMenu() {
		MenuItem root = new MenuItem("JSF");
		root.addMenuItem(new MenuItem("WelcomeJSF", WelcomeJSFWidget.class));
		root.addMenuItem(new MenuItem("Helloduke", HelloDukeWidget.class));
		root.addMenuItem(new MenuItem("DoubleDuke", TripleDukeWidget.class));
		root.addMenuItem(new MenuItem("GuessNumber", GuessNumberWidget.class));
		root.addMenuItem(new MenuItem("#FlowTest", JsfFlowTestWidget.class));
		
		return root;
	}
  
  protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
    if (ExceptionUtils.getRootCause(e) != null) {
      putViewDataOnce("rootStackTrace", 
          ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
    }        
    putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e)); 
    
    ServletUtil.include("/WEB-INF/jsp/menuError.jsp", getEnvironment(), output);
  }
}
