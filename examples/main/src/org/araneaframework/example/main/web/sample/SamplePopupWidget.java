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

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.message.LoginAndMenuSelectMessage;
import org.araneaframework.example.main.message.PopupMessageFactory;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.servlet.support.PopupWindowProperties;
import org.araneaframework.uilib.core.PopupFlowWidget;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class SamplePopupWidget extends TemplateBaseWidget {
	String title;
	int count = 1;
	
	public SamplePopupWidget() {
	}

	protected SamplePopupWidget(int count) {
		this.count = count;
	}

	protected void init() throws Exception {
		super.init();
		putViewData("title", "#" + Integer.toString(count) + ". Popup Example");
		setViewSelector("sample/samplePopup");
	}

	public void handleEventCreateThread() throws Exception {
		getMessageCtx().showInfoMessage("Popup window should have opened. If it did not, please relax your popup blocker settings.");
		getPopupCtx().open(
        new LoginAndMenuSelectMessage("Demos.#Simple.Simple_Form"), 
        new PopupWindowProperties(), this);
	}

	public void handleEventOpenUrl() throws Exception {
		getMessageCtx().showInfoMessage("Popup window should have opened. If it did not, please relax your popup blocker settings.");
		getPopupCtx().open("http://www.slashdot.org", new PopupWindowProperties());
	}
	
	public void handleEventOpenNewCustomFlow() throws Exception {
		getMessageCtx().showInfoMessage("Popup window should have opened. If it did not, please relax your popup blocker settings.");

		PopupWindowProperties p = new PopupWindowProperties();
		p.setHeight("600");
		p.setWidth("1000");
		p.setScrollbars("yes");
		PopupFlowWidget pfw = new PopupFlowWidget(new NameWidget(), p, new PopupMessageFactory());
		getFlowCtx().start(pfw, null, new SampleHandler());
	}

	public void handleEventEndFlow() {
		getFlowCtx().finish("Funky end for SamplePopupWidget!");
	}
	
	class SampleHandler implements FlowContext.Handler {
		public void onCancel() throws Exception {
		}

		public void onFinish(Object returnValue) throws Exception {
			getFlowCtx().replace(new InvisibleElementFormWidget((String)returnValue), null);
		}
	}
}
