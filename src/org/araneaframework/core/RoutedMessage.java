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

package org.araneaframework.core;

import org.apache.log4j.Logger;
import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.Path;
import org.araneaframework.core.util.ExceptionUtil;

public abstract class RoutedMessage implements Message {
  private static final Logger log = Logger.getLogger(RoutedMessage.class);
  
  private Path path;
  private String destination;
  
  public RoutedMessage(Path path) {
    Assert.notNullParam(this, path, "path");
    
    this.path = path;
    destination = path.toString();
  }

  public final void send(Object id, Component component) {
    //After routing finished
    if (!path.hasNext())
      return;
    
    //Before named hierarchy
    if (id == null)
      component._getComponent().propagate(this);
    
    //Routing to next
    if (path.getNext().equals(id)) {
      path.next();
      
      if (path.hasNext())
        component._getComponent().propagate(this);
      else {
        log.debug("Delivering message routed to '" + destination + "'");
        try {
          execute(component);
        }
        catch (Exception e) {
          throw ExceptionUtil.uncheckException(e);
        }
      }
    }
  }
  
  protected abstract void execute(Component component) throws Exception;
}
