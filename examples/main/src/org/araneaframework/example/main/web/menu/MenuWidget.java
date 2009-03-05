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

  private static final long serialVersionUID = 1L;

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
    ((SecurityContext) getEnvironment().requireEntry(SecurityContext.class)).logout();
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

    // Aranea 1.1 features/demos
    // Note that here we retrieve the sub menu and add new items to it.
    this.araneaMenu = result.addMenuItem(new MenuItem("AraneaRelease", ReleaseWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("Context_Menus", DemoContextMenuWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("Easy_AJAX_Update_Regions", EasyAJAXUpdateRegionsWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("Cooperative_Form", FriendlyUpdateDemoWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("AutoComplete", DemoAutoCompletionWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("List", SimpleListWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("SimpleEditableList", SimpleInMemoryEditableList.class));
    this.araneaMenu.addMenuItem(new MenuItem("Modal_Dialog", ModalDialogDemoWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("Seamless_Validation", SeamlessFormValidationDemoWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("Tabs_Demo", DemoNewTabWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("TreeComponent", SimpleTreeWidget.class));
    this.araneaMenu.addMenuItem(new MenuItem("Serverside_Polling", DemoActionPollWidget.class));

    // Management demos:
    // Note that here we refer using the name of the parent menu item to add sub menu items to it.
    result.addMenuItem(new MenuItem("Management"));
    result.addMenuItem("Management", new MenuItem("Persons"));

    // Example use of simple FlowCreator
    result.addMenuItem("Management.Persons", new MenuItem("View_Add",
        new FlowCreator() {

          private static final long serialVersionUID = 1L;

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
    MenuItem demosMenu = result.addMenuItem(new MenuItem("Demos"));
    MenuItem subMenu = null;

    // Simple demos:
    subMenu = demosMenu.addMenuItem(new MenuItem("Simple"));
    subMenu.addMenuItem(new MenuItem("Simple_Form", SimpleFormWidget.class));
    subMenu.addMenuItem(new MenuItem("Simple_Bean_Form", SimpleBeanFormWidget.class));
    subMenu.addMenuItem(new MenuItem("Search_Form", FormComplexConstraintDemoWidget.class));
    subMenu.addMenuItem(new MenuItem("Popup_Example", SamplePopupWidget.class));
    subMenu.addMenuItem(new MenuItem("MultiSelect", DemoMultiSelect.class));
    subMenu.addMenuItem(new MenuItem("RadioSelect", DemoRadioSelect.class));
    subMenu.addMenuItem(new MenuItem("demo_automaticForm_title", DemoAutomaticFormElement.class));

    // Advanced demos:
    subMenu = demosMenu.addMenuItem(new MenuItem("Advanced"));
    subMenu.addMenuItem(new MenuItem("File_Upload", DemoFileUpload.class));
    subMenu.addMenuItem(new MenuItem("Complex_Form", DemoComplexForm.class));
    subMenu.addMenuItem(new MenuItem("Rich_Text_Editor", DemoRichTextForm.class));
    subMenu.addMenuItem(new MenuItem("Advanced_Popup", DemoAdvancedPopupUsageWidget.class));
    subMenu.addMenuItem(new MenuItem("Flow_Navigation_Confirmation", DemoFlowEventConfirmationWidget.class));
    subMenu.addMenuItem(new MenuItem("demo_filteredinput", FilteredInputDemoWidget.class));
    subMenu.addMenuItem(new MenuItem("OnChangeListeners", DemoOnChangeListenersWidget.class));
    subMenu.addMenuItem(new MenuItem("Form_with_Actions", SampleActionFormWidget.class));
    subMenu.addMenuItem(new MenuItem("ModalDialogTesting", ModalDialogTestWidget.class));

    // Form lists demos:
    subMenu = demosMenu.addMenuItem(new MenuItem("Form_Lists"));
    subMenu.addMenuItem(new MenuItem("Display_Form", DemoDisplayForm.class));
    subMenu.addMenuItem(new MenuItem("Editable_List", DemoFormList.class));
    subMenu.addMenuItem(new MenuItem("In_memory_editable_list", DemoInMemoryEditableList.class));
    subMenu.addMenuItem(new MenuItem("Editable_checkbox_list", DemoCheckboxList.class));
    subMenu.addMenuItem(new MenuItem("Displayable_editable_list", DemoDisplayableEditableList.class));
    subMenu.addMenuItem(new MenuItem("Embedded_Form_List", DemoEmbeddedDisplayableEditableList.class));

    // Lists demos:
    subMenu = demosMenu.addMenuItem(new MenuItem("Lists"));
    subMenu.addMenuItem(new MenuItem("Contacts_SubBeanList", SimpleSubBeanListWidget.class));
    subMenu.addMenuItem(new MenuItem("Multi_List", MultiListWidget.class));

    // Trees demos:
    subMenu = demosMenu.addMenuItem(new MenuItem("Trees"));
    subMenu.addMenuItem(new MenuItem("Simple_Tree", SimpleTreeWidget.class));
    subMenu.addMenuItem(new MenuItem("Complex_Tree", ComplexTreeWidget.class));
    subMenu.addMenuItem(new MenuItem("Tree_with_Unsynchronized_Actions", UnsynchronizedTreeWidget.class));

    // The Misc menu:
    MenuItem errorMenu = result.addMenuItem(new MenuItem("Misc"));
    errorMenu.addMenuItem(new MenuItem("Error_on_init", InitErrorWidget.class));
    errorMenu.addMenuItem(new MenuItem("Error_on_event", EventErrorWidget.class));
    errorMenu.addMenuItem(new MenuItem("Error_on_render", RenderErrorWidget.class));
    errorMenu.addMenuItem(new MenuItem("Error_on_ajax_request", AjaxRequestErrorWidget.class));
    errorMenu.addMenuItem(new MenuItem("Redirecting", RedirectingWidget.class));

    return result;
  }

  protected void renderExceptionHandler(OutputData output, Exception e)
      throws Exception {
    if (ExceptionUtils.getRootCause(e) != null) {
      putViewDataOnce("rootStackTrace",
          ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
    }
    putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e));
    ServletUtil.include("/WEB-INF/jsp/error.jsp", this, output);
  }
}
