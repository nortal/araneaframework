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
import org.araneaframework.core.BaseApplicationService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockEventfulStandardService extends BaseApplicationService {
  private boolean actionCalled = false;
  private boolean destroyCalled = false;
  
  private transient Path path;
  private transient InputData input;
  private transient OutputData output;

  protected Object getActionId(InputData input) {
    return input.getScopedData(getScope().toPath()).get(ACTION_HANDLER_ID_KEY);
  }

  public void action(Path path, InputData input, OutputData output) throws Exception {
    actionCalled = true;
    this.path = path;
    this.input = input;
    this.output = output;
  }
  
  public void destroy() {
    destroyCalled = true;
  }
  
  public Environment getTheEnvironment() {
    return getEnvironment();
  }

  public InputData getInput() {
    return input;
  }

  public OutputData getOutput() {
    return output;
  }

  public Path getPath() {
    return path;
  }
  
  public boolean getActionCalled() {
    return actionCalled;
  }

  public boolean getDestroyCalled() {
    return destroyCalled;
  }
}
