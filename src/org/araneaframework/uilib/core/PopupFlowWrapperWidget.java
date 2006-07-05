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


package org.araneaframework.uilib.core;

import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.filter.StandardPopupFilterWidget.StandardPopupServiceInfo;
import org.araneaframework.servlet.service.WindowClosingService;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class PopupFlowWrapperWidget extends StandardWidget implements
		FlowContext {
	Widget child;

	public PopupFlowWrapperWidget(Widget child) {
		this.child = child;
	}
	
	protected Environment getChildWidgetEnvironment() {
		return new StandardEnvironment(getEnvironment(), FlowContext.class, this);
	}

	protected void init() throws Exception {
		super.init();

		addWidget("child", child);
	}

	public void start(Widget flow, Configurator configurator, Handler handler) {
		// test
		
		if (getLocalFlowContext() == getOpenerFlowContext()) {
			System.out.println("indeed");
		}
		
		getLocalFlowContext().start(flow, configurator, handler);
	}

	public void replace(Widget flow, Configurator configurator) {
		// XXX
		getLocalFlowContext().replace(flow, configurator);
		// throw AraneaRuntimeException();
	}

	public void finish(Object result) {
		// PopupWindowContext popupCtx = (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
		ThreadContext threadCtx = (ThreadContext) getEnvironment().getEntry(ThreadContext.class);
		TopServiceContext topCtx = (TopServiceContext) getEnvironment().getEntry(TopServiceContext.class);
		getOpenerFlowContext().finish(result);
		try {
          // on close, we shall redirect to page  that closes current popup window and reloads parent.
		  getOpenerPopupContext().close(threadCtx.getCurrentId().toString());

		  String randomId = RandomStringUtils.randomAlphanumeric(12);
		  threadCtx.addService(randomId, new WindowClosingService());

		  StandardPopupServiceInfo temp = 
			  new StandardPopupServiceInfo(
					  (String)topCtx.getCurrentId(), 
					  randomId, 
					  (PopupWindowProperties)null, 
					  ((ServletInputData)getCurrentInput()).getRequest().getRequestURL().toString());

		  ((ServletOutputData) getCurrentOutput()).getResponse().sendRedirect(temp.toURL());
		} catch (Exception e) {
          ExceptionUtil.uncheckException(e);
		}
	}

	public void cancel() {
		getOpenerFlowContext().cancel();
	}

	public boolean isNested() {
		return getLocalFlowContext().isNested();
	}

	public void reset(EnvironmentAwareCallback callback) {
		getLocalFlowContext().reset(callback);
	}

	public FlowReference getCurrentReference() {
		return getLocalFlowContext().getCurrentReference();
	}

	public void addNestedEnvironmentEntry(CustomWidget scope, Object entryId, Object envEntry) {
		getLocalFlowContext().addNestedEnvironmentEntry(scope, entryId,
				envEntry);
	}

	protected void render(OutputData output) throws Exception {
		output.pushScope("child");

		try {
			child._getWidget().render(output);
		} finally {
			output.popScope();
		}
	}
	
	private FlowContext getLocalFlowContext() {
		return (FlowContext) getEnvironment().getEntry(FlowContext.class);
	}
	
	private FlowContext getOpenerFlowContext() {
		PopupWindowContext popupCtx = (PopupWindowContext) getEnvironment()
				.getEntry(PopupWindowContext.class);
		// XXX
		return (FlowContext) ((CustomWidget) popupCtx.getOpener())
				.getChildEnvironment().getEntry(FlowContext.class);
	}
	
	protected PopupWindowContext getPopupContext() {
		return (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
	}

	private PopupWindowContext getOpenerPopupContext() {
		return (PopupWindowContext)((CustomWidget)getPopupContext().getOpener()).getChildEnvironment().getEntry(PopupWindowContext.class);
	}
}