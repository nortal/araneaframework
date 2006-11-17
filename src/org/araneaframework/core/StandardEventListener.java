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

import org.araneaframework.InputData;

/**
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 *
 */
public abstract class StandardEventListener implements EventListener {	
	public final void processEvent(Object eventId, InputData input) throws Exception {
		String eventParameter = (String) input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);
		processEvent(eventId, eventParameter, input);
	}
  
	public abstract void processEvent(Object eventId, String eventParam, InputData input) throws Exception;
}
