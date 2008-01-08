package org.araneaframework.example.main.web.demo;

import java.util.List;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IGeneralDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.util.DisplayItemUtil;

public class OldSelectDemoWidget extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	private IGeneralDAO generalDAO; 
	private FormWidget form;
	
	private FormElement personSelect;
	
	protected void init() throws Exception {
		setViewSelector("demo/oldSelect");
		putViewData("formLabel", "#New Select Control");

		List persons = generalDAO.getAll(PersonMO.class);
		SelectControl personSelectControl = new SelectControl();
		
		DisplayItemUtil.addItemsFromBeanCollection(personSelectControl, persons, "id", new Transformer() {
			public Object transform(Object o) {
				PersonMO p = (PersonMO) o;
				return p.getName() + " " + p.getSurname();
			}
		});

		form = new FormWidget();
		
		personSelect = form.createElement("#Persons", personSelectControl, new StringData(), false);

		form.addElement("personSelect", personSelect);
		
		personSelect.setDisabled(false);

		addWidget("form", form);
	}
	
	public void injectGeneralDAO(IGeneralDAO generalDAO) {
		this.generalDAO = generalDAO;
	}
	
	public void handleEventSubmit() throws Exception {
		form.convert();

        Object value = form.getValueByFullName("personSelect");
		getMessageCtx().showInfoMessage(new ReflectionToStringBuilder(value).toString());
        getMessageCtx().showInfoMessage("Corresponding object: " + 
        		new ReflectionToStringBuilder(generalDAO.getById(PersonMO.class, new Long((String)value))).toString());
	}
}
