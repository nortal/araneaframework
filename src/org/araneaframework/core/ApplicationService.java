package org.araneaframework.core;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.Service;
import org.araneaframework.Composite.CompositeService;
import org.araneaframework.Viewable.ViewableService;
import org.araneaframework.core.ApplicationComponent.ComponentViewModel;

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
  }}