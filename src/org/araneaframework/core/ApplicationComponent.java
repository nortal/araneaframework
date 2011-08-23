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

package org.araneaframework.core;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.Component;
import org.araneaframework.Composite.CompositeComponent;
import org.araneaframework.Environment;
import org.araneaframework.Scope;
import org.araneaframework.Viewable.ViewableComponent;

/**
 * Application component unifies <tt>Component</tt>, <tt>Composite</tt> and <tt>Viewable</tt> contracts. It also extends
 * <tt>Component</tt> with the child component environment and component view model features. They provide a couple of
 * essential changes:
 * <ul>
 * <li>a component is now known to be able to provide an environment for its child components that they can interact
 * with;
 * <li>a component can now create a model of its data that can be passed on to any kind of rendering engine that just
 * needs to be able to interact with the model.
 * </ul>
 * The base class for application development is also available: {@link BaseApplicationComponent}.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface ApplicationComponent extends CompositeComponent, ViewableComponent {

  /**
   * This method must return a new environment instance that will we passed on to added child components. Child
   * components should not have any way to modify the environment of their parents, although they can interact with the
   * items from the parent component environment, and they can decide what kind of environment they pass on to their own
   * child components. It is always up to the parent component to decide (and be responsible for) what the environment
   * passed to child components will contain.
   * 
   * @return A new instance of environment, must not be <code>null</code>!
   */
  Environment getChildEnvironment();

  /**
   * The contract for a model that is passed on the view. It declares the minimum set of properties available to the
   * view.
   * <p>
   * Each component has its own view model that is used when rendering the component. However, only components that are
   * <tt>Viewable</tt>, can be rendered. When one needs to be rendered, its view model instance will be created, taking
   * a snapshot of data that it can pass on when requested from the view. Therefore, this interface declares the
   * contract for the Model in Model-View-Controller design pattern (separating the controller from the view using the
   * model).
   */
  interface ComponentViewModel extends Serializable {

    /**
     * Provides the scope (path) of the current component.
     * 
     * @return The scope object describing the path of the current component, or <code>null</code> when the component
     *         has no such scope.
     * @since 1.1
     * @see Scope
     */
    Scope getScope();

    /**
     * Provides a map of child components of current component by their IDs.
     * 
     * @return A map of child components of current component by their IDs. An empty map, when no child component
     *         exists.
     */
    Map<String, Component> getChildren();
  }
}
