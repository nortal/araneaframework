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
import org.apache.log4j.Logger;
import org.araneaframework.Component;
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

/**
 * A {@link org.araneaframework.framework.FlowContext} where the flows are structured as a stack.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardFlowContainerWidget extends BaseApplicationWidget implements FlowContext {
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  private static final Logger log = Logger.getLogger(StandardFlowContainerWidget.class);
  
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

  public void start(Widget flow, Configurator configurator, Handler handler) {
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
    
    addWidget(frame.getName(), flow);

    if (configurator != null) {
      try {
        configurator.configure(flow);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }    
  }
  
  public void replace(Widget flow, Configurator configurator) {
    Assert.notNullParam(flow, "flow");
    
    CallFrame previousFrame = (CallFrame) callStack.removeFirst();
    CallFrame frame = makeCallFrame(flow, configurator, previousFrame.getHandler(), previousFrame);
    
    if (log.isDebugEnabled())
      log.debug("Replacing flow '" + previousFrame.getWidget().getClass().getName() + 
        "' with flow '" + flow.getClass().getName() + "'");
    
    removeWidget(previousFrame.getName());
    
    callStack.addFirst(frame);    
    
    addWidget(frame.getName(), flow);
    
    if (configurator != null) {
      try {
        configurator.configure(flow);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  public void finish(Object returnValue) {
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
  }

  public void cancel() {
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
  }
  
  public FlowContext.FlowReference getCurrentReference() {
  	return new FlowReference();
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
  
  public void reset(final EnvironmentAwareCallback callback) {
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
  
  //*******************************************************************
  // PROTECTED METHODS
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
  }
  
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
    
    public String getName() {
      return name;
    }

    public String toString() {
      return widget.getClass().getName();
    }
  }

  protected LinkedList getCallStack() {
    return callStack;
  }
}