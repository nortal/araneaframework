package org.araneaframework.example.jsf.web.jsf.helloDuke;

import org.araneaframework.example.jsf.TemplateBaseWidget;
import org.araneaframework.integration.jsf.JsfWidget;

public class HelloDukeWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/generic");
		addWidget("helloDuke", new JsfWidget("/jsf/helloduke/greeting.jsp"));
	}
	
	public String getJsfWidget() {
		return "helloDuke";
	}
}	
