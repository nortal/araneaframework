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

package org.araneaframework.http.router;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ReadWriteLock;
import org.araneaframework.core.util.ReaderPreferenceReadWriteLock;
import org.araneaframework.http.util.ServletUtil;

/**
 * Associates this service with the HttpSession. If a session does not exist, it is created.
 * Also handles the invalidation of the session.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class StandardHttpSessionRouterService extends BaseService {
  
  private static final long serialVersionUID = 1L;
  
  private static final Logger log = Logger.getLogger(StandardHttpSessionRouterService.class);
  
  /**
   * The destroy parameter key in the request.
   */
  public static final String DESTROY_SESSION_PARAMETER_KEY = "destroySession";
  
  /**
   * The synchronization parameter key in the request.
   *
   * @since 1.1
   */
  public static final String SYNC_PARAMETER_KEY = "sync";
  
  /**
   * The key of the service in the session.
   */
  public static final String SESSION_SERVICE_KEY = "sessionService";
  
  /**
   * The key of the synchronization object in the session.
   *
   * @since 1.1
   */
  public static final String SESSION_SYNC_OBJECT_KEY = "sessionSyncObject";
  
  private ServiceFactory serviceFactory;
  
  private Map locks = Collections.synchronizedMap(new HashMap());
  
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
    HttpSession sess = ServletUtil.getRequest(input).getSession();
    
    boolean destroySession = input.getGlobalData().get(DESTROY_SESSION_PARAMETER_KEY) != null;   
    if (destroySession) {
      sess.invalidate();  //XXX: Do we neeed to sync this?                    
      return;
    }
    
    // Requests are synchronized by default (if "sync" parameter is missing)
    if (!"false".equals(input.getGlobalData().get(SYNC_PARAMETER_KEY))) {
      /*
       * "Synchronized" requests use an additional dummy object for
       * synchronization, so that only one "synchronized" request is processed
       * at a time.
       */
      synchronized (getOrCreateSessionSyncObject(sess)) {
        doAction(path, input, output, sess); 
      }     
    }
    else {
      doAction(path, input, output, sess);
    }
  }
  
  /**
   * @since 1.1
   */
  protected void doAction(Path path, InputData input, OutputData output, HttpSession sess) throws Exception {
    /*
     * Both "synchronized" and "unsynchronized" requests use the session object
     * to synchronize critical sections dealing with locking in this method.
     */
    synchronized (sess) {
      ReadWriteLock lock = (ReadWriteLock) locks.get(sess);
      if (lock == null) {
        lock = new ReaderPreferenceReadWriteLock();
        locks.put(sess, lock);
      }
      lock.readLock().acquire();
    }
    
    RelocatableService service = getOrCreateSessionService(sess);
    
    try {
      service._getService().action(path, input, output);
    }
    finally {
      ReadWriteLock lock = (ReadWriteLock) locks.get(sess);
      lock.readLock().release();
    }
    
    synchronized (sess) {
      ReadWriteLock lock = (ReadWriteLock) locks.get(sess);
      if (lock.writeLock().attempt(0)) {
        try {
          log.info("Propagating session updates to cluster nodes");

          service._getRelocatable().overrideEnvironment(null);

          try {
            sess.setAttribute(SESSION_SERVICE_KEY, service);
          }
          catch (IllegalStateException  e) {
            log.warn("Session invalidated before request was finished.");
          }
        }
        finally {
          locks.remove(sess); // XXX why do we remove the lock? why not do lock.writeLock().release();
        }        
      }
    }
  }
  
  public void propagate(Message message, InputData input, OutputData output) { // XXX who uses this method?
    HttpSession sess = ServletUtil.getRequest(input).getSession();
    
    RelocatableService service = getOrCreateSessionService(sess);
    
    message.send(null, service);
    
    sess.setAttribute(SESSION_SERVICE_KEY, service);
  }
  
  private synchronized Object getOrCreateSessionSyncObject(HttpSession sess) {
    Object syncObject = sess.getAttribute(SESSION_SYNC_OBJECT_KEY);
    if (syncObject == null) {
      syncObject = new Serializable() {};
      sess.setAttribute(SESSION_SYNC_OBJECT_KEY, syncObject); // XXX why do we store this object in session? why not keep it in a synchronized map?
    }
    return syncObject;
  }
  
  private synchronized RelocatableService getOrCreateSessionService(HttpSession sess) {
    Environment newEnv = new StandardEnvironment(getEnvironment(), HttpSession.class, sess);
    
    RelocatableService result = null;   
    
    if (sess.getAttribute(SESSION_SERVICE_KEY) == null) {
      log.debug("Created HTTP session '"+sess.getId()+"'");
      result = new RelocatableDecorator(serviceFactory.buildService(getEnvironment()));        
      
      result._getComponent().init(getScope(), newEnv);
    }
    else {
      result = (RelocatableService) sess.getAttribute(SESSION_SERVICE_KEY);
      result._getRelocatable().overrideEnvironment(newEnv);
      log.debug("Reusing HTTP session '"+sess.getId()+"'");
    }
    
    return result;
  }

}
