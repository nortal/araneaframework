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

import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.example.main.SecurityContext;
import org.araneaframework.example.main.business.model.UserMO;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * This is root widget, always rendered after user has 'logged on'.
 * It consists only of menu widget. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class RootWidget extends BaseUIWidget implements SecurityContext {
	private static final long serialVersionUID = 1L;
	private MenuWidget menuWidget;
	private Widget topWidget;
	private UserMO activeUser;

	public RootWidget() {}

	public RootWidget(Widget topWidget) {
		this.topWidget = topWidget;
	}
	
	 public RootWidget(UserMO activeUser) {
	    this.activeUser = activeUser;
	  }

	protected void init() throws Exception {
		menuWidget = new MenuWidget(topWidget);
		addWidget("menu", menuWidget);
		setViewSelector("root");
		
		if (topWidget == null && activeUser != null)
			menuWidget.selectMenuItem("RSS");
		topWidget = null;
	}

	protected Environment getChildWidgetEnvironment() throws Exception {
		return new StandardEnvironment(super.getChildWidgetEnvironment(), SecurityContext.class, this);
	}

	public boolean hasPrivilege(String privilege) {
		return false;
	}

	public MenuWidget getMenuWidget() {
		return menuWidget;
	}

	public void logout() {
		getFlowCtx().replace(new LoginWidget(), null);
	}

  public UserMO getUser() {
    return activeUser;
  }
}
