package org.araneaframework.example.select;

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElementContext;

public class StraightBaseControl extends BaseApplicationWidget implements Control  {
	private FormElementContext formElementContext;
	
	
	
	public boolean isRead() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return false;
	}


	public void convert() {
		// TODO Auto-generated method stub
		
	}

	public Object getRawValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRawValueType() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setRawValue(Object value) {
		// TODO Auto-generated method stub
		
	}

	public void validate() {
		// TODO Auto-generated method stub
		
	}

	public void setFormElementCtx(FormElementContext feCtx) {
		this.formElementContext = feCtx;
	}

	public FormElementContext getFormElementCtx() {
		return formElementContext;
	}
	
	interface ControlReader extends Serializable {
		String[] readValue(Control control, InputData inputData);
	}
}
