package org.araneaframework.core;

import java.io.Serializable;
import org.araneaframework.Widget;
import org.araneaframework.Composite.CompositeWidget;
import org.araneaframework.Viewable.ViewableWidget;

/**
 * A Widget Component.
 */
public interface ApplicationWidget extends ApplicationService, Widget, CompositeWidget, ViewableWidget {

  /**
   * A view model for a Widget.
   */
  public interface WidgetViewModel extends ApplicationService.ServiceViewModel, Serializable {}

  /**
   * The key of the event handler.
   */
  public static final String EVENT_HANDLER_ID_KEY = "araWidgetEventHandler";
  public static final String EVENT_PARAMETER_KEY = "araWidgetEventParameter";

  /**
   * The key of the path of the event in the request.
   * 
   * @since 1.1
   */
  public static final String EVENT_PATH_KEY = "araWidgetEventPath";

}
