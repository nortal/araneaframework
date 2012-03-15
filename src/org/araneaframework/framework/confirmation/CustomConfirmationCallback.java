package org.araneaframework.framework.confirmation;

import java.io.Serializable;
import java.util.List;
/**
 * Callback that is called when custom confirmation is invoked.
 * 
 * @author Maksim Boiko
 *
 */
public interface CustomConfirmationCallback extends Serializable {
  
  String getMessage();

  List<ConfirmationButton> getConfirmationButtons();
}
