package org.araneaframework.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    Scope cur = this;
    
    List idlist = new ArrayList();
    
    while (cur != null) {
      if (cur.getId() != null)
        idlist.add(cur.getId().toString());      
      cur = cur.getParent();
    }
    
    Collections.reverse(idlist);

    return new StandardPath(idlist);
  }
  
  public String toString() {
    return toPath().toString();
  }

}
