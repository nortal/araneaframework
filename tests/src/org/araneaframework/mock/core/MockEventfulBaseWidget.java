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

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseWidget;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockEventfulBaseWidget extends BaseWidget {
  private boolean updateCalled = false;
  private boolean eventCalled = false;
  private boolean renderCalled = false;
  private boolean actionCalled = false;
  private boolean processCalled = false;
  private boolean destroyCalled = false;
    
  public void addComponent(Object key, Component component) throws Exception {
    _addComponent(key, component, getEnvironment());
  }
  
  public void removeComponent(Object key) throws Exception {
    _removeComponent(key);
  }

  protected void update(InputData input) throws Exception {
    updateCalled = true;
  }

  protected void event(Path path, InputData input) throws Exception {
    eventCalled = true;
  }


  protected void render(OutputData output) throws Exception {
    renderCalled = true;
  }


  protected void action(Path path, InputData input, OutputData output) throws Exception {
    actionCalled = true;
  }
  
  protected void process() {
    processCalled = true;
  }
  
  public void destroy() throws Exception {
    destroyCalled = true;
  }

  public boolean isActionCalled() {
    return actionCalled;
  }

  public boolean isEventCalled() {
    return eventCalled;
  }

  public boolean isRenderCalled() {
    return renderCalled;
  }

  public boolean isUpdateCalled() {
    return updateCalled;
  }

  public boolean isProcessCalled() {
    return processCalled;
  }
  
  public boolean getDestroyCalled() {
    return destroyCalled;
  }

  public void setDestroyCalled(boolean destroyCalled) {
    this.destroyCalled = destroyCalled;
  }
}
