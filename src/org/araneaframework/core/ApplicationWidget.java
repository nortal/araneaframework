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

import org.araneaframework.Composite.CompositeWidget;
import org.araneaframework.Viewable.ViewableWidget;

/**
 * Application Widget unifies <tt>Widget</tt>, <tt>Composite</tt> and <tt>Viewable</tt> contracts. It also extends
 * {@link ApplicationService} contract to application widgets.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface ApplicationWidget extends ApplicationService, CompositeWidget, ViewableWidget {

  /**
   * The request parameter name for retrieving delegated event handler ID.
   */
  String EVENT_HANDLER_ID_KEY = "araWidgetEventHandler";

  /**
   * The request parameter name for retrieving event handler parameter value.
   */
  String EVENT_PARAMETER_KEY = "araWidgetEventParameter";

  /**
   * The request parameter name for retrieving delegated event handler full path (scope).
   * 
   * @since 1.1
   */
  String EVENT_PATH_KEY = "araWidgetEventPath";

  /**
   * The view model contract for widgets.
   */
  interface WidgetViewModel extends ApplicationService.ServiceViewModel {
  }

}
