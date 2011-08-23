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

import java.util.Map;
import org.araneaframework.Composite.CompositeService;
import org.araneaframework.Viewable.ViewableService;

/**
 * Application Service unifies <tt>Service</tt>, <tt>Composite</tt> and <tt>Viewable</tt> contracts. It also extends
 * {@link ApplicationComponent} contract to application services.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface ApplicationService extends ApplicationComponent, CompositeService, ViewableService {

  /**
   * The request parameter name for retrieving delegated action handler ID.
   * 
   * @since 1.0.4
   */
  String ACTION_HANDLER_ID_KEY = "araServiceActionHandler";

  /**
   * The request parameter name for retrieving action handler parameter value.
   * 
   * @since 1.0.4
   */
  String ACTION_PARAMETER_KEY = "araServiceActionParameter";

  /**
   * The request parameter name for retrieving delegated action handler full path (scope).
   * 
   * @since 1.1
   */
  String ACTION_PATH_KEY = "araServiceActionPath";

  /**
   * The view model contract for services.
   */
  interface ServiceViewModel extends ApplicationComponent.ComponentViewModel {

    /**
     * Provides a map with custom data from the view.
     * 
     * @return A map with data or an empty map.
     */
    Map<String, Object> getData();
  }

}
