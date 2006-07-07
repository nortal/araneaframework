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

package org.araneaframework.framework.messages;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;

/**
 * Message that:
 * <ul> 
 *   <li>resets the first encountered {@link org.araneaframework.framework.FlowContext}</li>
 *   <li>starts a specified flow in it</li>
 * </ul>
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardFlowContextResettingMessage implements Message {
  Widget flow;
  
  public StandardFlowContextResettingMessage(Widget flow) {
    this.flow = flow;
  }
  
  public final void send(Object id, Component component){
    if (!(component instanceof FlowContext)) {
      component._getComponent().propagate(this);
    }
    else {
      try {
        this.execute(component);
      }
      catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  protected void execute(Component component) throws Exception {
    final FlowContext fCtx = (FlowContext) component;
  
    fCtx.reset(new EnvironmentAwareCallback() {
      public void call(Environment env) throws Exception {
        FlowContext f = (FlowContext)env.getEntry(FlowContext.class);
        if (flow != null)
          f.start(flow, null, null);
      }
    });
  }
}
