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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.ConfigurationContextUtil;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;


/**
 * Represents a general form element, a node in form element hierarchy.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class GenericFormElement extends BaseApplicationWidget {
  /** @since 1.1 */
  public static final String SEAMLESS_VALIDATION_ACTION_ID = "bgValidate";

  //*******************************************************************
  // FIELDS
  //*******************************************************************
  protected Constraint constraint;

  protected Map properties;
  
  protected boolean converted = false;
  protected boolean validated = false;
  protected Boolean backgroundValidation = null;
  
  private Set errors;
  
  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************
    
  protected void init() throws Exception {
    super.init();
    if (constraint != null)
      constraint.setEnvironment(getConstraintEnvironment());
  }

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
    if (constraint != null && isInitialized())
      constraint.setEnvironment(getConstraintEnvironment());
  }
  
  public Environment getConstraintEnvironment() {
    return getEnvironment();
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
  public boolean convertAndValidate() {
    convert();
    return validate();
  }  
  
  
  /**
   * Converts the value from {@link Control}s to {@link Data}.
   */
  public void convert() {
    try {
      converted = false;  
      validated = false;
		
      if (!isAlive())
        return;

      clearErrors();

      convertInternal();
		
      converted = isValid();
	} catch (Exception e) {
      ExceptionUtil.uncheckException(e);
	}
  }
    
  
  /**
   * Validates the element.
   * 
   * @return whether the element is valid.
   */
  public boolean validate() {
    boolean valid = false;
    try {
  	  validated = false;  	  	
  	
  	  valid = validateInternal();
    
  	  validated = valid;
  	  return valid;
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }

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
   * Since 1.1 this returns an immutable Set.
   */
  public Set getErrors() {
    return Collections.unmodifiableSet(getMutableErrors()); 
  }

  public void addError(String error) {
    Assert.notEmptyParam(error, "error");

    getMutableErrors().add(error);
    getMessageContext().showMessage(MessageContext.ERROR_TYPE, error);
  }

  public void addErrors(Set errors) {
    Assert.notNullParam(errors, "errors");
    for (Iterator i = errors.iterator(); i.hasNext(); )
      addError((String) i.next());
  }

  /**
   * Clears element errors.
   */
  public void clearErrors() {  
    getMessageContext().hideMessages(MessageContext.ERROR_TYPE, getErrors());
    errors = null;
  }

  public Object getValue() {
    return null;
  }
  
  /** @since 1.1 */
  public void setBackgroundValidation(boolean b) {
    this.backgroundValidation = Boolean.valueOf(b);
  }

  /** @since 1.1 */
  public boolean isBackgroundValidation() {
    if (this.backgroundValidation == null) {
      FormContext fctx = ((FormContext) getEnvironment().getEntry(FormContext.class));
      if (fctx != null)
        return fctx.isBackgroundValidation();
      return ConfigurationContextUtil.isBackgroundFormValidationEnabled(UilibEnvironmentUtil.getConfigurationContext(getEnvironment()));
    }
    return this.backgroundValidation.booleanValue();
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

  private MessageContext getMessageContext() {
    return (MessageContext) getEnvironment().requireEntry(MessageContext.class);
  }
    
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
  
  /**
   * @since 1.1
   */
  protected Set getMutableErrors() {
    if (errors == null)
      errors = new HashSet();
    return errors;
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
      Map m  = GenericFormElement.this.properties;
      this.properties = m == null ? m : Collections.unmodifiableMap(m);
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
