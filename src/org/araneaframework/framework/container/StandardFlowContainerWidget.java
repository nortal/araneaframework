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

package org.araneaframework.framework.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.Closure;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.event.StandardEventListener;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.EmptyCallStackException;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowContextWidget;
import org.araneaframework.http.WindowScrollPositionContext;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * A standard {@link FlowContext} implementation where the flows are treated as a stack of flows.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
@SuppressWarnings("unchecked")
public class StandardFlowContainerWidget extends BaseApplicationWidget implements FlowContextWidget {

  private static final Log LOG = LogFactory.getLog(StandardFlowContainerWidget.class);

  private static final String BASE_FLOW_KEY = "f";

  private static final String TOP_FLOW_KEY = BASE_FLOW_KEY + 0;

  /**
   * The stack of all the calls. Can be accessed through {@link #getCallStack()}.
   */
  private LinkedList<CallFrame> callStack = new LinkedList<CallFrame>();

  /**
   * The top flow widget that is registered as first flow during flow container initialization.
   */
  private Widget top;

  /**
   * Flow container property indicating whether widget will be finished when its flow-stack becomes empty and a parent
   * flow container exists.
   */
  private boolean finishable = true;

  /**
   * Flow container parameter that controls whether {@link FlowContextWidget#FLOW_CANCEL_EVENT} will be processed so
   * that the given number number of last flows will be canceled.
   * 
   * @since 2.0
   */
  private boolean allowFlowCancelEvent = true;

  private final Map<Class<?>, Object> nestedEnvironmentEntries = new HashMap<Class<?>, Object>();

  private final Map<Class<?>, LinkedList<Object>> nestedEnvEntryStacks = new HashMap<Class<?>, LinkedList<Object>>();

  /**
   * Constructs a new flow container widget with an empty flow stack.
   */
  public StandardFlowContainerWidget() {
  }

  /**
   * Constructs a new flow container widget with <code>topWidget</code> being the first flow on the top of flow stack.
   * 
   * @param topWidget A widget that will be automatically added to the flow stack when this widget is initialized
   *          (optional).
   */
  public StandardFlowContainerWidget(Widget topWidget) {
    this.top = topWidget;
  }

  /**
   * Method for specifying the first flow widget when this flow container is initialized. Throws a runtime exception
   * when this flow container is already initialized.
   * 
   * @param topWidget A widget that will be automatically added to the flow stack when this widget is initialized
   *          (optional).
   */
  public void setTop(Widget topWidget) {
    Assert.isTrue(!isInitialized(), "The flow container is already initialized. Cannot change top widget.");
    this.top = topWidget;
  }

  /**
   * Determines whether this flow container will ever return control to higher flow contexts in the component hierarchy.
   * <p>
   * When a flow container is finishable and a parent flow context exists, it will return control to it when its flow
   * stack becomes empty. By default, an instance of this class is finishable.
   * 
   * @param finishable A Boolean that is <code>true</code> when the flow container must be finishable.
   * @since 1.1
   */
  public void setFinishable(boolean finishable) {
    this.finishable = finishable;
  }

  /**
   * {@inheritDoc}
   */
  public void setAllowFlowCancelEvent(boolean allowFlowCancelEvent) {
    this.allowFlowCancelEvent = allowFlowCancelEvent;
  }

  /**
   * {@inheritDoc}
   */
  public void start(Widget flow) {
    start(flow, null, null);
  }

  /**
   * {@inheritDoc}
   */
  public void start(Widget flow, Handler<?> handler) {
    start(flow, null, handler);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("rawtypes")
  public void start(Widget flow, Configurator configurator, Handler handler) {
    TransitionHandler transitionHandler = getTransitionHandler();
    StartClosure startClosure = new StartClosure(flow, configurator, handler);
    doTransition(transitionHandler, Transition.START, startClosure);
  }

  /**
   * {@inheritDoc}
   */
  public void replace(Widget flow) {
    replace(flow, null);
  }

  /**
   * {@inheritDoc}
   */
  public void replace(Widget flow, Configurator configurator) {
    TransitionHandler transitionHandler = getTransitionHandler();
    ReplaceClosure replaceClosure = new ReplaceClosure(flow, configurator);
    doTransition(transitionHandler, Transition.REPLACE, replaceClosure);
  }

  /**
   * {@inheritDoc}
   */
  public void finish(Object returnValue) {
    TransitionHandler transitionHandler = getTransitionHandler();
    FinishClosure finishClosure = new FinishClosure(returnValue);
    doTransition(transitionHandler, Transition.FINISH, finishClosure);
  }

  /**
   * {@inheritDoc}
   */
  public void cancel() {
    TransitionHandler transitionHandler = getTransitionHandler();
    CancelClosure cancelClosure = new CancelClosure();
    doTransition(transitionHandler, Transition.CANCEL, cancelClosure);
  }

  /**
   * {@inheritDoc}
   */
  public void reset(final EnvironmentAwareCallback callback) {
    TransitionHandler transitionHandler = getTransitionHandler();
    ResetClosure resetClosure = new ResetClosure(callback);
    doTransition(transitionHandler, Transition.RESET, resetClosure);
  }

  /**
   * {@inheritDoc}
   */
  public TransitionHandler getTransitionHandler() {
    // The default handler is StandardTransitionHandler:
    TransitionHandler result = new StandardTransitionHandler();
    CallFrame activeCallFrame = getActiveCallFrame();

    if (activeCallFrame != null) {
      if (activeCallFrame.getWidget() instanceof FlowContextWidget) {
        FlowContextWidget flow = (FlowContextWidget) activeCallFrame.getWidget();
        result = flow.getTransitionHandler();

      } else if (activeCallFrame.getTransitionHandler() != null) {
        result = activeCallFrame.getTransitionHandler();
      }
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  public void setTransitionHandler(TransitionHandler transitionHandler) {
    CallFrame activeCallFrame = getActiveCallFrame();
    if (activeCallFrame != null) {
      if (activeCallFrame instanceof FlowContextWidget) {
        ((FlowContextWidget) activeCallFrame).setTransitionHandler(getTransitionHandler());
      } else {
        activeCallFrame.setTransitionHandler(transitionHandler);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public <T> void addNestedEnvironmentEntry(ApplicationWidget scope, final Class<T> entryId, T envEntry) {
    Assert.notNullParam(scope, "scope");
    Assert.notNullParam(entryId, "entryId");
    pushGlobalEnvEntry(entryId, envEntry);
    BaseWidget scopedWidget = new BaseWidget() {

      @Override
      protected void destroy() throws Exception {
        popGlobalEnvEntry(entryId);
      }
    };
    ComponentUtil.addListenerComponent(scope, scopedWidget);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isNested() {
    return !this.callStack.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Widget> getNestedFlows() {
    List<Widget> nestedFlows = new ArrayList<Widget>(this.callStack.size());
    for (CallFrame callFrame : this.callStack) {
      nestedFlows.add(0, callFrame.getWidget());
    }
    return nestedFlows;
  }

  @Override
  protected void init() throws Exception {
    refreshGlobalEnvironment();

    if (this.top != null) {
      start(this.top);
      this.top = null;
    }

    addEventListener(FLOW_CANCEL_EVENT, new FlowCancelListener());
  }

  @Override
  protected void destroy() throws Exception {
    if (!this.callStack.isEmpty()) {
      this.callStack.removeFirst();
    }

    for (Iterator<CallFrame> i = this.callStack.iterator(); i.hasNext();) {
      CallFrame frame = i.next();
      i.remove();

      if (frame.getWidget().isAlive()) {
        frame.getWidget()._getComponent().destroy();
      }
    }
  }

  /**
   * Renders the top flow in the flow stack.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void render(OutputData output) throws Exception {
    // Don't render empty call stack:
    if (!getCallStack().isEmpty()) {
      CallFrame frame = this.callStack.getFirst();
      getWidget(frame.getName())._getWidget().render(output);
    }
  }

  // *******************************************************************
  // IMPL SPECIFIC PROTECTED METHODS
  // *******************************************************************

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), this.nestedEnvironmentEntries);
  }

  /**
   * A callback method for specifying custom environment entries for a flow environment.
   * 
   * @param nestedEnvironmentEntries A map to be filled with custom environment entries.
   */
  protected void putLocalEnvironmentEntries(Map<Class<?>, Object> nestedEnvironmentEntries) {
    nestedEnvironmentEntries.put(FlowContext.class, this);
  }

  /**
   * Creates a <tt>CallFrame</tt> with given data.
   * 
   * @param callable The flow for the new call frame (required).
   * @param configurator Optional configurator for the flow.
   * @param handler Optional flow callback that is called when the flow completes.
   * @return The created call frame.
   */
  protected CallFrame makeCallFrame(Widget callable, Configurator configurator, Handler<Object> handler) {
    return new CallFrame(callable, configurator, handler, getActiveCallFrame());
  }

  /**
   * Provides the currently active call frame.
   * 
   * @return The currently active call frame, or <code>null</code> when the flow stack is empty.
   */
  protected CallFrame getActiveCallFrame() {
    return this.callStack.isEmpty() ? null : this.callStack.getFirst();
  }

  /**
   * Provides the currently active flow.
   * 
   * @return The currently active flow, or <code>null</code> when the flow stack is empty.
   * @since 1.1
   */
  public Widget getActiveFlow() {
    CallFrame frame = getActiveCallFrame();
    return frame != null ? frame.getWidget() : null;
  }

  /**
   * Activates the widget represented by the call frame.
   * 
   * @param frame The call frame from which a flow must be added to the flow container.
   * @since 1.1
   */
  protected void addFrameWidget(CallFrame frame) {
    final Widget flow = frame.getWidget();
    addWidget(frame.getName(), flow);
  }

  /**
   * Method that invokes flow transition with given data on active flow. Sub-classes can override this method to tweak
   * what happens during transition.
   * 
   * @param transitionHandler The transition handler to invoke (not <code>null</code>).
   * @param transitionType The transition type taking place (not <code>null</code>).
   * @param closure The closure performing the transition.
   * @since 1.1
   */
  protected void doTransition(TransitionHandler transitionHandler, Transition transitionType, Closure closure) {
    transitionHandler.doTransition(transitionType, getActiveFlow(), closure);
  }

  /**
   * Method that processes the reset flow-transition. During reset, all flows will be closed with following exceptions:
   * <ul>
   * <li>flow completion handlers won't be executed
   * <li>the {@link #finishable} property won't be checked here
   * </ul>
   * (Note these exceptions may change in future.)
   * 
   * @param callback Optional callback to be executed when reset has been completed.
   * @since 1.1
   */
  protected void doReset(final EnvironmentAwareCallback callback) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Resetting flows '" + this.callStack + "'");
    }

    for (CallFrame frame : this.callStack) {
      _getChildren().put(frame.getName(), frame.getWidget());
      removeWidget(frame.getName());
    }

    this.callStack.clear();

    if (callback != null) {
      try {
        callback.call(getChildWidgetEnvironment());
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * Method that processes the start flow-transition. Note that flow handler will still be called when the given flow is
   * replaced. However, it won't be called when flow stack is reset.
   * 
   * @param flow The flow to replace the current flow with (required).
   * @param configurator Optional configurator to configure the flow once it has been initialized.
   * @param handler Optional handler that will be called when the given flow completes.
   * @since 1.1
   */
  protected void doStart(Widget flow, Configurator configurator, Handler<Object> handler) {
    Assert.notNullParam(flow, "flow");

    CallFrame previous = getActiveCallFrame();
    CallFrame frame = makeCallFrame(flow, configurator, handler);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Starting flow '" + flow.getClass().getName() + "'");
    }

    if (previous != null && _getChildren().get(previous.getName()) != null) {
      ((Widget) getChildren().get(previous.getName()))._getComponent().disable();
      _getChildren().remove(previous.getName());
    }

    this.callStack.addFirst(frame);
    addFrameWidget(frame);

    if (configurator != null) {
      try {
        configurator.configure(flow);
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * Method that processes the finish flow-transition. When this flow container is {@link #finishable} and is nested
   * inside another flow container, this flow container will finish with the same return-value as provided to this
   * method.
   * 
   * @param returnValue Optional return value from the finishing flow to the flow handler.
   * @since 1.1
   */
  protected void doFinish(Object returnValue) {
    if (this.callStack.isEmpty()) {
      throw new EmptyCallStackException();
    }

    CallFrame previousFrame = this.callStack.removeFirst();
    CallFrame frame = getActiveCallFrame();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Finishing flow '" + previousFrame.getWidget().getClass().getName() + "'");
    }

    removeWidget(previousFrame.getName());

    if (frame != null) {
      _getChildren().put(frame.getName(), frame.getWidget());
      getChildren().get(frame.getName())._getComponent().enable();
    }

    if (previousFrame.getHandler() != null) {
      try {
        previousFrame.getHandler().onFinish(returnValue);
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }

    if (this.finishable && this.callStack.isEmpty() && isAlive()) {
      FlowContext parentFlowContainer = EnvironmentUtil.getFlowContext(getEnvironment());
      if (parentFlowContainer != null) {
        parentFlowContainer.finish(returnValue);
      }
    }
  }

  /**
   * Method that processes the cancel flow-transition. When this flow container is {@link #finishable} and is nested
   * inside another flow container, this flow container will also cancel its flow.
   * 
   * @since 1.1
   */
  protected void doCancel() {
    if (this.callStack.isEmpty()) {
      throw new EmptyCallStackException();
    }

    CallFrame previousFrame = this.callStack.removeFirst();
    CallFrame frame = getActiveCallFrame();

    if (LOG.isDebugEnabled()) {
      LOG.debug("Cancelling flow '" + previousFrame.getWidget().getClass().getName() + "'");
    }

    removeWidget(previousFrame.getName());

    if (frame != null) {
      _getChildren().put(frame.getName(), frame.getWidget());
      getChildren().get(frame.getName())._getComponent().enable();
    }

    if (previousFrame.getHandler() != null) {
      try {
        previousFrame.getHandler().onCancel();
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }

    if (this.finishable && this.callStack.isEmpty() && isAlive()) {
      FlowContext parentFlowContainer = EnvironmentUtil.getFlowContext(getEnvironment());
      if (parentFlowContainer != null) {
        parentFlowContainer.cancel();
      }
    }
  }

  /**
   * Method that processes the replace flow-transition. Note that the replacing flow will inherit the flow handler of
   * the previously active flow.
   * 
   * @param flow The flow to replace the current flow with (required).
   * @param configurator Optional configurator to configure the flow once it has been initialized.
   * @since 1.1
   */
  protected void doReplace(Widget flow, Configurator configurator) {
    Assert.notNullParam(flow, "flow");

    CallFrame previousFrame = this.callStack.removeFirst();
    CallFrame frame = makeCallFrame(flow, configurator, previousFrame.getHandler());

    if (LOG.isDebugEnabled()) {
      LOG.debug("Replacing flow '" + previousFrame.getWidget().getClass().getName() + "' with flow '"
          + flow.getClass().getName() + "'");
    }

    removeWidget(previousFrame.getName());
    this.callStack.addFirst(frame);
    addFrameWidget(frame);

    if (configurator != null) {
      try {
        configurator.configure(flow);
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * Provides access to the current call stack, where the first item (call frame) is the active one.
   * 
   * @return A list of current call stack
   */
  protected LinkedList<CallFrame> getCallStack() {
    return this.callStack;
  }

  // *******************************************************************
  // PRIVATE METHODS
  // *******************************************************************

  private void refreshGlobalEnvironment() {
    this.nestedEnvironmentEntries.clear();
    putLocalEnvironmentEntries(this.nestedEnvironmentEntries);

    for (Map.Entry<Class<?>, LinkedList<Object>> entry : this.nestedEnvEntryStacks.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        this.nestedEnvironmentEntries.put(entry.getKey(), entry.getValue().getFirst());
      }
    }
  }

  private LinkedList<Object> getEnvEntryStack(Class<?> entryId) {
    LinkedList<Object> envEntryStack = this.nestedEnvEntryStacks.get(entryId);
    if (envEntryStack == null) {
      envEntryStack = new LinkedList<Object>();
      this.nestedEnvEntryStacks.put(entryId, envEntryStack);
    }
    return envEntryStack;
  }

  private void pushGlobalEnvEntry(Class<?> entryId, Object envEntry) {
    getEnvEntryStack(entryId).addFirst(envEntry);
    refreshGlobalEnvironment();
  }

  private void popGlobalEnvEntry(Class<?> entryId) {
    getEnvEntryStack(entryId).removeFirst();
    refreshGlobalEnvironment();
  }

  /**
   * A widget, configurator and a handler are encapsulated into one logical structure, a call frame.
   */
  public static class CallFrame implements Serializable {

    private final Widget widget;

    private final Configurator configurator;

    private final Handler<Object> handler;

    private String name;

    private TransitionHandler transitionHandler;

    /**
     * Creates a new call frame.
     * 
     * @param widget The flow widget (required).
     * @param configurator The flow configurator (optional)
     * @param handler The handler that will be called when the flow finishes.
     * @param previous The previous call frame (<code>null</code> when this call frame is the first one).
     */
    protected CallFrame(Widget widget, Configurator configurator, Handler<Object> handler, CallFrame previous) {
      this.configurator = configurator;
      this.handler = handler;
      this.widget = widget;
      if (previous == null) {
        this.name = TOP_FLOW_KEY;
      } else {
        this.name = BASE_FLOW_KEY + (Integer.parseInt(previous.getName().substring(BASE_FLOW_KEY.length())) + 1);
      }
    }

    /**
     * Provides the configurator of this call frame.
     * 
     * @return The current call frame configurator, or <code>null</code> when none was specified.
     */
    public Configurator getConfigurator() {
      return this.configurator;
    }

    /**
     * Provides the flow handler of this call frame.
     * 
     * @return The current call frame handler, or <code>null</code> when none was specified.
     */
    public Handler<Object> getHandler() {
      return this.handler;
    }

    /**
     * Provides the flow widget of this call frame.
     * 
     * @return The current call frame flow.
     */
    public Widget getWidget() {
      return this.widget;
    }

    /**
     * Provides the name (ID) of the current flow.
     * 
     * @return Unique child key for contained flow widget.
     * @since 1.1
     */
    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.widget.getClass().getName();
    }

    /**
     * Provides the known transition handler of this call frame.
     * 
     * @return A transition handler of this call frame, or <code>null</code>.
     */
    public TransitionHandler getTransitionHandler() {
      return this.transitionHandler;
    }

    /**
     * Method for specifying the transition handler of this call frame.
     * 
     * @param transitionHandler A transition handler of this call frame.
     */
    protected void setTransitionHandler(TransitionHandler transitionHandler) {
      this.transitionHandler = transitionHandler;
    }
  }

  // ******************************************************************************
  // Protected Closure classes for executing flow navigation events from callbacks.
  // ******************************************************************************

  /**
   * A closure that handles starting a flow.
   * 
   * @since 1.1
   */
  private class StartClosure implements Closure, Serializable {

    private final Widget flow;

    private final Configurator configurator;

    private final Handler<Object> handler;

    /**
     * Creates a closure that start a new flow.
     * 
     * @param flow The flow to start (required).
     * @param configurator A configurator that configures the flow once it's started (optional).
     * @param handler A handler that should be called once the flow completes.
     */
    public StartClosure(Widget flow, Configurator configurator, Handler<Object> handler) {
      this.flow = flow;
      this.configurator = configurator;
      this.handler = handler;
    }

    /**
     * {@inheritDoc}
     */
    public void execute(Object obj) {
      doStart(this.flow, this.configurator, this.handler);
    }
  }

  /**
   * A closure that handles replacing a flow with another flow.
   * 
   * @since 1.1
   */
  private class ReplaceClosure implements Closure, Serializable {

    private Widget flow;

    private Configurator configurator;

    /**
     * Creates a closure that replaces current flow with a new flow.
     * 
     * @param flow The flow to replace the currently active flow (required).
     * @param configurator A configurator that configures the flow once it's started (optional).
     */
    public ReplaceClosure(Widget flow, Configurator configurator) {
      this.flow = flow;
      this.configurator = configurator;
    }

    /**
     * {@inheritDoc}
     */
    public void execute(Object obj) {
      doReplace(this.flow, this.configurator);
    }
  }

  /**
   * A closure that handles finishing a flow, making the previous flow active again.
   * 
   * @since 1.1
   */
  private class FinishClosure implements Closure, Serializable {

    private Object result;

    /**
     * Creates a closure that finishes the current flow.
     * 
     * @param result An optional return value to be passed back to the previous flow.
     */
    public FinishClosure(Object result) {
      this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    public void execute(Object obj) {
      doFinish(this.result);
    }
  }

  /**
   * A closure that handles canceling a flow, making the previous flow active again.
   * 
   * @since 1.1
   */
  private class CancelClosure implements Closure, Serializable {

    /**
     * {@inheritDoc}
     */
    public void execute(Object obj) {
      doCancel();
    }
  }

  /**
   * A closure that removes all flows.
   * 
   * @since 1.1
   */
  private class ResetClosure implements Closure, Serializable {

    private EnvironmentAwareCallback callback;

    /**
     * Creates a closure that resets the flow stack (all flows will be removed).
     * 
     * @param callback Optional callback to be called after flow context is reset.
     */
    public ResetClosure(EnvironmentAwareCallback callback) {
      this.callback = callback;
    }

    /**
     * {@inheritDoc}
     */
    public void execute(Object obj) {
      doReset(this.callback);
    }
  }

  /**
   * Standard implementation of a flow transition handler.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   */
  public static class StandardTransitionHandler implements FlowContext.TransitionHandler {

    /**
     * {@inheritDoc}
     */
    public void doTransition(Transition transitionType, Widget activeFlow, Closure transition) {
      notifyScrollContext(transitionType, activeFlow);
      transition.execute(activeFlow);
    }

    /**
     * Handles built-in {@link WindowScrollPositionContext} for adding/updating/removing window scroll positions.
     * 
     * @param transitionType The current flow transition type.
     * @param activeFlow The currently active flow (may be <code>null</code>).
     */
    protected void notifyScrollContext(Transition transitionType, Widget activeFlow) {
      if (activeFlow == null) {
        return;
      }
      WindowScrollPositionContext scrollCtx = activeFlow.getEnvironment().getEntry(WindowScrollPositionContext.class);
      if (scrollCtx != null) {
        switch (transitionType) {
          case START:
            scrollCtx.push();
            break;
          case FINISH:
          case CANCEL:
            scrollCtx.pop();
            break;
          case REPLACE:
            scrollCtx.resetCurrent();
            break;
          case RESET:
            scrollCtx.reset();
            break;
          default:
            throw new AraneaRuntimeException("Unsupported transition: " + transitionType);
        }
      }
    }
  }

  /**
   * The listener expects a positive integer indicating the number of last flows to cancel. Permission to do so must be
   * granted by {@link FlowContextWidget#setAllowFlowCancelEvent(boolean)}, which is enabled by default.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @see FlowContextWidget#FLOW_CANCEL_EVENT
   * @see FlowContextWidget#setAllowFlowCancelEvent(boolean)
   * @since 2.0
   */
  protected class FlowCancelListener extends StandardEventListener {

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) {
      if (StandardFlowContainerWidget.this.allowFlowCancelEvent && StringUtils.isNumeric(eventParam)) {
        int times = Integer.parseInt(eventParam);
        while (times-- > 0 && isNested()) {
          cancel();
        }
      }
    }

  }
}
