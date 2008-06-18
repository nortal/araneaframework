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
import org.araneaframework.core.BaseService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockEventfulBaseService extends BaseService {
  private boolean isActionCalled = false;
  private boolean destroyCalled = false;
  private boolean initCalled = false;

  @Override
  public void action(Path path, InputData input, OutputData output) throws Exception {
    isActionCalled = true;
  }
  
  @Override
  public void destroy() {
    destroyCalled = true;
  }
  
  @Override
  public void init() {
    initCalled = true;
  }
  
  public boolean getActionCalled() {
    return isActionCalled;
  }

  public void setActionCalled(boolean isActionCalled) {
    this.isActionCalled = isActionCalled;
  }

  public boolean getDestroyCalled() {
    return destroyCalled;
  }

  public boolean getInitCalled() {
    return initCalled;
  }
}
