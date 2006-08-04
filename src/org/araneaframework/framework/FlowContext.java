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
import org.araneaframework.Component;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Widget;
import org.araneaframework.core.Custom.CustomWidget;

/**
 * This context provides support for flow navigation and nesting. A flow is started using 
 * {@link #start(Component, org.araneaframework.framework.FlowContext.Configurator, org.araneaframework.framework.FlowContext.Handler)}
 * and continues to be active until it explicitly returns control to the caller using {@link #finish(Object)} or
 * {@link #cancel()}. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface FlowContext extends Serializable {
  /**
   * The key of the callable child.
   */
  public static final String FLOW_KEY = "f";

  /**
   * The key used for the CallStack in the OutputData attribute set.
   */
  public static final String CALL_STACK_KEY = "org.araneaframework.framework.FlowContext.CALL_STACK";

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
   */
  public FlowReference getCurrentReference();
  
  /**
   * Adds an environment entry that is visible in all subflows.
   */
  public void addNestedEnvironmentEntry(CustomWidget scope, final Object entryId, Object envEntry);

  
  public interface FlowReference extends Serializable {
    /**
     * Resets the flow stack up to the referred flow and provides the callback with the local environment
     * that can be used to manipulate the flow stack further.
     */
    public void reset(EnvironmentAwareCallback callback) throws Exception;
  }
  
  public interface Handler extends Serializable {
    public void onFinish(Object returnValue) throws Exception;   
    public void onCancel() throws Exception;
  }
  
  public interface Configurator extends Serializable {
    public void configure(Widget comp) throws Exception;
  }
}
