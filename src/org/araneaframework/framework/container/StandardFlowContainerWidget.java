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
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.EmptyCallStackException;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowContextWidget;
import org.araneaframework.http.ContainerStateContext;
import org.araneaframework.http.WindowScrollPositionContext;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * A {@link org.araneaframework.framework.FlowContext} where the flows are structured as a stack.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
@SuppressWarnings("unchecked")
public class StandardFlowContainerWidget extends BaseApplicationWidget implements FlowContextWidget {

  private static final Log LOG = LogFactory.getLog(StandardFlowContainerWidget.class);

  private static final String BASE_FLOW_KEY = "f";

  private static final String TOP_FLOW_KEY = BASE_FLOW_KEY + 0;

  /**
   * The stack of all the calls.
   */
  protected LinkedList<CallFrame> callStack = new LinkedList<CallFrame>();

  /**
   * The top callable widget.
   */
  protected Widget top;

  protected boolean finishable = true;

  private Map<Class<?>, Object> nestedEnvironmentEntries = new HashMap<Class<?>, Object>();

  private Map<Class<?>, LinkedList<Object>> nestedEnvEntryStacks = new HashMap<Class<?>, LinkedList<Object>>();

  /**
   * Constructs a {@link StandardFlowContainerWidget} with <code>topWidget</code> being the first flow on the top of
   * flow stack.
   */
  public StandardFlowContainerWidget(Widget topWidget) {
    this.top = topWidget;
  }

  public StandardFlowContainerWidget() {}

  public void setTop(Widget topWidget) {
    this.top = topWidget;
  }

  /**
   * Determines whether this {@link StandardFlowContainerWidget} will ever return control to {@link FlowContext} higher
   * in the {@link org.araneaframework.Component} hierarchy. If such {@link FlowContext} exists and finishable is set to
   * true, this {@link StandardFlowContainerWidget} will return control to it when last running flow inside it is
   * finished ({@link FlowContext#finish(Object)}) or canceled ({@link FlowContext#cancel()}). Default is
   * <code>true</code>.
   * 
   * @param finishable
   * @since 1.1
   */
  public void setFinishable(boolean finishable) {
    this.finishable = finishable;
  }

  public void start(Widget flow) {
    start(flow, null, null);
  }

  public void start(Widget flow, Handler handler) {
    start(flow, null, handler);
  }

  public void start(Widget flow, Configurator configurator, Handler handler) {
    TransitionHandler transitionHandler = getTransitionHandler();
    StartClosure startClosure = new StartClosure(flow, configurator, handler);
    doTransition(transitionHandler, FlowContext.TRANSITION_START, startClosure);
  }

  public void replace(Widget flow) {
    replace(flow, null);
  }

  public void replace(Widget flow, Configurator configurator) {
    TransitionHandler transitionHandler = getTransitionHandler();
    ReplaceClosure replaceClosure = new ReplaceClosure(flow, configurator);
    doTransition(transitionHandler, FlowContext.TRANSITION_REPLACE, replaceClosure);
  }

  public void finish(Object returnValue) {
    TransitionHandler transitionHandler = getTransitionHandler();
    FinishClosure finishClosure = new FinishClosure(returnValue);
    doTransition(transitionHandler, FlowContext.TRANSITION_FINISH, finishClosure);
  }

  public void cancel() {
    TransitionHandler transitionHandler = getTransitionHandler();
    CancelClosure cancelClosure = new CancelClosure();
    doTransition(transitionHandler, FlowContext.TRANSITION_CANCEL, cancelClosure);
  }

  public void reset(final EnvironmentAwareCallback callback) {
    TransitionHandler transitionHandler = getTransitionHandler();
    ResetClosure resetClosure = new ResetClosure(callback);
    doTransition(transitionHandler, FlowContext.TRANSITION_RESET, resetClosure);
  }

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

  public <T> void addNestedEnvironmentEntry(ApplicationWidget scope, final Class<T> entryId, T envEntry) {
    Assert.notNullParam(scope, "scope");
    Assert.notNullParam(entryId, "entryId");
    pushGlobalEnvEntry(entryId, envEntry);
    BaseWidget scopedWidget = new BaseWidget() {

      @Override
      protected void destroy() {
        popGlobalEnvEntry(entryId);
      }
    };
    ComponentUtil.addListenerComponent(scope, scopedWidget);
  }

  public boolean isNested() {
    return !this.callStack.isEmpty();
  }

  @Override
  protected void init() throws Exception {
    super.init();
    refreshGlobalEnvironment();

    if (this.top != null) {
      start(this.top);
      this.top = null;
    }
  }

  @Override
  protected void destroy() {
    if (!this.callStack.isEmpty()) {
      this.callStack.removeFirst();
    }

    for (Iterator<CallFrame> i = this.callStack.iterator(); i.hasNext();) {
      CallFrame frame = i.next();
      i.remove();
      frame.getWidget()._getComponent().destroy();
    }

    super.destroy();
  }

  /**
   * Invokes render on the top frame on the stack of callframes.
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

  protected void putLocalEnvironmentEntries(Map<Class<?>, Object> nestedEnvironmentEntries) {
    nestedEnvironmentEntries.put(FlowContext.class, this);
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), this.nestedEnvironmentEntries);
  }

  /**
   * Returns a new CallFrame constructed of the callable, configurator and handler.
   */
  protected CallFrame makeCallFrame(Widget callable, Configurator configurator, Handler<Object> handler,
      CallFrame previous) {
    return new CallFrame(callable, configurator, handler, previous);
  }

  protected CallFrame getActiveCallFrame() {
    return this.callStack.isEmpty() ? null : this.callStack.getFirst();
  }

  /** @since 1.1 */
  public Widget getActiveFlow() {
    CallFrame frame = getActiveCallFrame();
    return frame != null ? frame.getWidget() : null;
  }

  /**
   * Activates the widget represented by the {@link CallFrame}.
   * 
   * @since 1.1
   */
  protected void addFrameWidget(CallFrame frame) {
    final Widget flow = frame.getWidget();
    addWidget(frame.getName(), flow);
  }

  /** @since 1.1 */
  protected void doTransition(TransitionHandler transitionHandler, int transitionType, Closure closure) {
    transitionHandler.doTransition(transitionType, getActiveFlow(), closure);
  }

  /** @since 1.1 */
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

  /** @since 1.1 */
  protected void doStart(Widget flow, Configurator configurator, Handler<Object> handler) {
    Assert.notNullParam(flow, "flow");

    CallFrame previous = getActiveCallFrame();
    CallFrame frame = makeCallFrame(flow, configurator, handler, previous);

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

  /** @since 1.1 */
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
      previousFrame.getHandler().onFinish(returnValue);
    }

    if (this.finishable && this.callStack.isEmpty()) {
      FlowContext parentFlowContainer = EnvironmentUtil.getFlowContext(getEnvironment());
      if (parentFlowContainer != null) {
        parentFlowContainer.finish(returnValue);
      }
    }
  }

  /** @since 1.1 */
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
      previousFrame.getHandler().onCancel();
    }

    if (this.finishable && this.callStack.isEmpty()) {
      FlowContext parentFlowContainer = EnvironmentUtil.getFlowContext(getEnvironment());
      if (parentFlowContainer != null) {
        parentFlowContainer.cancel();
      }
    }
  }

  /** @since 1.1 */
  protected void doReplace(Widget flow, Configurator configurator) {
    Assert.notNullParam(flow, "flow");

    CallFrame previousFrame = this.callStack.removeFirst();
    CallFrame frame = makeCallFrame(flow, configurator, previousFrame.getHandler(), previousFrame);

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
   * A widget, configurator and a handler are encapsulated into one logical structure, a call frame. Class is used
   * internally.
   */
  protected static class CallFrame implements Serializable {

    private Widget widget;

    private Configurator configurator;

    private Handler<Object> handler;

    private String name;

    private TransitionHandler transitionHandler;
    private String pageTitle;
    private String componentTitle;

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

    public Configurator getConfigurator() {
      return this.configurator;
    }

    public Handler<Object> getHandler() {
      return this.handler;
    }

    public Widget getWidget() {
      return this.widget;
    }

    /**
     * @return unique child key for contained {@link Widget}.
     * @since 1.1
     */
    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.widget.getClass().getName();
    }

    public TransitionHandler getTransitionHandler() {
      return this.transitionHandler;
    }

    protected void setTransitionHandler(TransitionHandler transitionHandler) {
      this.transitionHandler = transitionHandler;
    }

    public String getPageTitle() {
      return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
      this.pageTitle = pageTitle;
    }

    public String getComponentTitle() {
      return componentTitle;
    }

    public void setComponentTitle(String componentTitle) {
      this.componentTitle = componentTitle;
    }
  }

  /*
   * Protected Closure classes for executing flow navigation events from callbacks.
   */
  /** @since 1.1 */
  protected class CancelClosure implements Closure, Serializable {

    public void execute(Object obj) {
      doCancel();
    }
  }

  /** @since 1.1 */
  protected class ResetClosure implements Closure, Serializable {

    protected EnvironmentAwareCallback callback;

    public ResetClosure(EnvironmentAwareCallback callback) {
      this.callback = callback;
    }

    public void execute(Object obj) {
      doReset(this.callback);
    }
  }

  /** @since 1.1 */
  protected class FinishClosure implements Closure, Serializable {

    protected Object result;

    public FinishClosure(Object result) {
      this.result = result;
    }

    public void execute(Object obj) {
      doFinish(this.result);
    }
  }

  /** @since 1.1 */
  protected class ReplaceClosure implements Closure, Serializable {

    protected Widget flow;

    protected Configurator configurator;

    public ReplaceClosure(Widget flow, Configurator configurator) {
      this.flow = flow;
      this.configurator = configurator;
    }

    public void execute(Object obj) {
      doReplace(this.flow, this.configurator);
    }
  }

  /** @since 1.1 */
  protected class StartClosure implements Closure, Serializable {

    protected Widget flow;

    protected Configurator configurator;

    protected Handler<Object> handler;

    public StartClosure(Widget flow, Configurator configurator, Handler<Object> handler) {
      this.flow = flow;
      this.configurator = configurator;
      this.handler = handler;
    }

    public void execute(Object obj) {
      doStart(this.flow, this.configurator, this.handler);
    }
  }

  public static class StandardTransitionHandler implements FlowContext.TransitionHandler {

    public void doTransition(int transitionType, Widget activeFlow, Closure transition) {
      notifyContexts(transitionType, activeFlow);
      transition.execute(activeFlow);
    }

    protected void notifyContexts(int transitionType, Widget activeFlow) {
      if (activeFlow == null) {
        return;
      }
      WindowScrollPositionContext scrollCtx = activeFlow.getEnvironment().getEntry(WindowScrollPositionContext.class);
      ContainerStateContext containerStateContext = activeFlow.getEnvironment().getEntry(ContainerStateContext.class);
      if (scrollCtx != null) {
        switch (transitionType) {
        case FlowContext.TRANSITION_START:
          scrollCtx.push();
          containerStateContext.push();
          break;
        case FlowContext.TRANSITION_FINISH:
        case FlowContext.TRANSITION_CANCEL:
          scrollCtx.pop();
          containerStateContext.pop();
          break;
        case FlowContext.TRANSITION_REPLACE:
          scrollCtx.resetCurrent();
          containerStateContext.resetCurrent();
          break;
        case FlowContext.TRANSITION_RESET:
          scrollCtx.reset();
          containerStateContext.reset();
          break;
        }
      }
    }
  }

  public void setComponentTitle(String componentTitle) {
    if(callStack.isEmpty()) {
      return;
    }
    CallFrame frame = callStack.getFirst();
    frame.setComponentTitle(componentTitle);
    
  }

  public void setPageTitle(String pageTitle) {
    if(callStack.isEmpty()) {
      return;
    }
    CallFrame frame = callStack.getFirst();
    frame.setPageTitle(pageTitle);
  }

  public String getComponentTitle() {
    if(callStack.isEmpty()) {
      return "";
    }
    CallFrame frame = callStack.getFirst();
    return frame.getComponentTitle();
  }

  public String getPageTitle() {
    if(callStack.isEmpty()) {
      return "";
    }
    CallFrame frame = callStack.getFirst();
    return frame.getPageTitle();
  }
  
  public List<String> getComponentTitles(){
    List<String> titles = new ArrayList<String>();
    for(CallFrame callFrame : getCallStack()) {
      titles.add(0, StringUtils.defaultString(callFrame.getComponentTitle()));
    }
    return titles;
  }
}
