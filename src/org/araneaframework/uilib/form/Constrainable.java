package org.araneaframework.uilib.form;

import java.io.Serializable;

public interface Constrainable extends Serializable {
  public Constraint getConstraint();
  public void setConstraint(Constraint constraint);

  public Object getValue();
}
