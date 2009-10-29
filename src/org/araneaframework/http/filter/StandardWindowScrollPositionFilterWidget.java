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
    protected LinkedList<String[]> savedCoordinates = new LinkedList<String[]>();
  protected String windowScrollX;
  protected String windowScrollY;
  
  /* ************************************************************************
   * WindowScrollPositionContext interface methods.
   * ********************************************************************** */
  public void reset() {
    resetCurrent();
    savedCoordinates.clear();
    LOG.debug("RESETTED all coords ");
  }
  
  public void pop() {
    if (!savedCoordinates.isEmpty()) { 
      String[] coords = savedCoordinates.removeFirst();
      windowScrollX = coords[0];
      windowScrollY = coords[1];
      
      LOG.debug("popped to coords " + windowScrollX + " " + windowScrollY);
    }
  }

  public void push() {
    savedCoordinates.addFirst(new String[] {windowScrollX, windowScrollY});
    LOG.debug("pushed coords " + windowScrollX + " " + windowScrollY);
    resetCurrent();
  }

  public void resetCurrent() {
    windowScrollX = windowScrollY = null;
  }

  public String getX() {
    return windowScrollX;
  }
  
  public String getY() {
    return windowScrollY;
  }
  
  public void scrollTo(String x, String y) {
    windowScrollX = x;
    windowScrollY = y;
    LOG.debug("scrolled to " + x + " " + y);
  }

  /* *********************************************************************** */
  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), WindowScrollPositionContext.class, this);
  }

  @Override
  protected void update(InputData input) throws Exception {
    windowScrollX = input.getGlobalData().get(WINDOW_SCROLL_X_KEY);
    windowScrollY = input.getGlobalData().get(WINDOW_SCROLL_Y_KEY);
    
    LOG.debug("REQUEST COORDS READ TO BE  to " + windowScrollX + " " + windowScrollY);

    super.update(input);
  }
}
