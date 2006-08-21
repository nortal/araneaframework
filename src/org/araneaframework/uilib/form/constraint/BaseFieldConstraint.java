package org.araneaframework.uilib.form.constraint;

import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.FormElementAware;
import org.araneaframework.uilib.form.FormElementContext;

public abstract class BaseFieldConstraint extends BaseConstraint implements FormElementAware {
  private FormElementContext feCtx;
  
  public void setFormElementCtx(FormElementContext feCtx) {
    this.feCtx = feCtx;
  }
  
  protected FormElementContext getFormElementCtx() {
    return this.feCtx;
  }
  
  public boolean validate() throws Exception {
    Assert.notNull(this, getFormElementCtx(), 
        "Form element context must be assigned to the constraint before it can function! " +
        "Make sure that the constraint is associated with a form element!");
    
    return super.validate();
  }
  
  protected String getLabel() {
    return getFormElementCtx().getLabel();
  }
  
  protected Object getValue() {
    return getFormElementCtx().getValue();
  }
  
  public boolean isRead() {
    return getFormElementCtx().isRead();
  }
  
  public boolean isDisabled() {
    return getFormElementCtx().isDisabled();
  }

  public boolean isMandatory() {
    return getFormElementCtx().isMandatory();
  }
}
