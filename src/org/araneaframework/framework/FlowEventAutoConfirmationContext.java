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
public interface FlowEventAutoConfirmationContext extends Serializable {
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
