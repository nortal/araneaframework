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

package org.araneaframework.example.main.web.list;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.company.CompanyListWidget;
import org.araneaframework.example.main.web.person.PersonListWidget;

/**
 * Widget that shows both person and company lists, demonstrating basic 
 * reusing of widgets in different flows.
 *  
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class MultiListWidget extends TemplateBaseWidget {

	protected void init() throws Exception {
		super.init();
		addWidget("personListWdgt", new PersonListWidget());
		addWidget("companyListWdgt", new CompanyListWidget());
		
		setViewSelector("list/multiList");
	}
	
	// note how we define no events in this widget - all events 
	// that our included components receive are defined by themselves.
}
