package org.araneaframework.example.blank;

import org.araneaframework.uilib.core.BaseUIWidget;

public class RootWidget extends BaseUIWidget {
	private static final long serialVersionUID = 1L;
	private MenuWidget menuWidget;

	public RootWidget() {}

	protected void init() throws Exception {
		menuWidget = new MenuWidget();
		addWidget("menu", menuWidget);
		setViewSelector("root");
	}

	public MenuWidget getMenuWidget() {
		return menuWidget;
	}
}
