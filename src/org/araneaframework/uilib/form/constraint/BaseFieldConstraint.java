package org.araneaframework.uilib.form.constraint;

import org.araneaframework.uilib.form.FormElementContext;

public abstract class BaseFieldConstraint extends BaseConstraint {
  public BaseFieldConstraint() {
  }
	
  protected FormElementContext getField() {
    return (FormElementContext)getEnvironment().requireEntry(FormElementContext.class);
  }
  
  protected String getLabel() {
    return getField().getLabel();
  }

  protected Object getValue() {
    return getField().getValue();
  }
  
  public boolean isRead() {
    return getField().isRead();
  }
  
  public boolean isDisabled() {
    return getField().isDisabled();
  }

  public boolean isMandatory() {
    return getField().isMandatory();
  }
}
