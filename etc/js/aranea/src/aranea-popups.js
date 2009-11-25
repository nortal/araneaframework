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

var Aranea = window.Aranea || {};

/**
 * Functionality for pop-ups. Depends on "aranea.js" which should be loaded beforehand.
 * @since 1.1
 */
Aranea.Popup = {

	//popup maps <popupId, popupProperties>
	popups: $H(),

	//opened windows
	openedPopupWindows: $H(),

	/* @since 1.1 **/
	addPopup: function(popupId, url, windowProperties) {
		Aranea.Popup.popups.set(popupId, {
			windowProperties: windowProperties,
			url: url
		});
	},

	/* @since 1.1 **/
	openPopup: function(popupId, url, windowProperties) {
		var w = window.open(url, popupId, windowProperties);
		if (w) {
			Aranea.Popup.openedPopupWindows.set(popupId, w);
			w.focus();
		}
	},

	/* @since 1.1 **/
	processPopups: function() {
		Aranea.Popup.popups.each(function(popup) {
			var props = Aranea.Popup.popups.unset(popup.key);
			Aranea.Popup.openPopup(popup.key, props.url, props.windowProperties);
		});
	},

	/* @since 1.1 **/
	submitThreadCloseRequest: function(win) {
		if (win && win.document) {
			var systemForm = $A(win.document.forms).detect(function(form){ return $(form).hasClassName('aranea-form') });
			if (systemForm) {
				systemForm.insert(new Element("input", { "name": "popupClose", "type": "hidden", "value": "true" }));
				win.Aranea.Page.event();
			}
		}
	},

	/* @since 1.1 **/
	closeOpenedPopupWindows: function() {
		Aranea.Popup.openedPopupWindows.each(function(item) {
			if (item.value) {
				Aranea.Popup.submitThreadCloseRequest(item.value);
				item.value.close();
			}
			Aranea.Popup.openedPopupWindows.unset(item.key);
		});
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
		window.setTimeout('window.close()', delay);
	}
};
