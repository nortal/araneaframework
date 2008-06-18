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
 * Relocatable is a component that can be relocated from one parent to another.
 * As the {@link Environment} of a component is inherited from its parent, and
 * once the parent changes, the new parent's <code>Environment</code> can
 * be different, this class provides
 * {@link Interface#overrideEnvironment(Environment)} for resetting the
 * {@link Environment}.
 * <p>
 * This interface should not be used directly, subinterfaces
 * {@link RelocatableComponent}, {@link RelocatableService}, and
 * {@link RelocatableWidget} should be used.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Relocatable extends Component {

  /**
   * This method must provide a <code>Relocatable.Interface</code> that will
   * be used to change the <code>Environment</code> of the relocatable
   * component/service/widget.
   * 
   * @return An implementation of <code>Relocatable.Interface</code>.
   */
  public Relocatable.Interface _getRelocatable();

  /**
   * The <code>Relocatable</code>'s main interface that handles the change of
   * <code>Environment</code>.
   */
  public interface Interface extends Serializable {

    /**
     * Overrides the current <code>Environment</code> with <code>newEnv</code>.
     * 
     * @param newEnv The new <code>Environment</code>.
     */
    public void overrideEnvironment(Environment newEnv);

    /**
     * Provides access to the current <code>Environment</code> of the
     * relocatable component.
     * 
     * @return The current <code>Environment</code> of the relocatable
     *         component.
     */
    public Environment getCurrentEnvironment();

  }

  /**
   * An interface for a relocatable <code>Component</code>.
   */
  public interface RelocatableComponent extends Relocatable, Component,
      Serializable {}

  /**
   * An interface for a relocatable <code>Service</code>.
   */
  public interface RelocatableService extends RelocatableComponent, Service,
      Serializable {}

  /**
   * An interface for a relocatable <code>Widget</code>.
   */
  public interface RelocatableWidget extends RelocatableService, Widget,
      Serializable {}

}
