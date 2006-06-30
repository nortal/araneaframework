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

package org.araneaframework.uilib.form.control;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;

import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.DecimalPattern;
import org.araneaframework.uilib.util.ErrorUtil;
import org.araneaframework.uilib.util.ValidationUtil;


/**
 * This class represents a textbox control that accepts only valid 
 * floating-point numbers.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FloatControl extends EmptyStringNullableControl {
	
	private BigDecimal minValue;
	private BigDecimal maxValue;
	
	/**
	 * The number format used by the element for parsing.
	 */
	protected Collection decimalFormat;
	protected NumberFormat currentNumberFormat;
	
	protected boolean confOverridden = false;  
	
	/**
	 * Creates the control initializing the pattern default.
	 */
	public FloatControl() {
		this.decimalFormat = Arrays.asList(new DecimalPattern[] {new DecimalPattern(null, null)});
		this.currentNumberFormat = NumberFormat.getNumberInstance();
	}
	
	/**
	 * Creates the control initializing the pattern to <code>decimalFormat</code>.
	 * @param decimalFormat the custom pattern.
	 */
	public FloatControl(DecimalPattern[] decimalFormat, DecimalPattern defaultOutputFormat) {
		this.decimalFormat = Arrays.asList(decimalFormat);
		this.currentNumberFormat = defaultOutputFormat.getNumberFormat();
		
		this.confOverridden = true;
	}	
	
	/**
	 * Makes a float control that has minimum and maximum value.
	 * 
	 * @param minValue minimum permitted value.
	 * @param maxValue maximum permitted value.
	 */
	public FloatControl(BigDecimal minValue, BigDecimal maxValue) {
		this();
		this.minValue = minValue;
		this.maxValue = maxValue;
	}    
	
	/**
	 * Sets the maximum value.
	 * @param maxValue maximum value.
	 */
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}
	
	/**
	 * Sets the minimum value.
	 * @param minValue minimum value.
	 */
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}
	
	/**
	 * Returns the maxValue.
	 * @return the maxValue.
	 */
	public BigDecimal getMaxValue() {
		return maxValue;
	}
	
	/**
	 * Returns the minValue.
	 * @return the minValue.
	 */
	public BigDecimal getMinValue() {
		return minValue;
	}
	
	/**
	 * Returns "BigDecimal".
	 * @return "BigDecimal".
	 */
	public String getRawValueType() {
		return "BigDecimal";
	}
	
	//*********************************************************************
	//* INTERNAL METHODS
	//*********************************************************************
	
	protected void init() throws Exception {
		super.init();
		if (!confOverridden) {
      Collection confFormat = (Collection) getConfiguration().getEntry(ConfigurationContext.CUSTOM_DECIMAL_FORMAT);    
			if (confFormat != null) decimalFormat = confFormat;
			
			DecimalPattern confOutputFormat = (DecimalPattern) getConfiguration().getEntry(ConfigurationContext.DEFAULT_DECIMAL_OUTPUT_FORMAT);    
			if (confOutputFormat != null) currentNumberFormat = confOutputFormat.getNumberFormat();
		}
	}
	
	/**
	 * Trims request parameter.
	 */
	protected String preprocessRequestParameter(String parameterValue) {
		String result = super.preprocessRequestParameter(parameterValue);
		return (result == null ? null : result.trim());
	}
	
	/**
	 * Checks that the submitted data is a valid floating-point number.
	 * 
	 */
	protected Object fromRequest(String parameterValue) {
		if (currentNumberFormat == null) {
			BigDecimal result = null;
			
			try {
				result = new BigDecimal(parameterValue);
			}
			catch (NumberFormatException e) {
				addError(
						ErrorUtil.localizeAndFormat(
								UiLibMessages.NOT_A_NUMBER, 
								ErrorUtil.localize(getLabel(), getEnvironment()),
								getEnvironment()));          
			}
			
			return result;
		}
		
		ValidationUtil.ParsedNumber result = ValidationUtil.parseNumber(parameterValue, decimalFormat);
		
		if (result != null) {
			currentNumberFormat = result.getFormat();
			return new BigDecimal(result.getNumber().toString());
		}
		
		addError(
				ErrorUtil.localizeAndFormat(
						UiLibMessages.WRONG_DECIMAL_FORMAT, 
						ErrorUtil.localize(getLabel(), getEnvironment()),
						decimalFormat.toString(),
						getEnvironment()));          
		
		return null;
	}
	
	/**
	 * 
	 */
	protected String toResponse(Object controlValue) {
		if (currentNumberFormat == null) {
			return ((BigDecimal) controlValue).toString();
		}
		return currentNumberFormat.format(((BigDecimal) controlValue).doubleValue());
	}
	
	/**
	 * Checks that the submitted value is in permitted range.
	 * 
	 */
	protected void validateNotNull() {        
		if (minValue != null && maxValue != null && ((((BigDecimal) value).compareTo(minValue) == -1) || ((BigDecimal) value).compareTo(maxValue) == 1)) {
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NUMBER_NOT_BETWEEN, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									minValue.toString(),
									maxValue.toString()
							},          
							getEnvironment()));           
		}      
		else if (minValue != null && ((BigDecimal) value).compareTo(minValue) == -1) {      
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NUMBER_NOT_GREATER, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									minValue.toString(),
							},          
							getEnvironment()));       
		}    
		else if (maxValue != null && ((BigDecimal) value).compareTo(maxValue) == 1) {
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NUMBER_NOT_LESS, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									maxValue.toString(),
							},          
							getEnvironment()));         
		}    
	}
	
	/**
	 * Returns {@link ViewModel}.
	 * @return {@link ViewModel}.
	 */
	public Object getViewModel() {
		return new ViewModel();
	}
	
	//*********************************************************************
	//* VIEW MODEL
	//*********************************************************************    
	
	/**
	 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
	 * 
	 */
	public class ViewModel extends StringArrayRequestControl.ViewModel {
		
		private BigDecimal maxValue;
		private BigDecimal minValue;
		
		/**
		 * Takes an outer class snapshot.     
		 */    
		public ViewModel() {
			this.maxValue = FloatControl.this.getMaxValue();
			this.minValue = FloatControl.this.getMinValue();
		}       
		
		/**
		 * Returns maximum permitted value.
		 * @return maximum permitted value.
		 */
		public BigDecimal getMaxValue() {
			return this.maxValue;
		}
		
		/**
		 * Returns minimum permitted value.
		 * @return minimum permitted value.
		 */
		public BigDecimal getMinValue() {
			return this.minValue;
		}  
	}
}
