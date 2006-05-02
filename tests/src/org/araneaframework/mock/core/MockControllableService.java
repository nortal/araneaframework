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

package org.araneaframework.mock.core;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockControllableService extends StandardService {
  private boolean keepActionBusy = false;
  private boolean actionCalled = false;
  private boolean destroyCalled = false;
  
  protected Object getActionId(InputData input) {
    return input.getGlobalData().get("actionId");
  }
  
  protected void action(Path path, InputData input, OutputData output) {
    actionCalled = true;
    keepActionBusy = true;
    while (keepActionBusy) {
      // Look everybody, look. I'm working out. Burning fat and what not.
    }
  }

  public boolean isKeepActionBusy() {
    return keepActionBusy;
  }

  public void setKeepActionBusy(boolean keepActionBusy) {
    this.keepActionBusy = keepActionBusy;
  } 
  
  public void destroy() {
    destroyCalled = true;
  }
  
  public boolean getActionCalled() {
    return actionCalled;
  }

  public boolean getDestroyCalled() {
    return destroyCalled;
  }
  
  public Environment getTheEnvironment() {
    return getEnvironment();
  }
}
