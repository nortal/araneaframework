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
 * Viewable is a component which has a view model via <code>getViewModel()</code>.
 * <p>
 * Viewable should not be implemented directly, instead {@link ViewableComponent}, {@link ViewableService},
 * {@link ViewableWidget} should be used.
 * <p>
 * <tt>Viewable</tt> follows the template pattern by defining <code>_getViewable()</code> which returns the
 * implementation. The implementation is used for retrieving the view model. The <tt>Viewable</tt> contract itself does
 * not expose direct view model exposing methods, since it's protected and handled by implementations.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Viewable extends Serializable {

  /**
   * The factory method returning the implementation of the Viewable.
   * 
   * @return the implementation of the Viewable.
   */
  Interface _getViewable();

  /**
   * The interface which takes care of calling the hooks in the <tt>Viewable</tt> template design pattern.
   * 
   * @see Viewable
   */
  interface Interface extends Serializable {

    /**
     * Exposes the view model for the viewable.
     * 
     * @return The view model of the viewable component. Must not be <code>null</code>!
     */
    Object getViewModel();
  }

  /**
   * A viewable <code>Component</code> contract.
   */
  interface ViewableComponent extends Viewable, Component {
  }

  /**
   * A viewable <code>Service</code> contract.
   */
  interface ViewableService extends Viewable.ViewableComponent, Service {
  }

  /**
   * A viewable <code>Widget</code> contract.
   */
  interface ViewableWidget extends Viewable.ViewableService, Widget {
  }
}
