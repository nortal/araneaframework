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

package org.araneaframework.uilib.flowcontext.transitionhandler;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.araneaframework.Widget;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.container.StandardFlowContainerWidget;

/**
 * {@link org.araneaframework.framework.FlowContext.TransitionHandler} with some special confirmation handling for
 * {@link org.araneaframework.framework.FlowContext#TRANSITION_CANCEL} events. It attaches event listener to active flow
 * when its cancellation request is received and asks user to confirm the action whenever the predicate
 * <code>shouldConfirm</code> evaluates to <code>true</code>.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1
 */
public class CancelConfirmingTransitionHandler extends StandardFlowContainerWidget.StandardTransitionHandler {

  private Predicate shouldConfirm;

  private String confirmationMessage;

  private boolean allCancellings;

  /**
   * Initializes a new transition handler that displays given <code>confirmationMessage</code> if predicate
   * <code>shouldConfirm</code> returns <code>true</code>. By default, this transition handler responds to
   * {@link FlowContext#TRANSITION_CANCEL}. If <code>allCancellings</code> is set to <code>true</code> then this
   * transition handler also responds to every other transition type, except {@link FlowContext#TRANSITION_START} and
   * {@link FlowContext#TRANSITION_FINISH}.
   * 
   * @param shouldConfirm The predicate that is used to check whether confirmation should be shown.
   * @param confirmationMessage The message to show if the predicate returns <code>true</code>.
   * @param allCancellings If <code>true</code> then transition type may be either {@link FlowContext#TRANSITION_CANCEL}
   *          , {@link FlowContext#TRANSITION_REPLACE}, or {@link FlowContext#TRANSITION_RESET}. Otherwise, only
   *          {@link FlowContext#TRANSITION_CANCEL} is monitored.
   * @since 1.2.2
   */
  public CancelConfirmingTransitionHandler(Predicate shouldConfirm, String confirmationMessage, boolean allCancellings) {
    Assert.notNullParam(this, shouldConfirm, "shouldConfirm");
    Assert.isInstanceOf(Serializable.class, shouldConfirm,
        "shouldConfirm Predicate must implement java.io.Serializable");
    Assert.notNullParam(this, confirmationMessage, "confirmationMessage");

    this.shouldConfirm = shouldConfirm;
    this.confirmationMessage = confirmationMessage;
    this.allCancellings = allCancellings;
  }

  /**
   * Initializes a new transition handler that displays given <code>confirmationMessage</code> if predicate
   * <code>shouldConfirm</code> returns <code>true</code>. By default, this transition handler responds to transitions
   * of type {@link FlowContext#TRANSITION_CANCEL}.
   * 
   * @param shouldConfirm The predicate that is used to check whether confirmation should be shown.
   * @param confirmationMessage The message to show if the predicate returns <code>true</code>.
   */
  public CancelConfirmingTransitionHandler(Predicate shouldConfirm, String confirmationMessage) {
    this(shouldConfirm, confirmationMessage, false);
  }

  @Override
  public void doTransition(FlowContext.Transition transitionType, final Widget activeFlow, final Closure transition) {
    boolean test = this.allCancellings ? transitionType != FlowContext.Transition.START
        && transitionType != FlowContext.Transition.FINISH : transitionType == FlowContext.Transition.CANCEL;

    if (test && this.shouldConfirm.evaluate(null)) {
      ConfirmationContext ctx = requireConfirmationContext(activeFlow);
      Closure parameterizedTransition = new ParameterizedTransition(transitionType, activeFlow, transition);
      ctx.confirm(parameterizedTransition, this.confirmationMessage);
    } else {
      super.doTransition(transitionType, activeFlow, transition);
    }
  }

  protected ConfirmationContext requireConfirmationContext(Widget activeFlow) {
    return activeFlow.getEnvironment().requireEntry(ConfirmationContext.class);
  }

  private final class ParameterizedTransition implements Closure, Serializable {

    private final Closure transition;

    private final Widget activeFlow;

    private FlowContext.Transition transitionType;

    private ParameterizedTransition(FlowContext.Transition transitionType, Widget activeFlow, Closure transition) {
      this.transitionType = transitionType;
      this.transition = transition;
      this.activeFlow = activeFlow;
    }

    public void execute(Object obj) {
      notifyScrollContext(this.transitionType, this.activeFlow);
      this.transition.execute(this.activeFlow);
    }
  }
}
