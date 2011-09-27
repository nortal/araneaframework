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

package org.araneaframework.framework.message;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * A message that resets a flow context and starts a flow in it. More specifically, it
 * <ol>
 * <li>resets the first encountered {@link FlowContext}</li>
 * <li>starts a specified flow in it</li>
 * </ol>
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardFlowContextResettingMessage implements Message {

  private final Widget flow;

  /**
   * Initializes a new flow context resetting message with given flow to start.
   * 
   * @param flow The flow to start (required, must not be initialized).
   */
  public StandardFlowContextResettingMessage(Widget flow) {
    Assert.notNullParam(flow, "flow");
    this.flow = flow;
  }

  /**
   * {@inheritDoc}
   */
  public final void send(Object id, Component component) {
    if (!(component instanceof FlowContext)) {
      component._getComponent().propagate(this);
    } else {
      try {
        execute((FlowContext) component);
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * Executes flow context resetting and the designated flow starting.
   * 
   * @param flowContext The flow context to work with.
   */
  protected void execute(FlowContext flowContext) {
    flowContext.reset(new EnvironmentAwareCallback() {

      public void call(Environment env) {
        FlowContext f = EnvironmentUtil.getFlowContext(env);
        if (StandardFlowContextResettingMessage.this.flow != null) {
          f.start(StandardFlowContextResettingMessage.this.flow);
        }
      }
    });
  }
}
