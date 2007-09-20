package org.araneaframework.example.main.release.demos;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.demo.DemoComplexForm;
import org.araneaframework.example.main.web.demo.DemoMultiSelect;
import org.araneaframework.example.main.web.misc.RedirectingWidget;
import org.araneaframework.example.main.web.sample.SimpleFormWidget;
import org.araneaframework.uilib.newtab.TabContainerWidget;
import org.araneaframework.uilib.newtab.TabWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoNewTabWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
	    setViewSelector("release/demos/newtab");
	    
	    TabContainerWidget containerWidget = new TabContainerWidget();
	    addWidget("tabContainer", containerWidget);
	    
	    containerWidget.addWidget("randomWidget", new DemoComplexForm());
	    containerWidget.addWidget("1", new TabWidget("#Simple Form Widget 1", new SimpleFormWidget()));
	    containerWidget.addWidget("2", new TabWidget("#Simple Form Widget 2", new SimpleFormWidget()));
	    containerWidget.addWidget("3", new TabWidget("#Redirecting widget", new RedirectingWidget()));
	    containerWidget.addWidget("4", new TabWidget("#Multi Select demo", new DemoMultiSelect()));
	    
	    containerWidget.removeWidget("1");
	}
}
