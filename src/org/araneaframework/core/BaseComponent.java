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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.Relocatable;
import org.araneaframework.Scope;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * The base class for all Aranea components. Base entities do not make a Composite pattern and only provide some very
 * basic services (mainly syncronization and messaging service)
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class BaseComponent implements Component {

  private static final Log LOG = LogFactory.getLog(BaseComponent.class);

  private Environment environment;

  private Scope scope;

  private Map<Object, Component> children;

  private Map<Object, Component> disabledChildren;

  private static final byte UNBORN = 1;

  private static final byte ALIVE = 2;

  private static final byte DEAD = 3;

  private byte state = UNBORN;

  private transient int callCount = 0;

  private transient int reentrantCallCount = 0;

  private transient ThreadLocal<Counter> reentrantTLS;

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  // *******************************************************************
  // PROTECTED METHODS
  // *******************************************************************

  /**
   * Init callback. Gets called when the component is initialized.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void init() throws Exception {}

  /**
   * Destroy callback. Gets called when the component is destroyed.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void destroy() throws Exception {}

  /**
   * Disables the component.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void disable() throws Exception {}

  /**
   * Enables the component.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void enable() throws Exception {}

  /**
   * Forwards the <code>message</code> to the component and to all of its children components.
   * 
   * @param message A message to forward.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void propagate(Message message) throws Exception {}

  /**
   * Handles any given exception.
   * 
   * @param e The exception that occurred.
   * @throws Exception Any runtime exception that may occur during error handling.
   */
  protected void handleException(Exception e) throws Exception {
    throw e;
  }

  public Environment getEnvironment() {
    return this.environment;
  }

  public Scope getScope() {
    return this.scope;
  }

  /**
   * Gets whether the component has been initialized. "Initialized" means that the <code>init()</code> method has been
   * called.
   * 
   * @return <code>true</code>, if the component has been initialized.
   */
  public boolean isInitialized() {
    return this.state != UNBORN;
  }

  public boolean isAlive() {
    return this.state == ALIVE;
  }

  /**
   * Specifies whether the component is dead. It means that the <code>destroy()</code> method has been called.
   * 
   * @return <code>true</code>, if the component has been destroyed.
   */
  public boolean isDead() {
    return this.state == DEAD;
  }

  /**
   * Sets the environment of this component to <code>env</code>.
   * 
   * @param env The new <code>Envrionment</code> for this component.
   */
  protected synchronized void _setEnvironment(Environment env) {
    this.environment = env;
  }

  /**
   * Sets the scope for this component.
   * 
   * @param scope the new scope to identify this component.
   * @since 1.1
   */
  protected void _setScope(Scope scope) {
    this.scope = scope;
  }

  /**
   * Waits until no call is made to the component. Used to synchronize calls to the component.
   */
  protected synchronized void _waitNoCall() throws InterruptedException {
    long waitStart = System.currentTimeMillis();

    while (this.callCount != this.reentrantCallCount) {
      this.wait(1000);

      if (this.callCount != this.reentrantCallCount) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Deadlock or starvation suspected, call count '" + this.callCount + "', reentrant call count '"
              + this.reentrantCallCount + "'");
        }

        if (waitStart < System.currentTimeMillis() - 10000) {
          LOG.error("Deadlock or starvation not solved in 10s, call count '" + callCount + "', reentrant call count '"
              + reentrantCallCount + "'", new AraneaRuntimeException("Deadlock or starvation suspected!"));
          return;
        }
      }
    }
  }

  /**
   * Checks if a call is valid (component is in the required state), increments the call count.
   */
  protected synchronized void _startCall() throws IllegalStateException {
    _checkCall();
    incCallCount();
  }

  /**
   * @since 1.1
   */
  protected synchronized void _strictStartCall() throws IllegalStateException {
    _strictCheckCall();
    incCallCount();
  }

  /**
   * Decrements the call count. Wakes up all threads that are waiting on this object's monitor
   */
  protected synchronized void _endCall() {
    decCallCount();
    this.notifyAll();
  }

  /**
   * Used for starting a call that is a re-entrant call. Meaning a call of this component is doing the calling
   */
  protected synchronized void _startWaitingCall() {
    if (getReentrantCount() >= 1) {
      this.reentrantCallCount++;
    }
  }

  /**
   * Decrements internal call count counter.
   */
  protected synchronized void _endWaitingCall() {
    if (getReentrantCount() >= 1) {
      this.reentrantCallCount--;
    }
  }

  /**
   * Checks if this component was initialized. If not, throws IllegalStateException. This is relatively loose check,
   * allowing leftover calls to dead components.
   * 
   * @throws IllegalStateException when component has never been initialized
   */
  protected void _checkCall() throws IllegalStateException {
    if (!isInitialized()) {
      throw new IllegalStateException("Component '" + getClass().getName() + "' was never initialized!");
    }
  }

  /**
   * Checks if this component is currently alive. This is strict check that disallows leftover calls to dead components.
   * 
   * @throws IllegalStateException when component is unborn or dead
   * @since 1.1
   */
  protected void _strictCheckCall() throws IllegalStateException {
    if (!isAlive()) {
      throw new IllegalStateException("Component '" + getClass().getName() + "' is not alive!");
    }
  }

  /**
   * Returns the children of this component.
   */
  protected Map<Object, Component> _getChildren() {
    synchronized (this) {
      if (this.children == null) {
        this.children = Collections.synchronizedMap(new LinkedHashMap<Object, Component>(1));
      }
    }
    return this.children;
  }

  /**
   * Returns the children of this component.
   */
  protected Map<Object, Component> _getDisabledChildren() {
    synchronized (this) {
      if (this.disabledChildren == null) {
        this.disabledChildren = Collections.synchronizedMap(new LinkedHashMap<Object, Component>(1));
      }
    }
    return this.disabledChildren;
  }

  /**
   * Adds a child component to this component with the key and initilizes it with the specified Environment env.
   */
  protected void _addComponent(Object key, Component component, Environment env) {
    _addComponent(key, component, new StandardScope(key, getScope()), env);
  }

  /**
   * Adds a child component to this component with the key and initilizes it with the specified Environment env.
   * 
   * @since 1.1
   */
  protected void _addComponent(Object key, Component component, Scope scope, Environment env) {

    Assert.notNull(this, component, "The component for key '" + key + "' is required.");
    Assert.notNull(this, key, "Cannot add a component of class '" + component.getClass() + "' under a null key!");

    // Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    _checkCall();

    // cannot add a child with key that clashes with a disabled child's key
    if (_getChildren().containsKey(key)) {
      if (_getDisabledChildren().containsKey(key)) {
        _enableComponent(key);
      }
      _removeComponent(key);
    }

    // Sequence is very important as by the time of init, the component should be in place, since during init the
    // component might be overridden.
    _getChildren().put(key, component);
    component._getComponent().init(scope, env);
  }

  /**
   * Removes the child component with the specified key from the children and calls destroy on it.
   * 
   * @param key The ID of the component to remove.
   */
  protected void _removeComponent(Object key) {
    Assert.notNullParam(this, key, "key");

    // Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    _checkCall();

    Component comp = _getChildren().get(key);
    if (comp == null) {
      return;
    }

    // Sequence is very important, and guarantees that there won't
    // be a second destroy call if the first one doesn't succeed.
    _getChildren().remove(key);
    comp._getComponent().destroy();
  }

  /**
   * Disables the child component with the specified key. A disabled child is a child that is removed from the standard
   * set of children
   * 
   * @param key The ID of the child component to disable.
   */
  protected void _disableComponent(Object key) {
    Assert.notNullParam(this, key, "key");

    // Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    _checkCall();

    boolean enabled = _getChildren().containsKey(key);
    boolean disabled = _getDisabledChildren().containsKey(key);

    if (!enabled && !disabled) {
      throw new NoSuchComponentException(key);
    }

    if (enabled) {
      _getChildren().get(key)._getComponent().disable();
      _getDisabledChildren().put(key, _getChildren().remove(key));
    }
  }

  /**
   * Enables a disabled child component with the specified key. If the key does not correspond to anything, a runtime
   * exception will occur.
   * 
   * @param key The ID of the child component to enable.
   */
  protected void _enableComponent(Object key) {
    Assert.notNullParam(this, key, "key");

    // Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    _checkCall();

    boolean enabled = _getChildren().containsKey(key);
    boolean disabled = _getDisabledChildren().containsKey(key);

    if (!disabled && !enabled) {
      throw new NoSuchComponentException(key);
    }

    if (disabled) {
      _getChildren().put(key, _getDisabledChildren().remove(key));
      _getChildren().get(key)._getComponent().enable();
    }
  }

  /**
   * Relocates parent's child with keyFrom to this BaseComponent with a new key keyTo. The child will get the
   * Environment specified by newEnv.
   * 
   * @param parent is the current parent of the child to be relocated.
   * @param newEnv the new Environment of the child.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  protected void _relocateComponent(Composite parent, Environment newEnv, Object keyFrom, Object keyTo) {
    Assert.notNull(this, parent, "Cannot relocate a component from under key '" + keyFrom + "' to key '" + keyTo
        + "' from a null parent!");

    Assert.notNull(this, parent._getComposite().getChildren().get(keyFrom),
        "The component to be relocated must be a child under key '" + keyFrom + "'!");

    Assert.isTrue(this, parent._getComposite().getChildren().get(keyFrom) instanceof Relocatable,
        "The component of class '" + parent._getComposite().getChildren().get(keyFrom).getClass()
            + "' to be relocated from under key '" + keyFrom + "' to key '" + keyTo + "' must implement Relocatable!");

    // Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, keyFrom, "keyFrom");
    Assert.isInstanceOfParam(String.class, keyTo, "keyTo");

    Relocatable comp = (Relocatable) parent._getComposite().detach(keyFrom);
    comp._getRelocatable().overrideEnvironment(newEnv);
    _getChildren().put(keyTo, comp);
  }

  /**
   * Forwards the <code>message</code> to the component and to all of its children components.
   * <p>
   * <b>Note:</b> this is the component propagation logic that should not be modified by applications (they should use
   * just the {@link BaseComponent#propagate(Message)}).
   * 
   * @param message A message to forward.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void _propagate(Message message) {
    Assert.notNullParam(this, message, "message");
    if (this.children == null || isDead()) {
      return;
    }

    Map<Object, Component> children = _getChildren();

    synchronized (children) {
      for (Map.Entry<Object, Component> entry : children.entrySet()) {
        // message has not destroyed by previously existing child
        if (entry.getValue() != null) {
          message.send(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  // *******************************************************************
  // PRIVATE METHODS
  // *******************************************************************

  private void incCallCount() {
    if (this.reentrantTLS == null) {
      this.reentrantTLS = new ThreadLocal<Counter>();
    }
    if (this.reentrantTLS.get() == null) {
      this.reentrantTLS.set(new Counter());
    }
    this.callCount++;
    this.reentrantTLS.get().counter++;
    if (getReentrantCount() > 1) {
      this.reentrantCallCount++;
    }
  }

  private void decCallCount() {
    if (getReentrantCount() > 1) {
      this.reentrantCallCount--;
    }
    this.callCount--;
    this.reentrantTLS.get().counter--;
  }

  private int getReentrantCount() {
    return (this.reentrantTLS == null || this.reentrantTLS.get() == null) ? 0 : this.reentrantTLS.get().counter;
  }

  // *******************************************************************
  // PROTECTED CLASSES
  // *******************************************************************

  // component implementation
  protected class ComponentImpl implements Component.Interface {

    public synchronized void init(Scope scope, Environment env) {
      Assert.notNull(this, env, "Environment cannot be null!");
      Assert.isTrue(this, !isInitialized(), "Cannot initialize the component more than once!");

      BaseComponent.this._setScope(scope);
      BaseComponent.this._setEnvironment(env);
      BaseComponent.this.state = ALIVE;

      EnvironmentUtil.injectEnvironmentEntries(env, BaseComponent.this);

      try {
        BaseComponent.this.init();
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
    }

    public void destroy() {
      _startWaitingCall();
      _strictCheckCall();
      try {
        _waitNoCall();

        _getChildren();
        synchronized (children) {
          for (Iterator<Map.Entry<Object, Component>> i = children.entrySet().iterator(); i.hasNext();) {
            i.next().getValue()._getComponent().destroy();
            i.remove();
          }
        }

        _getDisabledChildren();
        synchronized (disabledChildren) {
          for (Iterator<Map.Entry<Object, Component>> i = disabledChildren.entrySet().iterator(); i.hasNext();) {
            Component component = i.next().getValue();
            if (component != null) {
              component._getComponent().destroy();
              i.remove();
            }
          }
        }

        BaseComponent.this.destroy();

        BaseComponent.this.state = DEAD;
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      } finally {
        _endWaitingCall();
      }
    }

    public void propagate(Message message) {
      _startCall();
      try {
        BaseComponent.this.propagate(message);
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      } finally {
        _endCall();
      }
    }

    public void enable() {
      _startCall();
      try {
        BaseComponent.this.enable();
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      } finally {
        _endCall();
      }
    }

    public void disable() {
      _startCall();
      try {
        BaseComponent.this.disable();
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      } finally {
        _endCall();
      }
    }
  }

  private static class Counter {

    /**
     * The value of the counter
     */
    public int counter;
  }
}
