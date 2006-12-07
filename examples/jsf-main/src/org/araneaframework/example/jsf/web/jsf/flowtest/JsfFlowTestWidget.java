package org.araneaframework.example.jsf.web.jsf.flowtest;

import org.araneaframework.example.jsf.TemplateBaseWidget;
import org.araneaframework.example.jsf.web.jsf.WelcomeJSFWidget;
import org.araneaframework.example.jsf.web.jsf.helloDuke.HelloDukeWidget;

public class JsfFlowTestWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/flowtest");
		
		addWidget("sadWidget", new HelloDukeWidget());
	}
	
	public void handleEventNext() {
		getFlowCtx().start(new WelcomeJSFWidget(), null, null);
	}
}
