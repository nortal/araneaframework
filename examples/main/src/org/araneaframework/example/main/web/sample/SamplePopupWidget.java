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

package org.araneaframework.example.main.web.sample;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.menu.TemplateMenuMessage;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.support.PopupWindowProperties;

public class SamplePopupWidget extends TemplateBaseWidget {
	private static final Logger log = Logger.getLogger(SamplePopupWidget.class);

	protected void init() throws Exception {
		super.init();

		setViewSelector("sample/samplePopup");
	}

	public void handleEventCreateThread() throws Exception {
		getMessageCtx().showInfoMessage("Popup window should have opened. If it did not, please relax your popup blocker settings.");
		
		TemplateMenuMessage message = new TemplateMenuMessage("Samples.Simple_Form");
		
		PopupWindowContext popupCtx = (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
		popupCtx.open("prefix", new PopupWindowProperties(), message);
	}
}
