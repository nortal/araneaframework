package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Predicate;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FlowEventAutoConfirmationContext extends Serializable {
  void setConfirmationCondition(ConfirmationCondition p);
  //void askConfirmation(Closure onConfirm);
  ConfirmationCondition getCondition();

  interface ConfirmationCondition extends Serializable {
    Predicate getStartPredicate();
    Predicate getFinishPredicate();
    Predicate getResetPredicate();
    Predicate getReplacePredicate();
    Predicate getCancelPredicate();
  }
}
