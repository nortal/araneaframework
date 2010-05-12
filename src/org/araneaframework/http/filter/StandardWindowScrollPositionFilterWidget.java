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
import org.araneaframework.InputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.WindowScrollPositionContext;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardWindowScrollPositionFilterWidget extends BaseFilterWidget implements WindowScrollPositionContext {

  private static final Log LOG = LogFactory.getLog(StandardWindowScrollPositionFilterWidget.class);

  protected static final String DEFAULT_COORDINATE = "0";

  protected LinkedList<String[]> savedCoordinates = new LinkedList<String[]>();

  protected String windowScrollX;

  protected String windowScrollY;

  /* ************************************************************************
   * WindowScrollPositionContext interface methods.
   * **********************************************************************
   */

  public void reset() {
    resetCurrent();
    this.savedCoordinates.clear();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Reseted all stored window coordinates.");
    }
  }

  public void pop() {
    if (!this.savedCoordinates.isEmpty()) {
      String[] coords = this.savedCoordinates.removeFirst();
      this.windowScrollX = coords[0];
      this.windowScrollY = coords[1];

      if (LOG.isDebugEnabled()) {
        LOG.debug("Popped to window coordinates " + this.windowScrollX + " " + this.windowScrollY);
      }
    }
  }

  public void push() {
    this.savedCoordinates.addFirst(new String[] { this.windowScrollX, this.windowScrollY });

    if (LOG.isDebugEnabled()) {
      LOG.debug("Pushed window coordinates " + this.windowScrollX + " " + this.windowScrollY);
    }

    resetCurrent();
  }

  public void resetCurrent() {
    this.windowScrollX = this.windowScrollY = defaultValue(null);
  }

  public String getX() {
    return this.windowScrollX;
  }

  public String getY() {
    return this.windowScrollY;
  }

  public void scrollTo(String x, String y) {
    this.windowScrollX = defaultValue(x);
    this.windowScrollY = defaultValue(y);

    if (LOG.isDebugEnabled()) {
      LOG.debug("The window will be scrolled to " + x + " " + y);
    }
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), WindowScrollPositionContext.class, this);
  }

  @Override
  protected void update(InputData input) throws Exception {
    this.windowScrollX = defaultValue(input.getGlobalData().get(WINDOW_SCROLL_X_KEY));
    this.windowScrollY = defaultValue(input.getGlobalData().get(WINDOW_SCROLL_Y_KEY));

    if (LOG.isDebugEnabled()) {
      LOG.debug("Window scoll coordinates from request:  " + this.windowScrollX + " " + this.windowScrollY);
    }

    super.update(input);
  }

  /**
   * Resolves the default value for empty coordinate value.
   * 
   * @param value The value to check
   * @return The value or its replacement.
   * @since 2.0
   */
  protected static String defaultValue(String value) {
    return StringUtils.defaultString(value, DEFAULT_COORDINATE);
  }
}
