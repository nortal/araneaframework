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
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.TransactionContext;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockEventfulStandardWidget extends StandardWidget {
  private boolean updateCalled = false;
  private boolean eventProcessed = false;
  private boolean processCalled = false;
  private boolean renderCalled = false;
  private boolean destroyCalled = false;
  
  private boolean actionCalled = false;
  
  private transient InputData input;
  private transient OutputData output;
  private transient Path path;
  
  private Long transactionId;
  
  public void render(OutputData output) {
    transactionId = (Long)output.getAttribute(TransactionContext.TRANSACTION_ID_KEY);
    
    this.renderCalled = true;
  }
  
  public void update(InputData input) throws Exception {
    this.updateCalled = true;
    this.input = input;
  }
  
  public void process() throws Exception{
    this.processCalled = true;
  }
  
  public void event(Path path, InputData input) throws Exception {
    eventProcessed = true;
    this.path = path;
    this.input = input;
  }
  
  public void action(Path path, InputData input, OutputData output) {
    actionCalled = true;
    this.path = path;
    this.input = input;
    this.output = output;
  }
  
  public void destroy() {
    destroyCalled = true;
  }
  
  public boolean getUpdateCalled() {
    return updateCalled;
  }

  public boolean getEventProcessed() {
    return eventProcessed;
  }

  public boolean isProcessCalled() {
    return processCalled;
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

  public Long getTransactionId() {
    return transactionId;
  }
}
