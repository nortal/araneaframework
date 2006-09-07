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

function onWindowUnload() {
  closeOpenedPopupWindows();
}

getActiveAraneaPage().addSystemUnLoadEvent(onWindowUnload);

//popup maps
var popups = new Object();

// popup properties, used for all types of popups
var popupProperties = new Object();

// opened windows
var openedPopupWindows = new Object();

function addPopup(popupId, windowProperties, url) {
  popups[popupId] = popupId;
  popupProperties[popupId] = new Object();
  popupProperties[popupId].windowProperties = windowProperties;
  popupProperties[popupId].url = url;
}

function submitThreadCloseRequest(win) {
  if (win.document) {
    var closeParam = createNamedElement("input", "popupClose");
    closeParam.setAttribute("type", "hidden");
    closeParam.setAttribute("value", "true");
    //TODO: find the systemform reliably
    win.document.forms['system_form_0'].appendChild(closeParam);
    win.document.getActiveAraneaPage().submit_6(win.document.system_form_0, null, null, null, null, null);
  }
}

function closeOpenedPopupWindows() {
  for (var popupId in openedPopupWindows) {
    var w = openedPopupWindows[popupId];
    if (w) {
      submitThreadCloseRequest(w);
      w.close();
    }
  }
}

function openPopup(popupId) {
  var w = window.open(popupProperties[popupId].url, popupId, popupProperties[popupId].windowProperties);
  if (w) {
    openedPopupWindows[popupId] = w;
    w.focus();
  }
}

function processPopups() {
  for (var popupId in popups) {
    openPopup(popupId, popupProperties[popupId]);
  }
}

function reloadParentWindow() {
  if (window.opener) {
    var url = window.opener.document.location.href;
    url += (url.indexOf('?') >= 0) ? "&transactionId=override" : "?transactionId=override";
    window.opener.document.location.href=url;
  }
}

function closeWindow(delay) {
  setTimeout('window.close()', delay);
}
