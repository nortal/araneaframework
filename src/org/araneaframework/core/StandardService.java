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
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A full featured Service with support for composite, eventlisteners, viewmodel.
 *
 */
public abstract class StandardService extends BaseService implements Custom.CustomService {
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  /**
   * The attribute of the action id.
   */
  public static final String ACTION_ID_ATTRIBUTE = "serviceActionListenerId";
  
  private static final Logger log = Logger.getLogger(CustomWidget.class);

  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private Map actionListeners = Collections.synchronizedMap(new LinkedMap());
  protected Map viewData = new HashMap();

//*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
  protected class ViewableImpl implements Viewable.Interface {
    public Object getViewModel() {
      try {
        return StandardService.this.getViewModel();
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }
  
  protected class CompositeImpl implements Composite.Interface {
    public Map getChildren() {
      return StandardService.this.getChildren();
    }

    public void attach(Object key, Component comp) {
      _getChildren().put(key, comp);
    }

    public Component detach(Object key) {      
      return (Component) _getChildren().remove(key);
    }    
  }
  
  public class ViewModel implements Custom.ServiceViewModel {
    /**
     * Returns the children of this StandardService.
     */
    public Map getChildren() {
      return StandardService.this.getChildren();
    }
    
    public Map getData() {
      return viewData;
    }    
  }
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  
  public Composite.Interface _getComposite() {
    return new CompositeImpl();
  }
  
  public Viewable.Interface _getViewable() {
    return new ViewableImpl();
  }
  
  /**
   * Adds the ActionListener listener with the specified action id. 
   */
  public void addActionListner(Object actionId, ActionListener listener) {
    actionListeners.put(actionId, listener);
  }
  
  /**
   * Removes the ActionListener listener from this component.
   */
  public void removeActionListener(ActionListener listener) {
    actionListeners.values().remove(listener);
  }

  /**
   * Adds custom data to the widget view model (${widget.custom['key']}). This data will be available until explicitly
   * removed with {@link #removeViewData(String)}.
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
   * Returns an unmodifiable map of the children.
   */
  public Map getChildren() {
    return Collections.unmodifiableMap(new LinkedMap(_getChildren()));
  }

  /**
   * Adds a service with the specified key. Allready initilized services cannot be added. Duplicate
   * keys not allowed. The child is initialized with the Environment env.
   */
  public void addService(Object key, Service child, Environment env) {
    _addComponent(key, child, env);
  }
  
  /**
   * Adds a service with the specified key. Allready initilized services cannot be added. Duplicate
   * keys not allowed. The child is initialized with the Environment from
   * <code>getChildServiceEnvironment()</code>. 
   */
  public void addService(Object key, Service child) {
    try {
      _addComponent(key, child, getChildServiceEnvironment());
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  } 
  
  /**
   * Removes the service with the specified key.
   */
  public void removeService(Object key) {
    _removeComponent(key);
  }
  
  /**
   * Relocates parent's child with keyFrom to this service with a new key keyTo. The child
   * will get the Environment specified by newEnv.
   * @param parent is the current parent of the child to be relocated.
   * @param newEnv the new Environment of the child.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  public void relocateService(Composite parent, Environment newEnv, Object keyFrom, Object keyTo) {
    _relocateComponent(parent, newEnv, keyFrom, keyTo);
  }
  
  /**
   * Relocates parent's child with keyFrom to this service with a new key keyTo. The child
   * will get the Environment of this StandardService.
   * @param parent is the current parent of the child to be relocated.
   * @param keyFrom is the key of the child to be relocated.
   * @param keyTo is the the key, with which the child will be added to this StandardService.
   */
  public void relocateService(Composite parent, Object keyFrom, Object keyTo) {
    try {
      _relocateComponent(parent, getChildServiceEnvironment(), keyFrom, keyTo);
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }
  
  /**
   * Enables the service with the specified key. Only a disabled service can be enabled.
   */
  public void enableService(Object key) {
    _enableComponent(key);
  }
  
  /**
   * Disables the service with the specified key. Only a enabled service can be disabled. A disabled
   * service does not get any actions routed to them.
   */
  public void disableService(Object key) {
    _disableComponent(key);
  }
  
  public Environment getEnvironment() {
    return super.getEnvironment();
  }
  
  public Environment getChildEnvironment() {
    try {
      return getChildServiceEnvironment();
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
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
   * Returns the the Environment of this Service by default. Usually overridden.
   */
  protected Environment getChildServiceEnvironment() throws Exception{
    return getEnvironment();
  }
  
  /**
   * Returns the id of the action based on the input. Uses the ACTION_ID_ATTRIBUTE key
   * to extract it from InputData's global data.
   */
  protected Object getActionId(InputData input) {
    return input.getGlobalData().get(ACTION_ID_ATTRIBUTE);
  }

  
  protected void propagate(Message message) throws Exception {   
    _propagate(message);
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
    
    log.debug("Delivering action '" + actionId +"' to service '" + getClass().getName() + "'");    
    
    if (listener != null ) {
      listener.processAction(actionId, input, output);
    }
    else {
      log.warn("No listener found", new NoSuchActionListenerException(actionId));
      return;
    }
  }
}
