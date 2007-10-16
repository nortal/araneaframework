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

package org.araneaframework.core;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ProxyEventListener implements EventListener {
  /** @since 1.0.12 */
  private static final Class [] STRING_CLASS_ARRAY = new Class[] { String.class };
  public static final Logger log = Logger.getLogger(ProxyEventListener.class);
	
  protected Object eventTarget;

  public ProxyEventListener(Object eventTarget) {
    this.eventTarget = eventTarget;
  }

  public void processEvent(Object eventId, InputData input) throws Exception {
    String eventParameter = (String) input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);    
    
    Method eventHandler;
    // lets try to find a handle method with an empty argument
    try {               
      eventHandler = ProxiedHandlerUtil.getEventHandler((String)eventId, eventTarget);
      eventHandler.invoke(eventTarget, new Object[] {});

      return;
    } catch (NoSuchMethodException e) {/*OK*/}
    
    // lets try to find a method with a String type argument
    try {               
      eventHandler = ProxiedHandlerUtil.getEventHandler((String)eventId, eventTarget, STRING_CLASS_ARRAY ); 
      eventHandler.invoke(eventTarget, new Object[] { eventParameter });                
      
      return;
    } catch (NoSuchMethodException e) {/*OK*/}
    
    log.warn("Widget '" + input.getScope() +
        "' cannot deliver event as no event listeners were registered for the event id '" + eventId + "'!" + Assert.thisToString(eventTarget)); 
  }
}
