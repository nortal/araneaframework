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
 * Relocatable is a component that can be relocated from one parent to another. The {@link Environment} of a component
 * is usually inherited from its parent, and once the parent changes, the new parent's <code>Environment</code> can be
 * different. This contract provides {@link Interface#overrideEnvironment(Environment)} for resetting the
 * <tt>Environment</tt>.
 * <p>
 * This interface should not be used directly, instead {@link RelocatableComponent}, {@link RelocatableService}, and
 * {@link RelocatableWidget} should be used.
 * <p>
 * <tt>Relocatable</tt> follows the template pattern by defining <code>_getRelocatable()</code>, which returns the
 * implementation. Latter is used for managing component relocation and environment change. The <tt>Relocatable</tt>
 * contract itself does not expose direct methods for doing that, since they are protected and handled by
 * implementations.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Relocatable extends Component {

  /**
   * The factory method returning the implementation of the <tt>Relocatable</tt>.
   * 
   * @return The <tt>Relocatable</tt> implementation (must not return <code>null</code>).
   */
  Relocatable.Interface _getRelocatable();

  /**
   * The interface which takes care of calling the hooks in the <tt>Relocatable</tt> template design pattern.
   * 
   * @see Relocatable
   */
  interface Interface extends Serializable {

    /**
     * Overrides the current <code>Environment</code> with <code>newEnv</code>.
     * 
     * @param newEnv The new <code>Environment</code>.
     */
    void overrideEnvironment(Environment newEnv);

    /**
     * Provides access to the current <code>Environment</code> of the relocatable component.
     * 
     * @return The current <code>Environment</code> of the relocatable component.
     */
    Environment getCurrentEnvironment();
  }

  /**
   * A relocatable <code>Component</code> contract.
   */
  interface RelocatableComponent extends Relocatable, Component {
  }

  /**
   * A relocatable <code>Service</code> contract.
   */
  interface RelocatableService extends Relocatable.RelocatableComponent, Service {
  }

  /**
   * A relocatable <code>Widget</code> contract.
   */
  interface RelocatableWidget extends Relocatable.RelocatableService, Widget {
  }

}
