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

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.ActionListener;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.RenderStateAware;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.converter.BaseConverter;
import org.araneaframework.uilib.form.converter.ConverterFactory;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.util.ConfigurationContextUtil;
import org.araneaframework.uilib.util.Event;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * Represents a simple "leaf" form element that holds a {@link Control} and its {@link Data}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FormElement<C,D> extends GenericFormElement implements FormElementContext<C,D>, RenderStateAware {

  /**
   * The property key for custom {@link FormElementValidationErrorRenderer} that may be set for this {@link FormElement}
   * .
   * 
   * @since 1.1
   */
  public static final String ERROR_RENDERER_PROPERTY_KEY = "FormElementValidationErrorRenderer";

  private List<Event> initEvents = new ArrayList<Event>();

  protected Control<C> control;

  protected Converter<C, D> converter;

  protected Data<D> data;

  protected String label;

  private boolean rendered = false;

  private boolean ignoreEvents = true;

  protected boolean mandatory = false;

  protected boolean disabled;

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  /**
   * Returns control label.
   * 
   * @return control label.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Sets control label.
   * 
   * @param label control label.
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns {@link BaseConverter}.
   * 
   * @return the {@link BaseConverter}.
   */
  public Converter<C, D> getConverter() {
    return this.converter;
  }

  /**
   * Sets {@link BaseConverter}.
   * 
   * @param converter The {@link BaseConverter}to set.
   */
  public void setConverter(Converter<C, D> converter) {
    this.converter = converter;

    if (converter != null)
      converter.setFormElementCtx(this);
  }

  /**
   * Returns {@link Data}.
   * 
   * @return {@link Data}.
   */
  public Data<D> getData() {
    return this.data;
  }

  /**
   * Sets {@link Data}.
   * 
   * @param data {@link Data}.
   */
  @SuppressWarnings("unchecked")
  public void setData(Data<D> data) {
    this.data = data;
    data.setFormElementCtx((FormElementContext<Object, D>) this);
  }

  /**
   * Returns {@link Control}.
   * 
   * @return {@link Control}.
   */
  public Control<C> getControl() {
    return this.control;
  }

  /**
   * Sets {@link Control}.
   * 
   * @param control {@link Control}.
   */
  @SuppressWarnings("unchecked")
  public void setControl(Control<C> control) {
    Assert.notNullParam(control, "control");

    destroyControl();
    this.control = control;
    control.setFormElementCtx((FormElementContext<C, Object>) this);

    if (isInitialized()) {
      control._getComponent().init(getScope(), getEnvironment());
    }
  }

  /** @since 1.1 */
  protected void destroyControl() {
    if (this.control != null && this.control.isAlive()) {
      this.control._getComponent().destroy();
    }
  }

  /**
   * Finds a {@link BaseConverter}corresponding to current control and data item.
   * 
   * @throws AraneaRuntimeException if converter cannot be found.
   */
  @SuppressWarnings("unchecked")
  public Converter<C, D> findConverter() {
    ConfigurationContext confCtx = getEnvironment().requireEntry(ConfigurationContext.class);

    try {
      return ConverterFactory.getInstance(confCtx).findConverter(getControl().getRawValueType(),
          getData().getValueType());

    } catch (ConverterNotFoundException e) {
      throw new AraneaRuntimeException(
          "Could not find a field value " + "converter for field " + getScope().toString(), e);
    }
  }

  /**
   * Returns whether the form element was present in the last request.
   * 
   * @return whether the form element was present in the last request.
   */
  public boolean isRead() {
    return getControl() != null && getControl().isRead();
  }

  @Override
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  @Override
  public boolean isDisabled() {
    return this.disabled;
  }

  @Override
  public void markBaseState() {
    if (getData() != null) {
      getData().markBaseState();
    }
  }

  @Override
  public void restoreBaseState() {
    if (getData() != null) {
      getData().restoreBaseState();
    }
  }

  @Override
  public boolean isStateChanged() {
    return getData() != null ? getData().isStateChanged() : false;
  }

  @Override
  public D getValue() {
    return getData() != null ? this.data.getValue() : null;
  }

  public void setValue(D value) {
    if (getData() != null) {
      getData().setValue(value);
    }
  }

  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  @Override
  public void addError(String error) {
    super.addError(error);
    getFormElementValidationErrorRenderer().addError(this, error);
  }

  @Override
  public void clearErrors() {
    getFormElementValidationErrorRenderer().clearErrors(this);
    super.clearErrors();
  }

  /**
   * Sets the action listener that deals with background validation of form. It should be used when custom background
   * validation logic or behavior is wanted. Just for using default {@link FormElementValidationActionListener}, only
   * {@link FormElement#setBackgroundValidation(boolean)} needs to be called with parameter <code>true</code>.
   * 
   * @param actionListener custom listener that should handle validation of this {@link FormElement}
   * @since 1.1
   */
  public void setBackgroundValidationListener(ActionListener actionListener) {
    clearActionListeners(SEAMLESS_VALIDATION_ACTION_ID);
    addActionListener(SEAMLESS_VALIDATION_ACTION_ID, actionListener);
  }

  /**
   * @return {@link FormElementValidationErrorRenderer} which will take care of rendering validation error messages
   *         produced by this {@link FormElement}.
   * @since 1.1
   */
  public FormElementValidationErrorRenderer getFormElementValidationErrorRenderer() {
    FormElementValidationErrorRenderer result = ConfigurationContextUtil
        .getFormElementValidationErrorRenderer(UilibEnvironmentUtil.getConfiguration(getEnvironment()));
    if (result == null) {
      result = (FormElementValidationErrorRenderer) getProperty(ERROR_RENDERER_PROPERTY_KEY);
    }

    if (result == null) {
      return StandardFormElementValidationErrorRenderer.INSTANCE;
    }

    return result;
  }

  /** @since 1.1 */
  public void setFormElementValidationErrorRenderer(FormElementValidationErrorRenderer renderer) {
    setProperty(ERROR_RENDERER_PROPERTY_KEY, renderer);
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  @Override
  protected void update(InputData input) throws Exception {
    if (isDisabled() || !isRendered()) {
      setIgnoreEvents(true);
      return;
    }
    setIgnoreEvents(false);

    super.update(input);

    // There is only point to read from request if we have a control
    if (getControl() != null) {
      // Read the control
      getControl()._getWidget().update(input);
    }
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (!isDisabled() && isRendered()) {
      super.action(path, input, output);
    }
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    if (!path.hasNext() && !isDisabled() && !isIgnoreEvents())
      getControl()._getWidget().event(path, input);
  }

  @Override
  protected void handleAction(InputData input, OutputData output) throws Exception {
    update(input);
    super.handleAction(input, output);
    if (control != null && !getActionId(input).equals(SEAMLESS_VALIDATION_ACTION_ID))
      control._getService().action(null, input, output);
  }

  @Override
  public Environment getConstraintEnvironment() {
    return new StandardEnvironment(super.getConstraintEnvironment(), FormElementContext.class, this);
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   * @throws Exception
   */
  @Override
  public Object getViewModel() throws Exception {
    return new ViewModel();
  }

  /** @since 1.0.5 */
  public void addInitEvent(Event event) {
    if (isAlive()) {
      event.run();
    } else if (!isInitialized()) {
      if (initEvents == null)
        initEvents = new ArrayList<Event>();
      initEvents.add(event);
    }
  }

  @Override
  protected void init() throws Exception {
    super.init();

    if (getConverter() == null && getData() != null && getControl() != null)
      setConverter(findConverter());

    if (getControl() != null)
      getControl()._getComponent().init(getScope(), getEnvironment());

    runInitEvents();
    setBackgroundValidationListener(getDefaultBackgroundValidationListener());
  }

  /**
   * Returns new instance of {@link FormElementValidationActionListener} tied to this {@link FormElement}.
   * 
   * @since 1.1
   */
  protected FormElementValidationActionListener<C, D> getDefaultBackgroundValidationListener() {
    return new FormElementValidationActionListener<C, D>(this);
  }

  @Override
  protected void destroy() throws Exception {
    destroyControl();
  }

  /**
   * Uses {@link BaseConverter}to convert the {@link BaseControl}value to the {@link Data}value.
   */
  @Override
  protected void convertInternal() {
    D newDataValue = null;

    // There is only point to convert and set the data if it is present
    if (getData() != null && getControl() != null) {

      getControl().convert();

      // The data should be set only if control is valid
      if (isValid()) {
        // We assume that the convertor is present, if control and data are
        // here
        newDataValue = getConverter().convert(getControl().getRawValue());
      }
    }

    if (getData() != null && isValid()) {
      // converting should not affect Control's value -- so setDataValue() instead of setValue()
      getData().setDataValue(newDataValue);
    }
  }

  @Override
  protected boolean validateInternal() throws Exception {
    if (getControl() != null)
      getControl().validate();

    return super.validateInternal();
  }

  @Override
  public void accept(String id, FormElementVisitor visitor) {
    visitor.visit(id, this);
  }

  /**
   * Called from {@link FormElement#init()} to run queued events.
   * 
   * @since 1.0.5
   */
  protected void runInitEvents() {
    if (initEvents != null) {
      for (Runnable event : initEvents) {
        event.run();
      }
    }
    initEvents = null;
  }

  /**
   * Returns whether this {@link GenericFormElement} was rendered in response. Only formelements that were rendered
   * should be read from request.
   * 
   * @return whether this {@link GenericFormElement} was rendered
   */
  public boolean isRendered() {
    return this.rendered;
  }

  /**
   * Marks status of this {@link FormElement} rendered. Only rendered {@link FormElement}s may read the data from
   * subsequent request.
   */
  public void rendered() {
    _setRendered(true);
  }

  /**
   * @since 1.1
   */
  public void _setRendered(boolean rendered) {
    this.rendered = rendered;
  }

  /**
   * When this returns true,
   * 
   * @since 1.1
   */
  protected boolean isIgnoreEvents() {
    return ignoreEvents;
  }

  /**
   * When set
   * 
   * @since 1.1
   */
  protected void setIgnoreEvents(boolean ignoreEvents) {
    this.ignoreEvents = ignoreEvents;
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Represents a simple form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends GenericFormElement.ViewModel {

    private Control.ViewModel control;

    private String label;

    private boolean valid;

    private Object value;

    protected boolean mandatory;

    /**
     * Takes an outer class snapshot.
     * 
     * @throws Exception
     */
    public ViewModel() throws Exception {
      this.control = (Control.ViewModel) FormElement.this.getControl()._getViewable().getViewModel();
      this.label = FormElement.this.getLabel();
      this.valid = FormElement.this.isValid();
      this.value = FormElement.this.getData() != null ? FormElement.this.getData().getValue() : null;
      this.mandatory = FormElement.this.mandatory;
    }

    /**
     * Returns control.
     * 
     * @return control.
     */
    public Control.ViewModel getControl() {
      return this.control;
    }

    /**
     * Returns label.
     * 
     * @return label.
     */
    public String getLabel() {
      return this.label;
    }

    /**
     * @return {@link FormElementValidationErrorRenderer} which will take care of rendering validation error messages
     *         produced by {@link FormElement} represented by this {@link FormElement.ViewModel}
     * @since 1.1
     */
    public FormElementValidationErrorRenderer getFormElementValidationErrorRenderer() {
      return FormElement.this.getFormElementValidationErrorRenderer();
    }

    /**
     * Returns whether the element is valid.
     * 
     * @return whether the element is valid.
     */
    public boolean isValid() {
      return valid;
    }

    public boolean isMandatory() {
      return this.mandatory;
    }

    public Object getValue() {
      return this.value;
    }
  }
}
