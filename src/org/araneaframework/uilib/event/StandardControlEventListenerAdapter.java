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

package org.araneaframework.uilib.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.core.EventListener;


/**
 * This class manages all events. It is used by widgets to manage their events.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class StandardControlEventListenerAdapter implements EventListener {
  
  private static final Log log = LogFactory.getLog(StandardControlEventListenerAdapter.class);

  private Collection onClickEventListeners;
  private Collection onChangeEventListeners;
  
  /**
   * Adds a {@link OnChangeEventListener}which is called when the control value is changing.
   * 
   * @param onChangeEventListener {@link OnChangeEventListener}which is called when the control value is changing.
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    if (onChangeEventListeners == null)
      onChangeEventListeners = new ArrayList();
    onChangeEventListeners.add(onChangeEventListener);
  }
  
  public void clearOnChangeEventListeners() {
    if (onChangeEventListeners != null)
      onChangeEventListeners.clear();
  }

  /**
   * Returns whether there are any {@link OnChangeEventListener}s registered.
   * 
   * @return whether there are any {@link OnChangeEventListener}s registered.
   */
  public boolean hasOnChangeEventListeners() {
    return onChangeEventListeners != null && onChangeEventListeners.size() > 0;
  }

  /**
   * Adds a {@link OnClickEventListener}which is called when the control is clicked.
   * 
   * @param onClickEventListener {@link OnClickEventListener} which is called when the control is clicked.
   */
  public void addOnClickEventListener(OnClickEventListener onClickEventListener) {
    if (onClickEventListeners == null)
      onClickEventListeners = new ArrayList();
    onClickEventListeners.add(onClickEventListener);
  }
  
  public void clearOnClickEventListeners() {
    if (onClickEventListeners != null)
      onClickEventListeners.clear();
  }

  /**
   * Returns whether there are any {@link OnClickEventListener}s registered.
   * 
   * @return whether there are any {@link OnClickEventListener}s registered.
   */
  public boolean hasOnClickEventListeners() {
    return onClickEventListeners != null && onClickEventListeners.size() > 0;
  }

  public void processEvent(Object eventId, InputData input) throws Exception {
    if (OnChangeEventListener.ON_CHANGE_EVENT.equals(eventId)) {
      if (onChangeEventListeners != null)
        for (Iterator i = onChangeEventListeners.iterator(); i.hasNext();) {
          ((OnChangeEventListener) i.next()).onChange();
        }
    }
    else if (OnClickEventListener.ON_CLICK_EVENT.equals(eventId)) {
      if (onClickEventListeners != null)
        for (Iterator i = onClickEventListeners.iterator(); i.hasNext();) {
          ((OnClickEventListener) i.next()).onClick();
        }
    }
    else {
      log.warn("Cannot deliver event as no event listeners were registered for the event id '" + eventId + "'!"); 
    }    
  }
}
