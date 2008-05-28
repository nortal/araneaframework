/**
 * Initialization scripts for using Really Simple History (http://code.google.com/p/reallysimplehistory/)
 * functionality in Aranea. Purpose of this is to allow client-side navigation events to proceed normally,
 * supported by server-side state versioning.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

/** 
 * RSH event listener, RSH notifies this about the location changes (URL in browser window). 
 * @param newLocation the hash part of the new location URL 
 * @param historyData RSH managed history data for that hash
 */
AraneaPage.RSHListener = function(newLocation, historyData) {
  araneaPage().debug('detected navigation event ' + newLocation + " history: " + historyData);

  if (newLocation && !newLocation.startsWith("HTTP")) {
    window.dhtmlHistoryListenerRequestedState = newLocation;
    // this.event_6 = function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions)
    araneaPage().event_6(araneaPage().getSystemForm(), null, null, null, null, 'araneaGlobalClientHistoryNavigationUpdateRegion');
  }

  window.dhtmlHistory.firstLoad = false;
  window.dhtmlHistory.ignoreLocationChange = false;
};

/** Initializes history object, overriding default JSON stringifier and default JSON parser. */
window.dhtmlHistory.create({
  toJSON: function(o) {
    return Object.toJSON(o);
  }, 
  fromJSON: function(s) {
    return s.evalJSON();
  }
});

window.dhtmlHistory.initialize(AraneaPage.RSHListener);
