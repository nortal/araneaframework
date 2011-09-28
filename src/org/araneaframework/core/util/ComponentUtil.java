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

package org.araneaframework.core.util;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.math.RandomUtils;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Scope;
import org.araneaframework.core.ApplicationComponent;
import org.araneaframework.core.StandardScope;
import org.araneaframework.core.action.ActionListener;
import org.araneaframework.core.action.SynchronizedActionListener;
import org.araneaframework.framework.SynchronizedRequestRegistry;

/**
 * This utility class handles listening of component life-cycle events. Thus, it is possible to write a component (the
 * listener) that, for example, modifies the child <code>Environment</code> of a component (the target).
 * <p>
 * Its usage is quite simple:
 * 
 * <pre>
 * pushGlobalEnvEntry(entryId, envEntry);
 * BaseWidget listenerComp = new BaseWidget() {
 * 
 *   protected void destroy() throws Exception {
 *     popGlobalEnvEntry(entryId);
 *   }
 * };
 * ComponentUtil.addListenerComponent(targetComp, listenerComp);
 * </pre>
 * In the example above, it uses the life-cycle listener to remove an <code>Environment</code> entry that was added
 * before.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class ComponentUtil {

  /**
   * The request key prefix that will be used to identify the listeners invoked. Prior to 1.0.2 this constant contained
   * illegal characters (dots), thus using <code>addListenerComponent</code> broke component name scoping.
   */
  public static final String LISTENER_KEY = "ComponentUtil_LISTENER";

  /**
   * Instantiating this class is prohibited.
   */
  private ComponentUtil() {
    throw new UnsupportedOperationException();
  }

  /**
   * This method will attach the listener component to the target custom component, allowing it to receive all the
   * life-cycle events (which exactly depends on the target component type). The listener component may never get to do
   * something more than processing life-cycle events.
   * <p>
   * This allows for instance to add a child component that will execute some action on destroy, thus essentially tying
   * some action to the life-cycle of the target component. A typical application is to scope something (e.g.
   * environment entry) with the target component.
   * <p>
   * This method also handles initialization of the listener.
   * 
   * @param target The component that wants to have the new listener.
   * @param listener The listener component that will be added as a child component of the <code>target</code>.
   */
  public static void addListenerComponent(ApplicationComponent target, Component listener) {
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

  // allows adding listener components to not yet initialized components by failing lazily.
  private static class LateBindingChildEnvironment implements Environment {

    private final ApplicationComponent component;

    public LateBindingChildEnvironment(ApplicationComponent component) {
      this.component = component;
    }

    public <T> T getEntry(Class<T> key) {
      return getDelegateEnvironment().getEntry(key);
    }

    public <T> T requireEntry(Class<T> key) {
      return getDelegateEnvironment().requireEntry(key);
    }

    private Environment getDelegateEnvironment() {
      Environment result = this.component.getChildEnvironment();
      if (result == null) {
        throw new IllegalStateException(getClass().getName() + " does not yet have access to environment.");
      }
      return result;
    }
  }

  /**
   * This method will register <code>actionListener</code> as synchronized, if it implements
   * {@link SynchronizedActionListener}. All parameters are required, except environment. If environment is
   * <code>null</code> then no changes will be made.
   * 
   * @param env The component's environment. Used for {@link SynchronizedRequestRegistry} lookup.
   * @param componentScope The scope of the calling component.
   * @param actionId The action listener ID.
   * @param actionListener The object to check for {@link SynchronizedActionListener}.
   * @since 2.0
   */
  public static void registerActionListener(Environment env, Scope componentScope, String actionId,
      ActionListener actionListener) {
    handleActionListener(true, env, componentScope, actionId, actionListener);
  }

  /**
   * This method will loop items in <code>actionListeners</code> and register an action listener as synchronized when
   * interface {@link SynchronizedActionListener} is present on that listener. All parameters are required, except
   * environment. If environment is <code>null</code> then no changes will be made.
   * 
   * @param env The component's environment. Used for {@link SynchronizedRequestRegistry} lookup.
   * @param componentScope The scope of the calling component.
   * @param actionListeners The action listeners to check for {@link SynchronizedActionListener}s.
   * @since 2.0
   */
  public static void registerActionListeners(Environment env, Scope componentScope,
      Map<String, List<ActionListener>> actionListeners) {
    handleActionListeners(true, env, componentScope, actionListeners);
  }

  /**
   * This method will unregister synchronized <code>actionListener</code> (if it implements
   * {@link SynchronizedActionListener}) to stop its requests to be processed in synchronized manner, . All parameters
   * are required, except environment. If environment is <code>null</code> then no changes will be made.
   * 
   * @param env The component's environment. Used for {@link SynchronizedRequestRegistry} lookup.
   * @param componentScope The scope of the calling component.
   * @param actionId The action listener ID.
   * @param actionListener The action listener to check for {@link SynchronizedActionListener}.
   * @since 2.0
   */
  public static void unregisterActionListener(Environment env, Scope componentScope, String actionId,
      ActionListener actionListener) {
    handleActionListener(false, env, componentScope, actionId, actionListener);
  }

  /**
   * This method will unregister synchronized <code>actionListeners</code> (when {@link SynchronizedActionListener} is
   * implemented) to stop their requests to be processed in synchronized manner. All parameters are required, except
   * environment. If environment is <code>null</code> then no changes will be made.
   * 
   * @param env The component's environment. Used for {@link SynchronizedRequestRegistry} lookup.
   * @param componentScope The scope of the calling component.
   * @param actionListeners The action listeners to check for {@link SynchronizedActionListener}s.
   * @since 2.0
   */
  public static void unregisterActionListeners(Environment env, Scope componentScope,
      Map<String, List<ActionListener>> actionListeners) {
    handleActionListeners(false, env, componentScope, actionListeners);
  }

  /**
   * This method will unregister synchronized <code>actionListeners</code> (when {@link SynchronizedActionListener} is
   * implemented) to stop their requests to be processed in synchronized manner, . All parameters are required, except
   * environment. If environment is <code>null</code> then no changes will be made.
   * 
   * @param env The component's environment. Used for {@link SynchronizedRequestRegistry} lookup.
   * @param componentScope The scope of the calling component.
   * @param actionId The action listener ID.
   * @param actionListeners The action listeners to check for {@link SynchronizedActionListener}s.
   * @since 2.0
   */
  public static void unregisterActionListeners(Environment env, Scope componentScope, String actionId,
      List<ActionListener> actionListeners) {
    if (env != null && actionListeners != null && !actionListeners.isEmpty()) {
      for (ActionListener listener : actionListeners) {
        unregisterActionListener(env, componentScope, actionId, listener);
      }
    }
  }

  private static void handleActionListeners(boolean register, Environment env, Scope componentScope,
      Map<String, List<ActionListener>> actionListeners) {
    if (env != null && actionListeners != null && !actionListeners.isEmpty()) {
      for (Map.Entry<String, List<ActionListener>> entry : actionListeners.entrySet()) {
        for (ActionListener listener : entry.getValue()) {
          if (listener != null) {
            if (register) {
              registerActionListener(env, componentScope, entry.getKey(), listener);
            } else {
              unregisterActionListener(env, componentScope, entry.getKey(), listener);
            }
          }
        }
      }
    }
  }

  private static void handleActionListener(boolean register, Environment env, Scope componentScope, String actionId,
      ActionListener actionListener) {
    Assert.notNullParam(ComponentUtil.class, componentScope, "componentScope");
    Assert.notNullParam(ComponentUtil.class, actionId, "actionId");
    Assert.notNullParam(ComponentUtil.class, actionListener, "actionListener");

    if (env != null) {
      SynchronizedRequestRegistry registry = env.getEntry(SynchronizedRequestRegistry.class);
      if (registry != null && actionListener instanceof SynchronizedActionListener) {
        if (register) {
          registry.registerSynchronizedAction(componentScope.toString(), actionId);
        } else {
          registry.unregisterSynchronizedAction(componentScope.toString(), actionId);
        }
      }
    }
  }
}
