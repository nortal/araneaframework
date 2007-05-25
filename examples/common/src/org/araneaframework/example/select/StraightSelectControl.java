package org.araneaframework.example.select;

import org.araneaframework.example.select.model.OptionModel;
import org.araneaframework.uilib.form.control.BaseControl;

public class StraightSelectControl extends BaseControl {
	private OptionModel optionModel;

	public void convert() {

	}

	public Object getRawValue() {
		return null;
	}

	public String getRawValueType() {
		return "Object";
	}

	public boolean isRead() {
		return false;
	}

	public void setRawValue(Object value) {
		
	}

	public void validate() {
	}

	// optionmodel
	public OptionModel getOptionModel() {
		return optionModel;
	}

	public void setOptionModel(OptionModel optionModel) {
		this.optionModel = optionModel;
	}
}
