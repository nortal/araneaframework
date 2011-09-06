package org.araneaframework.framework;

import java.util.List;

/**
 * Context for holding {@link TransitionConfirmation}s.
 * 
 * @author Aleksandr Vorobjov (aleksandr.vorobjov@webmedia.ee)
 */
public interface TransitionConfirmationContext {
  
  void addConfirmation(final TransitionConfirmation confirmation);
  
  void removeConfirmation(TransitionConfirmation confirmation);
  
  List<TransitionConfirmation> getConfirmations();
  
}
