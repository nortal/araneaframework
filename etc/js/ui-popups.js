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

// functions for window close detection, submit event notifying serverside to
// close corresponding thread. Works only with IE and even then, not perfectly.
function onWindowClosingEvent() {
   if (window.event) {
	   if (window.event.clientX < 0 && window.event.clientY < 0) {
			var closeParam = document.createElement("<input type='hidden' name='popupClose' value='true'>");
			document.system_form_0.appendChild(closeParam);
			araneaSubmitEvent(document.system_form_0, "", "", "");
	   }
   }
}

function onWindowUnload() {
	onWindowClosingEvent();
}

addSystemUnloadEvent(onWindowUnload);

//popup maps
var popups = new Object();
var requestArgumentsPrefix = "?topServiceId=application&threadServiceId=";

// popup properties, used for all types of popups
var popupProperties = new Object();

function currentUrl() {
	if (document.system_form_0.action) {
		return document.system_form_0.action;
	}
	else if (system_form_0.action) {
		return system_form_0.action;
	} else {
		alert('Unable to get information about current URL. Requested popups probably cannot be processed.');
	}
}

function addPopup(popupId, properties) {
	popups[popupId] = popupId;
	popupProperties[popupId] = properties;
}

function openPopup(popupId, properties) {
	url = currentUrl() + requestArgumentsPrefix + popupId;
	w = window.open(url, popupId, properties);
	if (w) {
		w.focus();
	}
}

function processPopups() {
	for (popupId in popups) {
		openPopup(popupId, popupProperties[popupId]);
	}
}