package org.araneaframework.example.main.release.demos;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ComboTextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ComboTextInputDemoWidget extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;
	  private FormWidget form;
	  
	  protected void init() throws Exception {
		setViewSelector("release/demos/combotextInput");

		form = new FormWidget();
		ComboTextControl control = new ComboTextControl();

		control.addPredefinedInput("Raido Türk");
		control.addPredefinedInput("Lauri Tulmin");
		control.addPredefinedInput("Evelin Vanker");
		control.addPredefinedInput("Taavi Kotka");
		control.addPredefinedInput("Igor Ivanov");
		control.addPredefinedInput("Vladimir Shor");
		
		control.addPredefinedInput("Tiit Anmann");
		control.addPredefinedInput("Kristel Madiste");
		control.addPredefinedInput("Siim Puskai");
		control.addPredefinedInput("Eve Timmerman");
		control.addPredefinedInput("Priit Potter");
		
		control.addPredefinedInput("Tuuli Semevsky");
		control.addPredefinedInput("Andre Krull");

	    form.addElement("comboInput", "combo.demo.fieldinput", control, new StringData(), false);

		addWidget("form", form);
	  }
	  
	  public void handleEventSubmit() throws Exception {
		  if (form.convertAndValidate()) {
			  getMessageCtx().showInfoMessage( t("combo.submitmessage") + " " + (form.getValueByFullName("comboInput")));
		  }
	  }
}
