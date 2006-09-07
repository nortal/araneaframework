package org.araneaframework.uilib.form.constraint;

import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Constrainable;
import org.araneaframework.uilib.form.FormElement;

public abstract class BaseFieldConstraint extends BaseConstraint {
  public BaseFieldConstraint() {
  }
	
  public BaseFieldConstraint(FormElement field) {
    constrain(field);
  }

  public boolean validate() throws Exception {
	//XXX: add assertion
    /*Assert.notNull(this, getFormElementCtx(), 
        "Form element context must be assigned to the constraint before it can function! " +
        "Make sure that the constraint is associated with a form element!");*/
    
    return super.validate();
  }

  public void constrain(Constrainable constrainable) {
    //XXX: add message
    Assert.isInstanceOf(FormElement.class, constrainable, "BaseFieldConstraint can only constrain FormElements!");
    super.constrain(constrainable);
  }
  
  //XXX: how about getField() NULL Cehckss
  
  protected FormElement getField() {
    return getConstrainable() != null ? (FormElement)getConstrainable() : (FormElement) null;
  }
  
  protected Environment getEnvironment() {
    return getField().getEnvironment();
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
  
  protected ConfigurationContext getConfiguration() {
    return (ConfigurationContext) getEnvironment().getEntry(ConfigurationContext.class);
  }
  
  protected String t(String key) {
    LocalizationContext locCtx = 
     (LocalizationContext) getEnvironment().getEntry(LocalizationContext.class);
    return locCtx.localize(key);
  }
}
