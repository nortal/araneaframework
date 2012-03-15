package org.araneaframework.framework.confirmation;

import org.araneaframework.core.ApplicationComponent;

/**
 * So called automatic confirmation that is called has additional methods for determination whether confirmation is
 * needed and the location of confirmation in flow stack.
 * 
 * @author Maksim Boiko
 */
public interface AutoConfirmationCallback extends CustomConfirmationCallback {

  boolean needConfirmation();

  ApplicationComponent getOwnerComponent();
}
