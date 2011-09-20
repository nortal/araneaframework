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
import org.araneaframework.core.BaseApplicationWidget;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
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

  @Override
  public void render(OutputData output) {
    this.renderCalled = true;
  }

  @Override
  public void update(InputData input) {
    this.updateCalled = true;
    this.input = input;
  }

  @Override
  public void event(Path path, InputData input) throws Exception {
    this.eventProcessed = true;
    this.path = path;
    this.input = input;
    super.event(path, input);
  }

  @Override
  public void action(Path path, InputData input, OutputData output) throws Exception {
    this.actionCalled = true;
    this.path = path;
    this.input = input;
    this.output = output;
    super.action(path, input, output);
  }

  @Override
  protected void handleAction(InputData input, OutputData output) throws Exception {
  }

  @Override
  protected void handleEvent(InputData input) throws Exception {
  }

  @Override
  public void destroy() {
    this.destroyCalled = true;
  }

  public boolean getUpdateCalled() {
    return this.updateCalled;
  }

  public boolean getEventProcessed() {
    return this.eventProcessed;
  }

  public boolean getRenderCalled() {
    return this.renderCalled;
  }

  public boolean getActionCalled() {
    return this.actionCalled;
  }

  public InputData getInput() {
    return this.input;
  }

  public OutputData getOutput() {
    return this.output;
  }

  public boolean getDestroyCalled() {
    return this.destroyCalled;
  }

  public Path getPath() {
    return this.path;
  }

  public void setEventProcessed(boolean eventProcessed) {
    this.eventProcessed = eventProcessed;
  }
}
