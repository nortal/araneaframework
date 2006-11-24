package jsf.test.base;

import jsf.test.HelloWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class HelloCompositeWidget extends BaseUIWidget {
	protected void init() throws Exception {
		setViewSelector("composite");
		addWidget("helloWidget", new HelloWidget());
		addWidget("jsfWidget", new JsfWidget("welcomeJSF"));
	}
}
