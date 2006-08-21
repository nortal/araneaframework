/**
 * 
 */
package org.araneaframework.uilib.form;

import java.io.Serializable;

public interface GenericFormElementAware extends Serializable {
  public void setGenericFormElementCtx(GenericFormElementContext gfeCtx);
}