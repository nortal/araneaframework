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
import org.apache.log4j.Logger;
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
public abstract class StandardWidget extends BaseWidget implements Custom.CustomWidget {
      
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  
  /**
   * The key of the event handler.
   */
  public static final String EVENT_HANDLER_ID_KEY = "widgetEventHandler";
  public static final String EVENT_PARAMETER_KEY = "widgetEventParameter";
  
  private static final Logger log = Logger.getLogger(CustomWidget.class);
  
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private Map eventListeners = Collections.synchronizedMap(new LinkedMap());
  private EventListener globalListener;
  
  protected Map actionListeners = Collections.synchronizedMap(new LinkedMap());
  
  private Map viewData = new HashMap();
  private Map viewDataOnce = new HashMap();

  //*******************************************************************
  // CONSTRUCTORS
  //*******************************************************************

  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
  protected class ViewableImpl implements Viewable.Interface {
    public Object getViewModel() {
      try {
        return StandardWidget.this.getViewModel();
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
      return StandardWidget.this.getChildren();
    }

    /**
     * @see org.araneaframework.Composite.Interface#attach(java.lang.Object, org.araneaframework.Component)
     */
    public void attach(Object key, Component comp) {
      Object obj = _getChildren().get(key);
      if (obj == null) {
        _getChildren().put(key, comp);
      }
      else {
        throw new AraneaRuntimeException("Duplicate keys not allowed");
      }
    }

    /**
     * @see org.araneaframework.Composite.Interface#detach(java.lang.Object)
     */
    public Component detach(Object key) {
      Component comp = (Component) _getChildren().remove(key);
      
      if (comp == null) {
        throw new NoSuchWidgetException(key);
      }
      return comp;
    }    
  }
  //*******************************************************************
  // PRIVATE CLASSES
  //*******************************************************************

  //*******************************************************************
  // PUBLIC CLASSES
  //*******************************************************************
  
  public class ViewModel implements Custom.WidgetViewModel {
    private Map viewData;
    
    public ViewModel() {
      viewData = new HashMap(StandardWidget.this.viewData);
      viewData.putAll(viewDataOnce);
    }
    
    public Map getChildren() {
      return StandardWidget.this.getChildren();
    }
    
    public Map getData() {
      return viewData;
    }    
  }
  
  //*******************************************************************
  // PRIVATE METHODS
  //*******************************************************************

  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************
  
  protected boolean isWidgetPresent(InputData input) {
    return (input.getScopedData().get("__present") != null);
  }
  
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
    viewDataOnce.clear();

    handleUpdate(input);

    Iterator ite = (new HashMap(getChildren())).keySet().iterator();
    while (ite.hasNext()) {
      Object key = ite.next();
      Widget widget = (Widget) getChildren().get(key);
      if (widget != null) {
        try {
          input.pushScope(key);
          widget._getWidget().update(input);
        } finally {
          input.popScope();
        }
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
      
      Widget pWidget = (Widget)getChildren().get(next);           
      
      if (pWidget == null ) {
        log.warn("No widget found", new NoSuchWidgetException(next.toString()));  
        return;
      }
      
      try {
        input.pushScope(next);
        pWidget._getWidget().event(path, input);
      }
      finally {
        input.popScope();
      }

    }
    else {
      handleEvent(input);
    } 
  }
  
  protected void process() throws Exception {
    handleProcess();
    
    Iterator ite = (new HashMap(getChildren())).keySet().iterator();
    while(ite.hasNext()) {
      Widget child = (Widget)getChildren().get(ite.next());
      if (child != null) {
      child._getWidget().process();
    }
  }
  }
  
  /**
   * Callback called when <code>input(InputData)</code> is invoked.
   */
  protected void handleUpdate(InputData input) throws Exception {}
  
  /**
   * Callback called when <code>process()</code> is invoked.
   */
  protected void handleProcess() throws Exception {}
  
  /**
   * If no listeners are registered for the <code>getEventId(input)</code> event, throws
   * a {@link NoSuchEventListenerException}, otherwise calls the respective listeners. 
   */
  protected void handleEvent(InputData input) throws Exception {
    Object eventId = getEventId(input);        
    List listener = (List)eventListeners.get(eventId);  
    
    log.debug("Delivering event '" + eventId +"' to widget '" + getClass().getName() + "'");
    
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
    
    log.warn("No listener found", new NoSuchEventListenerException(eventId));
  }
  
  /**
   * If path hasNextStep() routes to the correct child, otherwise calls the
   * appropriate listener.
   */ 
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.next();
      
      Service service = (Service)getChildren().get(next);
      if (service == null) {
        log.warn("No service found", new NoSuchServiceException(next));  
        return;
      }
      
      try {
        input.pushScope(next);
        output.pushScope(next);
        
        service._getService().action(path, input, output);
      }
      finally {
        input.popScope();
        output.popScope();
      }
    }
    else {
      handleAction(input, output);
    }
  }
  
  /**
   * Calls the approriate listener, if none present throws
   * {@link NoSuchActionListenerException}.
   */
  protected void handleAction(InputData input, OutputData output) throws Exception {
    Object actionId = getActionId(input);    
    ActionListener listener = (ActionListener)actionListeners.get(actionId);
    
    if (log.isDebugEnabled())
      log.debug("Delivering action '" + actionId +"' to service '" + getClass().getName() + "'");    
    
    if (listener != null ) {
      listener.processAction(actionId, input, output);
    }
    else {
      log.warn("No listener found", new NoSuchActionListenerException(actionId));
      return;
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
  protected Object getEventId(InputData input) {
    return input.getGlobalData().get(EVENT_HANDLER_ID_KEY);
  }
  
  /**
   * Returns the id of the action based on the input. Uses the ACTION_ID_ATTRIBUTE key
   * to extract it from InputData's global data.
   */
  protected Object getActionId(InputData input) {
    return input.getGlobalData().get(StandardService.ACTION_ID_ATTRIBUTE);
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
  public void addGlobalEventListener(EventListener eventListener) {
    this.globalListener = eventListener;
  }
  
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
    List list = (List)eventListeners.get(eventId);
    
    if (list == null) {
      list = new ArrayList();
    }
    list.add(listener);
    
    eventListeners.put(eventId, list);
  }
  
  /**
   * Removes the listener from the Widget's eventlisteners.
   * @param listener the EventListener being added
   * @see #addEventListener
   */
  public void removeEventListener(EventListener listener) {
    Iterator ite = (new HashMap(eventListeners)).values().iterator();
    while(ite.hasNext()) {
      ((List)ite.next()).remove(listener);
    }
  }
  
  /**
   * Clears all the EventListeners from this Widget with the specified eventId.
   * @param eventId the id of the EventListeners.
   */
  public void clearEventlisteners(Object eventId) {
    eventListeners.remove(eventId);
  }
  
  /**
   * Adds custom data to the widget view model (${widget.custom['key']}). This data will be
   * available until explicitly removed with {@link #removeViewData(String)}.
   */
  public void putViewData(String key, Object customDataItem) {
    viewData.put(key, customDataItem);
  }

  /**
   * Removes the custom data under key.
   */
  public void removeViewData(String key) {
    viewData.remove(key);
  }

  /**
   * Adds custom data to the widget view model (${widget.custom['key']}). This data will be available during this
   * request only. 
   */
  public void putViewDataOnce(String key, Object customDataItem) {
    viewDataOnce.put(key, customDataItem);
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
    actionListeners.put(actionId, listener);
  }
  
  /**
   * Removes the ActionListener listener from this component.
   */
  public void removeActionListener(ActionListener listener) {
    actionListeners.values().remove(listener);
  }

}
