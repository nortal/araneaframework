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
 * of these may not work on all platforms and browsers. For meaning of all these attributes
 * <link>"http://msdn.microsoft.com/workshop/author/dhtml/reference/methods/open_0.asp"</link> is good reference.
 * 
 * Note that modeless and modal popups (IE proprietary extension) are not really fully-functional browser windows and
 * should only be used for displaying statical content.
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

  public String getChannelmode() {
    return this.propertyMap.get("channelmode");
  }

  public void setChannelmode(String channelmode) {
    this.propertyMap.put("channelmode", channelmode);
  }

  public String getDirectories() {
    return this.propertyMap.get("directories");
  }

  public void setDirectories(String directories) {
    this.propertyMap.put("directories", directories);
  }

  public String getFullscreen() {
    return this.propertyMap.get("fullscreen");
  }

  public void setFullscreen(String fullscreen) {
    this.propertyMap.put("fullscreen", fullscreen);
  }

  public String getHeight() {
    return this.propertyMap.get("height");
  }

  public void setHeight(String height) {
    this.propertyMap.put("height", height);
  }

  public String getLeft() {
    return this.propertyMap.get("left");
  }

  public void setLeft(String left) {
    this.propertyMap.put("left", left);
  }

  public String getLocation() {
    return this.propertyMap.get("location");
  }

  public void setLocation(String location) {
    this.propertyMap.put("location", location);
  }

  public String getMenubar() {
    return this.propertyMap.get("menubar");
  }

  public void setMenubar(String menubar) {
    this.propertyMap.put("menubar", menubar);
  }

  public String getResizable() {
    return this.propertyMap.get("resizable");
  }

  public void setResizable(String resizable) {
    this.propertyMap.put("resizable", resizable);
  }

  /**
   * @return whether the dialog window displays scrollbars
   */
  public String getScrollbars() {
    if (isModal() || isModeless()) {
      return this.propertyMap.get("scroll");
    }
    return this.propertyMap.get("scrollbars");
  }

  /**
   * Specifies whether the dialog window displays scrollbars. Sets property "scroll", if window is modal or modeless,
   * otherwise sets property "scrollbars".
   * 
   * @param scrollbars ("yes" | "no" | 0 | 1 | "on" | "off")
   */
  public void setScrollbars(String scrollbars) {
    if (isModal() || isModeless()) {
      this.propertyMap.put("scroll", scrollbars);
    } else {
      this.propertyMap.put("scrollbars", scrollbars);
    }
  }

  public String getStatus() {
    return this.propertyMap.get("status");
  }

  public void setStatus(String status) {
    this.propertyMap.put("status", status);
  }

  public String getTitlebar() {
    return this.propertyMap.get("titlebar");
  }

  public void setTitlebar(String titlebar) {
    this.propertyMap.put("titlebar", titlebar);
  }

  public String getToolbar() {
    return this.propertyMap.get("toolbar");
  }

  public void setToolbar(String toolbar) {
    this.propertyMap.put("toolbar", toolbar);
  }

  public String getTop() {
    return this.propertyMap.get("top");
  }

  public void setTop(String top) {
    this.propertyMap.put("top", top);
  }

  public String getWidth() {
    return this.propertyMap.get("width");
  }

  public void setWidth(String width) {
    this.propertyMap.put("width", width);
  }

  /**
   * Center property specifies whether to center the dialog window within the desktop. The default is yes. Applicable to
   * modal and modeless popups.
   */
  public String getCenter() {
    return this.propertyMap.get("center");
  }

  public void setCenter(String center) {
    this.propertyMap.put("center", center);
  }

  /**
   * DialogHide specifies whether the dialog window is hidden when printing or using print preview. This feature is only
   * available when a dialog box is opened from a trusted application. The default is no. Applicable to modal and
   * modeless popups.
   */
  public String getDialogHide() {
    return this.propertyMap.get("dialogHide");
  }

  public void setDialogHide(String dialogHide) {
    this.propertyMap.put("dialogHide", dialogHide);
  }

  /**
   * Edge specifies the edge style of the dialog window, default is "raised". Applicable to modal and modeless popups.
   */
  public String getEdge() {
    return this.propertyMap.get("edge");
  }

  /**
   * Specifies the edge style of the dialog window - valid values are "raised" and "sunken". Default is "raised".
   * Applicable to modal and modeless popups.
   */
  public void setEdge(String edge) {
    this.propertyMap.put("edge", edge);
  }

  /**
   * Specifies whether the dialog window displays the context-sensitive Help icon. The default is yes. Applicable to
   * modal and modeless popups.
   */
  public String getHelp() {
    return this.propertyMap.get("help");
  }

  /**
   * Specifies whether the dialog window displays the context-sensitive Help icon. The default is yes. Applicable to
   * modal and modeless popups.
   */
  public void setHelp(String help) {
    this.propertyMap.put("help", help);
  }

  /**
   * Specifies whether the dialog window displays the border window chrome. This feature is only available when a dialog
   * box is opened from a trusted application. The default is no. Applicable to modal and modeless popups.
   */
  public String getUnadorned() {
    return this.propertyMap.get("unadorned");
  }

  /**
   * Specifies whether the dialog window displays the border window chrome. This feature is only available when a dialog
   * box is opened from a trusted application. The default is no. Applicable to modal and modeless popups.
   */
  public void setUnadorned(String unadorned) {
    this.propertyMap.put("unadorned", unadorned);
  }

  public String getDialogHeight() {
    return this.propertyMap.get("dialogHeight");
  }

  /**
   * Sets the height of the dialog window (see Remarks for default unit of measure). Applicable to modal and modeless
   * popups.
   * 
   * @param dialogHeight
   */
  public void setDialogHeight(String dialogHeight) {
    this.propertyMap.put("dialogHeight", dialogHeight);
  }

  public String getDialogWidth() {
    return this.propertyMap.get("dialogWidth");
  }

  /**
   * Sets the width of the dialog window (see Remarks for default unit of measure). Applicable to modal and modeless
   * popups.
   * 
   * @param dialogWidth
   */
  public void setDialogWidth(String dialogWidth) {
    this.propertyMap.put("dialogWidth", dialogWidth);
  }

  public String getDialogLeft() {
    return this.propertyMap.get("dialogLeft");
  }

  /**
   * Sets the left position of the dialog window relative to the upper-left corner of the desktop. Applicable to modal
   * and modeless popups.
   * 
   * @param dialogLeft
   */
  public void setDialogLeft(String dialogLeft) {
    this.propertyMap.put("dialogLeft", dialogLeft);
  }

  public String getDialogTop() {
    return this.propertyMap.get("dialogTop");
  }

  /**
   * Sets the left position of the dialog window relative to the upper-left corner of the desktop. Applicable to modal
   * and modeless popups.
   * 
   * @param dialogTop
   */
  public void setDialogTop(String dialogTop) {
    this.propertyMap.put("dialogTop", dialogTop);
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("");

    for (Iterator<String> i = this.propertyMap.keySet().iterator(); i.hasNext();) {
      String key = i.next();
      sb.append(key);
      sb.append((this.modal || this.modeless) ? ':' : '=');
      sb.append(this.propertyMap.get(key).toString());
      if (i.hasNext()) {
        sb.append((this.modal || this.modeless) ? "; " : ",");
      }
    }

    return sb.toString();
  }
}
