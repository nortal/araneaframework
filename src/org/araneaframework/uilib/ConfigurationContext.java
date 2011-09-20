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

package org.araneaframework.uilib;

import java.io.Serializable;

/**
 * Configuration context for UiLib widgets. Constants defined here are the keys under which some existing widgets search
 * their configuration.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface ConfigurationContext extends Serializable {

  /**
   * <code>String</code> containing the patterns for <code>Date</code> date validation.
   */
  String CUSTOM_DATE_FORMAT = "uilib.widgets.forms.controls.CustomDateFormat";

  /**
   * <code>String</code> containing the patterns for <code>Date</code> time validation.
   */
  String CUSTOM_TIME_FORMAT = "uilib.widgets.forms.controls.CustomTimeFormat";

  /**
   * <code>String</code> containing the format for <code>Date</code> default date output.
   */
  String DEFAULT_DATE_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputDateFormat";

  /**
   * <code>String</code> containing the format for <code>Date</code> default time output.
   */
  String DEFAULT_TIME_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputTimeFormat";

  /**
   * The full class name of the implementation of {@link org.araneaframework.uilib.form.converter.ConverterProvider}
   * interface that will override the default.
   */
  String CUSTOM_CONVERTER_PROVIDER = "uilib.widgets.forms.converters.CustomConverterProvider";

  /**
   * <code>Long</code> that controls the default size of the list (e.g. how many rows are shown on one page).
   */
  String DEFAULT_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.DefaultListItemsOnPage";

  /**
   * <code>Long</code> that controls the full size of the list (e.g. how many rows maximum may be shown at once).
   */
  String FULL_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.FullListItemsOnPage";

  /**
   * <code>Long</code> that controls the default of how many list pages combine into one block for quick navigation.
   */
  String DEFAULT_LIST_PAGES_ON_BLOCK = "uilib.widgets.lists.DefaultListPagesOnBlock";

  /**
   * <code>Boolean</code> that controls whether to preserve the list starting row when switching to showing full list
   * and back.
   */
  String LIST_PRESERVE_STARTING_ROW = "uilib.widgets.lists.DefaultPreserveStartingRow";

  /**
   * <code>FilterFormBuilderVisitor.Configurator</code> that configures the built filter form elements.
   */
  String LIST_FILTER_CONFIGURATOR = "uilib.widgets.lists.ListFilterConfigurator";

  /**
   * A <tt>AutoCompleteTextControl.ResponseBuilder</tt> that configures how <tt>AutoCompleteTextControl</tt> sends back
   * the suggested completions.
   * 
   * @see org.araneaframework.uilib.form.control.AutoCompleteTextControl.ResponseBuilder
   */
  String AUTO_COMPLETE_RESPONSE_BUILDER = "uilib.widgets.AutoCompleteTextControl.DefaultResponseBuilder";

  /**
   * <code>LikeConfiguration</code> that configures Like filter in lists.
   */
  String LIKE_CONFIGURATION = "uilib.widgets.lists.LikeConfiguration";

  /**
   * This Boolean property should be set to <code>true</code> if application wants all forms to be validated on-the-fly.
   * Validation is done by invoking server-side <tt>ActionListener</tt>s that perform the validation. When this is set
   * to <code>false</code>, programmer can manually enable action validation for those <tt>FormWidget</tt> or
   * <tt>FormElement</tt> which should be validated on-the-fly. When <tt>ConfigurationContext</tt> does not include
   * entry corresponding to this property, it defaults to <code>false</code>.
   * 
   * @see org.araneaframework.uilib.form.FormElement#setBackgroundValidation(boolean)
   * @since 1.1
   */
  String BACKGROUND_FORM_VALIDATION = "uilib.widgets.forms.seamless.validation";

  /**
   * This property should be set to the class which stores the errors produced by failed <tt>FormElement</tt>
   * validations. When this property is not set, <tt>StandardFormElementValidationErrorRenderer</tt> is used.
   * 
   * @see org.araneaframework.uilib.form.FormElement
   * @see org.araneaframework.uilib.form.StandardFormElementValidationErrorRenderer
   */
  String FORMELEMENT_ERROR_RENDERER = "uilib.widgets.forms.formelement.error.renderer";

  /**
   * This property is of type <code>java.lang.Boolean</code> and specifies whether data of controls, that have fixed
   * values (i.e. <code>SelectControl</code>, <code>MultiSelectControl</code>), should be localized.
   * <p>
   * By default, it is not enabled (this may change in near future).
   * <p>
   * You can override this setting when you use a tag to render the control.
   * 
   * @since 1.2
   */
  String LOCALIZE_FIXED_CONTROL_DATA = "uilib.widgets.forms.control.data.localize";

  /**
   * This property should be set as a class implementing <tt>ExpressionEvaluationManager</tt> to specify a custom EL
   * evaluation manager. If none is provided, the <tt>DefaultExpressionEvaluationManager</tt> will be used, or
   * <tt>SpringExpressionEvaluationManager</tt> in a Spring framework application.
   * 
   * @since 1.0.1
   * @see org.araneaframework.jsp.tag.support.ExpressionEvaluationManager
   * @see org.araneaframework.jsp.tag.support.DefaultExpressionEvaluationManager
   * @see org.araneaframework.integration.spring.SpringExpressionEvaluationManager
   */
  String TAGS_EL_MANAGER = "uilib.tags.el.manager";

  /**
   * This property is mostly used internally to store the full names to action listeners that must be handled
   * asynchronously.
   * 
   * @since 2.0
   */
  String ASYNC_ACTION_LISTENERS = "uilib.widget.async.actions";

  /**
   * Returns a configuration entry with given name.
   * 
   * @param entryName The name, the entry will be identified with.
   * @return The value to be associated with the entry.
   */
  Object getEntry(String entryName);

  /**
   * Sets an entry in the configuration context. If the entry already exists, it will be overridden.
   * 
   * @param entryName The name, the entry will be identified with.
   * @param value The value to be associated with the entry.
   * @since 2.0
   */
  void setEntry(String entryName, Object value);
}
