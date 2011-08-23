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
  // PROTECTED CLASSES
  // *******************************************************************

  protected class ViewModel implements ApplicationComponent.ComponentViewModel {

    public final Scope getScope() {
      return BaseApplicationComponent.this.getScope();
    }

    public final Map<String, Component> getChildren() {
      return BaseApplicationComponent.this.getChildren();
    }
  }

  protected class ViewableImpl implements Viewable.Interface {

    public final Object getViewModel() {
      try {
        return BaseApplicationComponent.this.getViewModel();
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  protected class CompositeComponentImpl implements Composite.Interface {

    public final Map<String, Component> getChildren() {
      return BaseApplicationComponent.this.getChildren();
    }

    public final void attach(String key, Component comp) {
      _getChildren().put(key, comp);
    }

    public final Component detach(String key) {
      return _getChildren().remove(key);
    }
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * Returns a unmodifiable map of all the child components under this Component.
   * 
   * @return a map of child components
   */
  public final Map<String, Component> getChildren() {
    return Collections.unmodifiableMap(new LinkedHashMap<String, Component>(_getChildren()));
  }

  public final Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }

  public final Composite.Interface _getComposite() {
    return new CompositeComponentImpl();
  }

  /**
   * Adds a component with the specified key. Already initialized component cannot be added. Duplicate keys not allowed.
   * The child is initialized with the Environment env.
   */
  public final void addComponent(String key, Component child, Environment env) {
    _addComponent(key, child, env);
  }

  /**
   * Adds a component with the specified key. Already initialized components cannot be added. Duplicate keys not
   * allowed. The child is initialized with the Environment from <code>getChildComponentEnvironment()</code>.
   */
  public final void addComponent(String key, Component child) {
    _addComponent(key, child, getChildComponentEnvironment());
  }

  /**
   * Relocates parent's child with keyFrom to this component with a new key keyTo. The child will get the Environment
   * specified by newEnv.
   * 
   * @param parent is the current parent of the child to be relocated.
   * @param newEnv the new Environment of the child.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  public final void relocateComponent(Composite parent, Environment newEnv, String keyFrom, String keyTo) {
    _relocateComponent(parent, newEnv, keyFrom, keyTo);
  }

  /**
   * Relocates parent's child with keyFrom to this service with a new key keyTo. The child will get the Environment of
   * this StandardService.
   * 
   * @param parent is the current parent of the child to be relocated.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  public final void relocateComponent(Composite parent, String keyFrom, String keyTo) {
    _relocateComponent(parent, getChildComponentEnvironment(), keyFrom, keyTo);
  }

  /**
   * Enables the component with the specified key. Only a disabled component can be enabled.
   */
  public final void enableComponent(String key) {
    _enableComponent(key);
  }

  /**
   * Disables the component with the specified key. Only a enabled component can be disabled.
   */
  public final void disableComponent(String key) {
    _disableComponent(key);
  }

  /**
   * Removes the component with the specified key.
   */
  public final void removeComponent(String key) {
    _removeComponent(key);
  }

  public Environment getChildEnvironment() {
    return getChildComponentEnvironment();
  }

  @Override
  protected void propagate(Message message) {
    _propagate(message);
  }

  /**
   * Returns the view model. Usually overridden.
   */
  protected Object getViewModel() {
    return new ViewModel();
  }

  /**
   * Returns the the Environment of this Component by default. Usually overridden.
   */
  protected Environment getChildComponentEnvironment() {
    return getEnvironment();
  }
}
