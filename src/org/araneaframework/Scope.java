package org.araneaframework;

import java.io.Serializable;

public interface Scope extends Serializable {
  Object getId();
  Scope getParent();
  
  Path toPath();
}
