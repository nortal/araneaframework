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
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.WindowScrollPositionContext;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardWindowScrollPositionFilterWidget extends BaseFilterWidget implements WindowScrollPositionContext {

  private static final Log LOG = LogFactory.getLog(StandardWindowScrollPositionFilterWidget.class);

  protected static final String DEFAULT_COORDINATE = "0";

  protected LinkedList<String[]> savedCoordinates = new LinkedList<String[]>();

  private String windowScrollX;

  private String windowScrollY;

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
      updateScroll(coords[0], coords[1]);

      if (LOG.isDebugEnabled()) {
        LOG.debug("Popped to window coordinates [" + this.windowScrollX + "," + this.windowScrollY + "].");
      }
    }
  }

  public void push() {
    this.savedCoordinates.addFirst(new String[] { this.windowScrollX, this.windowScrollY });

    if (LOG.isDebugEnabled()) {
      LOG.debug("Pushed window coordinates [" + this.windowScrollX + "," + this.windowScrollY + "].");
    }

    resetCurrent();
  }

  public void resetCurrent() {
    updateScroll(null, null);
  }

  public String getX() {
    return this.windowScrollX;
  }

  public String getY() {
    return this.windowScrollY;
  }

  public void scrollTo(String x, String y) {
    updateScroll(x, y);

    if (LOG.isDebugEnabled()) {
      LOG.debug("The window will be scrolled to [" + this.windowScrollX + "," + this.windowScrollY + "].");
    }
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), WindowScrollPositionContext.class, this);
  }

  @Override
  protected void update(InputData input) throws Exception {
    updateScroll(input.getGlobalData().get(WINDOW_SCROLL_X_KEY), input.getGlobalData().get(WINDOW_SCROLL_Y_KEY));

    if (LOG.isDebugEnabled()) {
      LOG.debug("Window scoll coordinates from request: [" + this.windowScrollX + "," + this.windowScrollY + "].");
    }

    super.update(input);
  }


  /**
   * Resets window scroll coordinates when any message is present so that the user would see the message immediately.
   * 
   * @since 2.0
   */
  @Override
  protected void render(OutputData output) throws Exception {
    if (!getEnvironment().requireEntry(MessageContext.class).getMessages().isEmpty()) {
      resetCurrent();
      LOG.debug("Resetted window scroll coordinates due to presence of messages in MessageContext.");
    }
    super.render(output);
  }

  /**
   * Method used internally to update scroll position. The class-level variables {@link #windowScrollX} and
   * {@link #windowScrollY} should be updated only through this method so that the changes would also reach the form.
   * 
   * @param x A string value of horizontal scroll position, or <code>null</code> for no specific position.
   * @param y A string value of vertical scroll position, or <code>null</code> for no specific position.
   * @since 2.0
   */
  protected void updateScroll(String x, String y) {
    this.windowScrollX = defaultValue(x);
    this.windowScrollY = defaultValue(y);

    SystemFormContext formCtx = EnvironmentUtil.getSystemFormContext(getEnvironment());
    formCtx.addField(WINDOW_SCROLL_X_KEY, this.windowScrollX);
    formCtx.addField(WINDOW_SCROLL_Y_KEY, this.windowScrollY);
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
