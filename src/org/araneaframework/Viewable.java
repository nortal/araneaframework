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
 * Viewable is a component which has a view model via <code>getViewModel()</code>.
 * <p>
 * Viewable should not be implemented directly but subinterfaces ComponentInterface, 
 * ServiceInterface, WidgetInterface should be used.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Viewable extends Serializable {
  public Interface _getViewable();

  interface Interface extends Serializable {
    public Object getViewModel();
  }
  
  /**
   * Viewable Component.
   */
  public interface ViewableComponent extends Viewable, Component, Serializable {}
  
  /**
   * Viewable Service.
   */
  public interface ViewableService extends ViewableComponent, Service, Serializable {}
  
  /**
   * Viewable Widget.
   */
  public interface ViewableWidget extends ViewableService, Widget, Serializable {}
}
