package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.Widget;

/**
 * Simple confirmation context&mdash;expects immediate user confirmation
 * for going through with some event.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface ConfirmationContext extends Serializable {
  public static final String CONFIRMATION_RESULT_KEY = "ConfirmationContextResult";
  
  void registerConfirmation(Widget confirmationTarget, String confirmationMessage);
  String getConfirmationMessage();
}
