package org.araneaframework.uilib.form;

import java.io.Serializable;
import java.util.Set;
import org.araneaframework.Environment;

public interface GenericFormElementContext extends Serializable {
  public boolean isValid();
  
  public void addError(String error);
  public void addErrors(Set errors);
  
  public Environment getEnvironment();
}
