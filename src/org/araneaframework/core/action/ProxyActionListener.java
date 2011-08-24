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

package org.araneaframework.core.action;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * Action listener that proxies incoming actions (where this listener is registered) to the provided action target,
 * calling its <code>handleAction*</code> methods. The logic of calling these methods is actually contained in
 * {@link ProxiedHandlerUtil#invokeActionHandler(String, String, Widget)}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.11
 */
public final class ProxyActionListener extends StandardActionListener {

  private final Widget actionTarget;

  /**
   * Creates an action listener that proxies actions to given action target.
   * 
   * @param actionTarget The target widget receiving actions (required).
   */
  public ProxyActionListener(Widget actionTarget) {
    Assert.notNullParam(actionTarget, "actionTarget");
    this.actionTarget = actionTarget;
  }

  @Override
  protected void processAction(String actionId, String actionParam, InputData input, OutputData output) {
    ProxiedHandlerUtil.invokeActionHandler(actionId, actionParam, this.actionTarget);
  }
}
