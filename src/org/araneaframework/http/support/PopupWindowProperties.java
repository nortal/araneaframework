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

package org.araneaframework.http.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple class including the getters/setters for properties that can be set when opening browser windows. Note that all
 * of these may not work on all platforms and browsers. For the meaning of all these attributes
 * <a href="https://developer.mozilla.org/En/DOM/Window.open">here</a> is good reference.
 * <p>
 * Note that modeless and modal popups (IE proprietary extension) are not really fully-functional browser windows and
 * should only be used for displaying static content. Check the browser DOM reference for more precise information which
 * properties and how they are supported in browsers.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

public class PopupWindowProperties implements Serializable {

  private Map<String, String> propertyMap = new HashMap<String, String>();

  private boolean modal;

  private boolean modeless;

  public boolean isModal() {
    return this.modal;
  }

  public boolean isModeless() {
    return this.modeless;
  }

  public void setModal(boolean b) {
    this.modal = b;
    if (b) {
      this.modeless = false;
    }
  }

  public void setModeless(boolean b) {
    this.modeless = b;
    if (b) {
      this.modal = false;
    }
  }

  /**
   * A global way to specify popup window properties that are not defined as properties of this class.
   * 
   * @param property The name of the popup window property.
   * @param value The value for the popup window property. If value is <code>null</code>, the property will be removed.
   */
  public void set(String property, String value) {
    if (value == null) {
      this.propertyMap.remove(property);
    } else {
      this.propertyMap.put(property, value);
    }
  }

  /**
   * A global way to get a value of a popup window property. If the value is <code>null</code> then the property is not
   * included in the output of the {@link #toString()} method.
   * 
   * @param property The name of the popup window property.
   */
  public String get(String property) {
    return this.propertyMap.get(property);
  }

  public String getChannelmode() {
    return get("channelmode");
  }

  public void setChannelmode(String channelmode) {
    set("channelmode", channelmode);
  }

  public String getDirectories() {
    return get("directories");
  }

  public void setDirectories(String directories) {
    set("directories", directories);
  }

  public String getFullscreen() {
    return get("fullscreen");
  }

  public void setFullscreen(String fullscreen) {
    set("fullscreen", fullscreen);
  }

  public String getHeight() {
    return get("height");
  }

  public void setHeight(String height) {
    set("height", height);
  }

  public String getLeft() {
    return get("left");
  }

  public void setLeft(String left) {
    set("left", left);
  }

  public String getLocation() {
    return get("location");
  }

  public void setLocation(String location) {
    set("location", location);
  }

  public String getMenubar() {
    return get("menubar");
  }

  public void setMenubar(String menubar) {
    set("menubar", menubar);
  }

  public String getResizable() {
    return get("resizable");
  }

  public void setResizable(String resizable) {
    set("resizable", resizable);
  }

  /**
   * @return whether the dialog window displays scrollbars
   */
  public String getScrollbars() {
    if (isModal() || isModeless()) {
      return get("scroll");
    }
    return get("scrollbars");
  }

  /**
   * Specifies whether the dialog window displays scrollbars. Sets property "scroll", if window is modal or modeless,
   * otherwise sets property "scrollbars".
   * 
   * @param scrollbars ("yes" | "no" | 0 | 1 | "on" | "off")
   */
  public void setScrollbars(String scrollbars) {
    if (isModal() || isModeless()) {
      set("scroll", scrollbars);
    } else {
      set("scrollbars", scrollbars);
    }
  }

  public String getStatus() {
    return get("status");
  }

  public void setStatus(String status) {
    set("status", status);
  }

  public String getTitlebar() {
    return get("titlebar");
  }

  public void setTitlebar(String titlebar) {
    set("titlebar", titlebar);
  }

  public String getToolbar() {
    return get("toolbar");
  }

  public void setToolbar(String toolbar) {
    set("toolbar", toolbar);
  }

  public String getTop() {
    return get("top");
  }

  public void setTop(String top) {
    set("top", top);
  }

  public String getWidth() {
    return get("width");
  }

  public void setWidth(String width) {
    set("width", width);
  }

  /**
   * Center property specifies whether to center the dialog window within the desktop. The default is yes. Applicable to
   * modal and modeless popups.
   */
  public String getCenter() {
    return get("center");
  }

  public void setCenter(String center) {
    set("center", center);
  }

  /**
   * DialogHide specifies whether the dialog window is hidden when printing or using print preview. This feature is only
   * available when a dialog box is opened from a trusted application. The default is no. Applicable to modal and
   * modeless popups.
   */
  public String getDialogHide() {
    return get("dialogHide");
  }

  public void setDialogHide(String dialogHide) {
    set("dialogHide", dialogHide);
  }

  /**
   * Edge specifies the edge style of the dialog window, default is "raised". Applicable to modal and modeless popups.
   */
  public String getEdge() {
    return get("edge");
  }

  /**
   * Specifies the edge style of the dialog window - valid values are "raised" and "sunken". Default is "raised".
   * Applicable to modal and modeless popups.
   */
  public void setEdge(String edge) {
    set("edge", edge);
  }

  /**
   * Specifies whether the dialog window displays the context-sensitive Help icon. The default is yes. Applicable to
   * modal and modeless popups.
   */
  public String getHelp() {
    return get("help");
  }

  /**
   * Specifies whether the dialog window displays the context-sensitive Help icon. The default is yes. Applicable to
   * modal and modeless popups.
   */
  public void setHelp(String help) {
    set("help", help);
  }

  /**
   * Specifies whether the dialog window displays the border window chrome. This feature is only available when a dialog
   * box is opened from a trusted application. The default is no. Applicable to modal and modeless popups.
   */
  public String getUnadorned() {
    return get("unadorned");
  }

  /**
   * Specifies whether the dialog window displays the border window chrome. This feature is only available when a dialog
   * box is opened from a trusted application. The default is no. Applicable to modal and modeless popups.
   */
  public void setUnadorned(String unadorned) {
    set("unadorned", unadorned);
  }

  public String getDialogHeight() {
    return get("dialogHeight");
  }

  /**
   * Sets the height of the dialog window (see Remarks for default unit of measure). Applicable to modal and modeless
   * popups.
   * 
   * @param dialogHeight
   */
  public void setDialogHeight(String dialogHeight) {
    set("dialogHeight", dialogHeight);
  }

  public String getDialogWidth() {
    return get("dialogWidth");
  }

  /**
   * Sets the width of the dialog window (see Remarks for default unit of measure). Applicable to modal and modeless
   * popups.
   * 
   * @param dialogWidth
   */
  public void setDialogWidth(String dialogWidth) {
    set("dialogWidth", dialogWidth);
  }

  public String getDialogLeft() {
    return get("dialogLeft");
  }

  /**
   * Sets the left position of the dialog window relative to the upper-left corner of the desktop. Applicable to modal
   * and modeless popups.
   * 
   * @param dialogLeft
   */
  public void setDialogLeft(String dialogLeft) {
    set("dialogLeft", dialogLeft);
  }

  public String getDialogTop() {
    return get("dialogTop");
  }

  /**
   * Sets the left position of the dialog window relative to the upper-left corner of the desktop. Applicable to modal
   * and modeless popups.
   * 
   * @param dialogTop
   */
  public void setDialogTop(String dialogTop) {
    set("dialogTop", dialogTop);
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();

    for (Iterator<String> i = this.propertyMap.keySet().iterator(); i.hasNext();) {
      String key = i.next();
      sb.append(key);
      sb.append((this.modal || this.modeless) ? ':' : '=');
      sb.append(get(key));
      if (i.hasNext()) {
        sb.append((this.modal || this.modeless) ? "; " : ",");
      }
    }

    return sb.toString();
  }
}
