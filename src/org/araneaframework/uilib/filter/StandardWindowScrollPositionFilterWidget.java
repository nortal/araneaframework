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

package org.araneaframework.uilib.filter;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.uilib.core.WindowScrollPositionContext;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardWindowScrollPositionFilterWidget extends BaseFilterWidget implements WindowScrollPositionContext {
  protected String windowScrollX;
  protected String windowScrollY;
  
  /* ************************************************************************
   * WindowScrollPositionContext interface methods.
   * ************************************************************************/
  public void reset() {
    windowScrollX = windowScrollY = null;
  }

  public String getX() {
    return windowScrollX;
  }
  
  public String getY() {
    return windowScrollY;
  }
  
  public void setScrollCoordinates(String x, String y) {
    windowScrollX = x;
    windowScrollY = y;
  }

  /* *********************************************************************** */
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), WindowScrollPositionContext.class, this);
  }

  protected void update(InputData input) throws Exception {
    windowScrollX = (String) input.getGlobalData().get(WINDOW_SCROLL_X_KEY);
    windowScrollY = (String) input.getGlobalData().get(WINDOW_SCROLL_Y_KEY);

    super.update(input);
  }
  
  protected void render(OutputData output) throws Exception {
    output.pushAttribute(SCROLL_HANDLER_KEY, this);
    
    try {
      super.render(output);
    }
    finally {
      output.popAttribute(SCROLL_HANDLER_KEY);
    }
  }
}
