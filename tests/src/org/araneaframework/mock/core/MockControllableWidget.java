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

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockControllableWidget extends StandardWidget {
  private boolean keepEventBusy = true;

  protected Object getEventId(InputData input) {
    return null;
  }

  public void event(Path path, InputData input) {
    keepEventBusy = true;
    while(keepEventBusy) {
      // work-out
    }
  }
  
  public boolean isKeepEventBusy() {
    return keepEventBusy;
  }

  public void setKeepEventBusy(boolean keepEvent) {
    this.keepEventBusy = keepEvent;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {}
}
