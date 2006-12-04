package org.araneaframework.core;

import org.araneaframework.Path;
import org.araneaframework.Scope;

public class StandardScope implements Scope {
  private Object id;
  private Scope parent;
  
  public StandardScope(Object id, Scope parent) {
    this.id = id;
    this.parent = parent;
  }

  public Object getId() {
    return id;
  }

  public Scope getParent() {
    return parent;
  }

  public Path toPath() {
    return null;
  }

}
