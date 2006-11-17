/**
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
**/

package org.araneaframework.core;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Non-composite service component providing the extra action(Path, InputData, OutputData)
 * to BaseComponent.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class BaseService extends BaseComponent implements Service {
  
  protected InputData currentInputData;
  protected OutputData currentOutputData;

  public Service.Interface _getService() {
    return new ServiceImpl();
  }
  
  protected class ServiceImpl implements Service.Interface {
    public void action(Path path, InputData input, OutputData output){
      Assert.notNullParam(this, input, "input");
      Assert.notNullParam(this, output, "output");
      
      _startCall();
      currentInputData = input;
      currentOutputData = output;
      try {
        BaseService.this.action(path, input, output);
      }
      catch (Exception e) {
        try {
          handleServiceException(e);
        }
        catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      }
      finally {
        currentInputData = null;
        currentOutputData = null;
        _endCall();
      }
    }
  }

  /**
   * Services provide their services through the action method. An implementation of a non-composite
   * service like BaseService uses the action method to hook in the middle of the action routing and
   * provide filtering, logging etc.
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception{}    
  
  protected void handleServiceException(Exception e) throws Exception {
	handleException(e);
  }

  protected InputData getInputData() {
    return currentInputData;
  }
  
  protected OutputData getOutputData() {
    return currentOutputData;
  }
}
