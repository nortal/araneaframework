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

package org.araneaframework.http;

import java.io.Serializable;

/**
 * Filter that takes care of saving and restoring the browser window scroll position
 * between requests.
 */

public interface WindowScrollPositionContext extends Serializable {
  public static final String SCROLL_HANDLER_KEY="scrollHandler";

  public static final String WINDOW_SCROLL_X_KEY = "windowScrollX";
  public static final String WINDOW_SCROLL_Y_KEY = "windowScrollY";

  /** Resets the scroll coordinates. Typically it should be called when new flow is started. */
  void reset();
  
  /** Returns the currently saved horizontal scroll coordinate. */
  public String getX();
  /** Returns the currently saved vertical scroll coordinate. */
  public String getY();
  
  /** 
   * Sets new horizontal and vertical scroll coordinates. 
   * @param x horizontal scroll coordinate
   * @param y vertical scroll coordinate
   */
  public void scrollTo(String x, String y);
}
