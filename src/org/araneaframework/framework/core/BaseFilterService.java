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
 * A filter service is a Service which filters requests to its child service. A filter overrides one of the methods:
 * <ul>
 * <li><code>action(Path, InputData, OutputData)</code></li>
 * </ul>
 * and does the filtering by allowing the action to be invoked on the child or not. This class is a skeleton which lets
 * all the requests through, sets the child, handles the initilization and destroying of the child.
 * <p>
 * The child is initilized with <code>getChildServiceEnvironment()</code> which by default returns this component's
 * Environment. For alternate environments it should be overridden.
 * </p>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class BaseFilterService extends BaseService implements FilterService {

  protected Service childService;

  public BaseFilterService() {}

  public BaseFilterService(Service childService) {
    setChildService(childService);
  }

  /**
   * Sets the child service.
   * 
   * @param childService
   */
  public void setChildService(Service childService) {
    this.childService = childService;
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
   * Returns the Environment of this service by default. The child is initilized with this method. Meant for overriding.
   */
  protected Environment getChildEnvironment() {
    return getEnvironment();
  }
}
