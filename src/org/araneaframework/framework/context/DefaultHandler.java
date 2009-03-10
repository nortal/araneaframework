package org.araneaframework.framework.context;

import org.araneaframework.framework.FlowContext.Handler;

/**
 * A default implementation of <code>Handler</code> that does not have any
 * functionality. Instead it is more useful when implementing
 * <code>Handler</code> and (only) one of its methods.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.1
 */
public class DefaultHandler implements Handler {

  private static final long serialVersionUID = 1L;

  /**
   * An empty implementation for <code>onCancel</code> event. 
   */
  public void onCancel() throws Exception {}

  /**
   * An empty implementation for <code>onFinish</code> event. 
   */
  public void onFinish(Object returnValue) throws Exception {}
}
