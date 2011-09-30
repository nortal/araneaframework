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

package org.araneaframework.framework.filter;

import java.util.List;
import java.util.ListIterator;
import org.araneaframework.framework.FilterWidget;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Utility widget that creates a chain of given filter widgets. This class is mostly used in configuration when a chain
 * of widgets needs to be specified so that requests/responses would go through them in the specified order. Therefore,
 * this widget takes (upon initialization) the specified list of widgets, takes the last one, makes it the parent of
 * current child widget, and updates the current child widget to the last widget, and so on until all widgets are
 * processed the same way. The result is a chain of widgets in the specified order. The list of widgets is released when
 * the chain is completed.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardFilterChainWidget extends BaseFilterWidget {

  private List<FilterWidget> filterChain;

  /**
   * Method for providing a list of filter widgets (a filter chain).
   * 
   * @param filterChain A list of filter widgets.
   */
  public void setFilterChain(List<FilterWidget> filterChain) {
    this.filterChain = filterChain;
  }

  @Override
  protected void init() throws Exception {
    if (this.filterChain != null) {
      // We move from the back of the list backwards:
      for (ListIterator<FilterWidget> i = this.filterChain.listIterator(this.filterChain.size()); i.hasPrevious();) {
        FilterWidget filter = i.previous();
        filter.setChildWidget(getChildWidget());
        setChildWidget(filter);
      }
      this.filterChain = null;
    }

    this.filterChain = null;
    super.init();
  }
}
