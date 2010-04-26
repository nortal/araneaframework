/*
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
 */

package org.araneaframework.uilib.form;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Path;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.InvalidFormElementNameException;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.list.util.NestedFormUtil;
import org.araneaframework.uilib.util.NameUtil;

/**
 * This class represents a form element that can contain other form elements.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FormWidget extends GenericFormElement implements FormContext {

  protected Map<String, GenericFormElement> elements = new LinkedHashMap<String, GenericFormElement>();

  @Override
  protected void init() throws Exception {
    super.init();

    for (Map.Entry<String, GenericFormElement> element : getElements().entrySet()) {
      addWidget(element.getKey(), element.getValue());
    }
  }

  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), FormContext.class, this);
  }

  @Override
  public void clearErrors() {
    getMessageCtx().hideMessages(MessageContext.ERROR_TYPE, getErrors());
    super.clearErrors();
    for (GenericFormElement element : getElements().values()) {
      element.clearErrors();
    }
  }

  @Override
  public boolean isValid() {
    boolean result = super.isValid();
    for (GenericFormElement element : getElements().values()) {
      result &= element.isValid();
      if (!result) {
        break;
      }
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
    return elementName.indexOf('.') == -1 ? this.elements.get(elementName) : getGenericElementByFullName(elementName);
  }

  /**
   * Adds a contained element with given id after the element with specified id.
   * 
   * @param id The Id of the element to add.
   * @param element The element to add.
   * @param afterId The Id of an element after which <code>element</code> should be added.
   */
  public void addElementAfter(String id, GenericFormElement element, String afterId) {
    Assert.notEmptyParam(afterId, "afterId");

    // form is now the actual form to add element into:
    FormWidget form = NestedFormUtil.getDeepestForm(afterId, this);
    afterId = NameUtil.getLastName(afterId);

    form.addFlatElement(id, element, afterId, false);
  }

  /**
   * Adds a contained element with given id before the element with specified id. Should be only used in RARE cases
   * where internal order of elements matters for some reason.
   * 
   * @param id The Id of the element to add.
   * @param element The element to add.
   * @param beforeId The Id of an element before which <code>element</code> should be added.
   */
  public void addElementBefore(String id, GenericFormElement element, String beforeId) {
    Assert.notEmptyParam(beforeId, "beforeId");
    Assert.isTrue(id.indexOf(Path.SEPARATOR) == -1,
        "addElementBefore() does not accept nested 'id' parameter.");

    // form is now the actual form to add element into:
    FormWidget form = NestedFormUtil.getDeepestForm(beforeId, this);
    form.addFlatElement(id, element, beforeId, true);
  }

  private void addFlatElement(String id, GenericFormElement element, String existingId, boolean before) {
    Assert.notEmptyParam(id, "id");
    Assert.notNullParam(element, "element");
    Assert.isTrue(existingId.indexOf(Path.SEPARATOR) == -1,
        "addFlatElement() method does not accept nested 'exstingId': '" + existingId + "'!");

    if (!getElements().containsKey(existingId)) {
      throw new AraneaRuntimeException("The element '" + existingId + "' does not exist!");
    }

    Map<String, GenericFormElement> newElements = new LinkedHashMap<String, GenericFormElement>();

    for (Map.Entry<String, GenericFormElement> entry : this.elements.entrySet()) {
      boolean match = entry.getKey().equals(existingId);
      if (match && before) {
        newElements.put(id, element);
      }
      newElements.put(entry.getKey(), entry.getValue());
      if (match && !before) {
        newElements.put(id, element);
      }
    }

    if (isInitialized()) {
      addWidget(id, element);
    }

    this.elements = newElements;
  }

  /**
   * Adds a contained element.
   * 
   * @param element contained element.
   * @param id element id
   */
  public void addElement(String id, GenericFormElement element) {
    Assert.notEmptyParam(id, "id");
    Assert.notNullParam(element, "element");

    if (StringUtils.contains(id, BeanUtil.NESTED_DELIM)) {
      NestedFormUtil.addElement(this, id, element);
    } else {
      this.elements.put(id, element);
      if (isInitialized()) {
        addWidget(id, element);
      }
    }
  }

  /**
   * Removes a contained element by its name.
   */
  public void removeElement(String id) {
    Assert.notEmptyParam(id, "id");
    this.elements.remove(id);
    if (isInitialized()) {
      removeWidget(id);
    }
  }

  /**
   * Provides the current form elements.
   * 
   * @return The current form elements.
   */
  public Map<String, GenericFormElement> getElements() {
    return new LinkedHashMap<String, GenericFormElement>(this.elements);
  }

  @Override
  protected void convertInternal() throws Exception {
    for (GenericFormElement element : this.elements.values()) {
      element.convert();
    }
  }

  @Override
  protected boolean validateInternal() throws Exception {
    for (GenericFormElement element : this.elements.values()) {
      element.validate();
    }
    return super.validateInternal();
  }

  @Override
  public void markBaseState() {
    for (GenericFormElement element : this.elements.values()) {
      element.markBaseState();
    }
  }

  @Override
  public void restoreBaseState() {
    for (GenericFormElement element : this.elements.values()) {
      element.restoreBaseState();
    }
  }

  @Override
  public boolean isStateChanged() {
    boolean result = false;
    for (GenericFormElement element : this.elements.values()) {
      result |= element.isStateChanged();
    }
    return result;
  }

  @Override
  public void setDisabled(boolean disabled) {
    for (GenericFormElement element : this.elements.values()) {
      element.setDisabled(disabled);
    }
  }

  @Override
  public boolean isDisabled() {
    boolean result = false;
    for (GenericFormElement element : this.elements.values()) {
      result &= element.isDisabled();
    }
    return result;
  }

  @Override
  public void accept(String id, FormElementVisitor visitor) {
    visitor.visit(id, this);
    visitor.pushContext(id, this);

    for (Map.Entry<String, GenericFormElement> entry : elements.entrySet()) {
      String elementId = entry.getKey();
      GenericFormElement element = entry.getValue();
      element.accept(elementId, visitor);
    }

    visitor.popContext();
  }

  // *********************************************************************
  // * ELEMENT CREATION AND ADDITION
  // *********************************************************************

  /**
   * Adds a new sub-form to this {@link FormWidget}. Since Aranea 2.0 this method accepts nested paths. The nested paths
   * and sub-forms are created when they don't exist. If path is empty, no changes will be made.
   * 
   * @param path The (simple or nested) path of sub-form to add. Nested path has dots separating sub-form IDs in the
   *          order they will be created (the second sub-form will be the sub-form of the first sub-form, etc).
   * @return If path is empty or simple (not nested) then the current form widget is returned. Otherwise, the last
   *         created sub-form widget is returned.
   */
  public FormWidget addSubForm(String path) {
    FormWidget result = this;

    if (!StringUtils.isEmpty(path)) {
      StringTokenizer tokens = new StringTokenizer(path, BeanUtil.NESTED_DELIM);

      while (tokens.hasMoreTokens()) {
        String subFormId = tokens.nextToken();
        FormWidget subForm = result.getSubFormByFullName(subFormId);

        if (subForm == null) {
          result = new FormWidget();
          addElement(subFormId, result);
        } else {
          result = subForm;
        }
      }
    }

    return result;
  }

  /**
   * This method makes a {@link FormElement} with given {@link Control} and {@link Data}.
   * 
   * @param labelId localized label id
   * @param control the type of control
   * @param data the type of data
   * @param initialValue initial value for data
   * @param mandatory whether the element must be filled in
   * @return {@link FormElement} with given configuration
   */
  public <C, D> FormElement<C, D> createElement(String labelId, Control<C> control, Data<D> data, D initialValue,
      boolean mandatory) {
    if (data != null) {
      data.setValue(initialValue);
    }
    return createElement(labelId, control, data, mandatory);
  }

  /**
   * This method makes a {@link FormElement} with given {@link Control} and {@link Data}.
   * 
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @return {@link FormElement} with given configuration
   */
  public <C, D> FormElement<C, D> createElement(String labelId, Control<C> control, Data<D> data, boolean mandatory) {
    Assert.notNullParam(control, "control");

    FormElement<C, D> result = new FormElement<C, D>();

    result.setLabel(labelId);
    result.setMandatory(mandatory);
    result.setControl(control);
    if (data != null) {
      result.setData(data);
    }
    return result;
  }

  /**
   * This method makes a non-mandatory {@link FormElement} with given {@link Control} and {@link Data}.
   * 
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @return {@link FormElement} with given configuration
   */
  public <C, D> FormElement<C, D> createElement(String labelId, Control<C> control, Data<D> data) {
    return createElement(labelId, control, data, false);
  }

  /**
   * This method makes a non-mandatory {@link FormElement} with given {@link Control}. The form element is not supposed
   * to have a value (e.g. buttons).
   * 
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @return {@link FormElement} with given configuration
   * @since 2.0
   */
  public <C, D> FormElement<C, D> createElement(String labelId, Control<C> control) {
    return createElement(labelId, control, null, false);
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}. This method is usually used with
   * {@link ButtonControl}s or similar controls which need no data.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @since 2.0
   */
  public <C, D> FormElement<C, D> addElement(String elementName, String labelId, Control<C> control) {
    return addElement(elementName, labelId, control, null, false);
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   */
  public <C, D> FormElement<C, D> addElement(String elementName, String labelId, Control<C> control, Data<D> data) {
    return addElement(elementName, labelId, control, data, false);
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName The name of the form element.
   * @param labelId The ID of the localized label.
   * @param control The type of control data.
   * @param data The type of data.
   * @param initialValue The initial value for this element.
   */
  public <C, D> FormElement<C, D> addElement(String elementName, String labelId, Control<C> control, Data<D> data,
      D initialValue) {
    return addElement(elementName, labelId, control, data, initialValue, false);
  }

  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   */
  public <C, D> FormElement<C, D> addElement(String elementName, String labelId, Control<C> control, Data<D> data,
      boolean mandatory) {
    return addElement(elementName, labelId, control, data, null, mandatory);
  }


  /**
   * This method adds a {@link FormElement} to this {@link FormWidget}.
   * 
   * @param elementName the name of the form element.
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param initialValue The initial value for this element.
   * @param mandatory whether the element must be present in request.
   */
  public <C, D> FormElement<C, D> addElement(String elementName, String labelId, Control<C> control, Data<D> data,
      D initialValue, boolean mandatory) {
    FormElement<C, D> result = createElement(labelId, control, data, initialValue, mandatory);
    addElement(elementName, result);
    return result;
  }

  // *********************************************************************
  // * TRAVERSAL METHODS
  // *********************************************************************

  /**
   * Returns form element specified by full name.
   * 
   * @param fullName The full dot-separated name of the form element.
   * @return form element specified by full name.
   */
  public GenericFormElement getGenericElementByFullName(String fullName) {
    Assert.notEmptyParam(fullName, "fullName");

    GenericFormElement result = null;

    String currentElementName = NameUtil.getNamePrefix(fullName);
    String nextElementNames = NameUtil.getNameSuffix(fullName);

    if (nextElementNames == null) {
      result = getElement(currentElementName);
    } else {
      FormWidget nextElement = (FormWidget) getElement(currentElementName);

      if (nextElement != null) {
        result = nextElement.getGenericElementByFullName(nextElementNames);
      }
    }

    return result;
  }

  /**
   * Returns simple form element specified by full name.
   * 
   * @param fullName The full dot-separated name of the form element.
   * @return simple form element specified by full name.
   */
  @SuppressWarnings("unchecked")
  public <T> FormElement<?, T> getElementByFullName(String fullName) {
    return (FormElement<?, T>) getGenericElementByFullName(fullName);
  }

  /**
   * Returns simple form element specified by full name.
   * 
   * @param fullName The full dot-separated name of the form element.
   * @return simple form element specified by full name.
   */
  public FormWidget getSubFormByFullName(String fullName) {
    return (FormWidget) getGenericElementByFullName(fullName);
  }

  /**
   * Returns composite form element specified by full name.
   * 
   * @param fullName The full dot-separated name of the form element.
   * @return composite form element specified by full name.
   */
  public Control<?> getControlByFullName(String fullName) {
    FormElement<?, ?> el = getElementByFullName(fullName);
    return (el == null) ? null : el.getControl();
  }

  /**
   * Returns form element value specified by full name.
   * 
   * @param fullName The full dot-separated name of the form element.
   * @return form element value specified by full name.
   */
  public Object getValueByFullName(String fullName) {
    FormElement<?, ?> el = getElementByFullName(fullName);
    return (el == null) ? null : el.getValue();
  }

  /**
   * Sets form element value specified by full name.
   * 
   * @param fullName The full dot-separated name of the form element.
   * @param value form element value specified by full name.
   */
  public <T> void setValueByFullName(String fullName, T value) {
    FormElement<?, T> el = getElementByFullName(fullName);

    if (el == null)
      throw new InvalidFormElementNameException(fullName);

    el.getData().setValue(value);
  }

  @Override
  public void addError(String error) {
    super.addError(error);
    getMessageCtx().showErrorMessage(error);
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  @Override
  public Object getViewModel() {
    return new ViewModel();
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Represents a composite form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   * 
   */
  public class ViewModel extends GenericFormElement.ViewModel {

    /**
     * Returns the <code>Map</code> with element views.
     * 
     * @return the <code>Map</code> with element views.
     */
    public Map<String, Component> getElements() {
      return getChildren();
    }
  }
}
