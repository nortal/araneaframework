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

package org.araneaframework.uilib.event;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.core.EventListener;

/**
 * This class manages "onChange" and "onClick" events of {@link org.araneaframework.uilib.form.Control}s that support
 * those types of events.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardControlEventListenerAdapter implements EventListener {

  private static final Log LOG = LogFactory.getLog(StandardControlEventListenerAdapter.class);

  private Collection<OnClickEventListener> onClickEventListeners = new ArrayList<OnClickEventListener>();

  private Collection<OnChangeEventListener> onChangeEventListeners = new ArrayList<OnChangeEventListener>();

  /**
   * Adds a {@link OnChangeEventListener}, which is called when the control value is changing.
   * 
   * @param onChangeEventListener {@link OnChangeEventListener}, which is called when the control value is changing.
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    this.onChangeEventListeners.add(onChangeEventListener);
  }

  /**
   * Removes all registered <code>onChange</code> event listeners.
   */
  public void clearOnChangeEventListeners() {
    this.onChangeEventListeners.clear();
  }

  /**
   * Removes given <code>listener</code> from the registered <code>onChange</code> event listeners.
   * 
   * @param listener An <code>onChange</code> event listener to remove.
   * @since 1.1
   */
  public void removeOnChangeEventListener(OnChangeEventListener listener) {
    this.onChangeEventListeners.remove(listener);
  }

  /**
   * Returns whether there are any {@link OnChangeEventListener}s registered.
   * 
   * @return <code>true</code>, if there are any {@link OnChangeEventListener}s registered.
   */
  public boolean hasOnChangeEventListeners() {
    return !this.onChangeEventListeners.isEmpty();
  }

  /**
   * Adds a {@link OnClickEventListener}, which is called when the control is clicked.
   * 
   * @param onClickEventListener {@link OnClickEventListener}, which is called when the control is clicked.
   */
  public void addOnClickEventListener(OnClickEventListener onClickEventListener) {
    this.onClickEventListeners.add(onClickEventListener);
  }

  /**
   * Removes all registered <code>onClick</code> event listeners.
   */
  public void clearOnClickEventListeners() {
    this.onClickEventListeners.clear();
  }

  /**
   * Removes given <code>listener</code> from the registered <code>onClick</code> event listeners.
   * 
   * @param listener An <code>onClick</code> event listener to remove.
   * @since 1.1
   */
  public void removeOnClickEventListener(OnClickEventListener listener) {
    this.onClickEventListeners.remove(listener);
  }

  /**
   * Returns whether there are any {@link OnClickEventListener}s registered.
   * 
   * @return <code>true</code>, if there are any {@link OnClickEventListener}s registered.
   */
  public boolean hasOnClickEventListeners() {
    return !this.onClickEventListeners.isEmpty();
  }

  /**
   * Fires <code>onChange</code> or <code>onClick</code> event listeners, if they are in the request.
   */
  public void processEvent(String eventId, InputData input) throws Exception {
    if (OnChangeEventListener.ON_CHANGE_EVENT.equals(eventId)) {
      for (OnChangeEventListener listener : this.onChangeEventListeners) {
        listener.onChange();
      }
    } else if (OnClickEventListener.ON_CLICK_EVENT.equals(eventId)) {
      for (OnClickEventListener listener : this.onClickEventListeners) {
        listener.onClick();
      }
    } else {
      LOG.warn("Cannot deliver event as no event listeners were registered for the event id '" + eventId + "'!");
    }
  }

}
