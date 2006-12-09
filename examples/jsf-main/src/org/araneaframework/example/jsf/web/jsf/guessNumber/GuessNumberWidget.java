package org.araneaframework.example.jsf.web.jsf.guessNumber;

import org.araneaframework.example.jsf.TemplateBaseWidget;
import org.araneaframework.integration.jsf.JsfWidget;

public class GuessNumberWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/generic");
		addWidget("guessNumber", new JsfWidget("/jsf/guessNumber/greeting.jsp"));
	}
	
	public String getJsfWidget() {
		return "guessNumber";
	}
}
