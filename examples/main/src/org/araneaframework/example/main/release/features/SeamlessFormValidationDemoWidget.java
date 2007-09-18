package org.araneaframework.example.main.release.features;

import org.araneaframework.InputData;
import org.araneaframework.core.EventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.DateData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SeamlessFormValidationDemoWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("release/features/seamlessFormValidation/demo");
		addWidget("form1", buildFormWidget());
		FormWidget f2 = buildFormWidget();
		f2.enableBackgroundValidation();
		addWidget("form2", f2);
	}

	private FormWidget buildFormWidget() {
		final FormWidget form = new FormWidget();
		form.addElement("futureDate", "seamless.appointmentdate", new DateControl(), new DateData(), true);
		form.addElement("time", "seamless.appointmenttime", new TimeControl(), new DateData(), true);

		form.addEventListener("dummy", new EventListener() {
			public void processEvent(Object eventId, InputData input) throws Exception {
				form.convertAndValidate();
			}
		});
		return form;
	}
}
