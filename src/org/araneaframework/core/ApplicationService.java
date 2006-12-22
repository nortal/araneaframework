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
    public Map getData();
  }

  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  /**
   * The attribute of the action id.
   * @since 1.0.4
   */
  public static final String ACTION_HANDLER_ID_KEY = "serviceActionHandler";
  /** @since 1.0.4 */
  public static final String ACTION_PARAMETER_KEY = "serviceActionParameter"; 
}
