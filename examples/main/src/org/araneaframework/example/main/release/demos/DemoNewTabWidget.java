package org.araneaframework.example.main.release.demos;

import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.WidgetFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.demo.DemoMultiSelect;
import org.araneaframework.example.main.web.misc.RedirectingWidget;
import org.araneaframework.example.main.web.sample.SimpleFormWidget;
import org.araneaframework.uilib.tab.TabContainerWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoNewTabWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
	    setViewSelector("release/demos/newtab");
	    
	    TabContainerWidget containerWidget = new TabContainerWidget();
	    addWidget("tabContainer", containerWidget);
	    
	    containerWidget.addTab("1", "#Simple Form Widget 1", new SimpleFormWidget());
	    containerWidget.addTab("2", "#Simple Form Widget 2", new SimpleFormWidget());
	    containerWidget.addTab("3", "#Redirecting widget", new RedirectingWidget());
	    containerWidget.addTab("4", "#Multi Select demo", new DemoMultiSelect());
	    containerWidget.addTab("5", "#Stateless Tab", new WidgetFactory() {
			public Widget buildWidget(Environment env) {
				return new SimpleFormWidget();
			}
	    });

//	    containerWidget.addWidget("1", new TabWidget("#Simple Form Widget 1", new SimpleFormWidget()));
//	    containerWidget.addWidget("2", new TabWidget("#Simple Form Widget 2", new SimpleFormWidget()));
//	    containerWidget.addWidget("3", new TabWidget("#Redirecting widget", new RedirectingWidget()));
//	    containerWidget.addWidget("4", new TabWidget("#Multi Select demo", new DemoMultiSelect()));
	    
	    containerWidget.disableTab("3");
	    //containerWidget.removeWidget("1");
	}
}
