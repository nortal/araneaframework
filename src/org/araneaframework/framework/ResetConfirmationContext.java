package org.araneaframework.framework;

import java.util.LinkedList;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.core.util.ComponentUtil;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * Context for holding {@link TransitionConfirmation}s. Should be added to the environment in the upper-most flow and
 * visible to all subflows. It is up to the developer to decide where to iterate through predicates before closing the flow.
 * By default {@link StandardFlowContainerWidget#reset(EnvironmentAwareCallback env)} checks this context and clears if necessary.
 * 
 * @author Aleksandr Vorobjov (aleksandr.vorobjov@webmedia.ee)
 */
public class ResetConfirmationContext implements TransitionConfirmationContext {
  
  private LinkedList<TransitionConfirmation> confirmations = new LinkedList<TransitionConfirmation>();
    
  public void addConfirmation(final TransitionConfirmation confirmation) {
    confirmations.addFirst(confirmation);
    
    ComponentUtil.addListenerComponent(confirmation.getComponent(), new BaseUIWidget() {
      @Override
      public void destroy() {
        removeConfirmation(confirmation);
      }
    });
  }
  
  public void removeConfirmation(TransitionConfirmation confirmation) {
    confirmations.remove(confirmation);
  }
  
  public LinkedList<TransitionConfirmation> getConfirmations() {
    return confirmations;
  }
}
