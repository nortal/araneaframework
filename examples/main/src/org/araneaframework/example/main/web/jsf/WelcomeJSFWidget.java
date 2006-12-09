package org.araneaframework.example.main.web.jsf;

import org.araneaframework.framework.FlowContext;
import org.araneaframework.integration.jsf.JsfWidget;

public class WelcomeJSFWidget extends JsfWidget {
	public WelcomeJSFWidget() {
		super("/jsf/welcomeJSF.jsp");
	}
	
	public boolean isNested() {
		FlowContext c = (FlowContext)getEnvironment().getEntry(FlowContext.class);
		return (c != null && c.isNested());
	}
}
