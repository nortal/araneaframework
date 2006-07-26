package org.araneaframework.servlet;

import java.io.Serializable;

public interface ThreadCloningContext extends Serializable {
  public static final String CLONING_THREAD_KEY = "cloningThread";
  public static final String CLONABLE_THREAD_KEY = "clonableThread";
}
