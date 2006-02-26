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

import org.apache.log4j.Logger;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.example.main.web.util.EmptyWidget;
import org.araneaframework.uilib.core.StandardPresentationWidget;

/**
 * This is root widget. It initializes MenuWidget with
 * TemplateEmptyWidget as first element.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class RootWidget extends StandardPresentationWidget {

	private static final Logger log = Logger.getLogger(RootWidget.class);

	protected void init() throws Exception {
		addWidget("menu", new MenuWidget(new EmptyWidget()));
		setViewSelector("root");
		log.debug("Root widget initialized");
    
    throw new RuntimeException();
	}
}
