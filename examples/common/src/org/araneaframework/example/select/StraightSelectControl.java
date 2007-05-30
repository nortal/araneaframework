package org.araneaframework.example.select;

import org.araneaframework.example.select.model.OptionModel;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;

public class StraightSelectControl extends StraightBaseControl {
	private OptionModel optionModel;
	private StandardControlEventListenerAdapter eventListenerAdapter;

	public Object getRawValue() {
		return null;
	}

	public boolean isRead() {
		//XXX: nono no
		return super.isRead();
	}

	// optionmodel
	public OptionModel getOptionModel() {
		return optionModel;
	}

	public void setOptionModel(OptionModel optionModel) {
		this.optionModel = optionModel;
	}
}
