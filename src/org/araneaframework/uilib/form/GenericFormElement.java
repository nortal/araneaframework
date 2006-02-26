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

package org.araneaframework.uilib.form;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.araneaframework.uilib.form.constraint.Constraint;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * Represents a general form element, a node in form element hierarchy.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public abstract class GenericFormElement extends StandardPresentationWidget {

  //*******************************************************************
  // FIELDS
  //*******************************************************************
  protected Constraint constraint;

  protected Map properties = new HashMap();
  
  protected boolean converted = false;
  protected boolean validated = false;  
  
  protected Set errors = new HashSet();

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************
    
  /**
   * Returns all properties of the element as a map (string -&gt; string).
   * 
   * @return all properties as a map.
   */
  public Map getProperties() {
    return properties;
  }

  /**
   * Method for setting property values.
   * 
   * @param key name of the property to be set.
   * @param value value for the property.
   */
  public void setProperty(Object key, Object value) {
    properties.put(key, value);
  }

  /**
   * Returns the value of the property.
   * 
   * @param key the name of the property to get.
   * @return the value of the property, <code>null</code> if property is not defined.
   */
  public Object getProperty(Object key) {
    return properties.get(key);
  }

  /**
   * Returns element constraint.
   * 
   * @return element constraint.
   */
  public Constraint getConstraint() {
    return constraint;
  }

  /**
   * Sets element constraint.
   * 
   * @param constraint The constraint to set.
   */
  public void setConstraint(Constraint constraint) {
    this.constraint = constraint;
    
    if (constraint != null && isInitialized()) {
      constraint.setEnvironment(getEnvironment());
    }
  }

  /**
   * Returns whether the element is valid.
   * 
   * @return whether the element is valid.
   */
  public boolean isValid() {
    return (errors.size() == 0);
  }

  /**
   * Converts the element value from control to data item and validates the value.
   * 
   * @return whether the element is valid.
   */
  public boolean convertAndValidate() throws Exception  {
    convert();
    return validate();
  }  
  
  
  /**
   * Converts the element value from control to data item
   */
  public void convert()  throws Exception {
    clearErrors();
    
  	converted = false;  
  	validated = false;
  	
  	convertInternal();
  	
  	converted = isValid();
  }
    
  
  /**
   * Validates the element.
   * 
   * @return whether the element is valid.
   * @throws Exception 
   */
  public boolean validate() throws Exception {
  	validated = false;  	  	
  	
  	boolean valid = validateInternal();   
    
  	validated = valid;  	
  	return valid;
  }
  
  /**
   * Returns whether last evaluation (converting and optional validating) has succeeded.
   * @return whether last evaluation (converting and optional validating) has succeeded.
   */
  public boolean isEvaluated() {
  	return converted && validated;
  }    

  /**
   * Clears element errors.
   */
  public void clearErrors() {  
    errors.clear();   
  }

  //*********************************************************************
  //* OVERRIDABLE METHODS
  //*********************************************************************
  
  protected void init() throws Exception {
    super.init();
    
    if (getConstraint() != null)
      getConstraint().setEnvironment(getEnvironment());   
  }

  //*********************************************************************
  //* ABSTRACT METHODS
  //*********************************************************************
  
  /**
   * Marks the current value of the data item as the base state
   * that will be used to determine whether its state has changed in
   * {@link #isStateChanged()}. 
   */
  public abstract void markBaseState();
  
  /**
   * Returns whether data item state has changed after it was marked.
   * @return whether data item state has changed after it was marked.
   */
  public abstract boolean isStateChanged();
  
	/**
	 * Sets wether the element is disabled.
	 * @param disabled wether the element is disabled.
	 */
	public abstract void setDisabled(boolean disabled);
  
  /**
   * Returns wether the element is disabled.
   * @return wether the element is disabled.
   */
	public abstract boolean isDisabled();
    
  /**
   * Accepts the visitor.
   */
	public abstract void accept(String id, FormElementVisitor visitor);
	
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
    
  /**
   * Converts the element value from control to data item
   * @throws Exception 
   */
  protected abstract void convertInternal() throws Exception;    
  
  /**
   * Validates the element.
   * 
   * @return whether the element is valid.
   * @throws Exception 
   */
  protected boolean validateInternal() throws Exception {
    if (getConstraint() != null && isValid()) {
    	getConstraint().validate();    
      errors.addAll( ErrorUtil.showErrors(getConstraint().getErrors(), getEnvironment()));
      getConstraint().clearErrors();
    }

    return isValid();
  }
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * This class represents a form element view model.
   * 
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
   * 
   */
  public class ViewModel extends StandardWidget.ViewModel{
    
    private Map properties;

    /**
     * Takes a outer class snapshot.     
     */
    public ViewModel() {
      this.properties = GenericFormElement.this.properties;
    }
    
    /**
     * Returns form element properties.
     * @return form element properties.
     */
    public Map getProperties() {
      return properties;
    }
  }  
}
