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

import java.util.List;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.UserMO;
import org.araneaframework.example.main.web.course.RegistrationWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * This is login widget. After receiving "successful" login event, it
 * replaces itself on the call stack with real application.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class LoginWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  /* Widget we will create and attach to this widget. */
	private FormWidget form;
	private String initMsg;
	
	public LoginWidget(){}
	
	
	public LoginWidget(String message)
	{
	  this.initMsg = message;
	}

	protected void init() throws Exception {
		/* Sets the view selector that will be used for rendering this widget. 
		 * The path to real JSP file is determined by:
		 * StandardJspFilterService field jspPath (configured in aranea-conf.xml) + 
		 * viewselector + 
		 * ".jsp" */
		setViewSelector("login");
		
		/* Register a global proxying eventlistener - it receives all widget events and upon 
		 * receiving event named "someEvent" proxies it to "handleEventSomeEvent" method 
     * 
     * This listener is added by default in super class and is only shown here for
     * illustrative purposes. It can also be overridden on need.
     */
	    setGlobalEventListener(new ProxyEventListener(this));

		/* Create a new FormWidget with two self-described input fields. */
		form = new FormWidget();
		// Add the input fields. Arguments taken by addElement() :  
		// String elementName, String labelId, Control control, Data data, boolean mandatory
		form.addElement("username", "#User", new TextControl(), new StringData(), false);
		form.addElement("password", "#Password", new TextControl(), new StringData(), false);

		// attach created form to our widget. 
		addWidget("loginForm", form);
		
		if (initMsg != null) getMessageCtx().showInfoMessage(initMsg);
	}
	
	// implementation of the login handler
	private void handleEventLogin() throws Exception {
		if (form.convertAndValidate()) {
			String username = (String) form.getValueByFullName("username");
			String password = (String) form.getValueByFullName("password");
			
			List l  = getUserDAO().findByName(username);
			
			if (l.isEmpty()) {
			  getMessageCtx().showErrorMessage("No such user '" + username + "'");
			  return;
			}
			
			UserMO user = (UserMO) l.iterator().next();
			
			if (user.getPassword().equals(password)) {
			  getFlowCtx().replace(new RootWidget(user));
			  return;
			}

			getMessageCtx().showErrorMessage("User '" + username + "'" + " not allowed to log in with password '" + password + "'");
		}
	}
	
	private void handleEventRegisterAccount() throws Exception {
		getFlowCtx().replace(new RegistrationWidget(), null);
	}
}
