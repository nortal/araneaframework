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
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;

/**
 * Allows setting of {@link FlowEventConfirmationHandler} for flow navigation events,
 * to conditionally execute callbacks before end-user requested flow navigation 
 * events take place.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FlowEventConfirmationContext extends Serializable {
  /** Preferred identifier for event listener which receives confirmation events from client side. */
  String CONFIRMATION_EVENT_LISTENER_ID = "flowEventConfirmation";

  void setFlowEventConfirmationHandler(FlowEventConfirmationHandler handler);
  FlowEventConfirmationHandler getFlowEventConfirmationHandler();

  interface FlowEventConfirmationHandler extends Serializable {
    /** Sets the {@link ConfirmationCondition} which is evaluated after flow navigation events
     * take place. Depending on the outcome of the evaluation. */
    void setConfirmationCondition(ConfirmationCondition condition);
    ConfirmationCondition getConfirmationCondition();

	 /**
	  * Called by <code>FlowContext</code> to set <code>Closure</code> which can be used to perform the 
	  * navigation event (if navigation is confirmed).
  	* @param onConfirm <code>Serializable</code> <code>Closure</code>
	  */
    void setOnConfirm(Closure onConfirm);
    Closure getOnConfirm();

    /** 
     * Sets the closure executed when {@link ConfirmationCondition} predicate for flow event 
     * returns <code>true</code>. Closure should 
     * @param doConfirm <code>Serializable</code> <code>Closure</code> */
    void setDoConfirm(Closure doConfirm);
    Closure getDoConfirm();
  }

  /**
   * Supplies the evaluated <code>Predicate</code> which is evaluated to determine
   * whether the <code>doConfirm</code> closure (see {@link FlowEventConfirmationHandler#setDoConfirm(Closure)})
   * should be executed or not.
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  interface ConfirmationCondition extends Serializable {
    Predicate getStartPredicate();
    Predicate getFinishPredicate();
    Predicate getResetPredicate();
    Predicate getReplacePredicate();
    Predicate getCancelPredicate();
  }
}
