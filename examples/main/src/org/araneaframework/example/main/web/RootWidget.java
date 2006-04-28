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

package org.araneaframework.example.main.web;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.example.main.SecurityContext;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.uilib.core.StandardPresentationWidget;

/**
 * This is root widget. It initializes MenuWidget with
 * TemplateEmptyWidget as first element.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class RootWidget extends StandardPresentationWidget implements SecurityContext {
  MenuWidget menuWidget;

  protected void init() throws Exception {
    menuWidget = new MenuWidget(null);
    addWidget("menu", menuWidget);
    setViewSelector("root");
  }

  protected Environment getChildWidgetEnvironment() throws Exception {
    Map entries = new HashMap();
    entries.put(SecurityContext.class, this);
    return new StandardEnvironment(getEnvironment(), entries);
  }

  public boolean hasPrivilege(String privelege) {
    return false;
  }

  public MenuWidget getMenuWidget() {
    return menuWidget;
  }

  public void logout() throws Exception {
    getFlowCtx().replace(new LoginWidget(), null);
  }
}
