package org.araneaframework.example.jsf.web.jsf.helloDuke;

import org.araneaframework.example.jsf.TemplateBaseWidget;
import org.araneaframework.example.jsf.web.jsf.guessNumber.GuessNumberWidget;
import org.araneaframework.integration.jsf.JsfWidget;

public class TripleDukeWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/helloduke/tripleduke");
		addWidget("helloDuke1", new HelloDukeWidget());
		addWidget("helloDuke2", new GuessNumberWidget());
		addWidget("helloDuke3", new HelloDukeWidget() {
			protected void init() throws Exception {
				setViewSelector("jsf/generic");
				addWidget("helloDuke", new JsfWidget("/jsf/helloduke/greeting2.jsp"));
			}
		});
	}
}
