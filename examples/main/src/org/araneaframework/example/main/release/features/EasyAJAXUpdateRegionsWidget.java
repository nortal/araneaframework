package org.araneaframework.example.main.release.features;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.demo.DemoComplexForm;

/**
 * @author Taimo Peelo
 */
public class EasyAJAXUpdateRegionsWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		addWidget("1", new DemoComplexForm());
		addWidget("2", new DemoComplexForm());

		setViewSelector("release/features/easyAjaxUpdateRegions");
	}
}
