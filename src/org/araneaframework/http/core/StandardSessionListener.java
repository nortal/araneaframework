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

package org.araneaframework.http.core;


import javax.servlet.http.HttpSession;
import java.util.Collections;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Relocatable;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.Assert;
import org.araneaframework.http.router.StandardHttpSessionRouterService;

/**
 * A session listener which takes care of destroying the session service in the session.
 * 
 * @author "Taimo Peelo" (taimo@araneaframework.org)
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardSessionListener implements HttpSessionListener {

  public static final Log LOG = LogFactory.getLog(StandardSessionListener.class);

  public void sessionCreated(HttpSessionEvent sessEvent) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Session '" + sessEvent.getSession().getId() + "' created");
    }
  }

  public void sessionDestroyed(HttpSessionEvent sessEvent) {
    if (containsSessionService(sessEvent)) {
      // Aranea component hierarchy handle is present in session.
      // Invoke their destruction.
      destroyComponents(sessEvent);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Session " + sessEvent.getSession().getId() + " destroyed");
    }
  }

  /**
   * Checks whether the session contains Aranea session service. If it is not in the session then it is a sign that
   * Aranea components have not been created for this session.
   * 
   * @param sessEvent The session event object.
   * @return Whether the session contains session service.
   */
  protected boolean containsSessionService(HttpSessionEvent sessEvent) {
    Assert.notNullParam(this, sessEvent, "sessEvent");
    HttpSession session = sessEvent.getSession();
    return session != null && session.getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY) != null;
  }

  /**
   * Propagates <code>destroy()</code> on all Aranea components.
   * 
   * @since 1.2.2
   */
  @SuppressWarnings("unchecked")
  protected void destroyComponents(HttpSessionEvent sessEvent) {
    RelocatableDecorator service = (RelocatableDecorator) sessEvent.getSession().getAttribute(
        StandardHttpSessionRouterService.SESSION_SERVICE_KEY);

    if (service != null) {
      try {
        Relocatable.Interface relocatable = service._getRelocatable();

        if (relocatable.getCurrentEnvironment() == null) {
          relocatable.overrideEnvironment(new StandardEnvironment(null, Collections.EMPTY_MAP));
        }

        service._getComponent().destroy();
      } catch (Exception e) {
        LOG.error("Exception while destroying service in an expired session", e);
      }
    }
  }

}
