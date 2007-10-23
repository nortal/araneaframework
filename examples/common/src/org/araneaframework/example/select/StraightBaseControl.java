package org.araneaframework.example.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElementContext;

public class StraightBaseControl extends BaseApplicationWidget implements Control  {
	private FormElementContext formElementContext;

	private transient Collection clientValue;
	
	private Object value;


	//*********************************************************************
	//* PROTECTED LIFECYCLE METHODS
	//*********************************************************************  
	protected void init() throws Exception {
		super.init();

		Assert.notNull(this, getFormElementCtx(), "Form element context must be assigned to the control before it can be initialized! " +
		"Make sure that the control is associated with a form element!");
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		if (!isDisabled())
			super.action(path, input, output);
	}

	protected void update(InputData input) throws Exception {
		if (!isDisabled())
			clientValue = getControlReader().readValue(this, input);
		
		super.update(input);
	}

	protected void handleEvent(InputData input) throws Exception {
		if (!isDisabled())
			super.handleEvent(input);
	}
	
	// PROTECTED FormElementContext
	protected void addError(String error) {
		formElementContext.addError(error);
	}
		
	//*********************************************************************
	//* PUBLIC FormElementContext methods
	//*********************************************************************
	public void setFormElementCtx(FormElementContext feCtx) {
		this.formElementContext = feCtx;
	}

	public FormElementContext getFormElementCtx() {
		return formElementContext;
	}
	
	public boolean isDisabled() {
		return formElementContext.isDisabled();
	}
	
	public String getLabel() {
		return formElementContext.getLabel();
	}

	

	
	
	
	
	
	
	public boolean isRead() {
		return false;
	}

	
	public Object getRawValue() {
		return this.value;
	}

	public String getRawValueType() {
		return "Object";
	}

	public void setRawValue(Object value) {
		this.value = value;
		if (value instanceof Collection) {
			clientValue = (Collection) value;
		} else {
            if (clientValue == null)
              clientValue = new ArrayList(1);
			clientValue.clear();
			clientValue.add(value);
		}
	}

	public void convertAndValidate() {
		convert();
		validate();
	}

	public void convert() {
		Collection valueCollection = getControlReader().readValue(this, getInputData());
		getControlConverter().doConvert(valueCollection);
	}

	public void validate() {

	}



	// TODO: protected???
	public ControlReader getControlReader() {
		return StandardControlReader.INSTANCE;
	}

	public ControlWriter getControlWriter() {
		return new BaseControlWriter();
	}
	
	public Object getViewModel() throws Exception {
		return new ViewModel();
	}

	public class ViewModel extends BaseApplicationWidget.ViewModel implements Control.ViewModel {
		private String controlType;
		private String label;
		private boolean disabled;
		private boolean mandatory;
		private String simpleValue;

		public ViewModel() {
			String className = StraightBaseControl.this.getClass().getName();
			// Recognizes Controls that are defined as (anonymous) nested classes.
			// Prior to 1.5 getDeclaringClass() does not exist, so just look for '$'.
			if (className.indexOf('$') != -1)
				className = StraightBaseControl.this.getClass().getSuperclass().getName();
			className = className.substring(className.lastIndexOf(".") + 1);
			this.controlType = className;

			this.mandatory = StraightBaseControl.this.getFormElementCtx().isMandatory();
			this.disabled = StraightBaseControl.this.isDisabled();

			this.label = StraightBaseControl.this.getFormElementCtx().getLabel();
			this.simpleValue = getControlWriter().controlValue();
			//XXX:
			if (simpleValue == "null")
				simpleValue = null;
		}

		public String getControlType() {
			return controlType;
		}

		public String getLabel() {
			return label;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public boolean isMandatory() {
			return mandatory;
		}

		public String getSimpleValue() {
			return simpleValue;
		}
	}

	protected ControlConverter getControlConverter() {
		return new BaseControlConverter();
	}

	interface ControlConverter extends Serializable {
		void doConvert(Collection value);
	}

	public class BaseControlConverter implements ControlConverter {
		public void doConvert(Collection values) {
			if (values.isEmpty())
				setRawValue(null);
			else if (values.size() == 1)
				setRawValue(values.iterator().next());
			else
				setRawValue(values);
		}
	}

	interface ControlReader extends Serializable {		
		/**
		 * Reads the data directed to <i>Control</i> from given <i>InputData</i>.
		 * @return collection with values or empty collection
		 */
		Collection readValue(Control control, InputData inputData);
	}

	public static class StandardControlReader implements ControlReader {
		public static final StandardControlReader INSTANCE = new StandardControlReader();
		public Collection readValue(Control control, InputData inputData) {
			HttpServletRequest request = ServletUtil.getRequest(inputData);
			String[] values = request.getParameterValues(control.getScope().toString());

			List result = Collections.EMPTY_LIST;
			if (values != null) {
				result = new ArrayList(values.length);
				Collections.addAll(result, values);
			}

			return result;
		}
	}
	
	interface ControlWriter extends Serializable {		
		String controlValue();
	}
	
	public class BaseControlWriter implements ControlWriter {
		public String controlValue() {
			return String.valueOf(getRawValue());
		}
	}
}
