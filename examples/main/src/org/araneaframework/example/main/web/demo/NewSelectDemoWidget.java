package org.araneaframework.example.main.web.demo;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IGeneralDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.select.SelectControl;
import org.araneaframework.example.select.StraightSelectControl;
import org.araneaframework.example.select.StraightTextControl;
import org.araneaframework.example.select.model.StandardOptionModel;
import org.araneaframework.example.select.model.OptionModel.ValueAndDisplayEncoder;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.data.ObjectData;
import org.araneaframework.uilib.form.data.StringData;

public class NewSelectDemoWidget extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	private IGeneralDAO generalDAO; 
	private FormWidget form;
	
	private FormElement personSelect;
	private FormElement groupedPersonSelect;
	
	protected void init() throws Exception {
		setViewSelector("demo/newSelect");
		putViewData("formLabel", "#New Select Control");

		SelectControl personSelectControl = new SelectControl(generalDAO.getAll(PersonMO.class));
		personSelectControl.getModel().setValueAndDisplayEncoder(new X());
		
		form = new FormWidget();
		//form.addElement("tstelement", "#noalble", new StraightSelectControl(), new  StringData(), false);
		form.addElement("nt", "#new text", new StraightTextControl(), new StringData(), null, true);
		
		personSelect = form.createElement("#Persons", personSelectControl, new ObjectData(), false);
		
		List noBirthDate = generalDAO.getAll(PersonMO.class);
		CollectionUtils.filter(noBirthDate, 
				new Predicate() {
					public boolean evaluate(Object o) {
						PersonMO person = (PersonMO) o;
						return person.getBirthdate() == null;
					}
				}
				);
		
		List hasBirthDate = generalDAO.getAll(PersonMO.class);
		CollectionUtils.filter(hasBirthDate, 
				new Predicate() {
					public boolean evaluate(Object o) {
						PersonMO person = (PersonMO) o;
						return person.getBirthdate() != null;
					}
				}
				);
		
		StandardOptionModel noBDayModel = new StandardOptionModel(noBirthDate);
		StandardOptionModel withBDayModel = new StandardOptionModel(hasBirthDate);
		
		StandardOptionModel optionModel = new StandardOptionModel();
		optionModel.addSubOptionGroup("#NoBirthDate", null, noBDayModel);
		optionModel.addSubOptionGroup("#WithBirthDate", null, withBDayModel);
			
		groupedPersonSelect = form.createElement("#Persons", new SelectControl(optionModel), new ObjectData(), false);
		((SelectControl)groupedPersonSelect.getControl()).getModel().setValueAndDisplayEncoder(new X());
		
//		form.addElement("personSelect", personSelect);
//		form.addElement("groupedPersonSelect", groupedPersonSelect);
		
		personSelect.setDisabled(true);
		groupedPersonSelect.setDisabled(true);

		addWidget("form", form);
	}
	
	public void injectGeneralDAO(IGeneralDAO generalDAO) {
		this.generalDAO = generalDAO;
	}
	
	public void handleEventSubmit() throws Exception {
		//form.convert();
		
		//getMessageCtx().showInfoMessage(new ReflectionToStringBuilder(groupedPersonSelect.getValue()).toString());
	}
	
	private static class X implements ValueAndDisplayEncoder {
		public String getDisplay(Object o) {
			return ((PersonMO)o).getName();
		}

		public String getValue(Object o) {
			if (o == null)
				return null;
			
			//XXX what
			return ((PersonMO)o).getId().toString();
		}
	}
}
