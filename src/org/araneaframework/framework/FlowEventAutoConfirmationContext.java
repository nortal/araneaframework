package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;

/**
 * Allows setting of {@link FlowEventConfirmationHandler} for flow navigation events,
 * to conditionally execute some callbacks before end-user requested flow navigation 
 * events take place.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FlowEventAutoConfirmationContext extends Serializable {
  /** Preferred identifier for event listener which receives confirmation events from client side. */
  String CONFIRMATION_EVENT_LISTENER_ID = "flowEventConfirmation";

  /***/
  void setFlowEventConfirmationHandler(FlowEventConfirmationHandler handler);
  FlowEventConfirmationHandler getFlowEventConfirmationHandler();

  interface FlowEventConfirmationHandler extends Serializable {
    void setConfirmationCondition(ConfirmationCondition condition);
    ConfirmationCondition getConfirmationCondition();

	 /**
	  * Called by <code>FlowContext</code> to set <code>Closure</code> which can be used to perform the 
	  * navigation event (if navigation is confirmed).
  	  * @param p <code>Serializable</code> <code>Closure</code>.
	  */
    void setOnConfirm(Closure p);
    Closure getOnConfirm();
    
    void setDoConfirm(Closure p);
    Closure getDoConfirm();
  }

  interface ConfirmationCondition extends Serializable {
    Predicate getStartPredicate();
    Predicate getFinishPredicate();
    Predicate getResetPredicate();
    Predicate getReplacePredicate();
    Predicate getCancelPredicate();
  }
}
