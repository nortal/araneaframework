package org.araneaframework.core;

import org.apache.commons.lang.math.RandomUtils;
import org.araneaframework.Component;

/**
 * This utility class contains methods for managing Aranea components.
 * 
 * @author Jevgeni kabanov (ekabanov@webmedia.ee)
 */
public class ComponentUtil {
  public static final String LISTENER_KEY = "org_araneaframework_core_ComponentUtil_LISTENER";
  
  /**
   * This method will attach the listener component to the target custom component, allowing it to receive
   * all the lifecycle events (which exactly depends on the target component type).   
   * <p>
   * This allows for instance to add a child component that will execute some action on destroy, thus
   * essentially tying some action to the lifecycle of the target component. A typical application
   * is to scope something (e.g. environment entry) with the target component.
   */
  public static void addListenerComponent(Custom target, Component listener) throws Exception {
    listener._getComponent().init(target.getChildEnvironment());
           
    String key = LISTENER_KEY;    
    while (target._getComposite().getChildren().get(key) != null) {
      key = LISTENER_KEY + RandomUtils.nextLong();
    }
    
   target._getComposite().attach(key, listener);
  }
}
