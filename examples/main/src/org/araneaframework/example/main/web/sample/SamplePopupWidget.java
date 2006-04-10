package org.araneaframework.example.main.web.sample;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.menu.TemplateMenuMessage;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.support.PopupWindowProperties;

public class SamplePopupWidget extends TemplateBaseWidget {
	private static final Logger log = Logger.getLogger(SamplePopupWidget.class);

	protected void init() throws Exception {
		super.init();

		setViewSelector("sample/samplePopup");

	}

	public void handleEventCreateThread() throws Exception {
		getMessageCtx().showInfoMessage("Popup window should have opened. If it did not, please relax your popup blocker settings.");
		
		TemplateMenuMessage message = new TemplateMenuMessage("Samples.Simple_Form");
		
		PopupWindowContext popupCtx = (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
		popupCtx.open("prefix", new PopupWindowProperties(), message);
	}
}
