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
import java.util.LinkedHashMap;
import java.util.Map;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.Scope;
import org.araneaframework.Viewable;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Basic abstraction of application component extending the basic component abstraction.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public abstract class BaseApplicationComponent extends BaseComponent implements ApplicationComponent {

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * {@inheritDoc}
   */
  public final Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }

  /**
   * {@inheritDoc}
   */
  public final Composite.Interface _getComposite() {
    return new CompositeImpl();
  }

  /**
   * Returns a unmodifiable map of all the child components under this component.
   * 
   * @return An unmodifiable map of child components.
   */
  public final Map<String, Component> getChildren() {
    return Collections.unmodifiableMap(new LinkedHashMap<String, Component>(_getChildren()));
  }

  /**
   * Adds given component as a child component with the specified key. Already initialized component cannot be added.
   * The child is initialized with the provided environment.
   * 
   * @param key A not empty string for component ID, may be already used for another child component (required).
   * @param child Uninitialized component to be added to this component as a child (required).
   * @param env The environment assigned to the component during initialization (required).
   * @see #_addComponent(String, Component, Environment)
   */
  public final void addComponent(String key, Component child, Environment env) {
    _addComponent(key, child, env);
  }

  /**
   * Adds given component as a child component with the specified key. Already initialized component cannot be added.
   * The child is initialized with the Environment from <code>getChildComponentEnvironment()</code>.
   * 
   * @param key A not empty string for component ID, may be already used for another child component (required).
   * @param child Uninitialized component to be added to this component as a child (required).
   * @see #_addComponent(String, Component, Environment)
   * @see #getChildComponentEnvironment()
   */
  public final void addComponent(String key, Component child) {
    _addComponent(key, child, getChildComponentEnvironment());
  }

  /**
   * Relocates the child with <tt>keyFrom</tt> under provided <tt>parent</tt> to this component with a new key
   * <tt>keyTo</tt>. The relocated child component will get the specified environment.
   * 
   * @param parent The current parent of the child component to be relocated (required).
   * @param newEnv The new environment for the relocated child component.
   * @param keyFrom The current key of the child to be relocated (required).
   * @param keyTo The new key with which the child will be added as a child of component (required).
   * @see #_relocateComponent(Composite, Environment, String, String)
   */
  public final void relocateComponent(Composite parent, Environment newEnv, String keyFrom, String keyTo) {
    _relocateComponent(parent, newEnv, keyFrom, keyTo);
  }

  /**
   * Relocates the child with <tt>keyFrom</tt> under provided <tt>parent</tt> to this component with a new key
   * <tt>keyTo</tt>. The relocated child component will get the child-component environment from this component.
   * 
   * @param parent The current parent of the child component to be relocated (required).
   * @param keyFrom The current key of the child to be relocated (required).
   * @param keyTo The new key with which the child will be added as a child of component (required).
   * @see #_relocateComponent(Composite, Environment, String, String)
   * @see #getChildComponentEnvironment()
   */
  public final void relocateComponent(Composite parent, String keyFrom, String keyTo) {
    _relocateComponent(parent, getChildComponentEnvironment(), keyFrom, keyTo);
  }

  /**
   * Enables a disabled child component with the specified key. If the key does not correspond to any kind of child
   * component, a runtime exception will occur. When child component is already disabled, nothing will happen.
   * 
   * @param key The ID of the child component to enable (required).
   * @see #_enableComponent(String)
   */
  public final void enableComponent(String key) {
    _enableComponent(key);
  }

  /**
   * Disables the child component with the specified key. If the key does not correspond to any kind of child component,
   * a runtime exception will occur. A disabled child is a child that is removed from the active set of children to
   * disabled set of children. When child component is already disabled, nothing will happen.
   * 
   * @param key The ID of the child component to disable (required).
   * @see #_disableComponent(String)
   */
  public final void disableComponent(String key) {
    _disableComponent(key);
  }

  /**
   * Removes the child component with the specified key from the children and calls destroy on it. When no child
   * component with given key is found, nothing will happen.
   * 
   * @param key The ID of the component to remove (required).
   * @see #_removeComponent(Object)
   */
  public final void removeComponent(String key) {
    _removeComponent(key);
  }

  /**
   * Delegates the child environment creation to {@link #getChildComponentEnvironment()}.
   * <p>
   * {@inheritDoc}
   */
  public final Environment getChildEnvironment() {
    return getChildComponentEnvironment();
  }

  // *******************************************************************
  // PROTECTED METHODS
  // *******************************************************************

  @Override
  protected void propagate(Message message) {
    _propagate(message);
  }

  /**
   * The viewable contract delegated method that creates the view model for current component on-demand. This is usually
   * overridden when a component needs to provide a custom view model.
   * 
   * @return The view model of this component to be used in the view.
   */
  protected ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * Provides the environment that is passed on to child components when they are initialized. Usually overridden in
   * order to add new environment entries.
   * 
   * @return The environment from this component to its child-components.
   */
  protected Environment getChildComponentEnvironment() {
    return getEnvironment();
  }

  // *******************************************************************
  // PROTECTED CLASSES
  // *******************************************************************

  /**
   * The base implementation for application component view models.
   * 
   * @author Toomas Römer (toomas@webmedia.ee)
   */
  protected class ViewModel implements ApplicationComponent.ComponentViewModel {

    /**
     * {@inheritDoc}
     */
    public final Scope getScope() {
      return BaseApplicationComponent.this.getScope();
    }

    /**
     * {@inheritDoc}
     */
    public final Map<String, Component> getChildren() {
      return BaseApplicationComponent.this.getChildren();
    }
  }

  /**
   * The viewable contract implementation.
   * 
   * @author Toomas Römer (toomas@webmedia.ee)
   */
  protected class ViewableImpl implements Viewable.Interface {

    /**
     * {@inheritDoc}
     */
    public final Object getViewModel() {
      try {
        return BaseApplicationComponent.this.getViewModel();
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * The composite contract implementation.
   * 
   * @author Toomas Römer (toomas@webmedia.ee)
   */
  protected class CompositeImpl implements Composite.Interface {

    /**
     * {@inheritDoc}
     */
    public final Map<String, Component> getChildren() {
      return BaseApplicationComponent.this.getChildren();
    }

    /**
     * {@inheritDoc}
     */
    public final void attach(String key, Component comp) {
      _getChildren().put(key, comp);
    }

    /**
     * {@inheritDoc}
     */
    public final Component detach(String key) {
      return _getChildren().remove(key);
    }
  }
}
