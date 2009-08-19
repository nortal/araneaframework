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
 */

/**
 * Functionality for popups. Requires "aranea.js" to be executed beforehand.
 * @since 1.1
 */
Aranea.Popups = {

  //popup maps
  popups: {},

  //popup properties, used for all types of popups
  popupProperties: {},

  //opened windows
  openedPopupWindows: {},

  /* @since 1.1 **/
  addPopup: function(popupId, windowProperties, url) {
    this.popups[popupId] = popupId;
    this.popupProperties[popupId] = {};
    this.popupProperties[popupId].windowProperties = windowProperties;
    this.popupProperties[popupId].url = url;
  },

  /* @since 1.1 **/
  submitThreadCloseRequest: function(win) {
    if (win && win.document) {
      var systemForm = null;
      for (var i = 0; i < win.document.forms.length; i++) {
        if (win.document.forms[i].readAttribute('arn-systemForm')) {
          systemForm = win.document.forms[i];
        }
      }
      if (systemForm) {
        systemForm.appendChild(new Element("input",
                { "name": "popupClose", "type": "hidden", "value": "true" }));
        win._ap.event_6(systemForm);
      }
    }
  },

  /* @since 1.1 **/
  closeOpenedPopupWindows: function() {
    for (var popupId in this.openedPopupWindows) {
      var w = this.openedPopupWindows[popupId];
      if (w) {
        this.submitThreadCloseRequest(w);
        w.close();
      }
    }
  },

  /* @since 1.1 **/
  openPopup: function(popupId, popupData) {
    var f = function(id, data) {
      var w = window.open(data.url, id, data.windowProperties);
      if (w) {
        this.openedPopupWindows[id] = w;
        w.focus();
        w = null;
      }
    };
    f.bind(this).defer(popupId, popupData);
  },

  /* @since 1.1 **/
  processPopups: function() {
    for (var popupId in this.popups) {
      this.openPopup(popupId, this.popupProperties[popupId]);
    }
    this.popups = {};
    this.popupProperties = {};
  },

  /* @since 1.1 **/
  applyReturnValue: function(value, elementId) {
    if (window.opener) {
    	window.opener.document.getElementById(elementId).value = value;
    }
  },

  /* @since 1.1 **/
  reloadParentWindow: function(url) {
    if (window.opener) {
      window.opener.document.location.href = url;
    }
  },

  /* @since 1.1 **/
  delayedCloseWindow: function(delay) {
    setTimeout('window.close()', delay);
  }
};

/** @deprecated 
    @useless */
function onWindowUnload() {
  Aranea.Popups.closeOpenedPopupWindows();
}

/**
 * All of the following code is deprecated and dates back to 1.0.
 * Will be removed in 2.0.
 * @deprecated
 * @since 1.0
 */
var addPopup = Aranea.Popups.addPopup;
var submitThreadCloseRequest = Aranea.Popups.submitThreadCloseRequest;
var closeOpenedPopupWindows = Aranea.Popups.closeOpenedPopupWindows;
var openPopup = Aranea.Popups.openPopup;
var processPopups = Aranea.Popups.processPopups;
var applyReturnValue = Aranea.Popups.applyReturnValue;
var reloadParentWindow = Aranea.Popups.reloadParentWindow;
var closeWindow = Aranea.Popups.delayedCloseWindow;

