package org.araneaframework.example.jsf.web.jsf.helloDuke;

import org.araneaframework.example.jsf.TemplateBaseWidget;

public class DoubleDukeWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/helloduke/doubleduke");
		addWidget("helloDuke1", new HelloDukeWidget());
		addWidget("helloDuke2", new HelloDukeWidget());
	}
}
