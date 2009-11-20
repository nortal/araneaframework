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

/**
 * Initialization scripts for using Really Simple History (http://code.google.com/p/reallysimplehistory/)
 * functionality in Aranea. Purpose of this is to allow client-side navigation events to proceed normally,
 * supported by server-side state versioning.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

var Aranea = Aranea ? Aranea : {};

/** 
 * RSH event listener, RSH notifies this about the location changes (URL in browser window). 
 * @param newLocation the hash part of the new location URL 
 * @param historyData RSH managed history data for that hash
 */
Aranea.RSHListener = function(newLocation, historyData) {
	Aranea.Logger.debug('Detected navigation event ' + newLocation + " history: " + historyData);

	if (newLocation && !newLocation.startsWith("HTTP")) {
		window.dhtmlHistoryListenerRequestedState = newLocation;
		// event(requestType, eventId, widgetId, [eventParam], [eventCondition], [eventUpdateRgns], [form]
		Aranea.Page.event(null, '', '', null, null, 'araneaGlobalClientHistoryNavigationUpdateRegion');
	}

	window.dhtmlHistory.firstLoad = false;
	window.dhtmlHistory.ignoreLocationChange = false;
};

// Initializes history object, overriding default JSON stringifier and default JSON parser.
window.dhtmlHistory.create({
	toJSON:  Object.toJSON,
	fromJSON: function(s) { return s.evalJSON() }
});

document.observe('aranea:loaded', function() {
	window.dhtmlHistory.initialize();
	window.dhtmlHistory.addListener(Aranea.RSHListener);
});
