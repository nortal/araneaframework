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

package org.araneaframework.uilib.form.control;

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Scope;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This class is a control generalization that provides methods common to all HTML form controls. The methods include
 * XML output and error handling.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BaseControl<T> extends BaseApplicationWidget implements Serializable, Control<T> {

  protected T value;

  protected Object innerData;

  protected boolean isReadFromRequest;

  private FormElementContext<T, Object> feCtx;

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  /**
   * Returns the value of the control (value read from the request). Type of value depends on the type of control.
   * 
   * @return Returns the value of the control (value read from the request).
   */
  public T getRawValue() {
    return this.value;
  }

  /**
   * Sets the raw control value (as it was read from request/written to response). It is usually set by
   * {@link org.araneaframework.uilib.form.Converter} when value of {@link FormElement} that owns this
   * {@link BaseControl} changes.
   * 
   * @param value control value.
   * @see #getRawValue()
   */
  public void setRawValue(T value) {
    BaseControl.this.value = value;
  }

  @Override
  public BaseControl<T>.ViewModel getViewModel() {
    return new ViewModel();
  }

  public void setFormElementCtx(FormElementContext<T, Object> formElementContext) {
    this.feCtx = formElementContext;
  }

  public FormElementContext<T, Object> getFormElementCtx() {
    return this.feCtx;
  }

  /**
   * By default the control is considered read if it has a not null data read from request.
   */
  public boolean isRead() {
    return this.isReadFromRequest;
  }

  // *********************************************************************
  // * OVERRIDABLE METHODS
  // *********************************************************************

  public void convertAndValidate() {
    convert();
    validate();
  }

  public void convert() {}

  public void validate() {}

  /**
   * Sub controls should implement this method to store the value of this control. The <code>request</code> is provided
   * to read the appropriate value.
   * 
   * @param request The HTTP request containing request data.
   */
  protected void readFromRequest(HttpInputData request) {}

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  @Override
  protected void init() throws Exception {
    super.init();
    Assert.notNull(this, getFormElementCtx(), "Form element context must be assigned to the control before it can be "
        + "initialized! Make sure that the control is associated with a form element!");
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (!isDisabled()) {
      super.action(path, input, output);
    }
  }

  @Override
  protected void update(InputData input) throws Exception {
    super.update(input);
    if (!isDisabled()) {
      readFromRequest((HttpInputData) input);
    }
  }

  @Override
  protected void handleEvent(InputData input) throws Exception {
    if (!isDisabled()) {
      super.handleEvent(input);
    }
  }

  /**
   * Returns control label using form element context.
   * 
   * @return control label.
   */
  protected String getLabel() {
    return this.feCtx.getLabel();
  }

  /**
   * Returns whether the control is mandatory, i.e. must be inserted by user.
   * 
   * @return whether the control is mandatory, i.e. must be inserted by user.
   */
  protected boolean isMandatory() {
    return this.feCtx.isMandatory();
  }

  protected void addError(String error) {
    this.feCtx.addError(error);
  }

  protected void addErrorWithLabel(String errorMsg) {
    addErrorWithLabel(errorMsg, null);
  }

  protected void addErrorWithLabel(String errorMsg, Object lastParam) {
    addErrorWithLabel(errorMsg, lastParam, null);
  }

  protected void addErrorWithLabel(String errorMsg, Object lastParam1, Object lastParam2) {
    addError(MessageUtil.localizeAndFormat(getEnvironment(), errorMsg, MessageUtil.localize(getLabel(),
        getEnvironment()), lastParam1, lastParam2));
  }

  public boolean isDisabled() {
    return this.feCtx.isDisabled();
  }

  /**
   * Returns whether the control state is valid using form element context.
   * 
   * @return A boolean that is <code>true</code> when the control data is valid.
   */
  protected boolean isValid() {
    return this.feCtx.isValid();
  }

  @Override
  public Widget.Interface _getWidget() {
    return new WidgetImpl();
  }

  protected class WidgetImpl extends BaseWidget.WidgetImpl {

    @Override
    public void update(InputData input) {
      isReadFromRequest = false;
      super.update(input);
    }
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Represents a general control view model.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel implements Control.ViewModel {

    protected String controlType;

    protected boolean mandatory;

    protected boolean disabled;

    protected String label;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      Class<?> clazz = BaseControl.this.getClass();

      // Recognizes Controls that are defined as (anonymous) nested classes:
      if (clazz.getDeclaringClass() != null) {
        clazz = clazz.getDeclaringClass();
      }

      this.controlType = clazz.getSimpleName();
      this.mandatory = BaseControl.this.isMandatory();
      this.disabled = BaseControl.this.isDisabled();
      this.label = BaseControl.this.getLabel();
    }

    public Scope getScope() {
      return BaseControl.this.getScope();
    }

    public String getControlType() {
      return this.controlType;
    }

    public boolean isMandatory() {
      return this.mandatory;
    }

    public String getLabel() {
      return this.label;
    }

    public boolean isDisabled() {
      return this.disabled;
    }

  }
}
