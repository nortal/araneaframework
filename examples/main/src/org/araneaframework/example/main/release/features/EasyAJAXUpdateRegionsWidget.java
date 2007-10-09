package org.araneaframework.example.main.release.features;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.demo.DemoComplexForm;

/**
 * @author Taimo Peelo
 */
public class EasyAJAXUpdateRegionsWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		addWidget("1", new EasyAjaxDemoWidget("release/features/easyAjax/HttpRequest"));
		addWidget("2", new EasyAjaxDemoWidget("release/features/easyAjax/XMLHttpRequest", "Complex_Form"));
		setViewSelector("release/features/easyAjax/easyAjaxUpdateRegions");
	}
	
	private static class EasyAjaxDemoWidget extends DemoComplexForm {
		private String customViewSelector;
		private String label;
		
		public EasyAjaxDemoWidget(String customViewSelector,String label) {
			this.customViewSelector = customViewSelector;
			this.label = label;
		}
		
		protected void init() throws Exception {
			super.init();
			setViewSelector(customViewSelector);
			putViewData("formLabel", label);
		}
	}
}
