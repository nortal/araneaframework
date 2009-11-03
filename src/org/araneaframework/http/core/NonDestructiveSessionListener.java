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

import javax.servlet.http.HttpSessionEvent;

/**
 * A session listener which does not destroy the session service and other
 * Aranea components in the session. Useful to be used, e.g. when using remote
 * authentication techniques that invalidate the session.
 * 
 * @author "Taimo Peelo" (taimo@araneaframework.org)
 */
public class NonDestructiveSessionListener extends StandardSessionListener {

  @Override
  public void sessionDestroyed(HttpSessionEvent sessEvent) {
    if (LOG.isDebugEnabled() && containsSessionService(sessEvent)) {
      LOG.debug("Not destroying the Aranea components in the session session.");
      LOG.debug("Session " + sessEvent.getSession().getId() + " destroyed");
    }
  }

}
