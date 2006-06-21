package org.araneaframework.uilib.core;

import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.servlet.PopupWindowContext;

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
		getLocalFlowContext().start(flow, configurator, handler);
	}

	public void replace(Widget flow, Configurator configurator) {
		// XXX
		getLocalFlowContext().replace(flow, configurator);
		// throw AraneaRuntimeException();
	}

	public void finish(Object result) {
		PopupWindowContext popupCtx = (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
		ThreadContext threadCtx = (ThreadContext) getEnvironment().getEntry(ThreadContext.class);
		getOpenerFlowContext().finish(result);
		try {
		  getOpenerPopupContext().close(threadCtx.getCurrentId().toString());
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