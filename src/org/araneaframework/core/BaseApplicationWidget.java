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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.araneaframework.Service;
import org.araneaframework.Viewable;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A full featured Widget with support for composite, eventlisteners, viewmodel.
 * 
 */
public abstract class BaseApplicationWidget extends BaseWidget implements ApplicationWidget {
      
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  
  private static final Log log = LogFactory.getLog(BaseApplicationWidget.class);
  
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private Map eventListeners;
  private EventListener globalListener;
  
  private Map actionListeners;
  
  private Map viewData;
  private Map viewDataOnce;

  //*******************************************************************
  // CONSTRUCTORS
  //*******************************************************************

  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
  protected class ViewableImpl implements Viewable.Interface {
    public Object getViewModel() {
      try {
        return BaseApplicationWidget.this.getViewModel();
      }
      catch (Exception e) {        
        throw ExceptionUtil.uncheckException(e);        
      }
    }
  }
  
  protected class CompositeImpl implements Composite.Interface {
    /**
     * Returns a map of all the child components under this Composite.
     * @return a map of child components
     */
    public Map getChildren() {
      return BaseApplicationWidget.this.getChildren();
    }

    /**
     * @see org.araneaframework.Composite.Interface#attach(java.lang.Object, org.araneaframework.Component)
     */
    public void attach(Object key, Component comp) {
      _getChildren().put(key, comp);
    }

    /**
     * @see org.araneaframework.Composite.Interface#detach(java.lang.Object)
     */
    public Component detach(Object key) {
      return (Component) _getChildren().remove(key);
    }    
  }
  //*******************************************************************
  // PRIVATE CLASSES
  //*******************************************************************

  //*******************************************************************
  // PUBLIC CLASSES
  //*******************************************************************
  
  public class ViewModel implements ApplicationWidget.WidgetViewModel {
    private Map viewData;
    
    public ViewModel() {
      viewData = BaseApplicationWidget.this.viewData == null ? new HashMap() : new HashMap(BaseApplicationWidget.this.viewData);
      if (viewDataOnce != null)
        viewData.putAll(viewDataOnce);
    }
    
    
    
    public Map getChildren() {
      return BaseApplicationWidget.this.getChildren();
    }
    
    public Map getData() {
      return viewData;
    }    
  }
  
  //*******************************************************************
  // PRIVATE METHODS
  //*******************************************************************

  private Map getEventListeners() {
    if (eventListeners == null)
      eventListeners = new LinkedMap(1);
    
    return eventListeners;
  }
  
  private Map getActionListeners() {
    if (actionListeners == null)
      actionListeners = new LinkedMap(1);
      
    return actionListeners;
  }
  
  private Map getViewData() {
    if (viewData == null)
      viewData = new LinkedMap(1);
      
    return viewData;
  }
  
  private Map getViewDataOnce() {
    if (viewDataOnce == null)
      viewDataOnce = new LinkedMap(1);
      
    return viewDataOnce;
  }
  
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************
  
  /**
   * Returns the widget's Environment by default. Usually overridden.
   */
  protected Environment getChildWidgetEnvironment() throws Exception {
    return getEnvironment();
  }
  
  protected void propagate(Message message) throws Exception {
    _propagate(message);
  }

  protected void update(InputData input) throws Exception {
    if (viewDataOnce != null)
      viewDataOnce.clear();

    handleUpdate(input);

    Iterator ite = (new HashMap(getChildren())).keySet().iterator();
    while (ite.hasNext()) {
      Object key = ite.next();
      Component c = (Component) getChildren().get(key);
      if (c != null && c instanceof Widget) {
        ((Widget)c)._getWidget().update(input);
      }
    }
  }
  
  /**
   * If path hasNextStep() routes to the correct child, 
   * otherwise calls the appropriate listener. 
   */ 
  protected void event(Path path, InputData input) throws Exception {    
    if (path != null && path.hasNext()) {
      Object next = path.next();

      Assert.notNull(this, next, "Cannot deliver event to child under null key!");
      
      Widget pWidget = (Widget)getChildren().get(next);           
      
      if (pWidget == null) {
        if (log.isWarnEnabled()) {
          log.warn("Widget '" + getScope() + "' could not deliver event as child '" + next + "' was not found or not a Widget!" + Assert.thisToString(this));
        }
        return;
      }

      pWidget._getWidget().event(path, input);
    }
    else {
      handleEvent(input);
    } 
  }
  
  /**
   * Callback called when <code>update(InputData)</code> is invoked.
   */
  protected void handleUpdate(InputData input) throws Exception {}
  
  /**
   * Calls the respective listeners. 
   */
  protected void handleEvent(InputData input) throws Exception {
    String eventId = getEventId(input);
    
    if (eventId == null) {
      if (log.isWarnEnabled()) {
        log.warn("Widget '" + getScope() + "' cannot deliver event for a null action id!" + Assert.thisToString(this));
      }
      return;
    }
    
    List listener = eventListeners == null ? null : (List)eventListeners.get(eventId);  
    
    if (log.isDebugEnabled()) {
      log.debug("Delivering event '" + eventId + "' to widget '" + getClass().getName() + "'");
    }
    
    try {
      if (listener != null && listener.size() > 0) {
        Iterator ite = (new ArrayList(listener)).iterator();
        while(ite.hasNext()) {
          ((EventListener)ite.next()).processEvent(eventId, input);
        }

        return;
      }

      if (globalListener != null) {
        globalListener.processEvent(eventId, input);
        return;
      }
    }
    catch (Exception e) {
      throw new EventException(this, getScope().toString(), eventId, e);
    }

    if (log.isWarnEnabled()) {
      log.warn("Widget '" + getScope() +
         "' cannot deliver event as no event listeners were registered for the event id '" + eventId + "'!"  + Assert.thisToString(this));
    }
  }
  
  /**
   * If path hasNextStep() routes to the correct child, otherwise calls the
   * appropriate listener.
   */ 
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.next();
      
      Assert.notNull(this, next, "Cannot deliver action to child under null key!");
      
      Service service = (Service)getChildren().get(next);
      if (service == null) {
        log.warn("Service '" + getScope() +
            "' could not deliver action as child '" + next + "' was not found!" + Assert.thisToString(this));  
        return;
      }
      
      service._getService().action(path, input, output);
    }
    else {
      handleAction(input, output);
    }
  }
  
  /**
   * Calls the approriate listener
   */
  protected void handleAction(InputData input, OutputData output) throws Exception {
    Object actionId = getActionId(input);    
    
    if (actionId == null) {
      if (log.isWarnEnabled()) {
         log.warn("Service '" + getScope() + "' cannot deliver action for a null action id!" + Assert.thisToString(this));
      }
      return;
    }
    
    List listener = actionListeners == null ? null : (List)actionListeners.get(actionId);  

    if (log.isTraceEnabled()) {
      log.trace("Delivering action '" + actionId +"' to service '" + getClass() + "'");
    }
    
    if (listener != null && listener.size() > 0) {
      Iterator ite = (new ArrayList(listener)).iterator();
      while(ite.hasNext()) {
        ((ActionListener)ite.next()).processAction(actionId, input, output);
      }
      
      return;
    }

    if (log.isWarnEnabled()) {
      log.warn("Service '" + getScope() +
        "' cannot deliver action as no action listeners were registered for action id '" + actionId + "'!"  + Assert.thisToString(this));
    }
  }
  
  /**
   * Renders the component to output, meant for overriding.
   */
  protected void render(OutputData output) throws Exception {}
  
  /**
   * Returns the id of the event in InputData. By default returns EVENT_HANDLER_ID_KEY from
   * the input's global data.
   */
  protected String getEventId(InputData input) {
    return (String) input.getGlobalData().get(ApplicationWidget.EVENT_HANDLER_ID_KEY);
  }
  
  /**
   * Returns the id of the action based on the input. Uses the ACTION_HANDLER_ID_KEY key
   * to extract it from InputData's global data.
   */
  protected Object getActionId(InputData input) {
    return input.getGlobalData().get(ApplicationService.ACTION_HANDLER_ID_KEY);
  }
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  /**
   * Returns the Viewable.Interface internal implementation.
   * @return  the Viewable.Interface implementation
   */
  public Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }
  
  /**
   * Returns the Composite.Interface internal implementation.
   * @return the Composite.Interface implementation
   */
  public Composite.Interface _getComposite() {
    return new CompositeImpl();
  }
  
  /**
   * Returns all the childcomponents of this component.
   * @return a map of the childcomponents under this component
   */
  public Map getChildren() {
    return Collections.unmodifiableMap(new LinkedMap(_getChildren()));
  }
  
  /**
   * Returns the widget with the specified key.
   * @param key of the child being returned 
   * @return the Widget under the provided key
   */
  public Widget getWidget(Object key) {
    return (Widget) getChildren().get(key);
  }

  /**
   * Adds a widget as a child widget with the key. The child is initialized with the
   * environment provided.
   * 
   * @param key of the the child Widget 
   * @param child Widget being added
   * @param env the Environment the child will be initialized with
   */
  public void addWidget(Object key, Widget child, Environment env) {
    _addComponent(key, child, env);
  }
  
  /** Adds a widget as a child widget with the key. The child is initialized with the
   * Environment of this Widget
   * @param key of the the child Widget 
   * @param child Widget being added
   */
  public void addWidget(Object key, Widget child) {
    try {
      addWidget(key, child, this.getChildWidgetEnvironment());
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }
  
  /**
   * Removes component from the children and calls destroy on it.
   * @param key of the child being removed
   */
  public void removeWidget(Object key) {
    _removeComponent(key);
  }
  
  /**
   * Enables the widget with the specified key. Only a disabled widgets can be enabled.
   */
  public void enableWidget(Object key) {
    _enableComponent(key);
  }
  
  /**
   * Disables the widget with the specified key. Only a enabled widgets can be disabled.
   */
  public void disableWidget(Object key) {
    _disableComponent(key);
  }
  
  public Environment getEnvironment() {
    return super.getEnvironment();
  }
  
  public final Environment getChildEnvironment() {
    try {
      return getChildWidgetEnvironment();
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }
  
  /**
   * Adds a global eventlistener to this Widget. A global eventlistener gets
   * all the events.
   * 
   * @param eventListener a EventListener added as the global eventlistener.
   */
  public void setGlobalEventListener(EventListener eventListener) {
    Assert.notNullParam(this, eventListener, "eventListener");
    this.globalListener = eventListener;
  }
  
  /**
   * Clears the global eventlistener of this Widget.
   */
  public void clearGlobalEventListener() {
    this.globalListener = null;
  }

  /**
   * Adds an EventListener to this Widget with an eventId. Multiple listeners
   * can be added under one eventId.
   * 
   * @param eventId the eventId of the listener
   * @param listener the EventListener being added
   * @see #removeEventListener
   */
  public void addEventListener(Object eventId, EventListener listener) {
    Assert.notNullParam(this, eventId, "eventId");
    Assert.notNullParam(this, listener, "listener");
    
    List list = (List)getEventListeners().get(eventId);
    
    if (list == null) {
      list = new ArrayList(1);
    }
    list.add(listener);
    
    getEventListeners().put(eventId, list);
  }
  
  /**
   * Removes the listener from the Widget's eventlisteners.
   * @param listener the EventListener being added
   * @see #addEventListener
   */
  public void removeEventListener(EventListener listener) {
    Assert.notNullParam(this, listener, "listener");
    
    Iterator ite = (new HashMap(getEventListeners())).values().iterator();
    while(ite.hasNext()) {
      ((List)ite.next()).remove(listener);
    }
  }
  
  /**
   * Clears all the EventListeners from this Widget with the specified eventId.
   * @param eventId the id of the EventListeners.
   */
  public void clearEventlisteners(Object eventId) {
    Assert.notNullParam(this, eventId, "eventId");
    
    getEventListeners().remove(eventId);
  }
  
  /**
   * Adds custom data to the widget view model (${widget.custom['key']}). This data will be
   * available until explicitly removed with {@link #removeViewData(String)}.
   */
  public void putViewData(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    
    getViewData().put(key, customDataItem);
  }

  /**
   * Removes the custom data under key.
   */
  public void removeViewData(String key) {
    Assert.notNullParam(this, key, "key");
    
    getViewData().remove(key);
  }

  /**
   * Adds custom data to the widget view model (${widget.custom['key']}). This data will be available during this
   * request only. 
   */
  public void putViewDataOnce(String key, Object customDataItem) {
    Assert.notNullParam(this, key, "key");
    
    getViewDataOnce().put(key, customDataItem);
  }
  
  /**
   * Returns the view model. Usually overridden.
   */
  public Object getViewModel() throws Exception {
    return new ViewModel();
  }
 
  /**
   * Adds the ActionListener listener with the specified action id. 
   */
  public void addActionListener(Object actionId, ActionListener listener) {
    Assert.notNullParam(this, actionId, "actionId");
    Assert.notNullParam(this, listener, "listener");
    
    List list = (List)getActionListeners().get(actionId);
    
    if (list == null) {
      list = new ArrayList(1);
    }
    list.add(listener);
    
    getActionListeners().put(actionId, list);
  }
  
  /**
   * Removes the ActionListener listener from this component.
   */
  public void removeActionListener(ActionListener listener) {
    Assert.notNullParam(this, listener, "listener");
    
    Iterator ite = (new HashMap(getActionListeners())).values().iterator();
    while(ite.hasNext()) {
      ((List)ite.next()).remove(listener);
    }
  }
  
  /**
   * Clears all the ActionListeners with the specified actionId.
   * @param actionId the actionId
   */
  public void clearActionListeners(Object actionId) {
    Assert.notNullParam(this, actionId, "actionId");
    
    getActionListeners().remove(actionId);
  }
}
