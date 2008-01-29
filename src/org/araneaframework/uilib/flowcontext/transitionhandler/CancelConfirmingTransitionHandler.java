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

package org.araneaframework.uilib.flowcontext.transitionhandler;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.container.StandardFlowContainerWidget;

/**
 * {@link FlowContext.TransitionHandler} with some special confirmation handling for 
 * {@link FlowContext#TRANSITION_CANCEL} events. It attaches event listener to active
 * flow when its cancellation request is received and asks user to confirm the action 
 * whenever the predicate <code>shouldConfirm</code> evaluates to <code>true</code>. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class CancelConfirmingTransitionHandler extends StandardFlowContainerWidget.StandardTransitionHandler {
  private static final long serialVersionUID = 1L;
  private Predicate shouldConfirm;
  private String confirmationMessage;

  public CancelConfirmingTransitionHandler(Predicate shouldConfirm, String confirmationMessage) {
    Assert.notNullParam(this, shouldConfirm, "shouldConfirm");
    Assert.isInstanceOf(Serializable.class, shouldConfirm, "shouldConfirm Predicate must implement java.io.Serializable");
    Assert.notNullParam(this, confirmationMessage, "confirmationMessage");
    this.shouldConfirm = shouldConfirm;
    this.confirmationMessage = confirmationMessage;
  }

  public  void doTransition(int transitionType, Widget activeFlow, Closure transition) {
    if (transitionType == FlowContext.TRANSITION_CANCEL && shouldConfirm.evaluate(activeFlow)) {
      ConfirmationContext ctx = requireConfirmationContext(activeFlow);
      ctx.confirm(transition, confirmationMessage);
    } else
      super.doTransition(transitionType, activeFlow, transition);
  }

  protected ConfirmationContext requireConfirmationContext(Widget activeFlow) {
    ConfirmationContext ctx = (ConfirmationContext) activeFlow.getEnvironment().requireEntry(ConfirmationContext.class);
    return ctx;
  }
}