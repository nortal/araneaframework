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

package org.araneaframework.framework.core;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.FilterService;

/**
 * A filter service is a service, which filters requests to its child-service. A filter should override one of methods:
 * <ul>
 * <li><code>action(Path, InputData, OutputData)</code></li>
 * </ul>
 * and does the filtering by allowing the action to be invoked on the child or not. This class is a skeleton which lets
 * all the requests through, sets the child, handles the initialization and destroying of the child.
 * <p>
 * The child is initialized with <code>getChildServiceEnvironment()</code> which, by default, returns this component's
 * Environment. For alternate environments it should be overridden.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class BaseFilterService extends BaseService implements FilterService {

  /**
   * The child service where the filter can forward requests.
   */
  private Service childService;

  /**
   * Creates a new filter service with no child service at first.
   */
  public BaseFilterService() {
  }

  /**
   * Creates a new filter service with given child service.
   * 
   * @param childService The child service where this filter can forward requests.
   */
  public BaseFilterService(Service childService) {
    setChildService(childService);
  }

  /**
   * Sets the child service where this filter can forward requests. The child service cannot be changed once a
   * child-service is initialized.
   * 
   * @param childService The child service where this filter can forward requests.
   */
  public void setChildService(Service childService) {
    Assert.isTrue(!isInitialized(), "Cannot specify a child service more than once or after filter is initialized.");
    this.childService = childService;
  }

  /**
   * Provides access to the underlying child service.
   * 
   * @return The child service used by this filter.
   * @since 2.0
   */
  protected Service getChildService() {
    return this.childService;
  }

  @Override
  protected void init() throws Exception {
    Assert.notNull(this, this.childService, "Filter cannot have a null child!");
    this.childService._getComponent().init(getScope(), getChildEnvironment());
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    this.childService._getService().action(path, input, output);
  }

  @Override
  protected void propagate(Message message) throws Exception {
    message.send(null, this.childService);
  }

  @Override
  protected void destroy() throws Exception {
    this.childService._getComponent().destroy();
  }

  /**
   * Provides a custom environment that can be used for initializing child-service. Returns the environment of this
   * service by default.
   * 
   * @return The environment to be passed on to the child-service.
   */
  protected Environment getChildEnvironment() {
    return getEnvironment();
  }
}
