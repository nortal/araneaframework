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

package org.araneaframework.http.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple class including the getters/setters for properties 
 * that can be set when opening browser windows. Note that all of these
 * may not work on all platforms and browsers. For meaning of all these 
 * attributes
 * <link>"http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/open_0.asp"</link>
 * is good reference.
 * 
 * Note that modeless and modal popups (IE proprietary extension) are not really
 * fully-functional browser windows and should only be used for displaying statical content.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

public class PopupWindowProperties implements Serializable {
	private Map propertyMap = new HashMap();
	
	private boolean modal;
	private boolean modeless;
	
	public boolean isModal() {
		return modal;
	}
	
	public boolean isModeless() {
		return modeless;
	}
	
	public void setModal(boolean b) {
		modal = b;
		if (b) modeless = false;
	}
	
	public void setModeless(boolean b) {
		modeless = b;
		if (b) modal = false;
	}

	public String getChannelmode() {
		return (String)propertyMap.get("channelmode");
	}
	public void setChannelmode(String channelmode) {
		propertyMap.put("channelmode", channelmode);
	}
	
	public String getDirectories() {
		return (String)propertyMap.get("directories");
	}
	public void setDirectories(String directories) {
		propertyMap.put("directories", directories);
	}
	
	public String getFullscreen() {
		return (String)propertyMap.get("fullscreen");
	}
	public void setFullscreen(String fullscreen) {
		propertyMap.put("fullscreen", fullscreen);
	}
	
	public String getHeight() {
		return (String)propertyMap.get("height");
	}
	public void setHeight(String height) {
		propertyMap.put("height", height);
	}
	
	public String getLeft() {
		return (String)propertyMap.get("left");
	}
	public void setLeft(String left) {
		propertyMap.put("left", left);
	}
	
	public String getLocation() {
		return (String)propertyMap.get("location");
	}
	public void setLocation(String location) {
		propertyMap.put("location", location);
	}
	
	public String getMenubar() {
		return (String)propertyMap.get("menubar");
	}
	public void setMenubar(String menubar) {
		propertyMap.put("menubar", menubar);
	}
	
	public String getResizable() {
		return (String)propertyMap.get("resizable");
	}

	public void setResizable(String resizable) {
		propertyMap.put("resizable", resizable);
	}

	/**
	 * @return whether the dialog window displays scrollbars
	 */
	public String getScrollbars() {
		if (isModal() || isModeless())
			return (String)propertyMap.get("scroll");
		return (String)propertyMap.get("scrollbars");
	}
	/**
	 * Specifies whether the dialog window displays scrollbars.
	 * Sets property "scroll", if window is modal or modeless,
	 * otherwise sets property "scrollbars".
	 * @param scrollbars ("yes" | "no" | 0 | 1 | "on" | "off")
	 */
	public void setScrollbars(String scrollbars) {
		if (isModal() || isModeless())
			propertyMap.put("scroll", scrollbars);
		else
			propertyMap.put("scrollbars", scrollbars);
	}

	public String getStatus() {
		return (String)propertyMap.get("status");
	}
	public void setStatus(String status) {
		propertyMap.put("status", status);
	}

	public String getTitlebar() {
		return (String)propertyMap.get("titlebar");
	}
	public void setTitlebar(String titlebar) {
		propertyMap.put("titlebar", titlebar);
	}

	public String getToolbar() {
		return (String)propertyMap.get("toolbar");
	}
	public void setToolbar(String toolbar) {
		propertyMap.put("toolbar", toolbar);
	}
	
	public String getTop() {
		return (String)propertyMap.get("top");
	}
	public void setTop(String top) {
		propertyMap.put("top", top);
	}
	
	public String getWidth() {
		return (String)propertyMap.get("width");
	}

	public void setWidth(String width) {
		propertyMap.put("width", width);
	}
	
	/**
	 *  Center property specifies whether to center the dialog window within 
	 *  the desktop. The default is yes. Applicable to modal and modeless popups.
	 */
	public String getCenter() {
		return (String) propertyMap.get("center");
	}
	
	public void setCenter(String center) {
		propertyMap.put("center", center);
	}

	/**
	 *  DialogHide specifies whether the dialog window is hidden when printing or using print preview.
	 *  This feature is only available when a dialog box is opened from a trusted application. 
	 *  The default is no. Applicable to modal and modeless popups.
	 */
	public String getDialogHide() {
		return (String) propertyMap.get("dialogHide");
	}
	
	public void setDialogHide(String dialogHide) {
		propertyMap.put("dialogHide", dialogHide);
	}
	
	
	/**
	 * 	Edge specifies the edge style of the dialog window, default is "raised".
	 *  Applicable to modal and modeless popups.
	 */
	public String getEdge() {
		return (String)propertyMap.get("edge");
	}
	
	/**
	 * Specifies the edge style of the dialog window - valid values are "raised" and "sunken".
	 * Default is "raised". Applicable to modal and modeless popups.
	 */
	public void setEdge(String edge) {
		propertyMap.put("edge", edge);
	}
	
	/**
	 * 	Specifies whether the dialog window displays the context-sensitive Help icon. The default is yes.
	 *  Applicable to modal and modeless popups.
	 */
	public String getHelp() {
		return (String)propertyMap.get("help");
	}
	
	/**
	 * 	Specifies whether the dialog window displays the context-sensitive Help icon. The default is yes.
	 *  Applicable to modal and modeless popups.
	 */
	public void setHelp(String help) {
		propertyMap.put("help", help);
	}

	/**
	 * 	Specifies whether the dialog window displays the border window chrome. 
	 *  This feature is only available when a dialog box is opened from a trusted application. The default is no.
	 *  Applicable to modal and modeless popups.
	 */
	public String getUnadorned() {
		return (String)propertyMap.get("unadorned");
	}
	
	/**
	 * 	Specifies whether the dialog window displays the border window chrome. 
	 *  This feature is only available when a dialog box is opened from a trusted application. The default is no.
	 *  Applicable to modal and modeless popups.
	 */
	public void setUnadorned(String unadorned) {
		propertyMap.put("unadorned", unadorned);
	}
	
	public String getDialogHeight() {
		return (String)propertyMap.get("dialogHeight");
	}
	
	/**
	 * Sets the height of the dialog window (see Remarks for default unit of measure).
	 * Applicable to modal and modeless popups.
	 * @param dialogHeight
	 */
	public void setDialogHeight(String dialogHeight) {
		propertyMap.put("dialogHeight", dialogHeight);
	}
	
	public String getDialogWidth() {
		return (String)propertyMap.get("dialogWidth");
	}
	
	/**
	 * Sets the width of the dialog window (see Remarks for default unit of measure).
	 * Applicable to modal and modeless popups.
	 * @param dialogHeight
	 */
	public void setDialogWidth(String dialogWidth) {
		propertyMap.put("dialogWidth", dialogWidth);
	}
	
	public String getDialogLeft() {
		return (String)propertyMap.get("dialogLeft");
	}
	
	/**
	 * Sets the left position of the dialog window relative to the upper-left corner of the desktop.
	 * Applicable to modal and modeless popups.
	 * @param dialogHeight
	 */
	public void setDialogLeft(String dialogLeft) {
		propertyMap.put("dialogLeft", dialogLeft);
	}
	
	public String getDialogTop() {
		return (String)propertyMap.get("dialogTop");
	}
	
	/**
	 * Sets the left position of the dialog window relative to the upper-left corner of the desktop.
	 * Applicable to modal and modeless popups.
	 * @param dialogHeight
	 */
	public void setDialogTop(String dialogTop) {
		propertyMap.put("dialogTop", dialogTop);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		
		for (Iterator i = propertyMap.keySet().iterator(); i.hasNext(); ) {
			String key = (String) i.next();
			sb.append(key);
			sb.append((modal || modeless) ? ':' : '=');
			sb.append(propertyMap.get(key).toString());
			if (i.hasNext())
				sb.append((modal || modeless) ? "; " : ",");
		}

		return sb.toString();
	}
}
