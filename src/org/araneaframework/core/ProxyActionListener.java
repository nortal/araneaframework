/**
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.core;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.11
 */
public class ProxyActionListener implements ActionListener {
  public static final Log log = LogFactory.getLog(ProxyEventListener.class);
	
  protected Object actionTarget;

  public ProxyActionListener(Object actionTarget) {
    this.actionTarget = actionTarget;
  }

  public void processAction(Object actionId, InputData input, OutputData output) throws Exception {
    String actionParameter = (String) input.getGlobalData().get(ApplicationWidget.ACTION_PARAMETER_KEY);    
    String actionHandlerName = "handleAction" + ((String) actionId).substring(0, 1).toUpperCase() + ((String) actionId).substring(1);

    Method actionHandler;
    // lets try to find a handle method with an empty argument
    try {
      actionHandler = actionTarget.getClass().getMethod(actionHandlerName, new Class[] {});

      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + actionHandlerName + "()' of class '" + actionTarget.getClass().getName() + "'.");
      }
      actionHandler.invoke(actionTarget, new Object[] {});

      return;
    } catch (NoSuchMethodException e) {/*OK*/}

    // lets try to find a method with a String type argument
    try {               
      actionHandler = actionTarget.getClass().getMethod(actionHandlerName, new Class[] { String.class });

      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + actionHandlerName + "(String)' of class '" + actionTarget.getClass().getName() + "'.");
      }
      actionHandler.invoke(actionTarget, new Object[] { actionParameter });                

      return;
    } catch (NoSuchMethodException e) {/*OK*/}

    if (log.isWarnEnabled()) {
      log.warn("Widget '" + ((org.araneaframework.Component)actionTarget).getScope() +
        "' cannot deliver action as no action listeners were registered for the action id '" + actionId + "'!" + Assert.thisToString(actionTarget));
    }
  }

}
