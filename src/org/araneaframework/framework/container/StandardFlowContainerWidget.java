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

package org.araneaframework.framework.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.ChainedClosure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.EventListener;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.EmptyCallStackException;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowContextWidget;
import org.araneaframework.framework.FlowEventAutoConfirmationContext;
import org.araneaframework.framework.FlowEventAutoConfirmationContext.ConfirmationCondition;
import org.araneaframework.uilib.core.FlowEventConfirmationWidget;

/**
 * A {@link org.araneaframework.framework.FlowContext} where the flows are structured as a stack.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardFlowContainerWidget extends BaseApplicationWidget implements FlowContextWidget {
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  private static final Log log = LogFactory.getLog(StandardFlowContainerWidget.class);
  
  private static final String BASE_FLOW_KEY = "f";
  private static final String TOP_FLOW_KEY = BASE_FLOW_KEY + 0;
  
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  /**
   * The stack of all the calls.
   */
  protected LinkedList callStack = new LinkedList();
  /**
   * The top callable widget.
   */
  protected Widget top;
  protected boolean finishable = true;
  
  protected Widget flowEventConfirmationWidget;

  private Map nestedEnvironmentEntries = new HashMap();
  private Map nestedEnvEntryStacks = new HashMap();

  //*******************************************************************
  // CONSTRUCTORS
  //*******************************************************************
  
  /**
   * Constructs a {@link StandardFlowContainerWidget} with <code>topWidget</code> 
   * being the first flow on the top of flow stack.
   */
  public StandardFlowContainerWidget(Widget topWidget) {
    this.top = topWidget;
  }

  public StandardFlowContainerWidget() {
  }
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  
  public void setTop(Widget topWidget) {
    this.top = topWidget;
  }
  
  /**
   * Determines whether this {@link StandardFlowContainerWidget} will ever
   * return control to {@link FlowContext} higher in the {@link org.araneaframework.Component}
   * hierarchy. If such {@link FlowContext} exists and finishable is set to true, this
   * {@link StandardFlowContainerWidget} will return control to it when last running flow
   * inside it is finished ({@link FlowContext#finish(Object)}) or canceled ({@link FlowContext#cancel()}).
   * 
   * Default is <code>true</code>.
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
    FlowEventAutoConfirmationContext confirmationCtx = getActiveFlowEventAutoConfirmationContext();
    ConfirmationCondition condition = confirmationCtx != null ? confirmationCtx.getCondition() : null;
	if (condition != null && shouldConfirm(condition.getStartPredicate())) {
      doConfirm(new StartClosure(flow, configurator, handler));
    } else {
      doStart(flow, configurator, handler);
    }
  }

  public void replace(Widget flow) {
    replace(flow, null);
  }

  public void replace(Widget flow, Configurator configurator) {
	FlowEventAutoConfirmationContext confirmationCtx = getActiveFlowEventAutoConfirmationContext();
	ConfirmationCondition condition = confirmationCtx != null ? confirmationCtx.getCondition() : null;
    if (condition != null && shouldConfirm(condition.getReplacePredicate())) {
      doConfirm(new ReplaceClosure(flow, configurator));
    } else {
      doReplace(flow, configurator);
    }
  }

  public void finish(Object returnValue) {
	FlowEventAutoConfirmationContext confirmationCtx = getActiveFlowEventAutoConfirmationContext();
	ConfirmationCondition condition = confirmationCtx != null ? confirmationCtx.getCondition() : null;
    if (condition != null && shouldConfirm(condition.getFinishPredicate())) {
      doConfirm(new FinishClosure(returnValue));
    } else {
      doFinish(returnValue);
    }
  }

  public void cancel() {
	FlowEventAutoConfirmationContext confirmationCtx = getActiveFlowEventAutoConfirmationContext();
	ConfirmationCondition condition = confirmationCtx != null ? confirmationCtx.getCondition() : null;
    if (condition != null && shouldConfirm(condition.getCancelPredicate())) {
      doConfirm(new CancelClosure());
    } else {
      doCancel();
    }
  }

  public void reset(final EnvironmentAwareCallback callback) {
	FlowEventAutoConfirmationContext confirmationCtx = getActiveFlowEventAutoConfirmationContext();
	ConfirmationCondition condition = confirmationCtx != null ? confirmationCtx.getCondition() : null;
	
    if (condition != null && shouldConfirm(condition.getResetPredicate())) {
	  doConfirm(new ResetClosure(callback));
    } else {
      doReset(callback);
    }
  }

  public FlowContext.FlowReference getCurrentReference() {
  	return new FlowReference();
  }
  
  public void addNestedEnvironmentEntry(ApplicationWidget scope, final Object entryId, Object envEntry) {
    Assert.notNullParam(scope, "scope");
    Assert.notNullParam(entryId, "entryId");
    
    pushGlobalEnvEntry(entryId, envEntry);
    
    BaseWidget scopedWidget = new BaseWidget() {
      protected void destroy() throws Exception {
        popGlobalEnvEntry(entryId);
      }
    };
    ComponentUtil.addListenerComponent(scope, scopedWidget);
  }
  
  public boolean isNested() {
    return callStack.size() != 0;
  }
  
  //*******************************************************************
  // PROTECTED LIFECYCLE METHODS
  //*******************************************************************
  
  protected void init() throws Exception {
    super.init();
            
    refreshGlobalEnvironment();
    
    if (top != null) {
      start(top, null, null);
      top = null;
    }
  }
  
  protected void destroy() throws Exception {
    if (callStack.size() > 0)
      callStack.removeFirst();
    
    for (Iterator i = callStack.iterator(); i.hasNext();) {
      CallFrame frame = (CallFrame) i.next();
      i.remove();
      
      frame.getWidget()._getComponent().destroy();          
    }
    
    super.destroy();    
  }
  
  /**
   * Invokes render on the top frame on the stack of callframes.
   */
  protected void render(OutputData output) throws Exception {
    //Don't render empty callstack
    if (getCallStack().size() == 0) return; 
    
    CallFrame frame = (CallFrame) callStack.getFirst();
    
    getWidget(frame.getName())._getWidget().render(output);
    if (flowEventConfirmationWidget != null)
      flowEventConfirmationWidget._getWidget().render(output);
  }
  
  //*******************************************************************
  // IMPL SPECIFIC PROTECTED METHODS
  //*******************************************************************
  protected void putLocalEnvironmentEntries(Map nestedEnvironmentEntries) {
    nestedEnvironmentEntries.put(FlowContext.class, this);
  }
  
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(getEnvironment(), nestedEnvironmentEntries);
  }
  
  /**
   * Returns a new CallFrame constructed of the callable, configurator and handler.
   */
  protected CallFrame makeCallFrame(Widget callable, Configurator configurator, Handler handler, CallFrame previous) {
    return new CallFrame(callable, configurator, handler, previous);
  }
  
  /** @since 1.1 */
  protected FlowEventAutoConfirmationContext getActiveFlowEventAutoConfirmationContext() {
    LinkedList envEntryStack = getEnvEntryStack(FlowEventAutoConfirmationContext.class);
    if (envEntryStack.isEmpty()) 
      return null;

    return (FlowEventAutoConfirmationContext) envEntryStack.getFirst();
  }

  /**
   * Activates the widget represented by the {@link CallFrame}. 
   * @since 1.1 */
  protected void addFrameWidget(CallFrame frame) {
    final Widget flow = frame.getWidget();
    addNestedEnvironmentEntry((ApplicationWidget) flow, FlowEventAutoConfirmationContext.class, new FlowEventAutoConfirmationContextImpl());
	addWidget(frame.getName(), flow);
  }

  /** @since 1.1 */
  protected boolean shouldConfirm(Predicate p) {
    if (p != null) {
      return p.evaluate(((CallFrame)callStack.getFirst()).getWidget());
    }
    return false;
  }
  
  /** @since 1.1 */
  protected void doConfirm(Closure onNavigationConfirmed) {
    FlowEventConfirmationEventListener navigationConfirmationListener = new FlowEventConfirmationEventListener();
    ConfirmationCleanupClosure unconfirmed = new ConfirmationCleanupClosure(navigationConfirmationListener);
	ChainedClosure confirmed = new ChainedClosure(new Closure[] {onNavigationConfirmed, unconfirmed } );
    navigationConfirmationListener.setPositive(confirmed);
    navigationConfirmationListener.setNegative(unconfirmed);
  
	addEventListener("flowEventConfirmation", navigationConfirmationListener);

    flowEventConfirmationWidget = new FlowEventConfirmationWidget("Do it now?");
    addWidget("flowEventConfirmationWidget", flowEventConfirmationWidget);
  }
  
  protected void removeFlowEventConfirmationWidget() {
    removeWidget("flowEventConfirmationWidget");
    flowEventConfirmationWidget = null;
  }
  
  /** @since 1.1 */
  protected void doReset(final EnvironmentAwareCallback callback) {
	if (log.isDebugEnabled())
      log.debug("Resetting flows '" + callStack + "'");
    
    for (Iterator i = callStack.iterator(); i.hasNext();) {
      CallFrame frame = (CallFrame) i.next();
      
      _getChildren().put(frame.getName(), frame.getWidget());
      removeWidget(frame.getName());
    }
    
    callStack.clear();
    
    if (callback != null) try {
      callback.call(getChildWidgetEnvironment());
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }
  
  /** @since 1.1 */
  protected void doStart(Widget flow, Configurator configurator, Handler handler) {
	Assert.notNullParam(flow, "flow");

    CallFrame previous = callStack.size() == 0 ? null : (CallFrame) callStack.getFirst();
    CallFrame frame = makeCallFrame(flow, configurator, handler, previous);
    
    if (log.isDebugEnabled())
      log.debug("Starting flow '" + flow.getClass().getName() +"'");
    
    if (previous != null && _getChildren().get(previous.getName()) != null) {
      ((Widget) getChildren().get(previous.getName()))._getComponent().disable();
      _getChildren().remove(previous.getName());
    }
    
    callStack.addFirst(frame);
    
    addFrameWidget(frame);

    if (configurator != null) {
      try {
        configurator.configure(flow);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }
  
  /** @since 1.1 */
  protected void doFinish(Object returnValue) {
	if (callStack.size() == 0)
      throw new EmptyCallStackException();
    
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = callStack.size() > 0 ? (CallFrame) callStack.getFirst() : null;
    
    if (log.isDebugEnabled())
      log.debug("Finishing flow '" + previousFrame.getWidget().getClass().getName() + "'");
    
    removeWidget(previousFrame.getName());
    if (frame != null) {
      _getChildren().put(frame.getName(), frame.getWidget());
      ((Component) getChildren().get(frame.getName()))._getComponent().enable();
    }
    
    if (previousFrame.getHandler() != null) {
      try {
        previousFrame.getHandler().onFinish(returnValue);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }

    if (finishable && callStack.size() == 0) {
      FlowContext parentFlowContainer = (FlowContext) getEnvironment().getEntry(FlowContext.class);
      if (parentFlowContainer != null) {
        parentFlowContainer.finish(returnValue);
      }
    }
  }
  
  /** @since 1.1 */
  protected void doCancel() {
	if (callStack.size() == 0)
      throw new EmptyCallStackException();
    
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = callStack.size() > 0 ? (CallFrame) callStack.getFirst() : null;
    
    if (log.isDebugEnabled())
      log.debug("Cancelling flow '" + previousFrame.getWidget().getClass().getName() + "'");
    
    removeWidget(previousFrame.getName());
    if (frame != null) {
      _getChildren().put(frame.getName(), frame.getWidget());    
      ((Component) getChildren().get(frame.getName()))._getComponent().enable();
    }
    
    if (previousFrame.getHandler() != null) try {
      previousFrame.getHandler().onCancel();
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }

    if (finishable && callStack.size() == 0) {
      FlowContext parentFlowContainer = (FlowContext) getEnvironment().getEntry(FlowContext.class);
      if (parentFlowContainer != null) {
        parentFlowContainer.cancel();
      }
    }
  }

  /** @since 1.1 */
  protected void doReplace(Widget flow, Configurator configurator) {
	Assert.notNullParam(flow, "flow");
    
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = makeCallFrame(flow, configurator, previousFrame.getHandler(), previousFrame);
    
    if (log.isDebugEnabled())
      log.debug("Replacing flow '" + previousFrame.getWidget().getClass().getName() + 
        "' with flow '" + flow.getClass().getName() + "'");
    
    removeWidget(previousFrame.getName());
    
    callStack.addFirst(frame);    
    
    addFrameWidget(frame);

    if (configurator != null) {
      try {
        configurator.configure(flow);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }
  
  protected LinkedList getCallStack() {
    return callStack;
  }

  //*******************************************************************
  // PRIVATE METHODS
  //*******************************************************************
  private void refreshGlobalEnvironment() {
    nestedEnvironmentEntries.clear();
    
    putLocalEnvironmentEntries(nestedEnvironmentEntries);

    for (Iterator i = nestedEnvEntryStacks.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      Object entryId = entry.getKey();
      LinkedList stack = (LinkedList) entry.getValue();
      if (stack.size() > 0) {
        Object envEntry = stack.getFirst();
        nestedEnvironmentEntries.put(entryId, envEntry);
      }
    }    
  }

  private LinkedList getEnvEntryStack(Object entryId) {
    LinkedList envEntryStack = (LinkedList) nestedEnvEntryStacks.get(entryId);
    
    if (envEntryStack == null) {
      envEntryStack = new LinkedList();
      nestedEnvEntryStacks.put(entryId, envEntryStack);
    }
    
    return envEntryStack;
  }
  
  private void pushGlobalEnvEntry(Object entryId, Object envEntry) {
    getEnvEntryStack(entryId).addFirst(envEntry);
    
    refreshGlobalEnvironment();
  }
  
  private void popGlobalEnvEntry(Object entryId) {
    getEnvEntryStack(entryId).removeFirst();
    
    refreshGlobalEnvironment();
  }
  
  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
  
  protected class FlowReference implements FlowContext.FlowReference {
    private int currentDepth = StandardFlowContainerWidget.this.callStack.size();
  	
    public void reset(EnvironmentAwareCallback callback) throws Exception {
      Iterator i = callStack.iterator();
      while (i.hasNext() && callStack.size() > currentDepth) {
        CallFrame frame = (CallFrame) i.next();
        
        _getChildren().put(frame.getName(), frame.getWidget());
        removeWidget(frame.getName());
        
        i.remove();
      }
      
      if (callStack.size() > 0) {
        CallFrame frame = (CallFrame) callStack.getFirst();
        _getChildren().put(frame.getName(), frame.getWidget());
        ((Component) getChildren().get(frame.getName()))._getComponent().enable();
      }

      callback.call(getChildWidgetEnvironment());
    }  	
  }
  
  /**
   * A widget, configurator and a handler are encapsulated into one logical structure,
   * a call frame. Class is used internally.
   */
  protected static class CallFrame implements Serializable {
    private Widget widget;
    private Configurator configurator;
    private Handler handler;
    private String name;
    
    protected CallFrame(Widget widget, Configurator configurator, Handler handler, CallFrame previous) {
      this.configurator = configurator;
      this.handler = handler;
      this.widget = widget;
      
      if (previous == null)
        name = TOP_FLOW_KEY;
      else {
        name = BASE_FLOW_KEY + (Integer.parseInt(previous.getName().substring(BASE_FLOW_KEY.length())) + 1);
      }
    }

    public Configurator getConfigurator() {
      return configurator;
    }

    public Handler getHandler() {
      return handler;
    }

    public Widget getWidget() {
      return widget;
    }
    
    /**
     * @return unique child key for contained {@link Widget}. 
     * @since 1.1
     */
    public String getName() {
      return name;
    }

    public String toString() {
      return widget.getClass().getName();
    }
  }

  /* Protected Closure classes for executing flow navigation events from callbacks. */
  /** @since 1.1 */
  protected class CancelClosure implements Closure, Serializable {
    private static final long serialVersionUID = 1L;

	public void execute(Object obj) {
      doCancel();
    }
  }

  /** @since 1.1 */
  protected class ResetClosure implements Closure, Serializable {
	private static final long serialVersionUID = 1L;
    protected EnvironmentAwareCallback callback;
    
    public ResetClosure(EnvironmentAwareCallback callback) {
      this.callback = callback;
    }

    public void execute(Object obj) {
      doReset(callback);
    }
  }
  
  /** @since 1.1 */
  protected class FinishClosure implements Closure, Serializable {
    private static final long serialVersionUID = 1L;
    protected Object result;
    
    public FinishClosure(Object result) {
      this.result = result;
    }

    public void execute(Object obj) {
      doFinish(result);
    }
  }

  /** @since 1.1 */
  protected class ReplaceClosure implements Closure, Serializable {
    private static final long serialVersionUID = 1L;
    protected Widget flow;
    protected Configurator configurator;

    public ReplaceClosure(Widget flow, Configurator configurator) {
      this.flow = flow;
      this.configurator = configurator;
    }

    public void execute(Object obj) {
      doReplace(flow, configurator);
    }
  }
  
  /** @since 1.1 */
  protected class StartClosure implements Closure, Serializable {
    private static final long serialVersionUID = 1L;
    protected Widget flow;
    protected Configurator configurator;
    protected Handler handler;

    public StartClosure(Widget flow, Configurator configurator, Handler handler) {
      this.flow = flow;
      this.configurator = configurator;
      this.handler = handler;
    }

    public void execute(Object obj) {
      doReplace(flow, configurator);
    }
  }
	  
  /** @since 1.1 */
  protected static class FlowEventConfirmationEventListener extends StandardEventListener {
    private static final long serialVersionUID = 1L;
	private Closure positive, negative;
	  
	public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
      boolean b = Boolean.valueOf(eventParam).booleanValue();
      (b ? positive : negative).execute(null);
	}

    public void setPositive(Closure positive) {
      this.positive = positive;
    }

    public void setNegative(Closure negative) {
      this.negative = negative;
    }
  }
  
  /** @since 1.1 */
  protected class ConfirmationCleanupClosure implements Closure, Serializable {
    private static final long serialVersionUID = 1L;
	protected EventListener listener;

    public ConfirmationCleanupClosure(EventListener listener) {
      Assert.notNull(listener);
      this.listener = listener;
    }

	public void execute(Object obj) {
      removeEventListener(listener);
      removeFlowEventConfirmationWidget();
	}
  }
}
