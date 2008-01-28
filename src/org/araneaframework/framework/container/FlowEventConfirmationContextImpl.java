package org.araneaframework.framework.container;

import org.apache.commons.collections.Predicate;
import org.araneaframework.framework.FlowEventConfirmationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class FlowEventConfirmationContextImpl implements FlowEventConfirmationContext {
  private static final long serialVersionUID = 1L;
  private FlowEventConfirmationHandler handler;

  public void setFlowEventConfirmationHandler(FlowEventConfirmationHandler handler) {
    this.handler = handler;
  }
  
  public FlowEventConfirmationHandler getFlowEventConfirmationHandler() {
    return this.handler;
  }

  /** @since 1.1 */
  public static class NoopConfirmationCondition implements FlowEventConfirmationContext.ConfirmationCondition {
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
