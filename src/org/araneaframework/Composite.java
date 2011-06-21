/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework;

import java.io.Serializable;
import java.util.Map;

/**
 * Composite is a component with a set of child components. In a Composite
 * children can form a hierarchially having composites as children and so on.
 * <br><br>
 * This class should not be directly implemented, but {@link CompositeComponent}, 
 * {@link CompositeService} or {@link CompositeWidget} instead. 
 * <br><br>
 * The Component follows the template pattern by defining <code>_getComposite</code> which returns the
 * implementation.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Composite extends Serializable {
  
  /**
   * The factory method returning the implementation of the Composite.
   * @return the Composite implementation
   */
  public Composite.Interface _getComposite();
  
  /**
   * The interface which takes care of calling the hooks in the template.
   */
  public interface Interface extends Serializable {
    /**
     * Returns an unmodifiable map of all the child components.
     * @return a map of child components
     */
    public Map<String, Component> getChildren();
    
    /**
     * Attaches a component as a child of this component. No initialization of the
     * child takes place.
     * 
     * @param key of the added component
     * @param comp the component being attached
     */
    public void attach(String key, Component comp);
    
    /**
     * Detaches a child component with the specified key from this component. Child
     * will not be destroyed, just removed.
     * 
     * @param key of the child getting detached
     * @return the removed component
     */
    public Component detach(String key); 
  }
  
  /**
   * A composite component.
   */
  public interface CompositeComponent extends Composite, Component, Serializable {}
  
  /**
   * A composite service.
   */
  public interface CompositeService extends CompositeComponent, Service, Serializable {}
  
  /**
   * A composite widget.
   */
  public interface CompositeWidget extends CompositeService, Widget, Serializable {}
}
