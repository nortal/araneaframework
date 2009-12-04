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

package org.araneaframework.core;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.11
 */
public class ProxyActionListener implements ActionListener {

  protected Widget actionTarget;

  public ProxyActionListener(Widget actionTarget) {
    this.actionTarget = actionTarget;
  }

  public void processAction(String actionId, InputData input, OutputData output) throws Exception {
    String actionParameter = input.getGlobalData().get(ApplicationService.ACTION_PARAMETER_KEY);    
    ProxiedHandlerUtil.invokeActionHandler(actionId, actionParameter, actionTarget);
  }

}
