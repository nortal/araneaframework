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

/**
 * An entity with a life-cycle (<code>init</code>, <code>destroy</code>) and an Environment.
 * <p>
 * A components life-cycle is simple. It starts with initiation and ends with destruction.
 * <ul>
 * <li>A component once initiated, cannot be re-initiated.
 * <li>A component once destroyed, cannot be re-initiated.
 * <li>A component not initiated, cannot be destroyed.
 * </ul>
 * When a life-cycle contract is broken, {@link org.araneaframework.core.exception.AraneaRuntimeException} will be
 * thrown.
 * <p>
 * The component is initialized with an {@link Environment}, which cannot be altered during or after the initialization
 * process.
 * <p>
 * The Component follows the template pattern by defining <code>_getComponent()</code> which returns the implementation.
 * The implementation is used for managing the component life-cycle. The Component contract itself does not expose
 * direct life-cycle methods, since they are protected and handled by implementations.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public interface Component extends Serializable {

  /**
   * Provides access to the <code>Environment</code> of this <code>Component</code>.
   * 
   * @return the <code>Environment</code> of the <code>Component</code>.
   * @since 1.1
   */
  Environment getEnvironment();

  /**
   * Provides the scope of this <code>Component</code>. The <code>Scope</code> is related to {@link Path} and the idea
   * behind it is quite similar.
   * 
   * @return the scope of the <code>Component</code>
   * @since 1.1
   */
  Scope getScope();

  /**
   * Informs whether this <code>Component</code> is alive. If it is alive then it means that the <code>Component</code>
   * has been initialized and is not destroyed yet.
   * 
   * @return <code>true</code>, if this component has been initialized and is not destroyed. Otherwise,
   *         <code>false</code>.
   * @since 1.1
   */
  boolean isAlive();

  /**
   * The factory method returning the implementation of the Component.
   * 
   * @return the implementation of the Component.
   */
  Component.Interface _getComponent();

  /**
   * The interface which takes care of calling the hooks in the <tt>Component</tt> template design pattern.
   * 
   * @see Component
   */
  interface Interface extends Serializable {

    /**
     * Initializes this <code>Component</code> with the specified scope and environment.
     * 
     * @param scope The <code>Scope</code> of the <code>Component</code>.
     * @param env The <code>Environment</code> of this <code>Component</code>.
     * @see Path
     */
    void init(Scope scope, Environment env);

    /**
     * Destroys this <code>Component</code>.
     */
    void destroy();

    /**
     * Forwards the given <code>Message</code> to the <code>Component</code> and its children.
     * 
     * @param message The <code>Message</code> to forward.
     */
    void propagate(Message message);

    /**
     * Enables this <code>Component</code>.
     */
    void enable();

    /**
     * Disables this <code>Component</code>.
     */
    void disable();
  }
}
