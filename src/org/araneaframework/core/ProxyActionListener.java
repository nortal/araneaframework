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

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.11
 */
public class ProxyActionListener implements ActionListener {
  public static final Logger log = Logger.getLogger(ProxyActionListener.class);
	
  protected Object actionTarget;

  public ProxyActionListener(Object actionTarget) {
    this.actionTarget = actionTarget;
  }

  public void processAction(Object actionId, InputData input, OutputData output) throws Exception {
    String actionParameter = (String) input.getGlobalData().get(ApplicationWidget.ACTION_PARAMETER_KEY);
    
    Method actionHandler;
    // lets try to find a handle method with an empty argument
    try {
      actionHandler = ProxiedHandlerUtil.getActionHandler((String)actionId, actionTarget);

      if (log.isDebugEnabled()) {
    	String actionHandlerName = "handleAction" + ((String) actionId).substring(0, 1).toUpperCase() + ((String) actionId).substring(1);
        log.debug("Calling method '" + actionHandlerName + "()' of class '" + actionTarget.getClass().getName() + "'.");
      }
      actionHandler.invoke(actionTarget, new Object[] {});

      return;
    } catch (NoSuchMethodException e) {/*OK*/}

    // lets try to find a method with a String type argument
    try {               
      actionHandler = ProxiedHandlerUtil.getActionHandler((String)actionId, actionTarget, new Class[] { String.class }); 

      if (log.isDebugEnabled()) {
    	String actionHandlerName = "handleAction" + ((String) actionId).substring(0, 1).toUpperCase() + ((String) actionId).substring(1);
        log.debug("Calling method '" + actionHandlerName + "(String)' of class '" + actionTarget.getClass().getName() + "'.");
      }
      actionHandler.invoke(actionTarget, new Object[] { actionParameter });                

      return;
    } catch (NoSuchMethodException e) {/*OK*/}

    if (log.isEnabledFor(Priority.WARN)) {
      log.warn("Widget '" + input.getScope() +
        "' cannot deliver action as no action listeners were registered for the action id '" + actionId + "'!" + Assert.thisToString(actionTarget));
    }
  }

}
