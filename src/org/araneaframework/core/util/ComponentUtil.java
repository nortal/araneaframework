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
import org.araneaframework.Environment;
import org.araneaframework.core.ApplicationComponent;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoSuchEnvironmentEntryException;
import org.araneaframework.core.StandardScope;

/**
 * This utility class handles listening of component lifecycle events. Thus, it
 * is possible to write a component (the listener) that, for example, modifies
 * the child <code>Environment</code> of a component (the target).
 * <p>
 * Its usage is quite simple:
 * <pre><code>pushGlobalEnvEntry(entryId, envEntry);
 * 
 * BaseWidget listenerComp = new BaseWidget() {
 * 
 *   protected void destroy() throws Exception {
 *     popGlobalEnvEntry(entryId);
 *   }
 * 
 * };
 * ComponentUtil.addListenerComponent(targetComp, listenerComp);</code></pre>
 * 
 * In the example above, it uses the lifecycle listener to remove an
 * <code>Environment</code> entry that was added before.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class ComponentUtil {

  /**
   * The request key prefix that will be used to identify the listeners invoked.
   * Prior to 1.0.2 this constant contained illegal characters (dots), thus
   * using <code>addListenerComponent</code> broke component name scoping.
   */
  public static final String LISTENER_KEY = "ComponentUtil_LISTENER";
  
  /**
   * This method will attach the listener component to the target custom
   * component, allowing it to receive all the lifecycle events (which exactly
   * depends on the target component type). The listener component may never
   * get to do something more than processing lifecycle events.
   * <p>
   * This allows for instance to add a child component that will execute some
   * action on destroy, thus essentially tying some action to the lifecycle of
   * the target component. A typical application is to scope something (e.g.
   * environment entry) with the target component.
   * <p>
   * This method also handles initialization of the listener.
   * 
   * @param target The component that wants to have the new listener.
   * @param listener The listener component that will be added as a child
   *            component of the <code>target</code>.
   */
  public static void addListenerComponent(ApplicationComponent target,
      Component listener) {

    Assert.notNullParam(target, "target");
    Assert.notNullParam(listener, "listener");

    String key = LISTENER_KEY;
    while (target._getComposite().getChildren().get(key) != null) {
      key = LISTENER_KEY + RandomUtils.nextLong();
    }

    Environment env = target.isAlive() ? target.getChildEnvironment() : new LateBindingChildEnvironment(target);
    listener._getComponent().init(new StandardScope(key, target.getScope()), env);

    target._getComposite().attach(key, listener);    
  }

  // allows adding listener components to not yet initialized components by failing lazily
  private static class LateBindingChildEnvironment implements Environment {

    private static final long serialVersionUID = 1L;

    private ApplicationComponent component;

    public LateBindingChildEnvironment(ApplicationComponent component) {
      this.component = component;
    }

    public <T> T getEntry(Class<T> key) {
      return getDelegateEnvironment().getEntry(key);
    }

    public <T> T requireEntry(Class<T> key)
        throws NoSuchEnvironmentEntryException {
      return getDelegateEnvironment().requireEntry(key);
    }

    private Environment getDelegateEnvironment() {
      Environment result = component.getChildEnvironment();
      if (result == null) {
        throw new IllegalStateException(getClass().getName()
            + " does not yet have access to environment.");
      }
      return result;
    }

  }

}
