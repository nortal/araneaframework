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

package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;

/**
 * This context provides support for flow navigation and nesting. A flow is started using 
 * {@link #start(Widget, org.araneaframework.framework.FlowContext.Configurator, org.araneaframework.framework.FlowContext.Handler)}
 * and continues to be active until it explicitly returns control to the caller using {@link #finish(Object)} or
 * {@link #cancel()}. 
 * 
 * @see org.araneaframework.framework.container.StandardFlowContainerWidget
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface FlowContext extends Serializable {
  /** @since 1.1 */ 
  int TRANSITIONS_START = 1;
  /** @since 1.1 */
  int TRANSITIONS_FINISH = 2;
  /** @since 1.1 */
  int TRANSITIONS_CANCEL = 3;
  /** @since 1.1 */
  int TRANSITIONS_REPLACE = 4;
  /** @since 1.1 */
  int TRANSITIONS_RESET = 5;

  /** 
   * Starts a new nested subflow. Current flow becomes inactive untils subflow calls {@link #finish(Object)} or 
   * {@link #cancel()}.
   * @since 1.0.9
   */
  public void start(Widget flow);

  /**
   * Starts a new nested subflow. Current flow becomes inactive untils subflow calls {@link #finish(Object)} or 
   * {@link #cancel()}. {@link Handler} allows to receive notification, when the subflow ends execution.
   * @since 1.0.9
   */
  public void start(Widget flow, Handler handler);
  
  /**
   * Starts a new nested subflow, that can be configured using the configurator. Current flow becomes inactive
   * untils subflow calls {@link #finish(Object)} or {@link #cancel()}. {@link Handler} allows to receive notification,
   * when the subflow ends execution.
   */
  public void start(Widget flow, Configurator configurator, Handler handler);

  /**
   * Destroys the current flow and starts a new one. When the new flow will end execution it will return control
   * to the caller of the current flow (if there is one). 
   */
  public void replace(Widget flow);
  
  /**
   * Destroys the current flow and starts a new one. When the new flow will end execution it will return control
   * to the caller of the current flow (if there is one). Started subflow can be configured using the configurator.
   * @since 1.0.9 
   */
  public void replace(Widget flow, Configurator configurator);

  /**
   * Finisheds the current flow passing control back to the calling flow. Optionally may return some value that 
   * can be interpreted by the calling flow as the result of the call.
   */
  public void finish(Object result);
  
  /**
   * Finished the current flow passing control back to the calling flow. 
   * Should be interpreted by the calling flow as a unsuccessful return. 
   */
  public void cancel();  
  
  /**
   * Returns whether the current flow is nested, that is has a caller flow.
   */
  public boolean isNested();
  
  /**
   * Resets all currently running flows and calls the <code>callback</code> allowing to start 
   * new flows. Useful e.g. in a menu, when selecting a new menu item and reseting the old
   * stack. 
   */
  public void reset(EnvironmentAwareCallback callback);
  
  /**
   * Returns a reference to the current flow that can be used later to manipulate the current flow.
   * @deprecated to be removed in Aranea 2.0. Also see {@link FlowReference}
   */
  public FlowReference getCurrentReference();

  /**
   * Adds an environment entry that is visible in all subflows.
   */
  public void addNestedEnvironmentEntry(ApplicationWidget scope, final Object entryId, Object envEntry);

  /** 
   * This is unused -- only implementation is a protected class StandardFlowContainerWidget.FlowReference
   * FlowReference.reset() is not called from anywhere and is duplicate of FlowContext.reset() anyway.
   * @deprecated to be removed in Aranea 2.0 
   */
  public interface FlowReference extends Serializable {
    /**
     * Resets the flow stack up to the referred flow and provides the callback with the local environment
     * that can be used to manipulate the flow stack further.
     */
    public void reset(EnvironmentAwareCallback callback) throws Exception;
  }
 
  void setTransitionHandler(TransitionHandler listener);
  TransitionHandler getTransitionHandler();

  /**
   * Callback that will be run when flow has finished some way. 
   */
  public interface Handler extends Serializable {
    public void onFinish(Object returnValue) throws Exception;   
    public void onCancel() throws Exception;
  }
  
  /**
   * Configurator runs when {@link FlowContext} starts flow.
   */
  public interface Configurator extends Serializable {
    public void configure(Widget flow) throws Exception;
  }
  
  /**
   * Handles flow transitions.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.1
   */
  interface TransitionHandler extends Serializable {
    /**
     * @param eventType <code>FlowContext.START<code> .. <code>FlowContext.RESET<code>
     * @param activeFlow active flow at the moment of transition
     * @param transition <code>Serializable</code> closure that would be executed for transition to take effect
     * @return whether the transition should be immediately performed or not (when false is returned, this
     *         {@link TransitionHandler} usually should perform transition sometime later by executing
     *         the supplied <code>onTransitionConfirmed</code> <code>Closure</code>.
     */
    void beforeTransition(int eventType, Widget activeFlow, Closure transition);
    
    void doTransition();
  }
}
