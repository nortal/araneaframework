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

package org.araneaframework.example.main.release.demos;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.release.features.ExampleData;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ClientViewWidget extends TemplateBaseWidget {
	private ExampleData.Client client;
	
	public ClientViewWidget(ExampleData.Client client) {
		this.client = client;
	}

	protected void init() throws Exception {
		setViewSelector("release/demos/clientView");

		BeanFormWidget form = new BeanFormWidget(ExampleData.Client.class);
		form.addBeanElement("sex", "sed.Sex", new TextControl(), true);
		form.addBeanElement("forename", "sed.Forename", new TextControl(), true);
		form.addBeanElement("surname", "sed.Surname", new TextControl(),  true);
		form.addBeanElement("country", "common.Country", new TextControl(), true);
		
		form.readFromBean(client);
		
		addWidget("form", form);
	}

	public ExampleData.Client getClient() {
		return client;
	}
	
	private void handleEventReturn() {
		getFlowCtx().cancel();
	}
}
