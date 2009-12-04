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
import org.araneaframework.Service;
import org.araneaframework.Composite.CompositeService;
import org.araneaframework.Viewable.ViewableService;

/**
 * A Service Component.
 */
public interface ApplicationService extends ApplicationComponent, Service, CompositeService, ViewableService {

  /**
   * A view model for a Service.
   */
  public interface ServiceViewModel extends ApplicationComponent.ComponentViewModel, Serializable {
    /**
     * Can be used to custom data from the view.
     */
    public Map<String, Object> getData();
  }

  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  /**
   * The attribute of the action id.
   * @since 1.0.4
   */
  public static final String ACTION_HANDLER_ID_KEY = "araServiceActionHandler";
  /** @since 1.0.4 */
  public static final String ACTION_PARAMETER_KEY = "araServiceActionParameter";

  /**
   * The key of the path of the action in the request.
   * 
   * @since 1.1
   */
  public static final String ACTION_PATH_KEY = "araServiceActionPath";

}
