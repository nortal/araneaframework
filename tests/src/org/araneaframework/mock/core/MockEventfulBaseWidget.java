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

package org.araneaframework.mock.core;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseWidget;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class MockEventfulBaseWidget extends BaseWidget {

  private boolean updateCalled = false;

  private boolean eventCalled = false;

  private boolean renderCalled = false;

  private boolean actionCalled = false;

  private boolean destroyCalled = false;

  private boolean disableCalled = false;

  private boolean enableCalled = false;

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    this.actionCalled = true;
  }

  @Override
  protected void update(InputData input) throws Exception {
    this.updateCalled = true;
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    this.eventCalled = true;
  }

  @Override
  protected void render(OutputData output) throws Exception {
    this.renderCalled = true;
  }

  @Override
  protected void disable() throws Exception {
    super.disable();
    this.disableCalled = true;
  }

  @Override
  protected void enable() throws Exception {
    super.enable();
    this.enableCalled = true;
  }

  @Override
  public void destroy() throws Exception {
    this.destroyCalled = true;
  }

  public boolean isActionCalled() {
    return this.actionCalled;
  }

  public boolean isEventCalled() {
    return this.eventCalled;
  }

  public boolean isRenderCalled() {
    return this.renderCalled;
  }

  public boolean isUpdateCalled() {
    return this.updateCalled;
  }

  public boolean getDestroyCalled() {
    return this.destroyCalled;
  }

  public boolean isDisableCalled() {
    return this.disableCalled;
  }

  public boolean isEnableCalled() {
    return this.enableCalled;
  }
}
