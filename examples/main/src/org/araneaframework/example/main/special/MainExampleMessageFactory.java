package org.araneaframework.example.main.special;

import java.io.Serializable;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.example.main.web.RootWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.core.PopupFlowWidget.MessageFactory;

public class MainExampleMessageFactory implements MessageFactory, Serializable {
	public Message buildMessage(Widget widget) {
		return new MainExampleMessage(widget);
	}
	
	public class MainExampleMessage implements Message {
		Widget flow;
		
		public MainExampleMessage(Widget flow) {
			this.flow = flow;
		}
		
		public final void send(Object id, Component component){
			if (!(component instanceof FlowContext)) {
				component._getComponent().propagate(this);
			}
			else {
				try {
					this.execute(component);
				}
				catch (Exception e) {
					throw ExceptionUtil.uncheckException(e);
				}
			}
		}

		protected void execute(Component component) throws Exception {
			final FlowContext fCtx = (FlowContext) component;
			
			fCtx.reset(new EnvironmentAwareCallback() {
				public void call(Environment env) throws Exception {
					FlowContext f = (FlowContext)env.getEntry(FlowContext.class);
					if (flow != null)
						f.start(new RootWidget(flow), null, null);
				}
			});
		}
	}
}
