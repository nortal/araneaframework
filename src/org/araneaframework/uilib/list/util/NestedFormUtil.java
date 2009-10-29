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

package org.araneaframework.uilib.list.util;

import org.araneaframework.backend.util.BeanUtil;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.util.NameUtil;

/**
 * {@link FormWidget} and {@link BeanFormWidget} <code>addElement</code> methods that take full element Id (separated by
 * dots) as an argument.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class NestedFormUtil {

  // FormWidget

  /**
   * Adds a contained element.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param element The element that must be added.
   */
  public static void addElement(FormWidget form, String fullId, final GenericFormElement element) {
    addElement(form, fullId, new FormElementAdder<Object, Object>() {

      public FormElement<Object, Object> addFormElement(FormWidget form, String id) {
        form.addElement(id, element);
        return null;
      }
    });
  }

  /**
   * This method adds a {@link FormElement}.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @return the FormElement that was just added.
   */
  public static <C, D> FormElement<C, D> addElement(FormWidget form, String fullId, final String labelId,
      final Control<C> control, final Data<D> data, final boolean mandatory) throws Exception {
    return addElement(form, fullId, new FormElementAdder<C, D>() {

      public FormElement<C, D> addFormElement(FormWidget form, String id) {
        return form.addElement(id, labelId, control, data, mandatory);
      }
    });
  }

  /**
   * This method adds a {@link FormElement}.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param initialValue initial value.
   * @param mandatory whether the element must be present in request.
   * @return The FormElement that was just added.
   */
  public static <C, D> FormElement<C, D> addElement(FormWidget form, String fullId, final String labelId,
      final Control<C> control, final Data<D> data, final D initialValue, final boolean mandatory) throws Exception {
    return addElement(form, fullId, new FormElementAdder<C, D>() {

      public FormElement<C, D> addFormElement(FormWidget form, String id) {
        return form.addElement(id, labelId, control, data, initialValue, mandatory);
      }
    });
  }

  // BeanFormWidget
  /**
   * Adds a contained element.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param The element that must be added.
   */
  @SuppressWarnings("unchecked")
  public static void addElement(BeanFormWidget form, String fullId, final GenericFormElement element) {
    addBeanElement(form, fullId, new BeanFormElementAdder() {

      public FormElement addFormElement(BeanFormWidget form, String id) {
        form.addElement(id, element);
        return null;
      }
    });
  }

  /**
   * This method adds a {@link FormElement}.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param mandatory whether the element must be present in request.
   * @return The FormElement that was just added.
   */
  @SuppressWarnings("unchecked")
  public static FormElement addElement(BeanFormWidget form, String fullId, final String labelId,
      final Control control, final Data data, final boolean mandatory) throws Exception {
    return addBeanElement(form, fullId, new BeanFormElementAdder() {

      public FormElement addFormElement(BeanFormWidget form, String id) {
        return form.addElement(id, labelId, control, data, mandatory);
      }
    });
  }

  /**
   * This method adds a {@link FormElement}.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param data the type of data.
   * @param initialValue initial value.
   * @param mandatory whether the element must be present in request.
   * @return The FormElement that was just added.
   */
  @SuppressWarnings("unchecked")
  public static FormElement addElement(BeanFormWidget form, String fullId, final String labelId,
      final Control control, final Data data, final Object initialValue, final boolean mandatory) throws Exception {
    return addBeanElement(form, fullId, new BeanFormElementAdder() {

      public FormElement addFormElement(BeanFormWidget form, String id) {
        return form.addElement(id, labelId, control, data, initialValue, mandatory);
      }
    });
  }

  /**
   * This method adds a {@link FormElement}.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param mandatory whether the element must be present in request.
   * @return The FormElement that was just added.
   */
  @SuppressWarnings("unchecked")
  public static FormElement addBeanElement(BeanFormWidget form, String fullId, final String labelId,
      final Control control, final boolean mandatory) throws Exception {
    return addBeanElement(form, fullId, new BeanFormElementAdder() {

      public FormElement addFormElement(BeanFormWidget form, String id) {
        return form.addBeanElement(id, labelId, control, mandatory);
      }
    });
  }

  /**
   * This method adds a {@link FormElement}.
   * 
   * @param form The form widget where the element will be added.
   * @param fullId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param labelId id of the localized label.
   * @param control the type of control data.
   * @param initialValue initial value.
   * @param mandatory whether the element must be present in request.
   * @return The FormElement that was just added.
   */
  @SuppressWarnings("unchecked")
  public static FormElement addBeanElement(BeanFormWidget form, String fullId, final String labelId,
      final Control control, final Object initialValue, final boolean mandatory) throws Exception {
    return addBeanElement(form, fullId, new BeanFormElementAdder() {

      public FormElement addFormElement(BeanFormWidget form, String id) {
        return form.addBeanElement(id, labelId, control, initialValue, mandatory);
      }
    });
  }

  /**
   * FormWidget element adder.
   * 
   * @author Rein Raudjärv (rein@araneaframework.org)
   */
  private static interface FormElementAdder<C, D> extends Serializable {

    FormElement<C, D> addFormElement(FormWidget form, String id);
  }

  /**
   * BeanFormWidget element adder.
   * 
   * @author Rein Raudjärv (rein@araneaframework.org)
   */
  private static interface BeanFormElementAdder extends Serializable {

    @SuppressWarnings("unchecked")
    FormElement addFormElement(BeanFormWidget form, String id);
  }

  /**
   * This method adds a {@link FormElement} to {@link FormWidget}.
   * 
   * @param form The form widget where the element will be added.
   * @param elementId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param adder The element adder that will actually add the element.
   * @return The element returned from the adder.
   */
  private static <C, D> FormElement<C, D> addElement(FormWidget form, String elementId, FormElementAdder<C, D> adder) {
    if (StringUtils.contains(elementId, BeanUtil.NESTED_DELIM)) {
      String fullSubFormsId = StringUtils.substringBeforeLast(elementId, BeanUtil.NESTED_DELIM);
      elementId = StringUtils.substringAfterLast(elementId, BeanUtil.NESTED_DELIM);
      form = form.addSubForm(fullSubFormsId);
    }
    return adder.addFormElement(form, elementId);
  }

  /**
   * This method adds a {@link FormElement} to {@link BeanFormWidget}.
   * 
   * @param form The form widget where the element will be added.
   * @param elementId The full element id (a path that may be separated by dots to indicate sub-forms).
   * @param adder The element adder that will actually add the element.
   * @return The element returned from the adder.
   */
  @SuppressWarnings("unchecked")
  private static FormElement addBeanElement(BeanFormWidget form, String elementId, BeanFormElementAdder adder) {
    if (StringUtils.contains(elementId, BeanUtil.NESTED_DELIM)) {
      String fullSubFormsId = StringUtils.substringBeforeLast(elementId, BeanUtil.NESTED_DELIM);
      elementId = StringUtils.substringAfterLast(elementId, BeanUtil.NESTED_DELIM);
      form = form.addBeanSubForm(fullSubFormsId);
    }
    return adder.addFormElement(form, elementId);
  }

  /**
   * Returns the deepest sub-form on given path of given form.
   * 
   * @return deepest sub-form on given path of given form
   * @since 1.0.9
   */
  public static FormWidget getDeepestForm(String path, FormWidget form) {
    if (StringUtils.contains(path, BeanUtil.NESTED_DELIM)) {
      form = form.getSubFormByFullName(NameUtil.getLongestPrefix(path));
    }
    return form;
  }
}
