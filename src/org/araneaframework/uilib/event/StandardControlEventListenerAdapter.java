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
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.core.EventListener;

/**
 * This class manages "onChange" and "onClick" events of {@link org.araneaframework.uilib.form.Control}s that support
 * those types of events.
 * <p>
 * Extended support for all kinds of event listeners ({@link CustomEventListener}) in Aranea 2.0.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class StandardControlEventListenerAdapter implements EventListener {

  private static final Log LOG = LogFactory.getLog(StandardControlEventListenerAdapter.class);

  private Collection<OnClickEventListener> onClickEventListeners = new ArrayList<OnClickEventListener>();

  private Collection<OnChangeEventListener> onChangeEventListeners = new ArrayList<OnChangeEventListener>();

  private Map<String, Collection<CustomEventListener>> customEventListeners = new TreeMap<String, Collection<CustomEventListener>>();

  /**
   * Adds a {@link OnChangeEventListener}, which is called when the control value is changing.
   * 
   * @param onChangeEventListener {@link OnChangeEventListener}, which is called when the control value is changing.
   */
  public void addOnChangeEventListener(OnChangeEventListener onChangeEventListener) {
    this.onChangeEventListeners.add(onChangeEventListener);
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
   * Adds a <code>CustomEventListener</code>, which is called when the event with given <code>eventId</code> is fired.
   * 
   * @param eventId The name of the event that the given listener should be notified of.
   * @param customEventListener The listener, which is called when the event is fired.
   * @since 2.0
   */
  public void addCustomEventListener(String eventId, CustomEventListener customEventListener) {
    if (!this.customEventListeners.containsKey(eventId)) {
      this.customEventListeners.put(eventId, new LinkedList<CustomEventListener>());
    }
    this.customEventListeners.get(eventId).add(customEventListener);
  }

  /**
   * Removes all registered <code>onChange</code> event listeners.
   */
  public void clearOnChangeEventListeners() {
    this.onChangeEventListeners.clear();
  }

  /**
   * Removes all registered <code>onClick</code> event listeners.
   */
  public void clearOnClickEventListeners() {
    this.onClickEventListeners.clear();
  }

  /**
   * Removes all registered <i>custom</i> event listeners.
   * 
   * @since 2.0
   */
  public void clearCustomEventListeners() {
    this.customEventListeners.clear();
  }

  /**
   * Removes all registered <i>custom</i> event listeners that are registered for <code>eventId</code>.
   * 
   * @param eventId The name of the event for which all listeners will be removed.
   * @since 2.0
   */
  public void clearCustomEventListeners(String eventId) {
    this.customEventListeners.remove(eventId);
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
   * Removes given <code>listener</code> from the registered <code>onClick</code> event listeners.
   * 
   * @param listener An <code>onClick</code> event listener to remove.
   * @since 1.1
   */
  public void removeOnClickEventListener(OnClickEventListener listener) {
    this.onClickEventListeners.remove(listener);
  }

  /**
   * Removes given <code>listener</code> from all of the registered events (where it is registered as a listener).
   * 
   * @param listener The listener to remove.
   * @since 2.0
   */
  public void removeCustomEventListener(CustomEventListener listener) {
    for (String eventId : this.customEventListeners.keySet()) {
      removeCustomEventListener(eventId, listener);
    }
  }

  /**
   * Removes given <code>listener</code> from the registered event listeners of <code>eventId</code>.
   * 
   * @param eventId The name of the event for which the listener will be removed.
   * @param listener The listener to remove.
   * @since 2.0
   */
  public void removeCustomEventListener(String eventId, CustomEventListener listener) {
    if (this.customEventListeners.containsKey(eventId)) {
      Collection<CustomEventListener> listeners = this.customEventListeners.get(eventId);
      if (listeners.remove(listener) && listeners.isEmpty()) {
        this.customEventListeners.remove(eventId); // Remove the empty collection.
      }
    }
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
   * Returns whether there are any {@link OnClickEventListener}s registered.
   * 
   * @return <code>true</code>, if there are any {@link OnClickEventListener}s registered.
   */
  public boolean hasOnClickEventListeners() {
    return !this.onClickEventListeners.isEmpty();
  }

  /**
   * Returns whether there are any {@link CustomEventListener}s registered.
   * 
   * @return <code>true</code>, if there are any {@link CustomEventListener}s registered.
   * @since 2.0
   */
  public boolean hasCustomEventListeners() {
    return !this.customEventListeners.isEmpty();
  }

  /**
   * Returns whether there are any {@link CustomEventListener}s registered for given event ID.
   * 
   * @param eventId The name of the event for which listeners existence will be checked.
   * @return <code>true</code>, if there are any {@link CustomEventListener}s registered for given event ID.
   * @since 2.0
   */
  public boolean hasCustomEventListeners(String eventId) {
    return !this.customEventListeners.containsKey(eventId);
  }

  /**
   * Fires <code>onChange</code>, <code>onClick</code>, and custom event listeners, if they are in the request. When
   * incoming event ID equals to {@value OnChangeEventListener#ON_CHANGE_EVENT} or to
   * {@value OnClickEventListener#ON_CLICK_EVENT} and there are listeners registered for the same event among custom
   * event listeners then the latter ones will also get called.
   * 
   * @param eventId The name of the event for which listeners will be called.
   * @param input The incoming request data..
   */
  public void processEvent(String eventId, InputData input) throws Exception {
    boolean listenersFound = false;

    if (OnChangeEventListener.ON_CHANGE_EVENT.equals(eventId)) {
      for (OnChangeEventListener listener : this.onChangeEventListeners) {
        listenersFound = true;
        listener.onChange();
      }
    } else if (OnClickEventListener.ON_CLICK_EVENT.equals(eventId)) {
      for (OnClickEventListener listener : this.onClickEventListeners) {
        listenersFound = true;
        listener.onClick();
      }
    }

    if (this.customEventListeners.containsKey(eventId)) {
      listenersFound = true;
      for (CustomEventListener listener : this.customEventListeners.get(eventId)) {
        listener.onEvent(eventId);
      }
    }

    if (LOG.isWarnEnabled() && !listenersFound) {
      LOG.warn("Cannot deliver event as no event listeners were registered for the event ID '" + eventId + "'!");
    }
  }

}
