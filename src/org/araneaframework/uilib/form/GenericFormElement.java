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
import java.util.Map;
import java.util.Set;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * Represents a general form element, a node in form element hierarchy.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public abstract class GenericFormElement extends BaseApplicationWidget implements GenericFormElementContext {

  //*******************************************************************
  // FIELDS
  //*******************************************************************
  protected Constraint constraint;

  protected Map properties;
  
  protected boolean converted = false;
  protected boolean validated = false;  
  
  private Set errors;

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************
    
  /**
   * Returns all properties of the element as a map (string -&gt; string).
   * 
   * @return all properties as a map.
   */
  public Map getProperties() {
    if (properties == null)
      properties = new HashMap();
    return properties;
  }

  /**
   * Method for setting property values.
   * 
   * @param key name of the property to be set.
   * @param value value for the property.
   */
  public void setProperty(Object key, Object value) {
    Assert.notNullParam(key, "key");
    
    getProperties().put(key, value);
  }

  /**
   * Returns the value of the property.
   * 
   * @param key the name of the property to get.
   * @return the value of the property, <code>null</code> if property is not defined.
   */
  public Object getProperty(Object key) {
    Assert.notNullParam(key, "key");
    
    return getProperties().get(key);
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
    if (constraint != null)
      constraint.setGenericFormElementCtx(this);
  }

  /**
   * Returns whether the element is valid.
   * 
   * @return whether the element is valid.
   */
  public boolean isValid() {
    return (errors == null || errors.size() == 0);
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

  //*********************************************************************
  //* OVERRIDABLE METHODS
  //*********************************************************************

  protected void process() throws Exception {
    ErrorUtil.showErrors(getErrors(), getEnvironment());
    
    super.process();
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
   * Restores the value of the data item from the marked base state.
   */
  public abstract void restoreBaseState();
  
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
   * Returns whether the element is disabled.
   * @return whether the element is disabled.
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
      addErrors(getConstraint().getErrors());
      getConstraint().clearErrors();
    }

    return isValid();
  }
  
  public Set getErrors() {
    if (errors == null)
      errors = new HashSet();
    return errors;
  }
  
  public void addError(String error) {
    Assert.notNullParam(error, "error");
    
    getErrors().add(error);
  }
  
  public void addErrors(Set errors) {
    Assert.noNullElementsParam(errors, "errors");
    
    getErrors().addAll(errors);
  }
  
  /**
   * Clears element errors.
   */
  public void clearErrors() {  
    errors = null;
  }
  
  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * This class represents a form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends BaseApplicationWidget.ViewModel{
    
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
