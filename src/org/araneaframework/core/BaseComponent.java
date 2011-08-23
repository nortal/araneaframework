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
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.exception.NoSuchComponentException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * The base class for all Aranea components. Base entities do not make a Composite pattern and only provide some very
 * basic services (mainly synchronization and messaging service)
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class BaseComponent implements Component {

  private static final Log LOG = LogFactory.getLog(BaseComponent.class);

  private static final byte UNBORN = 1;

  private static final byte ALIVE = 2;

  private static final byte DEAD = 3;

  private Environment environment;

  private Scope scope;

  private Map<String, Component> children;

  private Map<String, Component> disabledChildren;

  private byte state = UNBORN;

  private transient int callCount = 0;

  private transient int reentrantCallCount = 0;

  private transient ThreadLocal<Integer> reentrantTLS;

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
   * Component initiation callback. Gets called when the component is initialized.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void init() throws Exception {
  }

  /**
   * Component destruction callback. Gets called when the component is destroyed.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void destroy() throws Exception {
  }

  /**
   * Disables the component.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void disable() throws Exception {
  }

  /**
   * Enables the component.
   * 
   * @throws Exception Any runtime exception that may occur.
   */
  protected void enable() throws Exception {
  }

  /**
   * Forwards the <code>message</code> to the component and to all of its children components.
   * 
   * @param message A message to forward.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void propagate(Message message) throws Exception {
  }

  /**
   * Handles any given exception. Default implementation just re-throws the exception.
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
   * Informs whether the component has been initialized. "Initialized" means that the <code>init()</code> method has
   * been called.
   * 
   * @return A Boolean that is <code>true</code>, if the component has been initialized.
   */
  public final boolean isInitialized() {
    return this.state != UNBORN;
  }

  public final boolean isAlive() {
    return this.state == ALIVE;
  }

  /**
   * Informs whether the component is dead. It means that the <code>destroy()</code> method has been called.
   * 
   * @return A Boolean that is <code>true</code>, if the component has been destroyed.
   */
  public final boolean isDead() {
    return this.state == DEAD;
  }

  /**
   * Sets the environment of this component to <code>env</code>.
   * 
   * @param env The new <code>Environment</code> for this component.
   */
  protected synchronized void _setEnvironment(Environment env) {
    this.environment = env;
  }

  /**
   * Sets the scope for this component.
   * 
   * @param scope The new scope to identify this component.
   * @since 1.1
   */
  protected void _setScope(Scope scope) {
    this.scope = scope;
  }

  /**
   * Waits until no call is being made to the component. Used to synchronize calls to the component.
   * <p>
   * When a call to the component is in progress, default implementation waits 1 second before trying again. It waits up
   * to 10 seconds in total before giving up.
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
          LOG.error("Deadlock or starvation not solved in 10s, call count '" + this.callCount
              + "', reentrant call count '" + this.reentrantCallCount + "'", new AraneaRuntimeException(
              "Deadlock or starvation suspected!"));
          return;
        }
      }
    }
  }

  /**
   * Checks if a call is valid (component has been initialized) and increments the call count (registers a call being
   * made to this component). Use {@link #_strictStartCall()}, if you also need to be sure that component is not
   * destroyed.
   * 
   * @see #_strictStartCall()
   * @see #_checkCall()
   */
  protected synchronized void _startCall() throws IllegalStateException {
    _checkCall();
    incCallCount();
  }

  /**
   * Checks if a call is valid (component has been initialized and is not destroyed) and increments the call count
   * (registers a call being made to this component).
   * 
   * @see #_startCall()
   * @see #_strictCheckCall()
   * @since 1.1
   */
  protected synchronized void _strictStartCall() throws IllegalStateException {
    _strictCheckCall();
    incCallCount();
  }

  /**
   * Decrements the call count (unregisters a call that was made to this component). Wakes up all threads that are
   * waiting on this object's monitor.
   */
  protected synchronized void _endCall() {
    decCallCount();
    notifyAll();
  }

  /**
   * Used for starting a call that is a re-entrant call. Meaning a call of this component is doing the calling.
   */
  protected synchronized void _startWaitingCall() {
    if (getReentrantCount() >= 1) {
      this.reentrantCallCount++;
    }
  }

  /**
   * Decrements internal call counter.
   */
  protected synchronized void _endWaitingCall() {
    if (getReentrantCount() >= 1) {
      this.reentrantCallCount--;
    }
  }

  /**
   * Checks if this component has been initialized. If not, throws <tt>IllegalStateException</tt>. This is relatively
   * loose check, allowing leftover calls to dead components.
   * 
   * @throws IllegalStateException when this component has never been initialized.
   */
  protected void _checkCall() throws IllegalStateException {
    if (!isInitialized()) {
      throw new IllegalStateException("Component '" + getClass().getName() + "' was never initialized!");
    }
  }

  /**
   * Checks if this component is currently alive (initialized and not destroyed).
   * 
   * @throws IllegalStateException when this component is not initialized or is destroyed.
   * @since 1.1
   */
  protected void _strictCheckCall() throws IllegalStateException {
    if (!isAlive()) {
      throw new IllegalStateException("Component '" + getClass().getName() + "' is not alive!");
    }
  }

  /**
   * Returns all enabled child components of this component. Note that the returned map is synchronized.
   * 
   * @return A map containing enabled child components of this component by their IDs. An empty map, when no child
   *         exists.
   */
  protected Map<String, Component> _getChildren() {
    synchronized (this) {
      if (this.children == null) {
        this.children = Collections.synchronizedMap(new LinkedHashMap<String, Component>(1));
      }
    }
    return this.children;
  }

  /**
   * Returns all disabled child components of this component. Note that the returned map is synchronized.
   * 
   * @return A map containing disabled child components of this component by their IDs. An empty map, when no disabled
   *         child exists.
   */
  protected Map<String, Component> _getDisabledChildren() {
    synchronized (this) {
      if (this.disabledChildren == null) {
        this.disabledChildren = Collections.synchronizedMap(new LinkedHashMap<String, Component>(1));
      }
    }
    return this.disabledChildren;
  }

  /**
   * Adds a child component to this component with the given ID (key) and initializes it with the specified environment.
   * Note that there are some aspects about adding a component:
   * <ul>
   * <li>component to be added must not be initialized, therefore no component cannot be added twice;
   * <li>when component key is already used, the previous component will be (enabled), removed and destroyed;
   * <li>component to be added will also get its own scope: &lt;the scope of this component&gt;.&lt;key&gt;;
   * <li>the provided environment does not necessarily have to contain parent component environment items.
   * </ul>
   * Input parameters will be validated and a runtime exception will be thrown, if they are not as required.
   * 
   * @param key A not empty string for component ID, may be already used for another child component (required).
   * @param component Uninitialized component to be added to this component as a child (required).
   * @param env The environment assigned to the component during initialization (required).
   */
  protected void _addComponent(String key, Component component, Environment env) {
    _addComponent(key, component, new StandardScope(key, getScope()), env);
  }

  /**
   * Adds a child component to this component with the given ID (key) and initializes it with the specified environment.
   * Note that there are some aspects about adding a component:
   * <ul>
   * <li>component to be added must not be initialized, therefore no component cannot be added twice;
   * <li>when component key is already used, the previous component will be (enabled), removed and destroyed;
   * <li>component to be added will also get its own scope: &lt;the provided scope&gt;.&lt;key&gt;;
   * <li>the provided environment does not necessarily have to contain parent component environment items.
   * </ul>
   * Input parameters will be validated and a runtime exception will be thrown, if they are not as required.
   * 
   * @param key A not empty string for component ID, may be already used for another child component (required).
   * @param component Uninitialized component to be added to this component as a child (required).
   * @param scope The parent scope for the component to be added (may be <code>null</code>).
   * @param env The environment assigned to the component during initialization (required).
   * @since 1.1
   */
  protected void _addComponent(String key, Component component, Scope scope, Environment env) {
    Assert.notNull(this, component, "The component to be added under key '" + key + "' must not be null.");
    Assert.notEmpty(key, "Cannot add a component of class '" + component.getClass() + "' under an empty key!");

    _checkCall();

    // cannot add a child with key that clashes with a disabled child's key
    if (_getChildren().containsKey(key)) {
      if (_getDisabledChildren().containsKey(key)) {
        _enableComponent(key);
      }
      _removeComponent(key);
    }

    if (scope == null) {
      scope = new StandardScope(key, null);
    }

    // Sequence is very important as by the time of initialization, the component should be in place, since during
    // initialization the component might be overridden.
    _getChildren().put(key, component);
    component._getComponent().init(scope, env);
  }

  /**
   * Removes the child component with the specified key from the children and calls destroy on it. When no child
   * component with given key is found, nothing will happen.
   * 
   * @param key The ID of the component to remove (required).
   */
  protected void _removeComponent(Object key) {
    Assert.notNullParam(this, key, "key");

    _checkCall();

    Component comp = _getChildren().get(key);
    if (comp == null) {
      return;
    }

    // Sequence is very important, and guarantees that there won't be a second destroy call if the first one doesn't
    // succeed.
    _getChildren().remove(key);
    comp._getComponent().destroy();
  }

  /**
   * Disables the child component with the specified key. If the key does not correspond to any kind of child component,
   * a runtime exception will occur. A disabled child is a child that is removed from the active set of children to
   * disabled set of children. When child component is already disabled, nothing will happen.
   * 
   * @param key The ID of the child component to disable (required).
   * @see #_getChildren()
   * @see #_getDisabledChildren()
   */
  protected void _disableComponent(String key) {
    Assert.notNullParam(this, key, "key");

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
   * Enables a disabled child component with the specified key. If the key does not correspond to any kind of child
   * component, a runtime exception will occur. When child component is already disabled, nothing will happen.
   * 
   * @param key The ID of the child component to enable (required).
   * @see #_getChildren()
   * @see #_getDisabledChildren()
   */
  protected void _enableComponent(String key) {
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
   * Relocates a component with ID <tt>keyFrom</tt> under given <tt>parent</tt> as child of this component with a new
   * key <tt>keyTo</tt>. The child will get (possibly a new) <tt>Environment</tt> specified by <tt>newEnv</tt>.
   * <p>
   * A runtime exception will occur when
   * <ul>
   * <li>no such child component exists under given parent;
   * <li>the child component is not {@link Relocatable}
   * </ul>
   * 
   * @param parent The current parent of the child component to be relocated (required).
   * @param newEnv The new environment for the relocated child component.
   * @param keyFrom The current key of the child to be relocated (required).
   * @param keyTo The new key with which the child will be added as a child of component (required).
   */
  protected void _relocateComponent(Composite parent, Environment newEnv, String keyFrom, String keyTo) {
    Assert.notNullParam(this, parent, "parent");
    Assert.notNullParam(this, keyFrom, "keyFrom");
    Assert.notNullParam(this, keyTo, "keyTo");

    Component child = parent._getComposite().getChildren().get(keyFrom);

    Assert.notNull(this, child, "The component to be relocated must be a child under key '" + keyFrom + "'!");
    Assert.isTrue(this, child instanceof Relocatable, "The component of class '" + child.getClass()
        + "' to be relocated from under key '" + keyFrom + "' to key '" + keyTo + "' must implement Relocatable!");

    Relocatable comp = (Relocatable) parent._getComposite().detach(keyFrom);
    comp._getRelocatable().overrideEnvironment(newEnv);
    _getChildren().put(keyTo, comp);
  }

  /**
   * Forwards the <code>message</code> to this component and to all of its child components.
   * <p>
   * <b>Note:</b> this is the component propagation logic that should not be modified by applications (they should use
   * just the {@link #propagate(Message)} method for that).
   * <p>
   * When this component has no children or is destroyed, nothing will happen.
   * 
   * @param message A message to forward (required).
   */
  protected void _propagate(Message message) {
    Assert.notNullParam(this, message, "message");
    if (this.children == null || isDead()) {
      return;
    }

    Map<String, Component> children = _getChildren();

    synchronized (children) {
      for (Map.Entry<String, Component> entry : children.entrySet()) {
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

  private void initReentrantCount() {
    if (this.reentrantTLS == null) {
      this.reentrantTLS = new Counter();
    }
  }

  private int getReentrantCount() {
    return this.reentrantTLS != null ? this.reentrantTLS.get() : 0;
  }

  private synchronized void incCallCount() {
    initReentrantCount();
    this.callCount++;
    this.reentrantTLS.set(this.reentrantTLS.get() + 1);
    if (getReentrantCount() > 1) {
      this.reentrantCallCount++;
    }
  }

  private synchronized void decCallCount() {
    if (getReentrantCount() > 1) {
      this.reentrantCallCount--;
    }
    this.reentrantTLS.set(this.reentrantTLS.get() - 1);
    this.callCount--;
  }

  // *******************************************************************
  // PROTECTED CLASSES
  // *******************************************************************

  /**
   * Base component implementation that uses synchronization to perform component actions to make sure no one else uses
   * it at the same time.
   */
  protected class ComponentImpl implements Component.Interface {

    /**
     * Before {@link BaseComponent#init()} method is called, environment entries will injected into component fields.
     * 
     * @see EnvironmentUtil#injectEnvironmentEntries(Environment, Object)
     */
    public synchronized void init(Scope scope, Environment env) {
      Assert.notNull(this, env, "Environment cannot be null!");
      Assert.isTrue(this, !isInitialized(), "Cannot initialize the component more than once!");

      _setScope(scope);
      _setEnvironment(env);
      EnvironmentUtil.injectEnvironmentEntries(env, BaseComponent.this);
      BaseComponent.this.state = ALIVE;

      try {
        BaseComponent.this.init();
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
    }

    /**
     * Destroys all child components and then invokes the {@link BaseComponent#destroy()} method. The component state
     * will end up dead and cannot be reused.
     */
    public void destroy() {
      _startWaitingCall();
      _strictCheckCall();
      try {
        _waitNoCall();

        _getChildren();
        synchronized (BaseComponent.this.children) {
          for (Iterator<Component> i = BaseComponent.this.children.values().iterator(); i.hasNext();) {
            try {
              i.next()._getComponent().destroy();
            } catch (Exception e) {
              LOG.warn("An exception was caught from (enabled) component destruction process", e);
            }
            i.remove();
          }
        }

        _getDisabledChildren();
        synchronized (BaseComponent.this.disabledChildren) {
          for (Iterator<Component> i = BaseComponent.this.disabledChildren.values().iterator(); i.hasNext();) {
            try {
              i.next()._getComponent().destroy();
            } catch (Exception e) {
              LOG.warn("An exception was caught from (disabled) component destruction process", e);
            }
            i.remove();
          }
        }

        BaseComponent.this.destroy();

      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      } finally {
        BaseComponent.this.state = DEAD;
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

  /**
   * A thread local counter used for keeping track of calls to this component.
   */
  private static class Counter extends ThreadLocal<Integer> {

    /**
     * Initial value is zero.
     */
    @Override
    protected Integer initialValue() {
      return 0;
    }
  }
}
