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
 * Relocatable is a component that can be relocated from one parent to another. As the Environment
 * of a component can be inhereted from the parent and the new parent's Environment can differ, this
 * class provides <code>overrideEnvironment(Environment)</code> for resetting the Environment.
 * <p>
 * This interface should not be used directly, subinterfaces Interface, ComponentInterface,
 * ServiceInterface and WidgetInterface should be used.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface Relocatable extends Serializable {
  public Relocatable.Interface _getRelocatable();
  
  /**
   * The Relocatable's main interface. 
   */
  public interface Interface extends Serializable {
    /**
     * Overrides the current environment with env.
     */
    public void overrideEnvironment(Environment newEnv) throws Exception; 
  }
  
  /**
   * A relocatable Component.
   */
  public interface RelocatableComponent extends Relocatable, Component, Serializable {}
  
  /**
   * A relocatable Service.
   */
  public interface RelocatableService extends RelocatableComponent, Service, Serializable {}
  
  /**
   * A relocatable Widget.
   */
  public interface RelocatableWidget extends RelocatableService, Widget, Serializable {}
}
