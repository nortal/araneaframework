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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.araneaframework.InputData;
import org.araneaframework.core.EventListener;
import org.araneaframework.uilib.InvalidEventException;


/**
 * This class manages all events. It is used by widgets to manage their events.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class StandardControlEventListenerAdapter implements EventListener {

  private Collection onClickEventListeners = new ArrayList();
  private Collection onChangeEventListeners = new ArrayList();

  private Map generalWidgetEventListeners = new HashMap();

  /**
   * Adds a {@link OnChangeEventListener}which is called when the control value is changing.
   * 
   * @param onChangeEventListener {@link OnChangeEventListener}which is called when the control value is changing.
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    onChangeEventListeners.add(onChangeEventListener);
  }

  /**
   * Returns whether there are any {@link OnChangeEventListener}s registered.
   * 
   * @return whether there are any {@link OnChangeEventListener}s registered.
   */
  public boolean hasOnChangeEventListeners() {
    return onChangeEventListeners.size() > 0;
  }

  /**
   * Returns whether there are any {@link GeneralWidgetEventListener}s registered for the given event.
   * 
   * @param eventName widget event.
   * 
   * @return whether there are any {@link GeneralWidgetEventListener}s registered for the given event.
   */
  public boolean hasGeneralWidgetEventListeners(String eventName) {
    List listenerList = ((List) generalWidgetEventListeners.get(eventName));
    return listenerList != null && listenerList.size() > 0;
  }

  /**
   * Adds a {@link OnClickEventListener}which is called when the control is clicked.
   * 
   * @param onClickEventListener {@link OnClickEventListener} which is called when the control is clicked.
   */
  public void addOnClickEventListener(OnClickEventListener onClickEventListener) {
    onClickEventListeners.add(onClickEventListener);
  }

  /**
   * Returns whether there are any {@link OnClickEventListener}s registered.
   * 
   * @return whether there are any {@link OnClickEventListener}s registered.
   */
  public boolean hasOnClickEventListeners() {
    return onClickEventListeners.size() > 0;
  }

  public void processEvent(Object eventId, InputData input) throws Exception {
    if (OnChangeEventListener.ON_CHANGE_EVENT.equals(eventId)) {
      for (Iterator i = onChangeEventListeners.iterator(); i.hasNext();) {
        ((OnChangeEventListener) i.next()).onChange();
      }
    }
    else if (OnClickEventListener.ON_CLICK_EVENT.equals(eventId)) {
      for (Iterator i = onClickEventListeners.iterator(); i.hasNext();) {
        ((OnClickEventListener) i.next()).onClick();
      }
    }
    else {
      throw new InvalidEventException((String) eventId);
    }    
  }
}
