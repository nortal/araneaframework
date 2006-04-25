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

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.Widget;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.uilib.InvalidFormElementNameException;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.NameUtil;


/**
 * This class represents a form element that can contain other form elements.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FormWidget extends GenericFormElement {

  //*******************************************************************
  // FIELDS
  //*******************************************************************

  protected LinkedMap elements = new LinkedMap();

  //*********************************************************************
  //* PUBLIC METHODS
  //*********************************************************************

  public void clearErrors() {
	  super.clearErrors();
	  for (Iterator i = getElements().values().iterator(); i.hasNext();) {
		 ((GenericFormElement) i.next()).clearErrors();
	  }
  }
  
  public boolean isValid() {
    boolean result = super.isValid();
    
    for (Iterator i = getElements().values().iterator(); i.hasNext();) {
      if (!result) 
        break;
      result &= ((GenericFormElement) i.next()).isValid();   
    }
    
    return result;
  }
  
  /**
   * Returns a contained element by its name.
   * 
   * @param elementName contained element name
   * @return a contained element by its name.
   */
  public GenericFormElement getElement(String elementName) {
    return (GenericFormElement) elements.get(elementName);
  }

  /**
   * Adds a contained element after specified id.
   * 
   * @param element contained element.
   * @param name element name
   * @throws Exception 
   */
  public void addElementAfter(String id, GenericFormElement element, String afterId) throws Exception {
    LinkedMap newElements = new LinkedMap();  
      
    if (!getElements().containsKey(afterId))
    	throw new AraneaRuntimeException("The element '" + afterId + "' does not exist!");
    
    for (Iterator i = elements.entrySet().iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        
        newElements.put(entry.getKey(), entry.getValue());
        if (entry.getKey().equals(afterId)) {
          newElements.put(id, element);
        }
    }
    
    if (isInitialized())
      addWidget(id, element);
      
    elements = newElements;
  }
  
  /**
   * Adds a contained element before specified id.
   * 
   * @param element contained element.
   * @param name element name
   * @throws Exception 
   */
  public void addElementBefore(String id, GenericFormElement element, String beforeId) throws Exception {
    LinkedMap newElements = new LinkedMap();  
    
    if (!elements.containsKey(beforeId))
    	throw new AraneaRuntimeException("The element '" + beforeId + "' does not exist!");
      
    for (Iterator i = elements.entrySet().iterator(); i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        
        if (entry.getKey().equals(beforeId))
            newElements.put(id, element);
        newElements.put(entry.getKey(), entry.getValue());
    }
      
    if (isInitialized())
      addWidget(id, element);
    
    elements = newElements;
  }
  
  /**
   * Adds a contained element. (better use {@link #addElement(GenericFormElement)}, since it's safer.
   * 
   * @param element contained element.
   * @param id element id
   * @throws Exception 
   */
  public void addElement(String id, GenericFormElement element) throws Exception {
    elements.put(id, element);
    
    if (isInitialized())
      addWidget(id, element);
  }

  /**
   * Removes a contained element by its name.
   * @throws Exception 
   */
  public void removeElement(String id) throws Exception {
    elements.remove(id);
    
    if (isInitialized())
      removeWidget(id);
  }

  /**
   * Returns elements.
   * @return elements.
   */
  public Map getElements() {
    return new LinkedMap(elements);
  }
  
  /**
   * Calls {@link GenericFormElement#convert()} for all contained elements.
   */
  protected void convertInternal()  throws Exception  {
    for (Iterator i = elements.values().iterator(); i.hasNext();) {
      ((GenericFormElement) i.next()).convert();
    }
  }

  /**
   * Controls that the constraints and all subcontrols are valid.
   */
  protected boolean validateInternal() throws Exception  {
    for (Iterator i = elements.values().iterator(); i.hasNext();)
      ((GenericFormElement) i.next()).validate();  
    
    return super.validateInternal();
  }

  public void markBaseState() {
    for (Iterator i = elements.values().iterator(); i.hasNext();)
      ((GenericFormElement) i.next()).markBaseState();
  }   
  
  public boolean isStateChanged() {
  	boolean result = false;
    for (Iterator i = elements.values().iterator(); i.hasNext();)
    	result |= ((GenericFormElement) i.next()).isStateChanged();
    return result;
  }   
  
  public void setDisabled(boolean disabled) {
    for (Iterator i = elements.values().iterator(); i.hasNext();)
      ((GenericFormElement) i.next()).setDisabled(disabled);
  }
  
  public boolean isDisabled() {
  	boolean result = false;
    for (Iterator i = elements.values().iterator(); i.hasNext();)
    	result &= ((GenericFormElement) i.next()).isDisabled();
    return result;
  }
  
  public void accept(String id, FormElementVisitor visitor) {
    visitor.visit(id, this);

    visitor.pushContext(id, this);

    for (Iterator i = elements.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();

      String elementId = (String) entry.getKey();
      GenericFormElement element = (GenericFormElement) entry.getValue();

      element.accept(elementId, visitor);
    }

    visitor.popContext();
  }
  

  //*********************************************************************
  //* ELEMENT CREATION AND ADDITION
  //*********************************************************************

  /**
   * Adds a new composite element.
   * 
   * @param elementName the name of the form element.
   * 
   * @return a new composite element.
   * @throws Exception 
   */
  public FormWidget addSubForm(String id) throws Exception {
  	FormWidget result = new FormWidget();
  	addElement(id, result);
    return result;
  }

  /**
   * This method makes a {@link FormElement}adding a {@link org.araneaframework.uilib.form.converter.BaseConverter}to the
   * given {@link BaseControl}and {@link Data}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @return {@link FormElement}by given parameters.
   * @throws Exception 
   */
  public FormElement createElement(String labelId, Control control, Data data, Object initialValue, boolean mandatory) throws Exception {
  	if (data != null)
  		data.setValue(initialValue);
  	return createElement(labelId, control, data, mandatory);
  }

  
  /**
   * This method makes a {@link FormElement}adding a {@link org.araneaframework.uilib.form.converter.BaseConverter}to the
   * given {@link BaseControl}and {@link Data}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @return {@link FormElement}by given parameters.
   * @throws Exception 
   */
  public FormElement createElement(String labelId, Control control, Data data, boolean mandatory) throws Exception {
    FormElement result = new FormElement();
    
    result.setLabel(labelId);
    control.setMandatory(mandatory);    
    control.setLabel(labelId);
    result.setControl(control);
    if (data != null) {
      result.setData(data);
    }
    return result;
  }

  /**
   * This method adds a {@link FormElement}adding a {@link org.araneaframework.uilib.form.converter.BaseConverter}to the given
   * {@link BaseControl}and {@link Data}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @throws Exception 
   */
  public FormElement addElement(String elementName, String labelId, Control control, Data data, boolean mandatory) throws Exception {
  	FormElement result = createElement(labelId, control, data, mandatory);
    addElement(elementName, result);
    return result;
  }
  
  /**
   * This method adds a {@link FormElement}adding a {@link org.araneaframework.uilib.form.converter.BaseConverter}to the given
   * {@link BaseControl}and {@link Data}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @throws Exception 
   */
  public FormElement addElement(String elementName, String labelId, Control control, Data data, Object initialValue, boolean mandatory) throws Exception {
  	FormElement result = createElement(labelId, control, data, initialValue, mandatory);
    addElement(elementName, result);
    return result;
  }  


  //*********************************************************************
  //* TRAVERSAL METHODS
  //*********************************************************************

  /**
   * Returns form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return form element specified by full name.
   */
  public GenericFormElement getGenericElementByFullName(String fullName) {
    GenericFormElement result = null;

    String currentElementName = NameUtil.getNamePrefix(fullName);
    String nextElementNames = NameUtil.getNameSuffix(fullName);

    if ("".equals(nextElementNames)) {
      result = getElement(currentElementName);
    }
    else {
      FormWidget nextElement = (FormWidget) getElement(currentElementName);

      if (nextElement == null) return null;

      result = nextElement.getGenericElementByFullName(nextElementNames);
    }

    return result;
  }

  /**
   * Returns simple form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return simple form element specified by full name.
   */
  public FormElement getElementByFullName(String fullName) {
    return (FormElement) getGenericElementByFullName(fullName);
  }
  
  /**
   * Returns simple form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return simple form element specified by full name.
   */
  public FormWidget getSubFormByFullName(String fullName) {
    return (FormWidget) getGenericElementByFullName(fullName);
  }
  
  /**
   * Returns composite form element specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return composite form element specified by full name.
   */
  public Control getControlByFullName(String fullName) {
  	FormElement el = getElementByFullName(fullName);
    return (el == null) ? null : el.getControl();
  }
  
  /**
   * Returns form element value specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @return form element value specified by full name.
   */
  public Object getValueByFullName(String fullName) {
  	FormElement el = getElementByFullName(fullName);
    return (el == null) ? null : el.getData().getValue();  	    
  }

  /**
   * Sets form element value specified by full name.
   * @param fullName The full dot-separated name of the form element.
   * @param value form element value specified by full name.
   */  
  public void setValueByFullName(String fullName, Object value) {
  	FormElement el = getElementByFullName(fullName);
  	
  	if (el == null)
  		throw new InvalidFormElementNameException(fullName);
  	
  	el.getData().setValue(value);
  }
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  protected void init() throws Exception {
    super.init();
    
    for (Iterator i = getElements().entrySet().iterator(); i.hasNext();) {
      Map.Entry element = (Map.Entry) i.next();
      addWidget(element.getKey(), (Widget) element.getValue());
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
   * Represents a composite form element view model.
   * 
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
   * 
   */
  public class ViewModel extends GenericFormElement.ViewModel {
    

    /**
     * Returns the <code>Map</code> with element views.
     * @return the <code>Map</code> with element views.
     */
    public Map getElements() {
      return getChildren();
    } 
  }
  
}
