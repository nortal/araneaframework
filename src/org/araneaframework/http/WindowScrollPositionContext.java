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

package org.araneaframework.http;

import java.io.Serializable;

/**
 * Filter that takes care of saving and restoring the browser window scroll position between requests.
 * <p>
 * This handling logic is implemented by, for example,
 * {@link org.araneaframework.framework.container.StandardFlowContainerWidget.StandardTransitionHandler} and
 * {@link org.araneaframework.http.filter.StandardWindowScrollPositionFilterWidget}.
 * <p>
 * To make window scroll positioning work, one must must reference the bean named <code>araneaScrollingFilter</code> in
 * the filter chain of <code>araneaCustomWidgetFilters</code> in <code>aranea-conf.xml</code> like this:
 * 
 * <pre><code>
 * &lt;bean id=&quot;araneaCustomWidgetFilters&quot; singleton=&quot;false&quot;
 *    class=&quot;org.araneaframework.framework.filter.StandardFilterChainWidget&quot;&gt;
 *   &lt;property name=&quot;filterChain&quot;&gt;
 *     &lt;list&gt;
 *       &lt;ref bean=&quot;araneaScrollingFilter&quot;/&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </code></pre>
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface WindowScrollPositionContext extends Serializable {

  /**
   * The request parameter describing the current window scroll horizontal position.
   */
  String WINDOW_SCROLL_X_KEY = "windowScrollX";

  /**
   * The request parameter describing the current window scroll vertical position.
   */
  String WINDOW_SCROLL_Y_KEY = "windowScrollY";

  /**
   * Resets all the remembered scroll coordinates to nothing. ({@link #pop()} will not have any further effect).
   */
  void reset();

  /**
   * Resets currently active scroll coordinates to null (preserves the poppable coordinates).
   * 
   * @since 1.1
   */
  void resetCurrent();

  /**
   * Resets the current scroll coordinates, which can be restored with {@link #pop}.
   * 
   * @since 1.1
   */
  void push();

  /**
   * Restores the previously pushed scroll coordinates.
   * 
   * @since 1.1
   */
  void pop();

  /**
   * Returns the current horizontal scroll coordinate.
   * 
   * @return the current horizontal scroll coordinate.
   */
  String getX();

  /**
   * Returns the current vertical scroll coordinate.
   * 
   * @return the current vertical scroll coordinate.
   */
  String getY();

  /**
   * Sets new horizontal and vertical scroll coordinates.
   * 
   * @param x horizontal scroll coordinate
   * @param y vertical scroll coordinate
   */
  void scrollTo(String x, String y);

}
