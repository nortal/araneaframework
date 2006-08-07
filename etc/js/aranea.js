function AraneaStore() {
  var objects = new Array();

  this.add = function(object) {
    var len = objects.length;
	objects[len] = object;
  }
  
  this.clear = function() {
  	objects = new Array();
  }
  
  this.length = function() {
    return objects.length;
  }

  this.getContents = function() {
    return objects;
  }
  
  this.forEach = function(f) {
    for(var i = 0; i < objects.length; i++) {
      f(objects[i]);
    }
  }
}

AraneaEventStore.prototype = new AraneaStore();
function AraneaEventStore() {
  var that = this;

  var processEvent = function(event) {
    if (typeof event != "function") {
      event;
    } else {
      event();
    }
  }

  this.execute = function() {
    that.forEach(processEvent);
    that.clear();
  }
}

// AraneaPage object is present on each page served by Aranea and contains common
// functionality for setting page related variables, events and functions.
function AraneaPage() {
  /** Variable that shows if page is active (form has not been submitted yet). */
  var pageActive = true;

  /** Variables holding different (un)load events that should be executed when page loads (body unload or alike) */
  var systemLoadEvents = new AraneaEventStore();
  var clientLoadEvents = new AraneaEventStore();
  var systemUnLoadEvents = new AraneaEventStore();

  loadEvents = new AraneaStore();
  loadEvents.add(systemLoadEvents);
  loadEvents.add(clientLoadEvents);
  
  unloadEvents = new AraneaStore();
  unloadEvents.add(systemUnLoadEvents);

  /** Return whether this page is active (has not been submitted yet). */
  this.isPageActive = function() {
    return pageActive;
  }

  /** Sets page status. */
  this.setPageActive = function(active) {
    if (typeof active == "boolean")
      pageActive = active;
  }

  this.addClientLoadEvent = function(event) {
  	clientLoadEvents.add(event);
  }
  
  this.addSystemLoadEvent = function(event) {
  	systemLoadEvents.add(event);
  } 
  
  this.onload = function() {
    loadEvents.forEach(function(eventHolder) {eventHolder.execute();});
  }

  this.onunload = function() {
    unloadEvents.forEach(function(eventHolder) {eventHolder.execute();});
  }
}

AraneaPage.prototype.submit = function(systemForm, parameters) {
  for (var property in parameters) {
    // let some property provide submit functionality
  	systemForm[property].value=property.value;
  }
}

// new aranea page
_ap = new AraneaPage();