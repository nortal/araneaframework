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

package org.araneaframework.http.router;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.ApplicationService;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ReadWriteLock;
import org.araneaframework.core.util.ReaderPreferenceReadWriteLock;
import org.araneaframework.framework.AsynchronousRequestRegistry;
import org.araneaframework.http.util.ServletUtil;

/**
 * Associates this service with the HttpSession. If a session does not exist, it is created. Also handles the
 * invalidation of the session.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class StandardHttpSessionRouterService extends BaseService implements AsynchronousRequestRegistry {

  private static final Log LOG = LogFactory.getLog(StandardHttpSessionRouterService.class);

  /**
   * The destroy parameter key in the request.
   */
  public static final String DESTROY_SESSION_PARAMETER_KEY = "destroySession";

  /**
   * The synchronization parameter key in the request.
   * 
   * @since 1.1
   */
  public static final String SYNC_PARAMETER_KEY = "araSync";

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

  private Map<HttpSession, ReadWriteLock> locks = Collections
      .synchronizedMap(new HashMap<HttpSession, ReadWriteLock>());

  private Map<String, Object> attributes = Collections.synchronizedMap(new HashMap<String, Object>());

  /**
   * A list of asynchronous actions by [target.name].
   * 
   * @since 2.0
   */
  private List<String> asynchronousActions = new LinkedList<String>();

  /**
   * Sets the factory which is used to build the service if one does not exist in the session.
   */
  public void setSessionServiceFactory(ServiceFactory factory) {
    this.serviceFactory = factory;
  }

  public void registerAsynchronousAction(String parentScope, String actionId) {
    Assert.notNullParam(this, parentScope, "parentScope");
    Assert.notNullParam(this, actionId, "actionId");
    this.asynchronousActions.add(parentScope + Path.SEPARATOR + actionId);
  }

  public void unregisterAsynchronousAction(String parentScope, String actionId) {
    Assert.notNullParam(this, parentScope, "parentScope");
    Assert.notNullParam(this, actionId, "actionId");
    this.asynchronousActions.remove(parentScope + Path.SEPARATOR + actionId);
  }

  /**
   * Routes an action to the service in the session. If the service does not exist, it is created.
   */
  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpSession sess = ServletUtil.getRequest(input).getSession();

    boolean destroySession = input.getGlobalData().get(DESTROY_SESSION_PARAMETER_KEY) != null;
    if (destroySession) {
      sess.invalidate();
    } else if (!isAsynchronous(input)) {
      // "Synchronized" requests use an additional dummy object for synchronization, so that only one "synchronized"
      // request is processed at a time.
      synchronized (getOrCreateSessionSyncObject()) {
        doAction(path, input, output, sess);
      }
    } else {
      doAction(path, input, output, sess);
    }
  }

  private boolean isAsynchronous(InputData input) {
    Map<String, String> data = input.getGlobalData();
    String actionId = data.get(ApplicationService.ACTION_HANDLER_ID_KEY);
    String targetPath = data.get(ApplicationService.ACTION_PATH_KEY);
    String sync = data.get(SYNC_PARAMETER_KEY);

    boolean result = isAsynchronous(actionId, targetPath) || !"false".equals(sync);
    if (LOG.isInfoEnabled()) {
      LOG.info("The action '" + targetPath + Path.SEPARATOR + actionId
          + "' is to be processed asynchronously!");
    }

    return result;
  }

  public boolean isAsynchronous(String actionId, String targetPath) {
    final String defaultStr = "-";
    StringBuffer action = new StringBuffer(StringUtils.defaultIfEmpty(targetPath, defaultStr));
    action.append('.').append(StringUtils.defaultIfEmpty(actionId, defaultStr));
    return this.asynchronousActions.contains(action.toString());
  }

  /**
   * @since 1.1
   */
  protected void doAction(Path path, InputData input, OutputData output, HttpSession sess) throws Exception {

    /*
     * Both "synchronized" and "unsynchronized" requests use the session object to synchronize critical sections dealing
     * with locking in this method.
     */

    synchronized (sess) {
      ReadWriteLock lock = this.locks.get(sess);
      if (lock == null) {
        lock = new ReaderPreferenceReadWriteLock();
        this.locks.put(sess, lock);
      }
      lock.readLock().acquire();
    }

    RelocatableService service = getOrCreateSessionService(sess);

    try {
      service._getService().action(path, input, output);
    } finally {
      ReadWriteLock lock = this.locks.get(sess);
      lock.readLock().release();
    }

    synchronized (sess) {
      ReadWriteLock lock = this.locks.get(sess);
      if (lock.writeLock().attempt(0)) {
        try {
          LOG.info("Propagating changes to session");
          service._getRelocatable().overrideEnvironment(null);

          try {
            sess.setAttribute(SESSION_SERVICE_KEY, service);
          } catch (IllegalStateException e) {
            LOG.warn("Session invalidated before request was finished.");
          }
        } finally {
          this.locks.remove(sess);
        }
      }
    }
  }

  private synchronized Object getOrCreateSessionSyncObject() {
    Object syncObject = this.attributes.get(SESSION_SYNC_OBJECT_KEY);
    if (syncObject == null) {
      syncObject = new SessionSyncObject();
      this.attributes.put(SESSION_SYNC_OBJECT_KEY, syncObject);
    }
    return syncObject;
  }

  private synchronized RelocatableService getOrCreateSessionService(HttpSession sess) {
    Environment newEnv = new StandardEnvironment(getEnvironment(), HttpSession.class, sess);

    RelocatableService result = null;

    if (sess.getAttribute(SESSION_SERVICE_KEY) == null) {
      LOG.debug("HTTP session '" + sess.getId() + "' was started.");
      result = new RelocatableDecorator(serviceFactory.buildService(getEnvironment()));
      result._getComponent().init(getScope(), newEnv);

    } else {
      result = (RelocatableService) sess.getAttribute(SESSION_SERVICE_KEY);
      result._getRelocatable().overrideEnvironment(newEnv);
      LOG.debug("Reusing HTTP session '" + sess.getId() + "'");
    }

    return result;
  }

  @Override
  public Environment getEnvironment() {
    return new StandardEnvironment(super.getEnvironment(), AsynchronousRequestRegistry.class, this);
  }

  // Objects of this class will be held in session. It is static so that it
  // would not depend on its parent as the latter might not serialize well.
  private static class SessionSyncObject implements Serializable {}

}
