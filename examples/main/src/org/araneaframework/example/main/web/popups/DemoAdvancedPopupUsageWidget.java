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

package org.araneaframework.example.main.web.popups;

import org.araneaframework.example.main.TemplateBaseWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoAdvancedPopupUsageWidget extends TemplateBaseWidget {
	private PersonEditableListPopupWidget editableList;
	private PersonEditableListPopupWidget anotherEditableList;
	private PersonEditableListPopupWidget yetAnotherEditableList;

	protected void init() throws Exception {
		setViewSelector("demo/advancedPopups");
		editableList = new PersonEditableListPopupWidget();

		anotherEditableList = new PersonEditableListPopupWidget();
		anotherEditableList.setUsePopupFlow(false);

		yetAnotherEditableList = new PersonEditableListPopupWidget();
		yetAnotherEditableList.setUsePopupFlow(false);
		yetAnotherEditableList.setUseAction(true);

		addWidget("editableList", editableList);
		addWidget("anotherEditableList", anotherEditableList);
		addWidget("yanotherEditableList", yetAnotherEditableList);
	}
}
