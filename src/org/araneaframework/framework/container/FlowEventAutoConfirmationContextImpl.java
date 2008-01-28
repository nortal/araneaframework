package org.araneaframework.framework.container;

import org.apache.commons.collections.Predicate;
import org.araneaframework.framework.FlowEventAutoConfirmationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class FlowEventAutoConfirmationContextImpl implements FlowEventAutoConfirmationContext {
  private static final long serialVersionUID = 1L;
  protected FlowEventConfirmationHandler handler;

  public void setFlowEventConfirmationHandler(FlowEventConfirmationHandler handler) {
    this.handler = handler;
  }
  
  public FlowEventConfirmationHandler getFlowEventConfirmationHandler() {
    return this.handler;
  }

  /** @since 1.1 */
  public static class NoopConfirmationCondition implements FlowEventAutoConfirmationContext.ConfirmationCondition {
    private static final long serialVersionUID = 1L;
    public static final NoopConfirmationCondition INSTANCE = new NoopConfirmationCondition();

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
