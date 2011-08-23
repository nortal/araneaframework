package org.araneaframework.uilib.list.util;

import org.araneaframework.core.exception.AraneaRuntimeException;

/**
 * Represents a validation failure.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.4
 */
public class ValidationFailureException extends AraneaRuntimeException {

  /**
   * Constructs a new validation failure exception. It should also contain a
   * general message about the validation failures (for later debugging).
   * 
   * @param message The message to describe the validation failure.
   */
  public ValidationFailureException(String message) {
    super(message);
  }

}
