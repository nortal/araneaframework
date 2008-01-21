package org.araneaframework.framework.container;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.FlowEventAutoConfirmationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FlowEventAutoConfirmationContextImpl implements FlowEventAutoConfirmationContext {
  private static final long serialVersionUID = 1L;
  protected Predicate condition;
  
  public void setConfirmationCondition(Predicate condition) {
    Assert.isInstanceOf(Serializable.class, condition, "Confirmation condition predicate must implement java.io.Serializable");
    this.condition = condition;
  }
  
  public void askConfirmation(Closure onConfirm) {
    
  }

  public Predicate getCondition() {
    return condition;
  }
}
