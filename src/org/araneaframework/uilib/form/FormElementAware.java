/**
 * 
 */
package org.araneaframework.uilib.form;

import java.io.Serializable;

public interface FormElementAware extends Serializable {
  public void setFormElementCtx(FormElementContext feCtx);
}