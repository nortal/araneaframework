package org.araneaframework.example.main.web.popups;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.uilib.core.BaseUIWidget;

public class PopupFlowContextWidget extends BaseApplicationWidget implements FlowContext {
	private Widget widget;
	private ClientSideReturnService finishingService;
	
	public PopupFlowContextWidget(BaseUIWidget widget, ClientSideReturnService finishingService) {
		this.widget = widget;
		this.finishingService = finishingService;
	}
	
	protected void init() throws Exception {
		addWidget("widget", widget);
		addNestedEnvironmentEntry(this, FlowContext.class, this);
	}

	protected void render(OutputData output) throws Exception {
		output.pushScope("widget");
		try {
			widget._getWidget().render(output);
		} finally {
			output.popScope();
		}
	}

	protected FlowContext getFlowCtx() {
		return (FlowContext) getEnvironment().requireEntry(FlowContext.class);
	}

	public void addNestedEnvironmentEntry(ApplicationWidget scope, Object entryId, Object envEntry) {
		getFlowCtx().addNestedEnvironmentEntry(scope, entryId, envEntry);
	}

	public void cancel() {
		
	}

	public void finish(Object result) {
		ThreadContext threadCtx = (ThreadContext) getEnvironment().getEntry(ThreadContext.class);
		TopServiceContext topCtx = (TopServiceContext) getEnvironment().getEntry(TopServiceContext.class);
	    try {
	      // close the session-thread serving popupflow
	    	threadCtx.close(threadCtx.getCurrentId());

	      String rndThreadId = RandomStringUtils.randomAlphanumeric(12);
	      // popup window is closed with redirect to a page that closes current window and reloads parent.
	      finishingService.setReturnValue(result);
	      threadCtx.addService(rndThreadId, finishingService);
	      ((HttpOutputData) getOutputData()).sendRedirect(getResponseURL(((HttpInputData) getInputData()).getContainerURL(), (String)topCtx.getCurrentId(), rndThreadId));
	    } catch (Exception e) {
	      ExceptionUtil.uncheckException(e);
	    }
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
	}

	public void start(Widget flow, Configurator configurator, Handler handler) {
		getFlowCtx().start(flow, configurator, handler);
	}
	
	///////
	protected String getResponseURL(String url, String topServiceId, String threadServiceId) {
		Map m = new HashMap();
		m.put(TopServiceContext.TOP_SERVICE_KEY, topServiceId);
		m.put(ThreadContext.THREAD_SERVICE_KEY, threadServiceId);
		m.put(TransactionContext.TRANSACTION_ID_KEY, TransactionContext.OVERRIDE_KEY);
		return ((HttpOutputData)getOutputData()).encodeURL(URLUtil.parametrizeURI(url, m));
	}
}
