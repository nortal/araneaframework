package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FlowEventAutoConfirmationContext extends Serializable {
  String CONFIRMATION_EVENT_LISTENER_ID = "flowEventConfirmation";

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
  }

  interface ConfirmationCondition extends Serializable {
    Predicate getStartPredicate();
    Predicate getFinishPredicate();
    Predicate getResetPredicate();
    Predicate getReplacePredicate();
    Predicate getCancelPredicate();
  }
}
