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

package org.araneaframework.jsp;

import org.araneaframework.uilib.ConfigurationContext;

/**
 * Non-standard-HTML attributes added to specific HTML tags to define
 * Aranea event and content model information.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class AraneaAttributes {
  private AraneaAttributes() {}
	
  /** Attribute identifying Aranea system form */
  public static final String SYSTEM_FORM = "arn-systemForm";
  /**
   * Attribute attached to Aranea component markers for identification of component. 
   * @since 1.1
   * */
  public static final String WIDGET_ID = "arn-widgetId";

  /** Event related non-standard HTML tag attributes */
  public interface Event {
     public static final String ID = "arn-evntId";
     public static final String TARGET_WIDGET_ID = "arn-trgtwdgt";
     public static final String PARAM = "arn-evntPar";
     public static final String UPDATE_REGIONS = "arn-updrgns";
     public static final String CONDITION = "arn-evntCond";
  }
  
  /** @since 1.0.11 */
  public interface FilteredInputControl {
    public static final String CHARACTER_FILTER = org.araneaframework.uilib.form.control.inputfilter.InputFilter.CHARACTER_FILTER_ATTRIBUTE;
  }
  
  /**
   * This attribute will be present on the form elements whose which should be validated on-the-fly.
   * (Default is set with {@link ConfigurationContext} entry {@link ConfigurationContext#BACKGROUND_FORM_VALIDATION}.
   * @since 1.1 */
  public static final String BACKGROUND_VALIDATION_ATTRIBUTE = "arn-bgValidate";

  public static final String BACKGROUND_VALIDATION_CLASS = "aranea-bg-validate";

  public static final String NO_BACKGROUND_VALIDATION_CLASS = "aranea-no-bg-validate";
}
