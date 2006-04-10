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

package org.araneaframework.example.main.web.menu;

import org.araneaframework.Component;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.example.main.web.RootWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.template.framework.TemplateMenuWidget;

public class TemplateMenuMessage extends BroadcastMessage {
	private String menuPath;
	
	public TemplateMenuMessage(String menuPath) {
		this.menuPath = menuPath;
	}
	
	protected void execute(Component component) throws Exception{
		if (component instanceof FlowContext && (!(component instanceof TemplateMenuWidget))) {
			final FlowContext ctx = (FlowContext)component;
			ctx.reset(new EnvironmentAwareCallback() {
		        public void call(org.araneaframework.Environment env) throws Exception {
		        	  ctx.start(new RootWidget(), null, null);
		        }
		      });
		}
		
		if (component instanceof RootWidget) {
			RootWidget r = (RootWidget) component;
			r.getMenuWidget().selectMenuItem(menuPath);
		}
	}
}
