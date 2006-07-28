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
import org.araneaframework.core.ActionListener;
import org.araneaframework.core.StandardService;

/**
 * @author Toomas Römer
 */
public class MockStandardService extends StandardService {
  private boolean actionCalled = false;

  protected Object getActionId(InputData input) {
    return input.getScopedData().get(ACTION_ID_ATTRIBUTE);
  }
  
  public boolean isActionCalled() {
    return actionCalled;
  }
  
  public class MockActionListener implements ActionListener {
    public void processAction(Object actionId, InputData input, OutputData output) throws Exception {
      MockStandardService.this.actionCalled = true;
    }    
  }
  
  public Environment getTheEnvironment() {
    return getEnvironment();
  }
}
