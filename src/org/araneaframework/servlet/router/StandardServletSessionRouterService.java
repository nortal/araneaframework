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

package org.araneaframework.servlet.router;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardRelocatableServiceDecorator;
import org.araneaframework.servlet.ServletInputData;

/**
 * Associates this service with the HttpSession. Is a session does not exist, it is created.
 * Also handles the invalidation of the session.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardServletSessionRouterService extends BaseService {
  private static final Logger log = Logger.getLogger(StandardServletSessionRouterService.class);
  
  /**
   * The destroy parameter key in the request.
   */
  public static final String DESTROY_SESSION_PARAMETER_KEY = "destroySession";
  
  /**
   * The key of the service in the session.
   */
  public static final String SESSION_SERVICE_KEY = "sessionService";
  
  private ServiceFactory serviceFactory;
  
  /**
   * Sets the factory which is used to build the service if one does not exist in the session.
   */
  public void setSessionServiceFactory(ServiceFactory factory) {
    serviceFactory = factory;
  }

  /**
   * Routes an action to the service in the session. If the service does not exist,
   * it is created.
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {       
    HttpSession sess = 
      ((ServletInputData) input).getRequest().getSession();
    
    boolean destroySession = ((ServletInputData)input).getGlobalData().get(DESTROY_SESSION_PARAMETER_KEY)!=null;
    
    Map map = new HashMap();
    map.put(HttpSession.class, sess);
    Environment newEnv = new StandardEnvironment(getEnvironment(), map);
    
    //XXX Should we synchronize on session?
    synchronized (sess) {
      Relocatable.RelocatableService service = null;   
            
      if (destroySession) {       
        sess.invalidate();                    
        return;
      }
      
      if (sess.getAttribute(SESSION_SERVICE_KEY) == null) {
        log.debug("Created HTTP session '"+sess.getId()+"'");
        service = new StandardRelocatableServiceDecorator(serviceFactory.buildService());        
        
        service._getComponent().init(newEnv);
      }
      else {
        service = (Relocatable.RelocatableService) sess.getAttribute(SESSION_SERVICE_KEY);
        service._getRelocatable().overrideEnvironment(newEnv);
        log.debug("Reusing HTTP session '"+sess.getId()+"'");
      }
      
      try {
        service._getService().action(path, input, output);
      }
      finally {
        service._getRelocatable().overrideEnvironment(null);
        try {        
          sess.setAttribute(SESSION_SERVICE_KEY, service);
        }
        catch (IllegalStateException  e) {
          log.warn("Session invalidated before request was finished", e);
        }
      }
    }
  }
}
