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
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
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
   * <code>List&gt;DecimalPattern&lt;</code> containing the patterns for <code>BigDecimal</code> decimal validation.
   */
  public static final String CUSTOM_DECIMAL_FORMAT = "uilib.widgets.forms.controls.CustomDecimalFormat";
  
  /**
   * <code>String</code> containing the format for <code>Date</code> default date output.
   */
  public static final String DEFAULT_DATE_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputDateFormat";  
 
  /**
   * <code>String</code> containing the format for <code>Date</code> default time output.
   */
  public static final String DEFAULT_TIME_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputTimeFormat";  

  /**
   * <code>DecimalPattern</code> containing the pattern for <code>BigDecimal</code> default decimal output.
   */
  public static final String DEFAULT_DECIMAL_OUTPUT_FORMAT = "uilib.widgets.forms.controls.DefaultOutputDecimalFormat";  
  
  /**
   * The full class name of the implementation of {@link org.araneaframework.uilib.form.converter.ConverterProvider} interface
   * that will override the default.
   */
  public static final String CUSTOM_CONVERTER_PROVIDER = "uilib.widgets.forms.converters.CustomConverterProvider";
  
  /**
   * <code>Long</code> that controls the default size of the list (i.e. how many rows are show on one page).
   */
  public static final String DEFAULT_LIST_ITEMS_ON_PAGE = "uilib.widgets.lists.DefaultListItemsOnPage";
  
  /**
   * <code>Long</code> that controls the full size of the list (i.e. how many rows maximum may be shown at once).
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
   * Returns a configuration entry.
   */
  public Object getEntry(String entryName);
}
