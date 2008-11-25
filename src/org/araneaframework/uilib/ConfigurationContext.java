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

package org.araneaframework.uilib;

import java.io.Serializable;

/**
 * Configuration context for Uilib widgets. Constants defined here are the keys
 * under which some existing widgets search their configuration.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface ConfigurationContext extends Serializable {

  /**
   * <code>String</code> containing the patterns for <code>Date</code> date
   * validation.
   */
  String CUSTOM_DATE_FORMAT = "uilib.widgets.forms.controls.CustomDateFormat";

  /**
   * <code>String</code> containing the patterns for <code>Date</code> time
   * validation.
   */
  String CUSTOM_TIME_FORMAT = "uilib.widgets.forms.controls.CustomTimeFormat";

  /**
   * <code>String</code> containing the format for <code>Date</code> default
   * date output.
   */
  String DEFAULT_DATE_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputDateFormat";

  /**
   * <code>String</code> containing the format for <code>Date</code> default
   * time output.
   */
  String DEFAULT_TIME_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputTimeFormat";

  /**
   * The full class name of the implementation of
   * {@link org.araneaframework.uilib.form.converter.ConverterProvider}
   * interface that will override the default.
   */
  String CUSTOM_CONVERTER_PROVIDER = "uilib.widgets.forms.converters.CustomConverterProvider";

  /**
   * <code>Long</code> that controls the default size of the list (eg how many
   * rows are shown on one page).
   */
  String DEFAULT_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.DefaultListItemsOnPage";

  /**
   * <code>Long</code> that controls the full size of the list (eg how many
   * rows maximum may be shown at once).
   */
  String FULL_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.FullListItemsOnPage";

  /**
   * <code>Long</code> that controls the default of how many list pages
   * combine into one block for quick navigation.
   */
  String DEFAULT_LIST_PAGES_ON_BLOCK = "uilib.widgets.lists.DefaultListPagesOnBlock";

  /**
   * <code>Boolean</code> that controls whether to preserve the list starting
   * row when switching to showing full list and back.
   */
  String LIST_PRESERVE_STARTING_ROW = "uilib.widgets.lists.DefaultPreserveStartingRow";

  /**
   * <code>FilterFormBuilderVisitor.Configurator</code> that configures the
   * built filter form elements.
   */
  String LIST_FILTER_CONFIGURATOR = "uilib.widgets.lists.ListFilterConfigurator";

  /**
   * {@link org.araneaframework.uilib.form.control.AutoCompleteTextControl.ResponseBuilder}
   * that configures how
   * {@link org.araneaframework.uilib.form.control.AutoCompleteTextControl}
   * sends back the suggested completions.
   */
  String AUTO_COMPLETE_RESPONSE_BUILDER = "uilib.widgets.AutoCompleteTextControl.DefaultResponseBuilder";

  /**
   * <code>LikeConfiguration</code> that configures Like filter in lists.
   */
  String LIKE_CONFIGURATION = "uilib.widgets.lists.LikeConfiguration";

  /**
   * This <code>java.lang.Boolean</code> property should be set to
   * <code>true</code> if application wants all forms to be validated
   * on-the-fly. Validation is done by invoking server-side
   * {@link org.araneaframework.core.ActionListener}s that perform the
   * validation. When this is set to <code>false</code>, programmer can
   * manually enable action validation for those
   * {@link org.araneaframework.uilib.form.FormWidget}/{@link  org.araneaframework.uilib.form.FormElement}
   * which should be validated on-the-fly. When {@link ConfigurationContext}
   * does not include entry corresponding to this property, it defaults to
   * <code>false</code>.
   * 
   * @since 1.1
   */
  String BACKGROUND_FORM_VALIDATION = "uilib.widgets.forms.seamless.validation";

  /**
   * This property should be set to the class which stores the errors produced
   * by failed {@link org.araneaframework.uilib.form.FormElement} validations.
   * When this property is not set,
   * {@link org.araneaframework.uilib.form.StandardFormElementValidationErrorRenderer}
   * is used.
   */
  String FORMELEMENT_ERROR_RENDERER = "uilib.widgets.forms.formelement.error.renderer";

  /**
   * This property is of type <code>java.lang.Boolean</code> and specifies
   * whether data of controls, that have fixed values (i.e.
   * <code>SelectControl</code>, <code>MultiSelectControl</code>), should be
   * localized.
   * <p>
   * By default, it is not enabled (this may change in near future).
   * <p>
   * You can override this setting when you use a tag to render the control.
   * 
   * @since 1.2
   */
  String LOCALIZE_FIXED_CONTROL_DATA = "uilib.widgets.forms.control.data.localize";

  /**
   * Returns a configuration entry with given name.
   */
  public Object getEntry(String entryName);
}
