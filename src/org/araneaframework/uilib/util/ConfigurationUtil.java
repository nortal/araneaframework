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

package org.araneaframework.uilib.util;

import static org.araneaframework.uilib.ConfigurationContext.CUSTOM_DATE_FORMAT;
import static org.araneaframework.uilib.ConfigurationContext.CUSTOM_TIME_FORMAT;
import static org.araneaframework.uilib.ConfigurationContext.DEFAULT_DATE_OUTPUT_FORMAT;
import static org.araneaframework.uilib.ConfigurationContext.DEFAULT_TIME_OUTPUT_FORMAT;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl.ResponseBuilder;

import org.araneaframework.uilib.form.converter.ConverterProvider;

import org.araneaframework.Environment;
import org.araneaframework.jsp.tag.support.DefaultExpressionEvaluationManager;
import org.araneaframework.jsp.tag.support.ExpressionEvaluationManager;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.FormElementValidationErrorRenderer;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public abstract class ConfigurationUtil {

  private ConfigurationUtil() {}

  public static boolean isBackgroundFormValidationEnabled(ConfigurationContext cctx) {
    Boolean b = (Boolean) cctx.getEntry(ConfigurationContext.BACKGROUND_FORM_VALIDATION);
    return b == null ? false : b.booleanValue();
  }

  public static Long getDefaultListItemsOnPage(ConfigurationContext cctx) {
    Long l = (Long) cctx.getEntry(ConfigurationContext.DEFAULT_LIST_ITEMS_ON_PAGE);
    return l;
  }

  public static FormElementValidationErrorRenderer getFormElementValidationErrorRenderer(ConfigurationContext cctx) {
    FormElementValidationErrorRenderer r = (FormElementValidationErrorRenderer) cctx
        .getEntry(ConfigurationContext.FORMELEMENT_ERROR_RENDERER);
    return r;
  }

  public static boolean isLocalizeControlData(Environment env, Boolean overrideValue) {
    if (overrideValue != null) {
      return overrideValue.booleanValue();
    } else {
      ConfigurationContext conf = UilibEnvironmentUtil.getConfiguration(env);
      Boolean setting = (Boolean) conf.getEntry(ConfigurationContext.LOCALIZE_FIXED_CONTROL_DATA);
      return setting == null ? false : setting.booleanValue();
    }
  }

  /**
   * @since 1.1.0.1
   */
  public static ExpressionEvaluationManager getResolver(ConfigurationContext cctx) {
    ExpressionEvaluationManager mgr = (ExpressionEvaluationManager) cctx.getEntry(ConfigurationContext.TAGS_EL_MANAGER);
    return mgr != null ? mgr : new DefaultExpressionEvaluationManager();
  }

  public static ConverterProvider getCustomConverterProvider(ConfigurationContext conf) {
    return (ConverterProvider) conf.getEntry(ConfigurationContext.CUSTOM_CONVERTER_PROVIDER);
  }

  public static ResponseBuilder getResponseBuilder(ConfigurationContext conf) {
    return (ResponseBuilder) conf.getEntry(ConfigurationContext.AUTO_COMPLETE_RESPONSE_BUILDER);
  }


  /**
   * Provides the date format specified in the configuration. If no format is specified in configuration then, for
   * convenience, the provided default format parameter is returned. The parameter <code>input</code> specifies whether
   * the date format is for date input or output.
   * 
   * @param env The <code>Environment</code> to use for searching configuration.
   * @param input Whether the date pattern is for inputting or outputting the value.
   * @param defaultValue The format to return when format is not specified in configuration.
   * @return The found format or the default value.
   * @since 2.0
   */
  public static String getCustomDateFormat(Environment env, boolean input, String defaultValue) {
    String key = input ? CUSTOM_DATE_FORMAT : DEFAULT_DATE_OUTPUT_FORMAT;
    String format = (String) getConfiguration(env).getEntry(key);
    return format == null ? defaultValue : format;
  }

  /**
   * Provides the time format specified in the configuration. If no format is specified in configuration then, for
   * convenience, the provided default format parameter is returned. The parameter <code>input</code> specifies whether
   * the time format is for time input or output.
   * 
   * @param env The <code>Environment</code> to use for searching configuration.
   * @param input Whether the time pattern is for inputting or outputting the value.
   * @param defaultValue The format to return when format is not specified in configuration.
   * @return The found format or the default value.
   * @since 2.0
   */
  public static String getCustomTimeFormat(Environment env, boolean input, String defaultValue) {
    String key = input ? CUSTOM_TIME_FORMAT : DEFAULT_TIME_OUTPUT_FORMAT;
    String format = (String) getConfiguration(env).getEntry(key);
    return format == null ? defaultValue : format;
  }

  private static ConfigurationContext getConfiguration(Environment env) {
    return env.requireEntry(ConfigurationContext.class);
  }
}
