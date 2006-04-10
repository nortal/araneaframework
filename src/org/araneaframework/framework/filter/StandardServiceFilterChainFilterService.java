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

package org.araneaframework.framework.filter;

import java.util.List;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import org.araneaframework.framework.FilterService;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardServiceFilterChainFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardServiceFilterChainFilterService.class);
  
  private List filterChain;  
  
  public void setFilterChain(List filterChain) {
    this.filterChain = filterChain;
  }
  
  protected void init() throws Exception {      
    for (ListIterator i = filterChain.listIterator(filterChain.size()); i.hasPrevious();) {
      FilterService filter = (FilterService) i.previous();
      
      filter.setChildService(childService);
      childService = filter;
    }
    
    super.init();       
  }
}
