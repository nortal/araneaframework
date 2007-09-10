package org.araneaframework.example.main.release.features;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.demo.DemoComplexForm;

/**
 * @author Taimo Peelo
 */
public class EasyAJAXUpdateRegionsWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		addWidget("1", new EasyAjaxDemoWidget("release/features/easyAjax/HttpRequest"));
		addWidget("2", new EasyAjaxDemoWidget("release/features/easyAjax/XMLHttpRequest"));
	}
	
	private static class EasyAjaxDemoWidget extends DemoComplexForm {
		private String customViewSelector;
		
		public EasyAjaxDemoWidget(String customViewSelector) {
			this.customViewSelector = customViewSelector;
		}
		
		protected void init() throws Exception {
			super.init();
			setViewSelector(customViewSelector);
		}
	}
}
