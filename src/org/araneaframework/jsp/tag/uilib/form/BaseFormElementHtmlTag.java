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

package org.araneaframework.jsp.tag.uilib.form;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Base form element tag.
 * 
 * @author Oleg Mürk
 */
@SuppressWarnings("unchecked")
public class BaseFormElementHtmlTag extends PresentationTag implements FormElementTagInterface {

  /** @since 1.1 */
  public static final String FORMELEMENT_SPAN_PREFIX = "fe-span-";

  public static final String RENDER_DISABLED_DISABLED = "disabled";

  public static final String RENDER_DISABLED_READONLY = "readonly";

  protected String formFullId;

  protected FormWidget.ViewModel formViewModel;

  protected FormElement.ViewModel formElementViewModel;

  protected Control.ViewModel controlViewModel;

  protected String localizedLabel;

  protected String accessKeyId;

  protected List<String> updateRegionNames;

  private boolean hasElementContextSpan = true;

  protected String derivedId;

  // Attributes

  protected boolean events = true;

  protected boolean validateOnEvent = false;

  /** @since 1.1 */
  protected boolean backgroundValidation = false;

  protected String accessKey;

  protected String id;

  protected String tabindex;

  protected String updateRegions;

  protected String globalUpdateRegions;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get form data
    this.formFullId = (String) requireContextEntry(FormTag.FORM_FULL_ID_KEY);
    this.formViewModel = (FormWidget.ViewModel) requireContextEntry(FormTag.FORM_VIEW_MODEL_KEY);
    FormWidget form = (FormWidget) requireContextEntry(FormTag.FORM_KEY);

    // In case the tag is in formElement tag

    this.derivedId = this.id;

    if (this.derivedId == null && getContextEntry(FormElementTag.ID_KEY) != null) {
      this.derivedId = (String) getContextEntry(FormElementTag.ID_KEY);
    }

    if (this.derivedId == null) {
      throw new MissingFormElementIdAraneaJspException(this);
    }

    FormElement fe = (FormElement) JspWidgetUtil.traverseToSubWidget(form, this.derivedId);
    fe.rendered();

    this.formElementViewModel = (FormElement.ViewModel) fe._getViewable().getViewModel();

    // Get control
    this.controlViewModel = this.formElementViewModel.getControl();
    this.localizedLabel = JspUtil.getResourceString(this.pageContext, this.formElementViewModel.getLabel());

    // We shall use the "accesskey" HTML attribute for this form element only if the attribute "accessKey"
    // was explicitly set (otherwise in most common cases the label tag sets up the access key)
    if (this.accessKeyId != null) {
      this.accessKey = JspUtil.getResourceStringOrNull(this.pageContext, this.accessKeyId);
    }

    if (this.accessKey != null && this.accessKey.length() != 1) {
      this.accessKey = null;
    }

    if (this.hasElementContextSpan) {
      writeFormElementContextOpen(out, this.formFullId, this.derivedId, true, this.pageContext);
    }

    this.updateRegionNames = JspUpdateRegionUtil.getUpdateRegionNames(this.pageContext, this.updateRegions, this.globalUpdateRegions);

    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, this.getFullFieldId());

    this.backgroundValidation = fe.isBackgroundValidation();

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    if (this.hasElementContextSpan) {
      writeFormElementContextClose(out);
      writeFormElementValidityMarkers(out, this.formElementViewModel.isValid(), FORMELEMENT_SPAN_PREFIX
          + this.formFullId + "." + derivedId);
      writeFormElementValidationErrorMessages(out);
    }
    return super.doEndTag(out);
  }

  /**
   * @since 1.1
   */
  protected void writeFormElementValidationErrorMessages(Writer out) throws JspException, AraneaJspException,
      IOException {
    if (!this.formElementViewModel.isValid()) {
      FormWidget form = (FormWidget) requireContextEntry(FormTag.FORM_KEY);
      String errors = this.formElementViewModel.getFormElementValidationErrorRenderer().getClientRenderText(
          (FormElement) JspWidgetUtil.traverseToSubWidget(form, this.derivedId));
      out.write(errors);
    }
  }

  @Override
  public void doFinally() {
    super.doFinally();
    this.formViewModel = null;
    this.formElementViewModel = null;
    this.controlViewModel = null;
    this.backgroundValidation = false;
  }

  /**
   * Adds CSS class if this element has background validation enabled. Note that when application wide validation
   * settings are the same as for that all form/elements, no class will be added.
   * 
   * @since 1.1
   */
  @Override
  protected String getStyleClass() {
    StringBuffer cssClass = new StringBuffer(StringUtils.defaultIfEmpty(super.getStyleClass(), ""));
    boolean globalBgValidation = ConfigurationUtil.isBackgroundFormValidationEnabled(getEnvironment());
    if (this.backgroundValidation != globalBgValidation) {
      cssClass.append(' ').append(AraneaAttributes.BACKGROUND_VALIDATION_CLASS);
    }
    return cssClass.toString();
  }

  protected void writeEventAttributes(Writer out, String jsEvent, String araneaEvent, String condition) throws Exception {
    if (this.events) {
      this.writeSubmitScriptForUiEvent(out, jsEvent, this.derivedId, araneaEvent, condition, this.updateRegionNames);
    }

  }
  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Element id, can also be inherited."
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether the element will send the events that are registered by server-side (by default 'true')."
   */
  public void setEvents(String events) throws JspException {
    this.events = evaluateNotNull("events", events, Boolean.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether the form will be validated on the client-side when the element generates an event (by default: false)."
   */
  public void setValidateOnEvent(String validateOnEvent) throws JspException {
    this.validateOnEvent = evaluateNotNull("validateOnEvent", validateOnEvent, Boolean.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "HTML tabindex for the element."
   */
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = evaluateNotNull("tabindex", tabindex, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setUpdateRegions(String updateRegions) {
    this.updateRegions = evaluate("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated globally. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) {
    this.globalUpdateRegions = evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }

  /**
   * Id of the resource specifying the accesskey for this element. Support for accesskeys is specific for each form
   * element and is to be implemented in each form element separately.
   * 
   * Usually you won't need to specify the access key for the element at all, but rather for the element's label. It's
   * OK to specify null, empty string, or nothing for this property.
   */
  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = evaluate("accessKeyId", accessKeyId, String.class);
  }

  /**
   * Computes field name.
   */
  protected String getFullFieldId() {
    return this.formFullId + "." + this.derivedId;
  }

  /**
   * Asserts that associated control is of given type. If the condition does not hold, throws exception.
   */
  protected void assertControlType(String type) throws JspException {
    if (!this.controlViewModel.getControlType().equals(type)) {
      throw new AraneaJspException("Control of type(s) '" + type + "' expected in form element '" + this.derivedId
          + "' instead of '" + this.controlViewModel.getControlType() + "'");
    }
  }

  public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId,
      PageContext pageContext) throws Exception {
    writeFormElementContextOpen(out, fullFormId, elementId, true, pageContext);
  }

  public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId, boolean isPresent,
      PageContext pageContext) throws Exception {
    writeFormElementContextOpen(out, fullFormId, elementId, isPresent, pageContext, FORMELEMENT_SPAN_PREFIX);
  }

  /**
   * Write a span with random ID around the element, and register this SPAN with javascript (done by external behavior
   * scripts, SPAN functions as keyboard handler). Default implementation does not use any parameters except
   * <code>Writer</code> and <code>PageContext</code>.
   * 
   * @param out The writer to write to.
   * @param fullFormId The full form ID.
   * @param elementId The current element ID.
   * @param isPresent 
   * @param pageContext The JSP page context.
   * @param idPrefix The prefix for the ID.
   * @throws Exception
   */
  public static void writeFormElementContextOpen(Writer out, String fullFormId, String elementId, boolean isPresent,
      PageContext pageContext, String idPrefix) throws Exception {
    // Enclose the element in a <span id=someuniqueid>
    String spanId = idPrefix + fullFormId + "." + elementId;

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "id", spanId);

    // We'll also use the span around a form element for tracking keyboard events.
    // that is, the span will call our handler on a keypress.
    // Another, better way would be to set up some global keypress handler, but unfortunately
    // that way it is too difficult to determine exactly which element was the target for the event.

    // All events are sent to a handler called "uiHandleKeypress(event, formElementId)"
    // We use the "keydown" event, not keypress, because this allows to catch F2 in IE.
    // Actual onkeydown event is attached to span with behavioural javascript --
    // that also takes care of adding hidden element into DOM that indicates this
    // form element is present in request.
    JspUtil.writeCloseStartTag(out);
  }

  /**
   * Closes the SPAN opened by writeFormElementContextOpen
   * 
   * @param out The writer where to write.
   * @throws IOException
   */
  public static void writeFormElementContextClose(Writer out) throws IOException {
    JspUtil.writeEndTag_SS(out, "span");
  }

  /** @since 1.1 */
  public static void writeFormElementValidityMarkers(Writer out, boolean valid, String spanId) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag(out);

    out.write("Aranea.UI.markFEContentStatus(" + valid + ", $('" + spanId + "'));");

    JspUtil.writeEndTag_SS(out, "script");
  }

  /**
   * Determines whether a "context span" will surround the contents of this tag. You should only be interested in this
   * property if you are making a subclass of this tag, and the tag might be used as a container for other form element
   * tags.
   * 
   * Normally, each "form-element" tag surrounds its contents with a HTML &lt;span&gt; tag, denoting the span of this
   * element (this is later used to highlight element in validation or to capture keyboard events). These spans should
   * not nest as this will produce problems capturing keyboard events. (the same keypress will be captured twice: once
   * by the child span, and once by the parent).
   * 
   * Therefore, if this "form-element-tag" might contain child "form-elements" (like a CheckboxMultiselect for example),
   * it should not have its own "context span", only its children should. (or maybe vice-versa). Anyway, setting this
   * value to false will disable the context span. Default is true.
   * 
   * @param hasElementContextSpan
   */
  protected void setHasElementContextSpan(boolean hasElementContextSpan) {
    this.hasElementContextSpan = hasElementContextSpan;
  }

  /**
   * @see #setHasElementContextSpan
   */
  protected boolean getHasElementContextSpan() {
    return this.hasElementContextSpan;
  }

  /**
   * Writes event custom attributes and submit script for <i>attributeName</i>.
   */
  protected void writeSubmitScriptForUiEvent(Writer out, String attributeName, String id, String eventId,
      String precondition, List<String> updateRegions) throws IOException {
    UiUpdateEvent event = new UiUpdateEvent(eventId, this.formFullId + "." + id, null, updateRegions);
    event.setEventPrecondition(precondition);
    JspUtil.writeEventAttributes(out, event);
    JspWidgetCallUtil.writeSubmitScriptForEvent(out, attributeName);
  }

  protected void writeSubmitScriptForUiEvent(Writer out, String attributeName) throws IOException {
    JspUtil.writeOpenAttribute(out, attributeName);
    JspWidgetCallUtil.writeSubmitScriptForEvent(out, attributeName);
    JspUtil.writeCloseAttribute(out);
  }

  protected String evaluateDisabledRenderMode(String renderMode) throws JspException {
    String resultRenderMode = evaluateNotNull("disabledRenderMode", renderMode, String.class);

    if (!resultRenderMode.equals(RENDER_DISABLED_DISABLED) && !resultRenderMode.equals(RENDER_DISABLED_READONLY)) {
      throw new JspException("Valid options for the disabledRenderMode attribute are '" + RENDER_DISABLED_DISABLED
          + "' and '" + RENDER_DISABLED_READONLY + "'. The value '" + resultRenderMode + "' is not valid.");
    }

    return resultRenderMode;
  }

}
