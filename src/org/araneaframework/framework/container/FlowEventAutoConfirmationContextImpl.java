package org.araneaframework.framework.container;

import java.io.Serializable;
import org.apache.commons.collections.Predicate;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.FlowEventAutoConfirmationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class FlowEventAutoConfirmationContextImpl implements FlowEventAutoConfirmationContext {
  private static final long serialVersionUID = 1L;
  protected ConfirmationCondition condition;
  
  public void setConfirmationCondition(ConfirmationCondition condition) {
    Assert.isInstanceOf(Serializable.class, condition, "Confirmation condition predicate must implement java.io.Serializable");
    this.condition = condition;
  }
  
  public ConfirmationCondition getCondition() {
    return condition;
  }
  
  public static class DefaultConfirmationCondition implements FlowEventAutoConfirmationContext.ConfirmationCondition {
	public Predicate getCancelPredicate() {
		return null;
	}

	public Predicate getFinishPredicate() {
		return null;
	}

	public Predicate getReplacePredicate() {
		return null;
	}

	public Predicate getResetPredicate() {
		return null;
	}

	public Predicate getStartPredicate() {
		return null;
	}
  }
}
