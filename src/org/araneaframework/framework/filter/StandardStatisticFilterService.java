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
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * A filter for calculating the time it takes for the child service to complete
 * its action method (the request). The elapsed time is available through
 * <code>getRequestTime()</code> and it is also logged with the INFO level. The logging
 * statement can have a prefix namespace set via <code>setNamespace()</code>.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardStatisticFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardStatisticFilterService.class);
  private String namespace;
  private long requestTime=-1;
  private long maxAllowedRequestTime=-1;
  
  public void setRequestTime(long requestTime) {
    this.requestTime = requestTime;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public void setMaxAllowedRequestTime(long maxAllowedRequestTime) {
    this.maxAllowedRequestTime = maxAllowedRequestTime;
  }

  protected void init() throws Exception {
    super.init();
    
    log.debug("Statistics filter service initialized.");
  }
  
  protected void destroy() throws Exception {
    super.destroy();
    
    log.debug("Statistics filter service destroyed.");
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    long start = System.currentTimeMillis();
    
    childService._getService().action(path, input, output);
    requestTime = System.currentTimeMillis() - start;
    log.info(namespace + ": request took " + requestTime + " ms.");
    
    if (maxAllowedRequestTime!=-1 && requestTime>maxAllowedRequestTime) {
      throw new AraneaRuntimeException("Request processing took longer than allowed. Allowed: <"
            +maxAllowedRequestTime+"ms Real: "+requestTime+"ms");
    }
  }
    
  public String getNamespace() {
    return namespace;
  }

  public long getRequestTime() {
    return this.requestTime;
  }
}
