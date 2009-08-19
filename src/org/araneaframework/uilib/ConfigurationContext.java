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

import org.araneaframework.integration.spring.SpringExpressionEvaluationManager;
import org.araneaframework.jsp.tag.support.DefaultExpressionEvaluationManager;
import org.araneaframework.jsp.tag.support.ExpressionEvaluationManager;
import java.io.Serializable;

/**
 * Configuration context for Uilib widgets. Defined constants
 * are the keys under which some existing widgets search their configuration.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface ConfigurationContext extends Serializable {
  /**
   * <code>String</code> containing the patterns for <code>Date</code> date validation.
   */
  public static final String CUSTOM_DATE_FORMAT = "uilib.widgets.forms.controls.CustomDateFormat";
  
  /**
   * <code>String</code> containing the patterns for <code>Date</code> time validation.
   */
  public static final String CUSTOM_TIME_FORMAT = "uilib.widgets.forms.controls.CustomTimeFormat";  
  
  /**
   * <code>String</code> containing the format for <code>Date</code> default date output.
   */
  public static final String DEFAULT_DATE_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputDateFormat";  
 
  /**
   * <code>String</code> containing the format for <code>Date</code> default time output.
   */
  public static final String DEFAULT_TIME_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputTimeFormat";  
  
  /**
   * The full class name of the implementation of {@link org.araneaframework.uilib.form.converter.ConverterProvider} interface
   * that will override the default.
   */
  public static final String CUSTOM_CONVERTER_PROVIDER = "uilib.widgets.forms.converters.CustomConverterProvider";
  
  /**
   * <code>Long</code> that controls the default size of the list (eg how many rows are shown on one page).
   */
  public static final String DEFAULT_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.DefaultListItemsOnPage";
  
  /**
   * <code>Long</code> that controls the full size of the list (eg how many rows maximum may be shown at once).
   */
  public static final String FULL_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.FullListItemsOnPage";
  
  /**
   * <code>Long</code> that controls the default of how many list pages combine into one block for quick navigation.
   */
  public static final String DEFAULT_LIST_PAGES_ON_BLOCK = "uilib.widgets.lists.DefaultListPagesOnBlock";
  
  /**
   * <code>Boolean</code> that controls whether to preserve the list starting row when switching to showing full list and back.
   */
  public static final String LIST_PRESERVE_STARTING_ROW = "uilib.widgets.lists.DefaultPreserveStartingRow";
  
  /**
   * <code>FilterFormBuilderVisitor.Configurator</code> that configures the built filter form elements.
   */
  public static final String LIST_FILTER_CONFIGURATOR = "uilib.widgets.lists.ListFilterConfigurator";  
  
  /**
   * {@link org.araneaframework.uilib.form.control.AutoCompleteTextControl.ResponseBuilder} that configures how
   * {@link org.araneaframework.uilib.form.control.AutoCompleteTextControl} sends back the suggested completions.
   */
  public static final String AUTO_COMPLETE_RESPONSE_BUILDER = "uilib.widgets.AutoCompleteTextControl.DefaultResponseBuilder";
  
  /**
   * <code>LikeConfiguration</code> that configures Like filter in lists.
   */
  public static final String LIKE_CONFIGURATION = "uilib.widgets.lists.LikeConfiguration";
  
  /**
   * This <code>java.lang.Boolean</code> property should be set to <code>true</code> if application wants all forms to 
   * be validated on-the-fly. Validation is done by invoking server-side {@link org.araneaframework.core.ActionListener}s that perform the 
   * validation. When this is set to <code>false</code>, programmer can manually enable action validation for those
   * {@link org.araneaframework.uilib.form.FormWidget}/{@link  org.araneaframework.uilib.form.FormElement} which should be validated on-the-fly. When {@link ConfigurationContext} does
   * not include entry corresponding to this property, it defaults to <code>false</code>.
   *
   * @since 1.1
   */
  public static final String BACKGROUND_FORM_VALIDATION = "uilib.widgets.forms.seamless.validation";
  
  /**
   * This property should be set to the class which stores the errors produced by failed {@link org.araneaframework.uilib.form.FormElement}
   * validations. When this property is not set, {@link org.araneaframework.uilib.form.StandardFormElementValidationErrorRenderer} is used.
   */
  public static final String FORMELEMENT_ERROR_RENDERER = "uilib.widgets.forms.formelement.error.renderer";

  /**
   * This property should be set as a class implementing
   * {@link ExpressionEvaluationManager} to specify a custom EL evaluation
   * manager. If none is provided, the
   * {@link DefaultExpressionEvaluationManager} will be used, or
   * {@link SpringExpressionEvaluationManager} in a Spring framework
   * application.
   */
  public static final String TAGS_EL_MANAGER = "uilib.tags.el.manager";

  /**
   * Returns a configuration entry with given name.
   */
  public Object getEntry(String entryName);
}
