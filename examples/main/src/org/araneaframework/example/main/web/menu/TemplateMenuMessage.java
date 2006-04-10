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
