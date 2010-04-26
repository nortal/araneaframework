/*
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
 */

package org.araneaframework.uilib.menu;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.araneaframework.Component;
import org.araneaframework.Scope;
import org.araneaframework.Widget;
import org.araneaframework.http.util.JsonArray;
import org.araneaframework.http.util.JsonObject;

/**
 * Represents web application context menu (right-click menu) hierarchy.
 * 
 * @see org.araneaframework.uilib.menu.ContextMenuWidget
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class ContextMenuItem implements Serializable {

  /**
   * Sub-menu of this <code>MenuItem</code>.
   */
  private Map<String, ContextMenuItem> subMenu;

  /**
   * Name for this menu item.
   */
  private String label;

  private ContextMenuEntry menuEntry;

  /**
   * Creates a top-level context menu which has no <code>label</code> and does not represent any menu entry.
   */
  public ContextMenuItem() {}

  /**
   * Creates a context menu item which has just <code>label</code>.
   */
  public ContextMenuItem(String label) {
    this.label = label;
  }

  /**
   * Creates context menu with both <code>label</code> and associated menu entry.
   */
  public ContextMenuItem(String label, ContextMenuEntry menuEntry) {
    this(label);
    this.menuEntry = menuEntry;
  }

  /**
   * Adds {@link ContextMenuItem} <code>item</code> as the sub-menu of this {@link ContextMenuItem}.
   * 
   * @return this {@link ContextMenuItem}
   */
  public ContextMenuItem addMenuItem(ContextMenuItem item) {
    if (this.subMenu == null) {
      this.subMenu = new LinkedHashMap<String, ContextMenuItem>();
    }
    this.subMenu.put(item.getLabel(), item);
    return item;
  }

  /**
   * Returns <code>label</code> of this {@link ContextMenuItem}.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Returns submenu of this {@link ContextMenuItem} as <code>Map&lt;String, ContextMenuItem&gt;</code>.
   */
  public Map<String, ContextMenuItem> getSubMenu() {
    return this.subMenu;
  }

  /**
   * Returns JSON representation of this context menu hierarchy.
   */
  public JsonObject toJSON() {
    JsonObject jsonObject = new JsonObject();

    if (this.label != null) {
      jsonObject.setStringProperty("label", this.label);
    }

    if (this.menuEntry != null) {
      Scope scope = this.menuEntry.getTarget() != null ? this.menuEntry.getTarget().getScope() : null;
      jsonObject.setStringProperty("target", scope + "");
      jsonObject.setStringProperty("type", this.menuEntry.getEventType());
      jsonObject.setStringProperty("id", this.menuEntry.getEventId().toString());
      jsonObject.setProperty("param", this.menuEntry.getEventParamDetector());
    }

    if (this.subMenu != null) {
      jsonObject.setProperty("submenu", menuMapToJsonArray(this.subMenu).toString());
    }

    return jsonObject;
  }

  protected JsonArray menuMapToJsonArray(Map<String, ContextMenuItem> map) {
    JsonArray result = new JsonArray();
    for (Iterator<ContextMenuItem> i = map.values().iterator(); i.hasNext();) {
      ContextMenuItem item = i.next();
      result.append(item.toJSON().toString());
    }
    return result;
  }

  /**
   * This class describes the menu entry corresponding to some {@link ContextMenuItem}. It contains information about
   * {@link Component} that is notified when entry is selected from menu, the event type (<code>action</code> or
   * <code>event</code>) which should be triggered and event parameter detector. It cannot be instantiated directly,
   * rather its subclasses {@link ContextMenuItem.ContextMenuActionEntry} or
   * {@link ContextMenuItem.ContextMenuEventEntry} should be used.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.1
   */
  public static class ContextMenuEntry implements Serializable {

    /**
     * Default event parameter detector (leaves event parameter undefined).
     */
    public static final String NULL_EVENT_PARAM_DETECTOR = "function() { return null; }";

    /**
     * Constant corresponding to Aranea <code>action</code> triggering by this <code>ContextMenuEntry</code>.
     */
    public static final String ACTION = "action";

    /**
     * Constant corresponding to Aranea <code>event</code> triggering by this <code>ContextMenuEntry</code>.
     */
    public static final String EVENT = "event";

    private Widget target;

    protected Object eventId;

    protected String eventType;

    protected String eventParamDetector = NULL_EVENT_PARAM_DETECTOR;

    protected ContextMenuEntry(Object eventId, String eventType) {
      this.eventId = eventId;
      this.eventType = eventType;
    }

    protected ContextMenuEntry(Object eventId, String eventType, Widget target) {
      this(eventId, eventType);
      this.target = target;
    }

    protected ContextMenuEntry(Object eventId, String eventType, Widget target, String eventParamDetector) {
      this(eventId, eventType);
      this.target = target;
      this.eventParamDetector = eventParamDetector;
    }

    /**
     * Returns the event ID generated when entry is selected.
     * 
     * @return The event ID for context menu entry.
     */
    public Object getEventId() {
      return this.eventId;
    }

    /**
     * Returns the event type of this <code>ContextMenuEntry</code>, either
     * {@link ContextMenuItem.ContextMenuEntry#ACTION} or {@link ContextMenuItem.ContextMenuEntry#EVENT}.
     * 
     * @return The request type for this context menu entry.
     */
    public String getEventType() {
      return this.eventType;
    }

    /**
     * Returns {@link Widget} that receives the event when entry is selected.
     * 
     * @return The request target for context menu entry.
     */
    public Widget getTarget() {
      return this.target;
    }

    /**
     * Returns the event parameter detector which detects the event parameter supplied along with event ID. If left
     * unspecified, this returns {@link ContextMenuItem.ContextMenuEntry#NULL_EVENT_PARAM_DETECTOR} which returns null.
     * Otherwise it returns custom Javascript function defined by developer.
     * 
     * @return The request parameter decorator for this context menu entry.
     */
    public String getEventParamDetector() {
      return this.eventParamDetector;
    }
  }

  /**
   * Context menu entry which generates Aranea <code>action</code> when entry is selected.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.1
   */
  public static class ContextMenuActionEntry extends ContextMenuEntry {

    public ContextMenuActionEntry(Object actionId, Widget target) {
      super(actionId, ACTION, target);
    }

    public ContextMenuActionEntry(Object actionId, Widget target, String eventParamDetector) {
      super(actionId, ACTION, target, eventParamDetector);
    }
  }

  /**
   * Context menu entry which generates Aranea <code>event</code> when entry is selected.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   * @since 1.1
   */
  public static class ContextMenuEventEntry extends ContextMenuEntry {

    public ContextMenuEventEntry(Object eventId, Widget target) {
      super(eventId, EVENT, target);
    }

    public ContextMenuEventEntry(Object eventId, Widget target, String eventParamDetector) {
      super(eventId, EVENT, target, eventParamDetector);
    }
  }
}
