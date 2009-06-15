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

package org.araneaframework.http.core;

import java.util.Collections;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Relocatable;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.http.router.StandardHttpSessionRouterService;

/**
 * A session listener which takes care of destroying the session service in the
 * session.
 * 
 * @author "Taimo Peelo" (taimo@araneaframework.org)
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardSessionListener implements HttpSessionListener {

  public static final Log log = LogFactory.getLog(StandardSessionListener.class);

  protected boolean destroyComponents = true;

  public void setDestroyComponents(boolean destroyComponents) {
    this.destroyComponents = destroyComponents;
  }

  public void sessionCreated(HttpSessionEvent sessEvent) {
    if (log.isDebugEnabled()) {
      log.debug("Session '" + sessEvent.getSession().getId() + "' created");
    }
  }

  public void sessionDestroyed(HttpSessionEvent sessEvent) {

    if (sessEvent.getSession().getAttribute(
        StandardHttpSessionRouterService.SESSION_SERVICE_KEY) != null) {
      // Aranea component hierarchy handle is present in session.
      // Invoke their destruction.
      destroyComponents(sessEvent);
    }

    if (log.isDebugEnabled()) {
      log.debug("Session " + sessEvent.getSession().getId() + " destroyed");
    }
  }

  /**
   * Propagates <code>destroy()</code> on all Aranea components.
   * 
   * @since 1.2.2
   */
  protected void destroyComponents(HttpSessionEvent sessEvent) {
    if (!this.destroyComponents) {
      return;
    }

    RelocatableDecorator service =
      (RelocatableDecorator) sessEvent.getSession().getAttribute(
            StandardHttpSessionRouterService.SESSION_SERVICE_KEY);

    if (service != null) {
      try {
        Relocatable.Interface relocatable = service._getRelocatable();

        if (relocatable.getCurrentEnvironment() == null) {
          relocatable.overrideEnvironment(new StandardEnvironment(null,
              Collections.EMPTY_MAP));
        }

        service._getComponent().destroy();
      } catch (Exception e) {
        log.error("Exception while destroying service in an expired session", e);
      }
    }
  }

}
