/**
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
**/

package org.araneaframework;

import java.io.Serializable;

/**
 * An entity with a licecycle (<code>init</code>, <code>destroy</code>) and an Environment.
 * <br><br>
 * A components lifecycle is simple. It starts with init and ends with destroy.
 * <ul>
 *  <li>A component once inited, cannot be reinited.
 *  <li>A component once destroyed, cannot be reinited.
 *  <li>A component not inited, cannot be destroyed.
 * </ul> 
 * If a lifecycle contract is broken {@link org.araneaframework.core.AraneaRuntimeException}
 * will be thrown.
 * <br><br>
 * The component is initialized with an {@link org.araneaframework.Environment}. The component's
 * Environment cannot be altered after the initialization process. 
 * <br><br>
 * The Component follows the template pattern by defining <code>_getComponent()</code> 
 * which returns the implementation.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Component extends Serializable {
  /** @since 1.1 */
  public Environment getEnvironment();

  /** @since 1.1 */
  public Scope getScope();
  
  /**
   * Returns true iff this component has been initialized and is not destroyed. 
   * @since 1.1 */
  public boolean isAlive();
  
  /**
   * The factory method returning the implementation of the Component.
   * @return the implementation of the Component.
   */
  public Component.Interface _getComponent();
  
  /**
   * The interface which takes care of calling the hooks in the template. 
   */
  public interface Interface extends Serializable {
    
    /**
     * Initializes this Component with the specified Environment.
     * @param scope TODO
     * @param env the Environment of this Component
     */
    public void init(Scope scope, Environment env);

    /**
     * Destroys this Component.
     */
    public void destroy();
    
    public void propagate(Message message);
    
    public void enable();
    public void disable();
  }
}
