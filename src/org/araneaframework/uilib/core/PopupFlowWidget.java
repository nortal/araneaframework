package org.araneaframework.uilib.core;

import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.support.PopupWindowProperties;

public class PopupFlowWidget extends StandardWidget {
	protected Message msg;
	protected PopupWindowProperties properties;

	public PopupFlowWidget(Widget widget, PopupWindowProperties properties,
			MessageFactory messageFactory) {
		this.msg = messageFactory.buildMessage(new PopupFlowWrapperWidget(widget));
		this.properties = properties;
	}

	protected void init() throws Exception {
		super.init();

		PopupWindowContext popupCtx = 
			(PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
		popupCtx.open(msg, properties, this);
	}

	public interface MessageFactory {
		public Message buildMessage(Widget rootWidget);
	}
}
