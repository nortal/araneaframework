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
import org.araneaframework.uilib.InvalidEventException;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class ProxyEventListener implements EventListener {
	public static final Logger log = Logger.getLogger(ProxyEventListener.class);
	
  protected Object eventTarget;

  public ProxyEventListener(Object eventTarget) {
    this.eventTarget = eventTarget;
  }

  public void processEvent(Object eventId, InputData input) throws Exception {
    String eventParameter = (String) input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);    
    String eventHandlerName = "handleEvent" + ((String) eventId).substring(0, 1).toUpperCase() + ((String) eventId).substring(1);
    
    try {
      Method eventHandler;
      // lets try to find a handle method with an empty argument
      try {               
	      eventHandler = eventTarget.getClass().getMethod(eventHandlerName, new Class[] {});
        
        log.debug("Calling method '" + eventHandlerName + "()' of class '" + eventTarget.getClass().getName() + "'.");       
	      eventHandler.invoke(eventTarget, new Object[] {});
                      
	      return;
      } catch (NoSuchMethodException e) {/*OK*/}
      
      // lets try to find a method with a String type argument
      try {               
        eventHandler = eventTarget.getClass().getMethod(eventHandlerName, new Class[] { String.class });
        
        log.debug("Calling method '" + eventHandlerName + "(String)' of class '" + eventTarget.getClass().getName() + "'.");       
        eventHandler.invoke(eventTarget, new Object[] { eventParameter });                
        
        return;
      } catch (NoSuchMethodException e) {/*OK*/}
      
      log.warn("No listener method found", new NoSuchEventListenerException(eventId));
    }
    catch (Exception e) {
      throw new InvalidEventException((String) eventId, e);
    }     
  }
}
