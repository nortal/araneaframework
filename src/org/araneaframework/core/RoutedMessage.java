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

package org.araneaframework.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.Path;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * {@link Message} that is sent to exactly one {@link Component} in hierarchy.
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class RoutedMessage implements Message {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(RoutedMessage.class);

  private Path path;

  private String destination;

  public RoutedMessage(Path path) {
    Assert.notNullParam(this, path, "path");

    this.path = path;
    destination = path.toString();
  }

  /**
   * Sends method that causes {@link RoutedMessage#execute(Component)} to be called for 
   * {@link Component} identified in hierarchy by the given <code>id</code>. 
   * 
   * @see org.araneaframework.Message#send(java.lang.Object, org.araneaframework.Component)
   */
  public final void send(Object id, Component component) {
    //After routing finished
    if (!path.hasNext()) {
      return;
    }
    
    //Before named hierarchy
    if (id == null) {
      component._getComponent().propagate(this);
      return;
    }
    
    //Routing to next
    if (path.getNext().equals(id)) {
      path.next();
      
      if (path.hasNext()) {
        component._getComponent().propagate(this);
      } else {
        log.debug("Delivering message routed to '" + destination + "'");
        try {
          execute(component);
        } catch (Exception e) {
          throw ExceptionUtil.uncheckException(e);
        }
      }
    }
  }

  /**
   * Method that is called on {@link Component} that is target of this
   * {@link RoutedMessage}.
   * 
   * @param component {@link Component} this {@link RoutedMessage} has reached
   */
  protected abstract void execute(Component component) throws Exception;

}
