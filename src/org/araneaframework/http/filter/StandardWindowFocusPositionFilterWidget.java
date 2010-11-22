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

package org.araneaframework.http.filter;

import java.util.LinkedList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.WindowFocusPositionContext;

/**
 * @author Maksim Boiko <mailto:max@webmedia.ee>
 */
public class StandardWindowFocusPositionFilterWidget extends BaseFilterWidget implements WindowFocusPositionContext {

  private static final Log LOG = LogFactory.getLog(StandardWindowFocusPositionFilterWidget.class);
  public static final String FOCUS_POSITION = "focusPosition";
  public static final String FOCUS_RESET = "FOCUS_RESET";
  
  protected LinkedList<String> savedPositions = new LinkedList<String>();

  protected String focusPosition;

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), WindowFocusPositionContext.class, this);
  }

  @Override
  public String getFocusedElement() {
    return this.focusPosition;
  }

  @Override
  public void setFocusToElement(String widgetId, String formId, String elementId) {
    final StringBuffer newFocusPosition = new StringBuffer(StringUtils.defaultString(widgetId));
    if (StringUtils.isNotBlank(formId)) {
      newFocusPosition.append(".").append(formId);
    }
    if (StringUtils.isNotBlank(elementId)) {
      newFocusPosition.append(".").append(elementId);
    }
    LOG.debug("Setting focus to " + newFocusPosition.toString());
    this.focusPosition = newFocusPosition.toString();
  }

  @Override
  public void setFocusToElement(String fullElementId) {
    LOG.debug("Setting focus to " + fullElementId);
    this.focusPosition = fullElementId;
  }
  
  @Override
  public void resetFocus() {
    this.focusPosition = null;
  }
  
  @Override
  protected void render(OutputData output) throws Exception {
    super.render(output);
    LOG.debug("Nullifying focus.");
    resetFocus();
  }
}
