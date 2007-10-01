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

package org.araneaframework.uilib.menu;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.Widget;
import org.araneaframework.http.util.JsonArray;
import org.araneaframework.http.util.JsonObject;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class ContextMenuItem implements Serializable {
	/** Submenu of this <code>MenuItem</code>. */
	private Map subMenu;
	
	/** Name for this menu item. */ 
	private String label;
			
	private ContextMenuEntry menuEntry;

	/**
	 * Creates a top context menu.
	 */
	public ContextMenuItem() {}

	public ContextMenuItem(String label) {
		this.label = label;
	}
	
	public ContextMenuItem(String label, ContextMenuEntry menuEntry) {
		this(label);
		this.menuEntry = menuEntry;
	}
	
	public ContextMenuItem addMenuItem(ContextMenuItem item) {
		if (subMenu == null)
			subMenu = new LinkedMap();
		subMenu.put(item.getLabel(), item);
		return item;
	}

	public String getLabel() {
		return label;
	}
	public Map getSubMenu() {
		return subMenu;
	}
	
	public JsonObject toJSON() {
		JsonObject jsonObject = new JsonObject();
		if (label != null)
			jsonObject.setStringProperty("label", label);
		if (menuEntry != null) {
			jsonObject.setStringProperty("target", menuEntry.getTarget() != null ? menuEntry.getTarget().getScope().toString() : "null");
			jsonObject.setStringProperty("type", menuEntry.getHappeningType());
			jsonObject.setStringProperty("id", menuEntry.getHappeningId().toString());
		} 
		if (subMenu != null) {
			jsonObject.setProperty("submenu", menuMapToJsonArray(subMenu).toString());
		}
		
		return jsonObject;
	}

	protected JsonArray menuMapToJsonArray(Map map) {
		JsonArray result = new JsonArray();
		for (Iterator i = map.values().iterator(); i.hasNext(); ) {
			ContextMenuItem item = (ContextMenuItem) i.next();
			result.append(item.toJSON().toString());
		}

		return result;
	}
	
	public static class ContextMenuEntry implements Serializable {
		public static final String ACTION = "action";
		public static final String EVENT = "event";

		private Widget target = null;
		protected Object happeningId = null;
		protected String happeningType = null;
		
		protected ContextMenuEntry(Object happeningId, String happeningType) {
			this.happeningId = happeningId;
			this.happeningType = happeningType;
		}
		
		protected ContextMenuEntry(Object happeningId, String happeningType, Widget target) {
			this(happeningId, happeningType);
			this.target = target;
		}

		public Object getHappeningId() {
			return happeningId;
		}

		public String getHappeningType() {
			return happeningType;
		}

		public Widget getTarget() {
			return target;
		}
	}
	
	public static class ContextMenuActionEntry extends ContextMenuEntry {
		public ContextMenuActionEntry(Object actionId, Widget target) {
			super(actionId, ACTION, target);
		}
	}

	public static class ContextMenuEventEntry extends ContextMenuEntry {
		public ContextMenuEventEntry(Object eventId, Widget target) {
			super(eventId, EVENT, target);
		}
	}
}
