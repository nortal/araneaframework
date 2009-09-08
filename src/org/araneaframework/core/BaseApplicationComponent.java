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
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.Scope;
import org.araneaframework.Viewable;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A component with support for composite.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class BaseApplicationComponent extends BaseComponent
  implements ApplicationComponent {

  private static final long serialVersionUID = 1L;

  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************

  protected class ViewModel implements ApplicationComponent.ComponentViewModel {

    private static final long serialVersionUID = 1L;

    /**
     * @see ApplicationComponent.ComponentViewModel#getScope() 
     * @since 1.1
     */
    public Scope getScope() {
      return BaseApplicationComponent.this.getScope();
    }

    public Map getChildren() {
      return BaseApplicationComponent.this.getChildren();
    }
  }

  protected class ViewableImpl implements Viewable.Interface {

    private static final long serialVersionUID = 1L;

    public Object getViewModel() {
      try {
        return BaseApplicationComponent.this.getViewModel();
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  protected class CompositeComponentImpl implements Composite.Interface {

    private static final long serialVersionUID = 1L;

    public Map getChildren() {
      return BaseApplicationComponent.this.getChildren();
    }

    public void attach(Object key, Component comp) {
      _getChildren().put(key, comp);
    }

    public Component detach(Object key) {
      return (Component) _getChildren().remove(key);
    }
  }

  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************

  /**
   * Returns a unmodifiable map of all the child components under this
   * Component.
   * 
   * @return a map of child components
   */
  public Map getChildren() {
    return Collections.unmodifiableMap(new LinkedMap(_getChildren()));
  }

  public Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }

  public Composite.Interface _getComposite() {
    return new CompositeComponentImpl();
  }

  /**
   * Adds a component with the specified key. Allready initilized component
   * cannot be added. Duplicate keys not allowed. The child is initialized with
   * the Environment env.
   */
  public void addComponent(Object key, Component child, Environment env) {
    _addComponent(key, child, env);
  }

  /**
   * Adds a component with the specified key. Allready initilized components
   * cannot be added. Duplicate keys not allowed. The child is initialized with
   * the Environment from <code>getChildComponentEnvironment()</code>.
   * 
   */
  public void addComponent(Object key, Component child) {
    _addComponent(key, child, getChildComponentEnvironment());
  }

  /**
   * Relocates parent's child with keyFrom to this component with a new key
   * keyTo. The child will get the Environment specified by newEnv.
   * 
   * @param parent is the current parent of the child to be relocated.
   * @param newEnv the new Environment of the child.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this
   *            StandardService.
   */
  public void relocateComponent(Composite parent, Environment newEnv,
      Object keyFrom, Object keyTo) {
    _relocateComponent(parent, newEnv, keyFrom, keyTo);
  }

  /**
   * Relocates parent's child with keyFrom to this service with a new key keyTo.
   * The child will get the Environment of this StandardService.
   * 
   * @param parent is the current parent of the child to be relocated.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this
   *            StandardService.
   */
  public void relocateComponent(Composite parent, Object keyFrom, Object keyTo) {
    _relocateComponent(parent, getChildComponentEnvironment(), keyFrom, keyTo);
  }

  /**
   * Enables the component with the specified key. Only a disabled componet can
   * be enabled.
   */
  public void enableComponent(Object key) {
    _enableComponent(key);
  }

  /**
   * Disables the component with the specified key. Only a enabled component can
   * be disabled.
   */
  public void disableComponent(Object key) {
    _disableComponent(key);
  }

  /**
   * Removes the component with the specified key.
   */
  public void removeComponent(Object key) {
    _removeComponent(key);
  }

  protected void propagate(Message message) {
    _propagate(message);
  }

  public Environment getEnvironment() {
    return super.getEnvironment();
  }

  public Environment getChildEnvironment() {
    return getChildComponentEnvironment();
  }

  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************

  /**
   * Returns the view model. Usually overridden.
   */
  protected Object getViewModel() throws Exception {
    return new ViewModel();
  }

  /**
   * Returns the the Environment of this Component by default. Usually
   * overridden.
   */
  protected Environment getChildComponentEnvironment() {
    return getEnvironment();
  }
}
