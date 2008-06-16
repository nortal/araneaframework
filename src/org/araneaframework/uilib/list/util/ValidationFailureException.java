package org.araneaframework.uilib.list.util;

import org.araneaframework.core.AraneaRuntimeException;

/**
 * Represents a validation failure.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.4
 */
public class ValidationFailureException extends AraneaRuntimeException {

  private static final long serialVersionUID = 1L;

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
