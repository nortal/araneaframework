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
package org.araneaframework.core.util;

import org.apache.commons.lang.math.RandomUtils;
import org.araneaframework.Component;
import org.araneaframework.core.ApplicationComponent;
import org.araneaframework.core.Assert;

/**
 * This utility class contains methods for managing Aranea components.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class ComponentUtil {
  public static final String LISTENER_KEY = "org.araneaframework.core.util.ComponentUtil.LISTENER";
  
  /**
   * This method will attach the listener component to the target custom component, allowing it to receive
   * all the lifecycle events (which exactly depends on the target component type).   
   * <p>
   * This allows for instance to add a child component that will execute some action on destroy, thus
   * essentially tying some action to the lifecycle of the target component. A typical application
   * is to scope something (e.g. environment entry) with the target component.
   */
  public static void addListenerComponent(ApplicationComponent target, Component listener) {
    Assert.notNullParam(target, "target");
    Assert.notNullParam(listener, "listener");
    
    listener._getComponent().init(target.getChildEnvironment());
           
    String key = LISTENER_KEY;    
    while (target._getComposite().getChildren().get(key) != null) {
      key = LISTENER_KEY + RandomUtils.nextLong();
    }
    
    target._getComposite().attach(key, listener);    
  }
}
