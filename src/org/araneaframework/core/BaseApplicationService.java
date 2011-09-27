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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Scope;
import org.araneaframework.Service;
import org.araneaframework.Viewable;
import org.araneaframework.core.action.ActionListener;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A full featured Service with support for composite, ActionListeners, ViewModel.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public abstract class BaseApplicationService extends BaseService implements ApplicationService {

  private static final Log LOG = LogFactory.getLog(BaseApplicationService.class);

  // *******************************************************************
  // FIELDS
  // *******************************************************************

  private Map<String, List<ActionListener>> actionListeners;

  private Map<String, Object> viewData;

  // *******************************************************************
  // PRIVATE METHODS
  // *******************************************************************

  @SuppressWarnings("unchecked")
  private synchronized Map<String, List<ActionListener>> getActionListeners() {
    if (this.actionListeners == null) {
      this.actionListeners = new LinkedMap(1);
    }
    return this.actionListeners;
  }

  @SuppressWarnings("unchecked")
  private synchronized Map<String, Object> getViewData() {
    if (this.viewData == null) {
      this.viewData = new LinkedMap(1);
    }
    return this.viewData;
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * {@inheritDoc}
   */
  public Composite.Interface _getComposite() {
    return new CompositeImpl();
  }

  /**
   * {@inheritDoc}
   */
  public Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }

  @Override
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  /**
   * Adds an action listener with the specified action ID. When a listener is already registered with same action ID, it
   * will be replaced with the new listener.
   * 
   * @param actionId The action ID for which the listener will be bound.
   * @param listener The listener to be bound to an action.
   */
  public void addActionListener(String actionId, ActionListener listener) {
    Assert.notNullParam(this, actionId, "actionId");
    Assert.notNullParam(this, listener, "listener");

    List<ActionListener> list = getActionListeners().get(actionId);

    if (list == null) {
      list = new ArrayList<ActionListener>();
    }

    list.add(listener);

    getActionListeners().put(actionId, list);
    ComponentUtil.registerActionListener(getEnvironment(), getScope(), actionId, listener);
  }

  /**
   * Removes the ActionListener listener from this component.
   * 
   * @param listener A listener to remove from listening actions.
   */
  public void removeActionListener(ActionListener listener) {
    Assert.notNullParam(this, listener, "listener");
    for (Map.Entry<String, List<ActionListener>> entry : getActionListeners().entrySet()) {
      if (entry.getValue().contains(listener)) {
        boolean existed = entry.getValue().remove(listener);
        if (existed) {
          ComponentUtil.unregisterActionListener(getEnvironment(), getScope(), entry.getKey(), listener);
        }
      }
    }
  }

  /**
   * Clears all the ActionListeners with the specified actionId.
   * 
   * @param actionId The ID of the ActionListeners.
   */
  public void clearActionlisteners(String actionId) {
    Assert.notNullParam(this, actionId, "actionId");
    List<ActionListener> listeners = getActionListeners().remove(actionId);
    ComponentUtil.unregisterActionListeners(getEnvironment(), getScope(), actionId, listeners);
  }

  /**
   * Adds custom data to the widget view model (${widget.custom['key']}). This data will be available until explicitly
   * removed with {@link #removeViewData(String)}.
   * 
   * @param key The key for later accessing the view data item.
   * @param customDataItem The value to add to view data.
   */
  public void putViewData(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    getViewData().put(key, customDataItem);
  }

  /**
   * Removes the custom data under key. Nothing happens when no view data item with given key exists.
   * 
   * @param key The key to identify the view data item to remove
   */
  public void removeViewData(String key) {
    Assert.notNullParam(this, key, "key");
    getViewData().remove(key);
  }

  /**
   * Returns an unmodifiable map of the children.
   * 
   * @return An unmodifiable map of child components.
   */
  public Map<String, Component> getChildren() {
    return Collections.unmodifiableMap(_getChildren());
  }

  /**
   * Adds given service as a child service with the specified key. Already initialized service cannot be added. The
   * child is initialized with the provided environment.
   * 
   * @param key A not empty string for service ID, may be already used for another child service (required).
   * @param child Uninitialized service to be added to this component as a child (required).
   * @param env The environment assigned to the component during initialization (required).
   * @see #_addComponent(String, Component, Environment)
   */
  public void addService(String key, Service child, Environment env) {
    _addComponent(key, child, env);
  }

  /**
   * Adds given service as a child service with the specified key. Already initialized service cannot be added. The
   * child is initialized with the Environment from <code>getChildServiceEnvironment()</code>.
   * 
   * @param key A not empty string for service ID, may be already used for another child service (required).
   * @param child Uninitialized service to be added to this service as a child (required).
   * @see #_addComponent(String, Component, Environment)
   * @see #getChildServiceEnvironment()
   */
  public void addService(String key, Service child) {
    try {
      _addComponent(key, child, getChildServiceEnvironment());
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Relocates the child with <tt>keyFrom</tt> under provided <tt>parent</tt> to this service with a new key
   * <tt>keyTo</tt>. The relocated child service will get the specified environment.
   * 
   * @param parent The current parent of the child service to be relocated (required).
   * @param newEnv The new environment for the relocated child service.
   * @param keyFrom The current key of the child to be relocated (required).
   * @param keyTo The new key with which the child will be added as a child of service (required).
   * @see #_relocateComponent(Composite, Environment, String, String)
   */
  public void relocateService(Composite parent, Environment newEnv, String keyFrom, String keyTo) {
    _relocateComponent(parent, newEnv, keyFrom, keyTo);
  }

  /**
   * Relocates the child with <tt>keyFrom</tt> under provided <tt>parent</tt> to this service with a new key
   * <tt>keyTo</tt>. The relocated child service will get the child-service environment from this service.
   * 
   * @param parent The current parent of the child component to be relocated (required).
   * @param keyFrom The current key of the child to be relocated (required).
   * @param keyTo The new key with which the child will be added as a child of component (required).
   * @see #_relocateComponent(Composite, Environment, String, String)
   * @see #getChildServiceEnvironment()
   */
  public void relocateService(Composite parent, String keyFrom, String keyTo) {
    try {
      _relocateComponent(parent, getChildServiceEnvironment(), keyFrom, keyTo);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Enables a disabled child service with the specified key. If the key does not correspond to any kind of child
   * component, a runtime exception will occur. When child service is already disabled, nothing will happen.
   * 
   * @param key The ID of the child service to enable (required).
   * @see #_enableComponent(String)
   */
  public void enableService(String key) {
    _enableComponent(key);
  }

  /**
   * Disables the child service with the specified key. If the key does not correspond to any kind of child service, a
   * runtime exception will occur. A disabled child is a child that is removed from the active set of children to
   * disabled set of children. When child service is already disabled, nothing will happen.
   * 
   * @param key The ID of the child service to disable (required).
   * @see #_disableComponent(String)
   */
  public void disableService(String key) {
    _disableComponent(key);
  }

  /**
   * Removes the child service with the specified key from the children and calls destroy on it. When no child component
   * with given key is found, nothing will happen.
   * 
   * @param key The ID of the service to remove (required).
   * @see #_removeComponent(Object)
   */
  public void removeService(Object key) {
    _removeComponent(key);
  }

  @Override
  public Environment getEnvironment() {
    return super.getEnvironment();
  }

  /**
   * Delegates the child environment creation to {@link #getChildServiceEnvironment()}.
   * <p>
   * {@inheritDoc}
   */
  public final Environment getChildEnvironment() {
    return getChildServiceEnvironment();
  }

  // *******************************************************************
  // PROTECTED METHODS
  // *******************************************************************

  @Override
  protected void propagate(Message message) throws Exception {
    _propagate(message);
  }

  /**
   * The viewable contract delegated method that creates the view model for current service on-demand. This is usually
   * overridden when a service needs to provide a custom view model.
   * 
   * @return The view model of this service to be used in the view.
   */
  protected ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * Provides the environment that is passed on to child services when they are initialized. Usually overridden in order
   * to add new environment entries.
   * 
   * @return The environment from this service to its child-services.
   */
  protected Environment getChildServiceEnvironment() {
    return getEnvironment();
  }

  /**
   * Reads an action ID value from input data.
   * 
   * @param input The input data where action ID will be read from.
   * @return The action ID value from input data.
   * @see ApplicationService#ACTION_HANDLER_ID_KEY
   */
  protected static String getActionId(InputData input) {
    Assert.notNull(input, "Cannot extract action ID from a null input!");
    return input.getGlobalData().get(ACTION_HANDLER_ID_KEY);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.next();
      Assert.notNull(this, next, "Cannot deliver action to child under null key!");

      Service service = (Service) getChildren().get(next);
      if (service == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Service '" + getScope() + "' could not deliver action as child '" + next + "' was not found!"
              + Assert.thisToString(this));
        }
        return;
      }

      service._getService().action(path, input, output);
    } else {
      handleAction(input, output);
    }
  }

  /**
   * Handles the incoming action (that was sent to this specific service) by invoking registered listener by action ID.
   * 
   * @param input Input data for the service or for its child components.
   * @param output Output data for the service or for its child components.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void handleAction(InputData input, OutputData output) throws Exception {
    String actionId = getActionId(input);

    if (actionId == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Service '" + getScope() + "' can't deliver action for a null action ID!" + Assert.thisToString(this));
      }
      return;
    }

    List<ActionListener> listeners = getActionListeners().get(actionId);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Delivering action '" + actionId + "' to service '" + getClass() + "'.");
    }

    if (listeners != null && !listeners.isEmpty()) {
      for (ActionListener listener : listeners) {
        listener.processAction(actionId, input, output);
      }
      return;
    }

    if (LOG.isWarnEnabled()) {
      LOG.warn("Service '" + getScope() + "' cannot deliver action as no action listeners were registered for action "
          + "id '" + actionId + "'!" + Assert.thisToString(this));
    }
  }

  // *******************************************************************
  // PROTECTED CLASSES
  // *******************************************************************

  /**
   * The base implementation for application service view models.
   * 
   * @author Toomas Römer (toomas@webmedia.ee)
   */
  public class ViewModel implements ApplicationService.ServiceViewModel {

    /**
     * {@inheritDoc}
     */
    public Scope getScope() {
      return BaseApplicationService.this.getScope();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Component> getChildren() {
      return BaseApplicationService.this.getChildren();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getData() {
      return getViewData();
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
    public Object getViewModel() {
      try {
        return BaseApplicationService.this.getViewModel();
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
    public Map<String, Component> getChildren() {
      return BaseApplicationService.this.getChildren();
    }

    /**
     * {@inheritDoc}
     */
    public void attach(String key, Component comp) {
      _getChildren().put(key, comp);
    }

    /**
     * {@inheritDoc}
     */
    public Component detach(String key) {
      return _getChildren().remove(key);
    }
  }

  /**
   * Extends the base functionality with registering asynchronous actions.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  protected class ComponentImpl extends BaseComponent.ComponentImpl {

    @Override
    public synchronized void init(Scope scope, Environment env) {
      super.init(scope, env);
      ComponentUtil.registerActionListeners(getEnvironment(), getScope(), getActionListeners());
    }
  }
}
