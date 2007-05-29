package org.araneaframework.example.select;

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.example.select.model.OptionModel;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.form.control.BaseControl;

public class StraightSelectControl extends BaseControl {
	private OptionModel optionModel;
	private StandardControlEventListenerAdapter eventListenerAdapter;

	public void convert() {

	}
	
	

	protected void update(InputData input) throws Exception {
		Object x = new StandardRequestProcessor().getParameters();
		super.update(input);
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
//	
//	  protected void readFromRequest(HttpInputData request) {
//		    String parameterValues[] = request.getParameterValues(getScope().toString());
//		    innerData = preprocessRequestParameters(parameterValues);
//		    isReadFromRequest = innerData != null;
//		  }
//	  
	 
	public interface RequestProcessor extends Serializable {
		Object getParameters();
	}
	
	public class StandardRequestProcessor implements RequestProcessor {
		public Object getParameters() {
			return getInputData().getScopedData(getScope().toPath());
		}
	}
}
