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
 * Represents an object with a set of child components. In a <tt>Composite</tt>, children can form a hierarchy having (
 * <tt>Composite</tt>) <tt>Component</tt>s as children and so on.
 * <p>
 * This class should not be directly implemented, but {@link CompositeComponent}, {@link CompositeService} or
 * {@link CompositeWidget} instead.
 * <p>
 * This contract follows the template pattern by defining <code>_getComposite</code> which returns the implementation.
 * The implementation is used to interact with the underlying object for reading, adding, or removing child components.
 * The <tt>Composite</tt> contract itself does not expose direct methods for accessing its child components, since they
 * are protected and handled by implementations.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Composite extends Serializable {

  /**
   * The factory method returning the implementation of the <tt>Composite</tt>.
   * 
   * @return The <tt>Composite</tt> implementation (must not return <code>null</code>).
   */
  Composite.Interface _getComposite();

  /**
   * The interface which takes care of calling the hooks in the <tt>Composite</tt> template design pattern.
   */
  interface Interface extends Serializable {

    /**
     * Provides an unmodifiable map of all the child components.
     * 
     * @return A map of child components, or an empty map when no child component exists.
     */
    Map<String, Component> getChildren();

    /**
     * Attaches given component as a child of this component. The child must be initialized before attaching.
     * 
     * @param key The ID for the added component.
     * @param comp The component being attached
     */
    void attach(String key, Component comp);

    /**
     * Detaches a child component with the specified key (ID) from this component. Child will not be destroyed, just
     * removed. The child component destruction should happen after calling this method, when appropriate.
     * 
     * @param key THE ID of the child getting detached.
     * @return The removed component.
     */
    Component detach(String key);
  }

  /**
   * A composite component.
   * 
   * @see Composite
   */
  interface CompositeComponent extends Composite, Component {
  }

  /**
   * A composite service.
   * 
   * @see Composite
   */
  interface CompositeService extends Composite.CompositeComponent, Service {
  }

  /**
   * A composite widget.
   * 
   * @see Composite
   */
  interface CompositeWidget extends Composite.CompositeService, Widget {
  }
}
