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

import java.util.HashMap;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.http.router.StandardHttpSessionRouterService;

/**
 * A session listener which takes care of destroying the session service in the session.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardSessionListener implements HttpSessionListener {
  public static final Log log = LogFactory.getLog(StandardSessionListener.class);
  
  public void sessionCreated(HttpSessionEvent sessEvent) {
    log.debug("Session '"+sessEvent.getSession().getId()+"' created");
  }

  public void sessionDestroyed(HttpSessionEvent sessEvent) {
    if (sessEvent.getSession().getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY) != null ) {
      RelocatableDecorator service = 
        (RelocatableDecorator) sessEvent.getSession().getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY);
      
      if (service != null)
        try {
          if (service._getRelocatable().getCurrentEnvironment() == null)
            service._getRelocatable().overrideEnvironment(new StandardEnvironment(null, new HashMap()));
          service._getComponent().destroy();
        }
        catch(Exception e) {
          log.error("Exception while destroying service in an expired session", e);
        }
    }
    log.debug("Session "+sessEvent.getSession().getId()+" destroyed");
  }
}
