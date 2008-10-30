/**
 * Copyright 2008 Webmedia Group Ltd.
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

var Aranea = Aranea ? Aranea : {};
/** @since 1.1 */
Aranea.Popups = {};

/** @deprecated 
    @useless */
function onWindowUnload() {
  Aranea.Popups.closeOpenedPopupWindows();
}

//popup maps
Aranea.Popups.popups = new Object();

// popup properties, used for all types of popups
Aranea.Popups.popupProperties = new Object();

// opened windows
Aranea.Popups.openedPopupWindows = new Object();

/* @since 1.1 **/
Aranea.Popups.addPopup = function(popupId, windowProperties, url) {
  Aranea.Popups.popups[popupId] = popupId;
  Aranea.Popups.popupProperties[popupId] = new Object();
  Aranea.Popups.popupProperties[popupId].windowProperties = windowProperties;
  Aranea.Popups.popupProperties[popupId].url = url;
};

/* @deprecated
   @since 1.0 **/
var addPopup = Aranea.Popups.addPopup;

/* @since 1.1 **/
Aranea.Popups.submitThreadCloseRequest = function(win) {
  if (win && win.document) {
    var systemForm = null;
    for (var i = 0; i < win.document.forms.length; i++) {
      if (win.document.forms[i].getAttribute('arn-systemForm')) {
	    systemForm = win.document.forms[i];
      }
  	}
    if (systemForm) {
      var closeParam = createNamedElement("input", "popupClose");
      closeParam.setAttribute("type", "hidden");
      closeParam.setAttribute("value", "true");
      systemForm.appendChild(closeParam);
      win.araneaPage().event_6(systemForm, null, null, null, null, null);
    }
  }
};

/* @deprecated
   @since 1.0 **/
var submitThreadCloseRequest = Aranea.Popups.submitThreadCloseRequest;

/* @since 1.1 **/
Aranea.Popups.closeOpenedPopupWindows = function() {
  for (var popupId in Aranea.Popups.openedPopupWindows) {
    var w = Aranea.Popups.openedPopupWindows[popupId];
    if (w) {
      Aranea.Popups.submitThreadCloseRequest(w);
      w.close();
    }
  }
};

/* @deprecated
   @since 1.0 **/
var closeOpenedPopupWindows = Aranea.Popups.closeOpenedPopupWindows;

/* @since 1.1 **/
Aranea.Popups.openPopup = function(popupId) {
  var w = window.open(Aranea.Popups.popupProperties[popupId].url, popupId, Aranea.Popups.popupProperties[popupId].windowProperties);
  if (w) {
    Aranea.Popups.openedPopupWindows[popupId] = w;
    w.focus();
  }
};
var openPopup = Aranea.Popups.openPopup;

/* @since 1.1 **/
Aranea.Popups.processPopups = function() {
  for (var popupId in Aranea.Popups.popups) {
    Aranea.Popups.openPopup(popupId, Aranea.Popups.popupProperties[popupId]);
  }
  Aranea.Popups.popups = new Object();
  Aranea.Popups.popupProperties = new Object();
};

/* @deprecated
   @since 1.0 **/
var processPopups = Aranea.Popups.processPopups;

/* @since 1.1 **/
Aranea.Popups.applyReturnValue = function(value, elementId) {
  if (window.opener) {
  	window.opener.document.getElementById(elementId).value = value;
  }
};

/* @deprecated
   @since 1.0 **/
var applyReturnValue = Aranea.Popups.applyReturnValue;

/* @since 1.1 **/
Aranea.Popups.reloadParentWindow = function(url) {
  if (window.opener) {
    window.opener.document.location.href=url;
  }
};

/** @deprecated
    @since 1.0 */
var reloadParentWindow = Aranea.Popups.reloadParentWindow;

/* @since 1.1 **/
Aranea.Popups.delayedCloseWindow = function(delay) {
  setTimeout('window.close()', delay);
};

/* @deprecated
   @since 1.0 **/
var closeWindow = Aranea.Popups.delayedCloseWindow; 

window['aranea-popups.js'] = true;