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

package org.araneaframework.example.main.message;

import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.example.main.web.LoginWidget;
import org.araneaframework.example.main.web.RootWidget;
import org.araneaframework.framework.FlowContext;

/**
 * A message that searches the {@link LoginWidget} to start a new root widget.
 */
public class LoginMessage extends BroadcastMessage {

  private static final long serialVersionUID = 1L;

  /**
   * Searches the {@link LoginWidget} to start a new root widget.
   */
  protected void execute(Component component) throws Exception {
    if (component instanceof LoginWidget) {
      LoginWidget loginWidget = (LoginWidget) component;

      Environment childEnv = loginWidget.getChildEnvironment();

      FlowContext flow = EnvironmentUtil.requireFlowContext(childEnv);
      flow.replace(new RootWidget(), null);
    }
  }

}
