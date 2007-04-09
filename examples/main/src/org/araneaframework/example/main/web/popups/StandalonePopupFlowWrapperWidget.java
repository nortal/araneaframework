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

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.OutputData;
import org.araneaframework.Service;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.core.PopupFlowWrapperWidget;

/**
 * Similar to {@link PopupFlowWrapperWidget} but this never proxies {@link FlowContext} calls
 * to opening thread's {@link FlowContext} and allows setting the {@link Service} that will
 * be executed upon return from wrapped {@link Widget}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandalonePopupFlowWrapperWidget extends BaseApplicationWidget implements FlowContext {
	private Widget widget;
	private ClientSideReturnService finishingService;
	private Service cancellingService;
	
	public StandalonePopupFlowWrapperWidget(BaseUIWidget widget) {
		this.widget = widget;
	}
	
	public void setFinishService(ClientSideReturnService service) {
		this.finishingService = service;
	}
	
	public ClientSideReturnService getFinishService() {
		return finishingService;
	}
	
	public void setCancelService(Service service) {
		cancellingService = service;
	}
	
	public Service getCancelService() {
		return cancellingService;
	}
	
	protected Environment getChildWidgetEnvironment() throws Exception {
		return new StandardEnvironment(super.getChildWidgetEnvironment(), FlowContext.class, this);
	}

	protected void init() throws Exception {
		addWidget("widget", widget);
	}

	protected void render(OutputData output) throws Exception {
		widget._getWidget().render(output);
	}

	protected FlowContext getFlowCtx() {
		return (FlowContext) getEnvironment().requireEntry(FlowContext.class);
	}

	public void addNestedEnvironmentEntry(ApplicationWidget scope, Object entryId, Object envEntry) {
		getFlowCtx().addNestedEnvironmentEntry(scope, entryId, envEntry);
	}

	public void cancel() {
		ThreadContext threadCtx = getThreadContext();
		TopServiceContext topCtx = getTopServiceContext();
	    try {
	      // close the session-thread serving popupflow
	    	threadCtx.close(threadCtx.getCurrentId());

	      String rndThreadId = RandomStringUtils.randomAlphanumeric(12);
	      Assert.notNull(cancellingService);
	      threadCtx.addService(rndThreadId, cancellingService);
	      ((HttpOutputData) getOutputData()).sendRedirect(getResponseURL(((HttpInputData) getInputData()).getContainerURL(), (String)topCtx.getCurrentId(), rndThreadId));
	    } catch (Exception e) {
	      ExceptionUtil.uncheckException(e);
	    }
	}

	public void finish(Object result) {
		ThreadContext threadCtx = getThreadContext();
		TopServiceContext topCtx = getTopServiceContext();
	    try {
	    	threadCtx.close(threadCtx.getCurrentId());

	      String rndThreadId = RandomStringUtils.randomAlphanumeric(12);

	      finishingService.setResult(result);
	      threadCtx.addService(rndThreadId, finishingService);
	      ((HttpOutputData) getOutputData()).sendRedirect(getResponseURL(((HttpInputData) getInputData()).getContainerURL(), (String)topCtx.getCurrentId(), rndThreadId));
	    } catch (Exception e) {
	      ExceptionUtil.uncheckException(e);
	    }
	}
	
	protected TopServiceContext getTopServiceContext() {
		return (TopServiceContext) getEnvironment().getEntry(TopServiceContext.class);
	}
	
	protected ThreadContext getThreadContext() {
		return (ThreadContext) getEnvironment().getEntry(ThreadContext.class);
	}

	public FlowReference getCurrentReference() {
		return getFlowCtx().getCurrentReference();
	}

	public boolean isNested() {
		return getFlowCtx().isNested();
	}

	public void replace(Widget flow, Configurator configurator) {
	}

	public void reset(EnvironmentAwareCallback callback) {
		throw new IllegalStateException();
	}

	public void start(Widget flow, Configurator configurator, Handler handler) {
		getFlowCtx().start(flow, configurator, handler);
	}
	
	protected String getResponseURL(String url, String topServiceId, String threadServiceId) {
		Map m = new HashMap();
		m.put(TopServiceContext.TOP_SERVICE_KEY, topServiceId);
		m.put(ThreadContext.THREAD_SERVICE_KEY, threadServiceId);
		m.put(TransactionContext.TRANSACTION_ID_KEY, TransactionContext.OVERRIDE_KEY);
		return ((HttpOutputData)getOutputData()).encodeURL(URLUtil.parametrizeURI(url, m));
	}
}
