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

var Aranea = window.Aranea || {};

/**
 * The history object for handling browser-history-based navigation. Refactored in 2.0 to make good use of
 * window.onhashchange event and since it was inevitable.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
Aranea.History = {

	/**
	 * Detects whether we use browser's "onhashchange" event for detecting navigation or we use RSH.
	 */
	SUPPORTS_HASH_CHANGE_EVENT: 'onhashchange' in window,

	/**
	 * The ID of the region that will be updated when the user clicks browser's Back/Forward button.
	 */
	UPDATE_REGION_ID: 'araneaGlobalClientHistoryNavigationUpdateRegion',

	beforeAjaxEvent: function(event) {
		if (Aranea.Page.Submitter.TYPE_SUBMIT != event.memo.type) {
			var stateId = Aranea.History.getCurrentStateIdFromUrl() || Aranea.Data.systemForm.araClientStateId.value;
			if (stateId.startsWith('HTTP')) {
				Aranea.History.lastHttpState = $(Aranea.Page.UPDATE_REGION_ID).innerHTML;
			}
		}
	},

	/**
	 * Reads the state ID from the current URL. The state ID will exist in the URL normally only when we're in the AJAX
	 * page state. Otherwise will be empty. The state ID is expected to match the the hash part of the URL.
	 * 
	 * @return The state ID from the current URL.
	 */
	getCurrentStateIdFromUrl: function() {
		var stateId = window.location.hash || '';
		return stateId.startsWith('#') ? stateId.substring(1) : stateId;
	},

	/**
	 * Initializes Aranea browser-history-based navigation support. Depending on whether the browser supports
	 * window.onhashchange either Aranea.History.initOnHashChange() or Aranea.History.initRSH() will be called. This
	 * function can be called any time when its appropriate (i.e. immediately or when page is loaded), and should be
	 * called only once (when the whole page is loaded, not when AJAX updates the page).
	 */
	init: function() {
		var that = Aranea.History;
		if (that.SUPPORTS_HASH_CHANGE_EVENT) {
			that.initOnHashChange();
		} else {
			that.initRSH();
		}
	},

	/**
	 * Initializes Aranea browser-history-based navigation support using browser's support for the "onhashchange"
	 * event.
	 */
	initOnHashChange: function() {
		var that = Aranea.History;
		window.onhashchange = that.StateChangeListener.wrap(that.OnHashChangeStateChangeListenerWrapper);
		document.observe('aranea:beforeEvent', that.beforeAjaxEvent.wrap(that.requestEventWrapper));
		Aranea.Logger.debug('Initialized support for browser-history-based navigation using window.onhashchange event.');
	},

	/** 
	 * Initializes Aranea browser-history-based navigation support using Really Simple History library
	 * (http://code.google.com/p/reallysimplehistory/). Note that rsh.js must be loaded before this function is called.
	 */
	initRSH: function() {
		var that = Aranea.History, rsh = window.dhtmlHistory, stateIdField = Aranea.Data.systemForm.araClientStateId;

		if (!stateIdField) {
			throw('The system form was expected to have a hidden input named "araClientStateId"!');
		}

		// Initializes history object, overriding default JSON stringifier and default JSON parser.
		rsh.create({
			toJSON:  Object.toJSON,
			fromJSON: function(s) { return s.evalJSON() }
		});
		rsh.initialize(that.StateChangeListener.wrap(that.RshStateChangeListenerWrapper));
		rsh.firstLoad = true;
		rsh.ignoreLocationChange = true;

		var stateId = $F(stateIdField);
		if (stateId.startsWith('HTTP')) {
			rsh.add(stateId, 'HTTP'); // The 2nd parameter is a memo for us that we can use later.
		}

		Aranea.Logger.debug('Initialized support for browser-history-based navigation using ReallySimpleHistory.');
	},

	/**
	 * This is called on page update with AJAX to register the new state.
	 * 
	 * @param stateId The new state ID.
	 */
	registerNewStateAs: function(stateId) {
		var that = Aranea.History;

		that.lastReceivedStateVersion = stateId;

		if (that.SUPPORTS_HASH_CHANGE_EVENT) {
			if (!stateId.startsWith('HTTP')) {
				window.location.hash = stateId; // Note: this will also cause "onhashchange" event.
			}
		} else {
			var memo = stateId.startsWith('HTTP') ? 'HTTP' : 'AJAX';
			window.dhtmlHistory.add(stateVersion, memo); // The 2nd parameter is a memo for us that we can use later.
		}

		var form = Aranea.Data.systemForm;
		if (form && form.araClientStateId) {
			form.araClientStateId.value = stateId;
		}
		form = null;
	},

	/**
	 * The core state change listener. It will be called when the state actually changes, provided with correct previous
	 * and new state IDs.
	 *  
	 * @param previousStateId The ID (never empty) of the previous state.
	 * @param newStateId The ID (never empty) of the new state.
	 */
	StateChangeListener: function(previousStateId, newStateId) {
		Aranea.Logger.debug('History: Detected navigation  "' + previousStateId + '" -> "' + newStateId + '"');

		if (Aranea.Page.isStateValid(newStateId) >= 0) {
		  var form = Aranea.Page.findSystemForm();
		  if (form && form.araClientStateId) {
			form.araClientStateId.value = newStateId;
		  }
		  form = null;

		  // ..a.Page.ajax(eventId, widgetId, [eventParam], [eventCondition], eventUpdateRgns, [form])
		  Aranea.Page.ajax('', '', null, null, Aranea.History.UPDATE_REGION_ID);
		} else {
          Aranea.Logger.debug('History: AJAX request skipped, state "' + newStateId + '" expired.');
		}
	},

	/**
	 * The wrapper for Aranea.History.StateChangeListener that should be used when the browser supports "onhashchange"
	 * event. It takes care of analyzing state change and resolves the new and previous state IDs. When the state
	 * change is verified, the original state change listener (parameter fnListener) will be called.
	 * 
	 * @param fnListener The state change listener to call when a state change occurs.
	 */
	OnHashChangeStateChangeListenerWrapper: function(fnListener) {
		var t = Aranea.History, newStateId = t.getCurrentStateIdFromUrl();
		if (Aranea.Page.isStateValid(newStateId) < 0) {
			return; // not valid state, the message should be shown to end user and appropriate redirect done
		}
		if (newStateId == t.lastReceivedStateVersion) {
			return; // This state was just loaded, therefore not a state change.
		}

		// When the new hash is empty then we have navigated to the page that was initially loaded with HTTP request and
		// that was later updated with AJAX requests. We try to reload the content either from a variable where the
		// content is stored or we do an AJAX request to reload the content.
		if (!newStateId) {
			if (t.lastHttpState) {
				Aranea.Logger.debug('History: Restoring previous state from memory...');
				$(t.UPDATE_REGION_ID).update(t.lastHttpState); // Replace HTML.
				t.lastReceivedStateVersion = newStateId; // Remember the state (empty string) change.
				Aranea.Page.findSystemForm(); // Important to make the updated page change use the correct system-form.
				return; // Return because no need to go server-side.
			} else if (t.lastReceivedStateVersion) {
				newStateId = 'HTTP:' + t.lastReceivedStateVersion;
			} else { // The last state was not stored in a variable. Reload from server.
				newStateId = Aranea.Page.findSystemForm().araClientStateId.value; // Read the state ID from form.
			}
		}

		if (newStateId) {
			Aranea.Logger.debug('History: Restoring previous state using AJAX update...');
			fnListener(t.lastReceivedStateVersion, newStateId);
		} else {
			Aranea.Logger.debug('History: No changes were done during "onhashchange" event because new state is empty!');
		}
	},

	/**
	 * The wrapper for Aranea.History.StateChangeListener that should be used when the Really Simple History library is
	 * used. Helps to integrate Aranea browser history support with RSH.
	 */
	RshStateChangeListenerWrapper: function(fnListener, newStateId) {
		fnListener(Aranea.History.lastReceivedStateVersion, newStateId);

		window.dhtmlHistory.firstLoad = false;
		window.dhtmlHistory.ignoreLocationChange = false;
	}
};

/**
 * Overrides original method to extract state 
 */
Aranea.Page.Submitter.AJAX.ResponseHeaderProcessor = function(transport) {
	var stateVersion = transport.getHeader('Aranea-Application-StateVersion');

	if (stateVersion) {
		var sForm = Aranea.Data.systemForm;
		if (sForm.araClientStateId) {
			sForm.araClientStateId.value = stateVersion;
		}
		Aranea.History.registerNewStateAs(stateVersion);
	}
};

document.observe('aranea:loaded', Aranea.History.init);

