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

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * A filter that logs the time it takes for the child service to complete
 * its action method (serving the request). The logging
 * statement can have a prefix namespace set via <code>setNamespace()</code>.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardStatisticFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardStatisticFilterService.class);
  private String namespace;

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    long start = System.currentTimeMillis();
    
    childService._getService().action(path, input, output);
    log.info(namespace + ": action took " + (System.currentTimeMillis() - start) + " ms.");
  }
    
  public String getNamespace() {
    return namespace;
  }
}
