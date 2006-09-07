package org.araneaframework.uilib.form.constraint;

import org.araneaframework.Environment;
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

  public void constrain(Constrainable constrainable) {
    LazyTypeAssert(FormElement.class, constrainable);
    super.constrain(constrainable);
  }

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
