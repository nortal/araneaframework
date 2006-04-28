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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.log4j.Logger;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.Relocatable;

/**
 * The base class for all Aranea components. Base entities do not make a Composite pattern 
 * and only provide some very basic services (mainly syncronization and messaging service)
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public abstract class BaseComponent implements Component {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private static final Logger log = Logger.getLogger(BaseComponent.class);
  
  private Environment environment;
  private Map children = Collections.synchronizedMap(new LinkedMap());
  private Map disabledChildren = Collections.synchronizedMap(new LinkedMap());
  
  private boolean wasInited = false;
  
  private transient int callCount = 0;
  private transient int reentrantCallCount = 0;
  private transient ThreadLocal reentrantTLS;
  
  private Collection destroyers = new ArrayList();

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
  protected Environment getEnvironment() {
    return environment;
  }
  
  /**
   * Returns true, if the BaseComponent has been initialized. 
   */
  protected boolean isInitialized() {
    return wasInited;
  } 
  
  /**
   * Sets the environment of this BaseComponent to environment. 
   */
  protected void _setEnvironment(Environment env) {
    this.environment = env;
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
   * Checks if this component is initialized. If not, throws IllegalStateException.
   */
  protected void _checkCall() throws IllegalStateException {
    if (!wasInited) {
      throw new IllegalStateException("Component '" + getClass().getName() + "' has not been initialized!");
    }
  }
  
  /**
   * Returns the children of this component.
   */
  protected Map _getChildren() {
    return children;
  }
  
  /**
   * Adds a child component to this component with the key and initilizes it with the
   * specified Environment env. 
   */
  protected void _addComponent(Object key, Component component, Environment env) throws Exception {
    _checkCall();
    
    // cannot add a child with key that clashes with a disabled child's key
    if (children.containsKey(key)) {
      if (disabledChildren.containsKey(key))
        _enableComponent(key);
      _removeComponent(key);
    }
    
    // Sequence is very important as by the time of init 
    // component should be in place since during init the 
    // component might be overridden.
    children.put(key, component);
    component._getComponent().init(env);
  }
  
  /**
   * Removes the childcomponent with the specified key from the children and calls destroy on it.
   */
  protected void _removeComponent(Object key) throws Exception {
    _checkCall();
    
    Component comp = (Component)children.get(key);
    
    if (comp == null) {
      return;
    }

    //Sequence is very important, and guarantees that there won't 
    //be a second destroy call if the first one doesn't succeed.
    children.remove(key);
    comp._getComponent().destroy();
  }

  /**
   * Disables the child component with the specified key. A disabled child is a child that is removed
   * from the standard set of children 
   */
  protected void _disableComponent(Object key) throws Exception {
    _checkCall();
    
    if (!children.containsKey(key)) {
      throw new NoSuchComponentException(key);
    }
    
    ((Component) children.get(key))._getComponent().disable();
    disabledChildren.put(key, children.remove(key));    
  }
  
  /**
   * Enables a disabled child component with the specified key.
   * @param key
   * @throws Exception
   */
  protected void _enableComponent(Object key) throws Exception {
    _checkCall();
    
    if (!disabledChildren.containsKey(key)) {
      throw new NoSuchComponentException(key);
    }
        
    children.put(key, disabledChildren.remove(key));
    ((Component) children.get(key))._getComponent().enable();
  }
  
  /**
   * Relocates parent's child with keyFrom to this BaseComponent with a new key keyTo. The child
   * will get the Environment specified by newEnv.  
   * @param parent is the current parent of the child to be relocated.
   * @param newEnv the new Environment of the child.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  protected void _relocateComponent(Composite parent, Environment newEnv, Object keyFrom, Object keyTo) throws Exception {
    if (!(parent._getComposite().getChildren().get(keyFrom) instanceof Relocatable)) {
      throw new AraneaRuntimeException("Child with key '"+keyFrom+"' of class '" + parent._getComposite().getChildren().get(keyFrom).getClass() + "' is not Relocatable");
    }
    
    Relocatable comp = (Relocatable) parent._getComposite().detach(keyFrom);
    comp._getRelocatable().overrideEnvironment(newEnv);
    
    children.put(keyTo, comp);
  }
  
  protected void _propagate(Message message) throws Exception {
    Iterator ite = (new HashMap(_getChildren())).keySet().iterator();
    while(ite.hasNext()) {
      Object key = ite.next();
      Component component = (Component)_getChildren().get(key);
      
      if (component == null ) {
        throw new NoSuchComponentException(key);
      }      
      
      message.send(key, component);
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
    
    public synchronized void init(Environment env) throws Exception {
      if (env == null) {
        throw new AraneaRuntimeException("Environment cannot be null");
      }
      
      if (wasInited) {
        throw new AraneaRuntimeException("Cannot initialize a component more than once");
      }      
      BaseComponent.this._setEnvironment(env);
      wasInited = true;
      BaseComponent.this.init();
    }
    
    public void destroy() throws Exception {     
      _startWaitingCall();
      
      try {
        /* XXX synch logic a bit weird. 
         * Second call to destroy should fail, not wait. */
        _waitNoCall();
        synchronized (this) {
          for (Iterator i = new HashMap(_getChildren()).keySet().iterator(); i.hasNext(); ) {
            _removeComponent(i.next());
          }
          
          for (Iterator i = new HashMap(disabledChildren).keySet().iterator(); i.hasNext(); ) {
            Component comp = (Component) disabledChildren.get(i.next());
            if (comp != null) {          
              comp._getComponent().destroy();             
            }
          }    
          
          BaseComponent.this.destroy();
        }
        wasInited = false;   
      }
      finally {
        _endWaitingCall();
      }
    }
    
    public void propagate(Message message) throws Exception {
      _startCall();
      try {      
        BaseComponent.this.propagate(message);
      }
      finally {
        _endCall();
      }
    }

    public void enable() throws Exception {
      _startCall();
      try {
        BaseComponent.this.enable();
      }
      finally {
        _endCall();
      }
    }

    public void disable() throws Exception {
      _startCall();
      try {
        BaseComponent.this.disable();
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
