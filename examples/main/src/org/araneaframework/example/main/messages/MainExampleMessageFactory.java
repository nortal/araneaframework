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


package org.araneaframework.example.main.messages;

import java.io.Serializable;
import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.example.main.web.RootWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.core.PopupFlowWidget.MessageFactory;

/**
 * Wrapper around the flow that is started from new session-thread. It pretends
 * to be {@link org.araneaframework.framework.FlowContext} for wrapped flows and proxies
 * method calls current <emphasis>real</emphasis> {@link org.araneaframework.framework.FlowContext} 
 * and to {@link org.araneaframework.framework.FlowContext} that requested starting of wrapped flow.
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
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
			FlowContext fCtx = (FlowContext) component;
			fCtx.start(new RootWidget(flow), null, null);
		}
	}
}
