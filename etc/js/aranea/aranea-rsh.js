/**
 * Initialization scripts for using Really Simple History (http://code.google.com/p/reallysimplehistory/)
 * functionality in Aranea. Purpose of this is to allow client-side navigation events to proceed normally,
 * supported by server-side state versioning.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */

/** Initializes history object, overriding default JSON stringifier and default JSON parser. */
window.dhtmlHistory.create({
  toJSON: function(o) {
    return Object.toJSON(o);
  }, 
  fromJSON: function(s) {
    return s.evalJSON();
  }
});

var initFunc = function() {
  var yourListener = function(newLocation, historyData) {
    araneaPage().debug('detected navigation event ' + newLocation + " history: " + historyData);

    if (newLocation && (!dhtmlHistory.isFirstLoad() || !dhtmlHistory.ignoreLocationChange) && historyData) {
      window.dhtmlHistoryListenerRequestedState = newLocation;
      // this.event_6 = function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions)
      araneaPage().event_6(araneaPage().getSystemForm(), null, null, null, null, 'globalBackRegion');
    }

    dhtmlHistory.firstLoad = false;
    dhtmlHistory.ignoreLocationChange = false;
  };

  dhtmlHistory.initialize();
  dhtmlHistory.addListener(yourListener);
}; 

araneaPage().addSystemLoadEvent(initFunc);
