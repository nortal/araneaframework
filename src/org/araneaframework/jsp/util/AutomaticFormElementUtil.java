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

package org.araneaframework.jsp.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.araneaframework.jsp.support.FormElementViewSelector;
import org.araneaframework.jsp.support.TagAttr;
import org.araneaframework.jsp.support.TagInfo;
import org.araneaframework.jsp.tag.uilib.form.element.AutomaticTagFormElementTag;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Utility class for setting {@link FormElement} properties that are needed for rendering them in Aranea JSP with
 * {@link org.araneaframework.jsp.tag.uilib.form.element.AutomaticTagFormElementTag}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see AutomaticTagFormElementTag
 * @see FormElementViewSelector
 * @see TagInfo
 * @see TagAttr
 */
@SuppressWarnings("unchecked")
public class AutomaticFormElementUtil {

  /**
   * Contains the editable (input) tags
   */
  public static final Map<Class<? extends Control>, String> CONTROLS_TO_EDITABLE_TAGS = new HashMap<Class<? extends Control>, String>();

  /**
   * Contains the display tags
   */
  public static final Map<Class<? extends Control>, String> CONTROLS_TO_DISPLAY_TAGS = new HashMap<Class<? extends Control>, String>();

  static {
    CONTROLS_TO_EDITABLE_TAGS.put(ButtonControl.class, "button");
    CONTROLS_TO_EDITABLE_TAGS.put(CheckboxControl.class, "checkbox");
    CONTROLS_TO_EDITABLE_TAGS.put(FileUploadControl.class, "fileUpload");
    CONTROLS_TO_EDITABLE_TAGS.put(MultiSelectControl.class, "multiSelect");
    CONTROLS_TO_EDITABLE_TAGS.put(SelectControl.class, "select");
    CONTROLS_TO_EDITABLE_TAGS.put(TextareaControl.class, "textarea");
    CONTROLS_TO_EDITABLE_TAGS.put(TextControl.class, "textInput");
    CONTROLS_TO_EDITABLE_TAGS.put(NumberControl.class, "numberInput");
    CONTROLS_TO_EDITABLE_TAGS.put(FloatControl.class, "floatInput");
    CONTROLS_TO_EDITABLE_TAGS.put(DateControl.class, "dateInput");
    CONTROLS_TO_EDITABLE_TAGS.put(DateTimeControl.class, "dateTimeInput");
    CONTROLS_TO_EDITABLE_TAGS.put(TimeControl.class, "timeInput");

    CONTROLS_TO_DISPLAY_TAGS.put(CheckboxControl.class, "checkboxDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(MultiSelectControl.class, "multiSelectDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(SelectControl.class, "selectDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(TextareaControl.class, "textareaDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(TextControl.class, "textInputDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(NumberControl.class, "numberInputDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(FloatControl.class, "floatInputDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(DateControl.class, "dateInputDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(DateTimeControl.class, "dateTimeInputDisplay");
    CONTROLS_TO_DISPLAY_TAGS.put(TimeControl.class, "timeInputDisplay");
  }

  // Cannot instantiate this class.
  private AutomaticFormElementUtil() {}

  /**
   * Assigns a {@link FormElementViewSelector} <code>viewSelector</code> to the specified {@link FormElement}.
   * 
   * @param formElement The form element that will get the view (tag) selector.
   * @param viewSelector The view (tag) selector that will be used on rendering.
   */
  public static void setFormElementViewSelector(FormElement<?, ?> formElement, FormElementViewSelector viewSelector) {
    formElement.setProperty(FormElementViewSelector.FORM_ELEMENT_VIEW_SELECTOR_PROPERTY, viewSelector);
  }

  /**
   * Assigns a {@link FormElementViewSelector} <code>viewSelector</code> to the {@link FormElement} that belongs to
   * <code>form</code> and has an ID <code>formElementId</code>.
   * 
   * @param form The form with the given element.
   * @param formElementId The ID of the form element that will get the view (tag) selector.
   * @param viewSelector The view (tag) selector that will be used on rendering.
   */
  public static void setFormElementViewSelector(FormWidget form, String formElementId,
      FormElementViewSelector viewSelector) {
    setFormElementViewSelector(form.getElementByFullName(formElementId), viewSelector);
  }

  /**
   * Associates {@link FormElement} with a JSP tag that {@link AutomaticTagFormElementTag} will use for actual rendering
   * of the {@link FormElement}.
   * 
   * @param formElement The form element that will get the view (tag) selector.
   * @param tagName name of the tag, without any namespace
   * @param tagAttributes Map &lt;attributeName, attributeValue&gt;
   */
  public static void setFormElementTag(FormElement<?, ?> formElement, String tagName, Map<String, Object> tagAttributes) {
    setFormElementViewSelector(formElement, new FormElementViewSelector(tagName, tagAttributes));
  }

  /**
   * Assigns a tag to the specified element.
   * 
   * @param form parent form or composite element.
   * @param formElementId id of the simple element.
   * @param tagName name of the tag that will be used to render the element.
   * @param tagAttributes tag custom attributes.
   */
  public static void setFormElementTag(FormWidget form, String formElementId, String tagName,
      Map<String, Object> tagAttributes) {
    setFormElementViewSelector(form, formElementId, new FormElementViewSelector(tagName, tagAttributes));
  }

  /**
   * Assigns an attributeless tag to the specified {@link FormElement}.
   * 
   * @param formElement id of the simple element.
   * @param tagName name of the tag that will be used to render the element, without any namespace.
   */
  public static void setFormElementTag(FormElement<?, ?> formElement, String tagName) {
    setFormElementTag(formElement, tagName, Collections.EMPTY_MAP);
  }

  /**
   * Assigns an attributeless tag to the specified to the {@link FormElement} that belongs to <code>form</code> and has
   * id <code>formElementId</code>.
   * 
   * @param form parent form or composite element.
   * @param formElementId id of the simple element.
   * @param tagName name of the tag that will be used to render the element, without any namespace.
   */
  public static void setFormElementTag(FormWidget form, String formElementId, String tagName) {
    setFormElementTag(form.getElementByFullName(formElementId), tagName);
  }

  /**
   * Assigns specified tag and attributes to the {@link FormElement}.
   * 
   * @param formElement The form element that will start to use the automatic tag.
   * @param tagName name of the tag that will be used to render the element, without any namespace
   * @param attributePairs tag attributes.
   */
  public static void setFormElementTag(FormElement<?, ?> formElement, String tagName, TagAttr... attributePairs) {
    Map<String, Object> attributes = new HashMap<String, Object>(attributePairs.length);
    for (TagAttr attr : attributePairs) {
      attributes.put(attr.getName(), attr.getValue());
    }
    setFormElementTag(formElement, tagName, attributes);
  }

  /**
   * Assigns tag with specified attributes to the specified to the {@link FormElement} that belongs to <code>form</code>
   * and has id <code>formElementId</code>.
   * 
   * @param form parent {@link FormWidget}
   * @param formElementId id of the form element
   * @param tagName name of the tag that will be used to render the element, without any namespace
   * @param attributePairs tag attributes.
   */
  public static void setFormElementTag(FormWidget form, String formElementId, String tagName, TagAttr[] attributePairs) {
    setFormElementTag(form.getElementByFullName(formElementId), tagName, attributePairs);
  }

  /**
   * Assigns the default editable (aka input) tags to given form element.
   * 
   * @param element The form element that will be editable.
   * @since 1.0.7
   */
  public static void setFormElementDefaultEditableTag(FormElement<?, ?> element) {
    setFormElementTag(element, CONTROLS_TO_EDITABLE_TAGS.get(element.getControl().getClass()));
  }

  /**
   * Assigns the default editable (aka input) tags to given form element.
   * 
   * @param element The form element that will be just viewable.
   * @since 1.0.7
   */
  public static void setFormElementDefaultDisplayTag(FormElement<?, ?> element) {
    setFormElementTag(element, CONTROLS_TO_DISPLAY_TAGS.get(element.getControl().getClass()));
  }

  /**
   * Assigns the default editable (aka input) tags to all of the elements of the form.
   * 
   * @param form parent form or composite element.
   */
  public static void setFormElementDefaultEditableTags(FormWidget form) {
    for (Map.Entry<String, GenericFormElement> entry : form.getElements().entrySet()) {
      GenericFormElement element = entry.getValue();
      if (element instanceof FormWidget) {
        setFormElementDefaultEditableTags((FormWidget) element);
      } else if (element instanceof FormElement) {
        setFormElementDefaultEditableTag((FormElement<?, ?>) element);
      }
    }
  }

  /**
   * Assigns the default display (a.k.a. read-only) tags to all of the elements of the form.
   * 
   * @param form parent form or composite element.
   */
  public static void setFormElementDefaultDisplayTags(FormWidget form) {
    for (Map.Entry<String, GenericFormElement> entry : form.getElements().entrySet()) {
      GenericFormElement element = entry.getValue();

      if (element instanceof FormWidget) {
        setFormElementDefaultDisplayTags((FormWidget) element);
      } else if (element instanceof FormElement) {
        setFormElementDefaultDisplayTag((FormElement) element);
      }
    }
  }

}
