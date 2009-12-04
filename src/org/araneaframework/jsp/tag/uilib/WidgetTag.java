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

package org.araneaframework.jsp.tag.uilib;

import java.util.HashMap;

import java.util.Map;

import org.araneaframework.core.StandardEnvironment;

import org.araneaframework.Environment;

import java.io.Writer;
import javax.servlet.jsp.tagext.Tag;

/**
 * Widget base tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "widget"
 *  body-content = "JSP"
 *  description = "UiLib widget tag.<br/>Makes available following page scope variables:<ul><li><i>widget</i> - UiLib widget view model.</li></ul>"
 */
public class WidgetTag extends BaseWidgetTag {

  public static final String WIDGET_ID_KEY = "widgetId";

  public static final String WIDGET_KEY = "widget";

  public static final String WIDGET_ENV = "environment";

  public static final String WIDGET_VIEW_MODEL_KEY = "viewModel";

  public static final String WIDGET_VIEW_DATA_KEY = "viewData";

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Set variables
    addContextEntry(WIDGET_ID_KEY, this.fullId);
    addContextEntry(WIDGET_KEY, this.widget);
    addContextEntry(WIDGET_ENV, getSimplifiedWidgetEnvironment());
    addContextEntry(WIDGET_VIEW_DATA_KEY, this.viewModel.getData());
    addContextEntry(WIDGET_VIEW_MODEL_KEY, this.viewModel);

    // Continue
    return Tag.EVAL_BODY_INCLUDE;
  }

  @Override
  protected Environment getEnvironment() {
    return new StandardEnvironment(super.getEnvironment(), getWidgetEnvironmentAsMap());
  }

  private Map<Class<?>, Object> getWidgetEnvironmentAsMap() {
    Map<Class<?>, Object> results = new HashMap<Class<?>, Object>();
    if (this.widget != null && this.widget.getChildEnvironment() instanceof StandardEnvironment) {
      StandardEnvironment stdEnv = (StandardEnvironment) this.widget.getChildEnvironment();
      results = stdEnv.getEntryMap();
    }
    return results;
  }

  /**
   * It is easier to access the environment in JSP EL when it is provided as map, and the key names are in string.
   * @return A map of widget's environment entries where key names are the simple class names.
   */
  private Map<String, Object> getSimplifiedWidgetEnvironment() {
    Map<String, Object> results = new HashMap<String, Object>();
    if (this.widget.getChildEnvironment() instanceof StandardEnvironment) {
      StandardEnvironment stdEnv = (StandardEnvironment) this.widget.getChildEnvironment();
      for (Map.Entry<Class<?>, Object> entry : stdEnv.getEntryMap().entrySet()) {
        results.put(entry.getKey().getSimpleName(), entry.getValue());
      }
    }
    return results;
  }
}
