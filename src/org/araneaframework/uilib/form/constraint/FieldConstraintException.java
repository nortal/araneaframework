package org.araneaframework.uilib.form.constraint;

import org.araneaframework.core.exception.AraneaRuntimeException;

/**
 * Exception thrown when a <code>(Generic)FormElement</code> associated with a <code>Constraint</code> could not be
 * determined.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FieldConstraintException extends AraneaRuntimeException {

  /**
   * Creates the exception without any message or other <code>Throwable</code>.
   */
  public FieldConstraintException() {
    super();
  }

  /**
   * Creates the exception with a descriptive message and the <code>Throwable</code> that was caught.
   * 
   * @param message A descriptive message to help solve this issue.
   * @param cause A <code>Throwable</code> that was caught.
   */
  public FieldConstraintException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates the exception with a descriptive message.
   * 
   * @param message A descriptive message to help solve this issue.
   */
  public FieldConstraintException(String message) {
    super(message);
  }

  /**
   * Creates the exception with the <code>Throwable</code> that was caught.
   * 
   * @param cause A <code>Throwable</code> that was caught.
   */
  public FieldConstraintException(Throwable cause) {
    super(cause);
  }
}