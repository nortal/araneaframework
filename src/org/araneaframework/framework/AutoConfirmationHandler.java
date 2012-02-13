package org.araneaframework.framework;

import java.io.Serializable;
import org.apache.commons.collections.Closure;

/**
 * @author Anton Stalnuhhin (antons@webmedia.ee)
 */
public interface AutoConfirmationHandler extends Serializable {

  /**
   * @return true, if autoconfirmation must be applied for the next transition.
   */
  boolean canRun(String autoConfirmationId);

  /**
   * Notification that START,REPLACE,FINISH or CANCEL transition is applied. Only if {@link #canRun(String)}.
   * 
   * @return true, if runs confirmation.
   */
  boolean onTransition(String autoConfirmationId);

  /**
   * Notification that RESET transition is applied. Only if {@link #canRun(String)}.
   */
  void destroy(String autoConfirmationId);
  
  void registerUserTransition(Closure closure);

}
