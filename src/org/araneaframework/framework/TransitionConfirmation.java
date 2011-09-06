package org.araneaframework.framework;

import org.araneaframework.core.ApplicationComponent;

import java.io.Serializable;

/**
 * Transition confirmation between {@link FlowContext}s. Implementation depends on a given component.
 * 
 * @author Aleksandr Vorobjov (aleksandr.vorobjov@webmedia.ee)
 */
public interface TransitionConfirmation extends Serializable {
  
  /**
   * @return confirmation message
   */
  String getMessage();
  
  /**
   * @return <code>true</code> - if confirmation is required before canceling the flow, otherwise - <code>false</code>;
   */
  boolean confirmRequired();
  
  /**
   * @return component, which registered the confirmation.
   */
  ApplicationComponent getComponent();
}
