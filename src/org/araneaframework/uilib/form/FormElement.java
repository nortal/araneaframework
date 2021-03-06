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
import org.araneaframework.framework.MessageContext.MessageData;
import org.araneaframework.framework.core.RenderStateAware;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.converter.BaseConverter;
import org.araneaframework.uilib.form.converter.ConverterFactory;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.util.ConfigurationUtil;
import org.araneaframework.uilib.util.Event;

/**
 * Represents a simple "leaf" form element that holds a {@link Control} and its {@link Data}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
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

  private boolean rendered;

  private boolean ignoreEvents = true;

  protected boolean mandatory;

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
    if (converter != null) {
      converter.setFormElementCtx(this);
    }
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
    if (data != null) {
      data.setFormElementCtx(FormElementContext.class.cast(this));
    }
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
    control.setFormElementCtx(FormElementContext.class.cast(this));

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

    DataType source = this.control.getRawValueType();
    DataType dest = this.data.getValueType();

    try {
      return ConverterFactory.getInstance(confCtx).findConverter(source, dest);
    } catch (ConverterNotFoundException e) {
      throw new AraneaRuntimeException("Could not find a field value converter (" + source + "->" + dest
          + ") for field '" + getScope().toString() + "'!", e);
    }
  }

  /**
   * Returns whether the form element was present in the last request.
   * 
   * @return whether the form element was present in the last request.
   */
  public boolean isRead() {
    return this.control != null && this.control.isRead();
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
    if (this.data != null) {
      this.data.markBaseState();
    }
  }

  @Override
  public void restoreBaseState() {
    if (this.data != null) {
      this.data.restoreBaseState();
    }
  }

  @Override
  public boolean isStateChanged() {
    return this.data != null && this.data.isStateChanged();
  }

  @Override
  public D getValue() {
    return this.data != null ? this.data.getValue() : null;
  }

  public void setValue(D value) {
    if (this.data != null) {
      this.data.setValue(value);
    }
  }

  public boolean isMandatory() {
    return this.mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  @Override
  public void addError(MessageData error) {
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
    FormElementValidationErrorRenderer result = (FormElementValidationErrorRenderer) getProperty(ERROR_RENDERER_PROPERTY_KEY);

    if (result == null) {
      result = ConfigurationUtil.getFormElementErrorRenderer(getEnvironment());
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
    if (this.control != null) {
      // Read the control
      this.control._getWidget().update(input);
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
    if (!path.hasNext() && !isDisabled() && !isIgnoreEvents()) {
      this.control._getWidget().event(path, input);
    }
  }

  @Override
  protected void handleAction(InputData input, OutputData output) throws Exception {
    update(input);
    super.handleAction(input, output);
    if (this.control != null && !getActionId(input).equals(SEAMLESS_VALIDATION_ACTION_ID)) {
      this.control._getService().action(null, input, output);
    }
  }

  @Override
  public Environment getConstraintEnvironment() {
    return new StandardEnvironment(super.getConstraintEnvironment(), FormElementContext.class, this);
  }

  /**
   * Returns {@link ViewModel}. {@inheritDoc}
   * 
   * @return {@link ViewModel}.
   * @throws Exception
   */
  @Override
  public Object getViewModel() {
    return new ViewModel();
  }

  /**
   * Adds an event that will be run when form element is initialized. When it is already initialized, the event will be
   * run immediately.
   * 
   * @param event The event to run.
   * @since 1.0.5
   */
  public void addInitEvent(Event event) {
    if (isAlive()) {
      event.run();
    } else if (!isInitialized()) {
      if (this.initEvents == null) {
        this.initEvents = new ArrayList<Event>();
      }
      this.initEvents.add(event);
    }
  }

  @Override
  protected void init() throws Exception {
    super.init();

    if (getConverter() == null && this.data != null && this.control != null) {
      setConverter(findConverter());
    }

    if (this.control != null) {
      this.control._getComponent().init(getScope(), getEnvironment());
    }

    runInitEvents();
    setBackgroundValidationListener(getDefaultBackgroundValidationListener());
  }

  /**
   * Returns new instance of {@link FormElementValidationActionListener} tied to this {@link FormElement}.
   * 
   * @return A new instance of <code>FormElementValidationActionListener</code> for this form element.
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
   * Uses {@link BaseConverter} to convert the {@link BaseControl} value to the {@link Data} value.
   */
  @Override
  protected void convertInternal() {
    D newDataValue = null;

    // There is only point to convert and set the data if it is present
    if (this.data != null && this.control != null) {

      this.control.convert();

      // The data should be set only if control is valid
      if (isValid()) {
        // We assume that the convertor is present, if control and data are here
        newDataValue = getConverter().convert(this.control.getRawValue());
      }
    }

    if (this.data != null && isValid()) {
      // converting should not affect Control's value -- so setDataValue() instead of setValue()
      this.data.setDataValue(newDataValue);
    }
  }

  @Override
  protected boolean validateInternal() throws Exception {
    if (this.control != null) {
      this.control.validate();
    }

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
   * @see #addInitEvent(Event)
   */
  protected void runInitEvents() {
    if (this.initEvents != null) {
      for (Runnable event : this.initEvents) {
        event.run();
      }
    }
    this.initEvents = null;
  }

  /**
   * Returns whether this {@link GenericFormElement} was rendered in response. Only form elements that were rendered
   * should be read from request.
   * 
   * @return Whether this {@link GenericFormElement} was rendered
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
   * Provides whether this form element is ignoring incoming events.
   * 
   * @return A boolean that is <code>true</code> when this form element is ignoring incoming events.
   * @since 1.1
   */
  protected boolean isIgnoreEvents() {
    return this.ignoreEvents;
  }

  /**
   * Sets a flag that controls whether this form element ignores incoming events. It does so until this flag is manually
   * changed again. By default, no event will be ignored. When incoming event is ignored, child components, such as the
   * control, won't receive it either.
   * 
   * @param ignoreEvents A boolean that is <code>true</code> to make this form element ignore incoming events.
   * @since 1.1
   */
  protected void setIgnoreEvents(boolean ignoreEvents) {
    this.ignoreEvents = ignoreEvents;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(":[").append(this.data == null ? "no_data" : this.data).append("]=").append(this.label);
    return sb.toString();
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * Represents a simple form element view model.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends GenericFormElement.ViewModel {

    private Control.ViewModel control;

    private String label;

    private boolean valid;

    private boolean disabled;

    private Object value;

    protected boolean mandatory;

    /**
     * Takes an outer class snapshot.
     * 
     * @throws Exception
     */
    public ViewModel() {
      this.control = (Control.ViewModel) FormElement.this.control._getViewable().getViewModel();
      this.label = FormElement.this.getLabel();
      this.valid = FormElement.this.isValid();
      this.value = FormElement.this.data != null ? FormElement.this.data.getValue() : null;
      this.mandatory = FormElement.this.isMandatory();
      this.disabled = FormElement.this.isDisabled();
    }

    /**
     * Returns the view model of the control tied to this form element.
     * 
     * @return The view model of the control.
     */
    public Control.ViewModel getControl() {
      return this.control;
    }

    /**
     * Returns label for this form element.
     * 
     * @return The label.
     */
    public String getLabel() {
      return this.label;
    }

    /**
     * Provides <code>FormElementValidationErrorRenderer</code>, which will take care of rendering validation error
     * messages produced by this {@link FormElement} represented by this view model.
     * 
     * @return The form element validation status renderer.
     * @since 1.1
     */
    public FormElementValidationErrorRenderer getFormElementValidationErrorRenderer() {
      return FormElement.this.getFormElementValidationErrorRenderer();
    }

    public String getRenderedErrorMessages() {
      return FormElement.this.getFormElementValidationErrorRenderer().getClientRenderText(FormElement.this);
    }

    /**
     * Returns whether the element is valid.
     * 
     * @return A boolean that is <code>true</code> when this element is valid.
     */
    public boolean isValid() {
      return this.valid;
    }

    /**
     * Returns whether this element is mandatory to fill in with correct data to reach valid form status.
     * 
     * @return A boolean that is <code>true</code> when this element is mandatory.
     */
    public boolean isMandatory() {
      return this.mandatory;
    }

    /**
     * Returns whether this element is disabled or not.
     * 
     * @return A boolean that is <code>true</code> when this element is disabled.
     */
    public boolean isDisabled() {
      return this.disabled;
    }

    /**
     * Provides the current value of this form element.
     * 
     * @return The current value.
     */
    public Object getValue() {
      return this.value;
    }
  }
}
