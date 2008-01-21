package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface FlowEventAutoConfirmationContext extends Serializable {
  void setConfirmationCondition(Predicate p);
  void askConfirmation(Closure onConfirm);
  Predicate getCondition();
}
