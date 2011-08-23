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

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * The base class for all Aranea services built on top of <tt>BaseComponent</tt>. This class does not support composite
 * design pattern, thus cannot contain child items by default.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class BaseService extends BaseComponent implements Service {

  protected transient InputData currentInputData;

  protected transient OutputData currentOutputData;

  public Service.Interface _getService() {
    return new ServiceImpl();
  }

  /**
   * Services provide their services through this method. An implementation of a non-composite service like
   * <code>BaseService</code> uses the action method to hook in the middle of the action routing and provide filtering,
   * logging etc.
   * <p>
   * Note that <tt>input</tt> and <tt>output</tt> data provided to this method are also available through
   * {@link #getInputData()} and {@link #getOutputData()} methods during action processing.
   * 
   * @param path The path of the component to whom the action is targeted (<code>null</code> is also valid).
   * @param input Input data for the service or for its child components.
   * @param output Output data for the service or for its child components.
   * @throws Exception Any runtime exception that may occur.
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
  }

  /**
   * A method for services to handle exceptions when the {@link #action(Path, InputData, OutputData)} method should
   * fail. Default implementation just delegates exception handling to {@link BaseComponent#handleException(Exception)}.
   * 
   * @param e An exception that has occurred.
   * @throws Exception Any exception that may occur during exception handling.
   */
  protected void handleServiceException(Exception e) throws Exception {
    handleException(e);
  }

  /**
   * Provides access to the current request data, which is available only during service action processing.
   * 
   * @return The current request data.
   */
  protected InputData getInputData() {
    return this.currentInputData;
  }

  /**
   * Provides access to the current response data, which is available only during service action processing.
   * 
   * @return The current response data.
   */
  protected OutputData getOutputData() {
    return this.currentOutputData;
  }

  /**
   * Base service implementation that uses synchronization to perform service actions to make sure no one else uses it
   * at the same time.
   */
  protected class ServiceImpl implements Service.Interface {

    public void action(Path path, InputData input, OutputData output) {
      Assert.notNullParam(this, input, "input");
      Assert.notNullParam(this, output, "output");

      _startCall();

      BaseService.this.currentInputData = input;
      BaseService.this.currentOutputData = output;
      try {
        if (isAlive()) {
          BaseService.this.action(path, input, output);
        }
      } catch (Exception e) {
        try {
          handleServiceException(e);
        } catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      } finally {
        BaseService.this.currentInputData = null;
        BaseService.this.currentOutputData = null;
        _endCall();
      }
    }

  }

}
