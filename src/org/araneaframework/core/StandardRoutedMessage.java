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

import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.Path;

public abstract class StandardRoutedMessage implements Message {
  private Path path;
  
  public StandardRoutedMessage(Path path) {
    this.path = path;
  }

  public final void send(Object id, Component component) throws Exception {
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
      else
        execute(component);
    }
  }
  
  protected abstract void execute(Component component) throws Exception;
}
