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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.framework.ExceptionHandlerFactory;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * A custom exception handling filter. If the child service's action method throws an exception,
 * then the response stream is rollbacked, a exception handler constructed, inited and the
 * request routed to the handler.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardCriticalExceptionHandlingFilterService extends BaseFilterService {
  private static final Log log = LogFactory.getLog(StandardCriticalExceptionHandlingFilterService.class);
  private ExceptionHandlerFactory factory;
  
  /**
   * Set the factory for creating the exception handling service.
   */
  public void setExceptionHandlerFactory(ExceptionHandlerFactory factory) {
    this.factory = factory;
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    AtomicResponseHelper arUtil = 
      new AtomicResponseHelper(output);
    
    try {
      childService._getService().action(path, input, output);
    }
    catch (Throwable e) {               
      if (ExceptionUtils.getRootCause(e) != null)
        log.error("Critical exception occured: ", ExceptionUtils.getRootCause(e));
      else
        log.error("Critical exception occured: ", e);
      
      if (e instanceof Error && 
          !(e instanceof StackOverflowError))
        throw (Error) e;
      
      arUtil.rollback();
      
      Service service = factory.buildExceptionHandler(e, getEnvironment());
      service._getComponent().init(getScope(), getEnvironment());
      try {
        log.debug("Routing request to the continuation.");
        service._getService().action(null, input, output);
      }
      finally {
        service._getComponent().destroy();
      }
    }

    arUtil.commit();
  }
}
