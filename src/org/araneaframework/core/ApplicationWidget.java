/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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
import org.araneaframework.Widget;
import org.araneaframework.Composite.CompositeWidget;
import org.araneaframework.Viewable.ViewableWidget;

/**
 * A Widget Component.
 */
public interface ApplicationWidget
  extends ApplicationService, Widget, CompositeWidget, ViewableWidget {

  /**
   * The key of the event handler.
   */
  String EVENT_HANDLER_ID_KEY = "araWidgetEventHandler";

  /**
   * The key of the event parameter.
   */
  String EVENT_PARAMETER_KEY = "araWidgetEventParameter";

  /**
   * The key of the path of the event in the request.
   * 
   * @since 1.1
   */
  String EVENT_PATH_KEY = "araWidgetEventPath";

  /**
   * A view model for a Widget.
   */
  public interface WidgetViewModel
    extends ApplicationService.ServiceViewModel, Serializable {}
}
