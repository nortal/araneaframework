/*
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
 */

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
import org.araneaframework.example.main.web.company.CompanyListWidget;
import org.araneaframework.example.main.web.contract.ContractAddEditWidget;
import org.araneaframework.example.main.web.contract.ContractListWidget;
import org.araneaframework.example.main.web.demo.DemoAutomaticFormElement;
import org.araneaframework.example.main.web.demo.DemoCheckboxList;
import org.araneaframework.example.main.web.demo.DemoComplexForm;
import org.araneaframework.example.main.web.demo.DemoDisplayForm;
import org.araneaframework.example.main.web.demo.DemoDisplayableEditableList;
import org.araneaframework.example.main.web.demo.DemoEmbeddedDisplayableEditableList;
import org.araneaframework.example.main.web.demo.DemoFileUpload;
import org.araneaframework.example.main.web.demo.DemoFlowEventConfirmationWidget;
import org.araneaframework.example.main.web.demo.DemoFormList;
import org.araneaframework.example.main.web.demo.DemoInMemoryEditableList;
import org.araneaframework.example.main.web.demo.DemoMultiSelect;
import org.araneaframework.example.main.web.demo.DemoOnChangeListenersWidget;
import org.araneaframework.example.main.web.demo.DemoRadioSelect;
import org.araneaframework.example.main.web.demo.DemoRichTextForm;
import org.araneaframework.example.main.web.demo.FilteredInputDemoWidget;
import org.araneaframework.example.main.web.list.MultiListWidget;
import org.araneaframework.example.main.web.list.SimpleSubBeanListWidget;
import org.araneaframework.example.main.web.misc.AjaxRequestErrorWidget;
import org.araneaframework.example.main.web.misc.EventErrorWidget;
import org.araneaframework.example.main.web.misc.InitErrorWidget;
import org.araneaframework.example.main.web.misc.RedirectingWidget;
import org.araneaframework.example.main.web.misc.RenderErrorWidget;
import org.araneaframework.example.main.web.person.PersonEditableListWidget;
import org.araneaframework.example.main.web.person.PersonListWidget;
import org.araneaframework.example.main.web.popups.DemoAdvancedPopupUsageWidget;
import org.araneaframework.example.main.web.sample.FormComplexConstraintDemoWidget;
import org.araneaframework.example.main.web.sample.SampleActionFormWidget;
import org.araneaframework.example.main.web.sample.SamplePopupWidget;
import org.araneaframework.example.main.web.sample.SimpleBeanFormWidget;
import org.araneaframework.example.main.web.sample.SimpleFormWidget;
import org.araneaframework.example.main.web.testing.ModalDialogTestWidget;
import org.araneaframework.example.main.web.tree.ComplexTreeWidget;
import org.araneaframework.example.main.web.tree.UnsynchronizedTreeWidget;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.MenuItem;
import org.araneaframework.uilib.support.FlowCreator;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MenuWidget extends TemplateMenuWidget {

  private MenuItem araneaMenu;

  public MenuWidget(Widget topWidget) throws Exception {
    super(topWidget);
  }

  protected void init() throws Exception {
    super.init(); // It is necessary here to invoke parent code also.
    addWidget("footer", new FooterWidget());
    addEventListener("logout", new ProxyEventListener(this));
    addEventListener("mainPage", new ProxyEventListener(this));
  }

  public void handleEventLogout() throws Exception {
    (getEnvironment().requireEntry(SecurityContext.class)).logout();
  }

  public void handleEventMainPage() throws Exception {
    reset(null);
    this.menu.clearSelection();
    this.menu.selectMenuItem("AraneaRelease");
  }

  public MenuItem getAraneaMenu() {
    return this.araneaMenu;
  }

  protected MenuItem buildMenu() throws Exception {
    MenuItem result = new MenuItem();

    // Aranea 2.0 features/demos
    // Note that here we retrieve the sub menu and add new items to it.
    this.araneaMenu = result.addMenuItem("AraneaRelease", ReleaseWidget.class);
    this.araneaMenu.addMenuItem("Context_Menus", DemoContextMenuWidget.class);
    this.araneaMenu.addMenuItem("Easy_AJAX_Update_Regions", EasyAJAXUpdateRegionsWidget.class);
    this.araneaMenu.addMenuItem("Cooperative_Form", FriendlyUpdateDemoWidget.class);
    this.araneaMenu.addMenuItem("AutoComplete", DemoAutoCompletionWidget.class);
    this.araneaMenu.addMenuItem("List", SimpleListWidget.class);
    this.araneaMenu.addMenuItem("SimpleEditableList", SimpleInMemoryEditableList.class);
    this.araneaMenu.addMenuItem("Modal_Dialog", ModalDialogDemoWidget.class);
    this.araneaMenu.addMenuItem("Seamless_Validation", SeamlessFormValidationDemoWidget.class);
    this.araneaMenu.addMenuItem("Tabs_Demo", DemoNewTabWidget.class);
    this.araneaMenu.addMenuItem("TreeComponent", SimpleTreeWidget.class);
    this.araneaMenu.addMenuItem("Serverside_Polling", DemoActionPollWidget.class);

    // Management demos:
    // Note that here we refer using the name of the parent menu item to add sub menu items to it.
    result.addMenuItem("Management");
    result.addMenuItem("Management", new MenuItem("Persons"));

    // Example use of simple FlowCreator
    result.addMenuItem("Management.Persons", new MenuItem("View_Add", new FlowCreator() {

      public Widget createFlow() {
        return new PersonListWidget(true);
      }
    }));

    result.addMenuItem("Management.Persons", new MenuItem("Editable_List_Memory", PersonEditableListWidget.Memory.class));
    result.addMenuItem("Management.Persons", new MenuItem("Editable_List_Backend", PersonEditableListWidget.Backend.class));
    result.addMenuItem("Management", new MenuItem("Companies"));
    result.addMenuItem("Management.Companies", new MenuItem("View_Edit", CompanyListWidget.class));
    result.addMenuItem("Management", new MenuItem("Contracts"));
    result.addMenuItem("Management.Contracts", new MenuItem("View_Edit", ContractListWidget.class));
    result.addMenuItem("Management.Contracts", new MenuItem("Add", ContractAddEditWidget.class));

    // The Demos menu:
    MenuItem demosMenu = result.addMenuItem("Demos");
    MenuItem subMenu = null;

    // Simple demos:
    subMenu = demosMenu.addMenuItem("Simple");
    subMenu.addMenuItem("Simple_Form", SimpleFormWidget.class);
    subMenu.addMenuItem("Simple_Bean_Form", SimpleBeanFormWidget.class);
    subMenu.addMenuItem("Search_Form", FormComplexConstraintDemoWidget.class);
    subMenu.addMenuItem("Popup_Example", SamplePopupWidget.class);
    subMenu.addMenuItem("MultiSelect", DemoMultiSelect.class);
    subMenu.addMenuItem("RadioSelect", DemoRadioSelect.class);
    subMenu.addMenuItem("demo_automaticForm_title", DemoAutomaticFormElement.class);

    // Advanced demos:
    subMenu = demosMenu.addMenuItem("Advanced");
    subMenu.addMenuItem("File_Upload", DemoFileUpload.class);
    subMenu.addMenuItem("Complex_Form", DemoComplexForm.class);
    subMenu.addMenuItem("Rich_Text_Editor", DemoRichTextForm.class);
    subMenu.addMenuItem("Advanced_Popup", DemoAdvancedPopupUsageWidget.class);
    subMenu.addMenuItem("Flow_Navigation_Confirmation", DemoFlowEventConfirmationWidget.class);
    subMenu.addMenuItem("demo_filteredinput", FilteredInputDemoWidget.class);
    subMenu.addMenuItem("OnChangeListeners", DemoOnChangeListenersWidget.class);
    subMenu.addMenuItem("Form_with_Actions", SampleActionFormWidget.class);
    subMenu.addMenuItem("ModalDialogTesting", ModalDialogTestWidget.class);

    // Form lists demos:
    subMenu = demosMenu.addMenuItem("Form_Lists");
    subMenu.addMenuItem("Display_Form", DemoDisplayForm.class);
    subMenu.addMenuItem("Editable_List", DemoFormList.class);
    subMenu.addMenuItem("In_memory_editable_list", DemoInMemoryEditableList.class);
    subMenu.addMenuItem("Editable_checkbox_list", DemoCheckboxList.class);
    subMenu.addMenuItem("Displayable_editable_list", DemoDisplayableEditableList.class);
    subMenu.addMenuItem("Embedded_Form_List", DemoEmbeddedDisplayableEditableList.class);

    // Lists demos:
    subMenu = demosMenu.addMenuItem("Lists");
    subMenu.addMenuItem("Contacts_SubBeanList", SimpleSubBeanListWidget.class);
    subMenu.addMenuItem("Multi_List", MultiListWidget.class);

    // Trees demos:
    subMenu = demosMenu.addMenuItem("Trees");
    subMenu.addMenuItem("Simple_Tree", SimpleTreeWidget.class);
    subMenu.addMenuItem("Complex_Tree", ComplexTreeWidget.class);
    subMenu.addMenuItem("Tree_with_Unsynchronized_Actions", UnsynchronizedTreeWidget.class);

    // The Misc menu:
    MenuItem errorMenu = result.addMenuItem("Misc");
    errorMenu.addMenuItem("Error_on_init", InitErrorWidget.class);
    errorMenu.addMenuItem("Error_on_event", EventErrorWidget.class);
    errorMenu.addMenuItem("Error_on_render", RenderErrorWidget.class);
    errorMenu.addMenuItem("Error_on_ajax_request", AjaxRequestErrorWidget.class);
    errorMenu.addMenuItem("Redirecting", RedirectingWidget.class);

    return result;
  }

  protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
    if (ExceptionUtils.getRootCause(e) != null) {
      putViewDataOnce("rootStackTrace", ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
    }
    putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e));
    ServletUtil.include("/WEB-INF/jsp/error.jsp", this, output);
  }
}
