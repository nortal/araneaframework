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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.Relocatable;
import org.araneaframework.Scope;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * The base class for all Aranea components. Base entities do not make a Composite pattern 
 * and only provide some very basic services (mainly syncronization and messaging service)
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class BaseComponent implements Component {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private static final Log log = LogFactory.getLog(BaseComponent.class);
  
  private Environment environment;
  private Scope scope;
  private Map children;
  private Map disabledChildren;
  
  private static final byte UNBORN = 1;
  private static final byte ALIVE = 2;
  private static final byte DEAD = 3;
  
  private byte state = UNBORN;
  
  private transient int callCount = 0;
  private transient int reentrantCallCount = 0;
  private transient ThreadLocal reentrantTLS;

  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }
      
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************
  /**
   * Init callback. Gets called when the component is initilized.
   */
  protected void init() throws Exception {
  }

  /**
   * Destroy callback. Gets called when the component is destroyed.
   */
  protected void destroy() throws Exception {
  }
  
  protected void disable() throws Exception {    
  }

  protected void enable() throws Exception {    
  }  
  
  protected void propagate(Message message) throws Exception {    
  }
  
  protected void handleException(Exception e) throws Exception {
    throw e;
  }
  
  /**
   * Returns the Environment of this BaseComponent.
   */
  public Environment getEnvironment() {
    return environment;
  }
  
  public Scope getScope() {
    return scope;
  }
  
  /**
   * Returns true, if the BaseComponent has been initialized. 
   */
  public boolean isInitialized() {
    return state != UNBORN;
  } 
  
  public boolean isAlive() {
    return state == ALIVE;
  }
  
  public boolean isDead() {
    return state == DEAD;
  }
  
  /**
   * Sets the environment of this BaseComponent to environment. 
   */
  protected synchronized void _setEnvironment(Environment env) {
    this.environment = env;
  }
  
  /**
   * Sets the environment of this BaseComponent to environment. 
   * 
   * @since 1.1
   */
  protected void _setScope(Scope scope) {
    this.scope = scope;
  }

  /**
   * Waits until no call is made to the component. Used to synchronize calls
   * to the component.
   */
  protected synchronized void _waitNoCall() throws InterruptedException {    
    long waitStart = System.currentTimeMillis();
    
    while (callCount != reentrantCallCount) { 
      this.wait(1000);
      
      if (callCount != reentrantCallCount) {
        log.warn("Deadlock or starvation suspected, call count '" + callCount + "', reentrant call count '" + reentrantCallCount + "'");
        
        if (waitStart < System.currentTimeMillis() - 10000) {
          log.error("Deadlock or starvation not solved in 10s, call count '" + 
              callCount + "', reentrant call count '" + reentrantCallCount + "'", 
              new AraneaRuntimeException("Deadlock or starvation suspected!"));
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
   * Decrements the call count. Wakes up all threads that are waiting on
   * this object's monitor
   */
  protected synchronized void _endCall() {
    decCallCount();
    this.notifyAll();
  }
  
  /**
   * Used for starting a call that is a re-entrant call. Meaning a call of this component
   * is doing the calling
   */
  protected synchronized void _startWaitingCall() {
    if (getReentrantCount() >= 1)
      reentrantCallCount++;
  }
  
  /**
   * Decrements internal callcount counter.
   */
  protected synchronized void _endWaitingCall() {    
    if (getReentrantCount() >= 1)
      reentrantCallCount--;
  }

  /**
   * Checks if this component was initialized. If not, throws IllegalStateException.
   * This is relatively loose check, allowing leftover calls to dead components.
   * @throws IllegalStateException when component has never been initialized
   */
  protected void _checkCall() throws IllegalStateException {
    if (!isInitialized()) {
      throw new IllegalStateException("Component '" + getClass().getName() + "' was never initialized!");
    }
  }
  
  /**
   * Checks if this component is currently alive. 
   * This is strict check that disallows leftover calls to dead components.
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
  protected Map _getChildren() {
    synchronized (this) {
      if (children == null)
        children = Collections.synchronizedMap(new LinkedMap(1));
    }
    return children;
  }
  
  /**
   * Returns the children of this component.
   */
  protected Map _getDisabledChildren() {
    synchronized (this) {
      if (disabledChildren == null)
        disabledChildren = Collections.synchronizedMap(new LinkedMap(1));
    }
    return disabledChildren;
  }
  
  /**
   * Adds a child component to this component with the key and initilizes it with the
   * specified Environment env. 
   */
  protected void _addComponent(Object key, Component component, Environment env){
    _addComponent(key, component, new StandardScope(key, getScope()), env);
  }
  
  /**
   * Adds a child component to this component with the key and initilizes it with the
   * specified Environment env. 
   * 
   * @since 1.1
   */
  protected void _addComponent(Object key, Component component, Scope scope, Environment env){
    Assert.notNull(this, key, 
        "Cannot add a component of class '" + (component == null ? null : component.getClass())+ "' under a null key!");
    //Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");

    _checkCall();
    
    // cannot add a child with key that clashes with a disabled child's key
    if (_getChildren().containsKey(key)) {
      if (_getDisabledChildren().containsKey(key))
        _enableComponent(key);
      _removeComponent(key);
    }
    
    // Sequence is very important as by the time of init 
    // component should be in place since during init the 
    // component might be overridden.
    _getChildren().put(key, component);
    component._getComponent().init(scope, env);
  }  
  
  /**
   * Removes the childcomponent with the specified key from the children and calls destroy on it.
   */
  protected void _removeComponent(Object key) {
    Assert.notNullParam(this, key, "key");
    //Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    
    _checkCall();
    
    Component comp = (Component)_getChildren().get(key);
    
    if (comp == null) {
      return;
    }

    //Sequence is very important, and guarantees that there won't 
    //be a second destroy call if the first one doesn't succeed.
    _getChildren().remove(key);
    comp._getComponent().destroy();
  }

  /**
   * Disables the child component with the specified key. A disabled child is a child that is removed
   * from the standard set of children 
   */
  protected void _disableComponent(Object key) {
    Assert.notNullParam(this, key, "key");
    //Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    
    _checkCall();
    
    if (!_getChildren().containsKey(key)) {
      throw new NoSuchComponentException(key);
    }
    
    ((Component) _getChildren().get(key))._getComponent().disable();
    _getDisabledChildren().put(key, _getChildren().remove(key));    
  }
  
  /**
   * Enables a disabled child component with the specified key.
   * @param key
   */
  protected void _enableComponent(Object key) {
    Assert.notNullParam(this, key, "key");
    //Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, key, "key");
    
    _checkCall();
    
    if (!_getDisabledChildren().containsKey(key)) {
      throw new NoSuchComponentException(key);
    }
        
    _getChildren().put(key, _getDisabledChildren().remove(key));
    ((Component) _getChildren().get(key))._getComponent().enable();
  }
  
  /**
   * Relocates parent's child with keyFrom to this BaseComponent with a new key keyTo. The child
   * will get the Environment specified by newEnv.  
   * @param parent is the current parent of the child to be relocated.
   * @param newEnv the new Environment of the child.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  protected void _relocateComponent(Composite parent, Environment newEnv, Object keyFrom, Object keyTo) {
    Assert.notNull(this, parent, "Cannot relocate a component from under key '" + keyFrom + "' to key '" + keyTo + "' from a null parent!");
    Assert.notNull(this, parent._getComposite().getChildren().get(keyFrom), "The component to be relocated must be a child under key '" + keyFrom + "'!");
    Assert.isTrue(this, parent._getComposite().getChildren().get(keyFrom) instanceof Relocatable, 
        "The component of class '" + parent._getComposite().getChildren().get(keyFrom).getClass() + "' to be relocated from under key '" + keyFrom + 
        "' to key '" + keyTo + "' must implement Relocatable!");
    //Only Strings are supported by this implementation
    Assert.isInstanceOfParam(String.class, keyFrom, "keyFrom");
    Assert.isInstanceOfParam(String.class, keyTo, "keyTo");
    
    Relocatable comp = (Relocatable) parent._getComposite().detach(keyFrom);
    comp._getRelocatable().overrideEnvironment(newEnv);
    
    _getChildren().put(keyTo, comp);
  }
  
  protected void _propagate(Message message) {
    Assert.notNullParam(this, message, "message");
    
    if (children == null || isDead())
      return;
    
    Iterator ite = (new HashMap(_getChildren())).keySet().iterator();
    while(ite.hasNext()) {
      Object key = ite.next();

      Component component = (Component)_getChildren().get(key);

      // message has not destroyed previously existing child
      if (component != null ) {
    	message.send(key, component);  
      }
    }
  }
  

  //*******************************************************************
  // PRIVATE METHODS
  //*******************************************************************
  
  private void incCallCount() {
    if (reentrantTLS == null)
      reentrantTLS = new ThreadLocal();
    
    if (reentrantTLS.get() == null)
      reentrantTLS.set(new Counter(0));
    
    callCount++;
    ((Counter) reentrantTLS.get()).counter++;
    
    if (getReentrantCount() > 1)
      reentrantCallCount++;
  }
   
  private void decCallCount() {
    if (getReentrantCount() > 1)
      reentrantCallCount--;
    
    callCount--;
    ((Counter) reentrantTLS.get()).counter--;
  }
  
  private int getReentrantCount() {     
    return (reentrantTLS == null || reentrantTLS.get() == null) ? 0 : ((Counter) reentrantTLS.get()).counter;
  }
  
  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
  //component implementation
  protected class ComponentImpl implements Component.Interface {
    
    public synchronized void init(Scope scope, Environment env) {
      Assert.notNull(this, env, "Environment cannot be null!");
      Assert.isTrue(this, !isInitialized(), "Cannot initialize the component more than once!");

      BaseComponent.this._setScope(scope);
      BaseComponent.this._setEnvironment(env);
      state = ALIVE;
      try {
        BaseComponent.this.init();
      }
      catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
    }
    
    public void destroy(){     
      _startWaitingCall();
      _strictCheckCall();

      try {
        /* XXX synch logic a bit weird. 
         * Second call to destroy should fail, not wait. */
        _waitNoCall();
        synchronized (this) {
          if (children != null)
            for (Iterator i = new HashMap(_getChildren()).keySet().iterator(); i.hasNext(); ) {
              _removeComponent(i.next());
            }
          
          if (disabledChildren != null)
            for (Iterator i =_getDisabledChildren().keySet().iterator(); i.hasNext(); ) {
              Component comp = (Component) _getDisabledChildren().get(i.next());
              if (comp != null) {          
                comp._getComponent().destroy();             
              }
            }    
          
          BaseComponent.this.destroy();
        }
        state = DEAD;
      }
      catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
      finally {
        _endWaitingCall();
      }
    }
    
    public void propagate(Message message) {
      _startCall();
      try {      
        BaseComponent.this.propagate(message);
      }
      catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }      
      finally {
        _endCall();
      }
    }

    public void enable() {
      _startCall();
      try {
        BaseComponent.this.enable();
      }
      catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }      
      finally {
        _endCall();
      }
    }

    public void disable() {
      _startCall();
      try {
        BaseComponent.this.disable();
      }
      catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }      
      finally {
        _endCall();
      }
    }
  }
  
  //*******************************************************************
  // PRIVATE CLASSES
  //*******************************************************************
  private static class Counter {
    /**
     * The value of the counter
     */
    public int counter; 
    
    /**
     * Initializes the counter with a value.
     * @param counter value of the counter
     */
    public Counter(int counter) {
      this.counter = counter;
    }
  }
}