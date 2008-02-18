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

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockEventfulStandardWidget extends BaseApplicationWidget {
  private boolean updateCalled = false;
  private boolean eventProcessed = false;
  private boolean renderCalled = false;
  private boolean destroyCalled = false;
  
  private boolean actionCalled = false;
  
  private transient InputData input;
  private transient OutputData output;
  private transient Path path;
  
  public void render(OutputData output) {
    
    this.renderCalled = true;
  }
  
  public void update(InputData input) throws Exception {
    this.updateCalled = true;
    this.input = input;
  }
  
  public void event(Path path, InputData input) throws Exception {
    eventProcessed = true;
    this.path = path;
    this.input = input;
    super.event(path, input);
  }
  
  public void action(Path path, InputData input, OutputData output) throws Exception {
    actionCalled = true;
    this.path = path;
    this.input = input;
    this.output = output;
    super.action(path, input, output);
  }
  
  protected void handleAction(InputData input, OutputData output) throws Exception {}

  protected void handleEvent(InputData input) throws Exception {}

  public void destroy() {
    destroyCalled = true;
  }
  
  public boolean getUpdateCalled() {
    return updateCalled;
  }

  public boolean getEventProcessed() {
    return eventProcessed;
  }

  public boolean getRenderCalled() {
    return renderCalled;
  }

  public boolean getActionCalled() {
    return actionCalled;
  }

  public InputData getInput() {
    return input;
  }

  public OutputData getOutput() {
    return output;
  }

  public boolean getDestroyCalled() {
    return destroyCalled;
  }

  public Path getPath() {
    return path;
  }

  public void setEventProcessed(boolean eventProcessed) {
    this.eventProcessed = eventProcessed;
  }
}
