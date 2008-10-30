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
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardFilterChainWidget extends BaseFilterWidget {

  private static final long serialVersionUID = 1L;

  private List filterChain;

  public void setFilterChain(List filterChain) {
    this.filterChain = filterChain;
  }

  protected void init() throws Exception {
    if (filterChain != null) {
      ListIterator i = filterChain.listIterator(filterChain.size());
      for (; i.hasPrevious();) {
        FilterWidget filter = (FilterWidget) i.previous();
        filter.setChildWidget(childWidget);
        childWidget = filter;
      }
    }
    filterChain = null;
    super.init();
  }
}
