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

package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.collections.Closure;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;

/**
 * This context provides support for flow navigation and nesting. A flow is started using
 * {@link #start(Widget, Configurator, Handler)} and continues to be active until it explicitly returns control to the
 * caller using {@link #finish(Object)} or {@link #cancel()}. Another option is that all flows are cancelled using
 * {@link #reset(EnvironmentAwareCallback)};
 * 
 * @see org.araneaframework.framework.container.StandardFlowContainerWidget
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FlowContext extends Serializable {

  /**
   * Transition types supported by the <code>FlowContext</code>.
   * 
   * @since 1.1 (constants added), 2.0 (refactored in favor of enum)
   */
  enum Transition {
    /**
     * Marks a flow start transition.
     */
    START,
    /**
     * Marks a flow finish transition.
     */
    FINISH,
    /**
     * Marks a flow canceling transition.
     */
    CANCEL,
    /**
     * Marks a flow replace-with-another-flow transition.
     */
    REPLACE,
    /**
     * Marks a flow context reset transition.
     */
    RESET
  }

  /**
   * Starts a new nested sub-flow. Current flow becomes inactive until sub-flow calls {@link #finish(Object)} or
   * {@link #cancel()}.
   * 
   * @param flow The uninitialized flow to start. Must not be <code>null</code>.
   * @since 1.0.9
   */
  void start(Widget flow);

  /**
   * Starts a new nested sub-flow. Current flow becomes inactive until sub-flow calls {@link #finish(Object)} or
   * {@link #cancel()}. {@link Handler} allows to receive notification, when the sub-flow ends execution.
   * 
   * @param flow The uninitialized flow to start. Must not be <code>null</code>.
   * @param handler The callback handler that will called when the given flow returns.
   * @since 1.0.9
   */
  void start(Widget flow, Handler<?> handler);

  /**
   * Starts a new nested sub-flow, that can be configured using the configurator. Current flow becomes inactive until
   * sub-flow calls {@link #finish(Object)} or {@link #cancel()}. {@link Handler} allows to receive notification, when
   * the sub-flow ends execution.
   * 
   * @param flow The uninitialized flow to start. Must not be <code>null</code>.
   * @param configurator The configurator that can configure the started flow after it is initialized.
   * @param handler The callback handler that will called when the given flow returns.
   */
  void start(Widget flow, Configurator configurator, Handler<?> handler);

  /**
   * Destroys the current last flow and starts a new one. When the new flow will end execution it will return control to
   * the caller of the current flow (if there is one).
   * 
   * @param flow The uninitialized flow to start. Must not be <code>null</code>.
   */
  void replace(Widget flow);

  /**
   * Destroys the current flow and starts a new one. When the new flow ends execution it will return control to the
   * caller of the current flow (if there is one). Started sub-flow can be configured using the configurator.
   * 
   * @param flow The uninitialized flow to start. Must not be <code>null</code>.
   * @param configurator The configurator that can configure the started flow after it is initialized.
   * @since 1.0.9
   */
  void replace(Widget flow, Configurator configurator);

  /**
   * Finishes the current flow passing control back to the calling flow. Optionally may return some value that can be
   * interpreted by the calling flow as the result of the call.
   * 
   * @param result The value to return by the finishing flow. It will be passed to the registered flow handler.
   */
  void finish(Object result);

  /**
   * Finishes the current flow passing control back to the calling flow. Should be interpreted by the calling flow as an
   * "unsuccessful" return.
   */
  void cancel();

  /**
   * Returns whether the current flow is nested, that is has a caller flow.
   * 
   * @return A Boolean that is <code>true</code> when this flow container contains nested flows.
   */
  boolean isNested();

  /**
   * Returns the nested flows where they are sorted ascending in the order they were initialized.
   * 
   * @return A collection of nested flows, or an empty collection when {@link #isNested()} returns <code>false</code>.
   * @since 2.0
   */
  Collection<Widget> getNestedFlows();

  /**
   * Resets all currently running flows and calls the <code>callback</code> allowing to start new flows. Useful e.g. in
   * a menu, when selecting a new menu item and reseting the old stack.
   * 
   * @param callback An optional callback that can do some work after flows are reseted.
   */
  void reset(EnvironmentAwareCallback callback);

  /**
   * Adds an environment entry that is visible in all sub-flows under the given <code>scope</code>.
   * 
   * @param <T> The type of environment entry value (ant thus environment entry key class type, too).
   * @param scope The widget that makes the environment entry visible to its all sub-flows.
   * @param entryId The environment entry key.
   * @param envEntry The environment entry value.
   */
  <T> void addNestedEnvironmentEntry(ApplicationWidget scope, final Class<T> entryId, T envEntry);

  /**
   * Sets the <code>FlowContext.TransitionHandler</code> which performs the flow navigation.
   * 
   * @param handler The transition handler that the last flow should use.
   * @since 1.1
   */
  void setTransitionHandler(TransitionHandler handler);

  /**
   * Returns currently active transition handler. If the most current child is a {@link FlowContextWidget}, it will take
   * its currently active transition handler recursively (since 1.2.2).
   * 
   * @return Currently active transition handler (never <code>null</code>).
   * @since 1.1
   */
  TransitionHandler getTransitionHandler();

  /**
   * Callback that will be run when flow has finished some way.
   */
  interface Handler<T> extends Serializable {

    /**
     * Callback method that will be called when the flow finishes by calling {@link FlowContext#finish(Object)}.
     * 
     * @param returnValue The value that was passed to {@link FlowContext#finish(Object)}.
     */
    void onFinish(T returnValue);

    /**
     * Callback method that will be called when the flow finishes by calling {@link FlowContext#cancel()}.
     */
    void onCancel();
  }

  /**
   * Configurator runs when {@link FlowContext} starts flow. It will be asked to configure the provided initialized
   * flow.
   * 
   * @see FlowContext#start(Widget, Configurator, Handler)
   */
  interface Configurator extends Serializable {

    /**
     * Implementation should do the necessary work to configure the given initialized flow.
     * 
     * @param flow The initialized flow to configure.
     * @throws Exception Any exception that may occur.
     */
    void configure(Widget flow) throws Exception;
  }

  /**
   * Performs the flow transitions in {@link FlowContext}.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.1
   */
  interface TransitionHandler extends Serializable {

    /**
     * The implementation should handle the transition with given data.
     * 
     * @param eventType The transition type to handle.
     * @param activeFlow The active flow at the moment of transition request
     * @param transition <code>Serializable</code> closure that needs to be executed for transition to happen.
     */
    void doTransition(Transition eventType, Widget activeFlow, Closure transition);
  }
}
