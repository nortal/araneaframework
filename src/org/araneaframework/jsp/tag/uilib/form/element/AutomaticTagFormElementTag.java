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

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.Writer;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.commons.beanutils.PropertyUtils;
import org.araneaframework.core.util.ClassLoaderUtil;
import org.araneaframework.http.JspContext;
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.support.FormElementViewSelector;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.uilib.form.FormElementTag;
import org.araneaframework.jsp.tag.uilib.form.FormElementTagInterface;
import org.araneaframework.jsp.tag.uilib.form.FormTag;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Automatic form element tag. Chooses the tag to draw the control based on the information supplied in the component.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "automaticFormElement"
 *  body-content = "JSP"
 *  description = "Automatic form element which dynamically draws correct form element tag."
 */
public class AutomaticTagFormElementTag extends BaseTag {

  protected String derivedId;

  private String id;

  protected String events;

  protected String validateOnEvent;

  protected String tabindex;

  protected String styleClass;

  protected String updateRegions;

  protected String globalUpdateRegions;

  protected FormWidget.ViewModel formViewModel;

  protected FormElement<?, ?>.ViewModel formElementViewModel;

  protected Control.ViewModel controlViewModel;

  protected FormElementTagInterface controlTag;

  @Override
  @SuppressWarnings("unchecked")
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

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

    this.formElementViewModel = (FormElement.ViewModel) JspWidgetUtil.traverseToSubWidget(form, this.derivedId)
        ._getViewable().getViewModel();

    // Get control
    this.controlViewModel = this.formElementViewModel.getControl();

    FormElementViewSelector viewSelector = (FormElementViewSelector) this.formElementViewModel.getProperties().get(
        FormElementViewSelector.FORM_ELEMENT_VIEW_SELECTOR_PROPERTY);

    if (viewSelector == null) {
      throw new JspException("The form element view selector was not passed!.");
    }

    JspContext config = getEnvironment().requireEntry(JspContext.class);
    Map<String, TagInfo> tagMapping = config.getTagMapping(viewSelector.getUri());

    if (tagMapping == null) {
      throw new JspException("The tag mapping was not found!.");
    }

    TagInfo tagInfo = tagMapping.get(viewSelector.getTag());

    if (tagInfo == null) {
      throw new JspException("Unexistant tag was passed to form element view selector!.");
    }

    this.controlTag = (FormElementTagInterface) ClassLoaderUtil.loadClass(tagInfo.getTagClassName()).newInstance();

    registerSubtag(this.controlTag);

    initTagAttributes(this.controlTag, viewSelector.getAttributes());

    this.controlTag.setId(this.derivedId);
    if (this.events != null) {
      this.controlTag.setEvents(this.events);
    }
    if (this.validateOnEvent != null) {
      this.controlTag.setValidateOnEvent(this.validateOnEvent);
    }
    if (this.styleClass != null) {
      this.controlTag.setStyleClass(this.styleClass);
    }
    if (this.tabindex != null) {
      this.controlTag.setTabindex(this.tabindex);
    }
    if (this.updateRegions != null) {
      this.controlTag.setUpdateRegions(this.updateRegions);
    }
    if (this.globalUpdateRegions != null) {
      this.controlTag.setGlobalUpdateRegions(this.globalUpdateRegions);
    }

    executeStartSubtag(this.controlTag);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    executeEndSubtag(this.controlTag);
    unregisterSubtag(this.controlTag);
    return super.doEndTag(out);
  }

  protected void initTagAttributes(Object tag, Map<String, Object> attributes) throws Exception {
    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
      PropertyUtils.setProperty(tag, entry.getKey(), entry.getValue());
    }
  }

  // Tag attributes

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Element id, can also be inherited."
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether the element will send the events that are registered by server-side (by default "true")."
   */
  public void setEvents(String events) {
    this.events = events;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether the form will be validated on the client-side when the element generates an event (by default: false)."
   */
  public void setValidateOnEvent(String validateOnEvent) {
    this.validateOnEvent = validateOnEvent;
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Element 'tabindex' attribute."
   */
  public void setTabindex(String tabindex) {
    this.tabindex = tabindex;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "CSS class without prefix of the dynamically selected tag."
   */
  public void setStyleClass(String styleClass) {
    this.styleClass = styleClass;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setUpdateRegions(String updateRegions) {
    this.updateRegions = updateRegions;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated globally. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) {
    this.globalUpdateRegions = globalUpdateRegions;
  }
}
