package org.araneaframework.example.main.web.demo;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IGeneralDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.select.SelectControl;
import org.araneaframework.example.select.model.OptionModel.ValueAndDisplayEncoder;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.data.StringData;

public class NewSelectDemoWidget extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	private IGeneralDAO generalDAO; 
	private FormWidget form;
	
	protected void init() throws Exception {
		setViewSelector("demo/newSelect");
		putViewData("formLabel", "#New Select Control");

		SelectControl personSelectControl = new SelectControl(generalDAO.getAll(PersonMO.class));
		personSelectControl.getModel().setValueAndDisplayEncoder(new X());
		
		form = new FormWidget();
		form.addElement("personSelect", "#Persons", personSelectControl, new StringData(), false);

		addWidget("form", form);
	}
	
	public void injectGeneralDAO(IGeneralDAO generalDAO) {
		this.generalDAO = generalDAO;
	}
	
	private static class X implements ValueAndDisplayEncoder {
		public String getDisplay(Object o) {
			return ((PersonMO)o).getName();
		}

		public String getValue(Object o) {
			return ((PersonMO)o).getId().toString();
		}
	}
}
