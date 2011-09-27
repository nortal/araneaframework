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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import org.araneaframework.Widget;
import org.araneaframework.core.action.ActionListener;
import org.araneaframework.core.event.EventListener;
import org.araneaframework.core.exception.EventException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A full featured Widget with support for composite, EventListeners, ViewModel.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class BaseApplicationWidget extends BaseWidget implements ApplicationWidget {

  private static final Log LOG = LogFactory.getLog(BaseApplicationWidget.class);

  // *******************************************************************
  // FIELDS
  // *******************************************************************

  private Map<String, List<EventListener>> eventListeners;

  private EventListener globalListener;

  private Map<String, List<ActionListener>> actionListeners;

  private Map<String, Object> viewData;

  private Map<String, Object> viewDataOnce;

  // *******************************************************************
  // PRIVATE METHODS
  // *******************************************************************

  private Map<String, List<EventListener>> getEventListeners() {
    if (this.eventListeners == null) {
      this.eventListeners = new LinkedHashMap<String, List<EventListener>>();
    }
    return this.eventListeners;
  }

  private Map<String, List<ActionListener>> getActionListeners() {
    if (this.actionListeners == null) {
      this.actionListeners = new LinkedHashMap<String, List<ActionListener>>();
    }
    return this.actionListeners;
  }

  private Map<String, Object> getViewData() {
    if (this.viewData == null) {
      this.viewData = new LinkedHashMap<String, Object>();
    }
    return this.viewData;
  }

  private Map<String, Object> getViewDataOnce() {
    if (this.viewDataOnce == null) {
      this.viewDataOnce = new LinkedHashMap<String, Object>();
    }
    return this.viewDataOnce;
  }

  // *******************************************************************
  // PROTECTED METHODS
  // *******************************************************************

  @Override
  protected void propagate(Message message) throws Exception {
    _propagate(message);
  }

  @Override
  protected void update(InputData input) throws Exception {
    if (this.viewDataOnce != null) {
      this.viewDataOnce.clear();
    }

    handleUpdate(input);

    for (Component child : getChildren().values()) {
      if (child != null && child instanceof Widget) {
        ((Widget) child)._getWidget().update(input);
      }
    }
  }

  /**
   * The viewable contract delegated method that creates the view model for current widget on-demand. This is usually
   * overridden when a widget needs to provide a custom view model.
   * 
   * @return The view model of this widget to be used in the view.
   */
  protected ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * Provides the environment that is passed on to child widgets when they are initialized. Usually overridden in order
   * to add new environment entries.
   * 
   * @return The environment from this widget to its child-widgets.
   */
  protected Environment getChildWidgetEnvironment() {
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

  /**
   * Reads an event ID value from input data.
   * 
   * @param input The input data where event ID will be read from.
   * @return The event ID value from input data.
   * @see ApplicationWidget#EVENT_HANDLER_ID_KEY
   */
  protected static String getEventId(InputData input) {
    Assert.notNull(input, "Cannot extract event ID from a null input!");
    return input.getGlobalData().get(ApplicationWidget.EVENT_HANDLER_ID_KEY);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (path != null && path.hasNext()) {
      String next = path.next();
      Assert.notNull(this, next, "Cannot deliver action to child under null key!");

      Service service = (Service) getChildren().get(next);
      if (service == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Widget '" + getScope() + "' could not deliver action as child '" + next + "' was not found!"
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
   * Handles the incoming action (that was sent to this specific widget) by invoking registered listener by action ID.
   * 
   * @param input Input data for the widget or for its child components.
   * @param output Output data for the widget or for its child components.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void handleAction(InputData input, OutputData output) throws Exception {
    String actionId = getActionId(input);

    if (actionId == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Widget '" + getScope() + "' cannot deliver action for a null action ID!" + Assert.thisToString(this));
      }
      return;
    }

    List<ActionListener> listeners = getActionListeners().get(actionId);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Delivering action '" + actionId + "' to widget '" + getClass() + "'.");
    }

    if (listeners != null && !listeners.isEmpty()) {
      for (ActionListener listener : listeners) {
        listener.processAction(actionId, input, output);
      }
      return;
    }

    if (LOG.isWarnEnabled()) {
      LOG.warn("Widget '" + getScope() + "' cannot deliver action as no action listeners were registered for action "
          + "id '" + actionId + "'!" + Assert.thisToString(this));
    }
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    if (path != null && path.hasNext()) {
      String next = path.next();

      Assert.notNull(this, next, "Cannot deliver event to child under null key!");

      Widget pWidget = (Widget) getChildren().get(next);

      if (pWidget == null) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Widget '" + getScope() + "' could not deliver event as child '" + next
              + "' was not found or not a Widget!" + Assert.thisToString(this));
        }
        return;
      }

      pWidget._getWidget().event(path, input);
    } else {
      handleEvent(input);
    }
  }

  /**
   * A callback method that is called when {@link #update(InputData)} is invoked. Allows the current widget to update
   * its state. Note that the child components of this widget are updated AFTER this method has been called.
   * <p>
   * Sub-classes are encouraged to override this method when needed instead of <code>update(InputData)</code> as in this
   * case there's usually no need to call the overridden method.
   * 
   * @param input Input data for the widget or for its child components.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void handleUpdate(InputData input) throws Exception {
  }

  /**
   * Handles the incoming event (that was sent to this specific widget) by invoking registered listener by event ID.
   * 
   * @param input Input data for the widget or for its child components.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void handleEvent(InputData input) throws Exception {
    String eventId = getEventId(input);

    if (eventId == null) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Widget '" + getScope() + "' cannot deliver event for a null action id!" + Assert.thisToString(this));
      }
      return;
    }

    List<EventListener> listeners = this.eventListeners == null ? null : this.eventListeners.get(eventId);

    if (LOG.isTraceEnabled()) {
      LOG.trace("Delivering event '" + eventId + "' to widget '" + getScope() + "', type: '" + getClass().getName()
          + "'");
    }

    try {
      if (listeners != null && !listeners.isEmpty()) {
        for (EventListener listener : listeners) {
          listener.processEvent(eventId, input);
        }
        return;
      } else if (this.globalListener != null) {
        this.globalListener.processEvent(eventId, input);
        return;
      }
    } catch (Exception e) {
      throw new EventException(this, String.valueOf(getScope()), eventId, e);
    }

    if (LOG.isWarnEnabled()) {
      LOG.warn("Widget '" + getScope() + "' cannot deliver event as no event listeners were registered for the event "
          + "id '" + eventId + "'!" + Assert.thisToString(this));
    }
  }

  /**
   * Renders the component state/response to output. This method is meant to be overridden for implementing custom
   * rendering support.
   * 
   * @param output Output data for the widget or for its child components.
   * @throws Exception Any runtime exception that may occur.
   */
  @Override
  protected void render(OutputData output) throws Exception {
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * {@inheritDoc}
   */
  public Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }

  /**
   * {@inheritDoc}
   */
  public Composite.Interface _getComposite() {
    return new CompositeImpl();
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
   * Returns the widget with the specified key.
   * 
   * @param key The ID of the child being returned.
   * @return The Widget under the provided key, or <code>null</code> when no such widget is found.
   */
  public Widget getWidget(String key) {
    return (Widget) getChildren().get(key);
  }

  /**
   * Adds given widget as a child widget with the specified key. Already initialized widget cannot be added. The child
   * is initialized with the provided environment.
   * 
   * @param key A not empty string for widget ID, may be already used for another widget widget (required).
   * @param child Uninitialized widget to be added to this component as a child (required).
   * @param env The environment assigned to the component during initialization (required).
   * @see #_addComponent(String, Component, Environment)
   */
  public void addWidget(String key, Widget child, Environment env) {
    _addComponent(key, child, env);
  }

  /**
   * Adds given widget as a child widget with the specified key. Already initialized widget cannot be added. The child
   * is initialized with the Environment from <code>getChildServiceEnvironment()</code>.
   * 
   * @param key A not empty string for widget ID, may be already used for another child widget (required).
   * @param child Uninitialized widget to be added to this widget as a child (required).
   * @see #_addComponent(String, Component, Environment)
   * @see #getChildWidgetEnvironment()
   */
  public void addWidget(String key, Widget child) {
    try {
      addWidget(key, child, getChildWidgetEnvironment());
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Enables a disabled child widget with the specified key. If the key does not correspond to any kind of child
   * component, a runtime exception will occur. When child widget is already disabled, nothing will happen.
   * 
   * @param key The ID of the child widget to enable (required).
   * @see #_enableComponent(String)
   */
  public void enableWidget(String key) {
    _enableComponent(key);
  }

  /**
   * Disables the child widget with the specified key. If the key does not correspond to any kind of child widget, a
   * runtime exception will occur. A disabled child is a child that is removed from the active set of children to
   * disabled set of children. When child widget is already disabled, nothing will happen.
   * 
   * @param key The ID of the child widget to disable (required).
   * @see #_disableComponent(String)
   */
  public void disableWidget(String key) {
    _disableComponent(key);
  }

  /**
   * Removes the child widget with the specified key from the children and calls destroy on it. When no child component
   * with given key is found, nothing will happen.
   * 
   * @param key The ID of the widget to remove (required).
   * @see #_removeComponent(Object)
   */
  public void removeWidget(Object key) {
    _removeComponent(key);
  }

  /**
   * Delegates the child environment creation to {@link #getChildWidgetEnvironment()}.
   * <p>
   * {@inheritDoc}
   */
  public final Environment getChildEnvironment() {
    return getChildWidgetEnvironment();
  }

  // *******************************************************************
  // SUPPORT FOR ACTION LISTENERS
  // *******************************************************************

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
      getActionListeners().put(actionId, list);
    }

    list.add(listener);
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
  public void clearActionListeners(String actionId) {
    Assert.notNullParam(this, actionId, "actionId");
    getActionListeners().remove(actionId);
  }

  // *******************************************************************
  // SUPPORT FOR EVENT LISTENERS
  // *******************************************************************

  /**
   * Defines a global event-listener to this widget. A global event-listener gets all the events.
   * 
   * @param eventListener A global event-listener for this widget (must not be <code>null</code>).
   * @see #clearGlobalEventListener()
   */
  public void setGlobalEventListener(EventListener eventListener) {
    Assert.notNullParam(this, eventListener, "eventListener");
    this.globalListener = eventListener;
  }

  /**
   * Clears the global event-listener of this widget.
   */
  public void clearGlobalEventListener() {
    this.globalListener = null;
  }

  /**
   * Registers an event-listener to this Widget with an event ID. Multiple listeners can be added under one event ID,
   * and they will be processed in the same order as they were added.
   * 
   * @param eventId The event ID for the listener (required).
   * @param listener The event-listener being added (required).
   * @see #removeEventListener(EventListener)
   */
  public void addEventListener(String eventId, EventListener listener) {
    Assert.notNullParam(this, eventId, "eventId");
    Assert.notNullParam(this, listener, "listener");
    List<EventListener> list = getEventListeners().get(eventId);
    if (list == null) {
      list = new ArrayList<EventListener>(1);
    }
    list.add(listener);
    getEventListeners().put(eventId, list);
  }

  /**
   * Unregisters given event-listener from all of the widget's event-listeners.
   * 
   * @param listener The event-listener to unregister (required).
   * @see #addEventListener(String, EventListener)
   */
  public void removeEventListener(EventListener listener) {
    Assert.notNullParam(this, listener, "listener");
    for (List<EventListener> listeners : getEventListeners().values()) {
      listeners.remove(listener);
    }
  }

  /**
   * Unregisters all event-listeners from this widget with the specified event ID.
   * 
   * @param eventId The event ID for which event-listeners will be removed.
   */
  public void clearEventlisteners(String eventId) {
    Assert.notNullParam(this, eventId, "eventId");
    getEventListeners().remove(eventId);
  }

  // *******************************************************************
  // SUPPORT FOR VIEW-DATA
  // *******************************************************************

  /**
   * Adds custom data to the widget view model by key. This data will remain to be available, until explicitly removed
   * with {@link #removeViewData(String)}.
   * 
   * @param key The key under which <code>customDataItem</code> will be made available (required).
   * @param customDataItem The data that will be made available (may be <code>null</code>).
   */
  public void putViewData(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    getViewData().put(key, customDataItem);
  }

  /**
   * Removes the custom data from widget view model under provided <code>key</code>.
   * 
   * @param key The key of view data item, which will be removed from widget's view data (required).
   */
  public void removeViewData(String key) {
    Assert.notNullParam(this, key, "key");
    getViewData().remove(key);
  }

  /**
   * Adds custom data to the widget view model by key. This data will be available during the current request only. It
   * will be discarded right before next {@link #update(InputData)} is called.
   * 
   * @param key The key under which <code>customDataItem</code> will be made available.
   * @param customDataItem The data that will be made available.
   */
  public void putViewDataOnce(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    getViewDataOnce().put(key, customDataItem);
  }

  // *******************************************************************
  // PROTECTED CLASSES
  // *******************************************************************

  /**
   * The base implementation for application widget view models.
   * 
   * @author Toomas Römer (toomas@webmedia.ee)
   */
  public class ViewModel implements ApplicationWidget.WidgetViewModel {

    private Map<String, Object> viewData;

    /**
     * The base view model takes a snapshot of widget's view data at the moment of its creation.
     */
    public ViewModel() {
      if (BaseApplicationWidget.this.viewData == null) {
        this.viewData = new HashMap<String, Object>();
      } else {
        this.viewData = new HashMap<String, Object>(BaseApplicationWidget.this.viewData);
      }

      if (BaseApplicationWidget.this.viewDataOnce != null) {
        this.viewData.putAll(BaseApplicationWidget.this.viewDataOnce);
      }
    }

    /**
     * {@inheritDoc}
     */
    public Scope getScope() {
      return BaseApplicationWidget.this.getScope();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Component> getChildren() {
      return BaseApplicationWidget.this.getChildren();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getData() {
      return this.viewData;
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
        return BaseApplicationWidget.this.getViewModel();
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
      return BaseApplicationWidget.this.getChildren();
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
}
