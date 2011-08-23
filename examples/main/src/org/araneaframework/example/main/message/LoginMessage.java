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

package org.araneaframework.example.main.message;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.core.message.BroadcastMessage;
import org.araneaframework.example.main.web.LoginWidget;
import org.araneaframework.example.main.web.RootWidget;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * A message that searches the {@link LoginWidget} to start a new root widget.
 */
public class LoginMessage extends BroadcastMessage {

  /**
   * Searches the {@link LoginWidget} to start a new root widget.
   */
  @Override
  protected void execute(Component component) throws Exception {
    if (component instanceof LoginWidget) {
      LoginWidget loginWidget = (LoginWidget) component;
      Environment childEnv = loginWidget.getChildEnvironment();
      EnvironmentUtil.requireFlowContext(childEnv).replace(new RootWidget());
    }
  }

}
