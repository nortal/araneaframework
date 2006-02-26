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

package org.araneaframework.example.main.web.person;

import org.apache.log4j.Logger;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;


/**
 * This widget is for displaying a person.
 * It cancels current call only.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>*
 */
public class PersonViewWidget extends BaseWidget {
	
	private static final Logger log = Logger.getLogger(PersonViewWidget.class);
	
	private Long id = null;
	
	/**
	 * @param id Person's Id.
	 */
	public PersonViewWidget(Long id) {
		super();
		this.id = id;
	}
	
	protected void init() throws Exception {
		super.init();
		log.debug("TemplatePersonViewWidget init called");
		setViewSelector("person/personView");
		addGlobalEventListener(new ProxyEventListener(this));
		
		putViewData("person" , getGeneralDAO().getById(PersonMO.class, id));
	}
	

	public void handleEventReturn(String eventParameter) throws Exception {
		log.debug("Event 'return' received!");
		getFlowCtx().cancel();
	}	
}
