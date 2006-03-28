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

package org.araneaframework.uilib.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.araneaframework.Component;
import org.araneaframework.Widget;
import org.araneaframework.uilib.support.FlowCreator;

/**
 * Aranea Template Application menu(item).
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class MenuItem implements Serializable {
	public static final String MENU_PATH_SEPARATOR = ".";
	
	/** Name for this menu item. */ 
	private String label;

	/** Flow which instance should be started when menuitem is selected. */
	private Class flowClass;
	/** <code>FlowCreator</code> used to create new flow instance started when menuitem is selected. */
	private FlowCreator flowCreator;

	/** Indicates whether the menuitem is in selected state. */
	private boolean selected;
	/** Indicates whether the menuitem serves as holder for others. */
	private boolean holder;

	/** Submenu of this <code>MenuItem</code> (may exist only when this <code>MenuItem</code> is a holder. */
	private Map subMenu;
	private List selectedItems = new ArrayList();

	/***************************************************************************************************
	 * CONSTRUCTORS 
	 ***************************************************************************************************/
	/**
	 * Creates a top menu.
	 */
	public MenuItem() {
		this.label = null;
		this.holder = true;
	}

	/**
	 * Creates a new menuitem that serves as holder to other menu items.
	 * @label label name assigned to created menuitem. May not contain dots.
	 */
	public MenuItem(String label) {
		checkLabelLegality(label);
		this.label = label;
		this.holder = true;
	}

	/**
	 * Creates a new menuitem that starts a new flow (instantiating given class with no-argument constructor) when selected.
	 * @param label name assigned to created menuitem. May not contain dots.
	 * @param flowClass class of flow.
	 */
	public MenuItem(String label, Class flowClass) {
		checkLabelLegality(label);
		this.label = label;
		this.flowClass = flowClass;
	}

	/**
	 * Creates a new menuitem that starts a new flow (instantiating it with specified constructor) when selected.
	 * @param label name assigned to created menuitem. May not contain dots.
	 * @param flowCreator <code>FlowCreator</code> used to create instance of flow to be started.
	 */
	public MenuItem(String label, FlowCreator flowCreator) {
		checkLabelLegality(label);
		this.label = label;
		this.flowCreator = flowCreator;
	}

	/***************************************************************************************************
	 * PRIVATE METHODS. 
	 ***************************************************************************************************/
	private void checkLabelLegality(String label) throws IllegalArgumentException {
		if  (label == null)
			throw new IllegalArgumentException("Menu label may not be null");
		if  (label.indexOf(MENU_PATH_SEPARATOR) != -1)
			throw new IllegalArgumentException("Menu labels may not contain '" + MENU_PATH_SEPARATOR + "'");
	}
	
	/**
	 * Adds a menu item to submenu of this <code>MenuItem</code>.
	 * @param item menu item to add into submenu of this <code>MenuItem</code>.
	 * @throws UnsupportedOperationException if this <code>MenuItem</code> is not a holder.
	 */
	private void addSubMenuItem(MenuItem item) throws UnsupportedOperationException {
		if (!holder)
			throw new UnsupportedOperationException("Menu items can only be added into holders.");
		if (subMenu == null)
			subMenu = new LinkedHashMap();
		subMenu.put(item.getLabel(), item);
	}
	
	/***************************************************************************************************
	 * PUBLIC METHODS. 
	 ***************************************************************************************************/
	/**
	 * Adds a new <code>MenuItem</code> under this <code>MenuItem</code>.
	 * @param item item to add.
	 * @return item added to menu
	 */
	public MenuItem addMenuItem(MenuItem item) {
		return addMenuItem(null, item);
	}

	/**
	 * Adds a new <code>MenuItem</code> under this <code>MenuItem</code> at given path.
	 * @param menuPath
	 * 		path to new menu item.
	 * 		Must be <code>null</code> or empty string for to add <code>MenuItem</code> directly to this <code>MenuItem</code>.
	 * 		Must consist of preceding menu labels separated by dots.
	 * @param item item to add into menu.
	 * @throws NullPointerException if some of the items on specified <code>menuPath</code> do not exist.
	 * @return item added to menu
	 */
	public MenuItem addMenuItem(String menuPath, MenuItem item) {
		String path = menuPath != null ? menuPath : "";
		MenuItem menu = this;

		for (StringTokenizer st = new StringTokenizer(path, MENU_PATH_SEPARATOR); st.hasMoreTokens(); )
			menu = (MenuItem)menu.subMenu.get(st.nextToken());

		menu.addSubMenuItem(item);

		return item;
	}
	
	/**
	 * Changes the selection in menu.
	 * @param menuPath path to select in menu.
	 * @return instance of flow that should be started after activating given selection.
	 * @throws Exception if menuPath is invalid or creation of flow fails.
	 */
	public Widget selectMenuItem(String menuPath) throws Exception {
		clearSelection();

		MenuItem menu = this;
    Widget resultFlow = null;
		
		try {
			for (StringTokenizer st = new StringTokenizer(menuPath, MENU_PATH_SEPARATOR); st.hasMoreTokens(); ) {
				menu = (MenuItem)menu.subMenu.get(st.nextToken());
				menu.setSelected(true);
				selectedItems.add(menu);
			}

			if (menu.flowClass != null)
				resultFlow = (Widget) menu.flowClass.newInstance();
			else if (menu.flowCreator != null)
				resultFlow = menu.flowCreator.createFlow();
		} catch (Exception e) {
			clearSelection();
			throw e;
		}

		return resultFlow;
	}
  
  public void clearSelection() {
    if (!selectedItems.isEmpty()) {
      for (Iterator i = selectedItems.iterator(); i.hasNext(); )
        ((MenuItem)i.next()).setSelected(false);
    }
    selectedItems.clear();
  }
	/***************************************************************************************************
	 * GETTERS. 
	 ***************************************************************************************************/
	public String getLabel() {
		return label;
	}

	public boolean isHolder() {
		return holder;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public Map getSubMenu() {
		return subMenu;
	}

	/***************************************************************************************************
	 * SETTERS. 
	 ***************************************************************************************************/
	public void setSelected(boolean isSelected) {
		selected = isSelected;
	}
}
