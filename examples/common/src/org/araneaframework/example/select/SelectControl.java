/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package org.araneaframework.example.select;

import java.util.Collection;
import org.araneaframework.example.select.model.OptionModel;
import org.araneaframework.example.select.model.StandardOptionModel;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.StringValueControl;

/**
 * This class represents a selectbox (aka dropdown) control.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SelectControl extends StringValueControl {
	protected OptionModel optionModel;
	
	public SelectControl(Collection c) {
		this.optionModel = new StandardOptionModel(c);
	}

	public SelectControl(OptionModel optionModel) {
		this.optionModel = optionModel; 
	}

	public OptionModel getModel() {
		return optionModel;
	}

	//*********************************************************************
	//* INTERNAL METHODS
	//*********************************************************************  	


	/**
	 * Controls that the value submitted by the user is found in the select
	 * items list. 
	 */
	protected void validateNotNull() {
		super.validateNotNull();

		String data = innerData == null ? null : ((String[]) innerData)[0];

		//TODO: check that submitted value is valid choice
//		if (!DisplayItemUtil.isValueInItems(SelectControl.this, data)) 
//		throw new SecurityException("A value '" + data + "' not found in the list has been submitted to a select control!");
	}

	/**
	 * Returns {@link ViewModel}.
	 * @return {@link ViewModel}.
	 */
	public Object getViewModel() {
		return new ViewModel();
	}

	protected String preprocessRequestParameter(String parameterValue) {
		//TODO: refactor ugly hack

		parameterValue = super.preprocessRequestParameter(parameterValue);

		//TODO: check that submitted value is valid choice
//		if (parameterValue != null && !DisplayItemUtil.isValueInItems(SelectControl.this, parameterValue)) 
//		throw new SecurityException("A value '" + parameterValue + "' not found in the list has been submitted to a select control!");

		//XXX: remove the code Handles disabled DisplayItems      
//		String[] previousValues = (String[]) innerData;		  	    	   

//		if (previousValues != null && previousValues.length == 1) {
//		int valueIndex = getValueIndex(previousValues[0]);

//		if (valueIndex != -1) {
//		DisplayItem previousDisplayItem = (DisplayItem) getDisplayItems().get(valueIndex);

//		if (previousDisplayItem.isDisabled() && parameterValue == null)
//		return previousDisplayItem.getValue();
//		}
//		}

		return parameterValue;
	}
	
	

	//*********************************************************************
	//* VIEW MODEL
	//*********************************************************************    

	public Object getRawValue() {
		//XXX: rethink
//		//optionModel.getValue();
		return super.getRawValue();
	}

	public String getRawValueType() {
		return "Object";
	}

	/**
	 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
	 * 
	 */
	public class ViewModel extends StringArrayRequestControl.ViewModel {
		private OptionModel optionModel;

		/**
		 * Takes an outer class snapshot.     
		 */    
		public ViewModel() {
			this.optionModel = SelectControl.this.optionModel;
		}

		public OptionModel getOptionModel() {
			return optionModel;
		}

		public String getSimpleValue() {
			// XXX: which option group?????
			return SelectControl.this.optionModel.getValueEncoder().encode(SelectControl.this.getRawValue());
		}
	}  
}
