package org.araneaframework.framework.confirmation;


import java.io.Serializable;
import java.util.LinkedList;
import org.apache.commons.collections.Closure;

/**
 * @author Anton Stalnuhhin (antons@webmedia.ee)
 */
public interface CustomConfirmationContext extends Serializable {

  /**
   * Registers an automatical confirmation, which will be called when the flow is about to be closed thanks to a user
   * action.
   * 
   * @param callback contains data for confirmation + callback
   */
  void registerAutoConfirmation(AutoConfirmationCallback callback);
  
  LinkedList<AutoConfirmationCallback> getAutoConfirmations();
  
  public void registerUserTransition(Closure closure);

  /**
   * Asks confirmation from the user and notifies the callback.
   * 
   * @param callback contains data for confirmation + callback.
   */
  void confirm(CustomConfirmationCallback callback);

  /**
   * Automatically runs notification for the closing flows (registered earlier).
   */
  void confirmRegisteredAuto(boolean runNeedConfirm);
}
