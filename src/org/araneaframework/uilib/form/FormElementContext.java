package org.araneaframework.uilib.form;


public interface FormElementContext extends GenericFormElementContext {
  public String getLabel();
  public boolean isMandatory();
  public boolean isDisabled();
  
  public boolean isRead();
  public Object getValue();
}
